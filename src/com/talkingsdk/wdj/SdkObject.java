package com.talkingsdk.wdj;

import android.os.Looper;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.app.Application;
import com.talkingsdk.MainApplication;
import com.talkingsdk.SdkCommonObject;
import com.talkingsdk.models.LoginData;
import com.talkingsdk.models.PayData;
import com.wandoujia.sdk.plugin.paydef.LoginCallBack;
import com.wandoujia.sdk.plugin.paydef.PayCallBack;
import com.wandoujia.sdk.plugin.paydef.User;
import com.wandoujia.sdk.plugin.paydef.WandouAccount;
import com.wandoujia.sdk.plugin.paydef.WandouOrder;
import com.wandoujia.sdk.plugin.paydef.WandouPay;
import com.wandoujia.sdk.plugin.paysdkimpl.PayConfig;
import com.wandoujia.sdk.plugin.paysdkimpl.WandouAccountImpl;
import com.wandoujia.sdk.plugin.paysdkimpl.WandouPayImpl;
import com.wandoujia.wandoujiapaymentplugin.utils.MSG;

//新版sdk
import com.wandoujia.mariosdk.plugin.api.api.WandouGamesApi;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnCheckLoginCompletedListener;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnLoginFinishedListener;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnLogoutFinishedListener;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnUserInfoSettingFinishedListener;
import com.wandoujia.mariosdk.plugin.api.model.model.LoginFinishType;
import com.wandoujia.mariosdk.plugin.api.model.model.LogoutFinishType;
import com.wandoujia.mariosdk.plugin.api.model.model.UnverifiedPlayer;
import com.wandoujia.mariosdk.plugin.api.model.model.WandouPlayer;
import com.wandoujia.mariosdk.plugin.api.model.model.result.UserInfoSettingResult;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnPayFinishedListener;
import com.wandoujia.mariosdk.plugin.api.model.model.PayResult;
public abstract class SdkObject extends SdkCommonObject {

    private static final String TAG = "WDJ";
    private WandouAccount account = new WandouAccountImpl();
    private WandouPay wandoupay = new WandouPayImpl();
    private boolean _restart = false;
    private boolean _isNewUser = false;
    private LoginData ld = null;
    private PayData _payData = null;
    private LoginCode _loginCode = null;

    private static final long APP_KEY = 100008237;//100000225; // 100008237;
    private static final String SECURITY_KEY = "10159606448b775c8de9d0e79a4bfff3";// "9e45e1d5cfcd2ad21f86c1b43a12b3d8"; //"10159606448b775c8de9d0e79a4bfff3";

    private WandouGamesApi wandouGamesApi;
    @Override
    public void onActivityCreate(Activity parentActivity) {
        super.onActivityCreate(parentActivity);
        wandouGamesApi = SdkMainApplication.getWandouGamesApi();
        // wandouGamesApi = new WandouGamesApi.Builder(getParentActivity(), APP_KEY, SECURITY_KEY).create();
        // wandouGamesApi.setLogEnabled(true);
        wandouGamesApi.init(getParentActivity());
        // startActivity(new Intent(getParentActivity(), GameActivity.class));
        PayConfig.init(getParentActivity(), getPropertiesByKey("APPKEYID"), getPropertiesByKey("APPSECRETKEY"));
       // login();
    }



    @Override
    public void onActivityDestroy() {

    }

    @Override
    public void login() {

    wandouGamesApi.login(new OnLoginFinishedListener() {
      @Override
      public void onLoginFinished(LoginFinishType loginFinishType, UnverifiedPlayer unverifiedPlayer) {
        String token = unverifiedPlayer.getToken();
         ld = new LoginData();
        //ld.setUsername(unverifiedPlayer.getUid().toString());
        ld.setSessionId(token);
        setLoginData(ld);
        onLoginedRequest(ld, 200);
      }
    });
        // account.doLogin(getParentActivity(), new LoginCallBack() {

        //     @Override
        //     public void onSuccess(User user, int type) {
        //         Log.d(TAG, "doLogin->onSuccess");
        //         // UID: 豌豆荚帐户, nick: 豌豆荚昵称, token: 豌豆荚帐户登陆验证, 15分钟内有效
        //         // 1.请把uid,token 提交游戏服务器
        //         // 2.游戏服务器收到uid,token后提交给豌豆荚服务器验证
        //         // 3.验证通过后，游戏服务器生成一个 cookie 给游戏客户端使用
        //         // 4.游戏客户端得到游戏的cookie 与游戏服务器进行交互通信，保证身份验证安全
                
        //         ld = new LoginData();
        //         ld.setUsername(user.getUid().toString());
        //         ld.setSessionId(user.getToken());
        //         setLoginData(ld);
        //         onLoginedRequest(ld, type);
        //         getParentActivity().finish();
        //         getParentActivity().startActivity(new Intent(getParentActivity(),
        //                 GameActivity.class));
        //     }

        //     @Override
        //     public void onError(int returnCode, String info) {
        //         // 请不要在这里重新调用 doLogin
        //         // 游戏界面上应该留有一个登录按钮，来触发 doLogin登录
        //         Log.e(TAG, MSG.trans(info));
        //     }
        // });

    }

    @Override
    public void logout() {
        Log.d(TAG, "doLogout");
        // doLogout无需处理回掉方法
        account.doLogout(getParentActivity(), new LoginCallBack() {

            @Override
            public void onSuccess(User user, int type) {
                Log.d(TAG, "doLogout->onSuccess:+" + user);
                onLogoutRequest(type);
                MainApplication.getInstance().notifyGameLogout(0);
            }

            @Override
            public void onError(int returnCode, String info) {
                // 请不要在这里重新调用 doLogin
                // 游戏界面上应该留有一个登录按钮，来触发 doLogin登录
                Log.e(TAG, MSG.trans(info));
            }
        });

    }

    @Override
    public void pay(PayData payData) {
        Log.d(TAG, "pay");
        _payData = payData;
        

        // WandouOrder order = new WandouOrder("豌豆币", "好多豌豆币", moneyInFen);
        // // 设置游戏订单号，最长50个字符
        // order.setOut_trade_no("GameOrderIdMaxLenth50");
        // // 触发支付
        // wandoupay.pay(getParentActivity(), order, new PayCallBack() {

        //     @Override
        //     public void onSuccess(User user, WandouOrder order) {
        //         Log.d(TAG, "pay->onSuccess:" + order);
        //         onPaidRequest(_payData, 0);
        //         MainApplication.getInstance().notifyGamePaid(_payData, 0);
        //     }

        //     @Override
        //     public void onError(User user, WandouOrder order) {
        //         Log.d(TAG, "pay->onError:" + order);
        //     }
        // });
     
          getParentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                float money = Float.parseFloat("0.01");
                long moneyInFen = (long) (money * 100);
                    wandouGamesApi.pay(getParentActivity(), "豌豆币", moneyInFen, "GameOrderIdMaxLenth50",
                     new OnPayFinishedListener(){
                          @Override
                            public void onPaySuccess(PayResult payResult) {

                            }

                            @Override
                            public void onPayFail(PayResult payResult) {

                            }

                           
                  });
                }
            });

    }

    @Override
    public void changeAccount() {
        account.reLogin(getParentActivity(), new LoginCallBack() {

            @Override
            public void onSuccess(User user, int type) {
                Log.d(TAG, "changeAccout->success:+" + user);

                // UID: 豌豆荚帐户, nick: 豌豆荚昵称, token: 豌豆荚帐户登陆验证, 15分钟内有效
                // 1.请把uid,token 提交游戏服务器
                // 2.游戏服务器收到uid,token后提交给豌豆荚服务器验证
                // 3.验证通过后，游戏服务器生成一个 cookie 给游戏客户端使用
                // 4.游戏客户端得到游戏的cookie 与游戏服务器进行交互通信，保证身份验证安全
                ld = new LoginData();
                ld.setUsername(user.getUid().toString());
                ld.setSessionId(user.getToken());
                setLoginData(ld);
            }

            @Override
            public void onError(int returnCode, String info) {
                // 请不要在这里重新调用 doLogin
                // 游戏界面上应该留有一个登录按钮，来触发 doLogin登录
                Log.d(TAG, MSG.trans(info));
            }
        });
    }

    @Override
    public void setRestartWhenSwitchAccount(boolean restart) {
        // do nothing
    }



    @Override
    public LoginCode getLoginCode() {
        return LoginCode.Success;
    }
      @Override
    public void onApplicationStart(Application obj) {
    }

    @Override
    public void onApplicationTerminate() {
    }
    //显示浮标
    public void showToolBar()
    {

    }
    //关闭浮标
    public void destroyToolBar()
    {

    }
    //显示用户中心
    public void showUserCenter()
    {
        
    }
    @Override
    public void onGameResume() {
        
        wandouGamesApi.onResume(getParentActivity());
    }

    @Override
    public void onGameFade() {
        // do nothing
    }
    
    @Override
    public void onKeyBack() {
        
    }

}