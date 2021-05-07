package crawl;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.NumberFormat;
import java.text.ParseException;

//Joshua Lanctot
//This class takes in a sentence to be searched on different retailers websites using Jsoup.
//The prices are returned for each website, showing the lowest price available.
public class PriceScraper {
    private String sentenceToBeSearched;
    private String amazonURL;
    private String bestBuyURL;
    private String walmartURL;
    private String targetURL;
    private String amazonTotal;
    private String bestBuyTotal;
    private String walmartTotal;
    private String targetTotal;

    PriceScraper(String sentenceToBeSearched){
        this.sentenceToBeSearched = sentenceToBeSearched;
    }

    //set the new URLs with the sentence to be searched
    public void setURLS() {
        StringBuilder newURL = new StringBuilder(sentenceToBeSearched);
        //if there is more than one word in the sentence, replace the spaces with +
        for (int i=0; i<newURL.length(); i++){
            if (newURL.charAt(i) == ' ')
                newURL.setCharAt(i, '+');
        }

        amazonURL = "https://www.amazon.com/s?k=" + newURL;
        bestBuyURL = "https://www.bestbuy.com/site/searchpage.jsp?st=" + newURL;
        walmartURL = "https://www.walmart.com/search/?query=" + newURL;
        targetURL = "https://www.target.com/s?searchTerm=" + newURL;
    }

    //connect to URLs and scrape the prices
    public void scrapeAmazon(){
        String amazonPrice = null;
        String amazonCents = null;
        String total = null;

        try{
            Document amazonDoc = Jsoup.connect(amazonURL).get();         //connect to URLs and get the first price from the html to be printed

            for (Element line: amazonDoc.select("div.s-desktop-width-max.s-opposite-dir")){
                amazonPrice = amazonDoc.select("span.a-price-whole").first().text();
                amazonCents = amazonDoc.select("span.a-price-fraction").first().text();
            }
            total = amazonPrice + amazonCents;

        } catch(Exception e){
            System.out.println("An error occurred, Amazon could not search for this item.");
        }
        amazonTotal = total;
    }

    //method for each website so that if an error on website occurs the others websites can still try
    public void scrapeBestBuy() throws ParseException {
        String bestBuyPrice = null;

        try {
            Document bestBuyDoc = Jsoup.connect(bestBuyURL).get();
            bestBuyPrice = bestBuyDoc.select("span.aria-hidden=\"true\"").first().text();

        } catch (Exception e){
            System.out.println("An error occurred, BestBuy could not search for this item.");
        }
        bestBuyTotal = bestBuyPrice;
    }

    public void scrapeWalmart(){
        String walmartPrice = null;

        try {
            Document walmartDoc = Jsoup.connect(walmartURL).get();

        } catch (Exception e){
            System.out.println("An error occurred, BestBuy could not search for this item.");
        }
    }

    public void scrapeTarget(){
        String targetPrice = null;

        try {
            Document targetDoc = Jsoup.connect(targetURL).get();

        } catch (Exception e){
            System.out.println("An error occurred, Target could not search for this item.");
        }
    }

    public void printPrices(){
        System.out.println("\nHere are the prices for " + sentenceToBeSearched + ":");
        System.out.println("Amazon: " + amazonTotal);
        System.out.println("BesBuy: " + bestBuyTotal);
    }


}
