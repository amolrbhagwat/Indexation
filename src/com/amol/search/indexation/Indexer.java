package com.amol.search.indexation;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class Indexer {

	public static void main(String args[]) throws Exception{
		String indexPath, inputPath;
				
		inputPath = "/home/amol/Documents/Search/Assignments/A1/corpus";
		indexPath = "/home/amol/Documents/Search/Assignments/A1/index";
		
		DocumentReader docReader = new DocumentReader(inputPath);
		
		generateIndex(indexPath, docReader);	
		
	}
	
	static void generateIndex(String indexPath, DocumentReader docReader) throws Exception{
		String[] fields = {"DOCNO", "HEAD", "BYLINE", "DATELINE", "TEXT"};
		
		Directory indexDir = FSDirectory.open(Paths.get(indexPath));
		
		IndexWriterConfig iwc = new IndexWriterConfig(new StandardAnalyzer());
		iwc.setOpenMode(OpenMode.CREATE);
		
		IndexWriter writer = new IndexWriter(indexDir, iwc);
		
		Node doc;
		while( (doc = docReader.getNextDoc()) != null){
			Map<String, String> fieldData = getFieldDataFromDoc(doc, fields);
			
			Document luceneDoc = new Document();
			
			if(fieldData.containsKey("DOCNO"))
				luceneDoc.add(new StringField("DOCNO", fieldData.get("DOCNO"), Field.Store.YES));
			if(fieldData.containsKey("HEAD"))
				luceneDoc.add(new StringField("HEAD", fieldData.get("HEAD"), Field.Store.YES));
			if(fieldData.containsKey("BYLINE"))
				luceneDoc.add(new StringField("BYLINE", fieldData.get("BYLINE"), Field.Store.YES));
			if(fieldData.containsKey("DATELINE"))
				luceneDoc.add(new StringField("DATELINE", fieldData.get("DATELINE"), Field.Store.YES));
			if(fieldData.containsKey("TEXT"))
				luceneDoc.add(new TextField("TEXT", fieldData.get("TEXT"), Field.Store.YES));
			
			writer.addDocument(luceneDoc);
		}
		
		writer.close();
		
		System.out.println("Indexed " + docReader.getNoOfFilesRead() + " files.");
	}
	
	static Map<String, String> getFieldDataFromDoc(Node doc, String[] fieldsToGet){
		Map<String, String> fieldData = new HashMap<>();
		
		NodeList children = doc.getChildNodes();
		for(String field : fieldsToGet){
			for(int i = 0; i < children.getLength(); i++){
				if(field.equalsIgnoreCase(children.item(i).getNodeName())){
					if(fieldData.containsKey(field)){
						fieldData.put(field, fieldData.get(field) + " " + children.item(i).getTextContent());
					}
					else{
						fieldData.put(field, children.item(i).getTextContent());
					}
				}
			}
		}

		return fieldData;
	}
	
}

