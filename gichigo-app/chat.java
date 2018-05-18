package com.example.user.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class chat extends AppCompatActivity {
    ConnectionClass connectionClass;
    private Toolbar mToolbar;
    ListView chat2;
    SimpleAdapter ADAhere;
    private EditText chattext;

    String name2,pname2,store2,quality2,querycmd3,car2;
    Double  price2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chat2 = (ListView) findViewById(R.id.chat2);
        connectionClass = new ConnectionClass();
        chattext = (EditText) findViewById(R.id.editText6);


        Bundle bundle = this.getIntent().getExtras();
        final String name = bundle.getString("name");
        final String pname = bundle.getString("pname");
        final Double price = bundle.getDouble("price");
        final String store = bundle.getString("store");
        final String quality = bundle.getString("quality");
        final String car = bundle.getString("car");
        final String querycmd2 = bundle.getString("querycmd");
        final String pid = bundle.getString("pid");
        name2=name;
        pname2=pname;
        price2=price;
        store2=store;
        quality2=quality;
        querycmd3=querycmd2;
        car2=car;


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.button14).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chattext2 =  chattext.getText().toString();
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();


                try {
                    Connection con = connectionClass.CONN();
                    String query2 = "insert into board (board_user_message,board_user_id,product_name,time,board_store_id,fcmid,product_id) values ('" + chattext2  + "', '" + name + "', '" + pname + "', '" + date +"', '" + store +"', '" + refreshedToken + "', '" + pid + "')";
                    PreparedStatement preparedStatement = con.prepareStatement(query2);
                    preparedStatement.executeUpdate();
                }catch (SQLException e) {
                    Toast.makeText(chat.this, e.getMessage().toString(),
                            Toast.LENGTH_LONG).show();
                }


                try {

                    Connection con = connectionClass.CONN();
                    Statement st = con.createStatement();
                    String querycmd = "select * from board WHERE product_name ='" + pname + "'";
                    ResultSet rs = st.executeQuery(querycmd);


                    ResultSetMetaData rsmd = rs.getMetaData();
                    List<Map<String,Object>> data = null;
                    data = new ArrayList<Map<String,  Object>>();
                    while(rs.next()) {
                        Map<String, Object> datanum = new HashMap<String, Object>();
                        datanum.put("A", rs.getString("board_user_message"));
                        datanum.put("B", rs.getString("time"));
                        datanum.put("C", rs.getString("board_user_id"));
                        data.add(datanum);
                    }
                    String[] from = {"A","B","C"};
                    int[] viewswhere = { R.id.chattext,R.id.timetext,R.id.buyer};
                    ADAhere = new SimpleAdapter(getBaseContext(), data,
                            R.layout.chat2, from, viewswhere);
                    chat2.setAdapter(ADAhere);
                    chat2.setSelection( chat2.getAdapter().getCount()-1);
                } catch (SQLException e) {
                    Toast.makeText(chat.this, e.getMessage().toString(),
                            Toast.LENGTH_LONG).show();
                }



            }
        });





        try {

            Connection con = connectionClass.CONN();
            Statement st = con.createStatement();
            String querycmd = "select * from board WHERE product_name ='" + pname + "'";
            ResultSet rs = st.executeQuery(querycmd);


            ResultSetMetaData rsmd = rs.getMetaData();
            List<Map<String,Object>> data = null;
            data = new ArrayList<Map<String,  Object>>();
            while(rs.next()) {
                Map<String, Object> datanum = new HashMap<String, Object>();
                datanum.put("A", rs.getString("board_user_message"));
                datanum.put("B", rs.getString("time"));
                datanum.put("C", rs.getString("board_user_id"));
                datanum.put("D", rs.getString("board_store_message"));
                datanum.put("E", rs.getString("store_time"));
                data.add(datanum);
            }
            String[] from = {"A","B","C","D","E"};
            int[] viewswhere = { R.id.chattext,R.id.timetext,R.id.buyer,R.id.textView16,R.id.textView17};
            ADAhere = new SimpleAdapter(getBaseContext(), data,
                    R.layout.chat2, from, viewswhere);
            chat2.setAdapter(ADAhere);

        } catch (SQLException e) {
            Toast.makeText(chat.this, e.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void onResume()
    {
        super.onResume();
        ADAhere.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Intent intent4 = new Intent(chat.this,order.class) ;
        Bundle bundle = new Bundle();
        bundle.putString("name", name2);
        bundle.putString("pname", pname2);
        bundle.putDouble("price", price2);
        bundle.putString("store", store2);
        bundle.putString("quality", quality2);
        bundle.putString("querycmd", querycmd3);
        bundle.putString("car", car2);
        intent4.putExtras(bundle);
        startActivity(intent4);
        finish();
    }











}
