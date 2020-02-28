package com.example.atm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

//ctrl+alt+← 回到上次工作的那一行
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CAMERA = 5;
    private EditText edUserid;
    private EditText edPasswd;
    private CheckBox cbRemember;
    private Intent helloService;
    private Intent helloService1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Fragment
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(R.id.container_news,NewsFragment.getInstance());
        fragmentTransaction.commit();//commit它就會把這個fragment放到container_news這個區塊中

        //Service
        helloService1 = new Intent(this,HelloService.class);
        helloService1.putExtra("NAME","T1");
        startService(helloService1);
        helloService1.putExtra("NAME","T2");
        startService(helloService1);
        helloService1.putExtra("NAME","T3");
        startService(helloService1);


        //camera();

        //存入資料
        //settingTest();
        findViews();
        //不行直接執行doInBackground，因為還在UI執行續
        new TestTask().execute("http://tw.yahoo.com");

    }

    public void map(View view){
        startActivity(new Intent(this,MapsActivity.class));
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive:Hello "+intent.getAction());
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(HelloService.ACTION_HELLO_DONE);
        registerReceiver(receiver,filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(helloService1);
        unregisterReceiver(receiver);
    }

    //網路連線
    public class TestTask extends AsyncTask<String,Void,Integer>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: ");
            Toast.makeText(LoginActivity.this,"onPreExecute",Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.d(TAG, "onPostExecute: ");
            Toast.makeText(LoginActivity.this,"onPostExecute"+integer,Toast.LENGTH_LONG).show();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            int data = 0;
            try {
                URL url = new URL(strings[0]);
                data = url.openStream().read();
                Log.d(TAG, "onCreate: " + data);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }
    }

    private void findViews() {
        edUserid = findViewById(R.id.userid);
        edPasswd = findViewById(R.id.passwd);
        cbRemember = findViewById(R.id.cb_rem_userid);
        cbRemember.setChecked(
                getSharedPreferences("atm",MODE_PRIVATE)
                        .getBoolean("REMEMBER_USERID",false));//setCheck是一個需要一個真假值，這段是要去取得目前儲存值裡面的真假值
        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getSharedPreferences("atm",MODE_PRIVATE)
                        .edit()
                        .putBoolean("REMEMBER_USERID",true)
                        .apply();
            }
        });
        String userid=getSharedPreferences("atm",MODE_PRIVATE)
                .getString("USERID","");
        edUserid.setText(userid);
    }

    private void settingTest() {
        getSharedPreferences("atm",MODE_PRIVATE)
                .edit()
                .putInt("LEVEL",3)
                .putString("NAME","Tom")
                .commit();
        //寫入資料
        int level = getSharedPreferences("atm",MODE_PRIVATE)
                .getInt("LEVEL",0);
        Log.d(TAG, "onCreate: "+level);//logd縮寫
    }

    private void camera() {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permission == PackageManager.PERMISSION_GRANTED){
//           ctrl + / 反註解
            //takePhoto();
        }else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},REQUEST_CODE_CAMERA);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((requestCode == REQUEST_CODE_CAMERA)){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                takePhoto();
            }
        }
    }

    //CAMERA權限允許進來這
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }

    public void login(View view){
        final String userid=edUserid.getText().toString();
        final String passwd=edPasswd.getText().toString();
        //導入Firebase
        FirebaseDatabase.getInstance().getReference("users").child(userid).child("password") //child代表這一個資料庫集底下的子資料集
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String pw= (String) dataSnapshot.getValue();
                    if (pw.equals(passwd)){
                        boolean remember=getSharedPreferences("atm",MODE_PRIVATE)
                                .getBoolean("REMEMBER_USERID",false);//預設沒有資料就預設它是不儲存
                         if (remember) {
                             //save userid
                             getSharedPreferences("atm", MODE_PRIVATE)
                                     .edit()//呼叫edit的方法取得編輯器
                                     .putString("USERID", userid)
                                     .apply();
                         }
                        setResult(RESULT_OK);
                        finish();//在一個Activity上面呼叫finish，會結束這個Activity
                    }else{
                          //對話框
                          new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("登入結果")
                                    .setMessage("登入失敗")
                                    .setPositiveButton("OK",null)
                                    .show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        //單行註解 ctrl+/ 區塊式註解 ctrl+shift+/
        /*if ("jack".equals(userid)&&"1234".equals(passwd)){
            setResult(RESULT_OK);
            finish();//在一個Activity上面呼叫finish，會結束這個Activity

        }*/
    }

    public void quit(View view){

    }
}
