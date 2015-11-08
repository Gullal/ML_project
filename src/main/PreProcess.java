package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PreProcess 
{
	static HashSet<String> uniGrams;
	static HashSet<String> biGrams;
	static HashSet<String> triGrams;
	static String regExp = "([a-z]+)((\\')|(?:-))*([a-z]+)";
	static Pattern pat = Pattern.compile(regExp);
	
	public static void main(String args[]) throws IOException
	{
		
//		String[] orgFiles = {"en_US.news.txt","en_US.blogs.txt"};
//		String[] dataFiles = {"training.txt","testing.txt"};
		
//		mergeAndDivideData(orgFiles);	
		
//		generateUniGrams();
		
//		generateBiGrams();
		
		generateTriGrams();
		
	}
	
	public static void loadUniGrams() 
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("output/testUniGrams.txt"));
			
			String line;
			
			uniGrams = new HashSet<>();
			
			while((line = br.readLine()) != null)
			{
				uniGrams.add(line.split(":")[0]);
			}
			
			br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
	}
	
	public static void loadBiGrams()
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("output/testBiGrams.txt"));
			
			String line;
			
			biGrams = new HashSet<>();
			
			while((line = br.readLine()) != null)
			{
				biGrams.add(line.substring(0, line.indexOf(":")));
			}
			
			br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		System.out.println("BiGrams Loaded");
	}
	
	public static void mergeAndDivideData(String[] filenames)
	{
		try
		{
			BufferedWriter bw1 = new BufferedWriter(new FileWriter("data/training.txt"));
			BufferedWriter bw2 = new BufferedWriter(new FileWriter("data/testing.txt"));
			
			for(int i=0;i<filenames.length;i++)
			{
				BufferedReader br = new BufferedReader(new FileReader("data/"+filenames[i]));
				
				String line;
				
				ArrayList<String> lines = new ArrayList<>();
				
				while((line = br.readLine()) != null)
				{
					lines.add(line);
				}
				
				br.close();
				
				int breakpt = lines.size()*70/100;
				
				for(int j=0;j<lines.size();j++)
				{
					if(j <= breakpt)
						bw1.write(lines.get(j)+"\n");
					else
						bw2.write(lines.get(j)+"\n");
				}
			}
			
			bw1.close();
			bw2.close();
		}	
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		System.out.println("Data Division Done");
	}
	
	public static void generateTriGrams()
	{
		loadBiGrams();
	
		LinkedHashMap<String,Integer> trigrams = new LinkedHashMap<>();
		
		String sentence;
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("data/testing.txt"));
			
			while((sentence = br.readLine()) != null)
			{
				sentence = sentence.toLowerCase();
				
				Matcher match = pat.matcher(sentence);
				
				ArrayList<String> wordsPerLine = new ArrayList<>();
				
				String term;
				
				while(match.find())
				{
					term = sentence.substring(match.start(),match.end());
					wordsPerLine.add(term);
				}
			
				
				for(int i = 0; i<wordsPerLine.size()-2; i++)
				{
	
					String pair = wordsPerLine.get(i)+" "+wordsPerLine.get(i+1);
					String tw = wordsPerLine.get(i+2);
					
					if(biGrams.contains(pair))
					{
						if(!trigrams.containsKey(pair+" "+tw))
							trigrams.put(pair+" "+tw, 1);
						else
							trigrams.put(pair+" "+tw, trigrams.get(pair+" "+tw)+1);
					}
				}
			}
			br.close();
			
			LinkedHashMap<String,Integer> trigrams2 = new LinkedHashMap<>();
			for(Map.Entry<String, Integer> entry: trigrams.entrySet() )
	        {
	        	if(entry.getValue() > 5)
	        	{
	        		trigrams2.put(entry.getKey(), entry.getValue());
	        	}
	        }
			
			trigrams = sortMapByValues(trigrams2);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter("output/testTriGrams.txt"));
			
			for(Map.Entry<String, Integer> entry: trigrams.entrySet() )
	        {
	        	bw.write(entry.getKey()+":"+entry.getValue()+"\n");  	
	        }
			
			bw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		System.out.println("TriGrams Done");
	}
	
	public static void generateBiGrams()
	{
		loadUniGrams();
		
		LinkedHashMap<String,Integer> bigrams = new LinkedHashMap<>();
		
		String sentence;
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("data/testing.txt"));
			
			while((sentence = br.readLine()) != null)
			{
				sentence = sentence.toLowerCase();
				
				Matcher match = pat.matcher(sentence);
				
				ArrayList<String> wordsPerLine = new ArrayList<>();
				
				String term;
				
				while(match.find())
				{
					term = sentence.substring(match.start(),match.end());
					wordsPerLine.add(term);
				}
			
				
				for(int i = 0; i<wordsPerLine.size()-1; i++)
				{

					String fw = wordsPerLine.get(i);
					String sw = wordsPerLine.get(i+1);
					
					if(uniGrams.contains(fw))
					{
						if(!bigrams.containsKey(fw+" "+sw))
							bigrams.put(fw+" "+sw, 1);
						else
							bigrams.put(fw+" "+sw, bigrams.get(fw+" "+sw)+1);
					}
				}
			}
			
			br.close();
			
			bigrams = sortMapByValues(bigrams);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter("output/testBiGrams.txt"));
			
			for(Map.Entry<String, Integer> entry: bigrams.entrySet() )
	        {
	        	if(entry.getValue() > 9)
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
		
		System.out.println("BiGrams Done");
	}
	
	public static void generateUniGrams() 
	{
		String sentence;
		
        LinkedHashMap<String,Integer> wordList = new LinkedHashMap<>();
        
        try
        {
        	BufferedReader br = new BufferedReader(new FileReader("data/training.txt"));
			
			while((sentence = br.readLine()) != null)
			{
				sentence = sentence.toLowerCase();
				
				Matcher match = pat.matcher(sentence);
				
				ArrayList<String> wordsPerLine = new ArrayList<>();
				
				String term;
				
				while(match.find())
				{
					term = sentence.substring(match.start(),match.end());
					wordsPerLine.add(term);
				}

	            for(String word: wordsPerLine)
	            {
	        		if(!wordList.containsKey(word))
	        			wordList.put(word, 1);
	        		else
	        			wordList.put(word, wordList.get(word)+1);
	            }
			}
			
			br.close();
	        
	        wordList = sortMapByValues(wordList);
	       
	        BufferedWriter bw = new BufferedWriter(new FileWriter("output/uniGrams.txt"));
	        
	        for(Map.Entry<String, Integer> entry: wordList.entrySet() )
	        {
	        	if(entry.getValue() >= 10)
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
        
        System.out.println("UniGrams Done");
	}
	
	public static LinkedHashMap<String,Integer> sortMapByValues(LinkedHashMap<String,Integer> map)
    {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());
 
        Collections.sort(list, (Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) -> (o2.getValue()).compareTo(o1.getValue()));
 
        LinkedHashMap<String,Integer> sortedMap = new LinkedHashMap<>();
        
        for (Map.Entry<String, Integer> entry : list) 
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        
        return sortedMap;
    }
}
