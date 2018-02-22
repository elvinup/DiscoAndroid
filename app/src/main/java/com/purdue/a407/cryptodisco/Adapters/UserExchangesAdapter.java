package com.purdue.a407.cryptodisco.Adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;
import com.purdue.a407.cryptodisco.Data.Entities.UserExchangeEntity;
import com.purdue.a407.cryptodisco.Fragments.ExchangesFragment;
import com.purdue.a407.cryptodisco.Fragments.MyExchangeFragment;
import com.purdue.a407.cryptodisco.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserExchangesAdapter extends RecyclerView.Adapter<UserExchangesAdapter.UserExchangeHolder> {

    Context context;
    List<UserExchangeEntity> exchanges;

    public UserExchangesAdapter(Context context, List<UserExchangeEntity> exchanges) {
        this.context = context;
        this.exchanges = exchanges;
    }


    @Override
    public UserExchangeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.holder_view_exchange, parent, false);
        UserExchangeHolder viewHolder = new UserExchangeHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserExchangeHolder holder, int position) {
        UserExchangeEntity exchange = exchanges.get(position);
        holder.exchangeName.setText(exchange.getName());
        holder.cardView.setOnClickListener(view -> {
            MyExchangeFragment fragment = MyExchangeFragment.newInstance(exchange.getName());
            AppCompatActivity activity = (AppCompatActivity)context;
            activity.getSupportFragmentManager().beginTransaction().
                    replace(R.id.replaceView,fragment).addToBackStack("my_exchange").commit();
        });
    }

    @Override
    public int getItemCount() {
        return exchanges.size();
    }

    public void addAll(List<UserExchangeEntity> newExchanges) {
        exchanges.clear();
        exchanges.addAll(newExchanges);
        notifyDataSetChanged();
    }

    public class UserExchangeHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cv)
        CardView cardView;

        @BindView(R.id.exchangeNameText)
        TextView exchangeName;

        public UserExchangeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
