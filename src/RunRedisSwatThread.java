import java.io.IOException;

import cn.edu.xmut.entry.ConfigInfo;

public class RunRedisSwatThread implements Runnable {

	private static int modelPos = 0;

	public static synchronized int getModelPos() {
		modelPos++;
		return modelPos - 1;
	}

	private Object lock = new Object();

	private String filepath;

	private String backfilepath;

	public String getBackfilepath() {
		return backfilepath;
	}

	public void setBackfilepath(String backfilepath) {
		this.backfilepath = backfilepath;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public RunRedisSwatThread(String filepath, String backuppath) {

		this.filepath = filepath;
		this.backfilepath = backuppath;
	}

	

	@Override
	public void run() {
		int k = getModelPos();

		while (k < ConfigInfo.simulateCount) {
			// run SWAT
			Process pr;
			try {
				pr = java.lang.Runtime.getRuntime().exec(
						this.getFilepath() + "\\runswatredis.bat "
								+ this.getFilepath());

				new Thread(new StreamDrainer(pr.getInputStream())).start();
				new Thread(new StreamDrainer(pr.getErrorStream())).start();
				pr.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// extract desire series and calculate the metrics
			// TODO

			k = getModelPos();
		}

		// send a finish signal
		synchronized (lock) {
			MonitorLogThread.simCount++;
		}
	}

}
