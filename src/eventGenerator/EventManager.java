package eventGenerator;

import java.util.ArrayList;
import java.util.List;

import eventGenerator.events.Event;


public class EventManager {

	private List<Event> events;
	
	private final static String [] eventHeaders={
			"case_id",
			"timestamp",
			"activity",
			"user"
	};
	
	public EventManager(){
		events=new ArrayList<Event>();
	}
	
	public EventManager(List<Event> collectors){
		this.events=collectors;
	}
	
	public void addEvent(Event c){
		events.add(c);
	}
	
	public void processLog(List<String> dataLines, int numPasses, String name) {
		
		for(int i=1;i<=numPasses;i++)
			for(String dataLine:dataLines){
				String [] splitLine=dataLine.split(",");
				for(Event events:events)
					if(events.getRequiredPass()==i)
						events.logData(splitLine,name);
			}
		
		String [] lastLine = dataLines.get(dataLines.size()-1).split(",");
		for(Event collector:events)
			if(collector.getRequiredPass()==Integer.MAX_VALUE)
				collector.logData(lastLine,name);
	}
	
	public String [] getOrderedHeaders(){
		return eventHeaders;
	}
	
	public List<String[]> getOrderedData(){
		List<String[]> retVal= new ArrayList<String[]>();
		for(Event e:events)
			for(String [] entry:e.getResults())
				retVal.add(entry);
		return retVal;
	}
	
	public void reset(){
		for(Event event:events)
			event.reset();
	}
	
}

	
