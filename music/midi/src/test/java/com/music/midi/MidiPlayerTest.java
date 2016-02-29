package com.music.midi;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.music.midi.MidiPlayer;

public class MidiPlayerTest {
	static MidiPlayer mp = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mp = new MidiPlayer();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPlayerMidi() throws Exception {
		mp.playerMidi("D:\\金山快盘\\音乐识别\\素材\\01.mid");
	}

	@Test
	public void testAnalyMidi() throws Exception {
		mp.analyMidi("D:\\01.mid");
	}

	@Test
	public void testReadMidi() throws IOException {
		FileInputStream input = new FileInputStream("d:\\01.mid");
		InputStreamReader reader = new InputStreamReader(input);
		BufferedReader buffer = new BufferedReader(reader);
		while (true) {

			String line = buffer.readLine();
			System.out.println(line);
			break;
		}

	}
}
