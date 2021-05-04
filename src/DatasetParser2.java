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
    public void findTopics() {
    	this.topics = new HashMap<String, String>();
    	Elements anchors = this.mainDoc.select(".browse-topic").select("a");
		
    	for (Element x : anchors) {
    		String link = x.attr("href"); 
    		String topic = x.text();
            this.topics.put(topic, "https://www.opendataphilly.org/" + link);
    	}
    }
  
    public void listSetsInTopics(String topic) {
        List<String> list = new ArrayList<String>();
        // find link for desired topic and load document 
    	String relevantURL = this.topics.get(topic);
	    try {
	        this.addDoc = Jsoup.connect(relevantURL).get();
	    }           
	    catch (IOException e) {
	        System.out.println("Failed to get document");
	    }

        boolean reachedLast = false;
    
        // extract titles
        while (!reachedLast) {
            Elements datasets = addDoc.select(".dataset-item"); 
            Elements anchors = datasets.select("div.dataset-content > h3").select("a"); 
            for (Element a : anchors) { // add title to list
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
        System.out.println(list);
    } 

}
