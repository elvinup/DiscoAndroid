package com.purdue.a407.cryptodisco.Testing;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


//This tests getting the exchange volumes in order. Everything is in exchanges

public class exchangeVolumeTesting {

    String exchange;
    double volume;

    static ArrayList<exchangeVolumeTesting> exchanges = new ArrayList<exchangeVolumeTesting>();

    @Override
    public String toString() {
        return "Exchange: " + this.exchange + '\n'
                + "Volume: " + this.volume + '\n';
    }

    public static void main(String[] args) {
        try {
            URL url = new URL("https://coinmarketcap.com/exchanges/volume/24-hour/");
            Document doc = Jsoup.parse(url, 3000);
            System.out.println(doc.title());

            //Grab the table
            Element table = doc.select("table[class=table table-condensed]").get(0);
            //This is a list of all the rows in that table
            Elements rows = table.select("tr");

            //Create exchange Volume object
            exchangeVolumeTesting entry = new exchangeVolumeTesting();
            for (Element row : rows) {
                //If there's no id, check to see if it is the total US$ row
                if (row.attr("id").length() != 0)
                {
                    entry.exchange = row.attr("id");
                    continue;
                }
                //Checks if is on row with "Total"
                else if (row.child(0).text().equals("Total"))
                {
                    //gets the 2nd index
                    String vol = row.child(1).attr("data-usd");

                    //edge case
                    if (vol.equals("?"))
                        continue;

                    entry.volume = Double.valueOf(row.child(1).attr("data-usd"));
                    exchanges.add(entry);
                    //Reset entry
                    entry = new exchangeVolumeTesting();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(exchanges.size());
        //Prints an exchange
        for (exchangeVolumeTesting entr: exchanges) {
            System.out.println(entr);
        }

    }
    
}
