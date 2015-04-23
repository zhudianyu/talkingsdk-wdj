package com.talkingsdk.wdj;
import com.talkingsdk.MainApplication;
import android.content.Context;
import com.wandoujia.mariosdk.plugin.api.api.WandouGamesApi;
public class SdkMainApplication extends MainApplication {

	
  private static final long APP_KEY = 100008237;//100000225; // 100008237;
  private static final String SECURITY_KEY = "10159606448b775c8de9d0e79a4bfff3";// "9e45e1d5cfcd2ad21f86c1b43a12b3d8"; //"10159606448b775c8de9d0e79a4bfff3";

  private static WandouGamesApi wandouGamesApi;

  public static WandouGamesApi getWandouGamesApi() {
    return wandouGamesApi;
  }


  @Override
  protected void attachBaseContext(Context base) {
    WandouGamesApi.initPlugin(base, APP_KEY, SECURITY_KEY);
    super.attachBaseContext(base);
  }


  @Override
  public void onCreate() {
    wandouGamesApi = new WandouGamesApi.Builder(this, APP_KEY, SECURITY_KEY).create();
    wandouGamesApi.setLogEnabled(true);
    super.onCreate();
  }
	@Override
	public String getSdkObjectPackagePath() {
	    return "com.talkingsdk.wdj.GameSdkObject";
	}
}
