package nl.tudelft.ec2interface.sysmonitor;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

public class SystemUsage {

	public static void main(String[] args)
	{
		SystemInfo info = SystemUsage.getInfo();
		System.out.println(SystemUsage.ToJson(info));
		System.out.println(SystemUsage.FromJson(SystemUsage.ToJson(info)));
	}
	
	public static SystemInfo getInfo()
	{
		SystemInfo sInfo = new SystemInfo();
		sInfo.setCpuInfo(new CpuUsage().getInfo());
		sInfo.setMemoryInfo(new MemoryUsage().getInfo());
		sInfo.setDiskInfo(new DiskUsage().getInfo());
		return sInfo;
	}
	
	public static String ToJson(SystemInfo sInfo)
	{
		try {
			
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(sInfo);
			return json;
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
			
		}
		
	}
	
	public static SystemInfo FromJson(String jsonString)
	{

		try {
			
			SystemInfo sInfo = new ObjectMapper().readValue(jsonString, SystemInfo.class);
			return sInfo;
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
