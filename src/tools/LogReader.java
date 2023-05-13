package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogReader {

	private List<Iterator<File>> allAssignmentIterators;
	private List<String> studentNames;
	
	
	public LogReader(File location){
		allAssignmentIterators=getAllAssignmentIterators(location); 
	}
	
	public int getNumAssignments(){
		return allAssignmentIterators.size();
	}
	
	public List<List<String>> getAllStudentsAssignmentLogLines(int assignmentIndex) throws FileNotFoundException{
		Iterator<File> assignmentLogIterator=allAssignmentIterators.get(0);
		if(assignmentLogIterator==null)
			return null;
		
		List<List<String>> allAssignmentLines = new ArrayList<List<String>>();
		studentNames = new ArrayList<String>();
		
		while(assignmentLogIterator.hasNext()){
			File assignmentLog=assignmentLogIterator.next();
			studentNames.add(determineStudentName(assignmentLog));
			
			Scanner scan = new Scanner(assignmentLog);
			ArrayList<String> logLines = new ArrayList<String>();
			
			while(scan.hasNext()){
				String nextLine=scan.nextLine();
				if(nextLine.split(",").length==9)
					logLines.add(nextLine);
			}
			scan.close();
			if(logLines.size()==0)
				continue;
			

			logLines.remove(0);
			allAssignmentLines.add(logLines);
		}
		
		return allAssignmentLines;
	}
	
	public List<String> getStudentNames(){
		return studentNames;
	}
	
	private static String determineStudentName(File log){
//		Matcher studentID=Pattern.compile(".*\\\\(.*)\\\\.*").matcher(log.toString());
//		Matcher studentID=Pattern.compile("\\(").matcher(log.toString());
//		if(studentID.find()) {
//			String name = studentID.group(0);
//			name = name.substring("Assignment 1\\\\".length(),name.lastIndexOf("\\\\"));
//			return name;
//		} else
//			return "I am error";
		
		
//		while(!log.getName().contains("Submission")) {
//			log = log.getParentFile();
//		}
//		log = log.getParentFile();
//		return log.getName();
		if (log.getPath().contains("Submission attachment")) {
			log = new File(log.getPath().substring(0, log.getPath().indexOf("Submission attachment")-1));
			return log.getName();
		} else if (log.getPath().contains("submission_")) {
			String name = log.getName();
			while (log.getPath().contains("submission_")) {
				name = log.getName();
				log = log.getParentFile();
			}
			return name;
		}
		return	log.getParentFile().getParentFile().getParentFile().getParentFile().getName();
//		return "Logs";
	}
	
	private static List<Iterator<File>> getAllAssignmentIterators(File location){
		ArrayList<Iterator<File>> allAssignments=new ArrayList<Iterator<File>>();
//		for(int i=0;i<13;i++){
//			String assignmentNumber = i==0?i+"_1":Integer.toString(i);
			String assignmentNumber = location.getName().substring(location.getName().toLowerCase().indexOf("assignment")+"assignment".length()+1);
			Iterator<File> testVal=new AssignmentLogIterator(location,assignmentNumber);
			if(testVal.hasNext())
				allAssignments.add(testVal);
			else// if(i==0)
				allAssignments.add(null);
//			else
//				break;
//		}
		return allAssignments;
	}
}
