package com.cy.slidingmenuvertical;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.cy.slidemenuvertical.SlidingMenuVertical;

public class SlideBottomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_bottom);

//        final TextView tv_middle = (TextView) findViewById(R.id.tv_middle);
        final SlidingMenuVertical slidingMenuVertical = ((SlidingMenuVertical) findViewById(R.id.slidingMenu));
        slidingMenuVertical.setDuration_max(300);
        slidingMenuVertical.setAmbit_scroll(0);
        slidingMenuVertical.setTopSlide(false);
        slidingMenuVertical.setOnSwitchListener(new SlidingMenuVertical.OnSwitchListener() {
            /*
                   滑动中
        y_now:实时view_bottom的top y, y_opened:抽屉打开时view_bootom的top y,y_closed:抽屉关闭时view_bottom的top y  top y:在屏幕中的top y坐标

                    */
            @Override
            public void onSwitching(boolean isToOpen, int y_now, int y_opened, int y_closed) {

//                tv_middle.setBackgroundColor(Color.argb((int) (1.0f * (y_opened - y_now) / (y_opened - y_closed) * 255),
//                        Color.red(0xff3F51B5), Color.green(0xff3F51B5), Color.blue(0xff3F51B5)));
//
//                tv_middle.setTextColor(Color.argb((int) (1.0f * (y_opened - y_now) / (y_opened - y_closed) * 255),
//                        Color.red(0xffffffff), Color.green(0xffffffff), Color.blue(0xffffffff)));
            }

            @Override
            public void onSwitched(boolean opened) {

                if (opened) {
//                    tv_middle.setBackgroundColor(0xffffffff);
//                    tv_middle.setTextColor(0xff454545);
                }
            }
        });

        findViewById(R.id.tv_switch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingMenuVertical.open(!slidingMenuVertical.isOpened());
            }
        });
    }
}
