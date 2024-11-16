// page-modify-tags.js
Page({
  data: {
    hobbies: [
      { id: 1, name: '篮球' },
      { id: 2, name: '足球' },
      { id: 3, name: '游泳' },
      { id: 4, name: '阅读' },
      { id: 5, name: '电影' },
      { id: 6, name: '音乐' },
      { id: 7, name: '旅行' }
    ],
    lifestyles: [
      { id: 1, name: '摄影' },
      { id: 2, name: '美食' },
      { id: 3, name: '健身' },
      { id: 4, name: '瑜伽' },
      { id: 5, name: '编程' },
      { id: 6, name: '科技' },
      { id: 7, name: '游戏' }
    ],
    popcultures: [
      { id: 1, name: '动漫' },
      { id: 2, name: '漫画' },
      { id: 3, name: '科幻' },
      { id: 4, name: '文学' },
      { id: 5, name: '艺术' },
      { id: 6, name: '历史' },
      { id: 7, name: '时尚' }
    ],
    selectedTags: [],
    currentCategory: '',
    isCustomPopupVisible: false,
    customInputValue: ''
  },

  onLoad: function(options) {
    // 初始化已选择的标签
    let selectedTags = [];
    if (options.selectedTags) {
      selectedTags = options.selectedTags.split(',');
    }
    this.setData({ selectedTags: selectedTags });
  },

  selectTag: function(e) {
    console.log(e);
    const id = e.currentTarget.dataset.id;
    const tag=this.data.hobbies[id-1].name;
    if (!this.data.selectedTags.includes(tag)) {
      this.setData({
        selectedTags: [...this.data.selectedTags, tag]
      });
      console.log(this.data.selectedTags)
    }
  },

  openCustomPopup: function(category) {
    this.setData({
      isCustomPopupVisible: true,
      currentCategory: category
    });
  },

  closeCustomPopup: function() {
    this.setData({
      isCustomPopupVisible: false,
      customInputValue: ''
    });
  },

  onCustomInput: function(e) {
    this.setData({
      customInputValue: e.detail.value
    });
  },

  addCustomTag: function() {
    const customTag = this.data.customInputValue.trim();
    if (customTag && !this.data.selectedTags.includes(customTag)) {
      this.setData({
        selectedTags: [...this.data.selectedTags, customTag],
        isCustomPopupVisible: false,
        customInputValue: ''
      });
    }
  },

  saveTags: function() {
    const selectedTags = this.data.selectedTags.join(',');
    wx.request({
      url: 'YOUR_BACKEND_URL',
      method: 'POST',
      data: {
        tags: selectedTags
      },
      success: function(res) {
        if (res.statusCode === 200) {
          wx.showToast({
            title: '保存成功',
            icon: 'success',
            duration: 1500
          });
          setTimeout(function() {
            wx.navigateBack({
              delta: 1
            });
          }, 1500);
        } else {
          wx.showToast({
            title: '保存失败',
            icon: 'none',
            duration: 2000
          });
        }
      },
      fail: function(err) {
        console.error(err);
      }
    });
  }
});