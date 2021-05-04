import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.security.KeyStore.Entry;
import java.util.*;

public class DatasetParser {
    private String baseURL;
    private Document currentDoc;
    Map<String, String> linkMap;
    private String currentURL;

    public DatasetParser() {
        this.baseURL = "https://www.opendataphilly.org/";
        this.currentURL = this.baseURL;
        try {
            this.currentDoc = Jsoup.connect(this.baseURL).get();
        } catch (IOException e) {
            System.out.print("OpenDataPhilly page could not be gotten.");
        }
    }

        /*
     * Creates article map to be a mapping of article titles to url from our current doc
     */
    public void getLinkMap() {
        this.linkMap = new HashMap<String, String>();
        Elements aTags = this.currentDoc.select("a"); // a with href
        for (Element aTag: aTags) {
            String theURL = aTag.attr("href");
            String theTitle = aTag.text().toLowerCase();
            this.linkMap.put(theTitle, theURL);
            // System.out.println(theTitle + " " + theURL);
        }
    }

    /*
    Obtains the link page of the page desired when given the page's name
     */
    public String getLinkPage(String linkTitle) {
        //for uniformity, all titles are made in lower case to prevent case issues
        linkTitle = linkTitle.toLowerCase();
        String relevantURL = this.linkMap.get(linkTitle);
        if (relevantURL.charAt(0) == '/') {
            relevantURL = "https://www.opendataphilly.org" + relevantURL;
        }

        try {
            this.currentDoc = Jsoup.connect(relevantURL).get();
        }
        catch (IOException e) {
            System.out.println("Not able to get new page.");
        }
        Element head = this.currentDoc.selectFirst("head");
        Element title = head.selectFirst("title");
        this.currentURL = relevantURL; 
        this.getLinkMap();

        return title.text();
    }

    // setting link page will just be use for when traversing through the pages since they have a standard format
    public void setLinkPage(String desiredURL) {
        try {
            this.currentDoc = Jsoup.connect(desiredURL).get();
        }
        catch (IOException e) {
            System.out.println("Not able to get new page.");
        }
        this.currentURL = desiredURL;
        this.getLinkMap();
    }

    public void initializeBaseDoc() {
        try {
            this.currentDoc = Jsoup.connect(this.baseURL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.getLinkMap();
    }

    public void findAllTopics(String datasetName) {
        String datasetNameLC = datasetName.toLowerCase();
        Elements topicsForDataset = null;
        getLinkPage("datasets");
        // System.out.println(this.currentDoc);
        /* while loop to go through all of the pages. Since we are just using this method in this manner, we can hardcode that there
         * 20 pages of datasets as of this moment on the site 
         */
        for (int i = 1; i < 21; i++) {
            if (this.currentURL.endsWith("dataset")) {
                setLinkPage(this.currentURL + "?page="+ Integer.toString(i));
            } else if (this.currentURL.contains("page=")) {
                this.currentURL = this.currentURL.substring(0, this.currentURL.length() - 1);
                setLinkPage(this.currentURL + Integer.toString(i));
            }

            if (this.linkMap.containsKey(datasetNameLC)) {
                getLinkPage(datasetNameLC);
                Element topicTab = null;
                Elements ulElements = this.currentDoc.select("ul");
                for (Element ulElement: ulElements) {
                    if (ulElement.className().equals("nav nav-tabs")) {
                        Elements liElements = ulElement.select("li");
                        for (Element liElement: liElements) {
                             if (liElement.text().toLowerCase().contains("topic")) {
                                topicTab = liElement;
                                break;
                             }
                        }
                        break;
                    }
                }

                Element topicATag = topicTab.select("a").first();
                String theURL = topicATag.attr("href");
                theURL = "https://www.opendataphilly.org" + theURL;
                setLinkPage(theURL);
                topicsForDataset = this.currentDoc.select("li>h3");
                break;        
            }
        }
            
        System.out.println("The topic(s) for " + datasetName + " is/are:");
        for (Element topic: topicsForDataset) {
            System.out.println(topic.text());
        }  

    }


}
