public class DatasetParserMain {
    public static void main (String[] args) {
        
        DatasetParser2 dsParser = new DatasetParser2(); 
        dsParser.makeTopicsMap();
        // dsParser.listSetsInTopic("Health / Human Services");
        // System.out.println("--------------");
        // dsParser.findDescription("Leaf Collection Areas");
        // System.out.println("--------------");
        // System.out.println("(* Trying a keyword that should bring no results *)");
        // dsParser.findDescription("unicorns"); // this should fail
        // System.out.println("--------------");
        // dsParser.matchKeyword("water");
        // System.out.println("--------------");
        // System.out.println("(* Trying a keyword that should yield no results *)");
        // dsParser.matchKeyword("randomkeywordthatshouldnotmatchanything"); // this should fail
        // System.out.println("--------------"); 
        // dsParser.matchKeywordUnderTopic("housing", "Budget / Finance");
        // System.out.println("--------------");
        // System.out.println("(* Trying a keyword that should yield no results *)");
        // dsParser.matchKeyword("randomkeywordthatshouldnotmatchanything"); // this should fail
        // System.out.println("--------------");
        // dsParser.findAllTags("Census Blocks");
        // System.out.println("--------------");
        // dsParser.findOrganizationAndPrintOthers("Instore Forgivable Loan Program");
        // System.out.println("--------------");

        DatasetParser dsparse = new DatasetParser();
        dsparse.getLinkMap();
        // dsparse.findAllTopics("Employee Earnings");
        // System.out.println("--------------");
        // dsparse.findAllTopics("Greater Philadelphia GeoHistory Network");
        // System.out.println("--------------");
        // dsparse.getSetsCreatedAtDate("Arts / Culture / History", 8, 12, 2014);
        // System.out.println("--------------");
        // // this shouldn't work
        // dsparse.getSetsCreatedAtDate("Arts / Culture / History", 9, 13, 2014);
        // System.out.println("--------------");
        //dsparse.numDatasetsContributed("NOAA");
        // System.out.println("--------------");
        // dsparse.numDatasetsContributed("PhiladelphiaDANCE.org");
        // System.out.println("--------------");
        // dsparse.numDatasetsContributed("School District of Philadelphia");
        // System.out.println("--------------");
        // dsparse.getNumDatasetsInTopic("elections / politics");
        // System.out.println("--------------");
        // dsparse.getOrgMostDatasets();
        // System.out.println("--------------");
        // dsparse.getTopicMostDatasets();
        // System.out.println("--------------");
        // dsparse.grabPartners();
        // System.out.println("--------------");
    }
}