package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

public class Test 
{
	static HashMap<String,LinkedHashSet<String>> biFinalModel;
	static HashMap<String,LinkedHashSet<String>> triFinalModel;
	static HashMap<String,LinkedHashSet<String>> testSet;
	
	public static void main(String args[])
	{
//		readFinalModels("finalBiGramModel.txt",1);
//		readFinalModels("testBiGrams.txt",3);
		
		readFinalModels("finalTriGramModel.txt",2);
		readFinalModels("testTriGrams.txt",4);
		
//		System.out.println(triFinalModel.size());
//		System.out.println(testSet.size());
			
		int cnt = 0;
		int total = 0;

		for(Map.Entry<String,LinkedHashSet<String>> entry : testSet.entrySet())
		{
			LinkedHashSet<String> test = entry.getValue();
			HashSet<String> tmp = new HashSet<>();
			int i = 0;
			
			Iterator<String> iter = test.iterator();
			while(iter.hasNext() && i < 5)
			{
				tmp.add(iter.next());
				i++;
			}
				
			iter = tmp.iterator();
			while(iter.hasNext())
			{
				LinkedHashSet<String> model = triFinalModel.get(entry.getKey());
				String st1 = iter.next();
				if(model !=null)
				{
					Iterator<String> iter2 = model.iterator();
					int j = 0;
	
					while(iter2.hasNext() && j < 5)
					{
						String st2 = iter2.next();
		
						if(st1.equals(st2))
						{
							cnt++;
							break;
						}
						j++;
					}
				}
				else if(st1.equals("the"))
					cnt++;
				
				total++;
			}
		}
		
		System.out.println("Total Correct Matches: "+cnt);
		System.out.println("Total Comparisons: "+total);
			
	}
	
	public static void readFinalModels(String filename, int type)
	{
		HashMap<String,LinkedHashSet<String>> finalRS = new HashMap<>();
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("output/"+filename));
			String line = null;
			String currWord = null;
			
			while((line = br.readLine())!=null)
			{
				LinkedHashSet<String> temp = new LinkedHashSet<>();
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
					temp.add(pair[0].trim());
				}
				
				finalRS.put(currWord, temp);
			}
		
			br.close();
			
			if(type == 1)
			{
				biFinalModel = finalRS;
				System.out.println("biGrams Result Set loaded");
			}
			else if(type == 2)
			{
				triFinalModel = finalRS;
				System.out.println("triGrams Result Set Loaded");
			}
			else if(type == 3)
			{
				testSet = finalRS;
				System.out.println("Test Bigrams Loaded");
			}
			else
			{
				testSet = finalRS;
				System.out.println("Test Trigrams Loaded");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
