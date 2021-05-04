import java.io.IOException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;


public class DatasetParser2 {
    private String baseURL; 
    private Document mainDoc;
    private Document addDoc; 
    Map<String, String> topics; 

    /*
     * Constructor that initializes the base URL and loads 
     * the document produced from that URL
     */
    public DatasetParser2() {
        this.baseURL = "https://www.opendataphilly.org/";
        try {
            this.mainDoc = Jsoup.connect(this.baseURL).get();
        } catch (IOException e) {
            System.out.println("Failed to get document");
        }
    }

    /*
     * Creates a HashMap that maps the topic to the respective page URL 
     * for that topic 
     */
    public void makeTopicsMap() {
    	this.topics = new HashMap<String, String>();
    	Elements anchors = this.mainDoc.select(".browse-topic").select("a");
		
    	for (Element x : anchors) {
    		String link = x.attr("href"); 
    		String topic = x.text();
            this.topics.put(topic, "https://www.opendataphilly.org/" + link);
    	}
    }
    
    /*
     * TODO
     */
    public void listSetsInTopic(String topic) {
        List<String> list = new ArrayList<String>();
        // find link for given topic and load document 
    	String relevantURL = this.topics.get(topic);
	    try {
	        this.addDoc = Jsoup.connect(relevantURL).get();
	    }           
	    catch (IOException e) {
	        System.out.println("Failed to get document");
	    }

        boolean reachedLast = false;
        // extract titles of all data sets on the current page 
        while (!reachedLast) {
            Elements datasets = addDoc.select(".dataset-item"); 
            Elements anchors = datasets.select("div.dataset-content > h3").select("a"); 
            for (Element a : anchors) { // add each title to list
                list.add(a.text());
            }

            // select last element and determine if there is a next page
            Elements pageNum = addDoc.select(".pagination > li");
            Element lastElement = pageNum.get(pageNum.size() - 1);
            if (lastElement.className().contains("active")) {
                reachedLast = true;
            }
            else {
                String nextPage = "https://www.opendataphilly.org/" + lastElement.select("a").attr("href");
                try {
                    this.addDoc = Jsoup.connect(nextPage).get();
                }           
                catch (IOException e) {
                    System.out.println("Failed to get document");
                }
            }
        }
        System.out.println("All datasets with the topic " + topic + ":");
        System.out.println(list);
    } 

    /*
     * TODO
     */
    public void findDescription(String dataName) {
        // find link to page containing all datasets 
        Element navBar = mainDoc.selectFirst(".navigation");
        Elements anchors = navBar.select("li > a"); 
        Element curr = anchors.get(0); 

        // go through each li tag until the one for datasets is found
        while (!curr.text().contains("Datasets")) {
            curr.nextElementSibling();
        }
        String datasetsLink = "https://www.opendataphilly.org/" + curr.attr("href");
        try {
            this.addDoc = Jsoup.connect(datasetsLink).get();
        } catch (IOException e) {
            System.out.println("Failed to get document");
        }

        // go through each page until page is found or last page is reached
        boolean reachedLast = false;
        boolean foundDataset = false;
        String link = null; // initialize link to page for given dataset
        while (!reachedLast && !foundDataset) {
            Elements datasets = addDoc.select(".dataset-item"); 
            Elements aTags = datasets.select("div.dataset-content > h3 > a"); 
            for (Element a : aTags) { // check if title matches 
                if (a.text().contains(dataName)) {
                    foundDataset = true; 
                    link = "https://www.opendataphilly.org/" + a.attr("href");
                    break;
                }
            }

            // if dataset has been found in prior step, skip over this 
            if (!foundDataset) {
                // select last element and determine if there is a next page
                Elements pageNum = addDoc.select(".pagination > li");
                Element lastElement = pageNum.get(pageNum.size() - 1);
                if (lastElement.className().contains("active")) {
                    reachedLast = true;
                }
                else {
                    String nextPage = "https://www.opendataphilly.org/" + lastElement.select("a").attr("href");
                    try {
                        this.addDoc = Jsoup.connect(nextPage).get();
                    }           
                    catch (IOException e) {
                        System.out.println("Failed to get document");
                    }
                }
            }
        }

        // return error msg if dataset not found, otherwise load page and print descr
        if (link == null) {
            System.out.println("Error! Data set not found.");
            return;
        }
        try {
            this.addDoc = Jsoup.connect(link).get();
        }           
        catch (IOException e) {
            System.out.println("Failed to get document");
        }

        String descr = addDoc.selectFirst("div.notes").text(); 
        System.out.println("Description for dataset " + dataName + ": " + descr);
    }

}
