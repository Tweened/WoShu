package com.toxicant.hua.woshu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by hua on 2016/3/7.
 */
@SuppressLint("ValidFragment")
public class ReviewsFragment extends Fragment {
    private TextView tv;
    private String text;
    public ReviewsFragment(String text){
        this.text=text;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_reviews,null);
        tv= (TextView) v.findViewById(R.id.tv_reviews_summary);
        tv.setText(text);
        return v;
    }
}
