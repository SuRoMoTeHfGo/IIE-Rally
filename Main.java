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
 		Port s2 = brick.getPort("S2"); // trykksensor
		NXTLightSensor lysSensor = new NXTLightSensor(s1); // NXT-lyssensor
		EV3ColorSensor fargeSensor = new EV3ColorSensor(s2); // EV3-fargesensor

		/* Definerer en lyssensor */
		SampleProvider lysLeser = lysSensor;  //
		float[] lysSample = new float[lysLeser.sampleSize()];  // tabell som innholder avlest verdi
		// readNormalizedValue()


		/* Definerer en fargesensor */
		SampleProvider fargeLeser = fargeSensor.getMode("RGB");  // svart = 0.01..
		float[] fargeSample = new float[fargeLeser.sampleSize()];  // tabell som innholder avlest verdi

		// Registrerer differentialPilot
		DifferentialPilot pilot = new DifferentialPilot(56, 120, Motor.B, Motor.C, false);
		pilot.setRotateSpeed(10);

	    double svart1 = 0.43; // verdi for svart på lysSensor
		double svart2 = 0.03; // verdi for svart på fargeSensor

		while (true) {
			lysLeser.fetchSample(lysSample, 0);
			fargeLeser.fetchSample(fargeSample, 0);
			if (lysSample[0] < svart1 && fargeSample[0] < svart2) {
				// Gjør noe
				System.out.println("GJØR NOE!!!!");
				pilot.forward();
			} else if (lysSample[0] < svart1 && fargeSample[0] > svart2) {
				System.out.println("Venstre!");
				pilot.setTravelSpeed(100);
				pilot.rotateLeft();
			} else if (lysSample[0] > svart1 && fargeSample[0] < svart2) {
				System.out.println("Høyre!");
				pilot.setTravelSpeed(100);
				pilot.rotateRight();
			} else {
				System.out.println("Fremover!");
				pilot.setTravelSpeed(180);
				pilot.forward();
			}
		}
	}
}
