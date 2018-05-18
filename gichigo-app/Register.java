package com.example.user.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Register extends AppCompatActivity {
    private EditText mAccount;
    private EditText mPwd;
    private EditText mPhone;
    private Button mSureButton,cancel;
    private EditText mPwdCheck;
    ConnectionClass connectionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        connectionClass = new ConnectionClass();
        mAccount = (EditText) findViewById(R.id.resetpwd_edit_name);
        mPwd = (EditText) findViewById(R.id.resetpwd_edit_pwd_old);
        mPhone = (EditText) findViewById(R.id.resetpwd_edit_phone);
        mPwdCheck = (EditText) findViewById(R.id.resetpwd_edit_pwd_new);
        cancel= (Button) findViewById(R.id.register_btn_cancel);
        mSureButton = (Button) findViewById(R.id.register_btn_sure);

        mSureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Re re = new Re();
                re.execute("");

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
                finish();

            }
        });

    }



    public class Re extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;


        String id = mAccount.getText().toString();
        String phone = mPhone.getText().toString();
        String password =  mPwd.getText().toString();
        String userPwdCheck = mPwdCheck.getText().toString().trim();


        @Override
        protected void onPostExecute(String r) {

            if(password.equals(userPwdCheck)==false){     //两次密码输入不一样
                Toast.makeText(Register.this, getString(R.string.pwd_not_the_same),Toast.LENGTH_SHORT).show();
                return ;
            }
            if(id.trim().equals("")|| password.trim().equals(""))
            {
                Toast.makeText(Register.this, "帳號或密碼不可為空白",Toast.LENGTH_SHORT).show();
                return ;
            }
            if(isSuccess) {
                Toast.makeText(Register.this, getString(R.string.register_success),Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
                finish();
            }else {
                Toast.makeText(Register.this, getString(R.string.name_already_exist, id),Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            if(id.trim().equals("")|| password.trim().equals(""))
                z = "Please enter User Id and Password";
            else
            {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    }else{
                        String query = "select * from userdb where id='" + id +  "'";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            isSuccess=false;
                        }
                        else
                        {
                            String query2 = "insert into userdb (id,password,phone) values ('"+id+"', '"+password+"', '"+phone+"')";
                            PreparedStatement preparedStatement = con.prepareStatement(query2);
                            preparedStatement.executeUpdate();
                            z = "Added Successfully";
                            isSuccess = true;
                        }

                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions";
                }
            }
            return z;
        }
    }

}
