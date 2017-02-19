package litd.lc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import litd.shared.LevelGeometry;

public class Converter {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		File trgDir = new File("../levels");
		for(File f : trgDir.listFiles())
		{
			System.out.println("Converting " + f.getName());
			ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream("../LITD-Client/media/" + f.getName().split("\\.")[0] + ".lvl"));
			ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream("../LITD-Server/media/" + f.getName().split("\\.")[0] + ".lvl"));
			
			LevelGeometry lg = new LevelGeometry(f);
			
			oos1.writeObject(lg);
			oos1.close();
			
			oos2.writeObject(lg);
			oos2.close();
		}
	}

}
