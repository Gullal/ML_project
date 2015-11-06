package main;

import java.io.*;
import java.util.*;

public class ComputeProbability 
{
	static HashMap<String,Integer> grams;
	
	public static void main(String args[])
	{

			readGrams("posBigrams.txt");
			System.out.println("\nGrams Loaded!");
			
//			readFileAndCompute("possibleTagsForPOSBigrams.txt","probabilityPosBiGrams.txt",grams);
			
			readFileAndCompute("possibleTagsForPOSTrigrams.txt","probabilityPosTriGrams.txt",grams);
			
//			readFileAndCompute("possibleWordsForBigrams.txt","probabilityBiGrams.txt",grams);
			
//			readFileAndCompute("possibleWordsForTrigrams.txt","probabilityTriGrams.txt",grams);

	}
	
	public static void readGrams(String filename)
	{
		try
		{
			String line;
			BufferedReader bfr = new BufferedReader(new FileReader("output/"+filename));
			grams = new HashMap<>();
			
			while((line = bfr.readLine()) != null)
			{
				String[] parts = line.split(":");
				grams.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
			}
			bfr.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void readFileAndCompute(String filename,String outfile, HashMap<String,Integer> gramList)
	{
		HashMap<String, ArrayList<Double>> probMap = new HashMap<>();
		
		try
		{
			PrintWriter pwOut = new PrintWriter("output/"+outfile);
			String line;
			String currentKey = null;
			double prob = 0.0;
			BufferedReader bfr = new BufferedReader(new FileReader("output/"+filename));
			while((line = bfr.readLine()) != null)
			{
				if(line.contains("\t"))
				{
					if(probMap.containsKey(currentKey))
					{
						ArrayList<Double> temp = probMap.get(currentKey);
						if(gramList.containsKey(line.split(":")[0].trim())){
							 //System.out.println(unigramList.get(line.split(":")[0].trim()));
							 prob = Double.parseDouble(line.split(":")[1].trim())/gramList.get(line.split(":")[0].trim());
							 
							 temp.add(prob);
							 probMap.put(currentKey,temp);
						}
					}			
				}
				else
				{
					currentKey = line.split(":")[0];
					ArrayList<Double> temp = new ArrayList<Double>();
					probMap.put(currentKey, temp);
				}
			}
			for(Map.Entry<String,ArrayList<Double>> entry : probMap.entrySet())
			{
				pwOut.append(entry.getKey() +" : "+entry.getValue()+"\n");
			}
			pwOut.close();
			bfr.close();
		}
		catch(IOException ie)
		{
			ie.printStackTrace();
		}
	}
	
}
