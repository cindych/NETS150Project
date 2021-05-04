import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.security.KeyStore.Entry;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            //System.out.println(theTitle + " " + theURL);
        }
    }

    public HashMap<String, String> getTopicMap() {
        HashMap<String, String> topicMap = new HashMap<String, String>();
        Elements aTags = this.currentDoc.select(".dataset-heading > a"); // a with href
        for (Element aTag: aTags) {
            String theURL = aTag.attr("href");
            String theTitle = aTag.text().toLowerCase();
            topicMap.put(theTitle, theURL);
            //System.out.println(theTitle + " " + theURL);
        }
        return topicMap;
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
        HashMap<String, String> topicMap = null;
        initializeBaseDoc();
        String datasetNameLC = datasetName.toLowerCase();
        Elements topicsForDataset = null;
        getLinkPage("datasets");
        getLinkMap();
        // System.out.println(this.currentDoc);
        /* while loop to go through all of the pages. Since we are just using this method in this manner, we can hardcode that there
         * 20 pages of datasets as of this moment on the site 
         */
        for (int i = 1; i < 21; i++) {
            if (this.currentURL.endsWith("dataset")) {
                setLinkPage(this.currentURL + "?page="+ Integer.toString(i));
            } else if (this.currentURL.contains("page=")) {
                this.currentURL = this.currentURL.substring(0, this.currentURL.lastIndexOf("=")+1);
                setLinkPage(this.currentURL + Integer.toString(i));
            }
            getLinkMap();
            topicMap = getTopicMap();
            if (topicMap.containsKey(datasetNameLC)) {
                setLinkPage("https://www.opendataphilly.org" + topicMap.get(datasetNameLC));
                getLinkMap();
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

    String matchMonths(int month) {
        switch(month) {
            case(1):
              return ("January");
            case(2):
              return ("February");
            case(3):
              return ("March");
            case(4):
              return ("April");
            case(5):
              return ("May");
            case(6):
              return ("June");
            case(7):
              return ("July");
            case(8):
              return ("August");
            case(9):
              return ("September");
            case(10):
              return ("October");
            case(11):
              return ("November");
            case(12):
              return ("December");
            default:
              return ("month number entered is not valid");
        }
    }

    void getSetsCreatedAtDate(String topic, int day, int month, int year) {
       if (month < 1 || month > 12) {
           System.out.println("invalid month entered.");
           return;
       } else if (day < 0 || day > 31) {
        System.out.println("invalid day entered.");
       } else if ((month == 9 || month == 4 || month == 6 || month == 11) && day > 30) {
        System.out.println("invalid day for specific month entered.");
        return;
       } else if (month == 2) {
           if ((year % 4 != 0 && day > 28) || (year%4==0 && day > 29)) {
            System.out.println("invalid day in February for year entered.");
            return;
           }
       }

        System.out.println("The dataset(s) under the topic " + topic + " that were created on " 
                 + matchMonths(month) +" " + day + " ," + year + " are: ");
        initializeBaseDoc();

        String topicLC = topic.toLowerCase();
        getLinkPage(topicLC);
        Elements pageNumbers = this.currentDoc.select(".pagination > li > a");
        
        int maxPage = Integer.valueOf(pageNumbers.get(pageNumbers.size() - 2).text());

        for (int i = 1; i < maxPage + 1; i++) {
            if (!this.currentURL.contains("page=")) {
                setLinkPage(this.currentURL + "?page="+ Integer.toString(i));
            } else {
                this.currentURL = this.currentURL.substring(0, this.currentURL.lastIndexOf("=")+1);
                setLinkPage(this.currentURL + Integer.toString(i));
            }

            getLinkMap();
            HashMap<String, String> topicMap = getTopicMap();
            Elements datasets = this.currentDoc.select(".dataset-heading > a");
            for (Element dataset: datasets) {
                setLinkPage("https://www.opendataphilly.org" + topicMap.get(dataset.text().toLowerCase()));
               Element dateCreated = this.currentDoc.select(".automatic-local-datetime").get(0);
               String pattern = "(\\d{4}+)-(\\d{2}+)-(\\d{2}+)*";
               Pattern p = Pattern.compile(pattern);
               Matcher m = p.matcher(dateCreated.toString());
                if (!m.find()) {
                     System.out.println("no 'Created' row in table.");
                } else {
                     if (year == Integer.valueOf(m.group(1)) && month == Integer.valueOf(m.group(2)) && day == Integer.valueOf(m.group(3))) {
                         System.out.println(dataset.text());
                    }
                }
            }
        }
    }


}
