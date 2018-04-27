package com.purdue.a407.cryptodisco.Testing;

import android.os.AsyncTask;
import android.util.Log;

import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;
import com.purdue.a407.cryptodisco.Data.Entities.exchangeVolumeEntity;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


//This tests getting the exchange volumes in order. Everything is in exchanges

public class exchangeVolumeTesting{


    static ArrayList<exchangeVolume> exchanges = new ArrayList<exchangeVolume>();
    //ArrayList<ExchangeEntity> retExchangeList = new ArrayList<>();
    exchangeVolume eV = new exchangeVolume();


    public void getExchangesbyVolume(List<ExchangeEntity> exchangeList, CDApi cdApi, exchangeVolumeCallback evc) {

        cdApi.getVolumes().enqueue(new Callback<List<exchangeVolumeEntity>>() {
            @Override
            public void onResponse(Call<List<exchangeVolumeEntity>> call, Response<List<exchangeVolumeEntity>> response) {
                if(response.code() != 200) {
                    // Error
                    Log.d("Volume Error Result", String.valueOf(response.code()));
                }
                else {
                    Log.d("Size of volume list", Integer.toString(response.body().size()));



                    for(exchangeVolumeEntity exch: response.body()) {
                        //exchangeVolume entry = new exchangeVolume();

                            for (ExchangeEntity i: exchangeList) {
                                if (exch.getExchange().equals(i.getName()))
                                {
                                    //exch.getPrice();
                                    //entry.exchange = exch.getExchange() + "\t" + exch.price;
                                    //entry.exchange = exch.getExchange();
                                    //entry.volume = Double.parseDouble(exch.getPrice().substring(1).replace(",", ""));
                                    if (!eV.retExchangeList.contains(i)) {
                                        eV.retExchangeList.add(i);
                                        eV.prices.add(exch.getPrice());
                                        break;
                                    }
                                }
                            }

                        //entry.exchange = exch.getExchange() + "\t" + exch.price;
                        //entry.volume = Double.parseDouble(exch.getPrice().substring(1).replace(",", ""));
                    }
                    //evc.callback(eV);
                    Log.d("PricesSize2", Integer.toString(eV.prices.size()));
                }
            }

            @Override
            public void onFailure(Call<List<exchangeVolumeEntity>> call, Throwable t) {

            }
        });

        /*
        boolean skip = false;

        exchangeVolume entry = new exchangeVolume();

        try {
            URL url = new URL("https://coinmarketcap.com/exchanges/volume/24-hour/");
            Document doc = Jsoup.parse(url, 6000);

            //Grab the table
            Element table = doc.select("table[class=table table-condensed]").get(0);
            //This is a list of all the rows in that table
            Elements rows = table.select("tr");

            //Create exchange Volume object
            outerloop:
            for (Element row : rows) {
                //System.out.println("Skip value " + Boolean.toString(skip));
                //If there's no id, check to see if it is the total US$ row
                if (row.attr("id").length() != 0)
                {
                    String name = row.attr("id");

                    //Use this for finding names of exchanges
                    //Log.d("OUR EXCHANGE ", name);
                    for(ExchangeEntity e : exchangeList) {
                        //System.out.println("Exchange NAME: " + e.getName());
                        //System.out.println("NAME: " + name);

                        if (e.getName().equals(name.toLowerCase())) {

                            entry.exchange = row.attr("id");
                            skip = false;
                            continue outerloop;
                        }
                    }

                    skip = true;

                }

                //Checks if is on row with "Total"
                else if (row.child(0).text().equals("Total") && !skip)
                {
                    //gets the 2nd index
                    String vol = row.child(1).attr("data-usd");

                    //edge case
                    if (vol.equals("?"))
                        continue;

                    entry.volume = Double.valueOf(row.child(1).attr("data-usd"));
                    if (!exchanges.contains(entry))
                        exchanges.add(entry);
                    //Reset entry
                    entry = new exchangeVolume();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        //System.out.println(exchanges.size());
        //Prints an exchange
        /*
        for (exchangeVolume entr: exchanges) {
            System.out.println(entr.exchange);

        }
        */

        /*
        //This sorts
        for (exchangeVolume entr: exchanges) {
            for (ExchangeEntity i: exchangeList) {
                if ((entr.exchange).equals(i.getName()))
                {
                    if (!retExchangeList.contains(i))
                        retExchangeList.add(i);
                }
            }
        }
        */

        /*
        for (ExchangeEntity entr: retExchangeList) {
            System.out.println(entr.getName());
        }
        */
        Log.d("PricesSize", Integer.toString(eV.prices.size()));

    }


    /*
    public static void main(String[] args) {
        exchangeVolumeTesting v = new exchangeVolumeTesting();
        v.getExchangesByVolume();
    }
    */
}
