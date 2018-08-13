package com.backaudio.videochat.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.backaudio.videochat.DemoCache;
import com.backaudio.videochat.R;
import com.backaudio.videochat.constant.CallStateEnum;
import com.netease.nim.avchatkit.AVChatKit;
import com.netease.nim.avchatkit.activity.AVChatActivity;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.model.AVChatCameraCapturer;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatSurfaceViewRenderer;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    //render
    private AVChatSurfaceViewRenderer smallRender;
    private AVChatSurfaceViewRenderer largeRender;

    private LinearLayout largeSizePreviewLayout;
    private Button btnLogin1, btnLogin2;
    private Button btnCallVideo2, btnCallAudio2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();


    }

    private void initView() {
        btnLogin1 = findViewById(R.id.login1);
        btnLogin2 = findViewById(R.id.login2);

        btnCallVideo2 = findViewById(R.id.call_video2);
        btnCallAudio2 = findViewById(R.id.call_audio2);

        btnLogin2.setOnClickListener(this);
        btnLogin1.setOnClickListener(this);
        btnCallVideo2.setOnClickListener(this);
        btnCallAudio2.setOnClickListener(this);
    }

    public void doLogin1() {
        final String account1 = "scj";
        final String token1 = "12345678";
        LoginInfo info = new LoginInfo(account1, token1); // config...
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        Log.i(TAG, "login success");
                        DemoCache.setAccount(account1);
                        Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == 302 || code == 404) {
                            Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "登录失败: " + code, Toast.LENGTH_SHORT).show();
                        }
                        Log.e(TAG, "login failed");
                    }

                    @Override
                    public void onException(Throwable exception) {
                        Toast.makeText(LoginActivity.this, R.string.login_exception, Toast.LENGTH_LONG).show();
                        Log.i(TAG, "login Exception");
                    }
                    // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
                };
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(callback);
    }

    public void doLogin2() {
        final String account2 = "scr";
        final String token2 = "123456";
        LoginInfo info = new LoginInfo(account2, token2); // config...
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        Log.i(TAG, "login success");
                        DemoCache.setAccount(account2);
                        Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == 302 || code == 404) {
                            Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "登录失败: " + code, Toast.LENGTH_SHORT).show();
                        }
                        Log.e(TAG, "login failed");
                    }

                    @Override
                    public void onException(Throwable exception) {
                        Toast.makeText(LoginActivity.this, R.string.login_exception, Toast.LENGTH_LONG).show();
                        Log.i(TAG, "login Exception");
                    }
                    // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
                };
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(callback);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login1:
                doLogin1();
                break;
            case R.id.login2:
                doLogin2();
                break;
            case R.id.call_video2:
                startAudioVideoCall_1(AVChatType.VIDEO);
                break;
            case R.id.call_audio2:
                startAudioVideoCall_1(AVChatType.AUDIO);
                break;
            default:
                break;

        }
    }


    /************************ 音视频通话 ***********************/

    public void startAudioVideoCall_1(AVChatType avChatType) {
        AVChatKit.outgoingCall(this, getAccount2(), UserInfoHelper.getUserDisplayName(getAccount2()), avChatType.getValue(), AVChatActivity.FROM_INTERNAL);
    }


    private String getAccount1() {
        final String account1 = "scj";
        return account1;
    }

    private String getAccount2() {
        final String account2 = "scr";
        return account2;
    }

}
