package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class PosNGramAnalysis 
{
	
	static MaxentTagger tagger = new MaxentTagger("models/english-left3words-distsim.tagger");
	
	public static void main(String args[])
	{
//		UniGramPos();
		
//		BiGramPos();
		
//		TriGramPos();
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
				String[] trigram = line.split(":");
				
				String tagdTrigram = tagger.tagString(trigram[0]);
				
				String posTrigram = "";
				
				String[] triplet = tagdTrigram.split(" ");
				
				posTrigram = posTrigram + triplet[0].substring(triplet[0].indexOf("_")+1) +" ";
	
				posTrigram = posTrigram + triplet[1].substring(triplet[1].indexOf("_")+1) +" ";
				
				posTrigram = posTrigram + triplet[2].substring(triplet[2].indexOf("_")+1);
	
				if(!posTrigrams.containsKey(posTrigram))
					posTrigrams.put(posTrigram, Integer.parseInt(trigram[1].trim()));
				else
					posTrigrams.put(posTrigram, posTrigrams.get(posTrigram)+Integer.parseInt(trigram[1].trim()));
			}
			
			br.close();
			
			posTrigrams = PreProcess.sortMapByValues(posTrigrams);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter("output/posTrigrams.txt"));
			
			for(Map.Entry<String, Integer> entry: posTrigrams.entrySet() )
	        {
	//        	if(entry.getValue() >= 10)
	        	{
	        		bw.write(entry.getKey()+":"+entry.getValue()+"\n");
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
				String[] bigram = line.split(":");
				
				String tagdBigram = tagger.tagString(bigram[0]);
				
				String posBigram = "";
				
				String[] pair = tagdBigram.split(" ");
				
				posBigram = posBigram + pair[0].substring(pair[0].indexOf("_")+1) +" ";

				posBigram = posBigram + pair[1].substring(pair[1].indexOf("_")+1);

				if(!posBigrams.containsKey(posBigram))
					posBigrams.put(posBigram, Integer.parseInt(bigram[1].trim()));
				else
					posBigrams.put(posBigram, posBigrams.get(posBigram)+Integer.parseInt(bigram[1].trim()));
			}
			
			br.close();
			
			posBigrams = PreProcess.sortMapByValues(posBigrams);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter("output/posBigrams.txt"));
			
			for(Map.Entry<String, Integer> entry: posBigrams.entrySet() )
	        {
//	        	if(entry.getValue() >= 10)
	        	{
	        		bw.write(entry.getKey()+":"+entry.getValue()+"\n");
	        	}
	        }
			
			bw.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void UniGramPos()
	{
		LinkedHashMap<String,Integer> posUnigrams = new LinkedHashMap<>();
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("output/UniGrams.txt"));
			
			String line;
			
			while((line = br.readLine()) != null)
			{
				String[] unigram = line.split(":");
				
				String tagdUnigram = tagger.tagString(unigram[0]);
				
				String posUnigram  = tagdUnigram.substring(tagdUnigram.indexOf("_")+1,tagdUnigram.indexOf("_")+4);;

				if(!posUnigrams.containsKey(posUnigram))
					posUnigrams.put(posUnigram, Integer.parseInt(unigram[1].trim()));
				else
					posUnigrams.put(posUnigram, posUnigrams.get(posUnigram)+Integer.parseInt(unigram[1].trim()));
			}
			
			br.close();
			
			posUnigrams = PreProcess.sortMapByValues(posUnigrams);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter("output/posUnigrams.txt"));
			
			for(Map.Entry<String, Integer> entry: posUnigrams.entrySet() )
	        {
//	        	if(entry.getValue() >= 10)
	        	{
	        		bw.write(entry.getKey()+":"+entry.getValue()+"\n");
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
