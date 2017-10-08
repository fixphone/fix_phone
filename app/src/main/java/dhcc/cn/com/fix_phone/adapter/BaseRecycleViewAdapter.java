package dhcc.cn.com.fix_phone.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dhcc.cn.com.fix_phone.listener.RecycleViewItemListener;

/**
 * Created by yiwei on 16/4/9.
 */
public abstract class BaseRecycleViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected RecycleViewItemListener itemListener;
    protected List<T> datas = new ArrayList<T>();

    public List<T> getData() {
        return datas;
    }

    public void setData(List<T> data) {
        this.datas = data;
    }

    public void setItemListener(RecycleViewItemListener listener) {
        this.itemListener = listener;
    }

    public void addData(List<T> data) {
        datas.addAll(data);
    }

}
