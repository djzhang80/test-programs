package cn.edu.xmut.parameteredit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class GWFileEditor extends FileEditor {
	private String backuppath;
	private String modelpath;
	private String subfix;

	public GWFileEditor(String path, String backup) {
		modelpath = path;
		backuppath = backup;
		subfix = "gw";
	}

	public void modifyGWFiles(String[] parameter, double[] tindex) {
		try {
			// delete all related *.mgt files in the directory
			File dir = new File(modelpath);
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(subfix) && !name.startsWith("output");
				}
			};
			File[] files = dir.listFiles(filter);
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
			File backupdir = new File(backuppath);
			File[] backupfiles = backupdir.listFiles(filter);
			OutputStream oStream = null;
			FileInputStream in2 = null;
			for (int i = 0; i < backupfiles.length; i++) {
				try {
					in2 = new FileInputStream(backupfiles[i]);
					List lines = IOUtils.readLines(in2);
					// =========================================
					// multi parameters need to be modified
					for (int j = 0; j < parameter.length; j++) {
						if (parameter[j].contains("ALPHA_BF")) {
							// 5
							// 0.0480 | ALPHA_BF : BAseflow alpha factor [days]
							String tp = (String) lines.get(4);
							String values = tp.substring(0, tp.indexOf("|"));
							double v = Double.parseDouble(values.trim());
							v = ParameterUtil.transfort(parameter[j], v,
									tindex[j]);
							String vsString = ParameterUtil
									.doubleToString(v, 4);
							String padded = "                "
									.substring(vsString.trim().length())
									+ vsString.trim()
									+ "    | ALPHA_BF : BAseflow alpha factor [days]";
							lines.set(4, padded);
						} else if (parameter[j].contains("RCHRG_DP")) {
							// 9
							// 0.0500 | RCHRG_DP : Deep aquifer percolation
							// fraction
							String tp = (String) lines.get(8);
							String values = tp.substring(0, tp.indexOf("|"));
							double v = Double.parseDouble(values.trim());
							v = ParameterUtil.transfort(parameter[j], v,
									tindex[j]);
							String vsString = ParameterUtil
									.doubleToString(v, 4);
							String padded = "                "
									.substring(vsString.trim().length())
									+ vsString.trim()
									+ "    | RCHRG_DP : Deep aquifer percolation fraction";
							lines.set(8, padded);
						} else if (parameter[j].contains("GW_DELAY")) {
							// 4
							// 31.0000 | GW_DELAY : Groundwater delay [days]
							int line = 3;
							String tp = (String) lines.get(line);
							String values = tp.substring(0, tp.indexOf("|"));
							double v = Double.parseDouble(values.trim());
							v = ParameterUtil.transfort(parameter[j], v,
									tindex[j]);
							String vsString = ParameterUtil
									.doubleToString(v, 4);
							String padded = "                "
									.substring(vsString.trim().length())
									+ vsString.trim()
									+ "    | GW_DELAY : Groundwater delay [days]";
							lines.set(line, padded);
						} else if (parameter[j].contains("GWQMN")) {
							// 6
							// 0.0000 | GWQMN : Threshold depth of water in the
							// shallow aquifer required for return flow to occur
							// [mm]
							int line = 5;
							String tp = (String) lines.get(line);
							String values = tp.substring(0, tp.indexOf("|"));
							double v = Double.parseDouble(values.trim());
							v = ParameterUtil.transfort(parameter[j], v,
									tindex[j]);
							String vsString = ParameterUtil
									.doubleToString(v, 4);
							String padded = "                "
									.substring(vsString.trim().length())
									+ vsString.trim()
									+ "    | GWQMN : Threshold depth of water in the shallow aquifer required for return flow to occur [mm]";
							lines.set(line, padded);
						} else if (parameter[j].contains("GW_REVAP")) {
							// 7
							// 0.0200 | GW_REVAP : Groundwater "revap"
							// coefficient
							int line = 6;
							String tp = (String) lines.get(line);
							String values = tp.substring(0, tp.indexOf("|"));
							double v = Double.parseDouble(values.trim());
							v = ParameterUtil.transfort(parameter[j], v,
									tindex[j]);
							String vsString = ParameterUtil
									.doubleToString(v, 4);
							String padded = "                "
									.substring(vsString.trim().length())
									+ vsString.trim()
									+ "    | GW_REVAP : Groundwater \"revap\" coefficient";
							lines.set(line, padded);
						} else if (parameter[j].contains("REVAPMN")) {
							// 8
							// 1.0000 | REVAPMN: Threshold depth of water in the
							// shallow aquifer for "revap" to occur [mm]
							int line = 7;
							String tp = (String) lines.get(line);
							String values = tp.substring(0, tp.indexOf("|"));
							double v = Double.parseDouble(values.trim());
							v = ParameterUtil.transfort(parameter[j], v,
									tindex[j]);
							String vsString = ParameterUtil
									.doubleToString(v, 4);
							String padded = "                "
									.substring(vsString.trim().length())
									+ vsString.trim()
									+ "    | REVAPMN: Threshold depth of water in the shallow aquifer for \"revap\" to occur [mm]";
							lines.set(line, padded);
						}
					}
					// ==================================================
					oStream = new FileOutputStream(modelpath
							+ backupfiles[i].getName());

					/*System.out.println(modelpath + backupfiles[i].getName());*/
					IOUtils.writeLines(lines, null, oStream);
				} catch (Exception e) {
					// logDebugInfo(e.toString());
					e.printStackTrace();
				} finally {
					IOUtils.closeQuietly(in2);
					IOUtils.closeQuietly(oStream);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void modifyFiles(String[] parameter, double[] tindex) {
		this.modifyGWFiles(parameter, tindex);

	}
}
