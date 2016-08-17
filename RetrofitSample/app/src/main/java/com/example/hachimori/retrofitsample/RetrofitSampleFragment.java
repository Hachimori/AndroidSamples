package com.example.hachimori.retrofitsample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.hachimori.retrofitsample.adapter.ReposCommitAdapter;
import com.example.hachimori.retrofitsample.model.Commits;
import com.example.hachimori.retrofitsample.model.GitHubService;
import com.example.hachimori.retrofitsample.model.Repos;
import com.example.hachimori.retrofitsample.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by benhachimori on 2016/07/31.
 */
public class RetrofitSampleFragment extends Fragment {
    
    private static String TAG = RetrofitSampleFragment.class.getSimpleName();
    
    /**
     * GitHub アカウント名の入力欄
     */
    @BindView(R.id.github_account_name) EditText mUserName;
    
    /**
     * リポジトリ/コミットのリスト
     */
    @BindView(R.id.repos_commit_list) ExpandableListView mReposCommitList;
    
    /**
     * ユーザ情報表示用の TextView
     */
    @BindView(R.id.user_info) TextView mUserInfo;
    
    /**
     * リポジトリ/コミットのリストの Adapter
     */
    private ReposCommitAdapter mReposCommitAdapter;
    
    private Retrofit mRetrofit;
    private GitHubService mService;
    
    private Unbinder mUnbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.retrofit_sample, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        
        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = mRetrofit.create(GitHubService.class);
        
        return view;
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    } 
    
    /**
     * "Get User Info" ボタンが押された時の挙動
     *   1. EditText から GitHub ユーザ名を取得
     *   2. ユーザ情報を取得
     *   3. TextView に表示
     */
    @SuppressWarnings("unused")
    @OnClick(R.id.get_user_info)
    void onGetUserInfoClicked() {
        
        // ユーザ名を取得
        String userName = mUserName.getText().toString();
        
        Call<User> userCall = mService.getUser(userName);
        
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                
                // ユーザ情報の取得
                User user = response.body();
                
                // ユーザ情報を文字列として取得
                StringBuilder sb = new StringBuilder();
                sb.append("User: " + user.getName() + "\n");
                sb.append("Company: " + user.getCompany() + "\n");
                sb.append("Email: " + user.getEmail() + "\n");
                sb.append("Bio: " + user.getBio() + "\n");
                sb.append("Created at: " + user.getCreatedAt() + "\n");
                sb.append("Updated at: " + user.getUpdatedAt() + "\n");
                
                // 文字列を取得
                mUserInfo.setText(sb.toString());
                
                // ユーザ情報を表示する
                mUserInfo.setVisibility(View.VISIBLE);
                
                // リポジトリのリストを非表示にする
                mReposCommitList.setVisibility(View.GONE);
            }
    
            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }
    
    /**
     * "Get Repository" ボタンが押された時の挙動
     *   1. EditText から GitHub ユーザ名を取得
     *   2. ユーザのリポジトリ一覧を取得
     *   3. 各リポジトリのコミット一覧を取得
     *   4. ExpandableListView に表示
     */
    @SuppressWarnings("unused")
    @OnClick(R.id.get_repository)
    void onGetRepositoryClicked() {
        
        final List<Repos> reposList = new ArrayList<>();
        final List<List<Commits>> commitsList = new ArrayList<>();
                
        mReposCommitAdapter = new ReposCommitAdapter(getContext(), reposList, commitsList);
        mReposCommitList.setAdapter(mReposCommitAdapter);
    
        new AsyncTask<String, Void, Void>() {
    
            @Override
            protected Void doInBackground(String... params) {
                
                // ユーザ名
                String userName = params[0];
                
                try {
                    // GitHub API にアクセスして、ユーザの GitHub リポジトリ一覧の取得
                    Call<List<Repos>> reposCall = mService.listRepos(userName);
                    reposList.addAll(reposCall.execute().body());
                    
                    // GitHub API にアクセスして、各リポジトリのコミット一覧を取得
                    for (Repos repos : reposList) {
                        String reposName = repos.getName();
                        Call<List<Commits>> commitsCall = mService.listCommit(userName, reposName);
                        commitsList.add(commitsCall.execute().body());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                return null;
            }
    
            @Override
            protected void onPostExecute(Void aVoid) {
                // データ更新の通知
                mReposCommitAdapter.notifyDataSetChanged();
                
                // リポジトリのリストを表示する
                mReposCommitList.setVisibility(View.VISIBLE);
                
                // ユーザ情報を非表示にする
                mUserInfo.setVisibility(View.GONE);
            }
        }.execute(mUserName.getText().toString());
    }
}