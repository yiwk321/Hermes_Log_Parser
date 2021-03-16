package collectors;

import java.text.ParseException;

public class WorkTimeCollector extends AbstractCollector{

	private final String movementMatchingRegex = ".*[\\+-]";
	private final long breakTime;
	
	private String lastTime=null;
	private double [] workingResults;
	private long currentTime=0;
	
	public WorkTimeCollector(long breakTime){
		reqPass=1;
		this.breakTime=breakTime;
	}

	
	@Override
	public void logData(String[] data) throws IllegalArgumentException {
		
		String time=data[TIME_TESTED_INDEX];
		if(lastTime!=null){
			try {
				long difference = secondsBetween(lastTime, time);
				currentTime+=difference>breakTime?breakTime:difference;
			} catch (ParseException e) {
				e.printStackTrace();
				throw new IllegalArgumentException("Invalid formatting");
			}
		}
		
		String [] finishedTests = getMatches(movementMatchingRegex,data[TEST_PASS_INDEX].split(" "),data[TEST_PARTIAL_INDEX].split(" "));
		if(finishedTests.length>0){
			long splitTime = (currentTime==0?breakTime:currentTime)/finishedTests.length;
			for(int i=0;i<testNames.length;i++)
				for(int j=0;j<finishedTests.length;j++)
					if(testNames[i].equals(finishedTests[j].replace("+", "").replace("-", "")))
						workingResults[i]+=splitTime;
			currentTime=0;
		}
		lastTime=time;
	}
	
	@Override
	public String[] getResults() {
		for(int i=0;i<workingResults.length;i++)
			results[i]=Double.toString(workingResults[i]);
		return super.getResults();
	}
	
	@Override
	public void reset() {
		currentTime=0;
		workingResults=new double[testNames.length];
		lastTime=null;
		super.reset();
	}


	@Override
	public boolean requiresTestNames() {
		return true;
	}


	@Override
	protected String getHeaderPhrase() {
		return " was worked on for x seconds";
	}
	
}
