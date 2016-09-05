package com.juns.wechat.activity;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.juns.wechat.R;
import com.juns.wechat.chat.adpter.ExpressionAdapter;
import com.juns.wechat.chat.adpter.ExpressionPagerAdapter;
import com.juns.wechat.chat.adpter.VoicePlayClickListener;
import com.juns.wechat.chat.utils.CommonUtils;
import com.juns.wechat.chat.utils.SmileUtils;
import com.juns.wechat.chat.widght.ExpandGridView;
import com.juns.wechat.chat.widght.PasteEditText;
import com.juns.wechat.common.CommonUtil;
import com.juns.wechat.util.LogUtil;
import com.juns.wechat.util.ToastUtil;
import com.juns.wechat.view.AudioRecordButton;
import com.juns.wechat.xmpp.util.SendMessage;
import com.wangzhe.photopicker.PhotoPicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王者 on 2016/8/7.
 */
public class ChatInputManager implements View.OnClickListener{
    private static final int EMOTICONS_COUNT = 62;

    private Button btnSetModeVoice; //输入语音按钮
    private Button btnSetModeKeyBoard; //输入文字按钮
    private LinearLayout llPressToSpeak; //按住说话
    private AudioRecordButton btnRecord; //录音按钮
    private RelativeLayout rlInputText; //输入文字区域
    private PasteEditText etInputText; //输入文本框
    private ImageView ivEmoticonsNormal; //表情按钮
    private ImageView ivEmoticonsChecked; //表情按钮选中
    private Button btnSend; //发送按钮
    private Button btnMore; //更多按钮
    private LinearLayout llMore; //表情及更多功能区域
    private LinearLayout llEmoticonContainer; //表情容器
    private ViewPager emoticonsViewPager; //表情viewPager
    private LinearLayout llMoreFunctionContainer; //图片，相册等更多功能容器
    private LinearLayout viewPhoto; //图片

    private ListView lvMessages;  //消息列表
    private List<String> emoticonsFileNames;
  //  private AnimationDrawable animationDrawable;

    private ChatActivity mChatActivity;


    public ChatInputManager(ChatActivity chatActivity){
        View view = chatActivity.getWindow().getDecorView();
        mChatActivity = chatActivity;

        btnSetModeVoice = (Button) view.findViewById(R.id.btn_set_mode_voice);
        btnSetModeKeyBoard = (Button) view.findViewById(R.id.btn_set_mode_keyboard);
        llPressToSpeak = (LinearLayout) view.findViewById(R.id.ll_press_to_speak);
        btnSend = (AudioRecordButton) view.findViewById(R.id.btn_record);

        rlInputText = (RelativeLayout) view.findViewById(R.id.rl_input_text);
        etInputText = (PasteEditText) view.findViewById(R.id.et_input_text);

        ivEmoticonsNormal = (ImageView) view.findViewById(R.id.iv_emoticons_normal);
        ivEmoticonsChecked = (ImageView) view.findViewById(R.id.iv_emoticons_checked);
        btnSend = (Button) view.findViewById(R.id.btn_send);
        btnMore = (Button) view.findViewById(R.id.btn_more);
        llMore = (LinearLayout) view.findViewById(R.id.ll_more);
        llEmoticonContainer = (LinearLayout) view.findViewById(R.id.ll_emoticon_container);
        emoticonsViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        llMoreFunctionContainer = (LinearLayout) view.findViewById(R.id.ll_more_function_container);
        viewPhoto = (LinearLayout) view.findViewById(R.id.view_photo);

        lvMessages = (ListView) view.findViewById(R.id.lvMessages);
        lvMessages.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    public void onCreate(){
        initEmoticonsList();
        initEmoticonsViewPager();
        setListener();
    }

    private void initEmoticonsList(){
        emoticonsFileNames = new ArrayList<>();
        for (int x = 0; x <= EMOTICONS_COUNT; x++) {
            String filename = "f_static_0" + x;
            emoticonsFileNames.add(filename);
        }
    }

    private void initEmoticonsViewPager(){
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        View gv3 = getGridChildView(3);
        views.add(gv1);
        views.add(gv2);
        views.add(gv3);
        emoticonsViewPager.setAdapter(new ExpressionPagerAdapter(views));
    }

    private void setListener(){
        etInputText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    rlInputText.setBackgroundResource(R.drawable.input_bar_bg_active);
                } else {
                    rlInputText.setBackgroundResource(R.drawable.input_bar_bg_normal);
                }

            }
        });
        etInputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlInputText.setBackgroundResource(R.drawable.input_bar_bg_active);
                llMore.setVisibility(View.GONE);
                ivEmoticonsNormal.setVisibility(View.VISIBLE);
                ivEmoticonsChecked.setVisibility(View.INVISIBLE);
                llEmoticonContainer.setVisibility(View.GONE);
                llMoreFunctionContainer.setVisibility(View.GONE);
            }
        });
        // 监听文字框
        etInputText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!TextUtils.isEmpty(s)) {
                    btnMore.setVisibility(View.GONE);
                    btnSend.setVisibility(View.VISIBLE);
                } else {
                    btnMore.setVisibility(View.VISIBLE);
                    btnSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnRecord.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
            @Override
            public void onFinished(float seconds, String filePath) {
                sendVoice(mChatActivity.getContactName(), (int) seconds, filePath);
            }
        });

        lvMessages.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CommonUtil.hideKeyboard(mChatActivity);
                llMore.setVisibility(View.GONE);
                ivEmoticonsNormal.setVisibility(View.VISIBLE);
                ivEmoticonsChecked.setVisibility(View.INVISIBLE);
                return false;
            }
        });

        ivEmoticonsNormal.setOnClickListener(this);
        ivEmoticonsChecked.setOnClickListener(this);
        btnSetModeKeyBoard.setOnClickListener(this);
        btnSetModeVoice.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnMore.setOnClickListener(this);

        mChatActivity.findViewById(R.id.view_camera).setOnClickListener(this);
        mChatActivity.findViewById(R.id.view_file).setOnClickListener(this);
        mChatActivity.findViewById(R.id.view_video).setOnClickListener(this);
        mChatActivity.findViewById(R.id.view_photo).setOnClickListener(this);
        mChatActivity.findViewById(R.id.view_location).setOnClickListener(this);
        mChatActivity.findViewById(R.id.view_audio).setOnClickListener(this);
    }

    public void onDestroy(){
        mChatActivity = null;
    }

    @Override
    public void onClick(View view) {
        String otherUserName = mChatActivity.getContactName();
        int id = view.getId();
        switch (id) {
            case R.id.view_camera:
               // selectPicFromCamera();// 点击照相图标
                break;
            case R.id.view_file:
                // 发送文件
               // selectFileFromLocal();
                break;
            case R.id.view_video:
                // 视频通话
			/*if (!EMChatManager.getDbManager().isConnected())
				Toast.makeText(this, Constants.NET_ERROR, 0).show();
			else
				startActivity(new Intent(this, VideoCallActivity.class)
						.putExtra("username", toChatUsername).putExtra(
								"isComingCall", false));*/
                break;
            case R.id.view_photo:
                selectPicFromLocal(); // 点击图片图标
                break;
            case R.id.view_location:
                break;
            case R.id.view_audio:
                // 语音通话
                break;
            case R.id.iv_emoticons_normal:
                CommonUtil.hideKeyboard(mChatActivity);
                // 点击显示表情框
                llMore.setVisibility(View.VISIBLE);
                ivEmoticonsNormal.setVisibility(View.INVISIBLE);
                ivEmoticonsChecked.setVisibility(View.VISIBLE);
                llMoreFunctionContainer.setVisibility(View.GONE);
                llEmoticonContainer.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_emoticons_checked:// 点击隐藏表情框
                CommonUtil.hideKeyboard(mChatActivity);
                ivEmoticonsNormal.setVisibility(View.VISIBLE);
                ivEmoticonsChecked.setVisibility(View.INVISIBLE);
                llMoreFunctionContainer.setVisibility(View.VISIBLE);
                llEmoticonContainer.setVisibility(View.GONE);
                llMore.setVisibility(View.GONE);
                break;
            case R.id.btn_set_mode_keyboard:
                setModeKeyboard(view);
                break;
            case R.id.btn_more:
                more(view);
                break;
            case R.id.btn_set_mode_voice:
                setModeVoice(view);
                break;
            case R.id.btn_send:
                // 点击发送按钮(发文字和表情)
                sendText(otherUserName);
                break;
            default:
                break;
        }
    }

    /**
     * 发送文本消息
     * @param otherUserName
     */
    private void sendText(String otherUserName){
        String content = etInputText.getText().toString();
        if(!TextUtils.isEmpty(content)){
            SendMessage.sendTextMsg(otherUserName, content);
            etInputText.getText().clear();
        }
    }

    /**
     * 从图库获取图片
     */
    private void selectPicFromLocal() {
        PhotoPicker.builder()
                .setPhotoCount(9)
                .setGridColumnCount(3)
                .start(mChatActivity);
    }

    /***
     * 发送语音消息
     * @param seconds
     * @param filePath
     */
    private void sendVoice(String otherUserName, int seconds, String filePath){
        File file = new File(filePath);
        if(!file.exists()){
            LogUtil.e("filePath is invalid!");
            return;
        }
        SendMessage.sendVoiceMsg(otherUserName, seconds, filePath);
    }



    /**
     * 显示语音图标按钮
     *
     * @param view
     */
    public void setModeVoice(View view) {
        CommonUtil.hideKeyboard(mChatActivity);
        rlInputText.setVisibility(View.GONE);
        llMore.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        btnSetModeKeyBoard.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.GONE);
        btnMore.setVisibility(View.VISIBLE);
        llPressToSpeak.setVisibility(View.VISIBLE);
        ivEmoticonsNormal.setVisibility(View.VISIBLE);
        ivEmoticonsChecked.setVisibility(View.INVISIBLE);
        llMoreFunctionContainer.setVisibility(View.VISIBLE);
        llEmoticonContainer.setVisibility(View.GONE);
    }

    /**
     * 显示键盘图标
     *
     * @param view
     */
    public void setModeKeyboard(View view) {
        rlInputText.setVisibility(View.VISIBLE);
        llMore.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        btnSetModeVoice.setVisibility(View.VISIBLE);
        etInputText.requestFocus();
        llPressToSpeak.setVisibility(View.GONE);
        if (TextUtils.isEmpty(etInputText.getText())) {
            btnMore.setVisibility(View.VISIBLE);
            btnSend.setVisibility(View.GONE);
        } else {
            btnMore.setVisibility(View.GONE);
            btnSend.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示或隐藏图标按钮页
     *
     * @param view
     */
    public void more(View view) {
        if (llMore.getVisibility() == View.GONE) {
            CommonUtil.hideKeyboard(mChatActivity);
            llMore.setVisibility(View.VISIBLE);
            llMoreFunctionContainer.setVisibility(View.VISIBLE);
            llEmoticonContainer.setVisibility(View.GONE);
        } else {
            if (llEmoticonContainer.getVisibility() == View.VISIBLE) {
                llEmoticonContainer.setVisibility(View.GONE);
                llMoreFunctionContainer.setVisibility(View.VISIBLE);
                ivEmoticonsNormal.setVisibility(View.VISIBLE);
                ivEmoticonsChecked.setVisibility(View.INVISIBLE);
            } else {
                llMore.setVisibility(View.GONE);
            }

        }

    }

    /**
     * 获取表情的gridview的子view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(mChatActivity, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<>();
        if (i == 1) {
            List<String> list1 = emoticonsFileNames.subList(0, 21);
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(emoticonsFileNames.subList(21, emoticonsFileNames.size()));
        } else if (i == 3) {
            list.addAll(emoticonsFileNames.subList(42, emoticonsFileNames.size()));
        }
        list.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(mChatActivity,
                1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    // 文字输入框可见时，才可输入表情
                    // 按住说话可见，不让输入表情
                    if (btnSetModeKeyBoard.getVisibility() != View.VISIBLE) {

                        if (filename != "delete_expression") { // 不是删除键，显示表情
                            String fieldValue = SmileUtils.getFieldValue(filename);
                            etInputText.append(SmileUtils.getSmiledText(mChatActivity, fieldValue));
                        } else { // 删除文字或者表情
                            if (!TextUtils.isEmpty(etInputText.getText())) {

                                int selectionStart = etInputText.getSelectionStart();// 获取光标的位置
                                if (selectionStart > 0) {
                                    String body = etInputText.getText()
                                            .toString();
                                    String tempStr = body.substring(0,
                                            selectionStart);
                                    int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                    if (i != -1) {
                                        CharSequence cs = tempStr.substring(i,
                                                selectionStart);
                                        if (SmileUtils.containsKey(cs.toString()))
                                            etInputText.getEditableText()
                                                    .delete(i, selectionStart);
                                        else
                                            etInputText.getEditableText()
                                                    .delete(selectionStart - 1,
                                                            selectionStart);
                                    } else {
                                        etInputText.getEditableText()
                                                .delete(selectionStart - 1,
                                                        selectionStart);
                                    }
                                }
                            }

                        }
                    }
                } catch (Exception e) {
                }

            }
        });
        return view;
    }

}
