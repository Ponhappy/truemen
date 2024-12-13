Page({
  data: {
    m: wx.getMenuButtonBoundingClientRect(), // 获取胶囊按钮位置
    s: wx.getSystemInfoSync(), // 获取设备信息
    autoFocus: true, // 确保输入框自动获得焦点
    query: '', // 搜索框输入内容
    historyList: ['搜索内容', '搜索内容', '搜索内容', '搜索内容', '搜索内容'], // 模拟的历史搜索数据
    filters: {
      view: [],
      type: [],
      time: []
    },
  },
  onReady: function () {
    this.setData({
      autoFocus: true,
    })
  },
   // 搜索按钮点击事件或输入框确认事件
   onSearch: function () {
    const { query } = this.data;
    if (query.trim() === '') return; // 如果搜索内容为空，不进行任何操作

    // 将新的搜索内容添加到历史记录中
    let historyList = this.data.historyList;
    if (historyList.includes(query)) {
      // 如果历史记录中已存在该搜索内容，则移除旧的
      historyList = historyList.filter(item => item !== query);
    }
    historyList.unshift(query); // 将新搜索内容添加到历史记录的最前面

    // 限制历史记录的数量（例如最多10条）
    if (historyList.length > 10) {
      historyList = historyList.slice(0, 10);
    }

    // 更新数据并保存到本地存储
    this.setData({
      historyList: historyList,
      query: '' // 清空输入框
    });
    wx.setStorageSync('historyList', historyList);

    // 在这里可以添加更多的搜索逻辑
    console.log('执行搜索：', query);
     // 发送查询活动请求
     this.sendActivityRequest(query, this.data.filters);

  },
   // 发送查询活动请求
   sendActivityRequest(query, filters) {
    const url = 'https://trumen.com/api/v1/activities';
    const params = {};

    if (query.trim()) {
      params.query = query;
    }

    if (filters.view.length > 0) {
      params.view = filters.view.join(',');
    }

    if (filters.type.length > 0) {
      params.type = filters.type.join(',');
    }

    if (filters.time.length > 0) {
      params.time = filters.time.join(',');
    }

    wx.request({
      url: url,
      method: 'GET',
      data: params,
      success: (res) => {
        console.log('查询结果:', res.data);
        // 处理查询结果，例如更新页面显示
        this.updateUIWithResults(res.data);
      },
      fail: (err) => {
        console.error('请求失败:', err);
      }
    });
  },
  // 过滤器点击事件
  onFilterClick: function (e) {
    const { type, value } = e.currentTarget.dataset;
    const filters = this.data.filters;

    if (type === 'view') {
      if (filters.view.includes(value)) {
        filters.view = filters.view.filter(item => item !== value);
      } else {
        filters.view.push(value);
      }
    } else if (type === 'type') {
      if (filters.type.includes(value)) {
        filters.type = filters.type.filter(item => item !== value);
      } else {
        filters.type.push(value);
      }
    } else if (type === 'time') {
      if (filters.time.includes(value)) {
        filters.time = filters.time.filter(item => item !== value);
      } else {
        filters.time.push(value);
      }
    }

    this.setData({
      filters: filters
    });
  },
  // 更新界面显示
  updateUIWithResults(results) {
    // 根据返回的结果更新界面显示
    // 例如：this.setData({ activities: results.data });
    console.log('更新界面显示:', results);
  },
   // 历史搜索项点击事件
   onHistoryItemClick: function (e) {
    const query = e.currentTarget.dataset.query;
    this.setData({
      query: query
    });
    this.onSearch();
  },

  // 输入框输入事件
  onInput: function (e) {
    this.setData({
      query: e.detail.value,
    })
  },

  // 返回按钮点击事件
  goBack: function () {
    wx.navigateBack() // 返回上一页
  },
  onLoad() {
    // 监听键盘高度变化
    wx.onKeyboardHeightChange((res) => {
      console.log('键盘高度变化:', res.height)
      // 可以根据键盘高度调整页面布局
    })
  },
  clearHistory: function () {
    // 清空历史搜索记录
    this.setData({
      historyList: [],
    })
  },
})
