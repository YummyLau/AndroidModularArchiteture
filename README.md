![banner][banner]

### About
该Sample主要是总结当前自己项目上的经验及学习[Google Architecture Components](https://developer.android.com/arch)方案进行项目尝试。  
目前已有部分线上App校验过该Sample的运行逻辑，完全可行，适合一些中小规模App快速开发，并结合自身的库及用户需求自定扩展。  
由于Android项目架构多样化，技术迭代更新很快，该项目会持续集成优化主流技术，并在线上App校验可行的情况下更新项目。希望有经验的开发者能指出不足，一起学习 :relaxed: :relaxed:


### Project Architeture  
项目采用模块化结构设计，基于[Google Architecture Components](https://developer.android.com/arch)方案和MVVM设计模式完成模块开发完成

- basiclib 分装实现开源库及私有库，存放公用基础类（如*BaseActivity*）
- componentlib 开放模块基础服务Api及模块Api，存放模块公用业务类（如*WeiboHttpStatus*）
- xxService  xx表示某种模块基础服务，按需实现，如*AccountService*、*SkinService*供模块调用
- xxComponent  xx表示某个模块，按需实现，如*WeiboComponent*等

<img src="https://github.com/YummyLau/AndroidModularArchiteture/blob/master/art/architeture.png" width = "900" align=center />

### Modular Component Relationship
无论是基础服务还是业务组件，都依赖于*Componentlib*  

- IService 定义公用基础服务Api
- IxxService 定义xx服务暴露的Api
- xxServiceImpl 定义xx服务api的实现类
- ServiceManager 管理*Service*的注册与反注册  

- IComponent 定义公用模块Api
- IxxComponent 定义xx模块暴露的Api
- xxComponentImpl 定义xx模块api的实现类
- ComponentManager 管理*Component*的绑定及解绑

<img src="https://github.com/YummyLau/AndroidModularArchiteture/blob/master/art/componentrelationship.png" width = "700" align=center />

### Modular Component Communication
模块间的交互有两种

- 主动调用某个模块的Api，可通过*ServiceManager*和*ComponentManager*获取具体模块*Impl*实现
- 针对模块单播、多播事件，可通过*EventBus*驱动实现

<img src="https://github.com/YummyLau/AndroidModularArchiteture/blob/master/art/componentcommunication.png" width = "900" align=center />

### Internal module(Service or Component) Architeture
模块内使用MVVM+[Google Architecture Components](https://developer.android.com/arch)实现

- Room
- Lifecycle-aware components
- ViewModels
- LiveData

<img src="https://github.com/YummyLau/AndroidModularArchiteture/blob/master/art/final-architecture.png" width = "700" align=center />

[License](https://github.com/YummyLau/AndroidModularArchiteture/blob/master/LICENSE)

[banner]: https://github.com/YummyLau/AndroidModularArchiteture/blob/master/art/banner.png


### 更新技术栈（进行中）
* 基础库提供
* 模块化及插件构建
* 模块通讯重构
* 性能优化方案
* java字节码及android持续集成技术
目前在feature_lab开始进行
