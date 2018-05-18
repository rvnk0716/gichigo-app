
package com.example.user.login;
import java.io.File;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;



public class order2 extends ActionBarActivity {
    private LruCache<String, Bitmap> mMemoryCache;

    private double latitude = 0.0;   //經度
    private double longitude = 0.0; //緯度
    private Toolbar mToolbar;
    private LocationManager lms;
    private String bestProvider = LocationManager.GPS_PROVIDER;
    final Context context = this;
    ImageView img2;
    ListView order;
    SimpleAdapter ADAhere;
    ConnectionClass connectionClass;
    String qqq;
    String productname2,name2;
    Button del;
    CountDownTimer mCountDownTimer;

    private File cacheDir;
    @SuppressLint("NewApi")
    private Connection CONN(String _user, String _pass, String _DB,
                            String _server) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnURL = "jdbc:jtds:sqlserver://" + "192.192.105.111" + ";"
                    + "databaseName=" + "gichigo" + ";user=" + "zxc123" + ";password="
                    +"123" + ";";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order2);



        connectionClass = new ConnectionClass();
        img2=(ImageView)findViewById(R.id.img2) ;
        order = (ListView) findViewById(R.id.lv2);
        del= (Button) findViewById(R.id.button16);


        mToolbar = (Toolbar) findViewById(R.id.toolbar2);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Bundle bundle = this.getIntent().getExtras();
        final String querycmd = bundle.getString("querycmd");
        final String name = bundle.getString("name");
        name2 = name;
        qqq=querycmd;
        LOAD order2 = new LOAD();
        order2.execute("");




    }

    public class LOAD extends AsyncTask<String, Integer, String> {
        private ProgressDialog progressBar;
        @Override
        protected void onPreExecute() {
            //執行前 設定可以在這邊設定
            super.onPreExecute();

            progressBar = new ProgressDialog(order2.this);
            progressBar.setMessage("Loading...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setCancelable(false);
            progressBar.show();

        }
        @Override
        protected void onPostExecute(String r) {
            order.setAdapter(ADAhere);
            progressBar.dismiss();

            if(ADAhere.isEmpty())
            {
                setContentView(R.layout.empty);
                mToolbar = (Toolbar) findViewById(R.id.toolbar2);
                mToolbar.setTitle("");
                mToolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
                setSupportActionBar(mToolbar);
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        }

        @Override
        protected String doInBackground(String... params) {



            try {
                Connection con = connectionClass.CONN();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(qqq);

                ResultSetMetaData rsmd = rs.getMetaData();
                List<Map<String, Object>> data = null;
                data = new ArrayList<Map<String, Object>>();
                while(rs.next()) {
                   final Map<String, Object> datanum = new HashMap<String, Object>();

                    datanum.put("A", rs.getString("product_name"));
                    datanum.put("B", rs.getString("order_quality"));
                    datanum.put("C", rs.getString("total_price"));
                    datanum.put("Z", rs.getString("product_pick"));

                    Statement st2 = con.createStatement();
                    Statement st3 = con.createStatement();
                    ResultSet rs2 = st2.executeQuery( "select product_picture from product where product_id='"+ rs.getInt("product_id")+"'" );
                    ResultSet rs3 = st3.executeQuery("select store_address from store where store_id='" + rs.getString("product_store") + "'");
                    while(rs3.next())
                    {
                        datanum.put("K",rs3.getString("store_address"));
                    }
                    while(rs2.next()) {
                        byte[] image = rs2.getBytes("product_picture");
                        Bitmap picturebmp = BitmapFactory.decodeByteArray(image, 0, image.length);
                        datanum.put("D", picturebmp);
                    }
                    datanum.put("E", "A0"+rs.getString("ordernum"));
                    datanum.put("F", rs.getString("ordernum"));



                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date d1=df.parse(rs.getString("product_pick"));
                    Date d2 = new Date(System.currentTimeMillis());//現在時間

                    if(d1.before(d2)){
                        datanum.put("L", "已逾時");
                    }else {
                        datanum.put("L",rs.getString("product_state"));
                    }
                    data.add(datanum);
                }
                String[] from = {"A", "B", "C", "D", "E","K","L"};
                int[] viewswhere = {R.id.Pname2, R.id.order_quality, R.id.toprice, R.id.img2, R.id.textView3,R.id.tvaddr,R.id.state};



                ADAhere = new MySimpleAdapter(getBaseContext(), data, R.layout.shoppingcartitem, from, viewswhere);





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
                Toast.makeText(order2.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
            catch (Exception ex) {

            }
                                      return null;
        }
    }


    public class MySimpleAdapter extends SimpleAdapter{

        Context context;

        public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data,
                               int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.context = context;
            // TODO Auto-generated constructor stub
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View v= super.getView(position, convertView, parent);
            Button btn=(Button) v.findViewById(R.id.button16);
            btn.setTag(position);
            TextView maps=(TextView) v.findViewById(R.id.tvaddr);
            maps.setTag(position);

            final TextView time2=(TextView) v.findViewById(R.id.picktime);
            time2.setTag(position);

            final TextView time3=(TextView) v.findViewById(R.id.state);
            time3.setTag(position);

            HashMap<String, Object> obj = (HashMap<String, Object>) ADAhere.getItem(position);
            final String water = (String) obj.get("F");
            final String address = (String) obj.get("K");
            final String datetime = (String) obj.get("Z");
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Date d1 = df.parse(datetime);//商品時間"

                Date d2 = new Date(System.currentTimeMillis());//現在時間
                long diff = d1.getTime() - d2.getTime();//這樣得到的差值是微秒級別

                long days = diff / (1000 * 60 * 60 * 24);
                long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);

                long mInitialTime = DateUtils.DAY_IN_MILLIS * days +
                        DateUtils.HOUR_IN_MILLIS * hours +
                        DateUtils.MINUTE_IN_MILLIS * minutes;



                mCountDownTimer = new CountDownTimer(mInitialTime, 1000) {
                    StringBuilder time = new StringBuilder();
                    @Override
                    public void onFinish() {
                       time2.setText(DateUtils.formatElapsedTime(0));
                        time3.setText("已逾時");
                        //mTextView.setText("Times Up!");
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {
                        time.setLength(0);
                        // Use days if appropriate
                        if(millisUntilFinished > DateUtils.DAY_IN_MILLIS) {
                            long count = millisUntilFinished / DateUtils.DAY_IN_MILLIS;
                            if(count > 1)
                                time.append(count).append(" 天 ");
                            else
                                time.append(count).append(" 天 ");

                            millisUntilFinished %= DateUtils.DAY_IN_MILLIS;
                        }

                        time.append(DateUtils.formatElapsedTime(Math.round(millisUntilFinished / 1000d)));
                        time2.setText(time.toString());
                    }
                }.start();
                //Toast.makeText(order.this, ""+days+"天"+hours+"小時"+minutes+"分", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {}

            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    productname2=water;
                    cancel();
                }
            });

            maps.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    lms = (LocationManager) getSystemService(LOCATION_SERVICE);
                    Criteria criteria = new Criteria();  //資訊提供者選取標準
                    bestProvider = lms.getBestProvider(criteria, true);

                    try {
                        Location location = lms.getLastKnownLocation(bestProvider);
                        getLocation(location);
                    } catch (SecurityException e) {
                        Toast.makeText(order2.this, "請打開GPS", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }

                    LocationListener locationListener = new LocationListener() {

                        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
                        @Override
                        public void onStatusChanged(String provider, int status,
                                                    Bundle extras) {

                        }

                        // Provider被enable时触发此函数，比如GPS被打开
                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        // Provider被disable时触发此函数，比如GPS被关闭
                        @Override
                        public void onProviderDisabled(String provider) {

                        }

                        // 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
                        @Override
                        public void onLocationChanged(Location location) {
                            if (location != null) {
                                Log.e("Map",
                                        "Location changed : Lat: "
                                                + location.getLatitude() + " Lng: "
                                                + location.getLongitude());
                                latitude = location.getLatitude(); // 经度
                                longitude = location.getLongitude(); // 纬度
                            }
                        }
                    };
                    try {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
                        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude(); //经度
                            longitude = location.getLongitude(); //纬度
                        }
                    } catch (SecurityException e) {
                        Toast.makeText(order2.this, "請打開網路", Toast.LENGTH_LONG).show();

                    }

                    String addressString = address.toString().trim();
                    Geocoder geoCoder = new Geocoder(getBaseContext());
                    try {
                        List<Address> addressLocation = geoCoder.getFromLocationName(addressString, 1);
                        double latitude2 = addressLocation.get(0).getLatitude();
                        double longitude2 = addressLocation.get(0).getLongitude();
                        String saddr = "saddr=" + latitude + "," + longitude;
                        String daddr = "daddr=" + latitude2 + "," + longitude2;
                        String uriString = "http://maps.google.com/maps?" + saddr + "&" + daddr;
                        Uri uri = Uri.parse(uriString);
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);

                        // If you want to get rid of the dialog,
                        // Before the startActivity() add this
                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                        startActivity(intent);
                    } catch (IOException e) {
                        Log.e("錯誤", e.toString());
                    }

                }
            });





            return v;
        }
    }

    void cancel() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("確定要取消訂單?")
                .setCancelable(false)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DeletePro deletePro = new DeletePro();
                        deletePro.execute("");

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public class DeletePro extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPostExecute(String r) {
            if (isSuccess) {
                Toast.makeText(order2.this, "移除成功", Toast.LENGTH_SHORT).show();
                LOAD order2 = new LOAD();
                order2.execute("");
            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = "delete  from order2 where ordernum='" +productname2 + "'";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();
                    z = "delete Successfully";
                    isSuccess = true;
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions";
            }
            return z;
        }
    }

    private void getLocation(Location location) { //將定位資訊顯示在畫面中
        if (location != null) {
            longitude = location.getLongitude();   //取得經度
            latitude = location.getLatitude();     //取得緯度
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.anim, R.animator.slide_out_left);

    }






}