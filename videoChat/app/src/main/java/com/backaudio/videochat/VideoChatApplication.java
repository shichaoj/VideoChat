package com.backaudio.videochat;


import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.backaudio.videochat.login.LoginActivity;
import com.netease.nim.avchatkit.AVChatKit;
import com.netease.nim.avchatkit.config.AVChatOptions;
import com.netease.nim.avchatkit.model.ITeamDataProvider;
import com.netease.nim.avchatkit.model.IUserInfoProvider;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.UIKitOptions;
import com.netease.nim.uikit.api.wrapper.NimUserInfoProvider;
import com.netease.nim.uikit.business.contact.core.query.PinYin;
import com.netease.nim.uikit.business.contact.core.util.ContactHelper;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderThumbBase;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.netease.nimlib.sdk.util.NIMUtil;

import java.io.IOException;

public class VideoChatApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DemoCache.setContext(this);
        NIMClient.init(this, loginInfo(), options());


        // 以下逻辑只在主进程初始化时执行
        if (NIMUtil.isMainProcess(this)) {

//             init pinyin
            PinYin.init(this);
            PinYin.validate();
            // 初始化UIKit模块
            initUIKit();
            // 初始化消息提醒
//            NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
            // 云信sdk相关业务初始化
//            NIMInitManager.getInstance().init(true);
            // 初始化音视频模块
            initAVChatKit();
        }
    }

    private void initAVChatKit() {
        AVChatOptions avChatOptions = new AVChatOptions(){
            @Override
            public void logout(Context context) {
//                MainActivity.logout(context, true);
            }
        };
        avChatOptions.entranceActivity = LoginActivity.class;
        avChatOptions.notificationIconRes = R.drawable.ic_stat_notify_msg;
        AVChatKit.init(avChatOptions);

        // 初始化日志系统
//        LogHelper.init();
        // 设置用户相关资料提供者
        AVChatKit.setUserInfoProvider(new IUserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return NimUIKit.getUserInfoProvider().getUserInfo(account);
            }

            @Override
            public String getUserDisplayName(String account) {
                return UserInfoHelper.getUserDisplayName(account);
            }
        });
        // 设置群组数据提供者
        AVChatKit.setTeamDataProvider(new ITeamDataProvider() {
            @Override
            public String getDisplayNameWithoutMe(String teamId, String account) {
                return TeamHelper.getDisplayNameWithoutMe(teamId, account);
            }

            @Override
            public String getTeamMemberDisplayName(String teamId, String account) {
                return TeamHelper.getTeamMemberDisplayName(teamId, account);
            }
        });
    }

    // 如果返回值为 null，则全部使用默认参数。
    private SDKOptions options() {
        SDKOptions options = new SDKOptions();

        // 配置 APP 保存图片/语音/文件/log等数据的目录
        options.sdkStorageRootPath = getAppCacheDir(this) + "/nim"; // 可以不设置，那么将采用默认路径

        // 配置数据库加密秘钥
        options.databaseEncryptKey = "NETEASE";

        // 配置是否需要预下载附件缩略图
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小
        options.thumbnailSize = MsgViewHolderThumbBase.getImageMaxEdge();


        // 在线多端同步未读数
        options.sessionReadAck = true;

        // 动图的缩略图直接下载原图
        options.animatedImageThumbnailEnabled = true;

        // 采用异步加载SDK
        options.asyncInitSDK = true;

        // 是否是弱IM场景
        options.reducedIM = false;

        // 是否检查manifest 配置，调试阶段打开，调试通过之后请关掉
        options.checkManifestConfig = false;

        // 是否启用群消息已读功能，默认关闭
        options.enableTeamMsgAck = true;


        // 打开消息撤回未读数-1的开关
        options.shouldConsiderRevokedMessageUnreadCount = true;

        return options;
    }

    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
    private LoginInfo loginInfo() {
        return null;
    }

    private void initUIKit() {
        // 初始化
        NimUIKit.init(this, buildUIKitOptions());

    }

    private UIKitOptions buildUIKitOptions() {
        UIKitOptions options = new UIKitOptions();
        // 设置app图片/音频/日志等缓存目录
        options.appCacheDir = getAppCacheDir(this) + "/app";
        return options;
    }

    /**
     * 配置 APP 保存图片/语音/文件/log等数据的目录
     * 这里示例用SD卡的应用扩展存储目录
     */
    static String getAppCacheDir(Context context) {
        String storageRootPath = null;
        try {
            // SD卡应用扩展存储区(APP卸载后，该目录下被清除，用户也可以在设置界面中手动清除)，请根据APP对数据缓存的重要性及生命周期来决定是否采用此缓存目录.
            // 该存储区在API 19以上不需要写权限，即可配置 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="18"/>
            if (context.getExternalCacheDir() != null) {
                storageRootPath = context.getExternalCacheDir().getCanonicalPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(storageRootPath)) {
            // SD卡应用公共存储区(APP卸载后，该目录不会被清除，下载安装APP后，缓存数据依然可以被加载。SDK默认使用此目录)，该存储区域需要写权限!
            storageRootPath = Environment.getExternalStorageDirectory() + "/" + "videoChat";
        }

        return storageRootPath;
    }



}
