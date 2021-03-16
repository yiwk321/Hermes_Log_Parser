package tools;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import dataStorage.SuitesAndTests;


public class TestingData {

	private static final int TEST_SUITE_INDEX=4,TEST_PASS_INDEX=5,TEST_PARTIAL_INDEX=6,TEST_FAIL_INDEX=7,TEST_UNTESTED_INDEX=8,TEST_SIZE=9;
	
	public static SuitesAndTests findAllSuitesAndTests(List<List<String>> assignmentData, int assignmentNumber) throws FileNotFoundException{
		List<String> suites=new ArrayList<String>();
		List<String> tests=new ArrayList<String>();
		
		Map<String,Integer> testCollector = new HashMap<String, Integer>();
		
		top:
		for(List<String> studentLogData:assignmentData){
			
			if(studentLogData.size()==0)
				continue;
			
			for(String suiteTested:studentLogData){
				String[]line=suiteTested.split(",");
				if(line.length != TEST_SIZE)
					continue top;
				if(!suites.contains(line[TEST_SUITE_INDEX]))
					suites.add(line[TEST_SUITE_INDEX]);
			}
			
			String [] lastLine=studentLogData.get(studentLogData.size()-1).replaceAll("\\+", "").replaceAll("-","").split(",");
			
			for(String testName:lastLine[TEST_PASS_INDEX].split(" "))
				if(testCollector.containsKey(testName))
					testCollector.replace(testName, testCollector.get(testName)+1);
				else
					testCollector.put(testName, 1);
			for(String testName:lastLine[TEST_PARTIAL_INDEX].split(" "))
				if(testCollector.containsKey(testName))
					testCollector.replace(testName, testCollector.get(testName)+1);
				else
					testCollector.put(testName, 1);
			for(String testName:lastLine[TEST_FAIL_INDEX].split(" "))
				if(testCollector.containsKey(testName))
					testCollector.replace(testName, testCollector.get(testName)+1);
				else
					testCollector.put(testName, 1);
			for(String testName:lastLine[TEST_UNTESTED_INDEX].split(" "))
				if(testCollector.containsKey(testName))
					testCollector.replace(testName, testCollector.get(testName)+1);
				else
					testCollector.put(testName, 1);
		}
		
		int halfNumStudents=assignmentData.size()/2;
		Iterator<Entry<String, Integer>> hashmapValues = testCollector.entrySet().iterator();
		while(hashmapValues.hasNext()){
			Entry<String,Integer> entry = hashmapValues.next();
			if(entry.getValue().intValue()>=halfNumStudents)
				tests.add(entry.getKey());
		}
		
		return new SuitesAndTests(suites,tests,assignmentNumber);
		
	}
	
	
	
	 
	
	
}


