package com.example.bookstoremarina.activity;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import com.example.bookstoremarina.R;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;

import com.example.bookstoremarina.fragment.BookListFragment;
import com.example.bookstoremarina.fragment.DetailFragment;
import com.example.bookstoremarina.fragment.FavoriteListFragment;
import com.example.bookstoremarina.interfaceBook.AdapterCallback;
import com.example.bookstoremarina.R;


public class MainActivity extends AppCompatActivity implements BookListFragment.GetItemCallback , FavoriteListFragment.GetFavoriteCallback {

    public EditText search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        BookListFragment itemListFragment = new BookListFragment();

        itemListFragment.connect(MainActivity.this);

        fragmentTransaction.add(R.id.container, itemListFragment,"list");
        fragmentTransaction.commit();
    }


    @Override
    public void getItem(String id) {
        DetailFragment detailFragment = new DetailFragment();

        Bundle args = new Bundle();
        args.putString("ID",id);

        detailFragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.container,detailFragment,"DETAIL");
        fragmentTransaction.addToBackStack("list");
        fragmentTransaction.commit();
    }
}


