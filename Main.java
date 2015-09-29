import lejos.hardware.motor.*;
import lejos.hardware.lcd.*;
import lejos.hardware.*;
import lejos.hardware.port.Port;
import lejos.hardware.ev3.EV3;
import lejos.robotics.SampleProvider;
import lejos.hardware.sensor.*;
import lejos.robotics.navigation.DifferentialPilot;


public class Main{
	public static void main (String[] arg) throws Exception  {

		Brick brick = BrickFinder.getDefault();
    	Port s1 = brick.getPort("S1"); // fargesensor
		EV3ColorSensor fargeSensor = new EV3ColorSensor(s1); // EV3-fargesensor

		/* Definerer en fargesensor */
		SampleProvider fargeLeser = fargeSensor.getRedMode();  // svart = 0.01..
		float[] fargeSample = new float[fargeLeser.sampleSize()];  // tabell som innholder avlest verdi

		// Registrerer differentialPilot
		DifferentialPilot pilot = new DifferentialPilot(56, 120, Motor.B, Motor.C, false);
		pilot.setTravelSpeed(80);
		pilot.setRotateSpeed(150);

		double svart = 0.02;
		double hvit = 0.12;

		while (true) {
			fargeLeser.fetchSample(fargeSample, 0);
			int dir = 0;
			if(dir == 0) {
				while(fargeSample[0] > svart) {
					pilot.arcForward(5);
				}
				while(fargeSample[0] > svart) {
					pilot.arcForward(5);
				}
				dir = 1;
			}
			if (dir == 1) {
				while(fargeSample[0] < svart) {
					pilot.arcForward(-5);
				}
				while(fargeSample[0] < svart) {
					pilot.arcForward(-5);
				}
				dir = 0;
			}
		}
	}
}
