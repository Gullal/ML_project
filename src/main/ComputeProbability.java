package main;

import java.io.*;
import java.util.*;

class WordModel
{
	public String word;
	public double probability;
}

public class ComputeProbability 
{
	static HashMap<String,Integer> grams;
	static HashMap<String, ArrayList<WordModel>> probMap = new HashMap<>();
	
	public static void main(String args[])
	{

//			readGrams("uniGrams.txt");
//			readGrams("biGrams.txt");
//			readGrams("posUnigrams.txt");
		readGrams("posBigrams.txt");
			System.out.println("\nGrams Loaded!");
			
//			readFileAndCompute("possibleTagsForPOSBigrams.txt","probabilityPosBiGrams.txt",grams);
			
//			readFileAndCompute("possibleTagsForPOSTrigrams.txt","probabilityPosTriGrams.txt",grams);
			
//			readFileAndCompute("possibleWordsForBigrams.txt","probabilityBiGrams.txt",grams);
			
//			mergeFiles("biGrams.txt","mergedProbBiGrams.txt");
			
//			readFileAndCompute("possibleWordsForTrigrams.txt","probabilityTriGrams.txt",grams);
			
//			mergedFiles2("triGrams.txt","mergedProbTriGrams.txt");
			
//			readFileAndCompute("possibleTagsForPOSBigrams.txt","probabilityPosBiGrams.txt",grams);
			
//			mergeFiles("posBigrams.txt","mergedPosBiGrams.txt");
		
//		readFileAndCompute("possibleTagsForPOSTrigrams.txt","probabilityPosTriGrams.txt",grams);
		
//		mergedFiles2("posTrigrams.txt","mergedPosTriGrams.txt");

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
		try
		{
			PrintWriter pwOut = new PrintWriter("output/"+outfile);
			String line;
			String currentKey = null;
			BufferedReader bfr = new BufferedReader(new FileReader("output/"+filename));
			while((line = bfr.readLine()) != null)
			{
				if(line.contains("\t"))
				{
					if(probMap.containsKey(currentKey))
					{
						ArrayList<WordModel> temp = probMap.get(currentKey);
						if(gramList.containsKey(line.split(":")[0].trim()))
						{
							WordModel wm = new WordModel();
							wm.word = line.split(":")[0];
							wm.probability  = Double.parseDouble(line.split(":")[1].trim())/gramList.get(line.split(":")[0].trim());
							temp.add(wm);
							probMap.put(currentKey,temp);
						}
					}			
				}
				else
				{
					currentKey = line.split(":")[0];
					ArrayList<WordModel> temp = new ArrayList<>();
					probMap.put(currentKey, temp);
				}
			}
			for(Map.Entry<String,ArrayList<WordModel>> entry : probMap.entrySet())
			{

					pwOut.append(entry.getKey() +" : ");
					for (WordModel t : entry.getValue())
					{
						pwOut.append(t.word + " - " +t.probability+"\n");
					}
					pwOut.append("\n");

			}
			pwOut.close();
			bfr.close();
		}
		catch(IOException ie)
		{
			ie.printStackTrace();
		}
		
		System.out.println("Prob Map Computed");
	}
	
	public static void mergedFiles2(String fileName, String outname)
	{
		String line;
		
		HashMap<String,LinkedHashMap<String,Double>> resultSet = new HashMap<>();
		
		try
		{
			PrintWriter pwOut = new PrintWriter("output/"+outname);
			BufferedReader br = new BufferedReader(new FileReader("output/"+fileName));

			while((line = br.readLine())!=null)
			{
				String triplet = line.split(":")[0];
				
				String[] tokenz = triplet.split(" ");
				String [] tokens = {tokenz[0].trim()+" "+tokenz[1].trim(), tokenz[2].trim()};
				
				ArrayList<WordModel> temp = probMap.get(tokens[1].trim());
				
				for(WordModel wm: temp)
				{
					if(wm.word.trim().equals(tokens[0].trim()))
					{
						LinkedHashMap<String,Double> temp2;
						if(!resultSet.containsKey(tokens[0]))
						{
							temp2 = new LinkedHashMap<>();
							temp2.put(tokens[1], wm.probability);
							resultSet.put(tokens[0],temp2 );		
						}
						else
						{
							temp2 = resultSet.get(tokens[0]);
							temp2.put(tokens[1], wm.probability);
							resultSet.put(tokens[0], temp2);
						}
					}
				}
			}
			
			for(Map.Entry<String,LinkedHashMap<String,Double>> entry : resultSet.entrySet())
			{
				pwOut.append(entry.getKey() +" :\n");
				LinkedHashMap<String,Double> sortedSet = sortMapByValues(entry.getValue());
				for (Map.Entry<String, Double> entry2: sortedSet.entrySet())
				{
					pwOut.append(entry2.getKey() + " - " +entry2.getValue()+"\n");
				}
				pwOut.append("\n");
			}
			
			br.close();
			pwOut.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void mergeFiles(String fileName,String outname)
	{
		String line;
		
		HashMap<String,LinkedHashMap<String,Double>> resultSet = new HashMap<>();
		
		try
		{
			PrintWriter pwOut = new PrintWriter("output/"+outname);
			BufferedReader br = new BufferedReader(new FileReader("output/"+fileName));

			while((line = br.readLine())!=null)
			{
				String pair = line.split(":")[0];
				
				String[] tokens = pair.split(" ");
				
				ArrayList<WordModel> temp = probMap.get(tokens[1]);
				
				for(WordModel wm: temp)
				{
					if(wm.word.trim().equals(tokens[0].trim()))
					{
						LinkedHashMap<String,Double> temp2;
						if(!resultSet.containsKey(tokens[0]))
						{
							temp2 = new LinkedHashMap<>();
							temp2.put(tokens[1], wm.probability);
							resultSet.put(tokens[0],temp2 );		
						}
						else
						{
							temp2 = resultSet.get(tokens[0]);
							temp2.put(tokens[1], wm.probability);
							resultSet.put(tokens[0], temp2);
						}
					}
				}
			}
			
			for(Map.Entry<String,LinkedHashMap<String,Double>> entry : resultSet.entrySet())
			{
				pwOut.append(entry.getKey() +" :\n");
				LinkedHashMap<String,Double> sortedSet = sortMapByValues(entry.getValue());
				for (Map.Entry<String, Double> entry2: sortedSet.entrySet())
				{
					pwOut.append(entry2.getKey() + " - " +entry2.getValue()+"\n");
				}
				pwOut.append("\n");
			}
			
			br.close();
			pwOut.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static LinkedHashMap<String,Double> sortMapByValues(LinkedHashMap<String,Double> map)
    {
        List<Map.Entry<String, Double>> list = new LinkedList<>(map.entrySet());
 
        Collections.sort(list, (Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) -> (o2.getValue()).compareTo(o1.getValue()));
 
        LinkedHashMap<String,Double> sortedMap = new LinkedHashMap<>();
        
        for (Map.Entry<String, Double> entry : list) 
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        
        return sortedMap;
    }
}
