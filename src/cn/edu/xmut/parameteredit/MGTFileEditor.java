package cn.edu.xmut.parameteredit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class MGTFileEditor extends FileEditor {
	private  String backuppath;
	private String modelpath;
	private  String subfix;

	public MGTFileEditor(String path, String backup) {
		modelpath = path;
		backuppath = backup;
		subfix = "mgt";
	}

	public void modifyMGTFiles(String[] parameter, double[] tindex) {
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
						if (parameter[j].contains("CN2")) {
							// 11
							// 73.00 | CN2: Initial SCS CN II value
							String tp = (String) lines.get(10);
							String values = tp.substring(0, tp.indexOf("|"));
							double v = Double.parseDouble(values.trim());
							v = ParameterUtil.transfort(parameter[j], v,
									tindex[j]);

							//
							if (v > 98) {
								v = 97;
							} else if (v < 35) {
								v = 36;
							}
							//

							String vsString = ParameterUtil
									.doubleToString(v, 2);
							String padded = "                "
									.substring(vsString.trim().length())
									+ vsString.trim()
									+ "    | CN2: Initial SCS CN II value";
							lines.set(10, padded);

						} else if (parameter[j].contains("other")) {
						}
					}
					// ==================================================
					oStream = new FileOutputStream(modelpath
							+ backupfiles[i].getName());
					/*System.out.println(modelpath + backupfiles[i].getName());

					if (lines == null || lines.size() <= 0) {
						System.out.println("aaaaaaaaaaaaaaaaaa");
					}*/
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
		this.modifyMGTFiles(parameter, tindex);

	}
}
