package cn.edu.xmut.redis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import redis.clients.jedis.Jedis;
import cn.edu.xmut.parameteredit.HRUFileEditor;
import cn.edu.xmut.parameteredit.ParameterUtil;
import cn.edu.xmut.util.StringFormat;

/**
 * 
 */

public class RedisDBUtils {

	/**
	 * 
	 */
	static String modelFilePath = "F:\\paper8\\model\\bak\\";

	public static String getModelFilePath() {
		return modelFilePath;
	}

	public static void setModelFilePath(String modelFilePath) {
		RedisDBUtils.modelFilePath = modelFilePath;
	}

	static String file1 = ".hru";
	static String file2 = ".mgt";
	static String file3 = ".sol";
	static String file4 = ".chm";
	static String file5 = ".gw";
	static String file6 = ".ops";
	static String file7 = ".sep";

	private static Jedis jedis;

	public static void setup() {
		jedis = new Jedis("127.0.0.1", 6379);
	}

	public static void close() {
		jedis.disconnect();
	}

	public static void load() {
		File[] files = getFiles();

		for (int i = 0; i < files.length; i++) {
			loadFile(files[i]);
		}
		// jedis.scriptLoad(script);

	}

	public static void loadFile(File file1) {

		FileReader filereader = null;
		try {
			filereader = new FileReader(file1);

			List<String> lines = IOUtils.readLines(filereader);

			for (int i = 0; i < lines.size(); i++) {

				String tmpString = lines.get(i);

				// System.out.println(tmpString);
				String[] tokens = tmpString.split("\\|");

				// tokens[0] = tokens[0].trim();

				String tString = StringFormat.fixLenConvert(i, 3, "0");
				int kk = 3 - tString.length();
				for (int j = 0; j < kk; j++) {
					tString = "0" + tString;
				}

				if (file1.getName().endsWith(".gw")) {
					// 000950007.mgt
					String mykey = tString + ".gwf."
							+ file1.getName().substring(0, 5) + "."
							+ file1.getName().substring(5, 9);
					jedis.set(mykey, tokens[0]);
					/*System.out.println(mykey);*/
					// xxx.www.bbbbb.kkkk[.ssss]
					/*xxx=lineno
					  www=file type
					  bbbbb=subbasn number
					  kkkk=hru number
					  ssss=simulation number (optional)
					  */
				} else {
					String mykey = tString + file1.getName().substring(9)+"."
							+ file1.getName().substring(0, 5) + "."
							+ file1.getName().substring(5, 9);
					jedis.set(mykey, tokens[0]);
					/*System.out.println(mykey);*/
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				filereader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public static File[] getFiles() {

		File dir = new File(getModelFilePath());

		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return !name.startsWith(".")
						&& !name.startsWith("output")
						&& (name.endsWith(file1) || name.endsWith(file2)
								|| name.endsWith(file3) || name.endsWith(file4)
								|| name.endsWith(file5) || name.endsWith(file6) || name
									.endsWith(file7));
			}
		};

		File[] files = dir.listFiles(filter);

		return files;

	}

	public static void loadParameters(String parameterNames,
			List<String> valueList) {

		String[] pns = parameterNames.split("\\s+");
		for (int i = 0; i < pns.length; i++) {
			Set<String> keys = jedis.keys(pns[i]);

			for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();

				String line = jedis.get(key);
				for (int j = 0; j < valueList.size(); j++) {

					if (pns[i].contains("SOL_Z") || pns[i].contains("SOL_K")
							|| pns[i].contains("SOL_AWC")) {

						// " Depth                [mm]:      100.00".length()
						String backpart = line.substring(39);
						// " Depth                [mm]:".length()
						String startpart = line.substring(0, 27);

						String midpart = line.substring(27, 39).trim();

						String[] values = valueList.get(j).split("\\s+");
						String value = values[i];

						double changes = Double.parseDouble(value.trim());

						double v = Double.parseDouble(midpart);
						v = ParameterUtil.transfort(pns[i], v, changes);
						String vsString = ParameterUtil.doubleToString(v, 2);

						String padded = startpart
								+ "            ".substring(vsString.trim()
										.length()) + vsString.trim() + backpart;
						jedis.set(
								key
										+ "."
										+ StringFormat.fixLenConvert(j + 1, 4,
												"0"), padded);

					} else if (pns[i].contains("CN2")) {

						String[] values = valueList.get(j).split("\\s+");
						String value = values[i];

						double oldv = Double.parseDouble(line.trim());
						double changes = Double.parseDouble(value.trim());
						oldv = ParameterUtil.transfort(pns[i], oldv, changes);

						if (oldv > 98) {
							oldv = 97;
						} else if (oldv < 35) {
							oldv = 36;
						}

						String vsString = ParameterUtil.doubleToString(oldv, 3);

						String padded = "                ".substring(vsString
								.trim().length()) + vsString.trim() + "    ";
						jedis.set(
								key
										+ "."
										+ StringFormat.fixLenConvert(j + 1, 4,
												"0"), padded);

					} else {
						String[] values = valueList.get(j).split("\\s+");
						String value = values[i];

						double oldv = Double.parseDouble(line.trim());
						double changes = Double.parseDouble(value.trim());
						oldv = ParameterUtil.transfort(pns[i], oldv, changes);
						String vsString = ParameterUtil.doubleToString(oldv, 3);
						String padded = "                ".substring(vsString
								.trim().length()) + vsString.trim() + "    ";
						jedis.set(
								key
										+ "."
										+ StringFormat.fixLenConvert(j + 1, 4,
												"0"), padded);
					}

				}
			}

		}

	}

	public static void loadScript(String script) {
		jedis.scriptLoad(script);
	}
}
