package com.shangxiazuoyou.wechatpaysdk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;

/**
 * 微信支付
 */
public class WeChatPay {

    private static WeChatPay mWeChatPay;
    private IWXAPI mIWXAPI;
    private WeChatPayResultCallback mCallback;

    private WeChatPay(Context context, String appId) {
        mIWXAPI = WXAPIFactory.createWXAPI(context, appId);
    }

    public static void init(Context context, String appId) {
        if (null == mWeChatPay) {
            mWeChatPay = new WeChatPay(context, appId);
        }
    }

    public static WeChatPay getInstance() {
        return mWeChatPay;
    }

    public IWXAPI getWXAPI() {
        return mIWXAPI;
    }

    public void doPay(Context context, WeChatPayInfo weChatPayInfo, WeChatPayResultCallback callback) {
        mCallback = callback;
        if (!isSupportWeChatPay(context)) {
            if (mCallback != null) {
                mCallback.onError(Constants.NO_OR_LOW_VISION);
            }
            return;
        }

        String appId = weChatPayInfo.getAppId();
        String partnerId = weChatPayInfo.getPartnerId();
        String prepayId = weChatPayInfo.getPrepayId();
        String packageValue = weChatPayInfo.getPackageValue();
        String nonceStr = weChatPayInfo.getNonceStr();
        String timeStamp = weChatPayInfo.getTimeStamp();
        String sign = weChatPayInfo.getSign();
        if (TextUtils.isEmpty(appId)
                || TextUtils.isEmpty(partnerId)
                || TextUtils.isEmpty(prepayId)
                || TextUtils.isEmpty(packageValue)
                || TextUtils.isEmpty(nonceStr)
                || TextUtils.isEmpty(timeStamp)
                || TextUtils.isEmpty(sign)) {
            if (mCallback != null) {
                mCallback.onError(Constants.ERROR_PAY_PARAMS);
            }
            return;
        }

        PayReq req = new PayReq();
        req.appId = appId;
        req.partnerId = partnerId;
        req.prepayId = prepayId;
        req.packageValue = packageValue;
        req.nonceStr = nonceStr;
        req.timeStamp = timeStamp;
        req.sign = sign;

        mIWXAPI.sendReq(req);
    }

    /**
     * 支付回调
     *
     * @param errorCode errCode值
     */
    public void onResponse(int errorCode) {
        if (mCallback == null) {
            return;
        }
        if (errorCode == 0) {
            mCallback.onSuccess();
        } else if (errorCode == -1) {
            mCallback.onError(Constants.ERROR_PAY);
        } else if (errorCode == -2) {
            mCallback.onCancel();
        }
        mCallback = null;
    }

    /**
     * 检测是否支持微信支付(其中包括是否安装微信App)
     *
     * @param context 上下文
     * @return 是否支持微信支付
     */
    private boolean isSupportWeChatPay(Context context) {
        if (mIWXAPI.isWXAppInstalled() && mIWXAPI.isWXAppSupportAPI()) {
            return true;
        } else {
            final PackageManager packageManager = context.getPackageManager();
            List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
            if (packageInfoList != null) {
                for (int i = 0; i < packageInfoList.size(); i++) {
                    String pn = packageInfoList.get(i).packageName;
                    if (pn.equalsIgnoreCase("com.tencent.mm")) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
