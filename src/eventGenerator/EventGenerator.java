package eventGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import dataStorage.SuitesAndTests;
import eventGenerator.events.WorkingOnEvent;
import tools.LogReader;
import tools.LogWriter;
import tools.TestingData;

public class EventGenerator {

	
	public void readData(File location, File output) throws IOException, ParseException{
		
		LogReader logReader = new LogReader(location);
		
//		for(int assignmentIndex=0;assignmentIndex<logReader.getNumAssignments();assignmentIndex++){
			int assignmentIndex = Integer.parseInt(location.getName().substring(location.getName().toLowerCase().indexOf("assignment")+"assignment".length()+1));

			List<List<String>> allAssignmentLines = logReader.getAllStudentsAssignmentLogLines(0);
			if(allAssignmentLines==null)
				return;
			
			List<String> studentNames = logReader.getStudentNames();
			
			SuitesAndTests assignmentSuitesAndTests = TestingData.findAllSuitesAndTests(allAssignmentLines,assignmentIndex);
			
			String [] tests = new String[assignmentSuitesAndTests.getTests().size()];
			assignmentSuitesAndTests.getTests().toArray(tests);
			
			EventManager dataCollection = new EventManager();
			dataCollection.addEvent(new WorkingOnEvent());
			
			
			String path=output.toString()+"/assignment"+assignmentIndex+"_events.csv";
			File outputData=new File(path);
			outputData.mkdirs();
			if (outputData.exists()) {
				outputData.delete();
			}
			outputData.createNewFile();
			FileWriter dataOut=new FileWriter(outputData);
			
			LogWriter.writeToFile(dataOut,dataCollection.getOrderedHeaders());
			
			for(int i=0;i<allAssignmentLines.size();i++){
				
				try{
					String studentName=studentNames.get(i);
					dataCollection.processLog(allAssignmentLines.get(i),1,studentName);
					List<String[]> collectedData=dataCollection.getOrderedData();
					dataCollection.reset();
					
					String [][] value = new String[collectedData.size()][];
					collectedData.toArray(value);
					LogWriter.writeToFileMultipleLines(dataOut,value);
				
				}catch(Exception e){
					e.printStackTrace();
					System.out.println(studentNames.get(i) + "   " + path);
					dataCollection.reset();
					return;
				}
			}
			dataOut.close();
//		}
		
		
	}
	
}
