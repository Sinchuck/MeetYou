package com.example.yang.meetyou;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Yang on 2016/12/5.
 */
public class CommentListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private CommentListAdapter mCommentListAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
    }

    class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                   CommentListActivity.this).inflate(R.layout.comment, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
            //
        }

        @Override
        public int getItemCount()
        {
            return 0;
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {



            public MyViewHolder(View view)
            {
                super(view);

            }
        }
    }

}
