package com.toxicant.hua.woshu;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;


/**
 * Created by hua on 2016/3/7.
 */
public class BookInfoActivity extends AppCompatActivity {
    private final static String TAG="bookinfo";
    private TabLayout tabLayout=null;
    private ViewPager viewPager=null;
    private ImageView image=null;
    private TextView tvTitle=null;
    private TextView tvOther=null;

    private BookSqlHelper sqlHelper=null;
    private SQLiteDatabase db;

    private ImageLoader imageLoader=null;
    private DisplayImageOptions options=null;

    private ArrayList<Fragment> fragments=new ArrayList<>();
    private String[] title={"网友点评","内容概要"};
    ISBNBookBean isbnBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookinfo);
        Intent it=getIntent();
        isbnBook= (ISBNBookBean) it.getSerializableExtra("book");
        ActionBar bar=getSupportActionBar();
        bar.setTitle("书籍详情");
        bar.setDisplayHomeAsUpEnabled(true);
        //初始化数据库
        sqlHelper=new BookSqlHelper(this,"book.db",null,1);
        db=sqlHelper.getWritableDatabase();
        //初始化loader
        imageLoader= ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        options=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.book_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        //初始化view
        tabLayout=$(R.id.tabLayout_info);
        viewPager=$(R.id.viewpager_info);
        image=$(R.id.iv_info_image);
        tvTitle=$(R.id.tv_info_title);
        tvOther=$(R.id.tv_info_other);
        //数据填充
        imageLoader.displayImage(isbnBook.getImage(), image, options);
        tvTitle.setText(isbnBook.getTitle());

        String o="出版社:"+isbnBook.getPublisher()+"\n"
                +"出版日期："+isbnBook.getPubdate()+"\n"
                +"价格："+isbnBook.getPrice()+"\n"
                +"豆瓣评分："+isbnBook.getRating().getAverage();
        tvOther.setText(o);


        //viewpager初始化


        Commentsfragment commentsfragment=new Commentsfragment(isbnBook.getId());
        ReviewsFragment reviewsFragment=new ReviewsFragment(isbnBook.getSummary());
        fragments.add(commentsfragment);
        fragments.add(reviewsFragment);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return title[position];
            }
        });

        //添加tab
//        tabLayout.addTab(tabLayout.newTab().setText("短评"));
//        tabLayout.addTab(tabLayout.newTab().setText("长评"));
        tabLayout.setupWithViewPager(viewPager);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_like){
            //插入到数据库
            ContentValues values=new ContentValues();
            values.put("isbn",isbnBook.getIsbn13());
            values.put("name",isbnBook.getTitle());
            values.put("image",isbnBook.getImage());
            db.insert("book",null,values);

            Toast.makeText(this,"已收藏~",Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
            return true;
        }
        if (item.getItemId() == android.R.id.home){
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public <T extends View> T $(int id){
        return (T) findViewById(id);
    }
}
