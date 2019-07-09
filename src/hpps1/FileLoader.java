package hpps1;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import com.pragprog.ahmine.ez.EZPlugin;

public class FileLoader extends EZPlugin{
	private File file;
	private ArrayList<String> ar;
	public FileLoader(File file) {
		if(file.exists()) {
			this.file = file;
			logger.info("[FileLoader] File vorhanden!");
		}else {
			logger.info("[FileLoader] File dont exist!");
			this.file = null;
		}
	}
	
	public ArrayList<String> readFile() {
		Scanner sc;
		ar = new ArrayList<>();
		try {
			if(file==null){
				logger.info("[FileLoader] File nicht lesbar!");
				return null;
				//throws Exception
			}
			sc = new Scanner(this.file);
			while(sc.hasNext()) {
				String sr = sc.next();
				ar.add(sr);
			}
			sc.close();
			return ar;
			
		} catch (FileNotFoundException e) {
			logger.info("[FileLoader] FILE NOT FOUND EXCEPTION");
		}
		return null;
	}
}
