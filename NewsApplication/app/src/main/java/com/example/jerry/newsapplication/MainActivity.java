package com.example.jerry.newsapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
 import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Newss>>
{
    private static final String NEWS_URL="https://content.guardianapis.com/search?q=debate&tag=politics/politics&from-date=2014-01-01&api-key=test&show-tags=contributor";
    private static final int LOADER=0;
    private static final String LOG="MainActivity.LOG_TAG";
    private static final int DELAY=3000;

    private Adapter Adapter;
    private ListView listView;
    private ArrayList<Newss>news;
    private TextView text;
    private SwipeRefreshLayout swipeLayout;
    private Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        news = new ArrayList<>();

        Adapter = new Adapter(this, news);
        listView = (ListView) findViewById(R.id.default_list_views);
        text = (TextView) findViewById(R.id.no_data_text_views);
        text.setVisibility(View.GONE);
        listView.setAdapter(Adapter);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(LOG, "onRefresh called from SwipeRefreshLayout");
                            refresh();

                    }
                }
        );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = news.get(position).getMLinkk();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });
        handler.post(runnableCodee);
    }
    private void refresh(){
        if(isNetworkConnected()){
            swipeLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeLayout.setRefreshing(true);
                }
            });
            handler.removeCallbacks(runnableCodee);
            getSupportLoaderManager().initLoader(LOADER,null,this).forceLoad();
        }else{
            text.setText(R.string.device_not_connected);
            text.setVisibility(View.VISIBLE);
        }

    }
    private boolean isNetworkConnected(){
        ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo()!=null;

    }
    private Runnable runnableCodee=new Runnable() {
        @Override
        public void run() {
            Log.d("Handlers","Called on main thread");
            refresh();
            handler.postDelayed(runnableCodee,DELAY);
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        refresh();
        return super.onOptionsItemSelected(item);

    }
    @Override
    public Loader<ArrayList<Newss>> onCreateLoader(int id,Bundle args){
        switch (id){
            case LOADER:
                Log.d(LOG,"onCreateLoader");
                return new ListLoader(this);
            default:
                return null;
        }
    }
    public static class ListLoader extends AsyncTaskLoader<ArrayList<Newss>> {
        public ListLoader(Context context) {
            super(context);
        }

        @Override
        public ArrayList<Newss> loadInBackground() {
            Log.d(LOG,"loadInBackground");
            return FetchingData.Newss(NEWS_URL);
        }

    }
    @Override
    public void onLoadFinished(Loader<ArrayList<Newss>> loader, ArrayList<Newss>data){

        swipeLayout.setRefreshing(false);
        news.clear();
        if(data!=null){
            news.addAll(data);
        }
        Adapter.notifyDataSetChanged();
        if(news.size()==0){
            text.setText(R.string.no_data_available);
            text.setVisibility(View.VISIBLE);
        }else{
            text.setVisibility(View.GONE);
        }
        handler.postDelayed(runnableCodee,DELAY);
    }
    @Override
    public void onLoaderReset(Loader<ArrayList<Newss>>loader) {
        Log.d(LOG,"Refresh");
    }
}