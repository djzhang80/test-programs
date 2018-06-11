package cn.edu.xmut.monitor;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class Monitor {



	public static String[] getCpuUsageRates() {
		Sigar sigar = SigarUtils.sigar;

		CpuPerc cpuList[] = null;
		try {
			cpuList = sigar.getCpuPercList();
		} catch (SigarException e) {
			e.printStackTrace();
			return null;
		}
		String[] usageRates = new String[cpuList.length];
		for (int i = 0; i < cpuList.length; i++) {
			usageRates[i] = CpuPerc.format(cpuList[i].getCombined());
		}

		return usageRates;
	}

	public static double getPhysicalMemoryUsageRate() {
		Sigar sigar = SigarUtils.sigar;
		Mem mem;
		try {
			mem = sigar.getMem();

			return mem.getUsedPercent();

		} catch (SigarException e) {
			e.printStackTrace();

		}
		return 0;
	}

}
