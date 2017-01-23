# Documentation
Explanation of files in: `src/main/resources/domain/purchases/`
 - `.xml` files in `xml/` contain the data to be processed
 - `.xsl` files in `xsl/` specify how the data is transformed from XML to CSV.
 - `.xsd` files in `xsd/` define the schema for the XML files

## The Program
This Scala program is used to convert XML to CSV according to an XSLT template, and test that the output results are correct using tests written using **ScalaTest**.
 
Upon running this program, it reads the contents of the passed configuration file, eg `application.conf`, to determine what files to convert and where to look for the XSL template to transform it according to.

It will load each XML file in the specified `r10.xml.source.path` directory and transform it according to the XSL in `r10.xsl.template` and save it to the output directory `r10.transform.output.path`, default: `output/r10`. 

Each of the XSL files define a different transformation, and extract different parts of the data.

For example,  `r10_extract_retailtransaction_customer.xsl` extracts only customer information and the transaction ID for each transaction.

## Testing

You can find the tests in `src/test/scala/com/sainsburys/dpp/transform`, there is one for each XSLT file.
They all inherit from the base class `XMLTest`.

At the top of the file, there are variables for `xmlPath`, `xslPath`, and `outPath`. There are set per-test.

Inside the test, `transformFiles(callback: (xmlFile: String, csvFile: String))` transforms each XML file in the `xmlPath` directory, and then calls the callback, passing the name of the XML file transformed and the CSV file it was saved to.
Inside this callback, the contents of the generated CSV are loaded into a 2D array, and the source XML file is loaded as a node tree as well (`NodeSeq`).
Assertions are then made to ensure that the data in the CSV array matches the data in the XML.

The `assertFields` function takes a `Map[Int, NodeSeq]` of column indices in the CSV file and relevant `Node` in the XML file and performs an assertion, checking that they are equal.

The `assertManyFields` function takes the 2D array of CSV data, a `NodeSeq` object with a collection of nodes where the data can be found in the XML (one for each row in the CSV) and a function `getFieldMap`, which takes a `NodeSeq` with a single node in, and returns a `Map[Int, NodeSeq]` mapping for each column index in the CSV to the node in the XML containing that data.
It then calls `assertFields` for each row in the CSV to make the assertions.

When tests have finished running, the output CSV files are automatically deleted from `output/test`.

## Structure of the XSL file
Each XSL file defines a number of templates which are systematically applied to the data in a logical order to transform it.
A template is denoted by `<xsl:template match="..." mode="..."></xsl:template>`. `match` is mandatory and is an XPath to the element it will match.
A single slash `/` refers to the root node in the document.
`mode` tells the program to only match this template if it has been applied with `<xsl:apply-templates mode="..." />` with the same mode.

### `<xsl:apply-templates select="..." mode="..." />`
This tag means "select all elements matching the XPath in `select`, and apply the matching template."
So, for example in `r10_extract_retailtransaction_customer.xsl`, `<xsl:apply-templates select="//r10Ex:Transaction" mode="s1_user-defined"/>` selects all `<Transaction>` nodes in the XML and applies the template `<xsl:template match="r10Ex:Transaction" mode="s1_user-defined">` to them.

### Modes
There are some pre-defined modes which are reused in different templates.

#### s3_flatten
'flattens' nested XML into one dimension using underscores to separate child nodes - attributes use a dot.

For example:
```
<IssuesCoupon Identifier="132332323-6f93-32323-83c0-d8dfe" SeriesId="PPL" OfferId="4389" RewardValue="0.02" StartDate="2016-05-16" ExpiryDate="2016-06-15">
  <Description>PPL_2p</Description>
  <ScanCode>12345678901234</ScanCode>
</IssuesCoupon>
```
Would flatten to the following:

```
IssuesCoupon.Identifier, IssuesCoupon.SeriesId, IssuesCoupon.OfferId,IssuesCoupon.RewardValue,IssuesCoupon.StartDate,IssuesCoupon.ExpiryDate,IssuesCoupon_Description,IssuesCoupon_ScanCode
132332323-6f93-32323-83c0-d8dfe,PPL,4389,0.02,2016-05-16,2016-06-15,PPL-2p,12345678901234 
```

### s4_equalising
TODO: Need to find out more about how this works, as it was existing code.

### s7_xml2csv
This mode takes the XML after all processing has been done, and converts it into a CSV format with the header row at the top containing all the field names, and the data below it.