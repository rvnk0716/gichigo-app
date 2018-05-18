package com.example.user.login;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


public class fcmset extends AppCompatActivity {

    private Toolbar mToolbar;
    CheckBox fcm, newtaipei, taipei, ty, tc, gc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcmset);
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
        fcm = (CheckBox) findViewById(R.id.fcm);
        newtaipei = (CheckBox) findViewById(R.id.newtaipei);
        taipei = (CheckBox) findViewById(R.id.taipei);
        ty = (CheckBox) findViewById(R.id.ty);
        tc = (CheckBox) findViewById(R.id.tc);
        gc = (CheckBox) findViewById(R.id.gc);
        newtaipei();
        taipei();
        Taoyuan();
        Taichung();
        Kaohsiung();
        fcm();
        if (fcm.isChecked()) {
            FirebaseInstanceId.getInstance().getToken();
            newtaipei.setClickable(true);
            taipei.setClickable(true);
            ty.setClickable(true);
            tc.setClickable(true);
            gc.setClickable(true);
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("newtaipei");
            FirebaseMessaging.getInstance().unsubscribeFromTopic("Taipei");
            FirebaseMessaging.getInstance().unsubscribeFromTopic("Taoyuan");
            FirebaseMessaging.getInstance().unsubscribeFromTopic("Taichung");
            FirebaseMessaging.getInstance().unsubscribeFromTopic("Kaohsiung");
            newtaipei.setChecked(false);
            newtaipei.setClickable(false);
            taipei.setChecked(false);
            taipei.setClickable(false);
            ty.setChecked(false);
            ty.setClickable(false);
            tc.setChecked(false);
            tc.setClickable(false);
            gc.setChecked(false);
            gc.setClickable(false);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.anim, R.animator.slide_out_left);

    }

    void newtaipei() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();
        if (preferences.contains("newtaipeichecked") && preferences.getBoolean("newtaipeichecked", false) == true) {
            newtaipei.setChecked(true);
        } else {
            newtaipei.setChecked(false);
        }
        newtaipei.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (newtaipei.isChecked()) {
                    FirebaseMessaging.getInstance().subscribeToTopic("newtaipei");
                    editor.putBoolean("newtaipeichecked", true);
                    editor.apply();
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("newtaipei");
                    editor.putBoolean("newtaipeichecked", false);
                    editor.apply();
                }
            }
        });
    }

    void taipei() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();
        if (preferences.contains("Taipeichecked") && preferences.getBoolean("Taipeichecked", false) == true) {
            taipei.setChecked(true);
        } else {
            taipei.setChecked(false);
        }
        taipei.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (taipei.isChecked()) {
                    FirebaseMessaging.getInstance().subscribeToTopic("Taipei");
                    editor.putBoolean("Taipeichecked", true);
                    editor.apply();
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Taipei");
                    editor.putBoolean("Taipeichecked", false);
                    editor.apply();
                }
            }
        });
    }

    void Taoyuan() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();
        if (preferences.contains("Taoyuanchecked") && preferences.getBoolean("Taoyuanchecked", false) == true) {
            ty.setChecked(true);
        } else {
            ty.setChecked(false);
        }
        ty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (ty.isChecked()) {
                    FirebaseMessaging.getInstance().subscribeToTopic("Taoyuan");
                    editor.putBoolean("Taoyuanchecked", true);
                    editor.apply();
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Taoyuan");
                    editor.putBoolean("Taoyuanchecked", false);
                    editor.apply();
                }
            }
        });
    }


    void Taichung() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();
        if (preferences.contains("Taichungchecked") && preferences.getBoolean("Taichungchecked", false) == true) {
            tc.setChecked(true);
        } else {
            tc.setChecked(false);
        }
        tc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (tc.isChecked()) {
                    FirebaseMessaging.getInstance().subscribeToTopic("Taichung");
                    editor.putBoolean("Taichungchecked", true);
                    editor.apply();
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Taichung");
                    editor.putBoolean("Taichungchecked", false);
                    editor.apply();
                }
            }
        });
    }

    void Kaohsiung() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();
        if (preferences.contains("Kaohsiungchecked") && preferences.getBoolean("Kaohsiungchecked", false) == true) {
            gc.setChecked(true);
        } else {
            gc.setChecked(false);
        }
        gc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (gc.isChecked()) {
                    FirebaseMessaging.getInstance().subscribeToTopic("Kaohsiung");
                    editor.putBoolean("Kaohsiungchecked", true);
                    editor.apply();
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Kaohsiung");
                    editor.putBoolean("Kaohsiungchecked", false);
                    editor.apply();
                }
            }
        });
    }


    void fcm() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();
        if (preferences.contains("fcmchecked") && preferences.getBoolean("fcmchecked", false) == true) {
            fcm.setChecked(true);
        } else {
            fcm.setChecked(false);
        }
        fcm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (fcm.isChecked()) {
                    open();

                    editor.putBoolean("fcmchecked", true);
                    editor.apply();
                    newtaipei.setClickable(true);
                    taipei.setClickable(true);
                    ty.setClickable(true);
                    tc.setClickable(true);
                    gc.setClickable(true);
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("newtaipei");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Taipei");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Taoyuan");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Taichung");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Kaohsiung");
                    newtaipei.setChecked(false);
                    newtaipei.setClickable(false);
                    taipei.setChecked(false);
                    taipei.setClickable(false);
                    ty.setChecked(false);
                    ty.setClickable(false);
                    tc.setChecked(false);
                    tc.setClickable(false);
                    gc.setChecked(false);
                    gc.setClickable(false);
                    editor.putBoolean("fcmchecked", false);
                    editor.apply();
                    cancel();
                }
            }
        });
    }

    void cancel() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("如要取消通知，可前往>設定>通知")
                .setCancelable(false)
                .setPositiveButton("設定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                        intent.putExtra("app_package", getPackageName());
                        intent.putExtra("app_uid", getApplicationInfo().uid);
                        intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
                        startActivity(intent);
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

    void open() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("如要開啟通知，可前往>設定>通知")
                .setCancelable(false)
                .setPositiveButton("設定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                        intent.putExtra("app_package", getPackageName());
                        intent.putExtra("app_uid", getApplicationInfo().uid);
                        intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
                        startActivity(intent);
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
}



