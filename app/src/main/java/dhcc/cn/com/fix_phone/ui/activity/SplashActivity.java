package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.utils.SpUtils;

/**
 * 2017/11/4 13
 *
 * @author admin
 */
public class SplashActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private static final String TAG = "SplashActivity";
    public static final  int    MAX = 5;
    private int count;
    private int[] images = new int[]{R.drawable.start_1, R.drawable.start_2, R.drawable.start_3, R.drawable.start_4};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        boolean onceTime = (boolean) SpUtils.get(this, "onceTime", false);
        if (onceTime) {
            startMainActivity();
        }
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return images.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(SplashActivity.this);
                imageView.setImageResource(images[position]);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                container.addView(imageView);
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == images.length - 1 && positionOffset == 0) {
                    count++;
                    if (count > MAX) {
                        startMainActivity();
                    }
                } else {
                    count = 0;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
