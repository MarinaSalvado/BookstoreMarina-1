package com.example.bookstoremarina.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import com.example.bookstoremarina.R;
import com.example.bookstoremarina.interfaceBook.GetItemsService;
import com.example.bookstoremarina.models.Item;
import com.example.bookstoremarina.models.Favorites;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private ImageView ivImage;
    private TextView tvTitle;
    private TextView tvDate;
    private TextView tvAuthor;
    private TextView tvRating;
    private TextView tvDescription;
    private Button btnAddFavorite;
    private TextView tvPrice;
    private Button tvBuy;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        ivImage = view.findViewById(R.id.iv_imageLinks);
        tvTitle = view.findViewById(R.id.tv_title);
        tvDate = view.findViewById(R.id.tv_publishedDate);
        tvAuthor = view.findViewById(R.id.tv_authors);
        tvRating = view.findViewById(R.id.tv_rating);
        tvDescription = view.findViewById(R.id.tv_description);
        tvPrice = view.findViewById(R.id.tv_price);
        btnAddFavorite = view.findViewById(R.id.btn_addfavorite);
        tvBuy = view.findViewById(R.id.btn_tvbuy);


        Realm.init(getActivity());

        Bundle args = this.getArguments();

        getItemDetail(args.getString("ID"));
        return view;
    }

    public void prepareView(final Item item) {
        tvTitle.setText(item.getVolumeInfo().getTitle());
        if (item.getVolumeInfo().getPublishedDate() != null) {
            tvDate.setText(item.getVolumeInfo().getPublishedDate());
        } else {
            tvDate.setVisibility(View.GONE);
        }
        tvAuthor.setText(item.getVolumeInfo().getAuthors().toString());
        tvDescription.setText(item.getVolumeInfo().getDescription());
        if (item.getSaleInfo().getListPrice() == null) {
            tvPrice.setText("");
            tvPrice.setVisibility(View.GONE);
        } else {
            tvPrice.setText("Preço: " + String.valueOf(item.getSaleInfo().getListPrice().getAmount()) + item.getSaleInfo().getListPrice().getCurrencyCode());
        }
        if (item.getVolumeInfo().getAverageRating() != null) {
            tvRating.setText("Rating: " + String.valueOf(item.getVolumeInfo().getAverageRating()));
        } else {
            tvRating.setVisibility(View.GONE);
        }
        if (item.getVolumeInfo().getImageLinks() != null) {
            Picasso.get().load(item.getVolumeInfo().getImageLinks().getThumbnail()).into(ivImage);
        }
        if (check(item) == false) {
            btnAddFavorite.setText("Adicionar Favorito");
        } else {
            btnAddFavorite.setText("Retirar Favorito");
        }
        btnAddFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getDefaultInstance();
                if (check(item) == false) {
                    realm.beginTransaction();
                    Favorites favorite = realm.createObject(Favorites.class);
                    favorite.setTvTitle(item.getVolumeInfo().getTitle());
                    favorite.setTvPublishedDate(item.getVolumeInfo().getPublishedDate());
                    favorite.setId(item.getId());
                    if (item.getVolumeInfo().getImageLinks() != null) {
                        favorite.setIvImageLinks(item.getVolumeInfo().getImageLinks().getSmallThumbnail());
                    } else {
                        favorite.setIvImageLinks(null);
                    }
                    realm.commitTransaction();
                    btnAddFavorite.setText("Retirar favorito");
                } else {
                    final RealmResults<Favorites> results = realm.where(Favorites.class).equalTo("id", item.getId()).findAll();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm2) {
                            results.deleteAllFromRealm();
                        }
                    });
                    btnAddFavorite.setText("Adicionar favorito");
                }
                tvBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getSaleInfo().getBuyLink() != null) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(item.getSaleInfo().getBuyLink()));
                            startActivity(i);
                        }
                    }
                });
            }
        });
    }
    private boolean check (Item item){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Favorites> user = realm.where(Favorites.class).equalTo("id", item.getId()).findAll();

        return user.size() > 0;
    }


    private void getItemDetail (String id){
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl("https://www.googleapis.com/books/v1/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        GetItemsService service = retrofit.create(GetItemsService.class);

        Call<Item> itemCall = service.getItemDetail(String.valueOf(id));

        itemCall.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                Log.v("RETROFIT", "OK");

                prepareView(response.body());
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Log.v("RETROFIT", "NOK");
            }
        });
    }

}

