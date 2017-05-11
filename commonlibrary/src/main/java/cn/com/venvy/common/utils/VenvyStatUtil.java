package cn.com.venvy.common.utils;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import cn.com.venvy.common.http.HttpRequest;
import cn.com.venvy.common.http.RequestFactory;
import cn.com.venvy.common.http.base.IRequestConnect;
import cn.com.venvy.common.http.base.IRequestHandler;
import cn.com.venvy.common.http.base.IResponse;
import cn.com.venvy.common.http.base.Request;
import cn.com.venvy.okhttp3.Call;
import cn.com.venvy.okhttp3.util.OkHttpUtils;
import cn.com.venvy.okhttp3.util.callback.StringCallback;
/***
 * 统计工具类
 * @author John
 */
public class VenvyStatUtil {

	private static volatile boolean allowStat= true;

	/**
	 * 师傅
	 * @param allowStat
	 */
	public static void requestAllowStatistics(boolean allowStat){
		VenvyStatUtil.allowStat = allowStat;
	}
	// 确定的版本号
	private static final String VERSION = "1";
	private static final String SERVICE = "2";
	private static final String CHANNEL = "";
	private static final String BRAND = "";
	private static final String DURATION = "0";
	private static final String BODY_LIVE_STAT_CAT = "?cat=";
	private static final String LIVE_URL_STAT_HEADER = "http://va.videojj.com/track/va.gif/";
	private static final String BODY_LIVE_STAT_SERVICE = "&s=";
	private static final String BODY_LIVE_STAT_VERSION = "&ver=";
	private static final String BODY_LIVE_STAT_RANDOM = "&n=";
	private static final String BODY_LIVE_STAT_VIDEO = "&v=";
	private static final String BODY_LIVE_STAT_CHANNEL = "&ch=";
	private static final String BODY_LIVE_STAT_PROJECT = "&proj=";
	private static final String BODY_LIVE_STAT_USER_AGNET = "&ua=";
	private static final String BODY_LIVE_STAT_REFER = "&refer=";
	private static final String BODY_LIVE_STAT_CLIENT = "&c=";
	private static final String BODY_LIVE_STAT_RESOLUTION = "&rs=";
	private static final String BODY_LIVE_STAT_LANGUAGE = "&lang=";
	private static final String BODY_LIVE_STAT_TAG = "&tag=";
	private static final String BODY_LIVE_STAT_DG = "&dg=";
	private static final String BODY_LIVE_STAT_BRAND = "&br=";
	private static final String BODY_LIVE_STAT_TYPE = "&t=";
	private static final String BODY_LIVE_STAT_X = "&x=";
	private static final String BODY_LIVE_STAT_Y = "&y=";
	private static final String BODY_LIVE_STAT_LINK = "&link=";
	private static final String BODY_LIVE_STAT_DURATION = "&dr=";
	private static final String APP_KEY = "&a=";
	public static final int STAT_BALLS = 0;
	public static final int STAT_ADS = 1;
	public static final int STAT_INVENTORY = 2;
	// 机器信息
	private String mUserAgnet;
	// 唯一标示符
	private String mClientId;
	// 分辨率
	private String mResolution;
	// 语言
	private String mLanguage;
	private String mVideoId;
	private String mProjectId;
	private String mService;
	private String mVersion;
	private String mChannel;
	private String mBrand;

	public static class StatConfigBuilder {
		private String userAgent;
		private String language;
		private String resolution;
		private String client;
		private String videoId;
		private String projectId;
		private String service;
		private String version;
		private String brand;
		private String channel;


		public VenvyStatUtil build(){
			checkInvalid();
			VenvyStatUtil instance = new VenvyStatUtil();
			instance.mUserAgnet = userAgent;
			instance.mLanguage = language;
			instance.mResolution = resolution;
			instance.mClientId = client;
			instance.mVideoId = videoId;
			instance.mProjectId = projectId;
			instance.mService = service;
			instance.mVersion = version;
			instance.mBrand = brand;
			instance.mChannel = channel;

			return instance;
		}

		private void checkInvalid(){
			if (TextUtils.isEmpty(service)) {
				service = SERVICE;
			}

			if(TextUtils.isEmpty(version)){
				version = VERSION;
			}
			if(TextUtils.isEmpty(brand)){
				brand = BRAND;
			}

			if(TextUtils.isEmpty(channel)){
				channel = CHANNEL;
			}
		}

		public StatConfigBuilder setService(String service) {
			this.service = service;
			return this;
		}

		public StatConfigBuilder setVersion(String version) {
			this.version = version;
			return this;
		}

		public StatConfigBuilder setChannel(String channel) {
			this.channel = channel;
			return this;
		}


		public StatConfigBuilder setBrand(String brand) {
			this.brand = brand;
			return this;
		}

		public StatConfigBuilder setUserAgent(String userAgent) {
			this.userAgent = userAgent;
			return this;
		}

		public StatConfigBuilder setLanguage(String language) {
			this.language = language;
			return this;
		}

		public StatConfigBuilder setResolution(String resolution) {
			this.resolution = resolution;
			return this;
		}

		public StatConfigBuilder setClient(String client) {
			this.client = client;
			return this;
		}
		public StatConfigBuilder setVideoId(String videoId) {
			this.videoId = videoId;
			return this;
		}

		public StatConfigBuilder setProjectId(String projectId) {
			this.projectId = projectId;
			return this;
		}
	}

	private VenvyStatUtil(){
	}

	/**
	 * true 不允许打点
	 * @return
	 */
	private static boolean disallowStat(){
		return !allowStat;
	}

	public void cat4(@Nullable String refer) {
		if(disallowStat()){
			return;
		}
		StringBuilder builder = new StringBuilder();
		builder.append(LIVE_URL_STAT_HEADER);
		builder.append(BODY_LIVE_STAT_CAT);
		builder.append(4);//cat
		builder.append(BODY_LIVE_STAT_SERVICE);
		builder.append(mService);//service
		builder.append(BODY_LIVE_STAT_VERSION);
		builder.append(mVersion);//version
		builder.append(BODY_LIVE_STAT_RANDOM);
		builder.append(System.currentTimeMillis());//random
		builder.append(BODY_LIVE_STAT_VIDEO);
		builder.append(mVideoId);//video
		builder.append(BODY_LIVE_STAT_CHANNEL);
		builder.append(mChannel);//channel
		builder.append(BODY_LIVE_STAT_PROJECT);
		builder.append(mProjectId);//project
		builder.append(BODY_LIVE_STAT_USER_AGNET);
		builder.append(mUserAgnet);//user_agent
		builder.append(BODY_LIVE_STAT_REFER );
		builder.append(refer);//refer
		builder.append(BODY_LIVE_STAT_CLIENT);
		builder.append(mClientId);//client
		builder.append(BODY_LIVE_STAT_RESOLUTION );
		builder.append(mResolution );//resolution
		builder.append(BODY_LIVE_STAT_LANGUAGE );
		builder.append(mLanguage);//language
		builder.append(BODY_LIVE_STAT_LANGUAGE );
		builder.append(mLanguage);//language
		builder.append(APP_KEY);//appkey
		builder.append(mProjectId);
		httpRequest(builder.toString());
	}

	// 离开直播间统计cat=19//
	public void cat19() {
		if(disallowStat()){
			return;
		}
		StringBuilder builder = new StringBuilder();
		builder.append(LIVE_URL_STAT_HEADER);
		builder.append(BODY_LIVE_STAT_CAT);
		builder.append(19);//cat
		builder.append(BODY_LIVE_STAT_SERVICE);
		builder.append(mService);//service
		builder.append(BODY_LIVE_STAT_VERSION);
		builder.append(mVersion);//version
		builder.append(BODY_LIVE_STAT_RANDOM);
		builder.append(System.currentTimeMillis());//random
		builder.append(BODY_LIVE_STAT_VIDEO);
		builder.append(mVideoId);//video
		builder.append(BODY_LIVE_STAT_CHANNEL);
		builder.append(mChannel);//channel
		builder.append(BODY_LIVE_STAT_PROJECT);
		builder.append(mProjectId);//project
		builder.append(APP_KEY);
		builder.append(mProjectId);//appkey
		builder.append(BODY_LIVE_STAT_USER_AGNET);
		builder.append(mUserAgnet);//user_agent
		builder.append(BODY_LIVE_STAT_REFER );
		builder.append("");//refer
		builder.append(BODY_LIVE_STAT_CLIENT);
		builder.append(mClientId);//client
		builder.append(BODY_LIVE_STAT_RESOLUTION);
		builder.append(mResolution);
		builder.append(mLanguage);//language
		builder.append(BODY_LIVE_STAT_LANGUAGE );
		httpRequest(builder.toString());
	}

	/**
	 * 热点/灵动求、直播广告显示，新广告曝光
	 */
	public void cat12(
			@NonNull  String tagId,
			@NonNull  String dgId,
			@Nullable String refer,
			@Nullable String type,
			@Nullable String x,
			@Nullable String y) {

		if(disallowStat()){
			return;
		}
		StringBuilder builder = new StringBuilder();
		builder.append( LIVE_URL_STAT_HEADER);
		builder.append( BODY_LIVE_STAT_CAT);
		builder.append(12);//cat
		builder.append(BODY_LIVE_STAT_SERVICE );
		builder.append(mService);//service
		builder.append(BODY_LIVE_STAT_VERSION);
		builder.append(mVersion);//veision
		builder.append(BODY_LIVE_STAT_RANDOM);
		builder.append(System.currentTimeMillis());//random
		builder.append(BODY_LIVE_STAT_VIDEO);
		builder.append(mVideoId);//video
		builder.append(BODY_LIVE_STAT_CHANNEL);
		builder.append(mChannel);//channel
		builder.append(BODY_LIVE_STAT_PROJECT);
		builder.append(mProjectId);//project
		builder.append(BODY_LIVE_STAT_TAG);
		builder.append(tagId);//tag
		builder.append(BODY_LIVE_STAT_DG);
		builder.append(dgId);//dg
		builder.append(BODY_LIVE_STAT_BRAND);
		builder.append(mBrand);//brand
		builder.append(BODY_LIVE_STAT_TYPE);
		builder.append(type);//type
		builder.append(APP_KEY);
		builder.append(mProjectId);
		builder.append(BODY_LIVE_STAT_X);
		builder.append(x);
		builder.append(BODY_LIVE_STAT_Y);
		builder.append(y);
		builder.append(BODY_LIVE_STAT_USER_AGNET);
		builder.append(mUserAgnet);//user-agent
		builder.append(BODY_LIVE_STAT_REFER);
		builder.append(refer);
		builder.append(BODY_LIVE_STAT_CLIENT);
		builder.append(mClientId);
		builder.append(BODY_LIVE_STAT_RESOLUTION);
		builder.append(mResolution);
		builder.append(BODY_LIVE_STAT_LANGUAGE);
		builder.append(mLanguage);
		httpRequest(builder.toString());
	}

	// 开启请求
	// 直播广告点击cat=9//
	public void cat9(
			@NonNull String tagId,
			@NonNull String dgId,
			@Nullable String refer,
			@Nullable String type) {
		if(disallowStat()){
			return;
		}
		StringBuilder builder = new StringBuilder();
		builder.append( LIVE_URL_STAT_HEADER);
		builder.append( BODY_LIVE_STAT_CAT);
		builder.append(9);
		builder.append(BODY_LIVE_STAT_SERVICE);
		builder.append(mService);
		builder.append(BODY_LIVE_STAT_VERSION);
		builder.append(mVersion);
		builder.append(BODY_LIVE_STAT_RANDOM);
		builder.append(System.currentTimeMillis());
		builder.append(BODY_LIVE_STAT_VIDEO);
		builder.append(mVideoId);
		builder.append(BODY_LIVE_STAT_CHANNEL);
		builder.append(mChannel);
		builder.append(BODY_LIVE_STAT_PROJECT);
		builder.append(mProjectId);
		builder.append(BODY_LIVE_STAT_TAG);
		builder.append(tagId);
		builder.append(BODY_LIVE_STAT_DG);
		builder.append(dgId);
		builder.append(BODY_LIVE_STAT_BRAND);
		builder.append(mBrand);
		builder.append(BODY_LIVE_STAT_TYPE);
		builder.append(type);
		builder.append(APP_KEY);
		builder.append(mProjectId);
		builder.append(BODY_LIVE_STAT_USER_AGNET);
		builder.append(mUserAgnet);
		builder.append(BODY_LIVE_STAT_REFER);
		builder.append(refer);
		builder.append(BODY_LIVE_STAT_CLIENT);
		builder.append(mClientId);
		builder.append(BODY_LIVE_STAT_RESOLUTION);
		builder.append(mResolution);
		builder.append(BODY_LIVE_STAT_LANGUAGE);
		builder.append(mLanguage);
		httpRequest(builder.toString());
	}

	// 热点推送cat=8
	public void cat8(@Nullable  String refer) {
		if(disallowStat()){
			return;
		}
		StringBuilder builder = new StringBuilder();
		builder.append( LIVE_URL_STAT_HEADER);
		builder.append( BODY_LIVE_STAT_CAT);
		builder.append(8);
		builder.append(BODY_LIVE_STAT_SERVICE);
		builder.append(mService);
		builder.append(BODY_LIVE_STAT_VERSION);
		builder.append(mVersion);
		builder.append(BODY_LIVE_STAT_RANDOM);
		builder.append(System.currentTimeMillis());
		builder.append(BODY_LIVE_STAT_VIDEO);
		builder.append(mVideoId);
		builder.append(BODY_LIVE_STAT_CHANNEL);
		builder.append(mChannel);
		builder.append(BODY_LIVE_STAT_PROJECT);
		builder.append(mProjectId);
		builder.append(APP_KEY);
		builder.append(mProjectId);
		builder.append(BODY_LIVE_STAT_USER_AGNET);
		builder.append(mUserAgnet);
		builder.append(BODY_LIVE_STAT_REFER);
		builder.append(refer);
		builder.append(BODY_LIVE_STAT_CLIENT);
		builder.append(mClientId);
		builder.append(BODY_LIVE_STAT_RESOLUTION);
		builder.append(mResolution);
		builder.append(BODY_LIVE_STAT_LANGUAGE);
		builder.append(mLanguage);
		httpRequest(builder.toString());
	}

	// 热点暂停cat=27
	public void cat27( @Nullable String refer) {
		if(disallowStat()){
			return;
		}
		StringBuilder builder = new StringBuilder();
		builder.append(LIVE_URL_STAT_HEADER);
		builder.append(BODY_LIVE_STAT_CAT);
		builder.append(27);
		builder.append(BODY_LIVE_STAT_SERVICE );
		builder.append(mService);
		builder.append(BODY_LIVE_STAT_VERSION);
		builder.append(mVersion);
		builder.append(BODY_LIVE_STAT_RANDOM);
		builder.append(System.currentTimeMillis());
		builder.append(BODY_LIVE_STAT_VIDEO);
		builder.append(mVideoId);
		builder.append(BODY_LIVE_STAT_CHANNEL);
		builder.append(mChannel);
		builder.append(BODY_LIVE_STAT_PROJECT);
		builder.append(mProjectId);
		builder.append(APP_KEY);
		builder.append(mProjectId);
		builder.append(BODY_LIVE_STAT_USER_AGNET);
		builder.append(mUserAgnet);
		builder.append(BODY_LIVE_STAT_REFER);
		builder.append(refer);
		builder.append(BODY_LIVE_STAT_CLIENT);
		builder.append(mClientId);
		builder.append(BODY_LIVE_STAT_RESOLUTION);
		builder.append(mResolution);
		builder.append(BODY_LIVE_STAT_LANGUAGE);
		builder.append(mLanguage);
		httpRequest(builder.toString());
	}

	/**
	 * 信息层显示cat=34*
	 */
	public void cat34(
			@NonNull  String tagId,
			@NonNull String dgId,
			@Nullable String linkId,
			@Nullable String refer,
			@Nullable String type) {
		if(disallowStat()){
			return;
		}
		StringBuilder builder = new StringBuilder();
		builder.append( LIVE_URL_STAT_HEADER);
		builder.append( BODY_LIVE_STAT_CAT);
		builder.append(34);
		builder.append(BODY_LIVE_STAT_SERVICE);
		builder.append(mService);
		builder.append(BODY_LIVE_STAT_VERSION );
		builder.append(mVersion);
		builder.append( BODY_LIVE_STAT_RANDOM);
		builder.append(System.currentTimeMillis());
		builder.append(BODY_LIVE_STAT_VIDEO);
		builder.append(mVideoId);
		builder.append(BODY_LIVE_STAT_CHANNEL);
		builder.append(mChannel);
		builder.append(BODY_LIVE_STAT_PROJECT);
		builder.append(mProjectId);
		builder.append(BODY_LIVE_STAT_TAG);
		builder.append(tagId);
		builder.append(BODY_LIVE_STAT_DG);
		builder.append(dgId);
		builder.append(BODY_LIVE_STAT_BRAND);
		builder.append(mBrand);
		builder.append(BODY_LIVE_STAT_TYPE);
		builder.append(type);
		builder.append(APP_KEY);
		builder.append(mProjectId);
		builder.append(BODY_LIVE_STAT_LINK);
		builder.append(linkId);
		builder.append(BODY_LIVE_STAT_USER_AGNET);
		builder.append(mUserAgnet);
		builder.append(BODY_LIVE_STAT_REFER);
		builder.append(refer);
		builder.append(BODY_LIVE_STAT_CLIENT);
		builder.append(mClientId);
		builder.append(BODY_LIVE_STAT_RESOLUTION );
		builder.append(mResolution);
		builder.append(BODY_LIVE_STAT_LANGUAGE);
		builder.append(mLanguage);
		httpRequest(builder.toString());
	}

	//广告曝光
	public void cat53(@Nullable String refer) {
		if(disallowStat()){
			return;
		}
		StringBuilder builder = new StringBuilder();
		builder.append( LIVE_URL_STAT_HEADER);
		builder.append( BODY_LIVE_STAT_CAT);
		builder.append(53);
		builder.append(BODY_LIVE_STAT_SERVICE);
		builder.append(mService);
		builder.append(BODY_LIVE_STAT_VERSION );
		builder.append(mVersion);
		builder.append( BODY_LIVE_STAT_RANDOM);
		builder.append(System.currentTimeMillis());
		builder.append(BODY_LIVE_STAT_VIDEO);
		builder.append(mVideoId);
		builder.append(BODY_LIVE_STAT_CHANNEL);
		builder.append(mChannel);
		builder.append(BODY_LIVE_STAT_PROJECT);
		builder.append(mProjectId);
		builder.append(APP_KEY);
		builder.append(mProjectId);
		builder.append(BODY_LIVE_STAT_USER_AGNET);
		builder.append(mUserAgnet);
		builder.append(BODY_LIVE_STAT_REFER);
		builder.append(refer);
		builder.append(BODY_LIVE_STAT_CLIENT);
		builder.append(mClientId);
		builder.append(BODY_LIVE_STAT_RESOLUTION );
		builder.append(mResolution);
		builder.append(BODY_LIVE_STAT_LANGUAGE);
		builder.append(mLanguage);
		httpRequest(builder.toString());
	}

	//广告点击
	public void cat54( @Nullable  String refer) {
		if(disallowStat()){
			return;
		}
		StringBuilder builder = new StringBuilder();
		builder.append( LIVE_URL_STAT_HEADER);
		builder.append( BODY_LIVE_STAT_CAT);
		builder.append(54);
		builder.append(BODY_LIVE_STAT_SERVICE);
		builder.append(mService);
		builder.append(BODY_LIVE_STAT_VERSION );
		builder.append(mVersion);
		builder.append( BODY_LIVE_STAT_RANDOM);
		builder.append(System.currentTimeMillis());
		builder.append(BODY_LIVE_STAT_VIDEO);
		builder.append(mVideoId);
		builder.append(BODY_LIVE_STAT_CHANNEL);
		builder.append(mChannel);
		builder.append(BODY_LIVE_STAT_PROJECT);
		builder.append(mProjectId);
		builder.append(APP_KEY);
		builder.append(mProjectId);
		builder.append(BODY_LIVE_STAT_USER_AGNET);
		builder.append(mUserAgnet);
		builder.append(BODY_LIVE_STAT_REFER);
		builder.append(refer);
		builder.append(BODY_LIVE_STAT_CLIENT);
		builder.append(mClientId);
		builder.append(BODY_LIVE_STAT_RESOLUTION );
		builder.append(mResolution);
		builder.append(BODY_LIVE_STAT_LANGUAGE);
		builder.append(mLanguage);
		httpRequest(builder.toString());
	}

	public void cat20(
			@NonNull String tagId,
			@NonNull String dgId,
			@Nullable String refer,
			@Nullable String type) {
		if(disallowStat()){
			return;
		}
		StringBuilder builder = new StringBuilder();
		builder.append( LIVE_URL_STAT_HEADER);
		builder.append( BODY_LIVE_STAT_CAT);
		builder.append(20);
		builder.append(BODY_LIVE_STAT_SERVICE);
		builder.append(mService);
		builder.append(BODY_LIVE_STAT_VERSION);
		builder.append(mVersion);
		builder.append(BODY_LIVE_STAT_RANDOM );
		builder.append(System.currentTimeMillis());
		builder.append(BODY_LIVE_STAT_VIDEO);
		builder.append(mVideoId);
		builder.append(BODY_LIVE_STAT_CHANNEL);
		builder.append(mChannel);
		builder.append( BODY_LIVE_STAT_PROJECT);
		builder.append( mProjectId);
		builder.append(BODY_LIVE_STAT_TAG);
		builder.append(tagId);
		builder.append(BODY_LIVE_STAT_DG);
		builder.append(dgId);
		builder.append(BODY_LIVE_STAT_BRAND);
		builder.append(mBrand);
		builder.append(BODY_LIVE_STAT_TYPE);
		builder.append(type);
		builder.append(APP_KEY);
		builder.append(mProjectId);
		builder.append(BODY_LIVE_STAT_DURATION);
		builder.append(DURATION);
		builder.append(BODY_LIVE_STAT_USER_AGNET);
		builder.append(mUserAgnet);
		builder.append(BODY_LIVE_STAT_REFER );
		builder.append(refer);
		builder.append(BODY_LIVE_STAT_CLIENT);
		builder.append(mClientId);
		builder.append(BODY_LIVE_STAT_RESOLUTION);
		builder.append(mResolution);
		builder.append(BODY_LIVE_STAT_LANGUAGE);
		builder.append(mLanguage);
		httpRequest(builder.toString());
	}

	/**
	 *  信息层外链点击cat=11
	 */
	public void cat11(String tagId, String dgId, String linkId, String refer,
					  String type) {
		if(disallowStat()){
			return;
		}
		StringBuilder builder = new StringBuilder();
		builder.append( LIVE_URL_STAT_HEADER);
		builder.append( BODY_LIVE_STAT_CAT);
		builder.append(11);
		builder.append(BODY_LIVE_STAT_SERVICE );
		builder.append(mService);
		builder.append(BODY_LIVE_STAT_VERSION);
		builder.append(mVersion);
		builder.append(BODY_LIVE_STAT_RANDOM);
		builder.append(System.currentTimeMillis());
		builder.append(BODY_LIVE_STAT_VIDEO);
		builder.append(mVideoId);
		builder.append(BODY_LIVE_STAT_CHANNEL);
		builder.append(mChannel);
		builder.append(BODY_LIVE_STAT_PROJECT);
		builder.append(mProjectId);
		builder.append(BODY_LIVE_STAT_TAG);
		builder.append(tagId);
		builder.append(BODY_LIVE_STAT_DG);
		builder.append(dgId);
		builder.append(BODY_LIVE_STAT_BRAND);
		builder.append(BRAND);
		builder.append(BODY_LIVE_STAT_TYPE);
		builder.append(type);
		builder.append(APP_KEY);
		builder.append(mProjectId);
		builder.append(BODY_LIVE_STAT_LINK);
		builder.append(linkId);
		builder.append(BODY_LIVE_STAT_USER_AGNET);
		builder.append(mUserAgnet);
		builder.append(BODY_LIVE_STAT_REFER);
		builder.append(refer);
		builder.append(BODY_LIVE_STAT_CLIENT);
		builder.append(mClientId);
		builder.append(BODY_LIVE_STAT_RESOLUTION);
		builder.append(mResolution);
		builder.append( BODY_LIVE_STAT_LANGUAGE);
		builder.append( mLanguage);
		httpRequest(builder.toString());
	}


	public void cat47(String tagId, String dgId, String linkId, String refer) {
		if(disallowStat()){
			return;
		}
		StringBuilder builder = new StringBuilder();
		builder.append( LIVE_URL_STAT_HEADER);
		builder.append( BODY_LIVE_STAT_CAT);
		builder.append(47);
		builder.append(BODY_LIVE_STAT_SERVICE );
		builder.append(mService);
		builder.append(BODY_LIVE_STAT_VERSION);
		builder.append(mVersion);
		builder.append(BODY_LIVE_STAT_RANDOM);
		builder.append(System.currentTimeMillis());
		builder.append(BODY_LIVE_STAT_VIDEO);
		builder.append(mVideoId);
		builder.append(BODY_LIVE_STAT_CHANNEL);
		builder.append(mChannel);
		builder.append(BODY_LIVE_STAT_PROJECT);
		builder.append(mProjectId);
		builder.append(BODY_LIVE_STAT_TAG);
		builder.append(tagId);
		builder.append(BODY_LIVE_STAT_DG);
		builder.append(dgId);
		builder.append(APP_KEY);
		builder.append(mProjectId);
		builder.append(BODY_LIVE_STAT_LINK);
		builder.append(linkId);
		builder.append(BODY_LIVE_STAT_USER_AGNET);
		builder.append(mUserAgnet);
		builder.append(BODY_LIVE_STAT_REFER);
		builder.append(refer);
		builder.append(BODY_LIVE_STAT_CLIENT);
		builder.append(mClientId);
		builder.append(BODY_LIVE_STAT_RESOLUTION);
		builder.append(mResolution);
		builder.append( BODY_LIVE_STAT_LANGUAGE);
		builder.append( mLanguage);
		httpRequest(builder.toString());
	}

	// 主播点击灵动球cat=31
	public void cat31(
			String refer, String type,String x,String y) {
		if(disallowStat()){
			return;
		}
		StringBuilder builder = new StringBuilder();
		builder.append( LIVE_URL_STAT_HEADER);
		builder.append( BODY_LIVE_STAT_CAT );
		builder.append(31);
		builder.append(BODY_LIVE_STAT_SERVICE);
		builder.append(mService);
		builder.append(BODY_LIVE_STAT_VERSION);
		builder.append(mVersion);
		builder.append(BODY_LIVE_STAT_RANDOM);
		builder.append(System.currentTimeMillis());
		builder.append( BODY_LIVE_STAT_VIDEO);
		builder.append( mVideoId);
		builder.append( BODY_LIVE_STAT_CHANNEL);
		builder.append( mChannel);
		builder.append( BODY_LIVE_STAT_PROJECT);
		builder.append( mProjectId);
		builder.append( BODY_LIVE_STAT_BRAND);
		builder.append( mBrand);
		builder.append( BODY_LIVE_STAT_TYPE);
		builder.append( type);
		builder.append( BODY_LIVE_STAT_X );
		builder.append( x);
		builder.append(  BODY_LIVE_STAT_Y);
		builder.append( y);
		builder.append( BODY_LIVE_STAT_USER_AGNET);
		builder.append( mUserAgnet);
		builder.append( BODY_LIVE_STAT_REFER);
		builder.append( refer);
		builder.append( BODY_LIVE_STAT_CLIENT);
		builder.append( mClientId);
		builder.append( BODY_LIVE_STAT_RESOLUTION);
		builder.append( mResolution);
		builder.append( BODY_LIVE_STAT_LANGUAGE );
		builder.append( mLanguage);
		httpRequest(builder.toString());
	}

	// 主播拖动灵动球cat=32
	public void cat32(
			String refer, String type) {
		if(disallowStat()){
			return;
		}
		StringBuilder builder = new StringBuilder();
		builder.append( LIVE_URL_STAT_HEADER);
		builder.append( BODY_LIVE_STAT_CAT);
		builder.append(32);
		builder.append(BODY_LIVE_STAT_SERVICE);
		builder.append(mService);
		builder.append(BODY_LIVE_STAT_VERSION);
		builder.append(mVersion);
		builder.append(BODY_LIVE_STAT_RANDOM);
		builder.append(System.currentTimeMillis());
		builder.append(BODY_LIVE_STAT_VIDEO);
		builder.append(mVideoId);
		builder.append(BODY_LIVE_STAT_CHANNEL);
		builder.append(mChannel);
		builder.append(BODY_LIVE_STAT_PROJECT);
		builder.append(mProjectId);
		builder.append(BODY_LIVE_STAT_BRAND);
		builder.append(mBrand);
		builder.append(BODY_LIVE_STAT_TYPE);
		builder.append(type);
		builder.append(BODY_LIVE_STAT_USER_AGNET);
		builder.append(mUserAgnet);
		builder.append(BODY_LIVE_STAT_REFER );
		builder.append(refer);
		builder.append(BODY_LIVE_STAT_CLIENT);
		builder.append(mClientId);
		builder.append(BODY_LIVE_STAT_RESOLUTION);
		builder.append(mResolution);
		builder.append(BODY_LIVE_STAT_LANGUAGE);
		builder.append(mLanguage);
		httpRequest(builder.toString());
	}

	// 连接MQTT cat=33//
	public void cat33(String refer) {
		if(disallowStat()){
			return;
		}
		// 获取时间戳
		StringBuilder builder = new  StringBuilder();
		builder.append( LIVE_URL_STAT_HEADER);
		builder.append( BODY_LIVE_STAT_CAT);
		builder.append(33);
		builder.append(BODY_LIVE_STAT_SERVICE);
		builder.append(mService);
		builder.append(BODY_LIVE_STAT_VERSION );
		builder.append(mVersion);
		builder.append(BODY_LIVE_STAT_RANDOM);
		builder.append(System.currentTimeMillis());
		builder.append(BODY_LIVE_STAT_VIDEO);
		builder.append(mVideoId);
		builder.append(BODY_LIVE_STAT_CHANNEL);
		builder.append(mChannel);
		builder.append(BODY_LIVE_STAT_PROJECT);
		builder.append(mProjectId);
		builder.append(BODY_LIVE_STAT_USER_AGNET );
		builder.append(mUserAgnet);
		builder.append(BODY_LIVE_STAT_REFER);
		builder.append(refer);
		builder.append( BODY_LIVE_STAT_CLIENT);
		builder.append( mClientId);
		builder.append( BODY_LIVE_STAT_RESOLUTION);
		builder.append( mResolution);
		builder.append(  BODY_LIVE_STAT_LANGUAGE );
		builder.append( mLanguage );
		String url = builder.toString();
		httpRequest(url);
	}

	protected static IRequestConnect requestConnect = RequestFactory.initConnect(RequestFactory.HttpPlugin.OK_HTTP);
	private void httpRequest(String url){
		Request request = HttpRequest.get(url);
		requestConnect.connect(request, new IRequestHandler.RequestHandlerAdapter() {
			@Override
			public void requestFinish(Request request, IResponse response) {
				if(response.isSuccess()) {
					VenvyLog.v("测试接口正常");
				}else {
					VenvyLog.e("测试接口失败");
				}

			}
		});

	}

}
