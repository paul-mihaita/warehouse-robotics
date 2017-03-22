package beats;

import java.io.File;

import lejos.nxt.Sound;

public class Mixtape {

	public static void play() {
		new Thread() {
			@Override
			public void run() {
				File file = new File("junction.wav");
				Sound.setVolume(Sound.VOL_MAX);
				Sound.playSample(file);
				p(432, 500);
				p(400, 500);
				p(350, 250);
			}
		}.run();
	}
	
	private static void p(int f, int d){
		Sound.playTone(f, d);
	}

}
