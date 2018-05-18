package com.example.user.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class Login extends Activity {                 //登录界面活动


    public int pwdresetFlag=0;
    ConnectionClass connectionClass;
    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    Button re;                 //注册按钮
    Button mLoginButton;                      //登录按钮
    private CheckBox mRememberCheck,auto;
    private SharedPreferences login_sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        connectionClass = new ConnectionClass();
        mAccount = (EditText) findViewById(R.id.login_edit_account);
        mPwd = (EditText) findViewById(R.id.login_edit_pwd);
        ImageView image = (ImageView) findViewById(R.id.logo);
        mRememberCheck = (CheckBox) findViewById(R.id.Login_Remember);
        auto = (CheckBox) findViewById(R.id.auto);
        login_sp = getSharedPreferences("userInfo", 0);
        String name=login_sp.getString("id", "");
        String pwd =login_sp.getString("password", "");
        boolean choseRemember =login_sp.getBoolean("mRememberCheck", false);
        boolean choseAutoLogin =login_sp.getBoolean("auto", false);
        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if(choseRemember){
            mAccount.setText(name);
            mPwd.setText(pwd);
            mRememberCheck.setChecked(true);
        }


       if(choseAutoLogin){
           auto.setChecked(true);
          Intent i = new Intent(Login.this, User2.class);
          Bundle bundle = new Bundle();
           bundle.putString("name", mAccount.getText().toString());
          i.putExtras(bundle);
          startActivity(i);
     }

        image.setImageResource(R.drawable.a10);
        re = (Button) findViewById(R.id.login_btn_register);
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(Login.this,Register.class) ;
                startActivity(intent3);
                finish();

            }
        });

        mAccount = (EditText) findViewById(R.id.login_edit_account);
        mPwd = (EditText) findViewById(R.id.login_edit_pwd);
        mLoginButton = (Button) findViewById(R.id.login_btn_login);
        mLoginButton.setOnClickListener(mListener);



    }
    View.OnClickListener mListener = new View.OnClickListener() {                  //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_btn_register:                            //登录界面的注册按钮
                    Intent intent_Login_to_Register = new Intent(Login.this,Register.class) ;    //切换Login Activity至User Activity
                    startActivity(intent_Login_to_Register);
                    finish();
                    break;
                case R.id.login_btn_login:                              //登录界面的登录按钮
                    DoLogin doLogin = new DoLogin();
                    doLogin.execute("");
                    break;
            }
        }
    };

    public class DoLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        final String userid = mAccount.getText().toString();
        final String password = mPwd.getText().toString();
        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(Login.this,r,Toast.LENGTH_SHORT).show();
            if(isSuccess) {

                SharedPreferences.Editor editor =login_sp.edit();

                    //保存用户名和密码
                    editor.putString("id", userid);
                    editor.putString("password", password);

                    //是否记住密码
                    if(mRememberCheck.isChecked()){
                        editor.putBoolean("mRememberCheck", true);
                    }else{
                        editor.putBoolean("mRememberCheck", false);
                    }

            if(auto.isChecked()){
                   editor.putBoolean("auto", true);
               }else{
               editor.putBoolean("auto", false);
             }
                    editor.commit();


                Intent i = new Intent(Login.this, User2.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", userid);
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            if(userid.trim().equals("")|| password.trim().equals(""))
                z = "請輸入帳號或密碼";
            else
            {
                try {

                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "請檢察網路連線";
                    } else {
                        String query = "select * from userdb where id='" + userid + "' and password='" + password + "'";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);


                        if(rs.next())
                        {
                            z = "登入成功";
                            isSuccess=true;
                        }
                        else
                        {
                            z = "登入失敗";
                            isSuccess = false;
                        }

                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "登入失敗";
                }
            }
            return z;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

}

