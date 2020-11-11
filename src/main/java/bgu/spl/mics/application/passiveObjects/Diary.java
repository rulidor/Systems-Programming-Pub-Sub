package bgu.spl.mics.application.passiveObjects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;


/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {
	/**
	 * Retrieves the single instance of this class.
	 */
	private List<Report> reports;
	private int total;
	private static Diary instanceDiary;

	private Diary(){
		reports = new LinkedList<>();
		total=0;
	}

	public static Diary getInstance() {
		if (instanceDiary == null)
			instanceDiary = new Diary();
		return instanceDiary;
	}

	public List<Report> getReports() {
		return this.reports;
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public synchronized void addReport(Report reportToAdd){
		if (!this.reports.contains(reportToAdd))
			this.reports.add(reportToAdd);
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */

	public void printToFile(String filename){
		JSONObject jsonObject;
		JSONObject rep1=new JSONObject();
		JSONArray serial;
		JSONArray names;
		JSONArray reportsToJson = new JSONArray();

		for( Report rep:this.reports)
		{
			jsonObject=new JSONObject();
			jsonObject.put("qTime",rep.getQTime());
			jsonObject.put("timeIssued",rep.getTimeIssued());
			jsonObject.put("missionName",rep.getMissionName());
			jsonObject.put("m",rep.getM());
			jsonObject.put("moneypenny",rep.getMoneypenny());

			serial=new JSONArray();
			for(String ser:rep.getAgentsSerialNumbers())
			{
				serial.add(ser);
			}
			jsonObject.put("agentsSerialNumbers",serial);
			names=new JSONArray();
			for(String name:rep.getAgentsNames())
			{
				names.add(name);
			}
			jsonObject.put("agentsNames",names);
			jsonObject.put("gadgetName",rep.getGadgetName());
			jsonObject.put("timeCreated",rep.getTimeCreated());
			reportsToJson.add(jsonObject);

		}

		rep1.put("reports",reportsToJson);

		rep1.put("total",this.total);

		try {
			PrintWriter pw = new PrintWriter(filename);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String out=gson.toJson(rep1);

			pw.append(out);

			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public int getTotal(){
		return this.total;
	}

	/**
	 * Increments the total number of received missions by 1
	 */
	public void incrementTotal(){
		this.total++;
	}
}
