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
		SampleProvider fargeLeser = fargeSensor.getMode("RGB");  // svart = 0.01..
		float[] fargeSample = new float[fargeLeser.sampleSize()];  // tabell som innholder avlest verdi

		// Registrerer differentialPilot
		DifferentialPilot pilot = new DifferentialPilot(56, 120, Motor.B, Motor.C, false);
		pilot.setTravelSpeed(80);
		pilot.setRotateSpeed(150);

		// Mål svart og hvit og bruk følgende formel for å finne midtverdi
		// midtverdi = (hvit + svart) / 2
		double svart = 0.016;
		double hvit = 0.046;
		double midtverdi = (hvit + svart) / 2;

		while (true) {
			fargeLeser.fetchSample(fargeSample, 0);
			pilot.arc((midtverdi - fargeSample[0])*1000);
		}
	}
}
