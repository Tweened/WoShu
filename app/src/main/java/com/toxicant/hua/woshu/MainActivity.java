package com.toxicant.hua.woshu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private final static  String TAG="Main";
    private static Boolean isExit = false;
    private OkhttpUitls okhttpUitls=OkhttpUitls.getInstance();
    private BookSqlHelper sqlHelper=null;
    private SQLiteDatabase db;
    private Gson gson=new Gson();
    private ProgressDialog dialog=null;
    private ArrayList<FavBook> mbooks=new ArrayList<>();
    private GridView gridView;
    private TextView emptyView;
    private FavAdapter adapter;

    private ImageLoader imageLoader=null;
    private DisplayImageOptions options=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setTitle("我书故我在");
        }
        emptyView= (TextView) findViewById(R.id.tv_fav_empty);
        //初始化loader
        imageLoader= ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        options=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.book_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        //初始化数据库
        sqlHelper=new BookSqlHelper(this,"book.db",null,1);
        db=sqlHelper.getWritableDatabase();
        //gridView
        gridView= (GridView) findViewById(R.id.gv_favBook);
        adapter=new FavAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isbnGetBookObject(mbooks.get(position).getIsbn());
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final FavBook book=mbooks.get(position);
                Snackbar snackbar=Snackbar.make(gridView,"确定删除《"+book.getName()+"》？",Snackbar.LENGTH_LONG);
                snackbar.setAction("删了~", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] isbnArg =new String[1];
                        isbnArg[0]=book.getIsbn();
                        db.delete("book", "isbn=?", isbnArg);
                        updateBooks();
                    }
                });
                snackbar.show();
                return true;
            }
        });


        dialog=new ProgressDialog(this);
        dialog.setMessage("正在加载数据…");
        dialog.setCancelable(false);
        updateBooks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_scan){
            Intent openCameraIntent = new Intent(MainActivity.this, CaptureActivity.class);
            startActivityForResult(openCameraIntent, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK &requestCode==0) {//成功扫描到数据
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
//           Toast.makeText(this,scanResult+"<-",Toast.LENGTH_SHORT).show();
               isbnGetBookObject(scanResult);
        }
        if (requestCode==666 &resultCode==RESULT_OK){//书籍详情页回来.并且喜欢了这本书
            updateBooks();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            exitBy2Click(); //调用双击退出函数
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }//onKeyDown
    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

    private void isbnGetBookObject(String isbn){
        dialog.show();
       okhttpUitls.get("https://api.douban.com/v2/book/isbn/" + isbn.trim(), new OkhttpUitls.MOkCallBack() {
           @Override
           public void onSuccess(String str) {
               ISBNBookBean isbnBook = gson.fromJson(str, ISBNBookBean.class);
               if (isbnBook.getId()!=null){
                   //这里传book给详情页面
                   Intent it=new Intent(MainActivity.this,BookInfoActivity.class);
                   it.putExtra("book",isbnBook);
                   startActivityForResult(it, 666);
               }else {
                   Toast.makeText(MainActivity.this,"很抱歉，没有这本书的资料",Toast.LENGTH_LONG).show();
               }
               dialog.hide();
           }

           @Override
           public void onError() {
               Toast.makeText(MainActivity.this,"你的网络有问题咯~",Toast.LENGTH_LONG).show();
               dialog.hide();
           }
       });
    }
    ArrayList<FavBook> getFavBooks(){
        ArrayList<FavBook> books=new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from book",null);
        if (cursor.moveToFirst()){
            do {
                String isbn=cursor.getString(cursor.getColumnIndex("isbn"));
                String image=cursor.getString(cursor.getColumnIndex("image"));
                String name=cursor.getString(cursor.getColumnIndex("name"));

                FavBook book=new FavBook();
                book.setIsbn(isbn);
                book.setImage(image);
                book.setName(name);
                books.add(book);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return books;
    }

    void updateBooks(){
        mbooks=getFavBooks();
        if (mbooks.size()==0){
            emptyView.setVisibility(View.VISIBLE);
        }else {
            emptyView.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }


    class FavAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mbooks.size();
        }

        @Override
        public Object getItem(int position) {
            return mbooks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            if (convertView!=null){
                v=convertView;
            }else{
                v= LayoutInflater.from(MainActivity.this).inflate(R.layout.item_favbook,null);
            }
            ImageView image= (ImageView) v.findViewById(R.id.iv_fav_image);
            TextView name= (TextView) v.findViewById(R.id.tv_fav_name);
            FavBook book=mbooks.get(position);
            imageLoader.displayImage(book.getImage(),image,options);
            name.setText(book.getName());
            return v;
        }
    }

}
