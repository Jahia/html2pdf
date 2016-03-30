# HTML to PDF Converter

## Overview

### Introduction
This module is a **Prototype** with which the Jahia Academy team is testing if [Apache PDFBox](https://pdfbox.apache.org/) can be used to convert the Academy HTML pages into PDF documents.

### Background
One of the features of the Jahia Academy is to allow the *download* of an HTML page. Downloading the document means actually downloading a PDF version of the HTML document.

We were looking for a dynamic solution, a solution that generates a PDF document in the "Jahia Looks" and that works on every Academy document.

### Academy Solution
The requirements are to convert the HTML document into a PDF, store it as a child node and distribute to the user. On every download check if a previously generated PDF exists and if yes if it's newer than the last page modification. So either simply download the PDF or create/re-create it dynamically.

## PDF Converter
For the prototype we were looking for an **Open Source** converter with a license suitable for Jahia.

### Apache PDFBox
[Apache PDFBox](https://pdfbox.apache.org/) is, based on *Open Source* and *licensing* requirements an ideal candidate. However, there are limitations that might make PDFBox challenging to be used.

## The HTML 2 PDF Protoype

### Parsing and Converting

The prototype starts with parsing the HTML paragraphs using the [jsoup](http://jsoup.org/) Java HTML parser. Then the prototype iterates through the DOM and identifies tags and converts the content accordingly.

### Hardcoded Conversion
In the first trials the conversion from e.g. a P-tag to the correct font, font-size, etc. was hardcoded.<br/>
Currently the title page, header and footer are still hardcoded.

### Configuration Option
In another update we included the option to configure styles and map these styles to HTML tags.<br/>
For example:<br/>
Styles --> normal-text: font=Lato, font-size=10pt, etc.<br/>
Mapping --> P = normal-text
The configuration still needs to be improved

### Multi-Page Option
Another requirement was to test the possibility of chaining multiple HTML pages to one document. This feature has been added as well and it's possible to configure additional HTML links to the main page.

### Component
A component has been created to test the HTML 2 PDF converter. The component will display a "PDF Download" icon and clicking on it will trigger the conversion process.

### The HTML 2 PDF Conversion Process
1. Click the PDF download icon
2. Code checks if the PDF document has been created previously
3. If yes, compare the "last publication" date with the PDF creation date.
4. If the PDF creation date is older than the *last publication* date OR there is no previously created PDF available, then create the PDF and store it as a child node of the page.

## Next Steps
This is a **Prototype**! It works for the Jahia Academy, but will need to be cleaned-up and brought into a state it can be shared by others

### Step 1 - Clean Up Java Code
The Java code is in working mode, but it's not following design patterns or other API standards. E.g. tags are identified and adapted in the same class. The code will need to be broken down into additional classes.

### Step 2 - Make Area starting point configurable
This prototype is adapted to Jahia Academy HTML pages. These pages have an area called "Document" and hence iterates through the paragraphs within the area. <br/>
The code has to be adapted to start in any given "Area", by adding a *default configuration* within Site settings and an *override* option within the component.

### Step 3 - Make Title Page configurable
Currently the first page of a PDF is hard coded. In the current Jahia layout the title page consists of a background image and centralized positioning of document title and subtitle.<br/>
We should build the possibility of configuring the title page.

### Step 4 - Extend Styles Configuration
The configuration of styles and mapping of styles to HTML tags is very user friendly. However, it's limited to some simple styling, like font, font-size, etc.<br/>
We should extend the style configuration with things like *background-image*, *lines (top/bottom)* and additional elements required 


