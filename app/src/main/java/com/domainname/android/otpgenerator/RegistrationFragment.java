package com.domainname.android.otpgenerator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.domainname.android.otpgenerator.util.Constants;
import com.domainname.android.otpgenerator.util.SharedPreferenceUtil;

/**
 * Created by Sandeep on 6/13/2017.
 * Registration Screens
 */

public class RegistrationFragment extends Fragment {
    private View rootView;
    private EditText etUserName;
    private EditText etPassword;
    private Button btnLogIn;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_registration, container, false);
        init();
        setListners();
        return rootView;
    }

    /**
     * initialize all the views here
     */
    private void init() {
        etPassword = (EditText) rootView.findViewById(R.id.etPassword);
        etUserName = (EditText) rootView.findViewById(R.id.etUserName);
        btnLogIn = (Button) rootView.findViewById(R.id.btnLogIn);
    }

    /**
     * all he listners are set here
     */
    private void setListners() {
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidDetails()) {
                    SharedPreferenceUtil.putValue(Constants.USERNAME, etUserName.getText().toString().trim());
                    SharedPreferenceUtil.putValue(Constants.PASSWORD, etPassword.getText().toString().trim());
                    SharedPreferenceUtil.putValue(Constants.IS_LOGGED_IN, true);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, new LoginFragment()).commit();
                }
            }
        });
    }

    /**
     * Check the validations
     *
     * @return
     */
    private boolean isValidDetails() {
        if (etUserName.getText().toString().trim().isEmpty()) {
            Toast.makeText(context, "Enter username!", Toast.LENGTH_SHORT).show();
            etUserName.requestFocus();
            return false;
        } else if (etPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(context, "Enter password!", Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

}
