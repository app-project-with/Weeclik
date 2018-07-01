package com.grace.weeclik.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.grace.weeclik.DetailsActivity;
import com.grace.weeclik.OnLoadMoreListener;
import com.grace.weeclik.R;
import com.grace.weeclik.model.Commerce;

import java.util.ArrayList;
import java.util.List;

/**
 * com.grace.weeclik.adapter
 * Created by grace on 06/05/2018.
 */
public class CommerceAdapter extends RecyclerView.Adapter {

    private final int ITEM = 0;
    private final int LOADING = 1;

    private Context context;
    private List<Commerce> commerces;

    // Le nombre min d'elts a avoir en dessous de la position de defilement en cours
    // avant de charger plus
    private int visible_item = 8;
    private int last_visible_item, total_item_count;
    private boolean isLoading = false;

    private OnLoadMoreListener onLoadMoreListener;

    public CommerceAdapter(Context context) {
        this.context = context;
        this.commerces = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return commerces.get(position) != null ? ITEM : LOADING;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater lInflater = LayoutInflater.from(parent.getContext());
        View view;

        switch (viewType) {
            case ITEM:
                view = lInflater.inflate(R.layout.item_main, parent, false);
                viewHolder = new ViewHolder(view);
                break;
            case LOADING:
                view = lInflater.inflate(R.layout.progressbar, parent, false);
                viewHolder = new ProgressViewHolder(view);
                break;
        }
        /*if (viewType == ITEM) {
            View view = lInflater.inflate(R.layout.item_main, parent, false);
            viewHolder = new ViewHolder(view);
        } else {
            View view = lInflater.inflate(R.layout.progressbar, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }*/
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final Commerce commerce = commerces.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                ((ViewHolder) holder).name.setText(commerce.getName());
                Glide
                        .with(context)
                        .load(commerce.getThumbnail())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                ((ViewHolder) holder).progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                ((ViewHolder) holder).progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(((ViewHolder) holder).thumbnail);
                break;
            case LOADING:
                // Do nothing
                break;
        }
/*
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).name.setText(commerce.getName());
            Glide.with(context).load(commerce.getThumbnail()).into(((ViewHolder) holder).thumbnail);
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
        */
    }

    /*@Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Commerce commerce = commerces.get(position);
        holder.name.setText(commerce.getName());
        Glide.with(context).load(commerce.getThumbnail()).into(holder.thumbnail);
    }*/

    @Override
    public int getItemCount() {
        return commerces == null ? 0 : commerces.size();
    }

    public void setLoading() {
        isLoading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    /* -------------------------------------- HELPERS -------------------------------------- */

    public void add(Commerce commerce) {
        commerces.add(commerce);
        notifyItemInserted(commerces.size() - 1);
    }

    public void addAll(List<Commerce> commerces) {
        for (Commerce commerce : commerces) {
            add(commerce);
        }
    }

    public void remove(Commerce commerce) {
        int position = commerces.indexOf(commerce);
        if (position > 1) {
            commerces.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoading = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoading = true;
        add(new Commerce());
    }

    public void removeLoadingFooter() {
        isLoading = false;

        int position = commerces.size() - 1;
        Commerce commerce = getItem(position);

        if (commerce != null) {
            commerces.remove(position);
            notifyItemRemoved(position);
        }
    }

    private Commerce getItem(int position) {
        return commerces.get(position);
    }


    /* --------------------------------- VIEW HOLDERS CLASS --------------------------------- */

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView thumbnail;
        ProgressBar progressBar;

        ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.item_main_text_view_name);
            thumbnail = itemView.findViewById(R.id.item_main_image_view);
            progressBar = itemView.findViewById(R.id.item_main_progress_bar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("NAME_TRADE", commerces.get(getLayoutPosition()).getName());
                    intent.putExtra("IMAGE_TRADE", commerces.get(getLayoutPosition()).getThumbnail());
                    intent.putExtra("NBSHARE_TRADE", commerces.get(getLayoutPosition()).getNbShare());
                    context.startActivity(intent);
                }
            });
        }
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
