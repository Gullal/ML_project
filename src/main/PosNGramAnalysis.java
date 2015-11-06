package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class PosNGramAnalysis 
{
	
	static MaxentTagger tagger = new MaxentTagger("models/english-left3words-distsim.tagger");
	
	public static void main(String args[])
	{
//		BiGramPos();
		
		TriGramPos();
	}
	
	public static void TriGramPos()
	{
		LinkedHashMap<String,Integer> posTrigrams = new LinkedHashMap<>();
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("output/triGrams.txt"));
			
			String line;
			
			while((line = br.readLine()) != null)
			{
				String trigram = line.substring(0, line.indexOf(":"));
				
				String tagdTrigram = tagger.tagString(trigram);
				
				String posTrigram = "";
				
				String[] triplet = tagdTrigram.split(" ");
				
				posTrigram = posTrigram + triplet[0].substring(triplet[0].indexOf("_")+1) +" ";
	
				posTrigram = posTrigram + triplet[1].substring(triplet[1].indexOf("_")+1) +" ";
				
				posTrigram = posTrigram + triplet[2].substring(triplet[2].indexOf("_")+1);
	
				if(!posTrigrams.containsKey(posTrigram))
					posTrigrams.put(posTrigram, 1);
				else
					posTrigrams.put(posTrigram, posTrigrams.get(posTrigram)+1);
			}
			
			br.close();
			
			posTrigrams = PreProcess.sortMapByValues(posTrigrams);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter("output/posTrigrams.txt"));
			
			for(Map.Entry<String, Integer> entry: posTrigrams.entrySet() )
	        {
	//        	if(entry.getValue() >= 10)
	        	{
	        		bw.write(entry.getKey()+" "+entry.getValue()+"\n");
	        	}
	        }
			
			bw.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	
	public static void BiGramPos()
	{
		LinkedHashMap<String,Integer> posBigrams = new LinkedHashMap<>();
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("output/biGrams.txt"));
			
			String line;
			
			while((line = br.readLine()) != null)
			{
				String bigram = line.substring(0, line.indexOf(":"));
				
				String tagdBigram = tagger.tagString(bigram);
				
				String posBigram = "";
				
				String[] pair = tagdBigram.split(" ");
				
				posBigram = posBigram + pair[0].substring(pair[0].indexOf("_")+1) +" ";

				posBigram = posBigram + pair[1].substring(pair[1].indexOf("_")+1);

				if(!posBigrams.containsKey(posBigram))
					posBigrams.put(posBigram, 1);
				else
					posBigrams.put(posBigram, posBigrams.get(posBigram)+1);
			}
			
			br.close();
			
			posBigrams = PreProcess.sortMapByValues(posBigrams);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter("output/posBigrams.txt"));
			
			for(Map.Entry<String, Integer> entry: posBigrams.entrySet() )
	        {
//	        	if(entry.getValue() >= 10)
	        	{
	        		bw.write(entry.getKey()+" "+entry.getValue()+"\n");
	        	}
	        }
			
			bw.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
