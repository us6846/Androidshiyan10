package com.example.androidshiyan10;
import java.util.ArrayList;
import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class MesgReceiver extends BroadcastReceiver {
    private List<Mess> msgList = new ArrayList<>();
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Object[] pdus =(Object[])bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[pdus.length];
        for(int i=0;i< messages.length;i++){
            messages[i]=SmsMessage.createFromPdu((byte[])pdus[i]);
        }
        String address = messages[0].getOriginatingAddress();
        String fullMessage = "";
        for(SmsMessage message : messages){
            fullMessage += message.getMessageBody();
        }
        Mess msg3 = new Mess(fullMessage+"",Mess.TYPE_RECEIVED);
        msgList.add(msg3);
    }
}
