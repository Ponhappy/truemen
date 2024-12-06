Component({
  properties: {
    // 从父组件传递过来的属性
    filterData: {
      type: Object,
      value: {}
    }
  },
  data: {
    selected: {
      onlySee: [],
      type: [],
      time: '',
      date:'选择日期'
    },
    showCalendar: false
  },
  methods: {
    toggleSelection: function (e) {
      const { category, option } = e.currentTarget.dataset;
      let selected = this.data.selected[category] || [];
      const index = selected.indexOf(option);

      if (index > -1) {
        selected.splice(index, 1);
      } else {
        selected.push(option);
      }

      this.setData({
        ['selected.' + category]: selected
      });
      console.log(this.data.selected)
    },
    clearSelection: function () {
      this.setData({
        selected: {
          onlySee: [],
          type: [],
          time: '',
          date:'选择日期'
        }
      });
    },
 // 确认选择的方法
 confirmSelection: function () {
  // 触发自定义事件，通知父组件或页面确认选择
  this.triggerEvent('confirm', this.data.selected);

  // 向后端发送请求
  const url = 'https://trumen.com/api/v1/activities';
  const selectedItems = this.data.selected;

  wx.request({
    url: url,
    method: 'POST',
    header: {
      'content-type': 'application/json' // 默认值
    },
    data: JSON.stringify(selectedItems), // 将数组转换为JSON字符串
    success: (res) => {
      console.log('成功发送数据:', res.data);
      // 根据返回结果进行相应处理
      if (res.statusCode === 200) {
        wx.showToast({
          title: '提交成功',
          icon: 'success',
          duration: 2000
        });
      } else {
        wx.showToast({
          title: '提交失败',
          icon: 'none',
          duration: 2000
        });
      }
    },
    fail: (err) => {
      console.error('发送数据失败:', err);
      wx.showToast({
        title: '网络错误，请重试',
        icon: 'none',
        duration: 2000
      });
    }
  });
},
    selectTime: function (e) {
      const time = e.currentTarget.dataset.option;

      this.setData({
        'selected.time': time,
        'selected.date':'选择日期'
      });
    },
    onDateChange: function (e) {
      const date = e.detail.value;
      this.setData({
        'selected.date': date,
      });
    },
    closeCalendar: function () {
      this.setData({
        showCalendar: false
      });
    }
  }
});