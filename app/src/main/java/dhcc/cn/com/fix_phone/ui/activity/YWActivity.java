package dhcc.cn.com.fix_phone.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.utils.SystemBarUtil;
import me.yokeyword.fragmentation.SwipeBackLayout;
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by yiw on 2017/1/9.
 */

public class YWActivity extends SwipeBackActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isApplyKitKatTranslucency()) {
            setTranslucentStatus(true);
        }
        if (isApplyColorPrimary()) {
            setSystemBarTintDrawable(getResources().getDrawable(R.color.basic_color_primary));
        }

        getSwipeBackLayout().setEdgeOrientation(SwipeBackLayout.EDGE_LEFT);
    }

    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    protected boolean isApplyColorPrimary() {
        return true;
    }

    private void setSystemBarTintDrawable(Drawable tintDrawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarUtil mTintManager = new SystemBarUtil(this);
            if (tintDrawable != null) {
                mTintManager.setStatusBarTintEnabled(true);
                mTintManager.setTintDrawable(tintDrawable);
            } else {
                mTintManager.setStatusBarTintEnabled(false);
                mTintManager.setTintDrawable(null);
            }
        }
    }

    protected void setTranslucentStatus(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }





}
