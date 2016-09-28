# Search (Information Retrieval) Assignment #1

In this project, we use Apache Lucene to generate an index, and test different analyzers such as:

  - StandardAnalyzer
  - SimpleAnalyzer
  - StopAnalyzer
  - KeywordAnalyzer

The dataset used is: Associated Press - 89

## Structure of the code

- IndexComparison.java: Creates indexes using different analyzers, and shows some statistics about them.
- Indexer.java: Generates an index with "DOCNO", "HEAD", "BYLINE", "DATELINE", "TEXT" fields, given an input directory containing *.trectext files.
- DocumentReader.java: Reads *.trectext files from a directory and provides one doc at a time via the getNextDoc() method.
- XMLInputStream.java: Provides a Stream with cleaned XML. (From: http://stackoverflow.com/a/8007565)

The code is a Eclipse project and can be imported directly. To run the project, export it as an executable jar named Assignment1.jar, with IndexComparison as the entry point.

Run it as follows:
```
$ java -jar Assignment1.jar index_dir input_dir
```
(without the trailing slash for the directory names)

## Answers

### Task 1: Generating Lucene Index for Experiment Corpus (AP89)

1. How many documents are there in this corpus? **84474**

2. Why different fields are treated with different kinds of java class? i.e., StringField and TextField are used for different fields in this example, why?

### Task 2: Test different analyzers

| Analyzer | Tokenization Applied | How many tokens are there for the field? | Stemming applied? | Stopwords removed? | How many terms are there in the dictionary? |
| :---: |:-------------:| :-----:| :---:| :---:| :---:|
| KeywordAnalyzer | No | 84474 | No | No | 84061 |
| SimpleAnalyzer | Yes | 37316074 | No | No | 169981 |
| StopAnalyzer | Yes | 26202405 | Yes | Yes | 169948 |
| StandardAnalyzer | Yes | 26635610 | Yes | No | 233384 |

## Issues:
- Read error when reading AP890520.trectext (MalformedByteSequenceException: Invalid byte 2 of 2-byte UTF-8 sequence)
  - Solution: Copy-paste the contents of the file into a new file, and overwrite the old file. This is an issue with the corpus, and not the program itself.

## References:
This project uses some 3rd party code:

* http://stackoverflow.com/a/8007565 to fix bad XML
* Parsing XML-like data without a root http://stackoverflow.com/a/11227161
* https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/ for reference

