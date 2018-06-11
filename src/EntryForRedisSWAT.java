import java.util.List;

import cn.edu.xmut.entry.ConfigInfo;
import cn.edu.xmut.latinhypercube.LatinHyperCube;
import cn.edu.xmut.redis.RedisDBUtils;

public class EntryForRedisSWAT {

	public static void main(String[] args) {
		
		ConfigInfo.init(args[0]);

		int models = ConfigInfo.modelCount;

		// generate parameter values
		List<String> pvsList = LatinHyperCube.genParameterVal(
				ConfigInfo.paraDefPath_redis, "", ConfigInfo.simulateCount);
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

		// connect to redis
		RedisDBUtils.setup();
		// load model files to redis
		RedisDBUtils.setModelFilePath(ConfigInfo.backupModelsPath);

		RedisDBUtils.load();
		
		
		ConfigInfo.histime = System.currentTimeMillis();
		// load parameter values to redis
		RedisDBUtils.loadParameters(ConfigInfo.paraNames,
				ConfigInfo.paramValueList);

		// load lua script
		RedisDBUtils.loadScript(ConfigInfo.script);

		// disconnect redis
		RedisDBUtils.close();

		// /////////////////////////////////////////////////////////////////////////////

		for (int i = 0; i < models; i++) {

			Thread thread = new Thread(new RunRedisSwatThread(
					ConfigInfo.modelsPath + "model" + (i + 1) + "\\",
					ConfigInfo.backupModelsPath));
			thread.start();
		}

		Thread mThread = new Thread(new MonitorLogThread(models,
				ConfigInfo.performanceoutputpath + "per_r_"
						+ ConfigInfo.modelCount + "_" + args[1] + ".csv"));
		mThread.start();
		// /////////////////////////////////////////////////////////////////////////////

	}

}
