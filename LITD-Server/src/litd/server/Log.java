package litd.server;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log {
	static Log singleton;
	
	public Log() throws IOException
	{
		singleton = this;
		
		writer = new FileWriter("log.log");
	}
	
	FileWriter writer;
	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy hh:mm:ss a");
	
	public static synchronized void log(String message)
	{
		try {
			String outMsg = singleton.formatter.format(Calendar.getInstance().getTime()) + ": " + message + "\n";
			singleton.writer.write(outMsg);
			singleton.writer.flush();
			System.out.print(outMsg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
