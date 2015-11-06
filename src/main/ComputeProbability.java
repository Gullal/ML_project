package main;

import java.io.*;
import java.util.*;

public class ComputeProbability 
{
	public static void main(String args[])
	{
		try
		{
			String line;
			BufferedReader bfr = new BufferedReader(new FileReader("H:/SS_IIIT/Workspace/NGramModel/output/wordList.txt"));
			HashMap<String, Integer> uniGrams = new HashMap<>();
			while((line = bfr.readLine()) != null)
			{
				String[] parts = line.split(":");
				uniGrams.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
			}
			System.out.println("\nUnigrams Loaded!");
			readFileAndCompute("possibleNextWordsForBigrams.txt",uniGrams);
			bfr.close();
		}
		catch(IOException ie)
		{
			ie.printStackTrace();
		}
	}
	
	public static void readFileAndCompute(String filename, HashMap<String,Integer> unigramList)
	{
		HashMap<String, ArrayList<Double>> probMap = new HashMap<>();
		
		try
		{
			PrintWriter pwOut = new PrintWriter("H:/SS_IIIT/Workspace/NGramModel/output/ProbabilityWords.txt");
			String line;
			String currentKey = null;
			double prob = 0.0;
			BufferedReader bfr = new BufferedReader(new FileReader("H:/SS_IIIT/Workspace/NGramModel/output/"+filename));
			while((line = bfr.readLine()) != null)
			{
				if(line.contains("\t"))
				{
					if(probMap.containsKey(currentKey))
					{
						ArrayList<Double> temp = probMap.get(currentKey);
						if(unigramList.containsKey(line.split(":")[0].trim())){
							 //System.out.println(unigramList.get(line.split(":")[0].trim()));
							 prob = Double.parseDouble(line.split(":")[1].trim())/unigramList.get(line.split(":")[0].trim());
							 
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
			
		}
	}
	
}
