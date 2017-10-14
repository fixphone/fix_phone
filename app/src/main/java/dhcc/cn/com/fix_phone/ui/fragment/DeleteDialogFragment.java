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
public class DeleteDialogFragment extends DialogFragment implements View.OnClickListener {

    private TextView mCancel;
    private TextView mConfirm;

    public static DeleteDialogFragment newInstance() {
        return new DeleteDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.fragment_delete_dialog, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCancel = view.findViewById(R.id.textView_cancel);
        mCancel.setOnClickListener(this);
        mConfirm = view.findViewById(R.id.textView_confirm);
        mConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.textView_cancel:
                dismiss();
                break;
            case R.id.textView_confirm:
                SelectListener listener = (SelectListener) getActivity();
                listener.onSelector();
                dismiss();
                break;
        }
    }

    public interface SelectListener {
        void onSelector();
    }
}
