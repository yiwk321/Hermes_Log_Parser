package compiledLogGenerator;

import java.util.ArrayList;
import java.util.List;

import collectors.Collector;

public class CollectorManager {

	private List<Collector> collectors;
	
	public CollectorManager(){
		collectors=new ArrayList<Collector>();
	}
	
	public CollectorManager(List<Collector> collectors){
		this.collectors=collectors;
	}
	
	public void addCollector(Collector c){
		collectors.add(c);
	}
	
	public void processLog(List<String> dataLines, int numPasses) {
		
		for(int i=1;i<=numPasses;i++)
			for(String dataLine:dataLines){
				String [] splitLine=dataLine.split(",");
				for(Collector collector:collectors)
					if(collector.getRequiredPass()==i)
						collector.logData(splitLine);
			}
		
		String [] lastLine = dataLines.get(dataLines.size()-1).split(",");
		for(Collector collector:collectors)
			if(collector.getRequiredPass()==Integer.MAX_VALUE)
				collector.logData(lastLine);
	}
	
	public List<String> getOrderedHeaders(){
		List<String> headers = new ArrayList<String>();
		for(Collector collector:collectors)
			for(String header:collector.getHeaders())
				headers.add(header);
		return headers;
	}
	
	public List<String> getOrderedData(){
		List<String> dataEntries = new ArrayList<String>();
		for(Collector collector:collectors)
			for(String dataEntry:collector.getResults())
				dataEntries.add(dataEntry);
		return dataEntries;
	}
	
	public void reset(){
		for(Collector collector:collectors)
			collector.reset();
	}
	
	public boolean specialPrint() {
		for(Collector collector:collectors)
			if(!collector.otherCollectorCompatable())
				return true;
		return false;
	}
	
}

	
