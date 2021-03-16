package eventGenerator.events;

import java.util.List;


public interface Event {
	
	static final int TEST_NUMBER_INDEX=0;
	static final int TIME_TESTED_INDEX=1;
	static final int PERCENT_PASSED_INDEX=2;
	static final int PERCENT_PASSED_CHANGE_INDEX=3;
	static final int TEST_SUITE_INDEX=4;
	static final int TEST_PASS_INDEX=5;
	static final int TEST_PARTIAL_INDEX=6;
	static final int TEST_FAIL_INDEX=7;
	static final int TEST_UNTESTED_INDEX=8;
	
	public void setName(String name);
	public void setCaseID(int id);
	public List<String []> getResults();
	public void logData(String [] data, String name) throws IllegalArgumentException;
	public void reset();
	public int getRequiredPass();
}
