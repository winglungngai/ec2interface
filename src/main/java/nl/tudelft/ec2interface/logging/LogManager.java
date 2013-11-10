package nl.tudelft.ec2interface.logging;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.ec2interface.sysmonitor.CpuInfo;
import nl.tudelft.ec2interface.sysmonitor.DiskInfo;
import nl.tudelft.ec2interface.sysmonitor.MemoryInfo;
import nl.tudelft.ec2interface.sysmonitor.SystemUsage;
import nl.tudelft.ec2interface.sysmonitor.SystemUsageInfo;

public class LogManager {
	
	DbConnector dbConnector = null;
	
	public LogManager()
	{
		String dbURL = "jdbc:mysql://localhost:3306/systemlog";
	       String dbuser = "root";
	       String dbPassword = "root";
			
	       dbConnector = new DbConnector(dbURL, dbuser, dbPassword);
	}
	
	public void logSystemUsage()
	{
	   SystemUsageInfo uInfo = new SystemUsage().getInfo();
	   CpuInfo cInfo = uInfo.getCpuInfo();
	   MemoryInfo mInfo = uInfo.getMemoryInfo();
	   DiskInfo dInfo = uInfo.getDiskInfo();
	   System.out.println(uInfo);
	   dbConnector.executeUpdate(String.format(
			   "INSERT INTO systemlog.usage (workerId, instanceId, timestamp, cpuModel, cpuUserTime, cpuSysTime, cpuIdleTime, cpuUsage, memoryTotal, memoryUsed, memoryActual, memoryUsage, memoryActualUsage, diskspaceTotal, diskspaceUsed, diskspaceUsage) "
			   + "VALUES ('%s', '%s', '%s', '%s', '%d', '%d', '%d', '%d', '%d', '%d', '%d', '%d', '%d', '%d', '%d', '%d');",
			   "workid", "instanceid", uInfo.getTimestamp(), 
			   cInfo.getCpuModel(), cInfo.getUserTime(), cInfo.getSysTime(), cInfo.getIdleTime(), cInfo.getUsage(), 
			   mInfo.getTotalSystemMemory(), mInfo.getUsedSystemMemory(), mInfo.getActualSystemMemory(), mInfo.getUsage(), mInfo.getActualUsage(), 
			   dInfo.getTotalDiskSpace(), dInfo.getUsedDiskSpace(), dInfo.getUsage()));
       dbConnector.close();
	}
	
	public List<SystemUsageInfo> readSystemUsage()
	{
		ResultSet rs = dbConnector.Read("Select * from systemlog.usage");
		List<SystemUsageInfo> uInfos = new ArrayList<SystemUsageInfo>();
		
		try {
			while(rs.next())
			{
				SystemUsageInfo uInfo = new SystemUsageInfo();
				CpuInfo cInfo = new CpuInfo();
				MemoryInfo mInfo = new MemoryInfo();
				DiskInfo dInfo = new DiskInfo();
				
				uInfo.setWorkerId(rs.getString("workerId"));
				uInfo.setInstanceId(rs.getString("instanceId"));
				uInfo.setTimestamp(rs.getTimestamp("timestamp"));
				
				cInfo.setCpuModel(rs.getString("cpuModel"));
				cInfo.setUserTime(rs.getInt("cpuUserTime"));
				cInfo.setSysTime(rs.getInt("cpuSysTime"));
				cInfo.setIdleTime(rs.getInt("cpuIdleTime"));
				cInfo.setUsage(rs.getInt("cpuUsage"));
				uInfo.setCpuInfo(cInfo);
				
				
				mInfo.setTotalSystemMemory(rs.getInt("memoryTotal"));
				mInfo.setUsedSystemMemory(rs.getInt("memoryUsed"));
				mInfo.setActualSystemMemory(rs.getInt("memoryActual"));
				mInfo.setUsage(rs.getInt("memoryUsage"));
				mInfo.setActualUsage(rs.getInt("memoryActualUsage"));
				uInfo.setMemoryInfo(mInfo);
				
				dInfo.setTotalDiskSpace(rs.getInt("diskspaceTotal"));
				dInfo.setUsedDiskSpace(rs.getInt("diskspaceUsed"));
				dInfo.setUsage(rs.getInt("diskspaceUsage"));
				uInfo.setDiskInfo(dInfo);
				
				uInfos.add(uInfo);
			}
			return uInfos;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		
	      LogManager lManager = new LogManager();
	      lManager.logSystemUsage();
	      System.out.println(lManager.readSystemUsage());
	   
		}

}
