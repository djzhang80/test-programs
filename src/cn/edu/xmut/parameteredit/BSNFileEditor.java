package cn.edu.xmut.parameteredit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class BSNFileEditor extends FileEditor {
	private  String backuppath;
	private  String modelpath;
	private  String subfix;

	public BSNFileEditor(String path,String backup) {
		modelpath = path;
		backuppath = backup;
		subfix = "bsn";
	}

	public void modifyBSNFiles(String parameter[], double tindex[]) {
		try {
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
					for (int j = 0; j < parameter.length; j++) {
						if (parameter[j].contains("SURLAG")) {
							//20
							//           4.000    | SURLAG : Surface runoff lag time [days]
							String tp = (String) lines.get(19);
							String values = tp.substring(0, tp.indexOf("|"));
							double v = Double.parseDouble(values.trim());
							v = ParameterUtil.transfort(parameter[j], v,
									tindex[j]);
							String vsString = ParameterUtil
									.doubleToString(v, 3);
							String padded = "                "
									.substring(vsString.trim().length())
									+ vsString.trim()
									+ "    | SURLAG : Surface runoff lag time [days]";
							lines.set(19, padded);
						} else if (parameter[j].contains("ESCO")) {
							//13
							//           0.950    | ESCO: soil evaporation compensation factor
							int line = 12;
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
									+ "    | ESCO: soil evaporation compensation factor";
							lines.set(line, padded);
						} else if (parameter[j].contains("ESCO")) {
							//14
							//           1.000    | EPCO: plant water uptake compensation factor
							int line = 13;
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
									+ "    | EPCO: plant water uptake compensation factor";
							lines.set(line, padded);
						}
					}
					oStream = new FileOutputStream(modelpath
							+ backupfiles[i].getName());
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
		this.modifyBSNFiles(parameter, tindex);
		
	}
}
