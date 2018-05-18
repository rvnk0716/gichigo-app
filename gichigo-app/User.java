package com.example.user.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class User extends AppCompatActivity {

    private Button mReturnButton;

    private Toolbar mToolbar;
    ConnectionClass connectionClass;
    private Button SaveButton;
    private EditText mPwd,mPwd2,mPhone;
    String name2;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mReturnButton = (Button)findViewById(R.id.returnback);
        connectionClass = new ConnectionClass();


        mPwd = (EditText) findViewById(R.id.editText9);
        mPwd2 = (EditText) findViewById(R.id.editText10);
        mPhone= (EditText) findViewById(R.id.editText11);

        final EditText abc=(EditText)findViewById(R.id.editText2);
        final EditText mPwd=(EditText)findViewById(R.id.editText9);
        final EditText mPhone=(EditText)findViewById(R.id.editText11);



        Bundle bundle = this.getIntent().getExtras();
        final String name = bundle.getString("name");
        abc.setText(name);
        name2=name;


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


        try {
            Connection con = connectionClass.CONN();

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery( "select * from userdb where id='" + name + "'");
            ResultSetMetaData rsmd = rs.getMetaData();
                if(rs.next())
                {
                    mPwd.setText(rs.getString("password"));
                    mPhone.setText(rs.getString("phone"));
                }
        }
        catch (Exception ex)
        {
            System.err.println("Got an exception! ");
            System.err.println(ex.getMessage());
        }
        final  String pass = mPwd.getText().toString();
        final  String phone = mPhone.getText().toString();
        SaveButton = (Button) findViewById(R.id.button13);
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePro updatePro = new UpdatePro();
                updatePro.execute("");

            }
        });

    }

    public void back_to_login(View view) {
        finish();
    }

    public class UpdatePro extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;
        String pass = mPwd.getText().toString();
        String phone = mPhone.getText().toString();

        String userPwdCheck = mPwd2.getText().toString().trim();

        @Override
        protected void onPostExecute(String r) {
            if ("".equals(mPwd2.getText().toString().trim()) == false) {
                if (pass.equals(userPwdCheck) == false) {     //两次密码输入不一样
                    Toast.makeText(User.this, getString(R.string.pwd_not_the_same), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (isSuccess) {
                Toast.makeText(User.this, getString(R.string.success), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(User.this, getString(R.string.fall), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else
                    {
                        String query = "Update userdb set password='"+pass+"', phone='"+phone+"' where id='"+name2+ "'";
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Updated Successfully";
                        isSuccess = true;
                    }
                }
             catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions";
            }
            return z;
        }
    }
    public void onBackPressed() {
        Intent intent4 = new Intent(User.this,User2.class) ;
        Bundle bundle = new Bundle();
        bundle.putString("name", name2);
        intent4.putExtras(bundle);
        startActivity(intent4);
        finish();
        overridePendingTransition(R.animator.anim, R.animator.slide_out_left);

    }
}
