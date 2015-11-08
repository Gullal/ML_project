package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestDataDivision 
{
	public static void main(String args[])
	{
		HashMap<String,LinkedHashMap<String,Integer>> list = new HashMap<>();
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("output/testTriGrams.txt"));
			
			String line;
			
			while((line = br.readLine())!=null)
			{
				String[] pair = line.split(":")[0].split(" ");
				int cnt = Integer.parseInt(line.split(":")[1]);
				pair[0] = pair[0]+" "+pair[1];
				pair[1] = pair[2].trim();
				
				LinkedHashMap<String,Integer> temp;
				
				if(!list.containsKey(pair[0]))
				{
					temp = new LinkedHashMap<>();
				}
				else
				{
					temp = list.get(pair[0]);
				}
				temp.put(pair[1], cnt);
				list.put(pair[0], temp);
			}
			
			br.close();
			
			BufferedWriter bw = new BufferedWriter(new FileWriter("output/testTriGrams.txt"));
			
			for(Map.Entry<String,LinkedHashMap<String,Integer>> entry : list.entrySet())
			{
				bw.write(entry.getKey()+":\n");
				LinkedHashMap<String,Integer> sortedRS = PreProcess.sortMapByValues(entry.getValue());
				
				for(Map.Entry<String,Integer> entry2 : sortedRS.entrySet())
				{
					bw.write(entry2.getKey()+" - "+entry2.getValue()+"\n");
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
