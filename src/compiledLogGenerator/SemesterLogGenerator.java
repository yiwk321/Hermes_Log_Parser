package compiledLogGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import collectors.Collector;
import dataStorage.SuitesAndTests;
import tools.LogReader;
import tools.LogWriter;
import tools.TestingData;

public class SemesterLogGenerator {

	private static final String [] baseCatagories={	
			"Name",							//0
		  	"Num times tested",				//1
		  	"First test date",				//2
		  	"Last test date",				//3
		  	};	

	 
	private final boolean enableBaseCategories;
	private Collector [] collectors;
	private String outputFileName;
	
	
	public SemesterLogGenerator(Collector [] collectors, boolean enableBaseCategories) {
		this(collectors,enableBaseCategories,"assignment#.csv");
	}
	
	public SemesterLogGenerator(Collector [] collectors, boolean enableBaseCategories,String outputName) {
		this.enableBaseCategories=enableBaseCategories;
		this.collectors=collectors;
		outputFileName=outputName;
	}
	
	/**
	 * Use a # to represent the assignment number
	 */
	public void setAssignmentOutput(String outputName) {
		outputFileName=outputName;
	}
	
	public void setCollectors(Collector [] collectors) {
		this.collectors=collectors;
	}
	
	public void readData(File location, File output) throws IOException, ParseException{
		
		LogReader logReader = new LogReader(location);
		
		for(int assignmentIndex=0;assignmentIndex<logReader.getNumAssignments();assignmentIndex++){
			
			List<List<String>> allAssignmentLines = logReader.getAllStudentsAssignmentLogLines(assignmentIndex);
			if(allAssignmentLines==null)
				continue;
			
			List<String> studentNames = logReader.getStudentNames();
			
			SuitesAndTests assignmentSuitesAndTests = TestingData.findAllSuitesAndTests(allAssignmentLines,assignmentIndex);
			
			String [] tests = new String[assignmentSuitesAndTests.getTests().size()];
			assignmentSuitesAndTests.getTests().toArray(tests);
			
			CollectorManager dataCollection = new CollectorManager();
			
			for(Collector collector:collectors) {
				if(collector.requiresTestNames()) 
					collector.setTestNames(tests);
				dataCollection.addCollector(collector);
			}
			
			
			List<String> dataCategories = new ArrayList<String>();
			
			if(enableBaseCategories)
				Collections.addAll(dataCategories, baseCatagories);
			
			dataCategories.addAll(dataCollection.getOrderedHeaders());

			//File Writing and output
			String path=output.toString()+"/"+outputFileName.replaceAll("#", Integer.toString(assignmentIndex));
			File outputData=new File(path);
			outputData.createNewFile();
			FileWriter dataOut=new FileWriter(outputData);
			
			LogWriter.writeToFile(dataOut,dataCategories);
			
			//Data processing for each student
			for(int i=0;i<allAssignmentLines.size();i++){
				
				try{
					dataCollection.processLog(allAssignmentLines.get(i),1);

					String studentName=studentNames.get(i);

					List<String> resultsList;
					
					//Name cannot be gotten from a collector, The other base categories could be collectors
					if(enableBaseCategories) {
						resultsList = new ArrayList<String>();
						resultsList.add(studentName);
						resultsList.add(Integer.toString(allAssignmentLines.get(i).size()));
						resultsList.add(allAssignmentLines.get(i).get(0).split(",")[1]);
						resultsList.add(allAssignmentLines.get(i).get(allAssignmentLines.get(i).size()-1).split(",")[1]);
						resultsList.addAll(dataCollection.getOrderedData());
					}else 
						resultsList = dataCollection.getOrderedData();
					
					
					dataCollection.reset();
				
					if(dataCollection.specialPrint()) 
						LogWriter.simpleWrite(dataOut,resultsList);
					else
						LogWriter.writeToFile(dataOut,resultsList);
				
				}catch(Exception e){
					e.printStackTrace();
					System.out.println(studentNames.get(i) + "   " + path);
					dataCollection.reset();
					return;
				}
			}
			dataOut.close();
		}
		
	}
	
}
