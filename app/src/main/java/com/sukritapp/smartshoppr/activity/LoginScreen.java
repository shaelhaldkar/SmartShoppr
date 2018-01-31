package com.sukritapp.smartshoppr.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sukritapp.smartshoppr.R;
import com.sukritapp.smartshoppr.database.LocalDataBase;
import com.sukritapp.smartshoppr.listner.OnSukritResponseListner;
import com.sukritapp.smartshoppr.sms.SmsListener;
import com.sukritapp.smartshoppr.sms.SmsReceiver;
import com.sukritapp.smartshoppr.util.AppLog;
import com.sukritapp.smartshoppr.util.Constant;
import com.sukritapp.smartshoppr.util.SmartShopprUtils;
import com.sukritapp.smartshoppr.webservice.WebRetriveDataTask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by abc on 4/2/2017.
 */

public class LoginScreen extends Activity implements View.OnClickListener, OnSukritResponseListner, SmsListener {

    private final String TAG = AppLog.getClassName();
    private TextView mBtnSingUp;
    private TextView mTxtVwSkip;
    private Button mBtnLogIn;
    private TextView mtxtVwForgetPassword;
    private Button mBtnGetPassword;
    private EditText mEdtvUserMobileNo;
    private EditText mEdtvUserPassword;
    private EditText mEdtvUserForgetMobile;
    private LinearLayout mLlSinginWindow;
    private LinearLayout mLlForgetWindow;
    OnSukritResponseListner onSukritResponseListner;
    private EditText mEdTvOTP;
    private ProgressDialog progressDialog;
    SmsReceiver smsReceiver = null;
    SmsListener smsListener = null;
    private int MY_PERMISSIONS_REQUEST_READ_MESSAGE = 100;
    private Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        SmartShopprUtils.getInstance().hideKeyBoard();
        setContentView(R.layout.login_screen);
        context = this;
        mLlSinginWindow = (LinearLayout) findViewById(R.id.ll_login_window);
        mLlForgetWindow = (LinearLayout) findViewById(R.id.ll_forgetPassword_window);
        mBtnSingUp = (TextView) findViewById(R.id.btn_signUp);
        mTxtVwSkip = (TextView) findViewById(R.id.txtVw_Skip);
        mBtnLogIn = (Button) findViewById(R.id.btn_Login);
        mtxtVwForgetPassword = (TextView) findViewById(R.id.txtVw_forgotPasword);
        mBtnGetPassword = (Button) findViewById(R.id.btn_Submitforpassword);
        mEdtvUserMobileNo = (EditText) findViewById(R.id.edtv_mobile);
        mEdtvUserPassword = (EditText) findViewById(R.id.edtv_password);
        mEdtvUserForgetMobile = (EditText) findViewById(R.id.edtv_forgotmobile);
        mBtnSingUp.setOnClickListener(this);
        mTxtVwSkip.setOnClickListener(this);
        mtxtVwForgetPassword.setOnClickListener(this);
        mBtnGetPassword.setOnClickListener(this);
        mBtnLogIn.setOnClickListener(this);
        onSukritResponseListner = this;
        mLlSinginWindow.setVisibility(View.VISIBLE);
        mLlForgetWindow.setVisibility(View.INVISIBLE);
        mtxtVwForgetPassword.setPaintFlags(mtxtVwForgetPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

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
        if (!SmartShopprUtils.getInstance().isConnectedToInternet()) {
            SmartShopprUtils.getInstance().showDialog(this, getResources().getString(R.string.no_internet_connection));

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LocalDataBase.getInstance().isSkippedSignUP() || LocalDataBase.getInstance().getIsUSerLogin()) {
            startActivity(new Intent(LoginScreen.this, MainActivity.class));
//            if (LocalDataBase.getInstance().getUberRideID().isEmpty()) {
//                startActivity(new Intent(LoginScreen.this, MainActivity.class));
//            }else {
//                startActivity(new Intent(LoginScreen.this, MyUberRide.class));
//            }
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.txtVw_Skip:
                doSkip();
                break;
            case R.id.btn_signUp:
                doSignUp();
                break;
            case R.id.btn_Login:
                doSignIn();
                break;
            case R.id.txtVw_forgotPasword:
                showForgetWindow();
                break;
            case R.id.btn_Submitforpassword:
                getPassword();
                break;
            default:
                break;

        }
    }

    private void getPassword() {

        if (!SmartShopprUtils.getInstance().isConnectedToInternet()) {
            SmartShopprUtils.getInstance().showDialog(this, getResources().getString(R.string.no_internet_connection));
            return;
        } else if (!(Patterns.PHONE.matcher(mEdtvUserForgetMobile.getText()).matches()) || (mEdtvUserForgetMobile.getText().length() < 10)) {
            mEdtvUserForgetMobile.setError("Please Enter Valid Number");
            mEdtvUserForgetMobile.requestFocus();
        } else {
            new WebRetriveDataTask().getUserPassword(this, Constant.URL_BASE + Constant.URL_FORGET_PASSWORD, mEdtvUserForgetMobile.getText().toString(), onSukritResponseListner);
            showProgressBar(LoginScreen.this);
        }
    }

    private void showForgetWindow() {
        if (!SmartShopprUtils.getInstance().isConnectedToInternet()) {
            SmartShopprUtils.getInstance().showDialog(this, getResources().getString(R.string.no_internet_connection));
            return;
        }
        mLlForgetWindow.setVisibility(View.VISIBLE);
        mLlSinginWindow.setVisibility(View.GONE);
    }

    private void doSignIn() {
        if (!SmartShopprUtils.getInstance().isConnectedToInternet()) {
            SmartShopprUtils.getInstance().showDialog(this, getResources().getString(R.string.no_internet_connection));
            return;
        } else if (!(Patterns.PHONE.matcher(mEdtvUserMobileNo.getText()).matches()) || (mEdtvUserMobileNo.getText().length() < 10)) {
            mEdtvUserMobileNo.setError("Please Enter Valid Number");
            mEdtvUserMobileNo.requestFocus();
        } else if (mEdtvUserPassword.getText().toString() == null || mEdtvUserPassword.getText().toString().equalsIgnoreCase("") || mEdtvUserPassword.getText().toString().length() == 0) {
            mEdtvUserPassword.setError("Please Enter Password");
            mEdtvUserPassword.requestFocus();
        } else {

            showProgressBar(this);
            new WebRetriveDataTask().signInUser(this, Constant.URL_BASE + Constant.URL_LOG_IN, mEdtvUserMobileNo.getText().toString(), mEdtvUserPassword.getText().toString(), onSukritResponseListner);

        }

    }

    private void doSignUp() {
        if (!SmartShopprUtils.getInstance().isConnectedToInternet()) {
            SmartShopprUtils.getInstance().showDialog(this, getResources().getString(R.string.no_internet_connection));
            return;
        }
        Intent intent = new Intent(LoginScreen.this, SignupActivity.class);
        startActivity(intent);
        finish();
    }

    private void doSkip() {
        LocalDataBase localDataBase = LocalDataBase.getInstance();
        localDataBase.setSignUp(true);
        Intent i = new Intent(LoginScreen.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void registerSMSReceiver() {
        registerReceiver(smsReceiver, new IntentFilter(
                "android.provider.Telephony.SMS_RECEIVED"));

        //   smsReceiver.bindListener(smsListener);
    }


    @Override
    public void onResponseReceived(String error, Constant.listtype tag, String response) {
        AppLog.info(TAG, "response : " + response);
        AppLog.info(TAG, "response tag: " + tag);
        progressDialog.dismiss();
        if (response!= null) {
            if (Constant.listtype.SIGNIN.toString().equalsIgnoreCase("" + tag)) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String responsMSG = jsonObject.getString("message");
                    if (responsMSG.contains(Constant.RESPONSE_MSG_SUCCESS)) {

                        startActivity(new Intent(LoginScreen.this, MainActivity.class));
                        LocalDataBase.getInstance().setUserDetail("", "", mEdtvUserMobileNo.getText().toString());
                        LocalDataBase.getInstance().setIsLogin(true);
                        finish();
                    } else {
//                    SmartShopprUtils.getInstance().showToastMSG(responsMSG);
                        SmartShopprUtils.getInstance().showDialog(this, responsMSG);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (Constant.listtype.GETPASSWORD.toString().equalsIgnoreCase("" + tag)) {
//            if (!response.isEmpty() && response.equalsIgnoreCase(Constant.RESPONSE_MSG_FORGET_PASSWORD))
//                SmartShopprUtils.getInstance().showInfoDialog(this, Constant.RESPONSE_MSG_FORGET_PASSWORD);

                if (!response.isEmpty()) {
                    try {
                        int startIndex = response.indexOf("{");
                        response = response.substring(startIndex);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String responsMSG = jsonObject.getString("flag");
                        if (responsMSG.contains("1")) {
                            showOTPDialog(this, "");
                        } else {
                            SmartShopprUtils.getInstance().showDialog(this, jsonObject.getString("message").toString());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    SmartShopprUtils.getInstance().showDialog(this, "something went wrong");
                }


            } else if (Constant.listtype.VALIDATEOTP.toString().equalsIgnoreCase("" + tag)) {
                if (!response.isEmpty()) {
                    try {
                        int startIndex = response.indexOf("{");
                        response = response.substring(startIndex);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String responsMSG = jsonObject.getString("message");
                        if (responsMSG.contains("Your password sent to your mobile number ")) {
                            mLlSinginWindow.setVisibility(View.VISIBLE);
                            mLlForgetWindow.setVisibility(View.GONE);
                            SmartShopprUtils.getInstance().showDialog(this, "we have sent new password via message!!");
                            // showNewPasswordWindow("Change Password");
                        } else {
                            SmartShopprUtils.getInstance().showDialog(this, jsonObject.getString("message").toString());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    SmartShopprUtils.getInstance().showDialog(this, error);
                }

            } else if (Constant.listtype.CHANGEPASSWORD.toString().equalsIgnoreCase("" + tag)) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String responsMSG = jsonObject.getString("message");
                    if (responsMSG.contains(Constant.RESPONSE_MSG_SUCCESS)) {
                        SmartShopprUtils.getInstance().showDialog(this, "Password changed successfully .");

                    } else {
//                    SmartShopprUtils.getInstance().showToastMSG(responsMSG);
                        SmartShopprUtils.getInstance().showDialog(this, responsMSG);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                AppLog.info(TAG, "inside else block : " + tag);
            }
        } else {
            SmartShopprUtils.getInstance().showErrorDialog(this, "Some thing went wrong");
        }
    }


    public void showOTPDialog(final Activity context, String msg) {
        AppLog.enter(TAG, AppLog.getMethodName());

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_otp, null);
        dialogBuilder.setView(dialogView);

        mEdTvOTP = (EditText) dialogView.findViewById(R.id.edtv_otp);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (mEdTvOTP.getText().length() > 0) {
                            new WebRetriveDataTask().getValidateUserOTP(LoginScreen.this, Constant.URL_BASE + Constant.URL_VALIDATE_FORGET_OTP, mEdtvUserForgetMobile.getText().toString(), mEdTvOTP.getText().toString(), onSukritResponseListner);
                            showProgressBar(LoginScreen.this);

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
    public void showProgressBar(Context mContext) {
        progressDialog = ProgressDialog.show(mContext, "", Constant.MSG_PROGRESS_DIALOG, false);
        progressDialog.setCancelable(false);
    }

    private void showNewPasswordWindow(String msg) {
//        http://mgsmapi.semseosmo.com/smartshopper/updatePassword? new_password=1234&mobile=8802423589
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_change_pwd, null);
        dialogBuilder.setView(dialogView);

        final EditText edTvPwd = (EditText) dialogView.findViewById(R.id.edtv_password);
        final EditText edTvConfrimPwd = (EditText) dialogView.findViewById(R.id.edtv_confirm_password);

        dialogBuilder.setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pwd = edTvPwd.getText().toString();
                        String cnfrmpwd = edTvConfrimPwd.getText().toString();
                        if (pwd.isEmpty()) {
                            edTvPwd.setError("Enter confirm Password ");
                        } else if (cnfrmpwd.isEmpty()) {
                            edTvConfrimPwd.setError("Enter confirm Password ");
                        } else if (edTvPwd.getText().toString().equalsIgnoreCase(edTvConfrimPwd.getText().toString())) {
                            edTvConfrimPwd.setError("Password do not match ");
                        } else {
//                            CHANGEPASSWORD
                            String URL = Constant.URL_BASE + Constant.URL_UPDATE_PASSWORD + "?new_password=" + pwd + "&mobile=" + mEdtvUserForgetMobile.getText().toString();
                            new WebRetriveDataTask().changePassword(context, Constant.listtype.CHANGEPASSWORD, URL, onSukritResponseListner);
                            showProgressBar(LoginScreen.this);
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
    protected void onStop() {

        try {
            if (smsReceiver != null && smsListener != null) {
                unregisterReceiver(smsReceiver);
                smsReceiver = null;
                smsListener = null;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (smsReceiver != null && smsListener != null) {
                unregisterReceiver(smsReceiver);
                smsReceiver = null;
                smsListener = null;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}