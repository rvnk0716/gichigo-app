package com.example.user.login;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class chat2 extends AppCompatActivity {
    SimpleAdapter ADAhere;
    ListView chat;

    ConnectionClass connectionClass;
    private Toolbar mToolbar;
    String name2;
    ImageView img2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
        chat = (ListView) findViewById(R.id.lv9);
        img2=(ImageView)findViewById(R.id.img) ;
        mToolbar = (Toolbar) findViewById(R.id.toolbar9);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        connectionClass = new ConnectionClass();

        Bundle bundle = this.getIntent().getExtras();

        final String name = bundle.getString("name");
        name2=name;
        final String car = bundle.getString("car");


        LOAD chat2 = new LOAD();
        chat2.execute("");
    }

    public class LOAD extends AsyncTask<String, String, String> {
        private ProgressDialog progressBar;
        @Override
        protected void onPreExecute() {
            //執行前 設定可以在這邊設定
            super.onPreExecute();
            progressBar = new ProgressDialog(chat2.this);
            progressBar.setMessage("Loading...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setCancelable(false);
            progressBar.show();
        }
        @Override
        protected void onPostExecute(String r) {
            chat.setAdapter(ADAhere);

            progressBar.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {



            try {
                Connection con = connectionClass.CONN();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select product_id from board WHERE board_user_id='" + name2 +  "'");

                ResultSetMetaData rsmd = rs.getMetaData();
                List<Map<String, Object>> data = null;
                data = new ArrayList<Map<String, Object>>();
                while (rs.next()) {
                    Map<String, Object> datanum = new HashMap<String, Object>();
                    Statement st2 = con.createStatement();
                    ResultSet rs2 = st2.executeQuery( "select * from product where product_id='"+ rs.getInt("product_id")+"'" );
                    while(rs2.next()) {
                        byte[] image = rs2.getBytes("product_picture");
                        Bitmap picturebmp = BitmapFactory.decodeByteArray(image, 0, image.length);
                        datanum.put("D", picturebmp);
                        datanum.put("E", rs2.getString("product_picture"));
                        datanum.put("A", rs2.getString("product_name"));
                        datanum.put("B", "$" + rs2.getString("product_price"));
                        datanum.put("Z", rs2.getDouble("product_price"));
                        datanum.put("C", rs2.getString("product_quality"));
                        datanum.put("F", rs2.getString("product_store"));
                        datanum.put("g", rs2.getString("product_date"));
                        datanum.put("H", rs2.getString("product_id"));
                        data.add(datanum);
                    }
                }

                String[] from = {"A", "B", "C", "D", "g"};

                int[] viewswhere = {R.id.Pname, R.id.Pprice, R.id.phone, R.id.img, R.id.textdate};
                ADAhere = new SimpleAdapter(getBaseContext(), data,
                        R.layout.item, from, viewswhere);




                ADAhere.setViewBinder(new SimpleAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view,Object data, String textRepresentation) {
                        if ((view instanceof ImageView) & (data instanceof Bitmap)) {
                            ImageView iv = (ImageView) view;
                            Bitmap bmp = (Bitmap) data;
                            iv.setImageBitmap(bmp);
                            return true;
                        }
                        return false;
                    }
                });

            } catch ( SQLException e) {

            }

            return null;
        }
    }


}
