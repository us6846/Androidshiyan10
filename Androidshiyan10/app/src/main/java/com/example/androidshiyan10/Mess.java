package com.example.androidshiyan10;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Mess{
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;
    private String content;
    private int type;

    public Mess(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }
    public int getType() {
        return type;
    }


}
