package dhcc.cn.com.fix_phone.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import dhcc.cn.com.fix_phone.R;

/**
 * 2017/10/8 19
 */
public class AlterDialogFragment extends DialogFragment implements View.OnClickListener {

    private TextView mFriend;
    private TextView mCircle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.fragment_alter_dialog, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFriend = view.findViewById(R.id.textView_friend);
        mFriend.setOnClickListener(this);
        mCircle = view.findViewById(R.id.textView_circle);
        mCircle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.textView_friend:
                break;
            case R.id.textView_circle:
                break;
        }
    }
}
