package com.example.jeff.jeff23andme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.TextView;


import com.example.jeff.jeff23andme.endpoints.LikesEndpoint;

import com.example.jeff.jeff23andme.endpoints.UsersEndpoint;
import com.example.jeff.jeff23andme.model.DeleteLikeResponse;
import com.example.jeff.jeff23andme.model.Image;
import com.example.jeff.jeff23andme.model.Images;
import com.example.jeff.jeff23andme.model.LikeResponse;

import com.example.jeff.jeff23andme.model.Media;


import com.example.jeff.jeff23andme.model.Recent;

import com.orhanobut.logger.Logger;


import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {


    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    private DataAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private List<ImageLike> imageLikeList;


    protected Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvEmptyView = findViewById(R.id.empty_view);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        imageLikeList = new ArrayList<>();
        handler = new Handler();

        loadData();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);

        // create an Object for Adapter
        mAdapter = new DataAdapter(imageLikeList, mRecyclerView, this, new DataAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, final int position, ImageLike imageLike) {
                Logger.d("id is " + imageLike.getMediaID());
                LikesEndpoint likesEndpoint = new LikesEndpoint(Utils.getToken(MainActivity.this));
                if (imageLike.isLiked()) {
                    likesEndpoint.unlike(imageLike.getMediaID()).enqueue(new retrofit2.Callback<DeleteLikeResponse>() {
                        @Override
                        public void onResponse(retrofit2.Call<DeleteLikeResponse> call, retrofit2.Response<DeleteLikeResponse> response) {
                            if (response.isSuccessful()) {
                                Logger.d("unliked");
                                mAdapter.updateLiked(position, false);
                            } else {
                                Logger.e("hum, unliked failed " + response.toString());
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<DeleteLikeResponse> call, Throwable t) {
                            Logger.e("unliked failed " + call.toString());
                        }
                    });
                } else {
                    likesEndpoint.like(imageLike.getMediaID()).enqueue(new retrofit2.Callback<LikeResponse>() {
                        @Override
                        public void onResponse(retrofit2.Call<LikeResponse> call, retrofit2.Response<LikeResponse> response) {
                            if (response.isSuccessful()) {
                                Logger.d("liked");
                                mAdapter.updateLiked(position, true);
                            } else {
                                Logger.e("hum, liked failed " + response.raw().toString());
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<LikeResponse> call, Throwable t) {
                            Logger.e("liked failed " + call.toString());
                        }
                    });
                }
            }
        });

        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);
        //  mAdapter.notifyDataSetChanged();


//        if (imageLikeList.isEmpty()) {
//            mRecyclerView.setVisibility(View.GONE);
//            tvEmptyView.setVisibility(View.VISIBLE);
//        } else {
//            mRecyclerView.setVisibility(View.VISIBLE);
//            tvEmptyView.setVisibility(View.GONE);
//        }

//        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                //add null , so the adapter will check view_type and show progress bar at bottom
//                imageLikeList.add(null);
//                mAdapter.notifyItemInserted(imageLikeList.size() - 1);
//
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //   remove progress item
//                        imageLikeList.remove(imageLikeList.size() - 1);
//                        mAdapter.notifyItemRemoved(imageLikeList.size());
//                        //add items one by one
//                        int start = imageLikeList.size();
//                        int end = start + 20;
//
//                        for (int i = start + 1; i <= end; i++) {
//                            imageLikeList.add(new ImageLike("https://images.google.com/images/branding/googleg/1x/googleg_standard_color_128dp.png", false));
//                            mAdapter.notifyItemInserted(imageLikeList.size());
//                        }
//                        mAdapter.setLoaded();
//                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
//                    }
//                }, 2000);
//
//            }
//        });


    }


    // load initial data
    private void loadData() {
        UsersEndpoint usersEndpoint = new UsersEndpoint(Utils.getToken(this));
        usersEndpoint.getRecent().enqueue(new retrofit2.Callback<Recent>() {
            @Override
            public void onResponse(retrofit2.Call<Recent> call, retrofit2.Response<Recent> response) {
                if (response.isSuccessful()) {
                    Recent recent = response.body();
                    List<Media> mediaList = recent.getMediaList();
                    for (int i = 0; i < mediaList.size(); i++) {
                        String id = mediaList.get(i).getId();
                        Images images = mediaList.get(i).getImages();
                        Image image = images.getStandardResolution();
                        imageLikeList.add(new ImageLike(id, image.getUrl(), mediaList.get(i).userHasLiked()));
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    Logger.e(response.toString());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Recent> call, Throwable t) {
                Logger.e("load recent failed");
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
