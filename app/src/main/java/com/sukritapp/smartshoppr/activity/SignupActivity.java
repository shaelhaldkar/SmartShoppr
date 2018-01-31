package com.sukritapp.smartshoppr.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.listner.OnSukritResponseListner;
import com.sukritapp.smartshoppr.sms.SmsListener;
import com.sukritapp.smartshoppr.sms.SmsReceiver;
import com.sukritapp.smartshoppr.util.AppLog;
import com.sukritapp.smartshoppr.util.Constant;
import com.sukritapp.smartshoppr.util.SmartShopprUtils;
import com.sukritapp.smartshoppr.webservice.WebRetriveDataTask;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends Activity implements View.OnClickListener, OnSukritResponseListner, SmsListener {
    private final String TAG = AppLog.getClassName();
    private EditText mEdTvName = null;
    private EditText mEdTvEmail = null;
    private EditText mEdTvPhoneNumber = null;
    private EditText mEdTvPin = null;
    private EditText mEdTvConfirmPin = null;
    private Button mBtnSubmit = null;
    private TextView mTxtVwSkip = null;
    private OnSukritResponseListner onSukritResponseListner = null;
    private String mMobileNumber = null;
    private EditText mEdTvOTP;
    SmsReceiver smsReceiver = null;
    SmsListener smsListener = null;
    private int MY_PERMISSIONS_REQUEST_READ_MESSAGE = 100;
    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppLog.enter(TAG, AppLog.getMethodName());
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.content_signup);

        mEdTvName = (EditText) findViewById(R.id.edTvName);
        mEdTvEmail = (EditText) findViewById(R.id.edTvEmail);
        mEdTvPhoneNumber = (EditText) findViewById(R.id.edTvContactNo);
        mEdTvPin = (EditText) findViewById(R.id.edTvpin);
        mEdTvConfirmPin = (EditText) findViewById(R.id.edTvConfirmPin);
        mBtnSubmit = (Button) findViewById(R.id.btnSubmit);
        mTxtVwSkip = (TextView) findViewById(R.id.txtVw_Skip);
        onSukritResponseListner = this;
        mBtnSubmit.setOnClickListener(this);
        mTxtVwSkip.setOnClickListener(this);
        smsListener = this;
        smsReceiver = new SmsReceiver();
        smsReceiver.bindListener(smsListener);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showPermission();
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            AppLog.info(TAG, "permission already granted");
            registerSMSReceiver();
        } else {
            registerSMSReceiver();
            AppLog.info(TAG, "permission check else block ");

        }

        AppLog.exit(TAG, AppLog.getMethodName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppLog.info(TAG, AppLog.getMethodName());


    }

    @Override
    protected void onPause() {
        super.onPause();
        AppLog.info(TAG, AppLog.getMethodName());

    }

    @Override
    public void onClick(View v) {
        AppLog.enter(TAG, AppLog.getMethodName());
        if (v.getId() == R.id.btnSubmit) {
            validateInput();
        } else if (v.getId() == R.id.txtVw_Skip) {
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        }
        AppLog.exit(TAG, AppLog.getMethodName());
    }

    private void validateInput() {

        AppLog.enter(TAG, AppLog.getMethodName());

        String name = mEdTvName.getText().toString();
        String email = mEdTvEmail.getText().toString();
        String contactNo = mEdTvPhoneNumber.getText().toString();

        if (name.equalsIgnoreCase("")) {
            mEdTvName.setError("Please Enter Name");
            mEdTvName.requestFocus();
        } else if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            mEdTvEmail.setError("Please Enter Valid Email ID");
            mEdTvEmail.requestFocus();
        } else if (!(Patterns.PHONE.matcher(contactNo).matches()) || (contactNo.length() < 10)) {
            mEdTvPhoneNumber.setError("Please Enter Valid Number");
            mEdTvPhoneNumber.requestFocus();
        } else if (mEdTvPin.getText().toString().isEmpty()) {
            mEdTvPin.setError("Please Enter Pin");
            mEdTvPin.requestFocus();
        } else if (mEdTvConfirmPin.getText().toString().isEmpty()) {
            mEdTvConfirmPin.setError("Confirm Pin");
            mEdTvConfirmPin.requestFocus();
        } else if (!mEdTvConfirmPin.getText().toString().equals(mEdTvPin.getText().toString())) {
            mEdTvConfirmPin.setError("Pin not matches");
            mEdTvConfirmPin.requestFocus();
        } else {
            String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            mMobileNumber = mEdTvPhoneNumber.getText().toString();

            new WebRetriveDataTask().registerUser(this, onSukritResponseListner, Constant.listtype.SIGNUP, Constant.URL_BASE + Constant.URL_SIGNUP, name, email, contactNo, deviceID,mEdTvPin.getText().toString());
            showProgressBar(SignupActivity.this);
            // submitRequest(contactNo, address, customerName, preferedtime);
        }
        AppLog.exit(TAG, AppLog.getMethodName());

    }

//    public void showDialogforOTP() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//// Add the buttons
//        LayoutInflater inflater = this.getLayoutInflater();
//        View OTPdailogView = inflater.inflate(R.layout.otp_dailog_layout, null);
//        final EditText edtxtOTP = (EditText) OTPdailogView.findViewById(R.id.edtvOTP);
//        // Inflate and set the layout for the dialog
//        // Pass null as the parent view because its going in the dialog layout
//        builder.setView(OTPdailogView);
//        builder.setPositiveButton("OK",null);
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // User cancelled the dialog
//            }
//        });
//        final AlertDialog dialog = builder.create();
//        dialog.show();
//        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (edtxtOTP.getText().toString().equalsIgnoreCase("12345")) {
//                    dialog.dismiss();
//                } else {
//                   edtxtOTP.setError("you entered wrong OTP");
//                }
//
//                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
//            }
//        });
//    }

    @Override
    public void onResponseReceived(String error,Constant.listtype tag, String response) {
        AppLog.info(TAG, "------");

        AppLog.info("" + tag,""+ response);
        progressDialog.dismiss();
//            {"message":"Success.","flag":"1"}\
        if(response!=null) {
            if (Constant.listtype.SIGNUP.toString().equalsIgnoreCase(tag.toString())) {
                // SMS-SHOOT-ID/washomart58f4c7187c55f{"message":"Success.","OTP":"5734","flag":"1"}
                try {
                    int startIndex = response.indexOf("{");
                    response = response.substring(startIndex);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            try {
                JSONObject jsonObject = new JSONObject(response);
                if (Constant.listtype.SIGNUP.toString().equalsIgnoreCase("" + tag) && jsonObject.getString("flag").equalsIgnoreCase("1")) {
                    showOTPDialog(this, null);
                } else if (tag.toString().contains(Constant.listtype.SENDOTP.toString())) {
                    if (jsonObject.getString("message").equalsIgnoreCase("user successfully register")) {
                        showDialog(this, "successfully registered !!");
//                    startActivity(new Intent(SignupActivity.this, LoginScreen.class));
//                    finish();
                    } else {
                        SmartShopprUtils.getInstance().showInfoDialog(SignupActivity.this, "Some thing went wrong");
                    }

                } else {
                    SmartShopprUtils.getInstance().showInfoDialog(this, jsonObject.getString("message"));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            SmartShopprUtils.getInstance().showErrorDialog(this, "Some thing went wrong");
        }
    }

    public void showDialog(final Activity context, String msg) {
        AppLog.enter(TAG, AppLog.getMethodName());
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(SignupActivity.this, LoginScreen.class));
                        finish();
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }

                })

                .show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignupActivity.this, LoginScreen.class));
        finish();
    }

    public void showOTPDialog(final Activity context, String msg) {
        AppLog.enter(TAG, AppLog.getMethodName());

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_otp, null);
        dialogBuilder.setView(dialogView);

        mEdTvOTP = (EditText) dialogView.findViewById(R.id.edtv_otp);

        dialogBuilder.setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (mEdTvOTP.getText().length() > 0) {
                            new WebRetriveDataTask().sendUserOTP(SignupActivity.this, Constant.URL_BASE + Constant.URL_VALIDATE_OTP, mMobileNumber, mEdTvOTP.getText().toString(), onSukritResponseListner);
                            showProgressBar(SignupActivity.this);

                        } else {
                            mEdTvOTP.setError("Enter OTP");
                        }
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(SignupActivity.this, "Password sent to attached email with this account.Please check and login", Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(SignupActivity.this, LoginScreen.class));
//                        finish();
                    }

                }).show();


    }

    public void showPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    MY_PERMISSIONS_REQUEST_READ_MESSAGE);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_MESSAGE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                registerSMSReceiver();
                // permission was granted, yay! Do the
                // contacts-related task you need to do.

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
            return;
        }

        // other 'case' lines to check for other
        // permissions this app might request
    }

    public void registerSMSReceiver() {
        registerReceiver(smsReceiver, new IntentFilter(
                "android.provider.Telephony.SMS_RECEIVED"));
        //   smsReceiver.bindListener(smsListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null && smsListener != null ) {
            try {
                unregisterReceiver(smsReceiver);
            }catch (IllegalStateException exception){
                exception.printStackTrace();
            }


            smsReceiver = null;
            smsListener = null;

        }

    }

    @Override
    public void messageReceived(String messageText) {
        AppLog.enter(TAG, AppLog.getMethodName());
        AppLog.info(TAG, messageText);
        try {
            String otp = messageText.substring(messageText.length() - 4, messageText.length());
            mEdTvOTP.setText(otp);
            mEdTvOTP.setSelection(otp.length());
            mEdTvOTP.clearFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProgressBar(Context mContext) {
        progressDialog = ProgressDialog.show(mContext, "", Constant.MSG_PROGRESS_DIALOG, false);
        progressDialog.setCancelable(false);
    }

}
