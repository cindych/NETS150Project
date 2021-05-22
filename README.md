# NETS150Project: OpenDataPhilly Search+

For NETS 150 (Market and Social Systems on the Internet), a course I took in Spring 2021, my partner Jada and I decided to create a tool with the goal of helping users search and retrieve information for particular datasets on [OpenDataPhilly](https://www.opendataphilly.org), a catalog of data in the Philadelphia region, to complement the existing built-in search functionality. Its features, among others, include finding datasets created on a specific date, finding datasets with a specific keyword in its title, retrieving information about the description and topics of a dataset, and retrieving information about the organization of a dataset and all other datasets published by the organization.

## <u> User Manual </u>

### Overview 
Use OpenDataPhilly Search+ via the DatasetParserMain class. Users can uncomment methods and appropriately change inputs as needed to fit their needs. 

### Fuctionality
#### **Inputs**
- `String topic` - topic options can be found on [OpenDataPhilly](https://www.opendataphilly.org) or clicking on the ‘Topics’ tab at the top of the homepage

- `String datasetName` / `String dataName` - dataset name options can be found by clicking on any of the topics or the ‘Datasets’ tab at the top of the homepage

- `String organization` - organization options can be found by clicking on the ‘Organizations’ tab at the top of the homepage

- `String s` - any string of characters (both lowercase and uppercase are valid)

#### **Methods With Inputs**
1. `findAllTopics`: finds all the topics that a specific data set covers
                
2. `getSetsCreatedAtDate`: goes through the landing pages of the datasets within the specific topic requested and tells you which were created on the day specified. 
                
3. `getNumDatasetsContributed`: gives user the number of datasets the organization they entered has contributed to OpenDataPhilly.org
            
4. `getNumDatasetsinTopic`: gives the user the number of datasets the topic entered has classified under it.

5. `listSetsInTopics`: prints out all datasets under a specific topic and returns list of datasets.
    * Note: inputted topic must have same punctuation and capitalization as on page
                
6. `findDescription`: prints out the description of a specified dataset. Prints an error message if the dataset is not found.
    * Note: inputted dataset must be written the same as shown on site

7. `matchKeyword`: prints out all datasets with a specific keyword in its title. Returns list of datasets.

8. `matchKeywordUnderTopic`: prints out all datasets with a specific keyword AND under a particular topic. Returns list of datasets. 
    * Note: inputted topic must have same punctuation and capitalization as on page

9. `findAllTags`: prints the tags of a given dataset and returns a list of the tags. Prints an error message if the dataset is not found. 
    * Note: inputted dataset must be written the same as shown on site

10. `findOrganizationAndPrintOthers`: prints out the organization that published a specified dataset as well as other datasets published by the same organization. Returns list of the other datasets. Prints an error message if the dataset is not found. 
    * Note: inputted dataset must be written the same as shown on site

#### **Inputless Methods**
1. `getOrgMostDatasets`: gives the organization that has contributed/has attributed to the highest number of datasets.        

2. `getTopicMostDatasets`: gives the topic with the highest number of datasets. 
            
3. `grabPartners`: returns the partners of OpenDataPhilly that are on the About page

#### **Example Code**
```java
DataSetParser2 dsParser = new DatasetParser2();
dsParser.makesTopicMap();
dsParser.listSetsInTopic("Real Estate / Land Records");
```
**Terminal Output**
![Terminal Output](https://user-images.githubusercontent.com/78625079/119208958-53ce5000-ba72-11eb-82c1-73b88852be4c.PNG)
