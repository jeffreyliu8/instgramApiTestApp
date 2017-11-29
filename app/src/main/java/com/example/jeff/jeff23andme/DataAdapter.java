package com.example.jeff.jeff23andme;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jeff on 11/22/17.
 */

public class DataAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<ImageLike> imageLikeList;
    private Context mContext;

    // The minimum amount of items to have below your current scroll position
// before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private RecyclerViewClickListener mListener;

    public void updateLiked(int position, boolean isLiked) {
        imageLikeList.get(position).setLiked(isLiked);
        notifyItemChanged(position);
    }

    public DataAdapter(List<ImageLike> imageLikes, RecyclerView recyclerView, Context context, RecyclerViewClickListener listener) {
        imageLikeList = imageLikes;
        this.mContext = context;
        this.mListener = listener;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore(imageLikeList.get(imageLikeList.size() - 1).getMediaID());
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return imageLikeList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item, parent, false);

            vh = new ImageLikeViewHolder(v, mListener);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageLikeViewHolder) {
            ImageLike singleImageLike = imageLikeList.get(position);
            Picasso.with(mContext).load(singleImageLike.getUrl()).into(((ImageLikeViewHolder) holder).tvImage);
            ((ImageLikeViewHolder) holder).tvIsLike.setText(singleImageLike.isLiked() ? "liked" : "not liked");
            ((ImageLikeViewHolder) holder).imageLike = singleImageLike;
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return imageLikeList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class ImageLikeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView tvImage;
        private TextView tvIsLike;
        private ImageLike imageLike;
        private RecyclerViewClickListener mListener;

        public ImageLikeViewHolder(View v, RecyclerViewClickListener listener) {
            super(v);
            mListener = listener;
            v.setOnClickListener(this);
            tvImage = v.findViewById(R.id.image);

            tvIsLike = v.findViewById(R.id.liked);
        }

        @Override
        public void onClick(View view) {
            Logger.d("cliked");
            mListener.onClick(view, getAdapterPosition(), imageLike);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar1);
        }
    }

    public interface RecyclerViewClickListener {

        void onClick(View view, int position, ImageLike imageLike);
    }
}