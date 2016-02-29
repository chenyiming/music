package com.music.midi;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.sound.midi.*;

import org.staccato.StaccatoUtil;

import vapu.examples.liquinth.MidiReceiver;
import vapu.examples.liquinth.MySynthesizer;

public class MidiPlayer {

	public void playerMidi(String filename) {
		try {
			// From file
			Sequence sequence = MidiSystem.getSequence(new File(filename));

			// From URL
			// sequence = MidiSystem.getSequence(new
			// URL("http://hostname/midifile"));

			// Create a sequencer for the sequence
			Sequencer midier = MidiSystem.getSequencer();
			midier.open();
			midier.setSequence(sequence);

			// Determining the Duration of a Midi Audio File
			double durationInSecs = midier.getMicrosecondLength() / 1000000.0;
			System.out.println("the duration of this audio is "
					+ durationInSecs + "secs.");

			// Determining the Position of a Midi Sequencer
			double seconds = midier.getMicrosecondPosition() / 1000000.0;
			System.out.println("the Position of this audio is " + seconds
					+ "secs.");

			// Setting the Volume of Playing Midi Audio
			if (midier instanceof Synthesizer) {
				Synthesizer synthesizer = (Synthesizer) midier;
				MidiChannel[] channels = synthesizer.getChannels();

				// gain is a value between 0 and 1 (loudest)
				double gain = 0.9D;
				for (int i = 0; i < channels.length; i++) {
					channels[i].controlChange(7, (int) (gain * 127.0));
				}
			}

			// Start playing
			midier.start();

			// Determining the Position of a Midi Sequencer
			Thread.currentThread().sleep(44000);
			seconds = midier.getMicrosecondPosition() / 1000000.0;
			System.out.println("the Position of this audio is " + seconds
					+ "secs.");

			// Add a listener for meta message events
			midier.addMetaEventListener(new MetaEventListener() {
				public void meta(MetaMessage event) {
					// Sequencer is done playing
					if (event.getType() == 47) {
						System.out.println("Sequencer is done playing.");
					}
				}
			});
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		} catch (MidiUnavailableException e) {
		} catch (InvalidMidiDataException e) {
		} catch (InterruptedException e) {
		}
	}

	public void analyMidi(String filename) throws Exception {
		// From file
		Sequence seq = MidiSystem.getSequence(new File(filename));

		Patch[] ps = seq.getPatchList();
		System.out.println(ps.length);
		for (Patch p : ps) {
			System.out.println(p.toString());
		}

		Track[] ts = seq.getTracks();
		System.out.println(ts.length);
		int ti = 1;
		for (Track track : ts) {
			System.out.println("轨道数" + (ti++));
			System.out.println("时间长度" + track.ticks());

			int size = track.size();
			System.out.println("事件数" + size);
			
			
			MidiReceiver rec=new MidiReceiver(new MySynthesizer());
			for (int i = 0; i < size; i++) {
				MidiEvent eve = track.get(i);
				MidiMessage mes = eve.getMessage();
				byte[] bs = mes.getMessage();
				
				rec.send(mes, 1000);
				
				
				System.out.print("\t ****  eve.getTick()=" + eve.getTick()
						+ "\tMidiMessage=" + mes.getClass().getName());
				System.out.print("\t" + mes.getLength() + ";state="
						+Integer.toHexString( mes.getStatus() )
						+"{comand:"+(mes.getStatus()& 0xF0)
						+",channel:"+(mes.getStatus()& 0x0F)+"}"
						
						+ ";len="+bs.length+"-->");
				
				
				String evtStr=StaccatoUtil.createPolyphonicPressureElement(bs[1], bs[2]);
				for (int j = 0; j < bs.length; j++)
					System.out.print(  Integer.toString(bs[j])+" ");
				System.out.print("   :"+evtStr);
				System.out.println();
				
			}
		}

	}
}
