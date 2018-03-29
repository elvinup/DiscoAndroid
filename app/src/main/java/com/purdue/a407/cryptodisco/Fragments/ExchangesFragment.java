package com.purdue.a407.cryptodisco.Fragments;


import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.purdue.a407.cryptodisco.Adapters.ExchangesAdapter;
import com.purdue.a407.cryptodisco.Adapters.UserExchangesAdapter;
import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;
import com.purdue.a407.cryptodisco.Data.Entities.UserExchangeEntity;
import com.purdue.a407.cryptodisco.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangesFragment extends Fragment {

    @Inject
    AppDatabase appDatabase;

    @BindView(R.id.searchText)
    AppCompatAutoCompleteTextView searchText;

    @BindView(R.id.myExchangeRecycler)
    RecyclerView exchangesRecycler;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.exchange_sort)
    Button exchangeSortButton;

    UserExchangesAdapter exchangesAdapter;

    ExchangesAdapter eAdapter;

    List<ExchangeEntity> searchEntities = new ArrayList<>();
    List<String> searchesByExchange = new ArrayList<>();

    ArrayAdapter<Object> arrayAdapter;
    Context context;


    public ExchangesFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Check if Account tab was selected
        Bundle args = getArguments();
        boolean isAccount = args.getBoolean("isAccount");
        //Log.d("ISACCOUNT: ", Boolean.toString(isAccount));

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_exchanges, container, false);
        ButterKnife.bind(this, view);
        ((App) getActivity().getApplication()).getNetComponent().inject(this);
        context = getActivity();

        if (isAccount)
        {
            title.setText("My Exchanges");
            exchangesAdapter = new UserExchangesAdapter(getActivity(), new ArrayList<>());
            exchangesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            exchangesRecycler.setAdapter(exchangesAdapter);

            appDatabase.userExchangeDao().userExchanges().observe(getActivity(), exchangeEntities ->
                    exchangesAdapter.addAll(exchangeEntities));
            //Remove search bar
            searchText.setVisibility(View.GONE);

        }
        //Otherwise we are in the exchanges tab
        else
        {
            title.setText("Exchanges");
            eAdapter = new ExchangesAdapter(getActivity(), new ArrayList<>());
            exchangesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            exchangesRecycler.setAdapter(eAdapter);
            appDatabase.exchangeDao().exchanges().observe(getActivity(), exchangeEntities ->
                    eAdapter.addAll(exchangeEntities));
        }


        searchText.setOnItemClickListener((adapterView, view12, i, l) -> {
            View view1 = getActivity().getCurrentFocus();
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            searchText.clearComposingText();
            String exch = searchText.getText().toString();
            searchText.setText("");
            ExchangeFragment fragment = ExchangeFragment.newInstance(exch);
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.replaceView,fragment).addToBackStack("exchange").commit();
        });

        // sort button clicked
        exchangeSortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("HELLO", "hi");
                //searchText.setText("test");
            }
        });

        new AsyncTask<Void, Void, List<ExchangeEntity>>() {
            @Override
            protected List<ExchangeEntity> doInBackground(Void... params) {
                return appDatabase.exchangeDao()
                        .exchangesNotLive();
            }
            @Override
            protected void onPostExecute(List<ExchangeEntity> exchangesBySearch) {
                ArrayList<String> strings = new ArrayList<>();
                for(ExchangeEntity entity: exchangesBySearch) {
                    strings.add(entity.getName());
                }
                arrayAdapter = new ArrayAdapter<>(context,
                        android.R.layout.simple_dropdown_item_1line,strings.toArray());
                searchText.setAdapter(arrayAdapter);
            }
        }.execute();

        return view;
    }

}
