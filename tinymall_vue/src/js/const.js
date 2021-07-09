export default {
  baseUrl: 'http://localhost:8080',
  depUrl: 'http://120.79.59.75:8080',
  userInfoKey: 'preUserInfo',
  defShortcuts:[{
    text: '最近一周',
    value: (() => {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
      return [start, end]
    })()
  }, {
    text: '最近一个月',
    value: (() => {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
      return [start, end]
    })()
  }, {
    text: '最近三个月',
    value: (() => {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
      return [start, end]
    })()
  }],
}