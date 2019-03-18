package com.example.bookstoremarina.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookstoremarina.R;
import com.example.bookstoremarina.interfaceBook.AdapterCallback;
import com.example.bookstoremarina.models.Item;
import com.example.bookstoremarina.models.Favorites;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookAdapterViewHolder> {

    private List<Item> items;
    private List<Favorites> favorites;
    private Context context;
    private AdapterCallback adapterCallback;


    public BookAdapter(List<Item> items, List<Favorites> favorites, Context context, AdapterCallback adapterCallback) {
        this.items = items;
        this.favorites = favorites;
        this.context = context;
        this.adapterCallback = adapterCallback;
    }


    @Override
    public BookAdapter.BookAdapterViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list,viewGroup, false);
        BookAdapter.BookAdapterViewHolder bookAdapterViewHolder = new BookAdapter.BookAdapterViewHolder(view);
        return bookAdapterViewHolder;
    }


    @Override
    public void onBindViewHolder( BookAdapterViewHolder bookAdapterViewHolder, final int i) {
        if(items != null){
            Item item = items.get(i);
            bookAdapterViewHolder.tvTitle.setText(item.getVolumeInfo().getTitle());
            bookAdapterViewHolder.tvPublishedDate.setText(item.getVolumeInfo().getPublishedDate());

            if(item.getVolumeInfo().getImageLinks()!=null){
                Picasso.get()
                        .load(item.getVolumeInfo().getImageLinks().getSmallThumbnail())
                        .into(bookAdapterViewHolder.ivImageLinks);
            }
            bookAdapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterCallback.selectBookOnclick(items.get(i).getId());
                }
            });
        }else {
            final Favorites favorite = favorites.get(i);
            bookAdapterViewHolder.tvTitle.setText(favorite.getTvTitle());
            bookAdapterViewHolder.tvPublishedDate.setText(favorite.getTvPublishedDate());
            if(favorite.getIvImageLinks()!=null){
                Picasso.get()
                        .load(favorite.getIvImageLinks())
                        .into(bookAdapterViewHolder.ivImageLinks);
            }
            bookAdapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterCallback.selectBookOnclick(favorite.getId());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(items==null){
            return favorites.size();
        }else{
            return items.size();
        }

    }


    public class BookAdapterViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivImageLinks;
        private TextView tvTitle;
        private TextView tvPublishedDate;

        public BookAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImageLinks  = itemView.findViewById(R.id.iv_imageLinks);
            tvTitle  = itemView.findViewById(R.id.tv_title);
            tvPublishedDate  = itemView.findViewById(R.id.tv_publishedDate);
        }
    }
}
