package crawl;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;

//Joshua Lanctot
//This class takes in a sentence to be searched on different retailers websites using Jsoup.
//The prices are returned for each website, showing the lowest price available.
public class PriceScraper {
    private HashMap<>

    private final String agent =
            "Mozilla/5.0";
    private String sentenceToBeSearched;
    private String amazonURL;
    private String bestBuyURL;
    private String neweggURL;

    private String amazonTotal;
    private String bestBuyTotal;
    private String neweggTotal;

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

    //connect to URLs and scrape the prices
    void scrapeAmazon(){
        String amazonPrice = null;
        String amazonCents = null;
        String total = null;

        try{
            Document amazonDoc = Jsoup.connect(amazonURL).userAgent(agent).get();         //connect to URLs and get the first price from the html to be printed
            amazonPrice = amazonDoc.select("span.a-price-whole").first().text();
            amazonCents = amazonDoc.select("span.a-price-fraction").first().text();
            amazonName = amazonDoc.select("a.a-link-normal.a-text-normal").first().text();
            total = amazonPrice + amazonCents;
            amazonTotal = total;

        } catch(Exception e){
            System.out.println("An error occurred, Amazon could not search for this item.");
        }
    }

    //method for each website so that if an error on website occurs the others websites can still try
    void scrapeBestBuy() throws ParseException {
        String bestBuyPrice = null;

        try {
            Document bestBuyDoc = Jsoup.connect(bestBuyURL).userAgent(agent).get();
            bestBuyPrice = bestBuyDoc.select("div.priceView-hero-price.priceView-customer-price").first().text();
            bestBuyName = bestBuyDoc.select("h4.sku-header").first().text();
            String newBestBuyPrice = bestBuyPrice.substring(1);
            bestBuyTotal = newBestBuyPrice;

        } catch (Exception e){
            System.out.println("An error occurred, BestBuy could not search for this item.");
        }
    }

    void scrapeNewegg() {
        String neweggPrice = null;
        String neweggCents = null;
        String total = null;

        try {
            Document neweggDoc = Jsoup.connect(neweggURL).userAgent(agent).get();
            Elements div = neweggDoc.select("div.list-wrap");                 //make sure the first item is selected and not an ad

            neweggPrice = div.select("li.price-current > strong").first().text();
            neweggCents = div.select("li.price-current > sup").first().text();
            neweggName = div.select("a.item-title").first().text();

            total = neweggPrice + neweggCents;
            neweggTotal = total;

        } catch (Exception e){
            System.out.println("An error occurred, BestBuy could not search for this item.");
        }
    }

    //print out names and prices
    void printPrices(){
        String[] total = {amazonTotal, bestBuyTotal, neweggTotal};
        String[] website = {"Amazon:","BestBuy:", "Newegg:"};
        String[] names = {amazonName, bestBuyName, neweggName};
        System.out.println();
        for (int i=0; i<total.length; i++){
            if (total[i] != null){
                System.out.println(website[i] + " " + names[i] + " for $" + total[i]);
            }
        }
    }
}
