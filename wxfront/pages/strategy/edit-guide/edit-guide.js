Page({
  data: {
    guideId: null,
    guide: null,
  },

  onLoad(options) {
    const { id, guide } = options;
    this.setData({
      guideId: id,
      guide: JSON.parse(guide),
    });
  },

  // 保存编辑后的攻略
  onSaveGuide() {
    const { guideId, guide } = this.data;
    wx.request({
      url: `http://localhost:8080/api/v1/guides/${guideId}/edit`, // 本地后端地址
      method: 'PUT',
      data: guide,
      success: (res) => {
        if (res.statusCode === 200) {
          wx.showToast({
            title: '保存成功',
            icon: 'success',
          });
          wx.navigateBack();
        } else {
          wx.showToast({
            title: '保存失败',
            icon: 'none',
          });
        }
      },
      fail: () => {
        wx.showToast({
          title: '网络请求失败',
          icon: 'none',
        });
      },
    });
  },

  // 增加一天
  onAddDay() {
    const { guideId } = this.data;
    wx.request({
      url: `http://localhost:8080/api/v1/guides/${guideId}/addDay`, // 本地后端地址
      method: 'POST',
      success: (res) => {
        if (res.statusCode === 200) {
          wx.showToast({
            title: '增加一天成功',
            icon: 'success',
          });
          // 重新获取攻略详情
          this.fetchGuideDetail(guideId);
        } else {
          wx.showToast({
            title: '增加一天失败',
            icon: 'none',
          });
        }
      },
      fail: () => {
        wx.showToast({
          title: '网络请求失败',
          icon: 'none',
        });
      },
    });
  },

  // 获取攻略详情
  fetchGuideDetail(guideId) {
    wx.request({
      url: `http://localhost:8080/api/v1/guides/${guideId}`, // 本地后端地址
      method: 'GET',
      success: (res) => {
        if (res.statusCode === 200) {
          this.setData({ guide: res.data });
        } else {
          wx.showToast({
            title: '获取攻略详情失败',
            icon: 'none',
          });
        }
      },
      fail: () => {
        wx.showToast({
          title: '网络请求失败',
          icon: 'none',
        });
      },
    });
  },

  // 监听标题输入
  onTitleInput(e) {
    const title = e.detail.value;
    this.setData({
      guide: { ...this.data.guide, title },
    });
  },

  // 监听描述输入
  onDescriptionInput(e) {
    const description = e.detail.value;
    this.setData({
      guide: { ...this.data.guide, description },
    });
  },
});