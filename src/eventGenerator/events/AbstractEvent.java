package eventGenerator.events;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class AbstractEvent implements Event{

	protected int reqPass;
	protected String name;
	protected int caseID;

	@Override
	public int getRequiredPass() {
		return reqPass;
	}

	@Override
	public void setName(String name) {
		this.name=name;
	}

	@Override
	public void setCaseID(int id) {
		caseID=id;
	}

	protected boolean contains(String [] searchData, String term){
		for(String data:searchData)
			if(data.equals(term))
				return true;
		return false;
	}
	
	protected boolean matches(String [] searchData, String term){
		for(String data:searchData)
			if(data.matches(term))
				return true;
		return false;
	}
	
	private static final SimpleDateFormat assignmentLogsDateFormat=new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	private static final SimpleDateFormat eventDateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	protected long secondsBetween(String date1, String date2) throws ParseException{
		Date first=assignmentLogsDateFormat.parse(date1);
		Date second=assignmentLogsDateFormat.parse(date2);
		long secondsBetween=TimeUnit.SECONDS.convert(second.getTime()-first.getTime(), TimeUnit.MILLISECONDS);
		return secondsBetween;
	}
	
	protected String convertDate(String date) throws ParseException {
		Date conv = assignmentLogsDateFormat.parse(date);
		return eventDateFormat.format(conv);
	}
	
	protected String [] getMatches(String term, String []... searchData){
		List<String> retVal = new ArrayList<String>();
		for(String [] arr:searchData)
			for(String data:arr)
				if(data.matches(term))
					retVal.add(data);
		String [] values = new String[retVal.size()];
		retVal.toArray(values);
		return values;
	}
	
}
