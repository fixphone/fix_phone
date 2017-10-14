package dhcc.cn.com.fix_phone.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import dhcc.cn.com.fix_phone.R;
import dhcc.cn.com.fix_phone.adapter.ConversationListAdapterEx;
import dhcc.cn.com.fix_phone.base.BaseFragment;
import dhcc.cn.com.fix_phone.base.GlideImageLoader;
import dhcc.cn.com.fix_phone.bean.CirCleADResponse;
import dhcc.cn.com.fix_phone.event.CircleAdEvent;
import dhcc.cn.com.fix_phone.ui.activity.NewFriendListActivity;
import dhcc.cn.com.fix_phone.ui.widget.DragPointView;
import dhcc.cn.com.fix_phone.utils.NToast;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 2017/9/16 23
 */
public class ImFragment extends BaseFragment implements DragPointView.OnDragListencer,
        IUnReadMessageObserver {

    @BindView(R.id.im_title_rg)
    RadioGroup mTitleRg;
    @BindView(R.id.list_item)
    RadioButton list_item;
    @BindView(R.id.add_friend_iv)
    ImageView mAddFriendIv;
    @BindView(R.id.seal_num)
    DragPointView mUnreadNumView;

    private ConversationListFragment mConversationListFragment = null;
    private ContactsFragment contactsFragment = null;
    private Conversation.ConversationType[] mConversationsTypes = null;
    private boolean isDebug;

    public static ImFragment newInstance() {
        return new ImFragment();
    }
    
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_im;
    }

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        isDebug = getContext().getSharedPreferences("config", getContext().MODE_PRIVATE).getBoolean("isDebug", false);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        contactsFragment = new ContactsFragment();
        initConversationList();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content_fl, contactsFragment);
        fragmentTransaction.add(R.id.content_fl, mConversationListFragment);
        fragmentTransaction.commit();
        list_item.setChecked(true);
        changeFragment(mConversationListFragment);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mTitleRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i){
                    case R.id.list_item:
                        changeFragment(mConversationListFragment);
                        break;
                    case R.id.contacts_item:
                        changeFragment(contactsFragment);
                        break;
                    default:
                        break;
                }
            }
        });
        mAddFriendIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), NewFriendListActivity.class));
            }
        });
        mUnreadNumView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mUnreadNumView.setDragListencer(this);
    }

    private void changeFragment(Fragment fragment){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.hide(contactsFragment);
        ft.hide(mConversationListFragment);
        ft.show(fragment);
        ft.commit();
    }

    private Fragment initConversationList() {
        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
            Uri uri;
            if (isDebug) {
                uri = Uri.parse("rong://" + getContext().getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "true") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                        .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
                        .build();
                mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                        Conversation.ConversationType.GROUP,
                        Conversation.ConversationType.PUBLIC_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.SYSTEM,
                        Conversation.ConversationType.DISCUSSION
                };

            } else {
                uri = Uri.parse("rong://" + getContext().getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                        .build();
                mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                        Conversation.ConversationType.GROUP,
                        Conversation.ConversationType.PUBLIC_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.SYSTEM
                };
            }
            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return listFragment;
        } else {
            return mConversationListFragment;
        }
    }

    @Override
    public void onCountChanged(int count) {
        if (count == 0) {
            mUnreadNumView.setVisibility(View.GONE);
        } else if (count > 0 && count < 100) {
            mUnreadNumView.setVisibility(View.VISIBLE);
            mUnreadNumView.setText(String.valueOf(count));
        } else {
            mUnreadNumView.setVisibility(View.VISIBLE);
            mUnreadNumView.setText("···");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("-----");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDragOut() {
        mUnreadNumView.setVisibility(View.GONE);
        NToast.shortToast(getContext(), "清除成功");
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (conversations != null && conversations.size() > 0) {
                    for (Conversation c : conversations) {
                        RongIM.getInstance().clearMessagesUnreadStatus(c.getConversationType(), c.getTargetId(), null);
                    }
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {

            }
        }, mConversationsTypes);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowAd(CircleAdEvent event) {
    }
}
