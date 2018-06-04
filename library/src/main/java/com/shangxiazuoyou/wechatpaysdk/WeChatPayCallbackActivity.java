package com.shangxiazuoyou.wechatpaysdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WeChatPayCallbackActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechatpay_callback);

        if (WeChatPay.getInstance() != null) {
            WeChatPay.getInstance().getWXAPI().handleIntent(getIntent(), this);
        } else {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (WeChatPay.getInstance() != null) {
            WeChatPay.getInstance().getWXAPI().handleIntent(intent, this);
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (WeChatPay.getInstance() != null) {
                if (baseResp.errStr != null) {
                    Log.e("WeChatPay", "errstr=" + baseResp.errStr);
                }
                WeChatPay.getInstance().onResponse(baseResp.errCode);
                finish();
            }
        }
    }
}
