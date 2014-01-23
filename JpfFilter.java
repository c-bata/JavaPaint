import java.io.File;
import javax.swing.filechooser.FileFilter;

public class JpfFilter extends FileFilter{
	public boolean accept(File f){
		if (f.isDirectory()){
			return true;
		}

		String ext = getExtension(f);
		if (ext != null){
			if (ext.equals("jpf")){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	public String getDescription(){
		return "JPFファイル";
	}

	private String getExtension(File f){
		String ext = null;
		String filename = f.getName();
		int dotIndex = filename.lastIndexOf('.');

		if ((dotIndex > 0) && (dotIndex < filename.length() - 1)){
			ext = filename.substring(dotIndex + 1).toLowerCase();
		}
		return ext;
	}
}
