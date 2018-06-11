import java.util.List;

import cn.edu.xmut.entry.ConfigInfo;
import cn.edu.xmut.latinhypercube.LatinHyperCube;

public class EntryForOriginalSWAT {

	public static void main(String[] args) {
		ConfigInfo.init(args[0]);
		int models = ConfigInfo.modelCount;

		// generate parameter values
		List<String> pvsList = LatinHyperCube.genParameterVal(
				ConfigInfo.paraDefPath, "", ConfigInfo.simulateCount);
		ConfigInfo.paramValueList = pvsList;

		for (int i = 0; i < ConfigInfo.paramValueList.size(); i++) {
			System.out.println(ConfigInfo.paramValueList.get(i));
		}

		ConfigInfo.paramNameList = LatinHyperCube.paraList;
		String tmp = "";
		int paracount = LatinHyperCube.paraList.size();
		for (int i = 0; i < paracount - 1; i++) {

			String pname = LatinHyperCube.paraList.get(i).split("\\s+")[0];
			tmp = tmp + pname + " ";

		}
		tmp = tmp += LatinHyperCube.paraList.get(paracount - 1).split("\\s+")[0];

		ConfigInfo.paraNames = tmp;

		System.out.println(tmp);

		// /////////////////////////////////////////////////////////////////////////////
		ConfigInfo.histime = System.currentTimeMillis();

		for (int i = 0; i < models; i++) {

			Thread thread = new Thread(new RunOriginalSwatThread(
					ConfigInfo.modelsPath + "model" + (i + 1) + "\\",
					ConfigInfo.backupModelsPath));
			thread.start();
		}

		Thread mThread = new Thread(new MonitorLogThread(models,
				ConfigInfo.performanceoutputpath + "per_o_"
						+ ConfigInfo.modelCount + "_" + args[1] + ".csv"));
		mThread.start();
		// /////////////////////////////////////////////////////////////////////////////

	}

}
