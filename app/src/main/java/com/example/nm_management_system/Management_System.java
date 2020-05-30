package com.example.nm_management_system;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;


public class Management_System extends AppCompatActivity {
    protected LinearLayout linearLayout_add;
    protected LinearLayout linearLayout_management;
    protected String txt_name ;
    protected String txt_account ;
    protected String txt_password ;
    protected String txt_password2 ;
    protected EditText Etxt_password;
    protected EditText Etxt_name;
    protected EditText Etxt_account;
    protected EditText Etxt_password2;
    protected EditText Etxt_E_Name;
    protected EditText Etxt_E_Account;
    protected EditText Etxt_E_Password;
    protected TextView txt_test;
    protected Boolean[] result = {true};
    protected Spinner spinner ;
    protected final int[] IntArray ={0,0};
    protected String[][] data = new String[][]{};


    //SQL因為是安桌程式的關係所以練線的程式碼須寫在Thread否則會無法連線
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management__system);
        Button B_add = findViewById(R.id.button_addmod);
        Button B_management = findViewById(R.id.button_managementmod);
        linearLayout_add = findViewById(R.id.linearLayout_add);
        linearLayout_management = findViewById(R.id.linearLayout_management);
        Etxt_name = findViewById(R.id.editText_addstorename);
        Etxt_account = findViewById(R.id.editText_addstoreaccount);
        Etxt_password = findViewById(R.id.editText_setpassword);
        Etxt_password2 = findViewById(R.id.editText_setpassword2);
        Etxt_E_Name = findViewById(R.id.editText_name);
        Etxt_E_Account = findViewById(R.id.editText_account);
        Etxt_E_Password = findViewById(R.id.editText_password);
        spinner = findViewById(R.id.spinner_selectstore);
        txt_test = findViewById(R.id.textView8);
    }


    public void A_mod(View view){
        linearLayout_add.setVisibility(View.VISIBLE);
        linearLayout_management.setVisibility(View.GONE);
        Etxt_account.setText("");
        Etxt_name.setText("");
        Etxt_password.setText("");
        Etxt_password2.setText("");
    }

    public void M_mod(View view){
        linearLayout_management.setVisibility(View.VISIBLE);
        linearLayout_add.setVisibility(View.GONE);

        Thread get_length = new get_length();
        Callable<String[][]> get_data_C = new callable();
        FutureTask<String[][]> get_data_task = new FutureTask<String[][]>(get_data_C);
        Thread get_data_T = new Thread(get_data_task);
        get_length.start();
        try {

            get_length.join();

            get_data_T.start();
            try{
                get_data_T.join();
                data =get_data_task.get();  //取得callable內執行結束後get_data所回傳的資料

            }catch (Exception e){}
        }
        catch (Exception e) {}

        set_spinner();
        Thread listen_spinner_selected = new spinner_selected();
        listen_spinner_selected.start();

    }
    public class spinner_selected extends Thread{
        public void run(){
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int p = position;
                    IntArray[1] = p;
                    Etxt_E_Name.setText(data[p][0]);
                    Etxt_E_Account.setText(data[p][1]);
                    Etxt_E_Password.setText(data[p][2]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



        }
    }
    public void set_spinner(){
        String[] Name_data = new String[IntArray[0]+1]; //因下拉選單第一格欲顯示"請選擇店家"，因此剩餘資料向後延一格
        Name_data[0] = "請選擇店家";
        Thread get_length = new get_length();
        Callable<String[][]> get_data_C = new callable();
        FutureTask<String[][]> get_data_task = new FutureTask<String[][]>(get_data_C);
        Thread get_data_T = new Thread(get_data_task);
        get_length.start();
        try {
            get_length.join();
            get_data_T.start();
            try{
                get_data_T.join();
                data =get_data_task.get();  //取得callable內執行結束後get_data所回傳的資料
                for (int i = 1 ; i  <= IntArray[0]; i++){

                    Name_data[i] = data[i][0];
                }
                ArrayAdapter<String> list = new ArrayAdapter<String>(this,R.layout.spinner_style,Name_data); //將Name_data轉換成ArrayAdapter,並設定spinner外觀參數文件為spinner_style.xml
                spinner.setAdapter(list); //設定spinner顯示文字
            }catch (Exception e){}
        }
        catch (Exception e) {}
    }


    public void delete_mod(View view){
        Thread delete_Thread = new delete_Thread();
        delete_Thread.start();
        try {
            delete_Thread.join();
            Log.e("Delete","成功");
            IntArray[0] = IntArray[0] - 1 ;
        } catch (Exception e){
        }
        set_spinner();
    }
    public class delete_Thread extends Thread {
        public void run() {
            MysqlCon con = new MysqlCon();
            con.connect_sql();
            con.delete(data[IntArray[1]][1]);


        }
    }
    public void edit_mod(View view){
        Thread edit_Thread = new edit_Thread();
        edit_Thread.start();
        try {
            edit_Thread.join();

        } catch (Exception e) {

        }
        set_spinner();

    }
    public class edit_Thread extends Thread {
        public void run() {
            MysqlCon con = new MysqlCon();
            con.connect_sql();
            String new_name = String.valueOf(Etxt_E_Name.getText());
            String new_account = String.valueOf(Etxt_E_Account.getText());
            String new_password = String.valueOf(Etxt_E_Password.getText());
            con.edit(data[IntArray[1]][1],new_name,new_account,new_password);

        }
    }
    public class get_length extends Thread{
        public void run(){
            int A = 0;
            MysqlCon con = new MysqlCon();
            con.connect_sql();
            A = Integer.parseInt(con.get_data_length());
            IntArray[0] =A ;

        }
    }
    public class callable implements Callable<String[][]>{
        public String[][] call() throws Exception{
            String[][] SA = new String[][]{};
            MysqlCon con = new MysqlCon();
            con.connect_sql();
            SA =  con.getData(IntArray[0]);
            return SA;
        }
    }

    public void B_add(View view){
        txt_password = Etxt_password.getText().toString();
        txt_password2 = Etxt_password2.getText().toString();
        txt_account = Etxt_account.getText().toString();
        txt_name = Etxt_name.getText().toString();
        int name = 1;
        int account = 1;
        int password = 1;
        int password2 = 1;
        if(txt_name.length()==0)                              //判斷NAME輸入框是否為空
        {
            Etxt_name.setHint("請輸入商家名稱");
            Etxt_name.setHintTextColor(Color.RED);
        } else {
            name = 0;
        }
        if(txt_account.length()==0)                              //判斷account輸入框是否為空
        {
            Etxt_account.setHint("請輸入店家帳號");
            Etxt_account.setHintTextColor(Color.RED);
        }else {
            account = 0;
        }
        if(txt_password.length()==0)                              //判斷password輸入框是否為空
        {
            Etxt_password.setHint("請輸入密碼");
            Etxt_password.setHintTextColor(Color.RED);
        }else {
            password = 0;
        }
        if(txt_password2.length()==0)                              //判斷password2輸入框是否為空
        {
            Etxt_password2.setHint("請輸入密碼");
            Etxt_password2.setHintTextColor(Color.RED);
        } else {
            if (txt_password.equals(txt_password2)) {              //判斷password是否等於passwprd2
                password2 = 0;
            } else {
                {Toast.makeText(Management_System.this,"密碼不相同請重新輸入", Toast.LENGTH_LONG).show();} //密碼重複驗證失敗
            }
        }
        if (name + account + password + password2 == 0){
            Thread t1 = new repeat();
            t1.start();
            try {
                t1.join();
                detection_result(result[0]);
            } catch (InterruptedException e){

            }
        }
    }
    public void detection_result(Boolean DR){

        if (DR){
            {Toast.makeText(Management_System.this,"新增帳戶失敗，帳戶已存在", Toast.LENGTH_LONG).show();}
            Etxt_account.setText("");
            Etxt_name.setText("");
            Etxt_password.setText("");
            Etxt_password2.setText("");
        } else {
            add_account();
            {Toast.makeText(Management_System.this,"新增帳戶成功", Toast.LENGTH_LONG).show();}
            Etxt_account.setText("");
            Etxt_name.setText("");
            Etxt_password.setText("");
            Etxt_password2.setText("");
        }
    }
    public class repeat extends Thread{
        public void run(){
            Boolean AA ;
            MysqlCon con = new MysqlCon();
            con.connect_sql();
            AA = con.repeat_detection(txt_account);
            result[0] = AA;
        }
}
    public void add_account(){
        new Thread(new Runnable(){
            @Override
            public void run(){
        MysqlCon con = new MysqlCon();
        con.connect_sql();
        con.update(txt_name,txt_account,txt_password);
    }
        }).start();
    }

}
