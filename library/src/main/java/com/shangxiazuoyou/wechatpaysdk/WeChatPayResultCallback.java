package com.shangxiazuoyou.wechatpaysdk;

/**
 * 微信支付结果回调
 */
public interface WeChatPayResultCallback {

    /**
     * 支付成功
     */
    void onSuccess();

    /**
     * 支付失败
     *
     * @param errorCode 错误码
     */
    void onError(int errorCode);

    /**
     * 支付取消
     */
    void onCancel();
}
