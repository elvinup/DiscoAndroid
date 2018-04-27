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
import android.widget.Toast;

import com.purdue.a407.cryptodisco.Activities.HomeActivity;
import com.purdue.a407.cryptodisco.Adapters.ExchangesAdapter;
import com.purdue.a407.cryptodisco.Adapters.UserExchangesAdapter;
import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;
import com.purdue.a407.cryptodisco.Data.Entities.UserExchangeEntity;
import com.purdue.a407.cryptodisco.Data.Entities.exchangeVolumeEntity;
import com.purdue.a407.cryptodisco.R;
import com.purdue.a407.cryptodisco.Testing.exchangeVolume;
import com.purdue.a407.cryptodisco.Testing.exchangeVolumeCallback;
import com.purdue.a407.cryptodisco.Testing.exchangeVolumeTesting;
import com.purdue.a407.cryptodisco.ViewModels.ExchangesViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangesFragment extends Fragment {

    @Inject
    AppDatabase appDatabase;

    @Inject
    CDApi cdApi;

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

    List<ExchangeEntity> test;

    exchangeVolume test2 = new exchangeVolume();

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
        eAdapter = new ExchangesAdapter(getActivity(), new ArrayList<>());
        if (isAccount)
        {
            title.setText("My Exchanges");
            exchangesAdapter = new UserExchangesAdapter(getActivity(), new ArrayList<>());
            exchangesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            exchangesRecycler.setAdapter(exchangesAdapter);

            appDatabase.userExchangeDao().userExchanges().observe(getActivity(), exchangeEntities ->
                    exchangesAdapter.addAll(exchangeEntities));
            //Remove search bar
            //searchText.setVisibility(View.GONE);
            container.removeView(container.findViewById(R.id.searchText));


        }
        //Otherwise we are in the exchanges tab
        else
        {
            title.setText("Exchanges");
            eAdapter = new ExchangesAdapter(getActivity(), new exchangeVolume());
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

        //Sort button clicked
        exchangeSortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_sort, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        //Toast.makeText(context, "" + item.getTitle(), Toast.LENGTH_SHORT).show();
                        if (item.getTitle().equals("Sort by Volume (Popularity)"))
                        {
                            Log.d("Check Menu", "Sort by Volume was pressed");
                            //List<ExchangeEntity> ents = appDatabase.exchangeDao().exchangesNotLive();

                            cdApi.getVolumes().enqueue(new Callback<List<exchangeVolumeEntity>>() {
                                @Override
                                public void onResponse(Call<List<exchangeVolumeEntity>> call, Response<List<exchangeVolumeEntity>> response) {
                                    if(response.code() != 200) {
                                        // Error
                                        Log.d("Volume Error Result", String.valueOf(response.code()));
                                    }
                                    else {
                                        Log.d("Size of volume list", Integer.toString(response.body().size()));

                                        List<ExchangeEntity> exchangeList = appDatabase.exchangeDao().exchangesNotLive();
                                        exchangeVolume eV = new exchangeVolume();

                                        for(exchangeVolumeEntity exch: response.body()) {
                                            //exchangeVolume entry = new exchangeVolume();

                                            for (ExchangeEntity i: exchangeList) {
                                                if (exch.getExchange().equals(i.getName()))
                                                {
                                                    if (!eV.retExchangeList.contains(i)) {
                                                        eV.retExchangeList.add(i);
                                                        Log.d("Price", exch.getPrice());
                                                        eV.prices.add(exch.getPrice());
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        eAdapter.addAll(eV.retExchangeList);
                                        eAdapter.addPrices(eV.prices);
                                        Log.d("PricesSize2", Integer.toString(eV.retExchangeList.size()));
                                        Log.d("PricesSize2", Integer.toString(eV.prices.size()));
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<exchangeVolumeEntity>> call, Throwable t) {

                                }
                            });

                            eAdapter.clear();
                            //Log.d("Size of ents " ,Integer.toString(ents.size()));
                            eAdapter.addAll(test2.retExchangeList);
                            eAdapter.addPrices(test2.prices);
                            //exchangeVolumeTesting v = new exchangeVolumeTesting();
                            //List<exchangeVolumeTesting> a = v.getExchangesByVolume(ents);
                            //System.out.println(a);

                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        new AsyncTask<Void, Void, List<ExchangeEntity>>() {
            @Override
            protected List<ExchangeEntity> doInBackground(Void... params) {
                /*
                exchangeVolumeTesting v = new exchangeVolumeTesting();
                exchangeVolumeCallback evc = new exchangeVolumeCallback() {
                    @Override
                    public void callback(exchangeVolume eV) {
                        eAdapter.addAll(eV.retExchangeList);
                        eAdapter.addPrices(eV.prices);
                    }
                };
                v.getExchangesbyVolume(appDatabase.exchangeDao()
                        .exchangesNotLive(), cdApi, evc);
                */

                //System.out.println(test);

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
