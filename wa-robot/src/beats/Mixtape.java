package beats;

import java.io.File;

import lejos.nxt.Sound;

public class Mixtape {

	public static void play() {
		new Thread() {
			@Override
			public void run() {
				File file = new File("junction.wav");
				Sound.playSample(file);
				p(123,100);
			}
		}.run();
	}
	
	private static void p(int f, int d){
		Sound.playTone(f, d);
	}

}
