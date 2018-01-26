<img src="https://github.com/YummyLau/AndroidModularArchiteture/blob/master/art/banner.png" width = "2328"  alt="图片描述" align=center />

![banner][banner]

### About
该Sample主要是总结当前自己项目上的经验及学习Google Architecture Component方案进行项目尝试。   
目前已有部分线上App校验过该Sample的运行逻辑，完全可行，适合一些中小规模App快速开发，并结合自身的库及用户需求自定扩展。
由于Android项目架构多样化，技术迭代更新很快，该项目会持续集成优化主流技术，并在线上App校验可行下更新项目。
希望有经验的开发者可以指出不足，一起学习。

### Project Architeture

项目采用模块化结构设计，基于Google Architecture Component方案和MVVM设计模式完成模块开发完成。

- basiclib 分装实现开源库及私有库，存放公用基础类（如BaseActivity）
- componentlib 开放模块基础服务Api及模块Api，存放模块公用业务类（如WeiboHttpStatus）
- xxService  xx表示某种模块基础服务，按需实现，如AccountService、SkinService供模块调用
- xxComponent  xx表示某个模块，按需实现，如WeiboComponent等

<img src=" https://github.com/YummyLau/AndroidModularArchiteture/blob/master/art/architeture.png" width = "500"  alt="图片描述" align=center />

### Modular Component Rekatuinship
![componentrelationship][componentrelationship]  

### Modular Component Communication
![componentcommunication][componentcommunication]  

### Component Architeture
模块内使用MVVM+Google Architecture Component实现
![final-architecture][final-architecture] 



Google Architecture Component
- Room
- Lifecycle-aware components
- ViewModels
- LiveData
- Paging

network Design
- Rxjava2
- OKHttp3
- Retrofit2.0

rounter and event
- Arounter
- EventBus


License
-------
The samples based on modular development,using [Architecture Components](https://developer.android.com/arch) to practice that u
The mvvm design pattern is used inside the component,

Copyright (c) [year] [copyright holders]

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


[architeture]: https://github.com/YummyLau/AndroidModularArchiteture/blob/master/art/architeture.png
[banner]: https://github.com/YummyLau/AndroidModularArchiteture/blob/master/art/banner.png
[componentcommunication]: https://github.com/YummyLau/AndroidModularArchiteture/blob/master/art/componentcommunication.png
[componentrelationship]: https://github.com/YummyLau/AndroidModularArchiteture/blob/master/art/componentrelationship.png
[final-architecture]: https://github.com/YummyLau/AndroidModularArchiteture/blob/master/art/final-architecture.png

