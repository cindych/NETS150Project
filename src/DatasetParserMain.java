public class DatasetParserMain {
    public static void main (String[] args) {
        
        DatasetParser dsparse = new DatasetParser();
        dsparse.getLinkMap();
        dsparse.findAllTopics("Employee Earnings");
    }
}