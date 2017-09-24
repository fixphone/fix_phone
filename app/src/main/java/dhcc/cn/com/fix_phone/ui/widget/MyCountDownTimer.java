package dhcc.cn.com.fix_phone.ui.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;


public class MyCountDownTimer extends CountDownTimer {

    private TextView mButton;
    private Context mContext;

    public MyCountDownTimer(long millisInFuture, long countDownInterval, TextView button, Context context) {
        super(millisInFuture, countDownInterval);
        mButton = button;
        mContext = context;
    }

    @Override
    public void onFinish() {
        mButton.setClickable(true);
        mButton.setText("获取验证码");
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mButton.setText(millisUntilFinished / 1000 + "");
    }
}