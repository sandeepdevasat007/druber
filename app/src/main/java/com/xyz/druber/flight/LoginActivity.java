package com.xyz.druber.flight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dji.ux.druber.R;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.xyz.druber.flight.network.service.UserAPIService;
import com.xyz.druber.flight.utils.NetworkUtils;
import com.xyz.druber.flight.utils.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 Created by SANDEEP
 **/


public class LoginActivity  extends AppCompatActivity{

    @BindView(R.id.email_txt)
    EditText email_txt;
    @BindView(R.id.pwd_txt)
    EditText pwd_txt;
    @BindView(R.id.login_txt)
    ShimmerTextView login_txt;
    @BindView(R.id.signup_txt)
    TextView signup_txt;
    @BindView(R.id.forgot_pwd_txt)
    TextView forgot_pwd_txt;

    private Shimmer shimmer;
    private boolean isStart = false;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Window window = this.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        unbinder = ButterKnife.bind(this);
        UserAPIService userAPIService = NetworkUtils.provideUserAPIService(this, "");
        FrameLayout progress_bar = findViewById(R.id.progress_bar);
        shimmer = new Shimmer()
                .setDirection(Shimmer.ANIMATION_DIRECTION_LTR)
                .setDuration(3000)
                .setStartDelay(2000);
        if (!isStart)
            shimmer.start(login_txt);
    }

    @OnClick(R.id.login_txt)
    public void login() {
        if (TextUtils.isEmpty(email_txt.getText().toString().trim()))
            ScreenUtils.getSneaker(this, "Email can't be empty!");
        else if (TextUtils.isEmpty(pwd_txt.getText().toString().trim()))
            ScreenUtils.getSneaker(this, "Password can't be empty!");
        else if (!email_txt.getText().toString().trim().contains("@"))
            ScreenUtils.getSneaker(this, "Not a valid email");
        else {
            doLogin();
        }
    }

    @OnClick(R.id.signup_txt)
    public void signUp() {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    private void doLogin() {
        String email = email_txt.getText().toString().trim();
        String password = pwd_txt.getText().toString().trim();
                startActivity(new Intent(LoginActivity.this, JobsActivity.class));
                finish();
    }




    @Override
    protected void onResume() {
        super.onResume();
        if (isStart) {
            isStart = false;
            shimmer.start(login_txt);
        }
    }

    @Override
    protected void onPause() {
        if (!isStart) {
            isStart = true;
            shimmer.cancel();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}


