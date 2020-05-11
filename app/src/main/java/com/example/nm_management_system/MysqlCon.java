package com.example.nm_management_system;

import android.util.Log;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlCon {
    String mysql_ip = "35.201.205.223";
    int mysql_port = 3306; // Port 預設為 3306
    String db_name = "Night_Market_management";
    String url = "jdbc:mysql://"+mysql_ip+":"+mysql_port+"/"+db_name;
    String db_user = "app-management";
    String db_password = "management";


    public Boolean connect_sql() {
        //載入驅動
        Boolean connect = false;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("DB","加載驅動成功");
        }catch( ClassNotFoundException e) {
            Log.e("DB","加載驅動失敗");

        }
        Connection con = null;
        // 連接資料庫
        try {
            con = DriverManager.getConnection(url,db_user,db_password);
            Log.v("DB","遠端連接成功");
            connect = true;
        }catch(SQLException e) {
            Log.e("DB","遠端連接失敗");
            Log.e("DB", e.toString());
        }
        return connect;
    }

    public String get_data_length(){
        //向伺服器詢問有幾筆資料
        String E = "";
        try {
            Connection con =  DriverManager.getConnection(url, db_user, db_password);
            Statement st = con.createStatement();
            ResultSet data_length  = st.executeQuery("SELECT COUNT(Name) from Store_Account");
            while (data_length.next())
            {
                try {
                    E = data_length.getString("COUNT(Name)");

                } catch (Exception e){
                    Log.e("DB","回傳資料為空");
                }
            }
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return E;
    }
    public Boolean repeat_detection(String A){
        //向伺服器確認帳號是否已存在
        boolean X = false;
        String sql = " select Account from Store_Account where Account='" + A + "'";
        try
        {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                String Z = rs.getString("Account");
                if(Z.equals(A)) {
                    X = true;
                } else {
                    X = false;
                }
            }
            Log.e("DB","repeat has do something");
            st.close();
        } catch (SQLException e){
            Log.e("ERROR-","repeat error");
            e.printStackTrace();
        }
        return X;
    }

    public void updata(String N,String A ,String P){
        String sql = "insert into Store_Account (Name,Account,Password,is_paid) values ('"+ N +"','"+ A +"','"+ P +"','"+"0') ";
        try {
            Connection con = DriverManager.getConnection(url,db_user,db_password);
            Statement st = con.createStatement();
            st.executeUpdate(sql);
            st.close();
        } catch (SQLException e){
            Log.e("ERROR-","updata error");
            e.printStackTrace();
        }

    }
    public void edit(String ON,String N,String A,String P){
        String sql = "update Store_Account set Name='" + N + "', Account='" + A + "', Password='" + P + "' where Name='"+ ON +"'";
        try {
            Connection con = DriverManager.getConnection(url,db_user,db_password);
            Statement st = con.createStatement();
            st.executeUpdate(sql);
            st.close();
            Log.v("DB","資料修改成功");
        } catch (SQLException e){
            Log.e("ERROR-","edit error");
            e.printStackTrace();
        }
    }
    public void delete(String N){
        String sql = "delete  from Store_Account where Name='"+ N +"'";
        try {
            Connection con = DriverManager.getConnection(url,db_user,db_password);
            Statement st = con.createStatement();
            st.executeUpdate(sql);
            st.close();
            Log.v("DB","資料修改成功");
        } catch (SQLException e){
            Log.e("ERROR-","delete error");
            e.printStackTrace();
        }
    }
    public String[][] getData(int A) {
        A++;    // 因欲設二維資料陣列[0][1~3]為空，所以資料向後延一格
        String[][] data = new String[A][3];
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT * from Store_Account" ;
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int i = 1;
            while (rs.next())
            {
                String Name = rs.getString("Name");
                String Account = rs.getString("Account");
                String Password = rs.getString("Password");
                int ii = 0;
                while (ii <= 2){
                    switch (ii) {
                        case 0:
                            data[i][0]=Name;
                            break;
                        case 1:
                            data[i][1]=Account;
                            break;
                        case 2:
                            data[i][2]=Password;
                            break;
                    }
                    ii++;
                }
                i++;
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
    public Boolean management_detection(String A,String P){
        //向伺服器確認帳號是否已存在
        boolean B = false;
        String sql = "SELECT Account FROM Management_Account WHERE Account='"+ A +"' AND Password='" + P +"'";
        try
        {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                String Z = rs.getString("Account");
                if(Z.equals(A)) {
                    B = true;
                } else {
                    B = false;
                }
            }
            Log.e("DB","Nanagement_repeat has do something");
            st.close();
        } catch (SQLException e){
            Log.e("ERROR-","Nanagement_repeat error");
            e.printStackTrace();
        }
        return B;
    }

    public void logout(){
        String sql = "exit";
        try {
            Connection con = DriverManager.getConnection(url,db_user,db_password);
            Statement st = con.createStatement();
            st.executeUpdate(sql);
            st.close();

        } catch (SQLException e){

            e.printStackTrace();
        }
    }

    public String[][] search_mod (String state,int L){
        String[][] data = new String[L][3] ;
         try {
             Connection con = DriverManager.getConnection(url, db_user, db_password);
             String sql = "select * from Store_Account where is_paid = "+ state + ";";
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql);
             int i = 0;
             while (rs.next())
             {
                 String Name = rs.getString("Name");
                 String Account = rs.getString("Account");
                 String is_paid = rs.getString("is_paid");
                 int ii = 0;
                 while (ii <= 2){

                     switch (ii) {
                         case 0:
                             data[i][0]=Name;
                             break;
                         case 1:
                             data[i][1]=Account;
                             break;
                         case 2:
                             data[i][2]=is_paid;
                             break;

                     }
                     ii++;
                 }
                 i++;
             }
             st.close();


         } catch (Exception e){
             e.printStackTrace();
             Log.e("DB","search_mod has not do something");

         }
        return data;
    }

    public String search_mod_length(String mod){
        //向伺服器詢問有幾筆資料
        String E = "";

        try {
            Connection con =  DriverManager.getConnection(url, db_user, db_password);
            Statement st = con.createStatement();
            ResultSet data_length  = st.executeQuery("SELECT COUNT(Name) from Store_Account where is_paid="+ mod +";");
            while (data_length.next())
            {
                try {
                    E = data_length.getString("COUNT(Name)");

                } catch (Exception e){
                    e.printStackTrace();
                    Log.e("DB","回傳資料為空");
                }
            }
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return E;
    }
}


