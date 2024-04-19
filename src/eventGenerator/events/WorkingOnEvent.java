package eventGenerator.events;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
//corresponds to WorkTimeCollector
public class WorkingOnEvent extends AbstractEvent{

	
	private final String movementMatchingRegex = ".*[\\+-]";
	
	private List<String []> workingResults;
	
	public WorkingOnEvent(){
		reqPass=1;
		this.workingResults=new ArrayList<String []>();
	}
	
	@Override
	public void logData(String[] data, String name) throws IllegalArgumentException {
		String [] passedTests = getMatches(movementMatchingRegex,data[TEST_PASS_INDEX].split(" "));
		String [] partialTests = getMatches(movementMatchingRegex,data[TEST_PARTIAL_INDEX].split(" "));
		String [] failedTests = getMatches(movementMatchingRegex,data[TEST_FAIL_INDEX].split(" "));
		
		try {
			for(String passedTest:passedTests){
				String [] event = {Integer.toString(caseID++),
						convertDate(data[TIME_TESTED_INDEX]),
						passedTest.replaceAll("\\+", "").concat("_passed"),
						name};
				workingResults.add(event);
			}
			
			for(String partialTest:partialTests){
				if(partialTest.contains("+")){
					String [] event = {Integer.toString(caseID++),
							convertDate(data[TIME_TESTED_INDEX]),
							partialTest.replaceAll("\\+", "").concat("_partial_growth"),
							name};
					workingResults.add(event);
				}else{
					String [] event = {Integer.toString(caseID++),
							convertDate(data[TIME_TESTED_INDEX]),
							partialTest.replaceAll("-", "").concat("_partial_decline"),
							name};
					workingResults.add(event);
				}	
			}
			
			for(String failedTest:failedTests){
				if(failedTest.contains("+")){
					String [] event = {Integer.toString(caseID++),
							convertDate(data[TIME_TESTED_INDEX]),
							failedTest.replaceAll("\\+", "").concat("_fail_growth"),
							name};
					workingResults.add(event);
				}else{
					String [] event = {Integer.toString(caseID++),
							convertDate(data[TIME_TESTED_INDEX]),
							failedTest.replaceAll("-", "").concat("_fail_decline"),
							name};
					workingResults.add(event);
				}	
			}
			
		}catch(ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<String[]> getResults() {
		return workingResults;
	}
	
	@Override
	public void reset() {
		workingResults=new ArrayList<String []>();
	}
}
