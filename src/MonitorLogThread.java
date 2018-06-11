import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;

import cn.edu.xmut.entry.ConfigInfo;
import cn.edu.xmut.monitor.Monitor;

public class MonitorLogThread implements Runnable {
	public int threadcount = 1;
	public static int simCount = 0;
	public String filename;

	public MonitorLogThread(int tcount, String filename) {
		this.filename = filename;
		threadcount = tcount;
	}

	@Override
	public void run() {

		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(new File(filename));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		ArrayList<String> lines = new ArrayList<String>();
	
		while (simCount < threadcount) {
			String[] cpus=Monitor.getCpuUsageRates();
			String tmp = "";
			for (int i = 0; i <cpus.length; i++) {
			tmp=tmp+cpus[i]+","	;
			}
			tmp = Monitor.getPhysicalMemoryUsageRate() + "," + tmp;
			lines.add(tmp);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			long tt=System.currentTimeMillis();
			
			lines.add(ConfigInfo.histime+","+tt+","+(tt-ConfigInfo.histime));
			IOUtils.writeLines(lines, IOUtils.LINE_SEPARATOR, outputStream);
			
			
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


}
