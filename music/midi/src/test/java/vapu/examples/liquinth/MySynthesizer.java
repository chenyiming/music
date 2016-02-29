package vapu.examples.liquinth;

public class MySynthesizer implements Synthesizer {

	@Override
	public void note_on(int key, int velocity) {
		// TODO Auto-generated method stub
		
		System.out.printf("key=%d;velocity=%d",key,velocity);

	}

	@Override
	public void all_notes_off(boolean sound_off) {
		// TODO Auto-generated method stub

	}

	@Override
	public int get_num_controllers() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String get_controller_name(int control) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int get_controller(int controller) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void set_controller(int controller, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void set_mod_wheel(int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void set_pitch_wheel(int value) {
		// TODO Auto-generated method stub

	}

}
