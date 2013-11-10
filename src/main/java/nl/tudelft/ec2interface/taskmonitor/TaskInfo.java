package nl.tudelft.ec2interface.taskmonitor;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class TaskInfo {

	private Timestamp receiveTime;
	private Timestamp queueTime;
	private Timestamp processingTime;
	private Timestamp finishTime;
	//private int id;
	//private int masterid;
	//private int workerid;
	
	public Timestamp getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}

	public Timestamp getQueueTime() {
		return queueTime;
	}

	public void setQueueTime(Timestamp queueTime) {
		this.queueTime = queueTime;
	}

	public Timestamp getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(Timestamp processingTime) {
		this.processingTime = processingTime;
	}

	public Timestamp getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Timestamp finishTime) {
		this.finishTime = finishTime;
	}
	
	public String displayTime(Timestamp timestamp)
	{
		Calendar amsterdam = Calendar.getInstance();
		amsterdam.setTimeInMillis(timestamp.getTime());
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Amsterdam"));
		return dateFormat.format(amsterdam.getTime());
	}

	public static void main(String[] args)
	{
		Timestamp t = new Timestamp(System.currentTimeMillis());
		System.out.println(new TaskInfo().displayTime(t));
	}
}
