package crawl;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

//Joshua Lanctot
//This class takes in a sentence to be searched on different retailers websites using Jsoup.
//The prices are returned for each website, showing the lowest price available.
public class PriceScraper {
    private HashMap<String, Double > amazonMap = new HashMap<>();
    private HashMap<String, Double > bestBuyMap = new HashMap<>();
    private HashMap<String, Double > neweggMap = new HashMap<>();
    private int numOfItems = 5;
    private final String agent =
            "Mozilla/5.0";
    private String sentenceToBeSearched;
    private String amazonURL;
    private String bestBuyURL;
    private String neweggURL;

    private String amazonName;
    private String bestBuyName;
    private String neweggName;

    PriceScraper(String sentenceToBeSearched){
        this.sentenceToBeSearched = sentenceToBeSearched;
    }

    //set the new URLs with the sentence to be searched
    void setURLS() {
        StringBuilder newURL = new StringBuilder(sentenceToBeSearched);
        //if there is more than one word in the sentence, replace the spaces with +
        for (int i=0; i<newURL.length(); i++){
            if (newURL.charAt(i) == ' ')
                newURL.setCharAt(i, '+');
        }

        amazonURL = "https://www.amazon.com/s?k=" + newURL;
        bestBuyURL = "https://www.bestbuy.com/site/searchpage.jsp?st=" + newURL;
        neweggURL = "https://www.newegg.com/p/pl?d=" + newURL;
    }

    //set number of items to get (default is 5)
    void setNumOfItems(int numOfItems) {
        this.numOfItems = numOfItems;
    }

    //connect to URLs and scrape the prices
    void scrapeAmazon(){
        String amazonPrice = null;
        String amazonCents = null;
        String total = null;
        int i = 0;

        try{
            //connect to URLs and get the first price from the html to be printed
            Document amazonDoc = Jsoup.connect(amazonURL)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").get();
            //loop getting names and prices until numOfItems is reached
            while (i != numOfItems){
                Elements select = amazonDoc.select("span.a-price-whole");
                amazonPrice = select.get(i).text();
                Elements select2 = amazonDoc.select("span.a-price-fraction");
                amazonCents = select2.get(i).text();
                total = amazonPrice + amazonCents;
                Elements select3 = amazonDoc.select("span.a-size-medium.a-color-base.a-text-normal");
                amazonName = select3.get(i).text();
                double newTotal = Double.parseDouble(total);
                amazonMap.put(amazonName, newTotal);
                i++;
            }

        } catch(Exception e){
            System.out.println("An error occurred, Amazon could not search for this item.");
        }
    }

    //method for each website so that if an error on website occurs the others websites can still try
    void scrapeBestBuy() {
        String bestBuyPrice = null;
        int i = 0;

        try {
            Document bestBuyDoc = Jsoup.connect(bestBuyURL).userAgent(agent).get();
            while (i != numOfItems){
                Elements select = bestBuyDoc.select("div.priceView-hero-price.priceView-customer-price");
                bestBuyPrice = select.get(i).child(0).text();
                Elements select2 = bestBuyDoc.select("h4.sku-header");
                bestBuyName = select2.get(i).text();
                String newBestBuyPrice = bestBuyPrice.substring(1);
                double newTotal = Double.parseDouble(newBestBuyPrice);
                bestBuyMap.put(bestBuyName, newTotal);
                i++;
            }

        } catch (Exception e){
            System.out.println("An error occurred, BestBuy could not search for this item.");
        }
    }

    void scrapeNewegg() {
        String neweggPrice = null;
        String neweggCents = null;
        String total = null;

        try {
            int i = 0;
            Document neweggDoc = Jsoup.connect(neweggURL).userAgent(agent).get();
            Elements div = neweggDoc.select("div.list-wrap");                 //make sure the first item is selected and not an ad
            while (i != numOfItems){
                Elements select = div.select("li.price-current > strong");
                neweggPrice = select.get(i).text();
                Elements select2 = div.select("li.price-current > sup");
                neweggCents = select2.get(i).text();
                Elements select3 = div.select("a.item-title");
                neweggName = select3.get(i).text();
                total = neweggPrice + neweggCents;
                double newTotal = Double.parseDouble(total);
                neweggMap.put(neweggName, newTotal);
                i++;
            }

        } catch (Exception e){
            System.out.println("An error occurred, Newegg could not search for this item.");
        }
    }

    //print out names and prices
    void printPrices(){
        String bestAmazonName = null;
        String bestBestBuyName = null;
        String bestNeweggName = null;
        double bestAmazonPrice = 0;
        double bestBestBuyPrice = 0;
        double bestNeweggPrice = 0;

        System.out.println("-------------RESULTS------------");
        System.out.print("Amazon Results:");
        System.out.println(" " + amazonURL);
        for (Map.Entry<String, Double> entry: amazonMap.entrySet()){
            if (bestAmazonPrice == 0){
                bestAmazonPrice = entry.getValue();
                bestAmazonName = entry.getKey();
            }
            else if (bestAmazonPrice > entry.getValue()){
                bestAmazonPrice = entry.getValue();
                bestAmazonName = entry.getKey();
            }
            System.out.println(entry.getKey() + " for $" + entry.getValue());
        }
        System.out.println("Lowest amazon price: " + bestAmazonName + " for $" + bestAmazonPrice);

        System.out.print("\nBestBuy Results:");
        System.out.println(" " + bestBuyURL);
        for (Map.Entry<String, Double> entry: bestBuyMap.entrySet()){
            if (bestBestBuyPrice == 0){
                bestBestBuyPrice = entry.getValue();
                bestBestBuyName = entry.getKey();
            }
            else if (bestBestBuyPrice > entry.getValue()){
                bestBestBuyPrice = entry.getValue();
                bestBestBuyName = entry.getKey();
            }
            System.out.println(entry.getKey() + " for $" + entry.getValue());
        }
        System.out.println("Lowest BestBuy price: " + bestBestBuyName + " for $" + bestBestBuyPrice);

        System.out.print("\nNewegg Results:");
        System.out.println(" " + neweggURL);
        for (Map.Entry<String, Double> entry: neweggMap.entrySet()){
            if (bestNeweggPrice == 0){
                bestNeweggPrice = entry.getValue();
                bestNeweggName = entry.getKey();
            }
            else if (bestNeweggPrice > entry.getValue()){
                bestNeweggPrice = entry.getValue();
                bestNeweggName = entry.getKey();
            }
            System.out.println(entry.getKey() + " for $" + entry.getValue());
        }
        System.out.println("Lowest Newegg price: " + bestNeweggName + " for $" + bestNeweggName);
    }
}
