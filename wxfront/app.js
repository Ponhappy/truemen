// app.js
import TencentCloudChat from '@tencentcloud/chat';
import TIMUploadPlugin from 'tim-upload-plugin';
import TIMProfanityFilterPlugin from 'tim-profanity-filter-plugin';
import { genTestUserSig } from './debug/GenerateTestUserSig';
App({
  globalData: {
    config: {
      userID: 'Testing', 
      SECRETKEY: 'f18419ace7c3df5cca41a8f140d30dd82538faee15016fab677c0b0fa9ad1f36',
      SDKAPPID: 1600060054, 
      EXPIRETIME: 604800,
    },
    host: 'http://localhost:8080',
    // host: 'http://abc.com',// 域名
    navBarHeight: 0, // 导航栏高度
    menuRight: 0, // 胶囊距右方间距（方保持左、右间距一致）
    menuTop: 0, // 胶囊距顶部间距
    menuHeight: 0, // 胶囊高度（自定义内容可与胶囊高度保证一致）
    menuWidth: 0, // 胶囊宽度（自定义内容可与胶囊宽度保证一致）
    searchMarginTop: 0, // 搜索框上边距
    searchWidth: 0, // 搜索框宽度
    searchHeight: 0, // 搜索框高度
    // 身份全局变量
    uid:null,  //用户标识符
    phone:null,
    wechatId: null,
    userName:"默认用户名",
    bio:"默认自我介绍",
    userId:"user_null",
    gender:"匿",
    birthDate:"2000-01-01",
    zodiac: '水瓶座',
    userInfo:null,
    isLogin:false,
    openid:null,
    token:null,
    session_key:null,
  },
  // 设置全局变量的方法
  setGlobalData: function(obj) {
    Object.assign(this.globalData, obj);
  },
  // 获取全局变量
  getGlobalData: function() {
    return this.globalData;
  },

  login:function(){
    // wx.login()获取code
        wx.login({
            success:(res)=>{
                console.log("code: " + res.code);
                wx.request({
                    url:getApp().globalData.host+'/wechat-login',
                    method:'POST',
                    data:{
                        code : res.code
                    }
                })
            }
    })
  },

  onLaunch: function (options) {
    // this.login() 
    console.log("app onLaunch")
    const that = this
    // 获取系统信息
    const systemInfo = wx.getSystemInfoSync()
    // 胶囊按钮位置信息
    const menuButtonInfo = wx.getMenuButtonBoundingClientRect()
    // console.log(systemInfo)
    // console.log(menuButtonInfo)

    const { screenWidth, statusBarHeight } = systemInfo
    const { top, width, height, right } = menuButtonInfo
    const margin = top - statusBarHeight
    // 导航栏高度 = 状态栏高度 + 顶部窗口高度（胶囊按钮位置信息上边距 - 状态栏高度） * 2 + 胶囊按钮高度 + 胶囊按钮位置信息下边距 - 状态栏高度
    that.globalData.navBarHeight = height + statusBarHeight + margin * 2
    that.globalData.menuRight = screenWidth - right
    that.globalData.menuTop = top
    that.globalData.menuHeight = height
    that.globalData.menuWidth = width
    this.globalData.searchMarginTop = statusBarHeight + margin // 状态栏 + 胶囊按钮边距
    this.globalData.searchHeight = height // 与胶囊按钮同高
    this.globalData.searchWidth = right - width - 20 // 胶囊按钮右边坐标 - 胶囊按钮宽度 = 按钮左边可使用宽度
    wx.$TUIKit = TencentCloudChat.create({
      SDKAppID: this.globalData.config.SDKAPPID,
    });
    const userSig = genTestUserSig(this.globalData.config).userSig 
    wx.$chat_SDKAppID = this.globalData.config.SDKAPPID;
    wx.TencentCloudChat = TencentCloudChat;
    wx.$chat_userID = this.globalData.config.userID;
    wx.$chat_userSig = userSig;
    wx.$TUIKit.registerPlugin({ 'tim-upload-plugin': TIMUploadPlugin });
    wx.$TUIKit.registerPlugin({ 'tim-profanity-filter-plugin': TIMProfanityFilterPlugin });
    wx.$TUIKit.login({
      userID: this.globalData.config.userID,
      userSig
  });
    // 监听系统级事件
    wx.$TUIKit.on(wx.TencentCloudChat.EVENT.SDK_READY, this.onSDKReady,this);    
  },
  onUnload() {
    wx.$TUIKit.off(wx.TencentCloudChat.EVENT.SDK_READY, this.onSDKReady,this);
  },
  onSDKReady(event) {
    // 监听到此事件后可调用 SDK 发送消息等 API，使用 SDK 的各项功能。
  }
})
