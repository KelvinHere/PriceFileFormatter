# PriceFileFormatter

Creates a formatted csv file from three input csv files.   A Java/SQL program using HSQLDB.

### Program overview

Inputs :
* **Price csv file** - New / Updated stock items - Unknown structure, user selects columns to use
* **Supplier csv file** - List of suppliers & associated data - Known structure, user can select supplier
* **Report csv file** - List of existing stock items and stock count - Known structure, user has no interaction

Output :

| Abbrev Description | Their SKU | Our SKU | Description | Extra Description | Net Cost	| Price 1 | Price 2 | Group1 | Group2 | Supplier Code | Vat Switch
|---|---|---|---|---|---|---|---|---|---|---|---|

* ***Formatted CSV file** with the structure above
    * Abbrev Description = First 2 words of description (Wet Panel)
    * Their SKU = SKU untouched (ABCD)
    * Our SKU = their SKU prefixed with supplier code (NQL ABCD)
    * Description = First 60 characters of the original description (Wet Panel chrome 1200mm)
    * Extra Description = Anything over 60 characters from original description
    * Net Cost = Net cost of item
    * Price 1 = Net Cost * selected supplier from Supplier file markup_1 * vat
    * Price 2 = Net Cost * selected supplier from Supplier file markup_2 * vat
    * Group 1 = selected supplier from Supplier file Group_1
    * Group 2 = selected supplier from Supplier file Group_2
    * Supplier Code = selected supplier in Supplier file Supplier_code
    * Vat Switch = selected supplier in Supplier file vat_switch

### Project Purpose
Learning project to display an understanding of Java, SQL database, connections, file handling, JUnit testing, Data Structures and Swing GUI.

### Database
Using HSQLDB the database model is built on the diagram below

### User Stories
**ID** | **As a/an** | **I want to be able to...** | **So that I can**
--- | --- | --- | ---
1 | User | Format a price file quickly | Can work on more important things 
2 | " | See if I have stock of an EOL item | Dump the stock quickly & remove it from show-room

### Features
* User can select a CSV file with products in
* User can select a supplier from a list
* User can create an output file from the inputs above

* CSV
	* Instead of using a CSV library, as a learning experience I have created a reader and parser manually
	* CSV headers are read and SQL tables can be created from these
	* Formatting CSV data, first commas (,) in between double quotes ("") are removed
	* Backslashes and double quotes are removed ("")


Program will :-
* Have GUI to select New Price file, Report file & Suppliers file
* Format the price file so it can be consumed by a legacy database
* Inform user of discontinued items that are currently in stock
* Create 3 files :-
    * Update price only on existing items
    * Create new item for new items
    * Create discontinued items list to be removed

### Testing
Testing is automated using JUnit

### Technologies Used
- [Java](https://www.java.com/en/) - Language
- [HSQLDB](https://hsqldb.org/) - Database
- [Eclipse](https://www.eclipse.org/ide/) - IDE
- Regex
- SQL
- [Git](https://git-scm.com/) - Version control
- [GitHub](https://www.github.com) - Code hosting platform
- [Draw.io](https://www.draw.io/) -Prototyping wire-framing tool
