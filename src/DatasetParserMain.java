public class DatasetParserMain {
    public static void main (String[] args) {
        
        DatasetParser2 dsParser = new DatasetParser2(); 
        dsParser.makeTopicsMap();
        dsParser.listSetsInTopic("Health / Human Services");
        System.out.println("--------------");
        dsParser.findDescription("Leaf Collection Areas");
        System.out.println("--------------");
        dsParser.findDescription("this should fail");
        System.out.println("--------------");
        dsParser.matchKeyword("water");
        System.out.println("--------------");
        dsParser.matchKeywordUnderTopic("vending", "Food");
        dsParser.findOrganizationAndPrintOthers("Vision Zero High Injury Network");

        DatasetParser dsparse = new DatasetParser();
        dsparse.getLinkMap();
        dsparse.findAllTopics("Employee Earnings");
        System.out.println("--------------");
        dsparse.findAllTopics("Greater Philadelphia GeoHistory Network");
        System.out.println("--------------");
        dsparse.getSetsCreatedAtDate("Arts / Culture / History", 8, 12, 2014);
        System.out.println("--------------");
        // this shouldn't work
        dsparse.getSetsCreatedAtDate("Arts / Culture / History", 9, 13, 2014);
        System.out.println("--------------");
    }
}