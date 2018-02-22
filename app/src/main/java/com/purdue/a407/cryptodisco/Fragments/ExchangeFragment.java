package com.purdue.a407.cryptodisco.Fragments;


import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kyleohanian.databinding.modelbindingforms.Listeners.OnBindDialogCancelListener;
import com.kyleohanian.databinding.modelbindingforms.Listeners.OnBindDialogCreateListener;
import com.kyleohanian.databinding.modelbindingforms.UIObjects.ModelForm;
import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.CacheData.CDResource;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Data.Entities.CoinPairingEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;
import com.purdue.a407.cryptodisco.Data.Entities.UserExchangeEntity;
import com.purdue.a407.cryptodisco.R;
import com.purdue.a407.cryptodisco.Repos.CoinPairingRepository;
import com.purdue.a407.cryptodisco.ViewModels.ExchangeViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeFragment extends Fragment {


    String titleString;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.addButton)
    AppCompatButton addButton;

    @BindView(R.id.searchText)
    AppCompatAutoCompleteTextView searchText;

    @Inject
    AppDatabase appDatabase;

    @Inject
    CoinPairingRepository coinPairingRepository;

    ExchangeViewModel viewModel;

    ArrayAdapter<Object> arrayAdapter;
    Context context;

    public ExchangeFragment() {
        // Required empty public constructor
    }

    public static ExchangeFragment newInstance(String title) {
        ExchangeFragment exchangeFragment = new ExchangeFragment();
        exchangeFragment.setTitle(title);
        return exchangeFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_exchange, container, false);
        ButterKnife.bind(this, view);
        ((App) getActivity().getApplication()).getNetComponent().inject(this);
        context = getActivity();
        setUpViewModel();
        return view;
    }

    public void setTitle(String titleString) {
        this.titleString = titleString;
    }

    public void setUpViewModel() {
        title.setText(titleString);
        coinPairingRepository.setExchange(titleString);
        viewModel = new ExchangeViewModel(coinPairingRepository);

        searchText.setOnItemClickListener((adapterView, view12, i, l) -> {
            View view1 = getActivity().getCurrentFocus();
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            searchText.clearFocus();
        });

        viewModel.getCoinPairings().observe(getActivity(), listCDResource -> {
            if(listCDResource.isLoading()) {
                return;
            }
            List<CoinPairingEntity> coinPairingEntities = listCDResource.getData();
            ArrayList<String> strings = new ArrayList<>();
            for(CoinPairingEntity entity: coinPairingEntities) {
                strings.add(entity.getCoin_short() + " -> " + entity.getMarket_short());
            }
            arrayAdapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_dropdown_item_1line,strings.toArray());
            searchText.setAdapter(arrayAdapter);
        });

    }

    @OnClick(R.id.addButton)
    public void onAdd() {
        ModelForm<UserExchangeEntity> form =
                new ModelForm<>(getActivity(), UserExchangeEntity.class);
        View viewForm = form.setUpCreate();
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(viewForm);
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
        form.setOnBindDialogCancelListener(view ->
                alertDialog.dismiss());

        form.setOnBindDialogCreateListener((obj, view) -> {
            UserExchangeEntity userExchangeEntity = (UserExchangeEntity)obj;
            userExchangeEntity.setName(titleString);
            appDatabase.userExchangeDao().insert(userExchangeEntity);
            alertDialog.dismiss();
        });
    }

}
