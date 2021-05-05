Project Name: OpenDataPhilly Search+
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

             findAllTopics: finds all the topics that a specific data set covers when once is already known
                input(s): String topic, String datasetName
                
            getSetsCreatedAtDate: goes through the landing pages of the datasets within the specific topic requested and tells you which were 
                created on the day specified. 
                input(s): String topic, int day, int month, int year 
                
            getNumDatasetsContributed: gives user the number of datasets the organization they entered has contributed to OpenDataPhilly.org
                input(s): String organization

            getOrgMostDatasets: gives the organization that has contributed/has attributed to the highest number of datasets. 
                input(s): none
            
            getNumDatasetsinTopic: gives user the number of datasets the topic entered has classified under it.
                input(s): String topic
            
            getTopicMostDatasets: gives the topic with the highest number of datasets. 
                input(s): none
            
            grabPartners: returns the partners of OpenDataPhilly that are on the About page
                inputs(s): none



