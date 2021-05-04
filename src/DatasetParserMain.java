public class DatasetParserMain {
    public static void main (String[] args) {
        
        DatasetParser2 dsParser = new DatasetParser2(); 
        dsParser.findTopics();
        dsParser.listSetsInTopics("Health / Human Services");
    }
}