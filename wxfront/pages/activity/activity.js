// pages/activity/activity.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    showFilterModal: true,
    m: wx.getMenuButtonBoundingClientRect(),
    s: wx.getSystemInfoSync(),
    filterData: {
      onlySee: ['活动', '群组', '我收藏的', '我参与过的', '我创建的'],
      type: ['户外', '休闲', '文化', '美食', '拍照', '探店', '一人游', '多人行', '一日游'],
      time: ['最远', '最近', '今天', '这一年', '这一月']
    },
    selectedFilters: {},
    activities: [
      {
        id: 1,
        title: "活动1",
        category: "分类1",
        time: "2024/09/21 13:00:00",
        location: "地点1",
        desc: "描述1",
        details: "详情1",
        img: "https://img.yzcdn.cn/vant/cat.jpeg",
        img_title: "图片1",
      },
      {
        id: 2,
        title: "活动2",
        category: "分类2",
        time: "2024/10/07 09:00:00",
        location: "地点2",
        desc: "描述2",
        details: "详情2",
        img: "https://img.yzcdn.cn/vant/cat.jpeg",
        img_title: "图片2",
      }
    ]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {},

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {},

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    this.getTabBar().init();
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {},

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {},

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {},

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {},

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {},
  showFilter: function () {
    this.setData({
      showFilterModal: !this.data.showFilterModal
    });
  },
  handleConfirm: function (e) {
    this.setData({
      showFilterModal: false,
      selectedFilters: e.detail
    });
    console.log('Selected Filters:', e.detail);
  },
  goToSearch: function () {
    wx.navigateTo({
      url: './search-content/search-content',
    })
  },
});
