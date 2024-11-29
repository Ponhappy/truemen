Component({
  options: {
    styleIsolation: "shared",
  },
  data: {
    active: "0",
    list: [
      {
        pagePath: "/pages/strategy/strategy",
        text: "攻略",
        iconPath: "../images/icons/icon_strategy.png",
        selectedIconPath: "../images/icons/icon_strategy_light.png",
        color: "#000000",
        selectedColor: "#F7AF0D",
      },
      {
        pagePath: "/pages/activity/activity",
        text: "活动",
        iconPath: "../images/icons/icon_activity.png",
        selectedIconPath: "../images/icons/icon_activity_light.png",
        color: "#000000",
        selectedColor: "#F7AF0D",
      },
      {
        pagePath: "/pages/index/index",
        text: "首页",
        iconPath: "../images/icons/icon_index.png",
        selectedIconPath: "../images/icons/icon_index_light.png",
        color: "#000000",
        selectedColor: "#C8102E",
      },
      {
        pagePath: "pages/friend/chat",
        text: "好友",
        iconPath: "../images/icons/icon_friend.png",
        selectedIconPath: "../images/icons/icon_friend_light.png",
        color: "#000000",
        selectedColor: "#002FA7",
      },
      {
        pagePath: "/pages/myself/myself",
        text: "我的",
        iconPath: "../images/icons/icon_myself.png",
        selectedIconPath: "../images/icons/icon_myself.png",
        color: "#000000",
        selectedColor: "#002FA7",
      },
    ],
  },

  methods: {
    onChange(event) {
      this.setData({
        active: event.detail,
      });
      wx.switchTab({
        url: this.data.list[event.detail].pagePath,
      });
    },

    init() {
      const page = getCurrentPages().pop();
      this.setData({
        active: this.data.list.findIndex(
          (item) => item.pagePath === `/${page.route}`
        ),
      });
    },
  },
});
