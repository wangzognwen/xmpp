package com.juns.wechat.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.chat.BaseActivity;

/**
 * Created by 王宗文 on 2016/6/20.
 */
public class SearchActivity extends BaseActivity {

    private EditText etSearch;
    private RelativeLayout rlSearch;
    private TextView tvSearchContent;
    private String search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        setListener();
    }

    private void initView(){
        etSearch = (EditText) findViewById(R.id.et_search);
        rlSearch = (RelativeLayout) findViewById(R.id.re_search);
        tvSearchContent = (TextView) findViewById(R.id.tv_search);
    }

    private void setListener(){
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search = etSearch.getText().toString();
                if(!TextUtils.isEmpty(search)){
                    tvSearchContent.setText(search);
                    rlSearch.setVisibility(View.VISIBLE);
                }else {
                    tvSearchContent.setText("");
                    rlSearch.setVisibility(View.GONE);
                }
            }
        });

        rlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchDialog();
            }
        });
    }

    private void showSearchDialog(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在查找联系人...");
        dialog.show();


    }
}
