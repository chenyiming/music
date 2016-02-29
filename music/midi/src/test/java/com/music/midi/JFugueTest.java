package com.music.midi;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;

import org.jfugue.midi.*;
import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.Token;
import org.jfugue.player.Player;
import org.jfugue.realtime.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.staccato.StaccatoParserListener;

import java.util.Scanner;

import javax.sound.midi.MidiUnavailableException;

import org.jfugue.realtime.RealtimePlayer;
import org.jfugue.theory.Note;

public class JFugueTest {

	String midifile = "d:\\01.mid";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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
	public void testPalyCB() {
		Player player = new Player();
		player.play("T120 TIME:4/4 V0 "
				//+ "A1qa100 Bb1qa100 B1qa100 C2qa100 C#2qa100 D2qa100 Eb2qa100 E2qa100 F2qa100 F#2qa100 G2qa100 G#2qa100 A2qa100 Bb2qa100 B2qa100 C3qa100 C#3qa100 D3qa100 Eb3qa100 E3qa100 F3qa100 F#3qa100 G3qa100 G#3qa100 A3qa100 Bb3qa100 B3qa100 C4qa100 C#4qa100 D4qa100 Eb4qa100 E4qa100 F4qa100 F#4qa100 G4qa100 G#4qa100 A4qa100 Bb4qa100 B4qa100 "
				+ "C5qa100 C#5qa100 "
				//+ "D5qa100 Eb5qa100 E5qa100 F5qa100 F#5qa100 G5qa100 G#5qa100 A5qa100 Bb5qa100 B5qa100"
				//+ " C6qa100 C#6qa100 D6qa100 Eb6qa100 E6qa100 F6qa100 F#6qa100 G6qa100 G#6qa100 A6qa100 Bb6qa100 B6qa100 C7qa100 C#7qa100 D7qa100 Eb7qa100 E7qa100 F7qa100 F#7qa100 G7qa100 G#7qa100 A7qa100 Bb7qa100 B7qa100 C8qa100 C#8qa100 D8qa100 Eb8qa100 E8qa100 F8qa100 F#8qa100 G8qa100 G#8qa100 A8qa100 Bb8qa100 B8qa100 C9qa100"
				);
	}

	@Test
	public void testSeeMidi() throws IOException, InvalidMidiDataException {
		Pattern pattern = MidiFileManager.loadPatternFromMidi(new File(
				this.midifile));
		System.out.println(pattern);
	}

	@Test
	public void testPlayMidi() throws InvalidMidiDataException, IOException {
		MyMidiParser parser = new MyMidiParser();
		StaccatoParserListener listener = new StaccatoParserListener();
		parser.addParserListener(listener);
		parser.parse(MidiSystem.getSequence(new File(this.midifile)));

		Pattern staccatoPattern = listener.getPattern();
		System.out.println(staccatoPattern);

		List<Token> ts = staccatoPattern.getTokens();
		for (Token t : ts) {
			System.out.println(t.getType() + "-->" + t.toString());
		}

		Player player = new Player();
		player.play(new Pattern("D5Q D5QA100"));
		//player.play(staccatoPattern);

	}

	@Test
	public void testPlayRealtimeMidi() throws Exception, IOException {
		RealtimePlayer player = new RealtimePlayer();
		Random random = new Random();
		Scanner scanner = new Scanner(System.in);
		boolean quit = false;
		while (quit == false) {
			System.out.print("Enter a '+C' to start a note, "
					+ "'-C' to stop a note, 'i' for a random instrument, "
					+ "'p' for a pattern, or 'q' to quit: ");
			String entry = scanner.next();
			if (entry.startsWith("+")) {
				player.startNote(new Note(entry.substring(1)));
			} else if (entry.startsWith("-")) {
				player.stopNote(new Note(entry.substring(1)));
			} else if (entry.equalsIgnoreCase("i")) {
				player.changeInstrument(random.nextInt(128));
			} else if (entry.equalsIgnoreCase("p")) {
				player.play(PATTERNS[random.nextInt(PATTERNS.length)]);
			} else if (entry.equalsIgnoreCase("q")) {
				quit = true;
			}
		}
		scanner.close();
		player.close();
	}

	Pattern[] PATTERNS = new Pattern[] {
			new Pattern("Cmajq Dmajq Emajq"),
			new Pattern("V0 Ei Gi Di Ci  V1 Gi Ci Fi Ei"),
			new Pattern("V0 Cmajq V1 Gmajq") };

}
