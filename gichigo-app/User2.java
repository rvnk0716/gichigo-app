package com.example.user.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class User2 extends AppCompatActivity {
    private Spinner spinner,spinner2;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapter2;
    private static final String[] area={"台北市","新北市","桃園市","台中市","高雄市"};
    private String[] taipei = new String[]{"","中正區","中山區","萬華區","士林區"};
    private String[][] area2 = new String[][]{{"","中正區","中山區","萬華區","士林區"},{"","板橋區","新店區","永和區","土城區"},
            {"","中壢區","平鎮區","龍潭區","八德區"},{"","豐原區","大甲區","后里區","沙鹿區"},{"","三民區","左營區","鳳山區","美濃區"}};
    private EditText search;
    private Context context;

    private ProgressDialog dialog = null;
    ConnectionClass connectionClass;
    SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
    String str = df.format(Calendar.getInstance().getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user2);
        final CharSequence title = getString(R.string.wait);
        final CharSequence string  = getString(R.string.wait2);


        Bundle bundle = this.getIntent().getExtras();
        final String name = bundle.getString("name");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        context = this;
        adapter = new ArrayAdapter<String>(this,R.layout.spinner,area);
        spinner = (Spinner) findViewById(R.id.spinner);
        adapter.setDropDownViewResource(R.layout.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(selectListener);

        adapter2 = new ArrayAdapter<String>(this,R.layout.spinner,taipei);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        adapter2.setDropDownViewResource(R.layout.spinner);
        spinner2.setAdapter(adapter2);
        spinner2= (Spinner) findViewById(R.id.spinner2);
        search = (EditText) findViewById(R.id.search_et_input);
        search.setBackgroundResource(R.drawable.search_edittext_shape);
        connectionClass = new ConnectionClass();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);toggle.syncState();
        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 登出
              onBackPressed();
                finish();
            }
        });

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         // 個人資料
                Intent intent4 = new Intent(User2.this,User.class) ;
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                intent4.putExtras(bundle);
                startActivity(intent4);
                finish();
                overridePendingTransition(R.animator.left_in, R.animator.right_oot);
                onBackPressed();
            }
        });
        // 教學
        findViewById(R.id.teach).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent4 = new Intent(User2.this,teach.class) ;
                startActivity(intent4);
                onBackPressed();
            }
        });
        // 推播
        findViewById(R.id.fcm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent11 = new Intent(User2.this,fcmset.class) ;
                startActivity(intent11);
                overridePendingTransition(R.animator.left_in, R.animator.right_oot);

            }
        });
        // 商品列表
            findViewById(R.id.near).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(User2.this,product.class) ;
                String querycmd = "select * from product WHERE product_date >= '"+str+"' and product_quality > 0";
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("querycmd", querycmd);
                bundle.putString("near", "near");
                intent5.putExtras(bundle);
                startActivity(intent5);

            }
        });
        findViewById(R.id.button13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
           //主頁面的確定搜尋
        findViewById(R.id.btclick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(User2.this,title,string,true);
                dialog.getWindow().setLayout(700,400);
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            dialog.dismiss();
                        }
                    }
                }.start();
                Intent intent6 = new Intent(User2.this,product.class) ;

                final String search2 = search.getText().toString();
                final String search3 =spinner2.getSelectedItem().toString();

                if(search3 ==""){
                    String querycmd = "select * from product WHERE product_date >= '"+str+"' and product_quality > 0 and product_name LIKE '%" +search2 + "%'";
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
                    bundle.putString("querycmd", querycmd);
                    intent6.putExtras(bundle);
                    startActivity(intent6);
                    overridePendingTransition(R.animator.anim, R.animator.slide_out_left);
                }else {
                    try {
                        Connection con = connectionClass.CONN();
                        Statement st = con.createStatement();

                        ResultSet rs = st.executeQuery("select * from store where store_name='" + search3 + "'");
                        if (rs.next()) {
                            final String string2 = rs.getString("store_id");

                            String querycmd = "select * from product WHERE product_date >= '"+str+"' and product_quality > 0 and product_name LIKE '%" + search2 + "%' and product_store ='" + string2 + "'";
                            Bundle bundle = new Bundle();
                            bundle.putString("name", name);
                            bundle.putString("querycmd", querycmd);
                            intent6.putExtras(bundle);
                            startActivity(intent6);
                            overridePendingTransition(R.animator.anim, R.animator.slide_out_left);
                        } else {
                            Toast.makeText(User2.this, "此區沒有商品!", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ex) {
                        System.err.println("Got an exception! ");
                        System.err.println(ex.getMessage());
                    }

                }

            }
        });
        //飲料類
        findViewById(R.id.button26).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(User2.this,title,string,true);
                dialog.getWindow().setLayout(700,400);
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            dialog.dismiss();
                        }
                    }
                }.start();

                Intent intent6 = new Intent(User2.this,product.class) ;
                String querycmd = "select * from product WHERE product_date >= '"+str+"' and product_quality > 0 and product_class ='飲料類'";
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("querycmd", querycmd);
                intent6.putExtras(bundle);
                startActivity(intent6);
                overridePendingTransition(R.animator.anim, R.animator.slide_out_left);

            }
        });
        //食品類
        findViewById(R.id.button25).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(User2.this,title,string,true);
                dialog.getWindow().setLayout(700,400);
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            dialog.dismiss();
                        }
                    }
                }.start();
                Intent intent6 = new Intent(User2.this,product.class) ;
                String querycmd = "select * from product WHERE product_date >= '"+str+"' and product_quality > 0 and product_class ='餅乾類'";
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("querycmd", querycmd);
                intent6.putExtras(bundle);
                startActivity(intent6);
                overridePendingTransition(R.animator.anim, R.animator.slide_out_left);
            }
        });
        //喜愛商品
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = new Intent(User2.this,product.class) ;

                String querycmd = "select * from car WHERE owner='" + name +  "'";

                Bundle bundle = new Bundle();
                bundle.putString("querycmd", querycmd);
                bundle.putString("name", name);
                bundle.putString("car", "car");
                intent6.putExtras(bundle);
                startActivity(intent6);
                overridePendingTransition(R.animator.left_in, R.animator.right_oot);
            }
        });
        //留言紀錄
        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = new Intent(User2.this,chat2.class) ;

                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("car", "car");
                intent6.putExtras(bundle);
                startActivity(intent6);
                overridePendingTransition(R.animator.left_in, R.animator.right_oot);
            }
        });
        //我的訂單
        findViewById(R.id.but).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(User2.this,order2.class) ;
                String querycmd = "select * from order2 WHERE owner='" + name +  "'";
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("querycmd", querycmd);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.animator.left_in, R.animator.right_oot);
            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private AdapterView.OnItemSelectedListener selectListener = new AdapterView.OnItemSelectedListener(){
        public void onItemSelected(AdapterView<?> parent, View v, int position,long id){
            //讀取第一個下拉選單是選擇第幾個
            int pos = spinner.getSelectedItemPosition();
            //重新產生新的Adapter，用的是二維陣列type2[pos]
            adapter2 = new ArrayAdapter<String>(context,R.layout.spinner, area2[pos]);
            //載入第二個下拉選單Spinner
            spinner2.setAdapter(adapter2);
        }

        public void onNothingSelected(AdapterView<?> arg0){

        }

    };


}
