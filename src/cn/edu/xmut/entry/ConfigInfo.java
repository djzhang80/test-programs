package cn.edu.xmut.entry;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class ConfigInfo {

	// path to the definition file of calibration parameter
	public static String paraDefPath = "F:\\paper8\\java\\models\\parameter_def.txt";
	public static String paraDefPath_redis = "F:\\paper8\\java\\models\\parameter_def_redis.txt";
	// where the models are saved
	public static String modelsPath = "F:\\paper8\\java\\models\\";
	public static String backupModelsPath = "F:\\paper8\\java\\models\\backup\\";
	public static String performanceoutputpath = "F:\\paper8\\java\\models\\";
	public static String sigarLibPath="F:\\paper8\\java\\hyperic-sigar-1.6.4\\sigar-bin\\lib";
	public static int modelCount = 2;
	public static int simulateCount = 4;

	// parameters' name
	public static List<String> paramNameList = null;
	public static String paraNames = "";
	// parameter value list
	public static List<String> paramValueList = null;
	public static long histime = 0;

	public static String script = "local lv=redis.call('get',KEYS[1]..KEYS[2]) if lv==false then return redis.call('get',KEYS[1]) else return lv end";

	public static void init(String filePath) {
		Properties pps = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(filePath));
			pps.load(in);
			
			System.out.println(pps.getProperty("paraDefPath"));
			System.out.println(pps.getProperty("paraDefPath_redis"));
			System.out.println(pps.getProperty("modelsPath"));
			System.out.println(pps.getProperty("backupModelsPath"));		
			
			System.out.println(pps.getProperty("performanceoutputpath"));
			System.out.println(pps.getProperty("sigarLibPath"));
			System.out.println(pps.getProperty("modelCount"));
			System.out.println(pps.getProperty("simulateCount"));
			
			paraDefPath = pps.getProperty("paraDefPath");
			paraDefPath_redis = pps.getProperty("paraDefPath_redis");
			modelsPath = pps.getProperty("modelsPath");
			backupModelsPath = pps.getProperty("backupModelsPath");
			performanceoutputpath = pps.getProperty("performanceoutputpath");
			sigarLibPath = pps.getProperty("sigarLibPath");
			
			modelCount = Integer.parseInt(pps.getProperty("modelCount"));
			simulateCount = Integer.parseInt(pps.getProperty("simulateCount"));
			

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
