<template>
    <div>
        <el-table 
            :data="data.tableData" 
            height="calc(100vh - 161px)" 
            size="default" 
            v-loading="loading"
            style="width: 100%" 
            @cell-mouse-enter="handleRowMouseEnter"
            @cell-mouse-leave="handleRowMouseLeave">
            <el-table-column prop="id" label="#" width="180" />
            <el-table-column label="歌曲">
                <template #default="scope">
                    <div style="display: flex;align-items: center;position: relative;">
                        <img :src="scope.row.coverPath" style="height: 40px;width: 40px;" :alt="scope.row.title"/>
                        <el-button
                            v-if="hoveredRowId === scope.row.id && !(playerStore.isPlaying && playerStore.currentSong.id === scope.row.id)"
                            icon="VideoPlay"
                            circle
                            size="small"
                            style="
                            position: absolute;
                            left: 10px;
                            top: 10px;
                            background-color: rgba(0, 0, 0, 0.2);
                            color: white;
                            width: 20px;
                            height: 20px;
                            padding: 0;
                            "
                            @click.stop="handlePlay(scope.row)"
                        />
                        <el-button
                            v-if="playerStore.isPlaying && playerStore.currentSong.id === scope.row.id"
                            icon="VideoPause"
                            circle
                            size="small"
                            style="
                            position: absolute;
                            left: 10px;
                            top: 10px;
                            background-color: rgba(0, 0, 0, 0.2);
                            color: white;
                            width: 20px;
                            height: 20px;
                            padding: 0;
                            "
                            @click.stop="handlePause"
                        />
                        <span style="margin-left: 10px;">{{ scope.row.title }}</span>
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="singer" label="歌手" />
            <el-table-column>
              <template #default="scope">
                <div style="display: flex;" v-if="hoveredRowId === scope.row.id">
                  <img 
                  v-if="!scope.row.isLiked"
                  src="../assets/img/我喜欢.png" 
                  style="height: 25px;"
                  @click="toLike(scope.row)"
                  />

                  <img 
                  v-else
                  src="../assets/img/喜欢.png" 
                  style="height: 25px;"
                  @click="toLike(scope.row)"
                  />
                </div>
              </template>
            </el-table-column>
            <el-table-column label="时长" >
                <template #default="scope">
                    {{ formatSecondsToTime(scope.row.duration) }}
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script setup>
import { formatSecondsToTime } from '@/utils/formatSecondsToTime';
import request from '@/utils/request';
import { reactive, ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { usePlayerStore } from '@/stores/player';
import { useUserStore } from '@/stores/user'
import { useRoute } from 'vue-router';

const playerStore = usePlayerStore();
const loading = ref(true)
const data = reactive({
    tableData: []
});
const userStore=useUserStore()

const hoveredRowId = ref(null);

//接收参数
const route=useRoute()
const {id}=route.params


// 加载用户喜欢的音乐
const load = async () => {
  try {
    const res = await request.get('/user/listLikedMusic');
    if (res.code === '200') {
      data.tableData = res.data;
      playerStore.setSongList(data.tableData);
      setTimeout(()=>{
        loading.value=false
      },500)
    }
  } catch (error) {
    console.error('加载失败：', error);
  }
};

// 播放音乐
const handlePlay = async (row) => {
  try {
    playerStore.play(row);
  } catch (error) {
    console.error('播放失败：', error);
    ElMessage.error('播放失败');
  }
};

// 暂停音乐
const handlePause = () => {
  playerStore.pause();
};


const toLike=(row)=>{
  if(userStore.isLoggedIn){
    const likedMusic={
      musicId:row.id
    }

    request.post('/user/toLiked',likedMusic).then((res)=>{
      if(res.code==='200'){
        ElMessage.success('操作成功')
        row.isLiked=!row.isLiked
      }
    })
  }
  
}

// 鼠标事件
const handleRowMouseEnter = (row) => {
  hoveredRowId.value = row.id;
};

const handleRowMouseLeave = () => {
  hoveredRowId.value = null;
};

// 初始加载
onMounted(() => {
  load();
});
</script>