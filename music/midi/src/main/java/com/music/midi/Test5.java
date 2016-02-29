package com.music.midi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

public class Test5 implements Runnable {
	private Sequencer midi;
	private String[] names = { "01.mid", "02.mid", "03.mid" };
	private int i;
	private Map<String, Sequence> map;

	public Test5() {
		initMap();
		new Thread(this).start();
	}

	private void initMap() {
		try {
			map = new Hashtable<String, Sequence>();
			midi = MidiSystem.getSequencer(false);
			midi.open();
			for (String s : names) {
				try {
					Sequence s1 = MidiSystem.getSequence(new File("D:/金山快盘/音乐识别/素材/"+s));
					map.put(s, s1);
				} catch (InvalidMidiDataException ex) {
					Logger.getLogger(Test5.class.getName()).log(Level.SEVERE,
							null, ex);
				} catch (IOException ex) {
					Logger.getLogger(Test5.class.getName()).log(Level.SEVERE,
							null, ex);
				}
			}
		} catch (MidiUnavailableException ex) {
			Logger.getLogger(Test5.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	private void createPlayer(String name) {
		try {
			Sequence se = map.get(name);
			midi.setSequence(se);
			midi.start();

		} catch (InvalidMidiDataException ex) {
			Logger.getLogger(Test5.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public void run() {
		while (true) {
			try {
				System.out.println("换文件了." + (++i));
				String name = names[(int) (Math.random() * names.length)];
				createPlayer(name);
				Thread.sleep(10000);

			} catch (InterruptedException ex) {
				Logger.getLogger(Test5.class.getName()).log(Level.SEVERE, null,
						ex);
			}
		}
	}

	public static void main(String[] args) {
		new Test5();
	}
}