package com.kuang2010.googleplay20.base;

import android.os.Bundle;
import android.widget.Toast;

import com.kuang2010.googleplay20.MainActivity;
import com.kuang2010.googleplay20.manager.ActivityStack;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * author: kuangzeyu2019
 * date: 2020/6/21
 * time: 9:24
 * desc:
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStack.getInastance().addActivity(this);
    }


    long mPreClickTime;
    @Override
    public void onBackPressed() {
        if (this instanceof MainActivity) {// 主页
            if (System.currentTimeMillis() - mPreClickTime > 2000) {// 两次点击的间隔大于2s中
                Toast.makeText(getApplicationContext(), "再按一次,退出谷歌市场", 0).show();
                mPreClickTime = System.currentTimeMillis();
                return;
            } else {
                // 完全退出
                ActivityStack.getInastance().finishAllActivity();
            }
        } else {
            super.onBackPressed();// finish
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStack.getInastance().removeActivity(this);
    }
}
