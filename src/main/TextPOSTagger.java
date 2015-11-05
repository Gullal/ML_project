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
		
		posTagWords("training.txt","posTrigrams.txt",3);
	}
	
	public static void posTagWords(String filename,String outputFile, int order)
	{
		//storing pairs of POS tags and their occurrences
		LinkedHashMap<String,Integer> posNGrams = new LinkedHashMap<String, Integer>();
		
		long lineCount = 1;

		try
		{
			PrintWriter outputPOS = new PrintWriter("H:/SS_IIIT/Workspace/NGramModel/output/"+outputFile);
			BufferedReader bfr = new BufferedReader(new FileReader("H:/SS_IIIT/Workspace/NGramModel/data/"+filename));
			String line;
			MaxentTagger tagger =  new MaxentTagger("H:/SS_IIIT/Workspace/NGramModel/models/english-left3words-distsim.tagger");
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

	public static void findProbableNextWords(String filename)
	{
		HashMap<String,ArrayList<String>> nextWords = new HashMap<String,ArrayList<String>>();
		try
		{
			BufferedReader bfr = new BufferedReader(new FileReader("data/"+filename));
			String line;

			while((line = bfr.readLine()) != null)
			{
				String str = line.split(":")[0];
				String[] wordPair = str.split(" ");
				if(!nextWords.containsKey(wordPair[0]))
				{
					ArrayList<String> temp = new ArrayList<String>();
					temp.add(wordPair[1]);
					nextWords.put(wordPair[0],temp);
				}
				else
				{
					ArrayList<String> temp = nextWords.get(wordPair[0]);
					temp.add(wordPair[1]);
					nextWords.put(wordPair[0],temp);
				}
			}

		}
		catch(IOException ie)
		{
			ie.printStackTrace();
		}
	}
}