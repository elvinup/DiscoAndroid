package com.purdue.a407.cryptodisco.Testing;

import android.os.AsyncTask;
import android.util.Log;

import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;

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


//This tests getting the exchange volumes in order. Everything is in exchanges

public class exchangeVolumeTesting{


    static ArrayList<exchangeVolume> exchanges = new ArrayList<exchangeVolume>();



    public List<ExchangeEntity> getExchangesbyVolume(List<ExchangeEntity> exchangeList) {
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
        for (exchangeVolume entr: exchanges) {
            System.out.println(entr.exchange);

        }

        ArrayList<ExchangeEntity> retExchangeList = new ArrayList<>();
        for (exchangeVolume entr: exchanges) {
            for (ExchangeEntity i: exchangeList) {
                if ((entr.exchange).equals(i.getName()))
                {
                    if (!retExchangeList.contains(i))
                        retExchangeList.add(i);
                }
            }
        }

        for (ExchangeEntity entr: retExchangeList) {
            System.out.println(entr.getName());
        }

        return retExchangeList;

    }


    /*
    public static void main(String[] args) {
        exchangeVolumeTesting v = new exchangeVolumeTesting();
        v.getExchangesByVolume();
    }
    */
}
