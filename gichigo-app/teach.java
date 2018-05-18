package com.example.user.login;


import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class teach extends AppCompatActivity {
    ViewPager mViewPager;
    List<View> viewList;
    RadioGroup mRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teach);


        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mRadio=(RadioGroup) findViewById(R.id.radiogroup);




        final LayoutInflater mInflater = getLayoutInflater().from(this);

        View v1 = mInflater.inflate(R.layout.intro_layout_1, null);
        View v2 = mInflater.inflate(R.layout.intro_layout_2, null);
        View v3 = mInflater.inflate(R.layout.intro_layout_3, null);
        View v4 = mInflater.inflate(R.layout.intro_layout_4, null);

        viewList = new ArrayList<View>();
        viewList.add(v1);
        viewList.add(v2);
        viewList.add(v3);
        viewList.add(v4);

        mViewPager.setAdapter(new MyViewPagerAdapter(viewList));
        mViewPager.setCurrentItem(0);



        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        mRadio.check(R.id.radioButton);
                        break;
                    case 1:
                        mRadio.check(R.id.radioButton2);
                        break;
                    case 2:
                        mRadio.check(R.id.radioButton3);
                        break;
                    case 3:
                        mRadio.check(R.id.radioButton4);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void trynow(View view) {
        finish();
        overridePendingTransition(R.animator.ap2, R.animator.ap1);// 淡出淡入动画效果
    }



}
