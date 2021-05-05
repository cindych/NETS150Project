import java.io.IOException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
     * Helper method
     * Updates addDoc with the link to the page containing all datasets
     */ 
    private void findDatasetsPage() {
        Element navBar = mainDoc.selectFirst(".navigation");
        Elements anchors = navBar.select("li > a"); 
        Element curr = anchors.get(0); 

        // go through each li tag until the one for datasets is found
        while (!curr.text().toLowerCase().contains("datasets")) {
            curr.nextElementSibling();
        }
        String datasetsLink = "https://www.opendataphilly.org/" + curr.attr("href");
        try {
            this.addDoc = Jsoup.connect(datasetsLink).get();
        } catch (IOException e) {
            System.out.println("Failed to get document");
        }    
    }

    /*
     * Helper method
     * returns true if next page was successfully loaded,
     * false otherise
     */
    private boolean loadNextPage() {
        Elements pageNum = addDoc.select(".pagination > li");
        if (pageNum.size() == 0) {
            return false; // return false if single page
        }

        Element lastElement = pageNum.get(pageNum.size() - 1);
        if (lastElement.className().contains("active")) {
            return false;
        }
        else {
            String nextPage = "https://www.opendataphilly.org/" + lastElement.select("a").attr("href");
            try {
                this.addDoc = Jsoup.connect(nextPage).get();
            }           
            catch (IOException e) {
                System.out.println("Failed to get document");
            }
            return true;
        }
    }

    /*
     * returns the URL for a given dataset,
     * returns null if dataset not found
     */
    private String findDatasetURL(String dataName) {
        // go through each page until dataset is found or last page is reached
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
                if (!loadNextPage()) {
                    reachedLast = true;
                }
            }
        }
        return link;
    } 
    
    /*
     * Returns a list of the names of all the data sets with
     * a specific topic and prints content of list 
     */
    public List<String> listSetsInTopic(String topic) {
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
            if (!loadNextPage()) {
                reachedLast = true;
            }
        }
        System.out.println("All datasets with the topic " + topic + ":");
        System.out.println(list);
        return list;
    } 

    /*
     * Prints out the description of a specified dataset,
     * returns an error message if dataset does not exist
     */
    public void findDescription(String dataName) {
        findDatasetsPage();
        String link = findDatasetURL(dataName);

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

    /*
     * Returns a list of the names of all data sets with
     * a specific keyword in its title and prints content of list
     */ 
    public List<String> matchKeyword(String s) {
        findDatasetsPage();
        List<String> list = new ArrayList<String>();
        String keyword = s.toLowerCase();

        // go through each page until last page is reached
        boolean reachedLast = false;
        while (!reachedLast) {
            Elements datasets = addDoc.select(".dataset-item"); 
            Elements aTags = datasets.select("div.dataset-content > h3 > a"); 
            for (Element a : aTags) { // check if title matches 
                String title = a.text().toLowerCase();
                if (title.contains(keyword)) {
                    list.add(a.text());
                }
            }

            // determine if there is a next page
            if (!loadNextPage()) {
                reachedLast = true;
            }
        }

        System.out.println("The datasets that have the keyword '" + keyword + "' are:");
        System.out.println(list);
        return list;
    }

    /*
     * Returns a list of the names of all data sets under a topic
     * with a specific keyword in its title and prints content of list
     */ 
    public List<String> matchKeywordUnderTopic(String s, String topic) {
        // find link for given topic and load document 
    	String relevantURL = this.topics.get(topic);
	    try {
	        this.addDoc = Jsoup.connect(relevantURL).get();
	    }           
	    catch (IOException e) {
	        System.out.println("Failed to get document");
	    }
        List<String> list = new ArrayList<String>();
        String keyword = s.toLowerCase();

        // go through each page until last page is reached
        boolean reachedLast = false;
        while (!reachedLast) {
            Elements datasets = addDoc.select(".dataset-item"); 
            Elements aTags = datasets.select("div.dataset-content > h3 > a"); 
            for (Element a : aTags) { // check if title matches 
                String title = a.text().toLowerCase();
                if (title.contains(keyword)) {
                    list.add(a.text());
                }
            }

            // determine if there is a next page
            if (!loadNextPage()) {
                reachedLast = true;
            }
        }

        System.out.print("The datasets that have the keyword '" + keyword);
        System.out.println("' under topic " + topic + " are:\n" + list);
        return list;
    }

    /*
     * Returns a list of all the tags for a given dataset
     * and prints them
     */
    public List<String> findAllTags(String dataName) {
        findDatasetsPage();
        List<String> listOfTags = new ArrayList<String>();
        Element allTags = null;

        // go through each page until dataset is found or last page is reached
        boolean reachedLast = false;
        boolean foundDataset = false;
        while (!reachedLast && !foundDataset) {
            Elements datasets = addDoc.select(".dataset-item"); 
            Elements aTags = datasets.select("div.dataset-content > h3 > a"); 
            for (Element a : aTags) { // check if title matches 
                if (a.text().contains(dataName)) {
                    foundDataset = true; 
                    Element liTag = a.parent().parent();
                    allTags = liTag.nextElementSibling();
                    break;
                }
            }

            // if dataset has been found in prior step, skip over this 
            if (!foundDataset) {
                // select last element and determine if there is a next page
                if (!loadNextPage()) {
                    reachedLast = true;
                    System.out.println("Dataset not found!");
                    return listOfTags; 
                }
            }
        }

        for (Element tag : allTags.select("li")) { 
            listOfTags.add(tag.text()); // add tags to list 
        }
        System.out.println("The tags for '" + dataName + "' are:");
        System.out.println(listOfTags);
        return listOfTags;
    }

    /*
     * Prints out the organization that published the dataset and 
     * also prints/returns the names of all other data sets published 
     * by same organization
     */ 
    public List<String> findOrganizationAndPrintOthers(String dataName) {
        findDatasetsPage();
        String link = findDatasetURL(dataName);
        List<String> list = new ArrayList<String>();

        // return error msg if dataset not found, otherwise load page and print descr
        if (link == null) {
            System.out.println("Error! Data set not found.");
            return list;
        }
        try {
            this.addDoc = Jsoup.connect(link).get();
        }           
        catch (IOException e) {
            System.out.println("Failed to get document");
        }

        // find organization name and link to all datasets published by organization
        Element orgInfo = addDoc.selectFirst("div.context-info > section.module-content");
        String orgName = orgInfo.selectFirst("h1").text();
        System.out.println("The organization who published '" + dataName + "' is " + orgName + ".");
        String linkToAllDatasets = "https://www.opendataphilly.org/" + orgInfo.select("div.image > a").attr("href");

        // load link to page of all datasets 
        try {
            this.addDoc = Jsoup.connect(linkToAllDatasets).get();
        }           
        catch (IOException e) {
            System.out.println("Failed to get document");
        }

        boolean reachedLast = false;
        // extract names of all data sets besides the original data set
        // given as an input argument
        while (!reachedLast) {
            Elements datasets = addDoc.select(".dataset-item"); 
            Elements anchors = datasets.select("div.dataset-content > h3").select("a"); 
            for (Element a : anchors) { // add each title to list
                if (!a.text().equals(dataName)) {
                    list.add(a.text());
                }
            }

            // determine if there is a next page
            if (!loadNextPage()) {
                reachedLast = true;
            }
        }

        if (list.size() == 0) {
            System.out.println("No other datasets! :(");
        }
        else {
            System.out.println("There are " + list.size() + " other datasets");
            System.out.println("Other datasets under the organization:");
            System.out.println(list);
        }
        return list;
    }
}