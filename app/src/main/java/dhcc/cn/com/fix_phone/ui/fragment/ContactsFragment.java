package dhcc.cn.com.fix_phone.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dhcc.cn.com.fix_phone.Account;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.adapter.FriendListAdapter;
import dhcc.cn.com.fix_phone.bean.GetFriendResponse;
import dhcc.cn.com.fix_phone.conf.Constants;
import dhcc.cn.com.fix_phone.db.Friend;
import dhcc.cn.com.fix_phone.event.DeleteFriendEvent;
import dhcc.cn.com.fix_phone.event.GetFriendEvent;
import dhcc.cn.com.fix_phone.remote.ApiManager;
import dhcc.cn.com.fix_phone.rong.BroadcastManager;
import dhcc.cn.com.fix_phone.rong.CharacterParser;
import dhcc.cn.com.fix_phone.rong.SealUserInfoManager;
import dhcc.cn.com.fix_phone.ui.activity.BlackListActivity;
import dhcc.cn.com.fix_phone.ui.activity.BusinessActivity;
import dhcc.cn.com.fix_phone.ui.widget.LoadDialog;
import dhcc.cn.com.fix_phone.ui.widget.SelectableRoundedImageView;
import dhcc.cn.com.fix_phone.utils.PinyinComparator;
import io.rong.imkit.RongIM;
import io.rong.imkit.mention.SideBar;
import io.rong.imlib.model.CSCustomServiceInfo;

import static dhcc.cn.com.fix_phone.rong.SealAppContext.UPDATE_FRIEND;

/**
 * tab 2 通讯录的 Fragment
 * Created by Bob on 2015/1/25.
 */
public class ContactsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ContactsFragment";

    private SelectableRoundedImageView mSelectableRoundedImageView;
    private TextView                   mNameTextView;
    private TextView                   mNoFriends;
    private TextView                   mUnreadTextView;
    private View                       mHeadView;
    private EditText                   mSearchEditText;
    private ListView                   mListView;
    private PinyinComparator           mPinyinComparator;
    private SideBar                    mSidBar;
    /**
     * 中部展示的字母提示
     */
    private TextView                   mDialogTextView;
    private List<Friend>               mFriendList;
    private List<Friend>               mFilteredFriendList;
    /**
     * 好友列表的 mFriendListAdapter
     */
    private FriendListAdapter          mFriendListAdapter;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser            mCharacterParser;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private String                     mId;
    private String                     mCacheName;

    private static final int CLICK_CONTACT_FRAGMENT_FRIEND = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ADD_FRIEND_SUCCESS);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mBroadcastReceiver, filter);
        SealUserInfoManager.getInstance().openDB();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_address, container, false);
        EventBus.getDefault().register(this);
        initView(view);
        initData();
        updateUI();
        refreshUIListener();
        return view;
    }

    private void initView(View view) {
        mSearchEditText = (EditText) view.findViewById(R.id.search);
        mListView = (ListView) view.findViewById(R.id.listview);
        mNoFriends = (TextView) view.findViewById(R.id.show_no_friend);
        mSidBar = (SideBar) view.findViewById(R.id.sidrbar);
        mDialogTextView = (TextView) view.findViewById(R.id.group_dialog);
        mSidBar.setTextView(mDialogTextView);
        LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
        mHeadView = mLayoutInflater.inflate(R.layout.item_contact_list_header, null);
        mUnreadTextView = (TextView) mHeadView.findViewById(R.id.tv_unread);
        RelativeLayout newFriendsLayout = (RelativeLayout) mHeadView.findViewById(R.id.re_newfriends);
        RelativeLayout selfLayout = (RelativeLayout) mHeadView.findViewById(R.id.contact_me_item);
        mSelectableRoundedImageView = (SelectableRoundedImageView) mHeadView.findViewById(R.id.contact_me_img);
        mNameTextView = (TextView) mHeadView.findViewById(R.id.contact_me_name);
        updatePersonalUI();
        mListView.addHeaderView(mHeadView);
        mNoFriends.setVisibility(View.VISIBLE);

        selfLayout.setOnClickListener(this);
        newFriendsLayout.setOnClickListener(this);
        //设置右侧触摸监听
        mSidBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mFriendListAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }

            }
        });
    }

    private void initData() {
        mFriendList = new ArrayList<>();
        FriendListAdapter adapter = new FriendListAdapter(getActivity(), mFriendList);
        mListView.setAdapter(adapter);
        mFilteredFriendList = new ArrayList<>();
        //实例化汉字转拼音类
        mCharacterParser = CharacterParser.getInstance();
        mPinyinComparator = PinyinComparator.getInstance();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mDialogTextView != null) {
            mDialogTextView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr 需要过滤的 String
     */
    private void filterData(String filterStr) {
        List<Friend> filterDateList = new ArrayList<>();
        try {
            if (TextUtils.isEmpty(filterStr)) {
                filterDateList = mFriendList;
            } else {
                filterDateList.clear();
                for (Friend friendModel : mFriendList) {
                    String name = friendModel.getName();
                    String displayName = friendModel.getDisplayName();
                    if (!TextUtils.isEmpty(displayName)) {
                        if (name.contains(filterStr) || mCharacterParser.getSpelling(name).startsWith(filterStr) || displayName.contains(filterStr) || mCharacterParser.getSpelling(displayName).startsWith(filterStr)) {
                            filterDateList.add(friendModel);
                        }
                    } else {
                        if (name.contains(filterStr) || mCharacterParser.getSpelling(name).startsWith(filterStr)) {
                            filterDateList.add(friendModel);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, mPinyinComparator);
        mFilteredFriendList = filterDateList;
        mFriendListAdapter.updateListView(filterDateList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_newfriends:
                mUnreadTextView.setVisibility(View.GONE);
                startActivity(new Intent(getActivity(), BlackListActivity.class));
                break;
            case R.id.contact_me_item:
                try {
                    //首先需要构造使用客服者的用户信息
                    CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
                    CSCustomServiceInfo csInfo = csBuilder.nickName("融云").build();
                    RongIM.getInstance().startCustomerServiceChat(getActivity(), mId, mCacheName, csInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void refreshUIListener() {
        BroadcastManager.getInstance(getActivity()).addAction(UPDATE_FRIEND, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateUI();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mBroadcastReceiver);
        try {
            BroadcastManager.getInstance(getActivity()).destroy(UPDATE_FRIEND);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void updateUI() {
        List<Friend> friends = SealUserInfoManager.getInstance().getFriends();
        updateFriendsList(friends);
    }

    private void updateFriendsList(List<Friend> friendsList) {
        if (friendsList == null) {
            return;
        }

        for (Friend friend : friendsList) {
            Log.d(TAG, "updateFriendsList: " + friend.toString());
        }

        //updateUI fragment初始化和好友信息更新时都会调用,isReloadList表示是否是好友更新时调用
        boolean isReloadList = false;
        if (mFriendList != null && mFriendList.size() > 0) {
            mFriendList.clear();
            isReloadList = true;
        }
        mFriendList = friendsList;
        if (mFriendList.size() > 0) {
            handleFriendDataForSort();
            mNoFriends.setVisibility(View.GONE);
        } else {
            mNoFriends.setVisibility(View.VISIBLE);
        }

        // 根据a-z进行排序源数据
        Collections.sort(mFriendList, mPinyinComparator);

        for (Friend friend : mFriendList) {
            Log.d(TAG, "updateFriendsList: " + friend.toString());
        }

        if (isReloadList) {
            mSidBar.setVisibility(View.VISIBLE);
            mFriendListAdapter.updateListView(mFriendList);
        } else {
            mSidBar.setVisibility(View.VISIBLE);
            mFriendListAdapter = new FriendListAdapter(getActivity(), mFriendList);

            mListView.setAdapter(mFriendListAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mListView.getHeaderViewsCount() > 0) {
                        String portraitUri = SealUserInfoManager.getInstance().getPortraitUri(mFriendList.get(position - 1));
                        getActivity().startActivity(new Intent(getActivity(), BusinessActivity.class).
                                putExtra("headurl", portraitUri).
                                putExtra("name", mFriendList.get(position - 1).getDisplayName()).
                                putExtra("isIMCall", true).
                                putExtra("userID", mFriendList.get(position - 1).getUserId()));
                    } else {
                        String portraitUri = SealUserInfoManager.getInstance().getPortraitUri(mFilteredFriendList.get(position));
                        getActivity().startActivity(new Intent(getActivity(), BusinessActivity.class).
                                putExtra("headurl", portraitUri).
                                putExtra("name", mFilteredFriendList.get(position).getDisplayName()).
                                putExtra("isIMCall", true).
                                putExtra("userID", mFilteredFriendList.get(position).getUserId()));
                    }
                }
            });


            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    final Friend bean = mFriendList.get(position - 1);
                    new AlertDialog.Builder(getContext()).setTitle("删除好友").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ApiManager.Instance().DeleteFriend(Account.getAccessToken(), bean.getUserId());
                            LoadDialog.show(getContext());
                        }
                    }).show();
                    return true;
                }
            });
            mSearchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                    filterData(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        if (mListView.getHeaderViewsCount() > 0) {
                            mListView.removeHeaderView(mHeadView);
                        }
                    } else {
                        if (mListView.getHeaderViewsCount() == 0) {
                            mListView.addHeaderView(mHeadView);
                        }
                    }
                }
            });
        }
    }

    private void updatePersonalUI() {
        mId = Constants.MI_SERVICE;
        mCacheName = "速通交易客服";
        mNameTextView.setText(mCacheName);
        if (!TextUtils.isEmpty(mId)) {
            mSelectableRoundedImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_launcher));
        }
    }

    private void handleFriendDataForSort() {
        for (Friend friend : mFriendList) {
            if (friend.isExitsDisplayName()) {
                String letters = replaceFirstCharacterWithUppercase(friend.getDisplayNameSpelling());
                friend.setLetters(letters);
            } else {
                String letters = replaceFirstCharacterWithUppercase(friend.getNameSpelling());
                friend.setLetters(letters);
            }
        }
    }

    private String replaceFirstCharacterWithUppercase(String spelling) {
        if (!TextUtils.isEmpty(spelling)) {
            char first = spelling.charAt(0);
            char newFirst = first;
            if (first >= 'a' && first <= 'z') {
                newFirst -= 32;
            }
            return spelling.replaceFirst(String.valueOf(first), String.valueOf(newFirst));
        } else {
            return "#";
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteFriend(DeleteFriendEvent event) {
        LoadDialog.dismiss(getContext());
        if (event.telCheckResponse != null && event.telCheckResponse.FIsSuccess) {
            ApiManager.Instance().GetListFriend(Account.getAccessToken(), "");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getFriend(GetFriendEvent event) {
        if (event.getFriendResponse != null && event.getFriendResponse.FObject != null) {
            List<GetFriendResponse.FriendList.Friend> friendList = event.getFriendResponse.FObject.list;
            List<Friend> list;
            if (friendList != null) {
                list = new ArrayList<>();
                SealUserInfoManager.getInstance().openDB();
                SealUserInfoManager.getInstance().deleteFriends();
                for (GetFriendResponse.FriendList.Friend f : friendList) {
                    Friend friend = new Friend(String.valueOf(f.FFriendID), f.FCompanyName, Uri.parse(f.FHeadUrl), null, null, null, null, null, CharacterParser.getInstance().getSpelling(f.FCompanyName), null);
                    SealUserInfoManager.getInstance().addFriend(friend);
                    list.add(friend);
                }
                updateFriendsList(list);
            }
        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constants.ADD_FRIEND_SUCCESS.equals(action)) {
                ApiManager.Instance().GetListFriend(Account.getAccessToken(), "");
            }
        }
    };
}
