package ws.dyt.refresh;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import ws.dyt.refresh.base.SingleFragmentActivity;
import ws.dyt.refresh.coordilayout.CoordiLayoutFragment;
import ws.dyt.refresh.main.MainTabFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageView iv = (ImageView) findViewById(R.id.rll_empty_iv_id);
        iv.setImageResource(R.drawable.rll_indi_init_loading);
        Drawable drawable = iv.getDrawable();
        if (null == drawable) {
            return;
        }
        if (drawable instanceof AnimationDrawable) {
            AnimationDrawable ad = (AnimationDrawable) drawable;
            ad.setOneShot(false);
            if(ad.isRunning()) {
                ad.stop();
            }
            ad.start();
            return;
        }

    }


    public void listTestOnClick(View view) {
        SingleFragmentActivity.to(this, TestFragment.class, null);
    }

    public void libsTestOnClick(View view) {
        SingleFragmentActivity.to(this, MainTabFragment.class, null);
    }

    public void coordilayoutTestOnClick(View view) {
        SingleFragmentActivity.to(this, CoordiLayoutFragment.class, null);
    }
}
