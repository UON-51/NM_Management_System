package com.example.nm_management_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
public EditText Login_A;
public EditText Login_P;
public TextView SVS;
public Button login;
public Boolean[] result = {false} ;
public final String[] login_info = {"",""};
final public Boolean[] server_status = {false};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Login_A = findViewById(R.id.Login_A);
        Login_P = findViewById(R.id.Login_P);
        login = findViewById(R.id.login_B);
        login.setVisibility(View.INVISIBLE);
        SVS = findViewById(R.id.textView_server);
        Thread sql_connect = new oncreat();
        sql_connect.start();
        try {
            sql_connect.join();
            if (server_status[0]){
            SVS.setText("伺服器連線成功");
            SVS.setTextColor(Color.GREEN);
            login.setVisibility(View.VISIBLE);
        } else {
            SVS.setText("伺服器連線失敗");
            SVS.setTextColor(Color.RED);

        }} catch (Exception e){e.printStackTrace();}


    }

    public void B_Login(View view){ //管理會登入
        Intent intent = new Intent();

        intent.setClass(MainActivity.this,Main2Activity.class);
        startActivity(intent);
        /*
        login_info[0] = String.valueOf(Login_A.getText());
        login_info[1] = String.valueOf(Login_P.getText());
        Thread A_P_detection = new A_P_detection();
        A_P_detection.start();

        try {
            A_P_detection.join();
            if (result[0]) {

                Intent intent = new Intent();

                intent.setClass(MainActivity.this,Main2Activity.class);
                startActivity(intent);

                Login_A.setText("");
                Login_P.setText("");
            } else {
            {
                Toast.makeText(MainActivity.this,"帳號或密碼錯誤，請重式", Toast.LENGTH_LONG).show();}
        }} catch (Exception e){e.printStackTrace();} */
    }
    public class A_P_detection extends Thread{
    public void run(){
        MysqlCon con = new MysqlCon();
        con.connect_sql();
        result[0] = con.management_detection(login_info[0],login_info[1]);
    }
    }
    public class oncreat extends Thread{
        public void run(){
            MysqlCon con = new MysqlCon();
            server_status[0] = con.connect_sql();
        }
    }
}


