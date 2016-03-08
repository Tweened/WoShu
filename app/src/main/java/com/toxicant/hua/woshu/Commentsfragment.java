package com.toxicant.hua.woshu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hua on 2016/3/7.
 */
@SuppressLint("ValidFragment")
public class Commentsfragment extends Fragment {
    private ListView commentsListView;
    private TextView loadView;
    private ArrayList<Comment> comments=new ArrayList<>();
    private String bookID=null;
    public Commentsfragment(String bookID){
        this.bookID=bookID;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_comments,null);
        commentsListView= (ListView) v.findViewById(R.id.listView_comment);
        loadView= (TextView) v.findViewById(R.id.tv_comment_loading);
        initData();
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    void initData(){
        OkhttpUitls okhttpUitls=OkhttpUitls.getInstance();
        okhttpUitls.get("https://book.douban.com/subject/" + bookID + "/comments/", new OkhttpUitls.MOkCallBack() {
            @Override
            public void onSuccess(String str) {
                loadView.setVisibility(View.GONE);
                try{
                    comments=getComments(str);
                    commentsListView.setAdapter(new MyCommentsArrayAdapter(getActivity(),R.layout.item_comment,comments));
                }catch (Exception e){//如果没有人评论过，会抛出异常
                    loadView.setText("暂时没有人评论");
                    loadView.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onError() {

            }
        });

    }
    private  ArrayList<Comment> getComments(String html) {
        ArrayList<Comment> comments=new ArrayList<Comment>();
        Document doc= Jsoup.parse(html);
        Elements elements=doc.select(".comment-item");
        for (Element element : elements) {
            Comment comment=new Comment();
            Element nameElement=element.child(1).child(1);
            comment.setNameAndTime(nameElement.text());
            Element textElement=element.child(2);
            comment.setText(textElement.text());
            comments.add(comment);
        }
        return comments;
    }
    class MyCommentsArrayAdapter extends ArrayAdapter<Comment>{
        Context context;
        int resource;
        List<Comment> objects;
        public MyCommentsArrayAdapter(Context context, int resource, List<Comment> objects) {
            super(context,resource,objects);
            this.context=context;
            this.resource=resource;
            this.objects=objects;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            if (convertView==null){
                v=LayoutInflater.from(context).inflate(resource,null);
            }else {
                v=convertView;
            }
            TextView text= (TextView) v.findViewById(R.id.tv_comment_text);
            TextView name= (TextView) v.findViewById(R.id.tv_comment_name);
            Comment c=objects.get(position);
            text.setText("\t\t"+c.getText());
            name.setText(c.getNameAndTime());
            return v;
        }
    }
}
