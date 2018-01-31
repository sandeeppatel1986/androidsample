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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sandeep on 6/13/2017.
 * OTP Generation Screens
 */

public class OTPFragment extends Fragment {
    private View rootView;
    private Button btnOK;
    private EditText etInserisci_pin;
    private Context context;
    private int otpCount = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_otp, container, false);
        init();
        setListners();

        return rootView;
    }

    /**
     * initialize all the views here
     */
    private void init() {
        btnOK = (Button) rootView.findViewById(R.id.btnOK);
        etInserisci_pin = (EditText) rootView.findViewById(R.id.etInserisci_pin);
    }

    private void setListners() {
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otpCount == 3) { //if user regenerate 3 times its blocked
                    Toast.makeText(context, " maximum allowed pin reached!", Toast.LENGTH_SHORT).show();
                    navigateToScreen();

                } else {
                    /*if (seed.isEmpty()) {
                        Toast.makeText(context, "Enter PIN!", Toast.LENGTH_SHORT).show();
                    } else*/
                    {
                        String seed64 = "3132333435363738393031323334353637383930"
                                + "3132333435363738393031323334353637383930"
                                + "3132333435363738393031323334353637383930" + "31323334";

                        String str_date = "01-01-2000";
                        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

                        long counter = System.currentTimeMillis() / 30000L;
                        try {
                            Date date = formatter.parse(str_date);
                            etInserisci_pin.setText(TOTP.generateTOTP(seed64, String.valueOf(counter), "6", "sha512"));
                            btnOK.setText(getString(R.string.rigenera));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }
                otpCount++;
            }
        });
    }

    /**
     * navigate to other screen
     */
    private void navigateToScreen() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, new RegistrationFragment()).commit();
    }


}