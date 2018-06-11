import java.io.IOException;
import java.util.HashMap;

import cn.edu.xmut.entry.ConfigInfo;
import cn.edu.xmut.parameteredit.BSNFileEditor;
import cn.edu.xmut.parameteredit.GWFileEditor;
import cn.edu.xmut.parameteredit.HRUFileEditor;
import cn.edu.xmut.parameteredit.MGTFileEditor;
import cn.edu.xmut.parameteredit.RTEFileEditor;
import cn.edu.xmut.parameteredit.SOLFileEditor;
import cn.edu.xmut.parameteredit.SUBFileEditor;

public class RunOriginalSwatThread implements Runnable {

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

	public RunOriginalSwatThread(String filepath, String backuppath) {

		this.filepath = filepath;
		this.backfilepath = backuppath;
		
		/*System.out.println("aaaaaaaaaaaaaaaaaaa"+filepath);
		System.out.println("bbbbbbbbbbbbbbbbbbbbbb"+backuppath);*/
	}

	public static void editModelInput(String input, String info,
			String modelPath, String backup) {

		String[] inputs = input.split("\\s+");
		
		/*System.out.println("eeeeeeeeeeeeeee"+modelPath);
		System.out.println("fffffffffffffff"+backup);*/

		GWFileEditor gwFileEditor = new GWFileEditor(modelPath, backup);
		MGTFileEditor mgtFileEditor = new MGTFileEditor(modelPath, backup);
		BSNFileEditor bsnFileEditor = new BSNFileEditor(modelPath, backup);
		HRUFileEditor hruFileEditor = new HRUFileEditor(modelPath, backup);
		SOLFileEditor solFileEditor = new SOLFileEditor(modelPath, backup);
		SUBFileEditor subFileEditor = new SUBFileEditor(modelPath, backup);
		RTEFileEditor rteFileEditor = new RTEFileEditor(modelPath, backup);

		String[] pnames = info.split("\\s+");

		HashMap<String, HashMap> fileMap = new HashMap<String, HashMap>();

		for (int i = 0; i < pnames.length; i++) {
			String[] parts = pnames[i].split("\\.");
			HashMap fileParaList = fileMap.get(parts[1]);
			if (fileParaList == null) {
				fileParaList = new HashMap();
				fileParaList.put(pnames[i], i);
				fileMap.put(parts[1], fileParaList);
			} else {
				fileParaList.put(pnames[i], i);
				fileMap.put(parts[1], fileParaList);
			}

		}

		HashMap pmap = fileMap.get("mgt");

		mgtFileEditor.modifyFiles(pmap, inputs);

		/*		a__SOL_Z(1).sol	10	30
				a__SOL_AWC(1).sol	0.05	0.2
				a__SOL_K(1).sol	0.3	0.7*/

		pmap = fileMap.get("sol");
		solFileEditor.modifyFiles(pmap, inputs);

		/*		v__OV_N.hru	0.1	0.3
				v__CANMX.hru	0	10
				v__ESCO.hru	0.3	0.7
				v__EPCO.hru	0	0.2*/

		pmap = fileMap.get("hru");
		hruFileEditor.modifyFiles(pmap, inputs);

		/*		v__CH_K1.sub	0	1
				v__CH_N1.sub	0.01	0.05
		*/
		pmap = fileMap.get("sub");
		subFileEditor.modifyFiles(pmap, inputs);
		/*	v__GW_DELAY.gw	20	40 
			v__ALPHA_BF.gw	0	0.2 
			v__GWQMN.gw	20	40 
			v__GW_REVAP.gw	0.1	0.2 
			v__REVAPMN.gw	0	10 
			v__RCHRG_DP.gw	0	0.5 */

		pmap = fileMap.get("gw");
		gwFileEditor.modifyFiles(pmap, inputs);

		pmap = fileMap.get("bsn");
		bsnFileEditor.modifyFiles(pmap, inputs);

		pmap = fileMap.get("rte");
		rteFileEditor.modifyFiles(pmap, inputs);
	}

	@Override
	public void run() {
		int k = getModelPos();

		while (k < ConfigInfo.simulateCount) {

			// edit SWAT input file
			/*System.out.println("zdj---------");*/
			editModelInput(ConfigInfo.paramValueList.get(k),
					ConfigInfo.paraNames, getFilepath(), getBackfilepath());
			
			
			/*System.out.println(ConfigInfo.paramValueList.get(k));
			System.out.println(ConfigInfo.paraNames);
			System.out.println("ccccccccccccccccccc"+getBackfilepath());
			System.out.println("ddddddddddddddddddd"+this.getFilepath());*/
			// run SWAT
			Process pr;
			try {
				pr = java.lang.Runtime.getRuntime().exec(
						this.getFilepath() + "\\runswat.bat "
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
