# androidx-swift

[`androidx-swift`](./kotlin) 是一个Android快速开发类库，也是一个工具库，内部已经集成了`Blankj`
开源的[AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)。

[`sword`](./sword) 是一个OkHttp + RxKotlin + Retrofit 的三剑客类库。

**功能列表：**

- 全局用户管理

- 配置管理

- MVP模式

- 工具类（感谢`Blankj`开源的[AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)）

- 适配器

- HTTP请求、拦截器、签名

*具体的细节功能这里不一一展示了，请移步到源码中查看。*

## 集成使用

1、配置仓库，或者自己下载源码上传到自己的仓库中又或者对源码级别依赖。

```gro
repositories {
	maven { url 'https://maven.raeblog.com/repository/public/' }
}
```

2、添加依赖

```gro
dependencies {
	// swift 核心库
    implementation 'androidx.swift:core-ktx:1.0.0'
	// sword 三剑客库 [可选]
    implementation 'androidx.swift:sword:1.0.0'
}
```



