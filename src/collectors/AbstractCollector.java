package collectors;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class AbstractCollector implements Collector {

	protected int reqPass;
	protected String [] headers;
	protected String [] results;
	protected String [] testNames;
	
	@Override
	public void reset() {
		results=new String[headers.length];
	}
	
	@Override
	public String[] getHeaders() {
		return headers;
	}

	@Override
	public int getRequiredPass() {
		return reqPass;
	}
	
	@Override
	public boolean otherCollectorCompatable() {
		return true;
	}
	
	@Override
	public String[] getResults() {
		return results;
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
	
	private static final SimpleDateFormat dateFormat=new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy");
	protected static long secondsBetween(String date1, String date2) throws ParseException{
		Date first=dateFormat.parse(date1);
		Date second=dateFormat.parse(date2);
		long secondsBetween=TimeUnit.SECONDS.convert(second.getTime()-first.getTime(), TimeUnit.MILLISECONDS);
		return secondsBetween;
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
	
	@Override
	public void setTestNames(String[] names) {
		testNames=names;
		generateHeaders();
		reset();
	}
	
	protected void generateHeaders() {
		String [] headers = new String[testNames.length];
		String headerPhrase = getHeaderPhrase();
		for(int i=0;i<headers.length;i++)
			headers[i]=testNames[i]+headerPhrase;
		this.headers=headers;
	}
	
	protected abstract String getHeaderPhrase();
	
	
}
