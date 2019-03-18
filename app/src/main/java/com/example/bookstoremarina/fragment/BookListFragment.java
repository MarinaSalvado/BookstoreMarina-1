package com.example.bookstoremarina.fragment;

import android.app.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.EditText;
import android.widget.Button;
import android.app.Fragment;

import com.example.bookstoremarina.R;
import com.example.bookstoremarina.activity.MainActivity;
import com.example.bookstoremarina.interfaceBook.AdapterCallback;
import com.example.bookstoremarina.adapter.BookAdapter;
import com.example.bookstoremarina.interfaceBook.GetItemsService;
import com.example.bookstoremarina.models.Item;
import com.example.bookstoremarina.models.Result;
import com.example.bookstoremarina.models.Favorites;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BookListFragment extends Fragment implements AdapterCallback {
    private RecyclerView itemRecyclerview;
    private BookAdapter bookAdapter;
    private RecyclerView.LayoutManager layoutManager;
    GetItemCallback getItemCallback;
    private String Search;
    private EditText et_search;
    private Button btn_search;
    private Button btn_favorite;
    public static final String KEY_DIC = "1";
    public static final String KEY_VALUE = "key_value";



    public void connect(Activity activity) { getItemCallback = (GetItemCallback) activity;
    }

    public BookListFragment() {
    }

    public void setSearch(String search) {
        Search = search;
    }
    public String getSearch() {
        return Search;
    }


    //public static BookListFragment newInstance(String search) {
       // BookListFragment bookListFragment = new BookListFragment();
    //}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_book_list, container, false);
            itemRecyclerview = (RecyclerView) view.findViewById(R.id.my_recycler);
            itemRecyclerview.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(getActivity());
            itemRecyclerview.setLayoutManager(layoutManager);
            Realm.init(getActivity());
            if(getSearch()!=null){
                getItems();
            }
            et_search = view.findViewById(R.id.et_search);
            btn_search = view.findViewById(R.id.btn_search);
            btn_favorite = view.findViewById(R.id.btn_favorite);
            btn_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(et_search.getText().toString()!=null){
                        setSearch(et_search.getText().toString());
                        getItems();

                        SharedPreferences msg           = getActivity().getSharedPreferences(KEY_DIC,0);
                        SharedPreferences.Editor editor = msg.edit();
                        editor.putString(KEY_VALUE,et_search.getText().toString());
                        editor.commit();
                    }
                }
            });

            btn_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FavoriteListFragment favoriteListFragment = new FavoriteListFragment();
                    favoriteListFragment.connect(getActivity());
                    fragmentTransaction.replace(R.id.container, favoriteListFragment,"list");
                    fragmentTransaction.addToBackStack("list");
                    fragmentTransaction.commit();
                }
            });
            SharedPreferences valorGuardado = getActivity().getSharedPreferences(KEY_DIC,0);
            String valor  = valorGuardado.getString(KEY_VALUE,"");
            et_search.setText(valor);
            setSearch(et_search.getText().toString()); //search
            if(et_search.getText().toString()!=null){
                this.setSearch(et_search.getText().toString());
                getItems();
            }
            return view;
        }


        private void getItems() {

            Retrofit retrofit = new Retrofit
                    .Builder()
                    .baseUrl("https://www.googleapis.com/books/v1/")
                    .addConverterFactory(GsonConverterFactory.create()).build();

            GetItemsService service = retrofit.create(GetItemsService.class);

            Call<Result> itemCall = service.getItems(this.getSearch());

            itemCall.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    Log.v("RETROFIT","OK");

                    if(response.isSuccessful()){
                        bookAdapter = new BookAdapter(response.body().getItems(),null,getActivity(), BookListFragment.this);
                        itemRecyclerview.setAdapter(bookAdapter);
                    }
                }
                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    Log.v("RETROFIT","NOK");
                }
            });
        }

        public interface GetItemCallback {
            void getItem(String id);
        }

        @Override
        public void selectBookOnclick(String id) {
            getItemCallback.getItem(id);
        }
    }
