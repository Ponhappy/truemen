// index.js
Page({
  data: {
    // 页面数据
    val:"123"
  },

  sendPostRequest: function() {
    wx.request({
      url: 'http://baidu.com/nihao?uid=0',
      method: 'POST',
      header: {
        'content-type': 'application/json' // 设置请求头
      },
      data: {
        msg: '123'
      },
      success: function(res) {
        // console.log('请求成功', res.data);
        console.log(res);
        wx.showToast({
          title: '请求成功',
          icon: 'success',
          duration: 2000
        });
      },
      fail: function(err) {
        console.error('请求失败', err);
        wx.showToast({
          title: '请求失败',
          icon: 'none',
          duration: 2000
        });
      }
    });
  }
});