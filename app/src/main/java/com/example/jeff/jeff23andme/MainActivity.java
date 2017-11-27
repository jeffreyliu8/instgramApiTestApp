package com.example.jeff.jeff23andme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.TextView;

import com.example.jeff.jeff23andme.endpoints.UsersEndpoint;
import com.example.jeff.jeff23andme.model.Image;
import com.example.jeff.jeff23andme.model.Images;
import com.example.jeff.jeff23andme.model.Media;

import com.example.jeff.jeff23andme.model.Recent;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    private DataAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private List<Student> studentList;


    protected Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar = findViewById(R.id.toolbar);
        tvEmptyView = findViewById(R.id.empty_view);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        studentList = new ArrayList<>();
        handler = new Handler();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Android Students");

        }

        loadData();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);

        // create an Object for Adapter
        mAdapter = new DataAdapter(studentList, mRecyclerView);

        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);
        //  mAdapter.notifyDataSetChanged();


        if (studentList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                studentList.add(null);
                mAdapter.notifyItemInserted(studentList.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        studentList.remove(studentList.size() - 1);
                        mAdapter.notifyItemRemoved(studentList.size());
                        //add items one by one
                        int start = studentList.size();
                        int end = start + 20;

                        for (int i = start + 1; i <= end; i++) {
                            studentList.add(new Student("Student " + i, "AndroidStudent" + i + "@gmail.com"));
                            mAdapter.notifyItemInserted(studentList.size());
                        }
                        mAdapter.setLoaded();
                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);

            }
        });

        Logger.d("token is " + Utils.getToken(this));
        UsersEndpoint usersEndpoint = new UsersEndpoint(Utils.getToken(this));
        usersEndpoint.getRecent().enqueue(new retrofit2.Callback<Recent>() {
            @Override
            public void onResponse(retrofit2.Call<Recent> call, retrofit2.Response<Recent> response) {
                if (response.isSuccessful()) {
                    Recent recent = response.body();
                    List<Media> mediaList = recent.getMediaList();
                    for (int i = 0; i < mediaList.size(); i++) {
                        Images images = mediaList.get(i).getImages();

                        Image image = images.getStandardResolution();
                        //image.getUrl()

                        Logger.d(image.getUrl());
                    }

                } else {
                    Logger.e(response.toString());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Recent> call, Throwable t) {

            }
        });


//        usersEndpoint.getRecent().enqueue(new retrofit2.Callback<Profile>() {
//            @Override
//            public void onResponse(retrofit2.Call<Profile> call, retrofit2.Response<Profile> response) {
//                if (response.isSuccessful()) {
//                    Profile profile = response.body();
//                    User user = profile.getUser();
//                    Logger.d(user.getId());
//                } else {
//                    Logger.e(response.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(retrofit2.Call<Profile> call, Throwable t) {
//                Logger.e("failed");
//            }
//        });
    }


    // load initial data
    private void loadData() {
        for (int i = 1; i <= 20; i++) {
            studentList.add(new Student("Student " + i, "androidstudent" + i + "@gmail.com"));
        }
    }

    private void run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient client = new OkHttpClient();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        // Error
                        Logger.e(e.toString());
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                // For the example, you can show an error dialog or a toast
//                                // on the main UI thread
//                            }
//                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        String res = response.body().string();
                        Logger.d("res is " + res);
                        // Do something with the response
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            CookieSyncManager.createInstance(this);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            Utils.setToken(this, null);
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
