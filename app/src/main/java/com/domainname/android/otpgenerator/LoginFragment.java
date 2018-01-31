package com.domainname.android.otpgenerator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.domainname.android.otpgenerator.util.Constants;
import com.domainname.android.otpgenerator.util.SharedPreferenceUtil;

/**
 * Created by Sandeep on 6/13/2017.
 * Login Screens
 */

public class LoginFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private Button btnGenerateOTP;
    private TextView txtUserName;
    private Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_login, container, false);
        init();
        setListners();
        if (SharedPreferenceUtil.getBoolean(Constants.IS_LOGGED_IN, false)) {
            btnLogout.setVisibility(View.VISIBLE);
            txtUserName.setVisibility(View.VISIBLE);
            txtUserName.setText(SharedPreferenceUtil.getString(Constants.USERNAME, ""));
        } else {
            btnLogout.setVisibility(View.INVISIBLE);
            txtUserName.setVisibility(View.INVISIBLE);
        }
        return rootView;
    }


    /**
     * initialize all the views here
     */
    private void init() {
        txtUserName = (TextView) rootView.findViewById(R.id.txtUserName);
        btnGenerateOTP = (Button) rootView.findViewById(R.id.btnGenerateOTP);
        btnLogout = (Button) rootView.findViewById(R.id.btnLogout);
    }

    private void setListners() {
        btnLogout.setOnClickListener(this);
        btnGenerateOTP.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGenerateOTP:
                navigateToScreen(1);
                break;
            case R.id.btnLogout:
                navigateToScreen(2);
                break;
        }
    }

    /**
     * navigate user to other screen according to option
     * @param type
     */
    private void navigateToScreen(int type) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (type == 1) {
            if (SharedPreferenceUtil.getBoolean(Constants.IS_LOGGED_IN, false)) {

                fragmentTransaction.replace(R.id.container, new OTPFragment()).addToBackStack(null).commit();
            } else {
                fragmentTransaction.replace(R.id.container, new RegistrationFragment()).commit();
            }
        } else {
            SharedPreferenceUtil.putValue(Constants.USERNAME, "");
            SharedPreferenceUtil.putValue(Constants.PASSWORD, "");
            SharedPreferenceUtil.putValue(Constants.IS_LOGGED_IN, false);

            fragmentTransaction.replace(R.id.container, new LoginFragment()).commit();
        }
    }
}
