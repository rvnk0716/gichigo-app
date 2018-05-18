
package com.example.user.login;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;



public class product extends ActionBarActivity {
  
    final Context context = this;
    ImageView img2;
    private Toolbar mToolbar;
    ListView lstcountry;
    SimpleAdapter ADAhere;
    ConnectionClass connectionClass;
    String qqq ,near2;
    TextView tv;
    private LocationManager lms;
    private double latitude = 0.0;   //經度
    private double longitude = 0.0; //緯度
    int count=1;
    double distance;
    String stringdouble2;
    private List<Map<String, Object>> Pois = new ArrayList<Map<String, Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        connectionClass = new ConnectionClass();
        img2=(ImageView)findViewById(R.id.img) ;
        lstcountry = (ListView) findViewById(R.id.lv);
        tv=(TextView) findViewById(R.id.textView14);
        Bundle bundle = this.getIntent().getExtras();
        final String name = bundle.getString("name");
        final String querycmd = bundle.getString("querycmd");
        final String car = bundle.getString("car");
        final String near = bundle.getString("near");
        if(car!=null){
            tv.setText("喜愛商品");
        }
        qqq=querycmd;
        near2=near;
        findViewById(R.id.btnssort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  qqq=querycmd;
              // qqq=qqq+"ORDER BY product_price";
              //  LOAD order2 = new LOAD();
              //  order2.execute("");
                PriceSort(Pois);
                lstcountry.setAdapter(ADAhere);
            }
        });
        findViewById(R.id.btndssort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   qqq=querycmd;
                   qqq=qqq+"ORDER BY product_date";
                  count=0;
                   LOAD order2 = new LOAD();
                   order2.execute("");

            }
        });
        findViewById(R.id.pois).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DistanceSort(Pois);
                    lstcountry.setAdapter(ADAhere);
                } catch (Exception ex) {
                    System.err.println("Got an exception! ");
                    System.err.println(ex.getMessage());
                    Toast.makeText(product.this, "無法確切定位您的位置", Toast.LENGTH_LONG).show();
                }
            }
        });

        mToolbar = (Toolbar) findViewById(R.id.toolbar6);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        setSupportActionBar(mToolbar);
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


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lms = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

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
            distance=Double.NaN;
            stringdouble2="";
        }

        if(near!=null){
            findViewById(R.id.btnssort).setVisibility(View.INVISIBLE);
            findViewById(R.id.btndssort).setVisibility(View.INVISIBLE);
            findViewById(R.id.pois).setVisibility(View.INVISIBLE);
            LOAD2 order2 = new LOAD2();
            order2.execute("");
        }else {
            LOAD order2 = new LOAD();
            order2.execute("");}

        lstcountry.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
// TODO Auto-generated method stub
                HashMap<String, Object> obj = (HashMap<String, Object>) ADAhere.getItem(position);
                String VehicleId = (String) obj.get("A");
                Double price = (Double) obj.get("Z");
                String quality = (String) obj.get("C");
                String store = (String) obj.get("F");
                String date = (String) obj.get("g");

                Intent intent4 = new Intent(product.this,order.class) ;
                Bundle bundle = new Bundle();
                bundle.putString("pname", VehicleId);
                bundle.putDouble("price", price);
                bundle.putString("quality", quality);
                bundle.putString("store", store);
                bundle.putString("name", name);
                bundle.putString("date", date);
                bundle.putString("car", car);
                bundle.putString("near", near);
                bundle.putString("querycmd", querycmd);
                String pid = (String) obj.get("H");
                bundle.putString("pid",pid);
                intent4.putExtras(bundle);
                startActivity(intent4);
                finish();
                 if(car!=null){
                     overridePendingTransition(R.animator.left_in, R.animator.right_oot);
                 }else {
                     overridePendingTransition(R.animator.anim, R.animator.slide_out_left);
                 }
                Toast.makeText(product.this, VehicleId, Toast.LENGTH_LONG).show();
            }
        });
    }

    public class LOAD extends AsyncTask<String, Integer, String> {
        private ProgressDialog progressBar;

        @Override
        protected void onPreExecute() {
            //執行前 設定可以在這邊設定
            super.onPreExecute();
            progressBar = new ProgressDialog(product.this);
            progressBar.setMessage("Loading...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setCancelable(false);
            progressBar.show();
        }
        @Override
        protected void onPostExecute(String r) {

            lstcountry.setAdapter(ADAhere);
            if(count==1){
                try {
                    DistanceSort(Pois);
                    lstcountry.setAdapter(ADAhere);
                    if(distance>100000) {
                        Toast.makeText(product.this, "定位不佳，如需得知距離請開啟GPS", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    System.err.println("Got an exception! ");
                    System.err.println(ex.getMessage());
                    Toast.makeText(product.this, "無法確切定位您的位置", Toast.LENGTH_LONG).show();
                }
            }
            if(ADAhere.isEmpty())
            {
                setContentView(R.layout.lovempty);
                mToolbar = (Toolbar) findViewById(R.id.toolbar6);
                mToolbar.setTitle("");
                mToolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
                setSupportActionBar(mToolbar);
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                        overridePendingTransition(R.animator.anim, R.animator.slide_out_left);
                    }
                });
            }
            progressBar.dismiss();
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
                while (rs.next()) {
                    Map<String, Object> datanum = new HashMap<String, Object>();
                    Statement st2 = con.createStatement();
                    ResultSet rs2 = st2.executeQuery( "select product_picture from product where product_id='"+ rs.getInt("product_id")+"'" );
                    while(rs2.next()) {
                        byte[] image = rs2.getBytes("product_picture");
                        Bitmap picturebmp = BitmapFactory.decodeByteArray(image, 0, image.length);
                        datanum.put("D", picturebmp);
                    }
                    Statement st3 = con.createStatement();
                    ResultSet rs3 = st3.executeQuery("select store_address from store where store_id='" + rs.getString("product_store") + "'");
                    while(rs3.next())
                    {
                        String addressString=rs3.getString("store_address");
                        Geocoder geoCoder = new Geocoder(getBaseContext());
                        try {
                            List<Address> addressLocation = geoCoder.getFromLocationName(addressString, 1);
                            double latitude2 = addressLocation.get(0).getLatitude();
                            double longitude2 = addressLocation.get(0).getLongitude();

                            String stringdouble=(DistanceText(distance(latitude,longitude,latitude2,longitude2)));
                            double stringdoubl2=distance(latitude,longitude,latitude2,longitude2);
                            distance=stringdoubl2;
                            datanum.put("K", stringdouble);
                            datanum.put("Q", stringdoubl2);
                        } catch (IOException e) {
                            Log.e("錯誤", e.toString());
                        }

                    }
                    datanum.put("E", rs.getString("product_picture"));
                    datanum.put("A", rs.getString("product_name"));
                    datanum.put("B", "$" + rs.getString("product_price"));
                    datanum.put("Z", rs.getDouble("product_price"));
                    datanum.put("C", rs.getString("product_quality"));
                    datanum.put("F", rs.getString("product_store"));
                    datanum.put("g", rs.getString("product_date"));
                    datanum.put("H", rs.getString("product_id"));
                    data.add(datanum);

                }
                Pois=data;
                String[] from = {"A", "B", "C", "D", "g","K"};
                int[] viewswhere = {R.id.Pname, R.id.Pprice, R.id.phone, R.id.img, R.id.textdate,R.id.distance};
                ADAhere = new SimpleAdapter(getBaseContext(), data,
                        R.layout.item, from, viewswhere);
                ADAhere.setViewBinder(new SimpleAdapter.ViewBinder() {

                    @Override
                    public boolean setViewValue(View view, Object data,
                                                String textRepresentation) {
                        if ((view instanceof ImageView) & (data instanceof Bitmap)) {
                            ImageView iv = (ImageView) view;
                            Bitmap bmp = (Bitmap) data;
                            iv.setImageBitmap(bmp);
                            return true;
                        }
                        return false;
                    }
                });

            } catch (SQLException e) {
                Toast.makeText(product.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }
    }
    public double distance(double longitude1, double latitudel, double longitude2, double latitude2 ){
        double radLatitadel =latitudel*Math.PI/180;
        double radLatitade2 =latitude2*Math.PI/180;
        double I=radLatitadel-radLatitade2;
        double p=longitude1*Math.PI/180-longitude2*Math.PI/180;
        double distance=2*Math.asin(Math.sqrt(Math.pow(Math.sin(I/2),2)+Math.cos(radLatitadel)*Math.cos(radLatitade2)*Math.pow(Math.sin(p/2),2)));
        distance=distance*6378137.0;
        distance=Math.round(distance*10000)/10000;
        return  distance;
    }
    private String DistanceText(double distance){
        if(distance<1000)return String.valueOf((int)distance+"m");
        else return new java.text.DecimalFormat("#.00").format(distance/1000)+"km";
    }


    private void DistanceSort(List<Map<String, Object>> poi)
    {
        Collections.sort(poi, new Comparator<Map<String, Object>>()
        {
            @Override
            public int compare(Map<String, Object> poi1, Map<String, Object> poi2)
            {
                return ((double)poi1.get("Q"))<(double)poi2.get("Q")? -1 : 1 ;
            }
        });
    }

    private void PriceSort(List<Map<String, Object>> poi)
    {
        Collections.sort(poi, new Comparator<Map<String, Object>>()
        {
            @Override
            public int compare(Map<String, Object> poi1, Map<String, Object> poi2)
            {
                return ((double)poi1.get("Z"))<(double)poi2.get("Z")? -1 : 1 ;
            }
        });
    }


    public class LOAD2 extends AsyncTask<String, Integer, String> {
        private ProgressDialog progressBar;

        @Override
        protected void onPreExecute() {
            //執行前 設定可以在這邊設定
            super.onPreExecute();
            progressBar = new ProgressDialog(product.this);
            progressBar.setMessage("Loading...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setCancelable(false);
            progressBar.show();
        }
        @Override
        protected void onPostExecute(String r) {
            lstcountry.setAdapter(ADAhere);
            if(ADAhere.isEmpty())
            {
                setContentView(R.layout.lovempty);
                TextView txt;
                txt=(TextView) findViewById(R.id.common_error_tv);
                if(near2!=null){
                  txt.setText(R.string.near_no_love);
                }
                mToolbar = (Toolbar) findViewById(R.id.toolbar6);
                mToolbar.setTitle("");
                mToolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
                setSupportActionBar(mToolbar);
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                        overridePendingTransition(R.animator.anim, R.animator.slide_out_left);
                    }
                });
            }

            progressBar.dismiss();
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
                while (rs.next()) {
                    Map<String, Object> datanum = new HashMap<String, Object>();

                    Statement st3 = con.createStatement();
                    ResultSet rs3 = st3.executeQuery("select store_address from store where store_id='" + rs.getString("product_store") + "'");
                    while(rs3.next())
                    {
                        String addressString=rs3.getString("store_address");
                        Geocoder geoCoder = new Geocoder(getBaseContext());
                        try {
                            List<Address> addressLocation = geoCoder.getFromLocationName(addressString, 1);
                            double latitude2 = addressLocation.get(0).getLatitude();
                            double longitude2 = addressLocation.get(0).getLongitude();

                            String stringdouble=(DistanceText(distance(latitude,longitude,latitude2,longitude2)));
                            double stringdoubl2=distance(latitude,longitude,latitude2,longitude2);
                            distance=stringdoubl2;
                            stringdouble2=stringdouble;
                            datanum.put("K", stringdouble);
                            datanum.put("Q", stringdoubl2);
                        } catch (IOException e) {
                            Log.e("錯誤", e.toString());
                        }

                    }
                    //設定距離1000M
                    if(distance<1000&&stringdouble2!=null) {
                        byte[] image = rs.getBytes("product_picture");
                        Bitmap picturebmp = BitmapFactory.decodeByteArray(image, 0, image.length);
                        datanum.put("D", picturebmp);
                        datanum.put("E", rs.getString("product_picture"));
                        datanum.put("A", rs.getString("product_name"));
                        datanum.put("B", "$" + rs.getString("product_price"));
                        datanum.put("Z", rs.getDouble("product_price"));
                        datanum.put("C", rs.getString("product_quality"));
                        datanum.put("F", rs.getString("product_store"));
                        datanum.put("g", rs.getString("product_date"));
                        data.add(datanum);
                    }
                }
                Pois=data;
                String[] from = {"A", "B", "C", "D", "g","K"};
                int[] viewswhere = {R.id.Pname, R.id.Pprice, R.id.phone, R.id.img, R.id.textdate,R.id.distance};
                ADAhere = new SimpleAdapter(getBaseContext(), data,
                        R.layout.item, from, viewswhere);
                ADAhere.setViewBinder(new SimpleAdapter.ViewBinder() {

                    @Override
                    public boolean setViewValue(View view, Object data,
                                                String textRepresentation) {
                        if ((view instanceof ImageView) & (data instanceof Bitmap)) {
                            ImageView iv = (ImageView) view;
                            Bitmap bmp = (Bitmap) data;
                            iv.setImageBitmap(bmp);
                            return true;
                        }
                        return false;
                    }
                });

            } catch (SQLException e) {
                Toast.makeText(product.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }
    }
}