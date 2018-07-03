### 项目说明
本项目适用于有微信支付功能的项目，建议download此库进行本地引入。此库会跟随微信官方的SDK变动而变动。

## 1. 如何添加

#### 下载此库，本地引入

## 2. AndroidManifest.xml配置信息

**权限声明**

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

**注册Activity**
```xml
<activity
    android:name="com.shangxiazuoyou.wechatpaysdk.WeChatPayCallbackActivity"
    android:configChanges="orientation|keyboardHidden|navigation|screenSize"
    android:launchMode="singleTop"
    android:exported="true"
    android:theme="@android:style/Theme.NoDisplay" />
<activity-alias
    android:name=".wxapi.WXPayEntryActivity"
    android:exported="true"
    android:targetActivity="com.shangxiazuoyou.wechatpaysdk.WeChatPayCallbackActivity" />
```

## 3. 添加混淆规则

#### 在app目录下的proguard-rules.pro中添加如下规则

```
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}
```

## 4. 发起支付

```java
WeChatPay.init(this, "wxxxxxxxxxxxxxxxxx");//将第二个参数替换为你申请的微信AppID
WeChatPayInfo weChatPayInfo = new WeChatPayInfo();//微信支付参数
WeChatPay.getInstance().doPay(this, weChatPayInfo, new WeChatPayResultCallback() {
    @Override
    public void onSuccess() {
        //pay success, do something here
    }

    @Override
    public void onError(int errorCode) {
        //pay error, do something here
    }

    @Override
    public void onCancel() {
        //pay cancel, do something here
    }
});
```
