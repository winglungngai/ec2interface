package nl.tudelft.ec2interface.sysmonitor;

public class SystemInfo {

	private CpuInfo cpuInfo;
	private MemoryInfo memoryInfo;
	private DiskInfo diskInfo;
	
	public CpuInfo getCpuInfo() {
		return cpuInfo;
	}

	public void setCpuInfo(CpuInfo cpuInfo) {
		this.cpuInfo = cpuInfo;
	}

	public MemoryInfo getMemoryInfo() {
		return memoryInfo;
	}

	public void setMemoryInfo(MemoryInfo memoryInfo) {
		this.memoryInfo = memoryInfo;
	}

	public DiskInfo getDiskInfo() {
		return diskInfo;
	}

	public void setDiskInfo(DiskInfo diskInfo) {
		this.diskInfo = diskInfo;
	}

	@Override
	public String toString() {
		return "SystemInfo " + "\n"
				+ cpuInfo + "\n"
				+ memoryInfo + "\n"
				+ diskInfo + "\n";
	}
	
	

}
