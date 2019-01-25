### 基础lib库


#### lib_net
主要实现网络请求逻辑， kotlin 实现， 默认使用 okhttp3 + Retorfit

#### lib_base
主要实现共有逻辑, kotlin 实现
* activity 封装基类 `Activity` , 支持换肤, 左侧滑动退出
* fragment 封装基类 `fragment`, 支持快速构建 fragment, 声明周期获取等
* view 封装常见使用 View, 比如说圆角 View 等
* listener 封装一些业务常见监听器, 比如双击等
* tips 封装一些开发的小技巧, 比如保活技术等
    * 保活三种方案
* util 封装常见使用工具
* rxjava 封装 rxjava 常见使用方法, 使用 autoDisposs 进行自动解除绑定