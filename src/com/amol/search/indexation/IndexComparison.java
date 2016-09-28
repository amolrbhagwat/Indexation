package com.amol.search.indexation;

import java.nio.file.Paths;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class IndexComparison {
	static String indexPath;
	
	public static void main(String args[]) throws Exception{
		if(args.length != 2){ 
	      System.out.println("Incorrect number of arguments!"); 
	      System.out.println("Parameters are: index_dir input_dir"); 
	      System.exit(1); 
	    } 
	 
	    indexPath = args[0]; 
	    String inputPath = args[1];
	    
	    Indexer.generateIndex(indexPath + "/keyword", new DocumentReader(inputPath), new KeywordAnalyzer());
	    Indexer.generateIndex(indexPath + "/simple", new DocumentReader(inputPath), new SimpleAnalyzer());
	    Indexer.generateIndex(indexPath + "/stop", new DocumentReader(inputPath), new StopAnalyzer());
	    Indexer.generateIndex(indexPath + "/standard", new DocumentReader(inputPath), new StandardAnalyzer());
	    
	    showStats();
	}
	
	public static void showStats() throws Exception{
		IndexReader keywordReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath + "/keyword")));
		IndexReader simpleReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath + "/simple")));	
		IndexReader stopReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath + "/stop")));	
		IndexReader standardReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath + "/standard")));	
		
		System.out.println("\nUsing KeywordAnalyzer");
		Terms keywordVocabulary = MultiFields.getTerms(keywordReader, "TEXT");
		System.out.println("Number of tokens: " + keywordVocabulary.getSumTotalTermFreq());
		System.out.println("Number of terms : " + countNoOfTerms(keywordVocabulary));
		
		System.out.println("\nUsing SimpleAnalyzer");
		Terms simpleVocabulary = MultiFields.getTerms(simpleReader, "TEXT");
		System.out.println("Number of tokens: " + simpleVocabulary.getSumTotalTermFreq());
		System.out.println("Number of terms : " + countNoOfTerms(simpleVocabulary));
		
		System.out.println("\nUsing StopAnalyzer");
		Terms stopVocabulary = MultiFields.getTerms(stopReader, "TEXT");
		System.out.println("Number of tokens: " + stopVocabulary.getSumTotalTermFreq());
		System.out.println("Number of terms : " + countNoOfTerms(stopVocabulary));
		
		System.out.println("\nUsing StandardAnalyzer");
		Terms standardVocabulary = MultiFields.getTerms(standardReader, "TEXT");
		System.out.println("Number of tokens: " + standardVocabulary.getSumTotalTermFreq());
		System.out.println("Number of terms : " + countNoOfTerms(standardVocabulary));
		
		
		keywordReader.close();
		simpleReader.close();
		stopReader.close();
		standardReader.close();
	}
	
	static long countNoOfTerms(Terms vocabulary) throws Exception{
		long count = 0;
		
		TermsEnum iterator = vocabulary.iterator();
		BytesRef byteRef;
		
		while((byteRef = iterator.next()) != null) {
			count++;
		}
		
		return count;
	}
}
