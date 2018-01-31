package com.sukritapp.smartshoppr.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.sukritapp.smartshoppr.util.AppLog;


public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;

//    public SmsReceiver(SmsListener listner) {
//        super();
//        this.mListener=listner;
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppLog.info("msg broadcast : ","====");
        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            AppLog.info("msg broadcast : ","===="+smsMessage.toString());

            String sender = smsMessage.getDisplayOriginatingAddress();
            //You must check here if the sender is your provider and not another one with same text.

            String messageBody = smsMessage.getMessageBody();

            //Pass on the text to our listener.
            if(mListener!=null) {
                mListener.messageReceived(messageBody);
            }
        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
