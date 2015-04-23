package com.talkingsdk.wdj;

import java.util.Map;

import org.json.JSONObject;

import android.util.Log;

import com.talkingsdk.MainApplication;
import com.talkingsdk.models.LoginData;
import com.talkingsdk.models.PayData;
import com.talkingsdk.wdj.SdkObject;
import com.unity3d.player.UnityPlayer;

public class GameSdkObject extends SdkObject {

    private static final String TAG = "GameSdkObject";

    @Override
    public void onLoginedRequest(LoginData lg, int code) {
        String ret = "{uid:'"+ lg.getUserId() +"',token:'"+ lg.getSessionId() + "''}";
        Log.i(TAG, ret);
        UnityPlayer.UnitySendMessage(getUnityGameObject(),"OnLoginResult",ret);
    }

    @Override
    public void onPaidRequest(PayData payData, int code) {
        Log.i(TAG, "payData");
        UnityPlayer.UnitySendMessage(getUnityGameObject(),"OnPayResult",payData.toString());
    }

    @Override
    public void onLogoutRequest(int code) {
        Log.d("Unity", "logout");
        UnityPlayer.UnitySendMessage(getUnityGameObject(),"OnLogoutResult","logout");
    }



}
