package com.juns.wechat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.adpter.SearchResultAdapter;
import com.juns.wechat.annotation.Content;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.common.BaseActivity;
import com.juns.wechat.xmpp.bean.SearchResult;

import java.util.List;

@Content(R.layout.activity_search_result)
public class SearchResultActivity extends BaseActivity {
    public static final String ARG_SEARCH_RESULTS = "search_results";

    private ListView lvSearchResultList;
    private List<UserBean> searchResults;
    private SearchResultAdapter searchResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        searchResults = getIntent().getParcelableArrayListExtra(ARG_SEARCH_RESULTS);
        if(searchResults == null || searchResults.isEmpty()){
            throw new IllegalArgumentException("search results is null or empty!");
        }
        ((TextView) findViewById(R.id.tvTitle)).setText("搜索结果");
        searchResultAdapter = new SearchResultAdapter(this);
        searchResultAdapter.setData(searchResults);
        lvSearchResultList = (ListView) findViewById(R.id.lvSearchResultList);
        lvSearchResultList.setAdapter(searchResultAdapter);

    }
}
