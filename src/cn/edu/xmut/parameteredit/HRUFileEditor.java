package cn.edu.xmut.parameteredit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class HRUFileEditor extends FileEditor {
	private  String backuppath;
	private  String modelpath;
	private  String subfix;

	public HRUFileEditor(String path,String backup) {
		modelpath = path;
		backuppath = backup;
		subfix = "hru";
	}

	public void modifyHRUFiles(String[] parameter, double[] tindex) {
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
						if (parameter[j].contains("OV_N")) {
							// 5
							//           0.140    | OV_N : Manning's "n" value for overland flow
							String tp = (String) lines.get(4);
							String values = tp.substring(0, tp.indexOf("|"));
							double v = Double.parseDouble(values.trim());
							v = ParameterUtil.transfort(parameter[j], v,
									tindex[j]);
							String vsString = ParameterUtil
									.doubleToString(v, 3);
							String padded = "                "
									.substring(vsString.trim().length())
									+ vsString.trim()
									+ "    | OV_N : Manning's \"n\" value for overland flow";
							lines.set(4, padded);
						} else if (parameter[j].contains("CANMX")) {
							//9
							//           0.000    | CANMX : Maximum canopy storage [mm]			
							int line = 8;
							String tp = (String) lines.get(line);
							String values = tp.substring(0, tp.indexOf("|"));
							double v = Double.parseDouble(values.trim());
							v = ParameterUtil.transfort(parameter[j], v,
									tindex[j]);
							String vsString = ParameterUtil
									.doubleToString(v, 3);
							String padded = "                "
									.substring(vsString.trim().length())
									+ vsString.trim()
									+ "    | CANMX : Maximum canopy storage [mm]";
							lines.set(line, padded);

						} else if (parameter[j].contains("EPCO")) {
							//11
							//           0.000    | EPCO : Plant uptake compensation factor	
							int line = 10;
							String tp = (String) lines.get(line);
							String values = tp.substring(0, tp.indexOf("|"));
							double v = Double.parseDouble(values.trim());
							v = ParameterUtil.transfort(parameter[j], v,
									tindex[j]);
							String vsString = ParameterUtil
									.doubleToString(v, 3);
							String padded = "                "
									.substring(vsString.trim().length())
									+ vsString.trim()
									+ "    | EPCO : Plant uptake compensation factor";
							lines.set(line, padded);

						} else if (parameter[j].contains("ESCO")) {
							//10
							//           0.000    | ESCO : Soil evaporation compensation factor		
							int line = 9;
							String tp = (String) lines.get(line);
							String values = tp.substring(0, tp.indexOf("|"));
							double v = Double.parseDouble(values.trim());
							v = ParameterUtil.transfort(parameter[j], v,
									tindex[j]);
							String vsString = ParameterUtil
									.doubleToString(v, 3);
							String padded = "                "
									.substring(vsString.trim().length())
									+ vsString.trim()
									+ "    | ESCO : Soil evaporation compensation factor";
							lines.set(line, padded);

						}
					}
					// ==================================================
					oStream = new FileOutputStream(modelpath
							+ backupfiles[i].getName());
					/*System.out.println(modelpath
							+ backupfiles[i].getName());*/
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
		this.modifyHRUFiles(parameter, tindex);

	}

}
