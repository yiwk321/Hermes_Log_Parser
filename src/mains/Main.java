package mains;

import java.io.File;
import java.io.FileWriter;

import collectors.*;
import compiledLogGenerator.SemesterLogGenerator;
import tools.CSVParser;
import tools.LogWriter;
public class Main {

	private static final int breakTime= (int)(1.5*60*60);
	
	public static void main(String[] args) {
		
		try {
			

			File data=new File("InputFolders/Comp524/Fall2020");
			File output=new File("NewVersionTest");



//			new EventGenerator().readData(data, output);
			
			Collector [] collectors = {
					new PercentPassedCollector(4),
					new AttemptsCollector(),
					new BreakTimeCollector(breakTime),
					new FinalStatusCollector(),
					new WorkTimeCollector(breakTime),
//					new KnownTimeCollector()
//					new WorkTimeStatisticsCollector()
			};
			
			new SemesterLogGenerator(collectors,true,"assignment#.csv").readData(data, output);
//			fixCSVData();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This was just used to correct data displaying in the WorkTimeStats Collector
	 */
	private static void fixCSVData() throws Exception {
		for(int i=0;i<7;i++) {
			File csvLoc = new File("./NewVersionTest/Assignment"+i+"_TestWorkTimes.csv");
			String [][] readFile = (new CSVParser(csvLoc)).readCSV();
			
			for(int j=0;j<readFile[0].length;j++) {
				int lastOpen=0;
				for(int k=0;k<readFile.length;k++) {
					System.out.println(readFile[k][j]);
					if(!readFile[k][j].equals("")) {
						if(lastOpen!=k) {
							readFile[lastOpen][j] = readFile[k][j];
							readFile[k][j]="";
						}
						lastOpen++;
					}else {
						System.out.println(readFile[k][j]);
					}
				}
			}
			
			FileWriter writeTo = new FileWriter(csvLoc);
			LogWriter.writeToFileMultipleLines(writeTo, readFile);
			writeTo.close();
		}
	}
	
}
