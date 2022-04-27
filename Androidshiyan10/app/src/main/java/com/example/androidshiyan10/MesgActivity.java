package com.example.androidshiyan10;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MesgActivity extends AppCompatActivity {
    private List<Mess> msgList = new ArrayList<>();
    private EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;
    private MessAdapter adapter1;
    private static final int TAKE_PHOTO = 1;
    private ImageView picture;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesg);
        Intent intent = getIntent();
        String number = intent.getStringExtra("number");

        //摄像头
        Button takePhoto = (Button) findViewById(R.id.photo);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File outputImage = new File(Environment.getExternalStorageDirectory(),"output_image.jpg");
                try{
                    if(outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                if(Build.VERSION.SDK_INT >= 24){
                    imageUri = FileProvider.getUriForFile(MesgActivity.this,"/",outputImage);
                }else{
                    imageUri = Uri.fromFile(outputImage);
                }
                Intent intent1 = new Intent("android.media.action.IMAGE_CAPTURE");
                intent1.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent1,TAKE_PHOTO);
            }
        });

        //设置信息
        initMsgs();
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send) ;
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter1 = new MessAdapter(msgList);
        msgRecyclerView.setAdapter(adapter1);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();

                if(!"".equals(content)){
                    Mess msg = new Mess(content,Mess.TYPE_SENT);
                    msgList.add(msg);
                    adapter1.notifyItemInserted(msgList.size() - 1);
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);
                    inputText.setText("");
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number+"", null,content, null, null);
                }
            }
        });

    }
    private void initMsgs(){
        Mess msg1 = new Mess("你好！",Mess.TYPE_RECEIVED);
        msgList.add(msg1);
        Mess msg2 = new Mess("你好呀！",Mess.TYPE_SENT);
        msgList.add(msg2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(MesgActivity.this, "保存在" + imageUri, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
