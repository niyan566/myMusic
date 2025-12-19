<template>
    <div class="container">
        <div>
            <el-button type="primary" @click="data.dialogVisible=true" style="margin-top: 20px;">新增</el-button>
        </div>
        <!--歌单列表-->
        <div class="playlist-grid">

            <div v-for="item in data.playLists" class="playlist-card"  @click="goToDetail(item.id)">
                <img :src=item.path alt="">
                <div class="playlist-title">{{ item.title }}</div>
            </div>

        </div>

        <!--新增歌单-->
        <el-dialog v-model="data.dialogVisible" title="新增歌单" @close="dialogClose"  width="500">
            <div style="padding: 20px 30px 0 30px;">
                <el-form :model="data.form">
                    <el-form-item label="歌单标题" label-width="80px">
                        <el-input v-model="data.form.title" autocomplete="off"/>
                    </el-form-item>
                    
                    <el-form-item label="封面" label-width="80px">
                        <div>
                        <el-upload
                        class="cover-uploader"
                        action="http://localhost:8080/file/uploadImg"
                        name="img"
                        :on-success="setUrl"
                        :show-file-list="false"
                        >
                        <!-- 图片尺寸和上传区域保持一致：120px×120px -->
                        <img v-if="data.form.path" :src="data.form.path" class="cover" />
                        <!-- 图标尺寸也和上传区域一致，避免溢出 -->
                        <el-icon v-else class="uploader-icon"><Plus /></el-icon>
                        </el-upload>
                    </div>
                    </el-form-item>
                    

                    <el-form-item label="描述" label-width="80px">
                        <el-input
                            v-model="data.form.describe"
                            :autosize="{ minRows: 2, maxRows: 4 }"
                            type="textarea"
                            placeholder="Please input"
                        />
                    </el-form-item>
                </el-form>
            </div>
            
            <template #footer>
            <div class="dialog-footer">
                <el-button @click="data.dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="addPlayList">
                新增
                </el-button>
            </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import router from '@/router';
import request from '@/utils/request';
import { ElButton, ElMessage } from 'element-plus';
import { reactive } from 'vue';
const data=reactive({
    form:{},
    dialogVisible:false,
    playLists:[]
})

const setUrl=(response)=>{
    data.form.path=response.data
}


const dialogClose=()=>{
    data.form.title=''
    data.form.imageUrl=''
    data.form.describe=''
}

const addPlayList=()=>{
    request.post('/playList/add',data.form).then((res)=>{
        if(res.code==='200'){
            ElMessage.success('操作成功')
            data.form=null
            data.dialogVisible=false
        }else{
            ElMessage.error(res.msg)
        }
    })
}

//加载,获取歌单列表
const load=()=>{
    request.get('/playList/list').then((res)=>{
        if(res.code==='200'){
            data.playLists=res.data
        }
    })
}

load()

//跳转到详情页
const goToDetail=(playListId)=>{
    router.push({
        name:'playList',
        params:{
            id:playListId
        }
    })
}

</script>

<style scoped>
    
        /* 1. 自定义上传容器：固定宽高+行内块，让边框有显示空间 */
    .cover-uploader {
    display: inline-block; /* 关键：让容器能设置宽高 */
    width: 120px;          /* 和图片/图标尺寸统一 */
    height: 120px;         /* 和图片/图标尺寸统一 */
    margin:20px 20px;
    border: 1px dashed #131313; /* 深色虚线，容易看清 */
    border-radius: 6px;
    cursor: pointer;
    position: relative;    /* 给图标/图片定位用 */
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
    }

    /* 2. 图片样式：填满容器 */
    .cover {
    width: 120px;           /* 用100%适配容器，避免尺寸不一致 */
    height: 120px;
    display: block;        /* 消除图片默认间距 */
    }

    /* 3. 图标样式：居中显示，尺寸和容器一致 */
    .uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 100%;           /* 适配容器宽度 */
    height: 100%;          /* 适配容器高度 */
    text-align: center;
    line-height: 120px;    /* 关键：让图标垂直居中 */
    position: absolute;    /* 绝对定位，和图片重叠 */
    top: 0;
    left: 0;
    }

    /* 可选：hover时边框变色，提升交互 */
    .cover-uploader:hover {
    border-color: #409EFF;
    }

   .container {
     padding: 0 32px; /* 左右一致 */
   }
   .playlist-grid {
     margin-top: 20px;
     display: grid;
     grid-template-columns: repeat(5, 1fr);
     gap: 10px;
   }
   .playlist-card {
     position: relative;
     width: 90%;
     padding-top: 90%; /* 使卡片变成正方形 */
     cursor: pointer;
     overflow: hidden;
     border-radius: 10px;
     transition: transform .2s, box-shadow .2s;
   }
   .playlist-card:hover {
     transform: scale(1.05);
     box-shadow: 0 8px 15px rgba(0,0,0,0.2);
   }
   .playlist-card img {
     position: absolute;
     top: 0;
     left: 0;
     width: 100%;
     height: 100%;
     object-fit: cover;
   }
   .playlist-title {
     position: absolute;
     bottom: 0;
     left: 0;
     width: 100%;
     padding: 6px;
     background: rgba(0,0,0,0.6);
     color: #fff;
     text-align: center;
     font-size: 14px;
     box-sizing: border-box;
   }
</style>