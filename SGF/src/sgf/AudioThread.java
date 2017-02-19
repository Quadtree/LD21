package sgf;

import java.applet.Applet;
import java.applet.AudioClip;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

public class AudioThread implements Runnable{

	ArrayBlockingQueue<String> audioNames;
	
	HashMap<String, AudioClip> audioFiles;
	
	boolean keepRun = true;
	
	Applet app;
	
	public void start()
	{
		Thread t = new Thread(this);
		t.start();
	}
	
	public void stop()
	{
		keepRun = false;
	}
	
	public void play(String soundName)
	{
		audioNames.add(soundName);
	}
	
	public AudioThread(Applet app)
	{
		this.app = app;
	}
	
	@Override
	public void run() {
		audioFiles = new HashMap<String, AudioClip>();
		audioNames = new ArrayBlockingQueue<String>(1024);
		while(keepRun)
		{
			while(!audioNames.isEmpty())
			{
				String name = audioNames.poll();
				
				if(!audioFiles.containsKey(name))
				{
					audioFiles.put(name, app.getAudioClip(app.getDocumentBase(), "./media/" + name + ".wav"));
				}
				audioFiles.get(name).play();
			}
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
			}
		}
	}
	
}
