package nl.tudelft.ec2interface.taskmonitor;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class TaskInfo {

	private int masterId;
	private int workerId;
	private Timestamp receiveTime;
	private Timestamp waitingTime;
	private Timestamp startTime;
	private Timestamp finishTime;
	private int taskSize;
	
	public int getMasterId() {
		return masterId;
	}

	public void setMasterId(int masterId) {
		this.masterId = masterId;
	}

	public int getWorkerId() {
		return workerId;
	}

	public void setWorkerId(int workerId) {
		this.workerId = workerId;
	}

	public Timestamp getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}

	public Timestamp getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(Timestamp waitingTime) {
		this.waitingTime = waitingTime;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Timestamp finishTime) {
		this.finishTime = finishTime;
	}
	
	public int getTaskSize() {
		return taskSize;
	}

	public void setTaskSize(int taskSize) {
		this.taskSize = taskSize;
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
