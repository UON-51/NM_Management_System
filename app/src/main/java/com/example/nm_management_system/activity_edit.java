package com.example.nm_management_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;


public class activity_edit extends AppCompatActivity {
    String[] pay = {"全部","已繳款","未繳款"};
    protected String[][] account_data = new String[][]{} ; //Save data //data[X][0~2] = {Name,Account,is_paid}
    protected Spinner spinner;  //spinner
    protected String state_selection; //Save witch mod user choose
    protected final int[] A = {0}; //Save search_data_langth
    private  RecyclerView recycler_View;
    private MyAdapter adapter;
    private ArrayList<String> mdata = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ArrayList<ClipData.Item> mydataset = new ArrayList<>();
        spinner =  findViewById(R.id.spinner_e);
        ArrayAdapter<String> list = new ArrayAdapter<String>(this,R.layout.spinner_style,pay); //將Name_data轉換成ArrayAdapter,並設定spinner外觀參數文件為spinner_style.xml
        spinner.setAdapter(list); //設定spinner顯示文字


    }
    public void search_B (View view){
        state_selection = ch2en(spinner.getSelectedItem().toString());
        Thread search_tread = new search_thread();
        search_tread.start();

        try {
         search_tread.join();
        for(int i = 0; i < A[0]; i ++){
            for (int ii = 0; ii < 3;ii++){
                mdata.add(account_data[i][ii]);

            }
        }
        

        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity_edit.this,"求值失敗", Toast.LENGTH_LONG).show();
        }
    }
    public String ch2en (String s){
        String re;
        switch (s){
            case "全部":
                re = "0 or is_paid = 1";
            break;
            case "已繳款":
                re = "true";
                break;
            case "未繳款":
                re = "false";
                break;
            default:
                re = "";
        }
        Log.e("DB",re);
        return re;
    }
    public class search_thread extends Thread {
        public void run() {
            MysqlCon sql = new MysqlCon();
            sql.connect_sql();
            String L = sql.search_mod_length(state_selection);
            A[0]= Integer.parseInt(L);
            account_data= sql.search_mod(state_selection,A[0]);

        }
    }

}
