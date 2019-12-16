该Sample主要是总结当前自己项目上的经验及学习[Google Architecture Components](https://developer.android.com/arch)方案进行项目尝试。  
目前已有部分线上App校验过该Sample的运行逻辑，完全可行，适合App快速开发，并结合自身的库及用户需求自定扩展。  
由于Android项目架构多样化，技术迭代更新很快，该项目会持续集成优化主流技术，并在线上App校验可行的情况下更新项目。希望有经验的开发者能指出不足，一起学习 :relaxed: :relaxed:

### Android 组件化架构实践

```
架构原则：内部的业务逻辑与外部无关，独立测试时不需要加载外部依赖
架构特性：易于维护，方便测试，高内聚，低耦合
工程结构：模块化开发
应用结构：组件化
协同工具：git相关
持续集成：jenkins + ci
```

#### 工程结构

<img src="./doc/component_build_0.png"  alt="component_all" align=center />

按照业务把工程划分以下几类模块

* **library层** 

	基础类库,存放精简的代码，高复用性，一般其他模块直接引用即可，比如Utils，BaseActivity 等
* **service层** 

	支持某类基础业务功能的独立模块，比如登陆服务，换肤服务.介于 library 层和 component 层中间，也可以直接被 app 层调用
	
* **component层** 

	聚合多中基础业务功能的复杂业务模块，比如朋友圈，附近的人，一般可能使用多个 service 服务，也可以直接使用 library
	
* **app层**
 	应用入口，聚合多个业务模块，比如主端或者调试程序

#### 应用结构

基于完全组件化开发, 协同 gradle plugin 插件进行工程约束辅助. [组件化插件](https://github.com/YummyLau/ComponentPlugin)

* 支持组件代码完全隔离，废除 router 硬编码跳转，采用面向接口编程
* 支持组件循环依赖（A组件依赖B组件sdk，B组件依赖A组件sdk）
* 便捷集成调试，支持单一模块调试多组件
* 接入成本极低，只需要导入配置，同时申明各组件sdk资源

把常规 module 转化成 组件
<img src="./doc/component_build_1.jpg"  alt="component_all" align=center />

打破常规 module 依赖，支持组件循环依赖
<img src="./doc/component_build_2.jpg"  alt="component_all" align=center />

开发时面向接口编程，打包时面向实现编译
<img src="./doc/component_build_3.jpg"  alt="component_all" align=center />

插件同时支持java/kotlin，插件 sample 如下

<img src="./doc/sample.png"  width = "270" height = "500" align=center />

#### Demo集成（持续完成更新...）

* **library层** 

```
libBase：基础公有类/工具库
libWebview：H5能力库
libPermission：权限管理库
libGeo：基于百度地图sdk封装的地理定位库
libNet：基于okhttp3+retrofit2封装的网络库
```

* **service层** 

```
serviceAccount：应用账号模块
serviceNet：应用网络模块，涉及接口解析，host配置，线程池管理等
serviceShare：分享模块
serviceSkin：换肤模块
serviceMedia：多媒体模块，包括图片展示，Gif播放管理，视频播放管理等
```
	
* **component层** 

```
compTabMine：wanAndroid 我的
compTabRecommandation：推荐
compTabHome：wanAndroid 首页
其中Home包含：
compSquare：wanAndroid 广场
compProject：wanAndroid 项目
compPaccounts：wanAndroid 公众号
compSystem：wanAndroid 体系
```

* **app层**

```
app: 主端 wanAndroid 客户端
debugModule： 集成调试应用
```

wanAndroid sample 如下

<img src="./doc/android_modular_architeture.png"  width = "270" height = "500" align=center />


### Android 技术栈（实现）

#### 技能栈
* 硬性要求（至少）
	* 熟悉掌握java/kotlin
	* 熟悉掌握四大组件/线程/进程通讯源码逻辑清晰
	* 熟悉掌握View自定义/绘制/扩展
	* 熟悉掌握ASM，WindowManager窗口管理，app启动流程/系统启动流程熟悉掌握
	* 熟悉性能优化，字节码相关/插件相关/热修复相关
	* 熟悉 git 版本管理及应对各种特殊场景
	* 熟悉 linux/unix 环境，熟悉主流开源框架，有阅读源码经历

* 掌握（最好）
	* 了解前段技术，js/h5等
	* 了解跨段技术，flutter等
	* 了解ios部分技术，与android常用实现区别
	* 了解产品开发流程：需求评估-技术评估-迭代开发-提测修复-回归验收-线上监控-反馈修复
	* 了解团队协同开发：人力评估/人员能力栈/积极性培养/新技术预研

#### 项目开发

* 熟悉组件化架构开发
* 熟悉混淆配置/多渠道打包流程/插件原理
* 熟悉调试手段/无线调试/模拟ip等
* 熟悉IDE使用
	* 内存/cpu/网络调试，卡顿检测
	* Gradle使用及基本依赖管理
	* 自定义配置，比如task定义，before run行为等
* 熟悉Android官方库，androidx标准使用
