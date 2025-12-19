<template>
  <el-container class="page">
    <el-header class="page-header">
      <el-row justify="center" align="middle">
        <h2 class="title">🎵 音乐上传</h2>
      </el-row>
    </el-header>

    <el-main class="page-main">
      <el-card class="upload-card">
        <el-row justify="center">
          <el-button
            type="primary"
            icon="el-icon-upload2"
            @click="triggerSelect"
            size="large"
          >
            选择 MP3 文件
          </el-button>
        </el-row>

        <input
          type="file"
          ref="fileInput"
          accept=".mp3"
          @change="onFileChange"
          style="display: none"
        />

        <el-row justify="center" class="file-info" v-if="file">
          <el-icon><document /></el-icon>
          <span class="file-name">{{ file.name }}</span>
        </el-row>

        <el-row justify="center" class="btn-row">
          <el-button
            type="success"
            :disabled="!file"
            @click="uploadWithFetch"
            size="large"
          >
            上传到服务器
          </el-button>
        </el-row>
      </el-card>
    </el-main>
  </el-container>
</template>

<script setup>
import { ref } from "vue"
import { ElMessage } from "element-plus"
import { Document } from "@element-plus/icons-vue"

const fileInput = ref(null)
const file = ref(null)

const triggerSelect = () => fileInput.value.click()

const onFileChange = (e) => {
  const f = e.target.files[0]
  if (!f) return

  if (!f.name.toLowerCase().endsWith(".mp3")) {
    ElMessage.error("请选择 MP3 格式的文件")
    file.value = null
    return
  }
  file.value = f
}

const uploadWithFetch = async () => {
  if (!file.value) return

  const formData = new FormData()
  formData.append("file", file.value)

  try {
    const res = await fetch("http://127.0.0.1:8080/music/upload", {
      method: "POST",
      body: formData
    })
    if (!res.ok) throw new Error("上传失败")
    ElMessage.success("上传成功 🎉")
    file.value = null
  } catch (err) {
    console.error(err)
    ElMessage.error("上传失败，请稍后重试 ❌")
  }
}
</script>

<style scoped>
.page {
  height: 100vh;
  background: #f0f2f5;
}

.page-header {
  background: #409eff;
  padding: 16px;
  color: white;
}

.title {
  color: white;
  font-size: 24px;
  font-weight: bold;
  margin: 0;
}

.page-main {
  display: flex;
  justify-content: center;
  align-items: center;
  padding-top: 50px;
}

.upload-card {
  width: 480px;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.file-info {
  margin-top: 12px;
  font-size: 16px;
  color: #606266;
  align-items: center;
  gap: 8px;
}

.file-name {
  font-weight: 600;
}

.btn-row {
  margin-top: 20px;
}
</style>
