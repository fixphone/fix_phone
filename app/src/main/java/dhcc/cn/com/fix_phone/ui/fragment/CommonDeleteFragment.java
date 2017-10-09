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
public class CommonDeleteFragment extends DialogFragment implements View.OnClickListener {

    private TextView mCancel;
    private TextView mConfirm;
    private TextView mTitleView;
    private TextView mDescView;

    private String title;
    private String desc;

    public static CommonDeleteFragment newInstance(String title, String desc) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("desc", desc);
        CommonDeleteFragment fragment = new CommonDeleteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        title = arguments.getString("title");
        desc = arguments.getString("desc");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.fragment_delete_ad_dialog, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCancel = view.findViewById(R.id.textView_cancel);
        mCancel.setOnClickListener(this);
        mConfirm = view.findViewById(R.id.textView_confirm);
        mTitleView = view.findViewById(R.id.textView_title);
        mDescView = view.findViewById(R.id.textView_desc);
        mConfirm.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTitleView.setText(title);
        mDescView.setText(desc);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.textView_cancel:
                dismiss();
                break;
            case R.id.textView_confirm:
                OnConfirmClickListener clickListener = (OnConfirmClickListener) getActivity();
                clickListener.onConfirm();
                dismiss();
                break;
        }
    }

    public interface OnConfirmClickListener {
        void onConfirm();
    }

}
