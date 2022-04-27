package com.example.androidshiyan10;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    List<String> contactsList=new ArrayList<>();
    private IntentFilter receiveFilter;
    private MesgReceiver mesgReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        receiveFilter = new IntentFilter();
        receiveFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        mesgReceiver = new MesgReceiver();
        registerReceiver(mesgReceiver, receiveFilter);
        ListView contactsView=(ListView)findViewById(R.id.contacts_view);
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,contactsList);
        contactsView.setAdapter(adapter);
        contactsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String[] messages = contactsList.get(i).split("\n");
                String number = messages[1] ;
                Intent intent = new Intent(MainActivity.this,MesgActivity.class);
                intent.putExtra("number",number);
                startActivity(intent);

            }
        });

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.READ_CONTACTS},1);
        }
        else{
            readContacts();
        }
    }
    private void readContacts(){
        Cursor cursor=null;
        try{
            //查询联系人数据
            cursor=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,null,null,null);
            if(cursor!=null){
                while(cursor.moveToNext()){
                    //获取联系人姓名
                    @SuppressLint("Range") String displayName= (String) cursor.getString(cursor.getColumnIndex
                            (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    //获取联系人手机号
                    @SuppressLint("Range") String displayNumber=(String)cursor.getString(cursor.getColumnIndex
                            (ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactsList.add(displayName+"\n"+displayNumber);
                }
                adapter.notifyDataSetChanged();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if(cursor!=null){
                cursor.close();
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    readContacts();
                }
                else {
                    Toast.makeText(this,"You denied permission",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default:
                break;
        }
    }

}



