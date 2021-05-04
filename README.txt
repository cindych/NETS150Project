Project Name: TODO
Project Members: Cindy Chen, Jada Harrison
Description: This program was made to help users search for datasets and retrieve information for 
             particular datasets on https://www.opendataphilly.org/, a catalog of data in the
             Philadelphia region to complement the existing built-in search functionality! 

             Its features among others include finding datasets created on a specific date, finding
             datasets with a specific keyword in its title, retrieving information about the 
             description and topics of a dataset, and retrieving information about the organization 
             of a dataset and all other datasets published by the organization. 

             Features
             listSetsInTopics: prints out all datasets under a specific topic 
                (inputted topic with same punctuation and capitalization as on page)
                
             findDescription: prints out the description of a specified dataset 
                (inputted dataset must be the same as shown on site, otherwise
                error message is printed out)

             matchKeyword: prints out all datasets with a specific keyword in its title 

             matchKeywordUnderTopic: prints out all datasets with a specific keyword AND  
                under a particular topic

             findOrganizationAndPrintOthers: prints out the organization that published a 
                specified dataset as well as other datasets published by same organization 

             TODO 

Categories: World Wide Web (WWW)
    -> We interacted directly with URLs and the World Wide Web, using JSoup to parse and find HTML elements 
       in order to collect information (web scraping) as needed from https://www.opendataphilly.org/ for our functions.

Work Breakdown
    -> Jada: DatasetParser.java + DatasetParserMain.java

    -> Cindy: DatasetParser2.java + DatasetParserMain.java
        Aside from helper methods, implemented the following functions in DatasetParser2.java: 
        listSetsInTopic, findDescription, matchKeyword, matchKeywordUnderTopic, 
        findOrganizationAndPrintOthers
    