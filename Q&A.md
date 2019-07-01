# Q&A

### 为什么会有 LiveData 转换？

1. Repository 获取到的数据传递到 View 之前需要改变数据类型，返回不一样的 LiveData，并且这样的 LiveData 可以共享生命周期状态；
2. 需要以 key 值来更新 LiveData 的 value。相比只创建 LiveData value，多一个 key 的 LiveData，保证 View 订阅的 LiveData 是同一个，且相当于多了一层缓冲，只有在 key 值改变时，才会发生查询 value 操作。

### 都有哪些转换类型？

1. Transformations#map：进行一些数据类型的转换
2. Transformations#switchMap：和 map 类似，不同的是 Function 函数返回值必须是 LiveData
3. MediatorLiveData：Transformations#map，switchMap 内部都是使用 MediatorLiveData 实现。可以通过 addSource 方法来合并处理多个 LiveData 数据。

### 这个项目里是否需要？

现有项目内，只有简单的关联数据库的 LiveData，也没有其他需要转换的场景，所以目前可能不需要 LiveData 转换。

### 为什么需要dagger？

解决依赖注入的问题，便捷的构建对象，进行模块解耦。