package crawl;


import org.jsoup.nodes.Document;

//Joshua Lanctot
//This class takes in a sentence to be searched on different retailers websites using JSoup.
//The prices are returned for each website, showing the lowest price available.
public class PriceScraper {
    private String sentenceToBeSearched;
    private String

    PriceScraper(String sentenceToBeSearched){
        this.sentenceToBeSearched = sentenceToBeSearched;
    }


    public void scrape(){
        try{
            Document amazonDoc = JSoup.connect(url).get();

        } catch(Exception e){
            e.printStackTrace();
        }
    }


}
