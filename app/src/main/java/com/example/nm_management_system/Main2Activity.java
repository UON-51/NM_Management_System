package com.example.nm_management_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity {
 public Button logout_B ;
 public Button management_B;
 public Button edit_B;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        logout_B = findViewById(R.id.logout_B);
        management_B = findViewById(R.id.management_B);
        edit_B = findViewById(R.id.edit_B);

    }
    public void Logout_B_code(View view) {
        Thread sql_logout = new sql_logout();
        sql_logout.start();
        try {
            sql_logout.join();
            Intent intent = new Intent();
            intent.setClass(Main2Activity.this,MainActivity.class);
            Main2Activity.this.finish();
        } catch (Exception e ){

        }
    }
    public void management_B_code(View view){
        Intent intent = new Intent();
        intent.setClass(Main2Activity.this,activity_edit.class);
        startActivity(intent);
    }
    public  void edit_B_code(View view){
        Intent intent = new Intent();
        intent.setClass(Main2Activity.this,Management_System.class);
        startActivity(intent);
    }
    public class sql_logout extends Thread{
        public void run(){
            MysqlCon con = new MysqlCon();
            con.logout();

        }
    }
}
