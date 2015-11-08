package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class FinalModel 
{
	static HashMap<String,LinkedHashMap<String,Double>> gramsRS;
	static HashMap<String,LinkedHashMap<String,Double>> posGramRS;
	static HashMap<String,LinkedHashMap<String,Double>> finalModel;
	static MaxentTagger tagger;
	
	public static void main(String args[]) throws IOException
	{
//		readProbFiles("mergedProbBiGrams.txt",1);
//		readProbFiles("mergedPosBiGrams.txt",2);
//		storeFinalMap(1);
		readProbFiles("mergedProbTriGrams.txt",1);
		readProbFiles("mergedPosTriGrams.txt",2);
		storeFinalMap(2);
		
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		String line = br.readLine();
		
	}
	
	public static void readProbFiles(String filename, int type)
	{
		HashMap<String,LinkedHashMap<String,Double>> finalRS = new HashMap<>();
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("output/"+filename));
			String line = null;
			String currWord = null;
			
			while((line = br.readLine())!=null)
			{
				LinkedHashMap<String,Double> temp = new LinkedHashMap<>();
				if(line.contains(":"))
				{
					currWord = line.split(":")[0].trim();
				}
				else if(line.isEmpty())
				{
					continue;
				}
				else
				{
					String[] pair = line.split(" - ");
					temp = finalRS.get(currWord);
					temp.put(pair[0].trim(), Double.parseDouble(pair[1].trim()));
				}
				
				finalRS.put(currWord, temp);
			}
		
			br.close();
			
			if(type == 1)
			{
				gramsRS = finalRS;
				System.out.println("grams Result Set loaded");
			}
			else
			{
				posGramRS = finalRS;
				System.out.println("PosGrams Result Set Loaded");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void storeFinalMap(int type)
	{
		tagger = new MaxentTagger("models/english-left3words-distsim.tagger");
		
		finalModel = new HashMap<>();
		
		for(Map.Entry<String,LinkedHashMap<String,Double>> entry : gramsRS.entrySet())
		{
//			String tag1 = tagger.tagString(entry.getKey()).split("_")[1].substring(0,3).trim();
			
			String[] tmp = tagger.tagString(entry.getKey()).split(" ");
			tmp[0] = tmp[0].split("_")[1].trim();
			tmp[1] = tmp[1].split("_")[1].trim();
			String tag1 = tmp[0]+" "+tmp[1];
			LinkedHashMap<String,Double> temp = posGramRS.get(tag1);
			
			if(temp != null)
			{
				for (Map.Entry<String, Double> entry2: entry.getValue().entrySet())
				{
					double prob0 = entry2.getValue();
					String tag2 = tagger.tagString(entry2.getKey()).split("_")[1].substring(0,3).trim();
					
					double prob1;
					if(temp.get(tag2) != null)
						prob1 = temp.get(tag2);
					else
						prob1 = 0;
				
					double finalProb = 0.7 * prob0 + 0.3 * prob1;
					
					LinkedHashMap<String,Double> temp2;
					
					if(!finalModel.containsKey(entry.getKey()))
						temp2 = new LinkedHashMap<>();
					else
						temp2 = finalModel.get(entry.getKey());
		
					temp2.put(entry2.getKey(), finalProb);
					finalModel.put(entry.getKey(), temp2);
				}
			}
		}
		
		String file;
		try
		{
			if (type == 1)
				file = "finalBiGramModel.txt";
			else
				file = "finalTriGramModel.txt";
			
			BufferedWriter bw = new BufferedWriter(new FileWriter("output/"+file));
			
			for(Map.Entry<String,LinkedHashMap<String,Double>> entry : finalModel.entrySet())
			{
				bw.write(entry.getKey()+":\n");
				LinkedHashMap<String,Double> sortedRS = ComputeProbability.sortMapByValues(entry.getValue());
				
				for(Map.Entry<String,Double> entry2 : sortedRS.entrySet())
				{
					bw.write(entry2.getKey()+" - "+Math.round(entry2.getValue()*10000.0)/10000.0+"\n");
				}
				bw.write("\n");
			}
			
			bw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
