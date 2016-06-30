package com.example.hachimori.recyclerviewsample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecyclerViewSampleFragment extends Fragment {
    
    private RecyclerView mRecyclerView;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.recyclerview_sample, container, false);
        
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        
        // LinearLayout 形式の RecyclerView とする
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        
        CustomAdapter adapter = new CustomAdapter(new ArrayList<Student>() {{
            add(new Student("name1", 20, "male"));
            add(new Student("name2", 22, "female"));
            add(new Student("name3", 18, "female"));
            add(new Student("name4", 21, "male"));
            add(new Student("name5", 21, "male"));
        }});
        mRecyclerView.setAdapter(adapter);
        
        // スワイプされたときの挙動を定義
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
    
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                
                // 横にスワイプされたら要素を消す
                int swipedPosition = viewHolder.getAdapterPosition();
                CustomAdapter adapter = (CustomAdapter) mRecyclerView.getAdapter();
                adapter.remove(swipedPosition);
            }
        };
        (new ItemTouchHelper(callback)).attachToRecyclerView(mRecyclerView);
        
        return view;
    }
    
    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        
        private List<Student> mStudentList;
        
        
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            TextView age;
            TextView gender;
            Button add;
            
            public ViewHolder(View v) {
                super(v);
                
                // 名前
                name = (TextView) v.findViewById(R.id.name);
                
                // 年齢
                age = (TextView) v.findViewById(R.id.age);
                
                // 性別
                gender = (TextView) v.findViewById(R.id.gender);
                
                // "下に追加する" ボタン
                add = (Button) v.findViewById(R.id.add);
            }
        }
        
        public CustomAdapter(List<Student> studentList) {
            mStudentList = studentList;
        }
         
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            
            View v = LayoutInflater
                    .from(viewGroup.getContext())
                    .inflate(R.layout.recyclerview_sample_item, viewGroup, false);
            
            return new ViewHolder(v);
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int position) {
            
            // 文字列をセット
            viewHolder.name.setText(mStudentList.get(position).getName());
            viewHolder.age.setText(String.valueOf(mStudentList.get(position).getAge()));
            viewHolder.gender.setText(mStudentList.get(position).getGender());
            
            // "下に追加" ボタンが押されたら、下にランダムな要素を追加する
            viewHolder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    
                    // ランダムな要素の作成
                    Random rnd = new Random(); 
                    Student toAdd = new Student("name" + rnd.nextInt(10), 18 + rnd.nextInt(5), rnd.nextBoolean() ? "male" : "female");
                    
                    // 要素の追加
                    addItem(viewHolder.getAdapterPosition() + 1, toAdd);
                }
            });
        }
        
        @Override
        public int getItemCount() {
            return mStudentList.size();
        }
    
        /**
         * 要素を挿入する
         * 
         * @param position 挿入する位置
         * @param student 挿入する要素
         */
        public void addItem(int position, Student student) {
            
            mStudentList.add(position, student);
            notifyItemInserted(position);
        }
    
        /**
         * 要素を削除する
         * 
         * @param position 削除する位置
         */
        public void remove(int position) {
            
            mStudentList.remove(position);
            notifyItemRemoved(position);
        }
    }
    
    private class Student {
        
        /** 名前 **/ 
        private String mName;
        
        /** 年齢 **/ 
        private int mAge;
        
        /** 性別 **/ 
        private String mGender;
        
        public Student(String name, int age, String gender) {
            mName = name;
            mAge = age;
            mGender = gender;
        }
        
        public String getName() {
            return mName;
        }
        
        public int getAge() {
            return mAge;
        }
        
        public String getGender() {
            return mGender;
        }
    }
}
