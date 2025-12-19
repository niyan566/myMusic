<template>
    <div style="background-color: #f2f5f8;">
        <!-- 头部导航栏 -->
        <div style="height: 80px;width: 100%;display: flex;background-color:#fff;align-items: center;z-index: 1;box-shadow: 0 1px 4px #0000000d; ">
            <div style="margin-left: 313px;">
                <el-input
                    v-model="data.search"
                    placeholder="歌曲、歌手以及更多内容"
                    prefix-icon="Search"
                />
            </div>
            <div style="margin-left: auto;margin-right: 313px;">
                <el-button v-if="!userStore.isLoggedIn" type="primary" @click="data.DialogVisible = true">登 录</el-button>
                <template v-else>
                    <span style="margin-right: 16px;">欢迎，{{ userStore.user.username }}</span>
                    <el-button type="danger" @click="logout">退出</el-button>
                </template>
            </div>
        </div>

        <!-- 中间部分 -->
        <div style="display: flex;background-color: #f2f5f8;margin: 1px 313px 0 313px; min-height: calc(100vh - 161px); align-items: stretch;">
            <!-- 左侧菜单栏 -->
            <div style="width: 220px;background-color: #fff;border-right: 1px solid #e0e0e0; ">
            <el-menu
                :default-active="$route.path"
                router
            >
                <el-menu-item index="/home/musicLibrary">
                <el-icon><Headset /></el-icon>
                <span>乐库</span>
                </el-menu-item>

                <el-menu-item index="/home/playLists">
                <el-icon><Document /></el-icon>
                <span>歌单</span>
                </el-menu-item>

                <el-menu-item index="/home/likedMusic">
                <el-icon><Star /></el-icon>
                <span>我喜欢的音乐</span>
                </el-menu-item>

                <el-menu-item index="recently">
                <el-icon><Clock /></el-icon>
                <span>最近播放</span>
                </el-menu-item>
            </el-menu>
            </div>
            <!-- 右侧主体部分 -->
            <div style="flex: 1; background-color: white;">
                <RouterView/>
            </div>
        </div>
        
        <!-- 底部播放器 -->
        <div class="player-container">
            <!-- 歌曲信息区 -->
            <div class="song-info">
                <img
                    :src="currentSong.coverPath || 'https://picsum.photos/id/1/50/50'"
                    alt="歌曲封面"
                    class="song-cover"
                />
                <div class="song-meta">
                    <div class="song-title">{{ currentSong.title || '示例歌曲' }}</div>
                    <div class="song-artist">{{ currentSong.singer || '未知歌手' }}</div>
                </div>
            </div>

            <!-- 核心控制区 -->
            <div class="control-area">
                <!-- 控制按钮 -->
                <div class="control-buttons">
                    <img
                        class="control-icon"
                        src="@/assets/img/我喜欢.png"
                        alt="喜欢"
                    />
                    <img
                        class="control-icon"
                        src="@/assets/img/上一首.png"
                        alt="上一首"
                        @click="handlePrev"
                    />
                    <!-- 播放/暂停按钮 -->
                    <img
                        class="play-btn"
                        :src="isPlaying ? playIcon.pause : playIcon.play"
                        alt="播放/暂停"
                        @click="togglePlay"
                    />
                    <img
                        class="control-icon"
                        src="@/assets/img/下一首.png"
                        alt="下一首"
                        @click="handleNext"
                    />
                    <img
                        class="control-icon"
                        src="@/assets/img/循环.png"
                        alt="循环"
                    />
                </div>

                <!-- 细进度条 -->
                <div class="progress-container">
                    <span class="time current-time">{{ formatCurrentTime }}</span>
                    <div class="progress-bar" @click="handleProgressClick">
                        <div class="progress-fill" :style="{ width: progress + '%' }"></div>
                        <div
                            class="progress-handle"
                            :style="{ left: progress + '%' }"
                        ></div>
                    </div>
                    <span class="time total-time">{{ formatTotalTime }}</span>
                </div>
            </div>

            <!-- 音量控制区 -->
            <div class="volume-control">
                <!-- 音量控制可以后续添加 -->
            </div>
        </div>

        <!--登录组件-->
        <div>
             <el-dialog v-model="data.DialogVisible" title="登 录" width="410" @close="closeDialog" center style="padding: 30px;">
                <div style="width: 100%;align-items: center;display: flex;flex-direction: column;">
                    <el-input v-model="data.username" style="width: 100%;height: 40px;margin: 20px 0;" clearable placeholder="用户名" />
                    <el-input v-model="data.password" style="width: 100%;;height: 40px;" show-password clearable placeholder="密码" />
                </div>

                <template #footer>
                <div class="dialog-footer" style="margin-top: 30px;">
                    <el-button @click="data.DialogVisible = false" style="margin-right: 200px;">取消</el-button>
                    <el-button type="primary" @click="login">
                    登录
                    </el-button>
                </div>
                </template>
            </el-dialog>
        </div>
    </div>
</template>

<script setup>
import { computed, reactive } from "vue";
import { usePlayerStore } from '@/stores/player';
import {useUserStore} from '@/stores/user';
import { storeToRefs } from 'pinia';
import { Headset, Document, Star, Clock } from '@element-plus/icons-vue';
import request from "@/utils/request";
import { ElMessage } from "element-plus";

const data = reactive({
    search: '',
    DialogVisible:false,
    username:'',
    password:''
})

const userStore=useUserStore()
const playerStore = usePlayerStore()

const { isPlaying, currentTime, totalTime, currentSong } = storeToRefs(playerStore);

// 播放/暂停图标路径
const playIcon = {
    play: "/src/assets/img/播放.png",
    pause: "/src/assets/img/暂停.png",
};


const closeDialog=()=>{
    data.DialogVisible=false,
    data.username='',
    data.password=''
}

const login=()=>{
    const user={
        username:data.username,
        password:data.password
    }
    request.get('/user/login',{params:user}).then((res)=>{
        if(res.code==='200'){
            ElMessage.success("登录成功")
            userStore.setUserInfo(res.data.token, res.data.user)
            data.DialogVisible=false
            //登录结束刷新
            setTimeout(() => {
                window.location.reload();  // 重新加载页面
            }, 500);
        }else{
            ElMessage.error(res.msg)
        }
    })
}

const logout=()=>{
    userStore.logout()
    ElMessage.success("已退出登录")
    setTimeout(() => {
        window.location.reload();  // 重新加载页面
    }, 500);
}

// 计算进度百分比
const progress = computed(() => {
    if (totalTime.value && currentTime.value) {
        return Math.min(100, Math.max(0, (currentTime.value / totalTime.value) * 100));
    }
    return 0;
});

// 播放/暂停切换
const togglePlay = () => {
    playerStore.togglePlay();
};

// 上一首
const handlePrev = () => {
    playerStore.prevSong();
};

// 下一首
const handleNext = () => {
    playerStore.nextSong();
};

// 点击进度条跳转
const handleProgressClick = (e) => {
    const barWidth = e.currentTarget.offsetWidth;
    const clickPosition = e.offsetX;
    const clickPercent = (clickPosition / barWidth) * 100;

    if (totalTime.value) {
        const targetTime = (clickPercent / 100) * totalTime.value;
        playerStore.seek(targetTime);
    }
};

// 格式化时间
const formatCurrentTime = computed(() => formatTime(currentTime.value));
const formatTotalTime = computed(() => formatTime(totalTime.value));

function formatTime(seconds) {
    if (isNaN(seconds) || seconds < 0) return "00:00";
    
    const min = Math.floor(seconds / 60);
    const sec = Math.floor(seconds % 60);
    return `${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}`;
}

const load=()=>{
    userStore.initUserInfo()
}

load()

</script>

<style scoped>
/* 保持原有的样式不变 */
.player-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 80px;
  background-color: #fff;
  padding: 0 20px;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
  box-sizing: border-box;
}

.song-info {
  display: flex;
  align-items: center;
  width: 280px;
  gap: 10px;
}
.song-cover {
  width: 50px;
  height: 50px;
  border-radius: 4px;
  object-fit: cover;
}
.song-meta {
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.song-title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}
.song-artist {
  font-size: 12px;
  color: #999;
}

.control-area {
  display: flex;
  flex-direction: column-reverse;
  align-items: center;
  justify-content: center;
  flex: 1;
  gap: 10px;
}

.progress-container {
  display: flex;
  align-items: center;
  width: 100%;
  max-width: 600px;
  gap: 10px;
}
.time {
  font-size: 11px;
  color: #999;
  width: 40px;
  text-align: center;
}
.progress-bar {
  flex: 1;
  height: 3px;
  background-color: #e9e9e9;
  border-radius: 3px;
  position: relative;
  cursor: pointer;
}
.progress-fill {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  background: linear-gradient(90deg, #3b82f6, #8b5cf6);
  border-radius: 3px;
  transition: width 0.1s linear;
}
.progress-handle {
  position: absolute;
  top: 50%;
  transform: translate(-50%, -50%);
  width: 12px;
  height: 12px;
  background-color: #3b82f6;
  border-radius: 50%;
  box-shadow: 0 0 5px rgba(59, 130, 246, 0.5);
  opacity: 0;
  transition: opacity 0.2s ease;
}
.progress-bar:hover .progress-handle {
  opacity: 1;
}

.control-buttons {
  display: flex;
  align-items: center;
  gap: 15px;
}
.control-icon {
  width: 24px;
  height: 24px;
  cursor: pointer;
}
.play-btn {
  width: 48px;
  height: 48px;
  cursor: pointer;
}

.volume-control {
  width: 280px;
}
</style>