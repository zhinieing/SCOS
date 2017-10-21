package es.source.code.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.SEND_SMS;

public class SCOSHelper extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.gv_helper)
    GridView gvHelper;

    private static final int REQUEST_CALL_PHONE = 0;

    private static final int REQUEST_SEND_SMS = 1;

    private String[] data = {
            "用户使用协议",
            "关于系统",
            "电话人工帮助",
            "短信帮助",
            "邮件帮助"
    };

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoshelper);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);

        gvHelper.setAdapter(adapter);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        if(msg.arg1 == 1){
                            Toast.makeText(SCOSHelper.this, "求助邮件已发送成功", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };

        gvHelper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:

                        callPhone();
                        break;
                    case 3:

                        sendSMS();
                        break;
                    case 4:

                        MailSender mailSender = new MailSender();
                        mailSender.start();
                        break;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CALL_PHONE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    callPhone();
                }
                break;
            case REQUEST_SEND_SMS:
                if(grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    sendSMS();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void callPhone(){
        try{
            if(ContextCompat.checkSelfPermission(SCOSHelper.this, CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(SCOSHelper.this, new String[]{CALL_PHONE, SEND_SMS}, REQUEST_CALL_PHONE);
            } else {
                Intent ImplicitIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + 5554));
                startActivity(ImplicitIntent);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void sendSMS(){
        try{
            if(ContextCompat.checkSelfPermission(SCOSHelper.this, SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(SCOSHelper.this, new String[]{CALL_PHONE, SEND_SMS}, REQUEST_SEND_SMS);
            } else {
                SmsManager.getDefault().sendTextMessage("5554", null, "test scos helper", null, null);
                Toast.makeText(this, "求助短信发送成功", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    class MailSender extends Thread{
        @Override
        public void run() {

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:pengming@gamil.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "您的建议");
            intent.putExtra(Intent.EXTRA_TEXT, "我们很希望能得到您的建议！！！");
            startActivity(intent);

            Message msg = new Message();
            msg.what = 1;
            msg.arg1 = 1;

            handler.sendMessage(msg);

        }
    }

}
