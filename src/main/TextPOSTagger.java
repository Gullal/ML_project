package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class TextPOSTagger
{
	static String strPattern = "([a-z]+)((\\')|(?:-))*([a-z]+)";
	
	public static void main(String[] args)
	{
		//posTagWords("training.txt","posBigrams.txt",2);
		
		//posTagWords("training.txt","posTrigrams.txt",3);
		
//		findProbableNextWords("biGrams.txt", "possibleWordsForBigrams.txt");
		
//		findProbableNextWordsForBigrams("triGrams.txt", "possibleWordsForTrigrams.txt");
	}
	
	public static void posTagWords(String filename,String outputFile, int order)
	{
		//storing pairs of POS tags and their occurrences
		LinkedHashMap<String,Integer> posNGrams = new LinkedHashMap<String, Integer>();
		
		long lineCount = 1;

		try
		{
			PrintWriter outputPOS = new PrintWriter("output/"+outputFile);
			BufferedReader bfr = new BufferedReader(new FileReader("data/"+filename));
			String line;
			MaxentTagger tagger =  new MaxentTagger("models/english-left3words-distsim.tagger");
			while((line = bfr.readLine()) != null)
			{
				ArrayList<String> posOccurences = new ArrayList<String>();
				line = line.toLowerCase();
				Pattern pat = Pattern.compile(strPattern);
				Matcher match = pat.matcher(line);
				String w;

				while(match.find())
				{
					w = line.substring(match.start(),match.end());
					String tagged = tagger.tagString(w);
					String tag = tagged.split("_")[1].substring(0,2);
					posOccurences.add(tag);
				}

				if(order == 2)
				{
					for (int i=0; i<posOccurences.size()-1; i++)
					{
						int count = 1;
						if(!posNGrams.containsKey(posOccurences.get(i)+" "+posOccurences.get(i+1)))
						{
							posNGrams.put(posOccurences.get(i)+" "+posOccurences.get(i+1), count);
						}
						else
						{
							int inc = posNGrams.get(posOccurences.get(i)+" "+posOccurences.get(i+1))+1;
							posNGrams.put(posOccurences.get(i)+" "+posOccurences.get(i+1), inc);
						}
					}
				}
				else if (order == 3)
				{
					for(int j = 0; j<posOccurences.size()-2; j++)
					{
						int count = 1;
						if(!posNGrams.containsKey(posOccurences.get(j)+ " "+posOccurences.get(j+1)+" "+posOccurences.get(j+2)))
						{
							posNGrams.put(posOccurences.get(j)+ " "+posOccurences.get(j+1)+" "+posOccurences.get(j+2),count);
						}
						else
						{
							int incr = posNGrams.get(posOccurences.get(j)+ " "+posOccurences.get(j+1)+" "+posOccurences.get(j+2))+1;
							posNGrams.put(posOccurences.get(j)+ " "+posOccurences.get(j+1)+" "+posOccurences.get(j+2),incr);
						}
					}
				}
				System.out.println(lineCount++);
			}

			//sorting bigrams by occurrences
			LinkedHashMap<String, Integer> resultSorted = PreProcess.sortMapByValues(posNGrams);

			for (Map.Entry<String, Integer> entry : resultSorted.entrySet()) {
				//if(entry.getValue()>=10){
				outputPOS.append(entry.getKey()+" : "+entry.getValue()+"\n");
				//}
			}

			outputPOS.close();
			bfr.close();

		}
		catch(IOException ie)
		{
			ie.printStackTrace();
		}
	}

	public static void findProbableNextWords(String filename, String outputFile)
	{
		HashMap<String,LinkedHashMap<String,Integer>> nextWords = new HashMap<>();
		try
		{
			BufferedReader bfr = new BufferedReader(new FileReader("data/"+filename));
			PrintWriter pwOut = new PrintWriter("output/"+outputFile);
			String line;

			while((line = bfr.readLine()) != null)
			{
				String[] str = line.split(":");
				String[] wordPair = str[0].split(" ");
				LinkedHashMap<String,Integer> temp;
				if(!nextWords.containsKey(wordPair[1]))
				{
					 temp = new LinkedHashMap<>();
				}
				else
				{
					temp = nextWords.get(wordPair[1]);
				}
				
				temp.put(wordPair[0],Integer.parseInt(str[1].trim()));
				nextWords.put(wordPair[1],temp);
			}
			for(Map.Entry<String, LinkedHashMap<String,Integer>> entry : nextWords.entrySet())
			{
				pwOut.append(entry.getKey() + ":\n");
				LinkedHashMap<String,Integer> sortedWords = PreProcess.sortMapByValues(entry.getValue());
				for(Map.Entry<String, Integer> entry2 : sortedWords.entrySet())
				{
					pwOut.append("\t"+entry2.getKey() + " : "+entry2.getValue()+"\n");
				}
			}
			bfr.close();
			pwOut.close();
		}
		catch(IOException ie)
		{
			ie.printStackTrace();
		}
	}
	public static void findProbableNextWordsForBigrams(String filename, String outputFile)
	{
		HashMap<String,LinkedHashMap<String,Integer>> nextWordsForBigrams = new HashMap<String,LinkedHashMap<String,Integer>>();
		try
		{
			BufferedReader bfr = new BufferedReader(new FileReader("output/"+filename));
			PrintWriter pwOut = new PrintWriter("output/"+outputFile);
			String line;

			while((line = bfr.readLine()) != null)
			{
				String[] str = line.split(":");
				String[] wordPair = str[0].split(" ");
				String bigramKey = null;
				if(wordPair.length == 3)
				{
					bigramKey = wordPair[0]+" "+wordPair[1];
					if(!nextWordsForBigrams.containsKey(wordPair[2]))
					{
						LinkedHashMap<String,Integer> temp = new LinkedHashMap<String,Integer>();
						temp.put(bigramKey,Integer.parseInt(str[1].trim()));
						nextWordsForBigrams.put(wordPair[2],temp);
					}
					else
					{
						LinkedHashMap<String,Integer> temp = nextWordsForBigrams.get(wordPair[2]);
						temp.put(bigramKey,Integer.parseInt(str[1].trim()));
						nextWordsForBigrams.put(wordPair[2],temp);
					}
				}
				
				
			}
			for(Map.Entry<String, LinkedHashMap<String,Integer>> entry : nextWordsForBigrams.entrySet())
			{
				pwOut.append(entry.getKey() + " :\n");
				LinkedHashMap<String,Integer> sortedMap = PreProcess.sortMapByValues(entry.getValue());
				for(Map.Entry<String,Integer> word : sortedMap.entrySet())
				{
					pwOut.append("\t"+word.getKey()+" : "+ word.getValue()+"\n");
				}
			}
			bfr.close();
			pwOut.close();
		}
		catch(IOException ie)
		{
			ie.printStackTrace();
		}
	}
}

