package dhcc.cn.com.fix_phone.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import dhcc.cn.com.fix_phone.Account;
import dhcc.cn.com.fix_phone.MyApplication;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.base.RongBaseActivity;
import dhcc.cn.com.fix_phone.bean.GetFriendResponse;
import dhcc.cn.com.fix_phone.db.Friend;
import dhcc.cn.com.fix_phone.event.AddFriendEvent;
import dhcc.cn.com.fix_phone.event.GetFriendEvent;
import dhcc.cn.com.fix_phone.event.QueryUserEvent;
import dhcc.cn.com.fix_phone.remote.ApiManager;
import dhcc.cn.com.fix_phone.rong.BroadcastManager;
import dhcc.cn.com.fix_phone.rong.CharacterParser;
import dhcc.cn.com.fix_phone.rong.SealAppContext;
import dhcc.cn.com.fix_phone.rong.SealConst;
import dhcc.cn.com.fix_phone.rong.SealUserInfoManager;
import dhcc.cn.com.fix_phone.rong.network.async.AsyncTaskManager;
import dhcc.cn.com.fix_phone.rong.network.http.HttpException;
import dhcc.cn.com.fix_phone.rong.response.FriendInvitationResponse;
import dhcc.cn.com.fix_phone.rong.response.GetUserInfoByPhoneResponse;
import dhcc.cn.com.fix_phone.ui.widget.DialogWithYesOrNoUtils;
import dhcc.cn.com.fix_phone.ui.widget.LoadDialog;
import dhcc.cn.com.fix_phone.ui.widget.SelectableRoundedImageView;
import dhcc.cn.com.fix_phone.utils.AMUtils;
import dhcc.cn.com.fix_phone.utils.CommonUtils;
import dhcc.cn.com.fix_phone.utils.NToast;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imlib.model.UserInfo;

import static dhcc.cn.com.fix_phone.rong.SealAppContext.UPDATE_FRIEND;
import static dhcc.cn.com.fix_phone.rong.SealAppContext.UPDATE_RED_DOT;

public class SearchFriendActivity extends RongBaseActivity {

    private static final String TAG = "SearchFriendActivity";
    private static final int CLICK_CONVERSATION_USER_PORTRAIT = 1;
    private static final int SEARCH_PHONE = 10;
    private static final int ADD_FRIEND = 11;
    private EditText mEtSearch;
    private LinearLayout searchItem;
    private TextView searchName;
    private SelectableRoundedImageView searchImage;
    private String mPhone;
    private String addFriendMessage;
    private String mFriendId;

    private Friend mFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        EventBus.getDefault().register(this);
        setTitle("增加好友");
        mHeadRightText.setText("查找");
        mHeadRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiManager.Instance().QueryUserFriend(Account.getAccessToken(), mEtSearch.getText().toString().trim());
                LoadDialog.show(SearchFriendActivity.this);
            }
        });

        mEtSearch = (EditText) findViewById(R.id.search_edit);
        searchItem = (LinearLayout) findViewById(R.id.search_result);
        searchName = (TextView) findViewById(R.id.search_name);
        searchImage = (SelectableRoundedImageView) findViewById(R.id.search_header);
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() == 11) {
//                    mPhone = s.toString().trim();
//                    if (!AMUtils.isMobile(mPhone)) {
//                        NToast.shortToast(mContext, "非法手机号");
//                        return;
//                    }
//                    hintKbTwo();
//                    LoadDialog.show(mContext);
//                    request(SEARCH_PHONE, true);
//                } else {
//                    searchItem.setVisibility(View.GONE);
//                }
                if(s.length() > 0){
                    mHeadRightText.setVisibility(View.VISIBLE);
                }else {
                    mHeadRightText.setVisibility(View.GONE);
                    searchItem.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFriendOrSelf(mFriendId)) {
                    Intent intent = new Intent(SearchFriendActivity.this, UserDetailActivity.class);
                    intent.putExtra("friend", mFriend);
                    intent.putExtra("type", CLICK_CONVERSATION_USER_PORTRAIT);
                    startActivity(intent);
                    SealAppContext.getInstance().pushActivity(SearchFriendActivity.this);
                    return;
                }
                DialogWithYesOrNoUtils.getInstance().showEditDialog(mContext, getString(R.string.add_text), getString(R.string.add_friend), new DialogWithYesOrNoUtils.DialogCallBack() {
                    @Override
                    public void executeEvent() {

                    }

                    @Override
                    public void updatePassword(String oldPassword, String newPassword) {

                    }

                    @Override
                    public void executeEditEvent(String editText) {
                        if (!CommonUtils.isNetworkConnected(mContext)) {
                            NToast.shortToast(mContext, "网络不可用");
                            return;
                        }
                        addFriendMessage = editText;
                        if (TextUtils.isEmpty(editText)) {
                            addFriendMessage = "我是" + getSharedPreferences("config", MODE_PRIVATE).getString(SealConst.SEALTALK_LOGIN_NAME, "");
                        }
                        if (!TextUtils.isEmpty(mFriendId)) {
                            LoadDialog.show(mContext);
                            ApiManager.Instance().AddFriend(Account.getAccessToken(), mFriendId);
                        } else {
                            NToast.shortToast(mContext, "id is null");
                        }
                    }
                });
            }
        });
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case SEARCH_PHONE:
                return action.getUserInfoFromPhone("86", mPhone);
            case ADD_FRIEND:
                return action.sendFriendInvitation(mFriendId, addFriendMessage);
        }
        return super.doInBackground(requestCode, id);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {
                case SEARCH_PHONE:
                    final GetUserInfoByPhoneResponse userInfoByPhoneResponse = (GetUserInfoByPhoneResponse) result;
                    if (userInfoByPhoneResponse.getCode() == 200) {
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, "success");
                        mFriendId = userInfoByPhoneResponse.getResult().getId();
                        searchItem.setVisibility(View.VISIBLE);
                        String portraitUri = null;
                        if (userInfoByPhoneResponse.getResult() != null) {
                            GetUserInfoByPhoneResponse.ResultEntity userInfoByPhoneResponseResult = userInfoByPhoneResponse.getResult();
                            UserInfo userInfo = new UserInfo(userInfoByPhoneResponseResult.getId(),
                                    userInfoByPhoneResponseResult.getNickname(),
                                    Uri.parse(userInfoByPhoneResponseResult.getPortraitUri()));
                            portraitUri = SealUserInfoManager.getInstance().getPortraitUri(userInfo);
                        }
                        ImageLoader.getInstance().displayImage(portraitUri, searchImage, MyApplication.getOptions());
                        searchName.setText(userInfoByPhoneResponse.getResult().getNickname());
                        searchItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isFriendOrSelf(mFriendId)) {
                                    Intent intent = new Intent(SearchFriendActivity.this, UserDetailActivity.class);
                                    intent.putExtra("friend", mFriend);
                                    intent.putExtra("type", CLICK_CONVERSATION_USER_PORTRAIT);
                                    startActivity(intent);
                                    SealAppContext.getInstance().pushActivity(SearchFriendActivity.this);
                                    return;
                                }
                                DialogWithYesOrNoUtils.getInstance().showEditDialog(mContext, getString(R.string.add_text), getString(R.string.add_friend), new DialogWithYesOrNoUtils.DialogCallBack() {
                                    @Override
                                    public void executeEvent() {

                                    }

                                    @Override
                                    public void updatePassword(String oldPassword, String newPassword) {

                                    }

                                    @Override
                                    public void executeEditEvent(String editText) {
                                        if (!CommonUtils.isNetworkConnected(mContext)) {
                                            NToast.shortToast(mContext, "网络不可用");
                                            return;
                                        }
                                        addFriendMessage = editText;
                                        if (TextUtils.isEmpty(editText)) {
                                            addFriendMessage = "我是" + getSharedPreferences("config", MODE_PRIVATE).getString(SealConst.SEALTALK_LOGIN_NAME, "");
                                        }
                                        if (!TextUtils.isEmpty(mFriendId)) {
                                            LoadDialog.show(mContext);
                                            request(ADD_FRIEND);
                                        } else {
                                            NToast.shortToast(mContext, "id is null");
                                        }
                                    }
                                });
                            }
                        });

                    }
                    break;
                case ADD_FRIEND:
                    FriendInvitationResponse fres = (FriendInvitationResponse) result;
                    if (fres.getCode() == 200) {
                        NToast.shortToast(mContext, "请求成功");
                        LoadDialog.dismiss(mContext);
                    } else {
                        NToast.shortToast(mContext, "请求失败 错误码:" + fres.getCode());
                        LoadDialog.dismiss(mContext);
                    }
                    break;
            }
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (requestCode) {
            case ADD_FRIEND:
                NToast.shortToast(mContext, "你们已经是好友");
                LoadDialog.dismiss(mContext);
                break;
            case SEARCH_PHONE:
                if (state == AsyncTaskManager.HTTP_ERROR_CODE || state == AsyncTaskManager.HTTP_NULL_CODE) {
                    super.onFailure(requestCode, state, result);
                } else {
                    NToast.shortToast(mContext, "用户不存在");
                }
                LoadDialog.dismiss(mContext);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        hintKbTwo();
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private boolean isFriendOrSelf(String id) {
        String inputPhoneNumber = mEtSearch.getText().toString().trim();
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        String selfPhoneNumber = sp.getString(SealConst.SEALTALK_LOGING_PHONE, "");
        if (inputPhoneNumber != null) {
            if (inputPhoneNumber.equals(selfPhoneNumber)) {
                mFriend = new Friend(sp.getString(SealConst.SEALTALK_LOGIN_ID, ""),
                        sp.getString(SealConst.SEALTALK_LOGIN_NAME, ""),
                        Uri.parse(sp.getString(SealConst.SEALTALK_LOGING_PORTRAIT, "")));
                return true;
            } else {
                mFriend = SealUserInfoManager.getInstance().getFriendByID(id);
                if (mFriend != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void queryUser(QueryUserEvent event) {
        LoadDialog.dismiss(this);
        if(event.queryUserResponse != null){
            Log.d(TAG, "queryUser: " + event.queryUserResponse.FObject);
            if(event.queryUserResponse.FObject != null && !event.queryUserResponse.FObject.list.isEmpty()){
                searchItem.setVisibility(View.VISIBLE);
                mFriendId = event.queryUserResponse.FObject.list.get(0).FFriendID;
                Glide.with(this).load(event.queryUserResponse.FObject.list.get(0).FHeadUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(searchImage);
                searchName.setText(event.queryUserResponse.FObject.list.get(0).FCompanyName);
            }
        }else {
            NToast.shortToast(mContext, event.errorMessage);
            LoadDialog.dismiss(mContext);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addFriend(AddFriendEvent event) {
        LoadDialog.dismiss(this);
        if(event.addFriendResponse != null){
            Log.d(TAG, "queryUser: " + event.addFriendResponse.FObject);
            if(event.addFriendResponse.FObject != null){
                NToast.shortToast(mContext, "请求成功");
                LoadDialog.dismiss(mContext);
            }
        }else {
        NToast.shortToast(mContext, "请求失败:" + event.errorMessage);
        LoadDialog.dismiss(mContext);
    }
    }

}
