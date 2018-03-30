package com.purdue.a407.cryptodisco.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.purdue.a407.cryptodisco.Adapters.NotificationsAdapter;
import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Helpers.DeviceID;
import com.purdue.a407.cryptodisco.R;
import com.purdue.a407.cryptodisco.ViewModels.NotificationsViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsFragment extends Fragment {

    @Inject
    DeviceID deviceID;

    @Inject
    AppDatabase appDatabase;

    NotificationsViewModel viewModel;

    @BindView(R.id.notificationsRecycler)
    RecyclerView recyclerView;

    NotificationsAdapter notificationsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container,false);
        ButterKnife.bind(this, view);
        ((App) getActivity().getApplication()).getNetComponent().inject(this);
        viewModel = new NotificationsViewModel(getActivity().getApplication(),
                appDatabase);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        notificationsAdapter = new NotificationsAdapter(getActivity(), new ArrayList<>());
        recyclerView.setAdapter(notificationsAdapter);
        viewModel.getNotifications().observe(getActivity(), notificationsEntities ->
                notificationsAdapter.addAll(notificationsEntities));
        return view;
    }
}
