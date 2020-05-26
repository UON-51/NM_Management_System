package com.example.nm_management_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;


public class activity_edit extends AppCompatActivity {
    String[] pay = {"全部","已繳款","未繳款"};
    protected String[][] account_data = new String[][]{} ; //Save data //data[X][0~2] = {Name,Account,is_paid}
    protected Spinner spinner;  //spinner
    protected String state_selection; //Save witch mod user choosed
    protected final int[] A = {0}; //Save search_data_langth
    protected final String[] Change =new String[2];
    private  RecyclerView recyclerView;
    private Myadapter Myadapter;
    private LinkedList<HashMap<String,String>> data = new LinkedList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ArrayList<ClipData.Item> mydataset = new ArrayList<>();
        spinner =  findViewById(R.id.spinner_e);
        ArrayAdapter<String> list = new ArrayAdapter<String>(this,R.layout.spinner_style,pay); //將Name_data轉換成ArrayAdapter,並設定spinner外觀參數文件為spinner_style.xml
        spinner.setAdapter(list); //設定spinner顯示文字
        recyclerView = findViewById(R.id.RecyclerView);


    }
    public void search_B (View view){
        state_selection = ch2en(spinner.getSelectedItem().toString());
        Thread search_tread = new search_thread();
        search_tread.start();
        data = new LinkedList<>();
        try {
         search_tread.join();
        for(int i = 0; i < A[0]; i ++){
            HashMap<String,String> row = new HashMap<>();
            row.put("name",account_data[i][0]);
            row.put("account",account_data[i][1]);
            if(account_data[i][2].equals("1")){
                row.put("is_paid","true");
            } else {
                row.put("is_paid","false");
            }


            data.add(row);
        }

            Myadapter = new Myadapter();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(Myadapter);


        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity_edit.this,"失敗", Toast.LENGTH_LONG).show();
        }
    }

    private class Myadapter extends RecyclerView.Adapter<Myadapter.MyViewHolder> {

        class MyViewHolder extends RecyclerView.ViewHolder{
            public View list_item;
            public TextView name;
            public TextView account;
            public CheckBox is_paid;
            public MyViewHolder(View v){
                super(v);
                list_item = v;
                name = list_item.findViewById(R.id.name_text);
                account =list_item.findViewById(R.id.account_text);
                is_paid = list_item.findViewById(R.id.info_chcekbox);
            }
        }

        @NonNull
        @Override
        public Myadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View list_item = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
            MyViewHolder vh = new MyViewHolder(list_item);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull Myadapter.MyViewHolder holder, final int position) {
            holder.name.setText(data.get(position).get("name"));
            holder.account.setText(data.get(position).get("account"));
            holder.is_paid.setChecked(Boolean.parseBoolean(data.get(position).get("is_paid")));
            holder.is_paid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                int c = 0;
                if (isChecked){
                    c = 1;
                }  else {
                    c = 0;
                }
                Change[0] = data.get(position).get("account");
                Change[1] = Integer.toString(c);
                Thread is_paid_change_thread = new is_paid_change_thread();
                is_paid_change_thread.start();
                try {
                    is_paid_change_thread.join();
                } catch (Exception e){

                }


                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
    public String ch2en (String s)//中英轉換
    {
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
    public class is_paid_change_thread extends Thread {
        public void run() {
            MysqlCon con = new MysqlCon();
            con.is_paid_change(Change[0],Change[1]);


        }
    }
    public class search_thread extends Thread {
        public void run() {
            MysqlCon sql = new MysqlCon();
            String L = sql.search_mod_length(state_selection);
            A[0]= Integer.parseInt(L);
            account_data= sql.search_mod(state_selection,A[0]);

        }
    }

}
