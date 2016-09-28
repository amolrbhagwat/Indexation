package com.amol.search.indexation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DocumentReader {
	private ArrayList<String> filenames;
	private int currentFileIndex = 0;
	private NodeList docsInCurrentFile;
	private int currentDocIndex = 0;
	private int noOfFilesRead = 0;
	
	public DocumentReader(String inputPath) throws Exception{
		filenames = new ArrayList<>();
		File inputDir = new File(inputPath);
		
		if(inputDir.exists() && inputDir.isDirectory()){
			listFilesInDir(inputDir, ".trectext");	
		}
		
		File firstFile = new File(filenames.get(0));
		loadFromFile(firstFile);
		
	}
	
	private void listFilesInDir(File dir, String extension) throws Exception{
		for(File file : dir.listFiles()){
			if(extension == null || extension == ""){
				filenames.add(file.getAbsolutePath());
			}
			if(file.getName().endsWith(extension)){
				filenames.add(file.getAbsolutePath());
			}
		}
	}
	
	private boolean loadFromFile(File file) throws Exception{
		// This method loads all the documents from a trectext
		// In order to avoid the runtime error "The markup in the document following the root element must be well-formed."
		// adding a root element to the stream
		// Idea from: http://stackoverflow.com/a/11227161
		// XML parsing reference from: https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
		
		if(!file.exists() || !file.isFile()){
			return false;
		}
		

		FileInputStream fis = new FileInputStream(file);
		XMLInputStream xis = new XMLInputStream(fis);
		System.out.println("Reading: " + file.getName());
		noOfFilesRead++;
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		
		Enumeration<InputStream> streams = Collections.enumeration(
			Arrays.asList(new InputStream[] { //StandardCharsets.US_ASCII
				new ByteArrayInputStream("<root>".getBytes()),
			    xis,
			    new ByteArrayInputStream("</root>".getBytes()),
			}));
		SequenceInputStream seqStream = new SequenceInputStream(streams);
		
		Document doc = dBuilder.parse(seqStream, "UTF-8");
		
		doc.getDocumentElement().normalize();
		
		docsInCurrentFile = doc.getElementsByTagName("DOC");
		return true;
	}
	
	public Node getNextDoc() throws Exception{
		if(currentDocIndex < docsInCurrentFile.getLength()){
			return docsInCurrentFile.item(currentDocIndex++);
		}
		else{
			if(currentFileIndex < filenames.size()-1){
				currentDocIndex = 0;
				if(!loadFromFile(new File(filenames.get(++currentFileIndex)))){
					return null;
				}
				if(currentDocIndex < docsInCurrentFile.getLength()){
					return docsInCurrentFile.item(currentDocIndex++);
				}
				else{
					return null;
				}
			}
			else{
				return null;
			}
		}
	}
	
	public int getNoOfFilesRead(){
		return noOfFilesRead;
	}
	
}
