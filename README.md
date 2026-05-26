# captcha-jdk21

一个独立的点选验证码 Java 工具工程，基于 `form-in-21-4.2.00` 里的 `/api/cdk/efile/cltdenglv/getCaptcha` 逻辑重建，已去掉 `com.anji-plus:captcha:1.4.0` 依赖。

## 1. 项目说明

这个工程的目标是提供一个可单独编译、可直接调用的点选验证码实现，核心入口是 `xc.crabapple.core.file.image.captcha.anji.AnjiCaptchaUtil`。

默认情况下不需要外部底图目录，工程会自动生成一组可用的默认点选底图，方便直接跑通。

## 2. 目录结构

```text
src/main/java
├── xc/crabapple/core/file/image/captcha/anji/AnjiCaptchaUtil.java
├── com/anji/captcha/model/common/*
├── com/anji/captcha/model/vo/*
├── com/anji/captcha/service/*
├── com/anji/captcha/service/impl/*
└── com/anji/captcha/util/*

src/test/java
└── com/captcha/demo/CaptchaSmokeTest.java
```

## 3. 文件说明

### 3.1 入口工具

`xc/crabapple/core/file/image/captcha/anji/AnjiCaptchaUtil.java`

- 工程对外的唯一推荐入口。
- 提供 `init`、`get`、`check`、`verify` 四个静态方法。
- 负责初始化验证码服务、加载底图、包装调用链。

### 3.2 模型层

`com/anji/captcha/model/common/*`

- `Const.java`：验证码配置键常量。
- `CaptchaBaseMapEnum.java`：底图类型枚举。
- `CaptchaTypeEnum.java`：验证码类型枚举。
- `RepCodeEnum.java`：返回码枚举。
- `ResponseModel.java`：接口返回封装。

`com/anji/captcha/model/vo/*`

- `CaptchaVO.java`：验证码请求和响应载体。
- `PointVO.java`：点选坐标对象。

### 3.3 服务层

`com/anji/captcha/service/*`

- `CaptchaService.java`：验证码服务接口。
- `CaptchaCacheService.java`：缓存服务接口。

`com/anji/captcha/service/impl/*`

- `AbstractCaptchaService.java`：公共初始化与基础校验逻辑。
- `ClickWordCaptchaServiceImpl.java`：点选验证码主实现。
- `CaptchaServiceFactory.java`：服务和缓存工厂。
- `CaptchaCacheServiceMemImpl.java`：本地缓存实现。

### 3.4 工具层

`com/anji/captcha/util/*`

- `AESUtil.java`：点选结果加密解密。
- `CacheUtil.java`：本地过期缓存。
- `ImageUtils.java`：底图加载、Base64 转换、图片选择。
- `JsonUtil.java`：`PointVO` 的轻量 JSON 序列化/反序列化。
- `Base64Utils.java`：Base64 编解码。
- `FileCopyUtils.java`：流转字节数组。
- `MD5Util.java`：浏览器信息指纹。
- `RandomUtils.java`：随机数和随机字符串。
- `StringUtils.java`：字符串判空/比较。

### 3.5 测试

`src/test/java/com/captcha/demo/CaptchaSmokeTest.java`

- 验证 `get -> check -> verify` 全流程。
- 也是最直接的使用示例。

## 4. 功能流程

1. `get` 生成点选验证码图片和坐标答案。
2. `check` 校验用户点选坐标是否正确，并返回二次校验串。
3. `verify` 校验二次校验串是否有效。

## 5. 快速开始

### 5.1 环境要求

- JDK 21
- Maven 3.9+

### 5.2 构建

```bash
mvn test
```

或者：

```bash
mvn -DskipTests package
```

## 6. 用法说明

### 6.1 初始化

```java
AnjiCaptchaUtil.init(null);
```

如果你有自己的点选底图目录，也可以传入：

```java
AnjiCaptchaUtil.init("D:/captcha/pic-click");
```

目录要求：

- 目录下应包含 `.png` 图片
- 图片会被随机挑选作为验证码底图
- 如果目录不可用，会自动回退到工程内置默认底图

### 6.2 获取验证码

```java
CaptchaVO request = new CaptchaVO();
ResponseModel responseModel = AnjiCaptchaUtil.get(request);
```

成功后，`responseModel.getRepData()` 是一个 `CaptchaVO`，其中常用字段如下：

- `token`：本次验证码唯一标识
- `secretKey`：坐标加密密钥
- `originalImageBase64`：验证码图片
- `wordList`：需要用户点击的文字列表

### 6.3 校验验证码

`check` 需要传入：

- `token`
- `pointJson`

其中 `pointJson` 是点选坐标的 JSON，先按 `secretKey` 加密。

```java
String pointJson = JsonUtil.toJSONString(pointList);
String encryptedPointJson = AESUtil.aesEncrypt(pointJson, captchaVO.getSecretKey());

CaptchaVO checkRequest = new CaptchaVO();
checkRequest.setToken(captchaVO.getToken());
checkRequest.setPointJson(encryptedPointJson);

ResponseModel checkResponse = AnjiCaptchaUtil.check(checkRequest);
```

校验成功后，返回的 `CaptchaVO` 会带上：

- `result = true`
- `captchaVerification`：二次校验串

### 6.4 二次校验

```java
CaptchaVO verifyRequest = new CaptchaVO();
verifyRequest.setCaptchaVerification(checkedCaptchaVO.getCaptchaVerification());

ResponseModel verifyResponse = AnjiCaptchaUtil.verify(verifyRequest);
```

## 7. 测试示例

工程自带的 `CaptchaSmokeTest` 已覆盖完整流程：

```bash
mvn test

```

## 8. 注意事项

- 这是独立工具工程，不依赖 `com.anji-plus:captcha:1.4.0`。
- 默认底图是自动生成的，适合本地联调和脱离项目运行。
- 如果你接入自己的底图目录，建议确保目录里全是可用的 `.png` 文件。
- `pointJson` 必须先加密后再传给 `check`。

## 9. 推荐调用顺序

```text
init -> get -> check -> verify

```

