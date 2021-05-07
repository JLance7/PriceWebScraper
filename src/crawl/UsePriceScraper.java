package crawl;

import java.util.Scanner;

public class UsePriceScraper {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the item you would like to get the prices scraped from retailers: ");
        String sentence = input.nextLine();
        PriceScraper priceScraper = new PriceScraper(sentence);
        priceScraper.scrape();


    }
}
