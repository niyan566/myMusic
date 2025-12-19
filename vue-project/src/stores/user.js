import { defineStore } from 'pinia'

// 定义用户 store
export const useUserStore = defineStore('user', {
  state: () => ({
    token: '', // 存储token
    user: '', // 存储用户信息
    isLoggedIn:false//登录状态
  }),
  actions: {
    // 设置用户信息（登录成功后调用）
    setUserInfo(token, user) {
      this.token = token
      this.user = user
      this.isLoggedIn=true
      // 持久化到本地存储（防止页面刷新丢失）
      localStorage.setItem('token', token)
      localStorage.setItem('user', JSON.stringify(user))
      localStorage.setItem('isLoggedIn', this.isLoggedIn)
    },
    // 初始化用户信息（页面刷新时调用）
    initUserInfo() {
      this.token = localStorage.getItem('token') || ''
      const storedUser = localStorage.getItem('user')
      this.user = storedUser ? JSON.parse(storedUser) : null
      this.isLoggedIn=localStorage.getItem('isLoggedIn')||false
    },
    // 退出登录（清空信息）
    logout() {
      this.token = ''
      this.user = ''
      this.isLoggedIn=false
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      localStorage.removeItem('isLoggedIn')
    }
  }
})