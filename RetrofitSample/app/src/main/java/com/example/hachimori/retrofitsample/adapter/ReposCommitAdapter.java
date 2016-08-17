package com.example.hachimori.retrofitsample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.hachimori.retrofitsample.R;
import com.example.hachimori.retrofitsample.model.Commits;
import com.example.hachimori.retrofitsample.model.Repos;

import java.util.List;

/**
 * Created by benhachimori on 2016/08/07.
 */
public class ReposCommitAdapter extends BaseExpandableListAdapter {
    
    private Context mContext;
    private List<Repos> mReposList;
    private List<List<Commits>> mCommitList;
    
    public ReposCommitAdapter(Context context, List<Repos> reposList, List<List<Commits>> commitList) {
        mContext = context;
        mReposList = reposList;
        mCommitList = commitList;
    }
    
    @Override
    public int getGroupCount() {
        return mReposList.size();
    }
    
    @Override
    public int getChildrenCount(int groupPosition) {
        return mCommitList.get(groupPosition).size();
    }
    
    @Override
    public Repos getGroup(int groupPosition) {
        return mReposList.get(groupPosition);
    }
    
    @Override
    public Commits getChild(int groupPosition, int childPosition) {
        return mCommitList.get(groupPosition).get(childPosition);
    }
    
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    
    @Override
    public boolean hasStableIds() {
        return true;
    }
    
    /**
     * 親アイテムの取得
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        
        View v = LayoutInflater
                .from(mContext)
                .inflate(R.layout.repository_list_cell, null);
        
        // 親アイテムを取得
        Repos repos = mReposList.get(groupPosition);
        
        // リポジトリ名をセット
        ((TextView) v.findViewById(R.id.repository_name)).setText(repos.getName());
        
        // リポジトリの説明をセット
        ((TextView) v.findViewById(R.id.repository_description)).setText(repos.getDescription());
        
        // URLをセット
        ((TextView) v.findViewById(R.id.repository_url)).setText(repos.getUrl());
        
        return v;
    }
    
    /**
     * 子アイテムの取得
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        
        View v = LayoutInflater
                .from(mContext)
                .inflate(R.layout.commit_list_cell, null);
        
        // 子アイテムを取得
        Commits commit = mCommitList.get(groupPosition).get(childPosition);
        
        // Commit ID をセット
        ((TextView) v.findViewById(R.id.commit_id)).setText(commit.getSha());
        
        // 名前をセット
        ((TextView) v.findViewById(R.id.committer_name)).setText(commit.getCommiterName());
        
        // Email をセット 
        ((TextView) v.findViewById(R.id.committer_email)).setText("(" + commit.getCommiterEmail() + ")");
        
        // 日付をセット
        ((TextView) v.findViewById(R.id.commit_date)).setText(commit.getCommitDate());
        
        // 説明をセット
        ((TextView) v.findViewById(R.id.commit_description)).setText(commit.getMessage());
        
        return v;
    }
    
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}