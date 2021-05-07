package crawl;

import java.text.ParseException;
import java.util.Scanner;

public class UsePriceScraper {
    public static void main(String[] args) throws ParseException {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the item you would like to get the prices scraped from retailers:");
        String sentence = input.nextLine();

        PriceScraper priceScraper = new PriceScraper(sentence);
        priceScraper.setURLS();
        priceScraper.scrapeAmazon();
        priceScraper.scrapeBestBuy();
//        priceScraper.scrapeWalmart();
//        priceScraper.scrapeTarget();
        priceScraper.printPrices();


    }
}
