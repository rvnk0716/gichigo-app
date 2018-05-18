package com.example.user.login;
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
import android.os.CountDownTimer;
import android.provider.Settings;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class order extends MapsActivity {

    ConnectionClass connectionClass;
    String name2, pname2, store2, quality2, querycmd3, car2, date2,near2;
    Double  price2;
    String ID;
    ListView chat;
    SimpleAdapter ADAhere, ADAhere2;
    ImageView like;

    private Toolbar mToolbar;
    private double latitude = 0.0;   //經度
    private double longitude = 0.0; //緯度
    int count = 1;
    int total = 0;
    private LocationManager lms;
    private String bestProvider = LocationManager.GPS_PROVIDER;
    String ptime;
    CountDownTimer mCountDownTimer;

    TextView mTextView;
    @Override
    public void onResume() {
        super.onResume();
        ADAhere.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ImageButton Imagebutton = (ImageButton) findViewById(R.id.imageButton);
        Imagebutton.setImageResource(R.drawable.ssdk_oks_classic_whatsapp);
        connectionClass = new ConnectionClass();
        final TextView abc4 = (TextView) findViewById(R.id.txt_date);
        final TextView abc5 = (TextView) findViewById(R.id.txt_store_name);
        final TextView abc6 = (TextView) findViewById(R.id.textView25);
        final TextView abc7 = (TextView) findViewById(R.id.txt_tel);
        final TextView abc = (TextView) findViewById(R.id.txt_name);
        final TextView abc2 = (TextView) findViewById(R.id.txt_price);
        final ImageView abc3 = (ImageView) findViewById(R.id.imageView4);
        final TextView countnum = (TextView) findViewById(R.id.tvNum2);
        final TextView totalprice = (TextView) findViewById(R.id.totalprice);
        Bundle bundle = this.getIntent().getExtras();
        final String name = bundle.getString("name");
        final String pname = bundle.getString("pname");
        final Double price = bundle.getDouble("price");
        final String store = bundle.getString("store");
        final String quality = bundle.getString("quality");
        final String car = bundle.getString("car");
        final String near = bundle.getString("near");
        final String date = bundle.getString("date");
        final String querycmd2 = bundle.getString("querycmd");
        final String pid = bundle.getString("pid");
        near2=near;
        name2 = name;
        pname2 = pname;
        price2 = price;
        store2 = store;
        quality2 = quality;
        querycmd3 = querycmd2;
        car2 = car;
        date2 = date;
        abc2.setText("$" + (int)Math.round(price));
        totalprice.setText("$" + (int)Math.round(price));
        final int price3 = (int)Math.round(price);
        total += price3 * count;

        abc.setText(pname);
        pname2 = pname;

        chat = (ListView) findViewById(R.id.chat2);
        like = (ImageView) findViewById(R.id.like);
        mToolbar = (Toolbar) findViewById(R.id.toolbar5);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(car!=null) {
                    onBackPressed();
                    overridePendingTransition(R.animator.anim, R.animator.slide_out_left);
                }else {
                    onBackPressed();
                    overridePendingTransition(R.animator.left_in, R.animator.right_oot);
                }
            }
        });


        findViewById(R.id.btnmap).setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(order.this, "請打開GPS", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(order.this, "請打開網路", Toast.LENGTH_LONG).show();

                }

                String addressString = abc6.getText().toString().trim();
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

        findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "tel:" + abc7.getText().toString();
                Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(number));
                startActivity(it);
            }
        });
        findViewById(R.id.btnhome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(order.this, User2.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                i.putExtras(bundle);
                startActivity(i);
                finish();
                overridePendingTransition(R.animator.left_in, R.animator.right_oot);
            }
        });
        findViewById(R.id.ivAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                countnum.setText(String.valueOf(count));
                total = price3 * count;
                totalprice.setText("$" + String.valueOf(total));
            }
        });
        findViewById(R.id.ivReduce).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 1) {
                    count--;
                    countnum.setText(String.valueOf(count));
                    total = price3 * count;
                    totalprice.setText("$" + String.valueOf(total));
                }
            }
        });


        if (car != null) {
            like.setImageResource(R.mipmap.icon_collected_black);
        } else {
            like.setImageResource(R.mipmap.icon_uncollect_black);
        }
        findViewById(R.id.order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = new Order();
                order.execute("");
            }
        });

        findViewById(R.id.like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (car != null) {
                    DeletePro deletePro = new DeletePro();
                    deletePro.execute("");
                    onBackPressed();
                    overridePendingTransition(R.animator.anim, R.animator.slide_out_left);
                } else {
                    Insert re = new Insert();
                    re.execute("");
                }
            }
        });
        findViewById(R.id.textView4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(order.this, chat.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("pname", pname);
                bundle.putDouble("price", price);
                bundle.putString("store", store);
                bundle.putString("quality", quality);
                bundle.putString("car", car);
                bundle.putString("querycmd", querycmd2);
                bundle.putString("pid",pid);
                intent4.putExtras(bundle);
                startActivity(intent4);
                finish();
            }
        });

        try {
            Connection con = connectionClass.CONN();
            Statement st = con.createStatement();
            String querycmd = "select * from board WHERE product_name ='" + pname + "'";
            ResultSet rs = st.executeQuery(querycmd);

            ResultSetMetaData rsmd = rs.getMetaData();
            List<Map<String, Object>> data = null;
            data = new ArrayList<Map<String, Object>>();
            while (rs.next()) {
                Map<String, Object> datanum = new HashMap<String, Object>();
                datanum.put("A", rs.getString("board_user_message"));
                datanum.put("B", rs.getString("time"));
                datanum.put("C", "買家:");
                datanum.put("D", rs.getString("board_user_id"));
                datanum.put("E", rs.getString("product_name"));
                datanum.put("F", rs.getString("board_store_message"));
                datanum.put("G", rs.getString("store_time"));
                datanum.put("H", rs.getString("product_id"));
                data.add(datanum);
            }
            String[] from = {"A", "B", "C", "D", "F", "G"};
            int[] viewswhere = {R.id.chattext, R.id.timetext, R.id.textView11, R.id.buyer, R.id.textView16, R.id.textView17};

            ADAhere = new SimpleAdapter(getBaseContext(), data, R.layout.chat2, from, viewswhere);
            chat.setAdapter(ADAhere);

            if (ADAhere.isEmpty()) {
                Map<String, Object> datanum2 = new HashMap<String, Object>();
                datanum2.put("E", pname);
                datanum2.put("gochat", "目前還沒有留言唷");
                data.add(datanum2);
                int[] viewswhere2 = {R.id.gochat};
                ADAhere2 = new SimpleAdapter(getBaseContext(), data, R.layout.gochat, new String[]{"gochat"}, viewswhere2);
                chat.setAdapter(ADAhere2);
            }
            chat.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADAhere.getItem(position);
                    Intent intent4 = new Intent(order.this, chat.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
                    String VehicleId = (String) obj.get("E");
                    bundle.putString("pname", VehicleId);
                    bundle.putDouble("price", price);
                    bundle.putString("store", store);
                    bundle.putString("quality", quality);
                    bundle.putString("car", car);
                    bundle.putString("querycmd", querycmd2);

                    bundle.putString("pid",pid);
                    intent4.putExtras(bundle);
                    startActivity(intent4);
                    finish();
                }
            });
        } catch (SQLException e) {
            Toast.makeText(order.this, e.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
        }

        try {
            Connection con = connectionClass.CONN();
            Statement st = con.createStatement();
            Statement st2 = con.createStatement();
            ResultSet rs = st.executeQuery("select * from store where store_id='" + store + "'");
            ResultSet rs2 = st2.executeQuery("select * from product where product_name='" + pname + "'");

            ResultSetMetaData rsmd = rs.getMetaData();
            if (rs.next()) {
                abc5.setText(rs.getString("store_name"));
                abc6.setText(rs.getString("store_address"));
                abc7.setText(rs.getString("store_tel"));
            }
            if (rs2.next()) {
                abc4.setText(rs2.getString("product_date"));
                ptime=rs2.getString("product_date");
                byte[] image = rs2.getBytes("product_picture");
                Bitmap picturebmp = BitmapFactory.decodeByteArray(image, 0, image.length);
                abc3.setImageBitmap(picturebmp);
                ID = rs2.getString("product_id");
            }
        } catch (Exception ex) {
            System.err.println("Got an exception! ");
            System.err.println(ex.getMessage());
        }
        time();
    }

    private void getLocation(Location location) { //將定位資訊顯示在畫面中
        if (location != null) {
            longitude = location.getLongitude();   //取得經度
            latitude = location.getLatitude();     //取得緯度
        }
    }

    public class DeletePro extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;
        @Override
        protected void onPostExecute(String r) {
            if (isSuccess) {
                Toast.makeText(order.this, "移除成功", Toast.LENGTH_SHORT).show();
                like.setImageResource(R.mipmap.icon_uncollect_black);
            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = "delete from car where owner='" + name2 + "' and product_name='" + pname2 + "' and product_store='" + store2 + "'";
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

    public class Insert extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess2 = false;

        @Override
        protected void onPostExecute(String r) {
            if (isSuccess2) {
                Toast.makeText(order.this, getString(R.string.add), Toast.LENGTH_SHORT).show();
                like.setImageResource(R.mipmap.icon_collected_black);
            } else {
                Toast.makeText(order.this, getString(R.string.fall2), Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = "select * from car where owner='" + name2 + "' and product_name='" + pname2 + "'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        isSuccess2 = false;
                    } else {
                        String query2 = "insert into car (owner,product_name,product_price,product_quality,product_id,product_store,product_date) values ('" + name2 + "', '" + pname2 + "', '" + price2 + "', '" + quality2 + "', '" + ID + "', '" + store2 + "', '" + date2 + "')";
                        PreparedStatement preparedStatement = con.prepareStatement(query2);
                        preparedStatement.executeUpdate();
                        z = "Added Successfully";
                        isSuccess2 = true;
                    }

                }
            } catch (Exception ex) {
                isSuccess2 = false;
                z = "Exceptions";
            }

            return z;
        }
    }

    public class Order extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPostExecute(String r) {
            if (isSuccess) {

                AlertDialog alertDialog = new AlertDialog.Builder(order.this).create();
                alertDialog.setTitle("訂購成功!!");
                alertDialog.setMessage("商品將會保留20分鐘\n如需延長請使用商品留言板通知店家");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
// here you can add functions
                    }
                });
                alertDialog.setIcon(R.drawable.icon);
                alertDialog.show();
            } else {
                Toast.makeText(order.this, getString(R.string.fall3), Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        protected String doInBackground(String... params) {


            try {

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar c=Calendar.getInstance();
                c.add(Calendar.MINUTE,20);
                String date=df.format(c.getTime());

                Connection con = connectionClass.CONN();

                if (con == null) {

                    z = "Error in connection with SQL server";
                } else {
                    String query = "select * from order2 where owner='" + name2 + "' and product_name='" + pname2 + "'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        isSuccess = false;
                    } else {

                        String query2 = "insert into order2 (owner,product_name,total_price,order_quality,product_id,product_store,product_state,product_pick) values ('" + name2 + "', '" + pname2 + "', '" + total + "', '" + count + "', '" + ID + "', '" + store2 + "', '" + "已預訂" +"','"+date+ "')";
                        PreparedStatement preparedStatement = con.prepareStatement(query2);
                        preparedStatement.executeUpdate();
                        z = "Added Successfully";
                        isSuccess = true;
                    }

                }
            } catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions";
            }

            return z;
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent4 = new Intent(order.this, product.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", name2);
        bundle.putString("pname", pname2);
        bundle.putDouble("price", price2);
        bundle.putString("store", store2);
        bundle.putString("quality", quality2);
        bundle.putString("querycmd", querycmd3);
        bundle.putString("car", car2);
        bundle.putString("near", near2);
        bundle.putString("date", date2);
        intent4.putExtras(bundle);
        startActivity(intent4);
        finish();
        if(car2!=null) {
            overridePendingTransition(R.animator.anim, R.animator.slide_out_left);
        }else {
            overridePendingTransition(R.animator.left_in, R.animator.right_oot);
        }
    }

    void time() {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date d1 = df.parse(ptime+" 12:00:00");//商品時間"

            Date d2 = new Date(System.currentTimeMillis());//現在時間
            long diff = d1.getTime() - d2.getTime();//這樣得到的差值是微秒級別

            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);

            long mInitialTime = DateUtils.DAY_IN_MILLIS * days +
                    DateUtils.HOUR_IN_MILLIS * hours +
                    DateUtils.MINUTE_IN_MILLIS * minutes;

            mTextView = (TextView) findViewById(R.id.empty);

            mCountDownTimer = new CountDownTimer(mInitialTime, 1000) {
                StringBuilder time = new StringBuilder();
                @Override
                public void onFinish() {
                    mTextView.setText(DateUtils.formatElapsedTime(0));
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
                    mTextView.setText(time.toString());
                }
            }.start();
             //Toast.makeText(order.this, ""+days+"天"+hours+"小時"+minutes+"分", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {}
    }

}










