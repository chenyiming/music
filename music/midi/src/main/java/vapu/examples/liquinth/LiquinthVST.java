package vapu.examples.liquinth;

import im.composer.vapu.VAPU;

import javax.sound.midi.MidiMessage;

import org.jaudiolibs.audioservers.AudioConfiguration;

public class LiquinthVST extends VAPU {
	private static final int NUM_PROGRAMS = 16, MIX_BUF_FRAMES = 1024;

	private Synthesizer synthesizer;
	private AudioSource audio_source;
	private MidiReceiver midi_receiver;
	private Program[] programs;
	private int current_program;
	private int[] mix_buf;

	@Override
	public synchronized void configure(AudioConfiguration context) throws Exception {
		super.configure(context);
		Liquinth liquinth = new Liquinth((int) context.getSampleRate());
		synthesizer = liquinth;
		audio_source = liquinth;
		midi_receiver = new MidiReceiver(synthesizer);

		programs = new Program[NUM_PROGRAMS];
		for (int prg_idx = 0; prg_idx < NUM_PROGRAMS; prg_idx++) {
			programs[prg_idx] = new Program("Blank " + prg_idx, synthesizer);
		}

		mix_buf = audio_source.allocate_mix_buf(context.getMaxBufferSize());

	}

	private void set_controller(int ctrl_idx, int value) {
		synthesizer.set_controller(ctrl_idx, value);
	}

	public void setSampleRate(float sample_rate) {
		audio_source.set_sampling_rate((int) sample_rate);
	}

	public void setProgram(int index) {
		if (index < 0 || index >= NUM_PROGRAMS)
			return;
		programs[current_program].store();
		current_program = index;
		programs[current_program].load();
	}

	public void setParameter(int index, float value) {
		set_controller(index, (int) Math.round(value * 127));
	}

	public float getParameter(int index) {
		return synthesizer.get_controller(index) / 127f;
	}

	public void setProgramName(String name) {
		programs[current_program].name = name;
	}

	public String getProgramName() {
		return programs[current_program].name;
	}

	public String getParameterLabel(int index) {
		return "";
	}

	public String getParameterDisplay(int index) {
		return "";
	}

	public String getParameterName(int index) {
		return synthesizer.get_controller_name(index);
	}

	public String getProgramNameIndexed(int category, int index) {
		if (index < 0 || index >= NUM_PROGRAMS)
			return "";
		return programs[index].name;
	}

	/* Deprecated as of VST 2.4 */
	public boolean copyProgram(int dest_idx) {
		if (dest_idx < 0 || dest_idx >= NUM_PROGRAMS)
			return false;
		programs[dest_idx] = new Program(programs[current_program]);
		return true;
	}

	public String getEffectName() {
		return Liquinth.VERSION;
	}

	public String getVendorString() {
		return Liquinth.AUTHOR;
	}

	public String getProductString() {
		return Liquinth.VERSION;
	}

	public int getNumPrograms() {
		return NUM_PROGRAMS;
	}

	public int getNumParams() {
		return synthesizer.get_num_controllers();
	}

	public boolean setBypass(boolean value) {
		return false;
	}

	public int getProgram() {
		return current_program;
	}

	public boolean string2Parameter(int index, String value) {
		try {
			float float_value = Float.parseFloat(value);
			setParameter(index, float_value);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		midi_receiver.send(message, timeStamp);
	}

	/* Deprecated as of VST 2.4 */
	public void process(float[][] inputs, float[][] outputs, int frames) {
		float[] output = outputs[0];
		int out_idx = 0;
		while (frames > 0) {
			int length = frames;
			if (length > MIX_BUF_FRAMES)
				length = MIX_BUF_FRAMES;
			audio_source.get_audio(mix_buf, length);
			for (int mix_idx = 0; mix_idx < length; mix_idx++) {
				float out = mix_buf[mix_idx];
				output[out_idx++] += out * 0.00003f;
			}
			frames -= length;
		}
	}

	public void processReplacing(float[][] inputs, float[][] outputs, int frames) {
		float[] output = outputs[0];
		int out_idx = 0;
		while (frames > 0) {
			int length = frames;
			if (length > MIX_BUF_FRAMES)
				length = MIX_BUF_FRAMES;
			audio_source.get_audio(mix_buf, length);
			for (int mix_idx = 0; mix_idx < length; mix_idx++) {
				float out = mix_buf[mix_idx];
				output[out_idx++] = out * 0.00003f;
			}
			frames -= length;
		}
	}

	@Override
	protected void executeCommand(long timeStamp, String command) {

	}

	@Override
	protected void computeBuffer() {
		try {
			audio_source.get_audio(mix_buf, buf.getSampleCount());
			float[] arr = new float[buf.getSampleCount()];
			for(int i=0;i<arr.length;i++){
				arr[i] = mix_buf[i]* 0.00003f;
			}
			for (int i = 0; i < buf.getChannelCount(); i++) {
				buf.setRawChannel(i, arr);
			}
		} catch (Exception e) {
		}
	}

}

class Program {
	public String name = "";
	private int[] controllers;
	private Controls controls;

	public Program(String name, Controls controls) {
		this.name = name;
		controllers = new int[controls.get_num_controllers()];
		set_controls(controls);
		store();
	}

	public Program(Program program) {
		name = program.name;
		controls = program.controls;
		controllers = new int[program.controllers.length];
		for (int idx = 0; idx < controllers.length; idx++) {
			controllers[idx] = program.controllers[idx];
		}
	}

	public void load() {
		for (int idx = 0; idx < controllers.length; idx++) {
			controls.set_controller(idx, controllers[idx]);
		}
	}

	public void store() {
		for (int idx = 0; idx < controllers.length; idx++) {
			controllers[idx] = controls.get_controller(idx);
		}
	}

	public void set_controls(Controls controls) {
		if (controls.get_num_controllers() != controllers.length) {
			throw new IllegalArgumentException("Number of controllers differ.");
		}
		this.controls = controls;
	}
}
