import request from '@/utils/request'
import { defineStore } from 'pinia'

export const usePlayerStore = defineStore('player', {
  state: () => ({
    currentSong: {},
    isPlaying: false,
    currentTime: 0,
    totalTime: 0,
    audioUrl: '',
    audioRef: null,
    songList: []
  }),
  actions: {
    // 初始化音频实例
    initAudio() {
      if (!this.audioRef) {
        this.audioRef = new Audio()
        
        // 监听时间更新
        this.audioRef.addEventListener('timeupdate', () => {
          this.currentTime = this.audioRef.currentTime
          this.totalTime = this.audioRef.duration || 0
        })
        
        // 监听播放结束
        this.audioRef.addEventListener('ended', () => {
          this.isPlaying = false
        })
      }
    },
    // 设置歌曲列表
    setSongList(list) {
      this.songList = list
    },
    // 播放歌曲
    play(song) {
      this.initAudio()
      this.currentSong = song
      this.audioUrl = song.path
      this.audioRef.src = song.path

      //向后端发送播放记录
      const recentlyPlayed={
        musicId:song.id
      }
      request.post('/recentlyPlayed/record',recentlyPlayed)

      this.audioRef.play().then(() => {
        this.isPlaying = true
      }).catch(err => {
        console.error('播放失败:', err)
        this.isPlaying = false
      })
    },
    // 暂停播放
    pause() {
      if (this.audioRef) {
        this.audioRef.pause()
        this.isPlaying = false
      }
    },
    // 切换播放状态
    togglePlay() {
      if (!this.audioRef || !this.audioUrl) return
      
      if (this.isPlaying) {
        this.audioRef.pause()
      } else {
        this.audioRef.play()
      }
      this.isPlaying = !this.isPlaying
    },
    // 进度调整
    seek(time) {
      if (this.audioRef) {
        this.audioRef.currentTime = time
        this.currentTime = time
      }
    },
    // 下一首
    nextSong() {
      if (!this.songList.length) return
      
      const currentIndex = this.songList.findIndex(song => song.id === this.currentSong.id)
      const nextIndex = (currentIndex + 1) % this.songList.length
      const nextSong = this.songList[nextIndex]
      
      this.play(nextSong)
      //this.getSongUrlAndPlay(nextSong)
    },
    // 上一首
    prevSong() {
      if (!this.songList.length) return
      
      const currentIndex = this.songList.findIndex(song => song.id === this.currentSong.id)
      const prevIndex = (currentIndex - 1 + this.songList.length) % this.songList.length
      const prevSong = this.songList[prevIndex]
      
      this.play(prevSong)
    }
  }
})