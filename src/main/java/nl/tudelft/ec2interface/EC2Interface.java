package nl.tudelft.ec2interface;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceStatus;
import com.amazonaws.services.ec2.model.Placement;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;

public class EC2Interface {

	private AmazonEC2 ec2;

	public static void main(String[] args) throws IOException {
		EC2Interface ec2 = new EC2Interface();
		System.out.println(ec2.runNewInstance());
		//System.out.println(ec2.getInstanceInfo("i-4c0d6316"));
		//System.out.println(ec2.getInstanceList().toString());
		//ec2.terminateInstance("i-4c0d6316");
		//ec2.terminateInstances(new ArrayList<String>(){{add("i-b8e4a1e3");add("i-5f879804");}});
	}
	
	public EC2Interface() {
		
		// Create the AmazonEC2Client object so we can call various APIs.
		ec2 = new AmazonEC2Client(new ClasspathPropertiesFileCredentialsProvider());
		Region usWest1 = Region.getRegion(Regions.US_WEST_1);
		ec2.setRegion(usWest1);
	}
	
	public String runNewInstance()
	{
		RunInstancesRequest runInstancesRequest = new RunInstancesRequest();

		runInstancesRequest
				.withImageId("ami-acf9cde9")
				.withInstanceType("t1.micro")
				.withMinCount(1)
				.withMaxCount(1)
				.withKeyName("joseph_wing")
				.withSecurityGroups("sg_testinstance1")
				.withPlacement(new Placement("us-west-1c"));

		RunInstancesResult runInstancesResult = ec2.runInstances(runInstancesRequest);
		List<Instance> instances = runInstancesResult.getReservation().getInstances();
		
		System.out.println("RunInstance request has been sent");
		
		String instanceId = instances.get(0).getInstanceId();
		
		System.out.println(getInstanceInfo(instanceId));
		
		
		boolean configured = false;
		
		do {
			try {
				
				Thread.sleep(60 * 1000);
				System.out.println(getInstanceInfo(instanceId));
				InstanceInfo iInfo = getInstanceInfo(instanceId);
				if (iInfo.getStatus().equals("running")) {
						configureInstance(iInfo.getPublicIP());
						configured = true;
				}
				
			} catch (AmazonServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (!configured);
		
		return instanceId;
	}
	
	public void terminateInstance(String instanceId)
	{
		ArrayList<String> instanceIds = new ArrayList<String>();
		instanceIds.add(instanceId);
		terminateInstances(instanceIds);
	}
	
	public void terminateInstances(ArrayList<String> instanceIds)
	{
		try {
        	// Terminate instances.
        	TerminateInstancesRequest terminateRequest = new TerminateInstancesRequest(instanceIds);
        	ec2.terminateInstances(terminateRequest);
    	} catch (AmazonServiceException e) {
    		// Write out any exceptions that may have occurred.
           System.out.println("Error terminating instances");
           System.out.println("Caught Exception: " + e.getMessage());
           System.out.println("Reponse Status Code: " + e.getStatusCode());
           System.out.println("Error Code: " + e.getErrorCode());
           System.out.println("Request ID: " + e.getRequestId());
        }
	}
	
	public InstanceInfo getInstanceInfo(String instanceId)
	{
		InstanceInfo iInfo = null;
		
		DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest().withInstanceIds(instanceId);
		DescribeInstancesResult describeInstancesResult = ec2.describeInstances(describeInstancesRequest);
		List<Reservation> list = describeInstancesResult.getReservations();
		
		for (Reservation res : list) {
			List<Instance> instancelist = res.getInstances();

			for (Instance instance : instancelist) {
				iInfo = InstanceInfo.ReadInfo(instance);
			}
		}

		return iInfo;
	}
	
	public ArrayList<String> getInstanceList()
	{
		ArrayList<String> instanceIds = new ArrayList<String>();
		
		DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
		DescribeInstancesResult describeInstancesResult = ec2.describeInstances(describeInstancesRequest);
		List<Reservation> list = describeInstancesResult.getReservations();
		
		for (Reservation res : list) {
			List<Instance> instancelist = res.getInstances();

			for (Instance instance : instancelist) {
				if(instance.getKeyName().equals("joseph_wing"))
					instanceIds.add(instance.getInstanceId());
			}
		}
		return instanceIds;
	}
	
	public void configureInstance(String publicIP)
	{
		try {
			final ProcessBuilder pb = new ProcessBuilder("/bin/sh", "initInstance", publicIP , "joseph_wing.pem");
			pb.directory(new File("."));
			pb.redirectErrorStream(true);
			final Process p = pb.start();
			//final int processStatus = p.waitFor();
			
//		    InputStream is = p.getInputStream();
//		    InputStreamReader isr = new InputStreamReader(is);
//		    BufferedReader br = new BufferedReader(isr);
//		    String line;
//		    while ((line = br.readLine()) != null) {
//		      System.out.println(line);
//		    }
		    System.out.println("Program terminated!");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
