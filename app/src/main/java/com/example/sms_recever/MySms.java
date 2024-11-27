package com.example.sms_recever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MySms extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        processSms(context, intent);
    }

    private void processSms(Context context, Intent intent) {
        Bundle mybundle = intent.getExtras();
        String message = "";
        String body = "";
        String address = "";
        if (mybundle != null){
            Object[] mysms = (Object[]) mybundle.get("pdus");
            for( int i = 0 ; i < mysms.length; i++) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) mysms[i]);
                body = sms.getMessageBody(); //lấy nội dung tin nhắn
                address = sms.getOriginatingAddress();// lấy số điện thoại

                message = "từ số điện thoại: " + address + "\n" + "nội dung: " + body + " vừa gửi đến ";
            }
            Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG).show();
            Intent updateIntent = new Intent("newSmsReceived");
            updateIntent.putExtra("message", message);
            LocalBroadcastManager.getInstance(context).sendBroadcast(updateIntent);
        }
    }

}