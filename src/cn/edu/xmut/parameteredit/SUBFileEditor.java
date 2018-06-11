package cn.edu.xmut.parameteredit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class SUBFileEditor extends FileEditor {
	private String backuppath;
	private String modelpath;
	private String subfix;

	public SUBFileEditor(String path, String backup) {
		modelpath = path;
		backuppath = backup;
		subfix = "sub";
	}

	public void modifySUBFiles(String[] parameter, double[] tindex) {
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
						if (parameter[j].contains("CH_K1")) {
							int line = 27;
							// 28
							// 0.500 | CH_K1 : Effective hydraulic conductivity
							// in tributary channel [mm/hr]
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
									+ "    | CH_K1 : Effective hydraulic conductivity in tributary channel [mm/hr]";
							lines.set(line, padded);

						} else if (parameter[j].contains("CH_N1")) {

							int line = 28;
							// 29
							// 0.014 | CH_N1 : Manning's "n" value for the
							// tributary channels
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
									+ "    | CH_N1 : Manning's \"n\" value for the tributary channels";
							lines.set(line, padded);

						}
					}
					// ==================================================
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
		this.modifySUBFiles(parameter, tindex);

	}
}
