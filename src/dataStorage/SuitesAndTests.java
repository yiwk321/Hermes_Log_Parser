package dataStorage;

import java.util.List;

public class SuitesAndTests {
	private final List<String> suites;
	private final List<String> tests;
	private final int assignmentNumber;
	
	public SuitesAndTests(List<String> suites,List<String>tests,int assignmentNumber){
		this.suites=suites;
		this.tests=tests;
		this.assignmentNumber=assignmentNumber;
	}
	
	public List<String> getSuites(){
		return suites;
	}
	
	public List<String> getTests(){
		return tests;
	}
	
	public int getAssignmentNumber(){
		return assignmentNumber;
	}
	
}
