package com.domainname.android.otpgenerator;

import android.app.Application;

import com.domainname.android.otpgenerator.util.SharedPreferenceUtil;

/**
 * Created by sanddep on 6/13/2017.
 */

public class OTPGeneratorApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferenceUtil.init(this);
    }
}
