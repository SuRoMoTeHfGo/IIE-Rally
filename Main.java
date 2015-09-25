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
		pilot.setTravelSpeed(100);
		pilot.setRotateSpeed(100);

	    double svart1 = 0.43;
		double svart2 = 0.033;
		int hinder = 0;

		while (true) {
			lysLeser.fetchSample(lysSample, 0);
			fargeLeser.fetchSample(fargeSample, 0);
			if (lysSample[0] < svart1 && fargeSample[0] < svart2) {
				// Gjør noe med denne logikken
				switch (hinder) {
					case 0:
						System.out.println("Hinder 1");
						pilot.forward();
						Thread.sleep(50);
						pilot.rotateLeft();
						Thread.sleep(100);
						pilot.forward();
						Thread.sleep(50);
						hinder++;
						break;
					case 1:
							System.out.println("Hinder 2");
						int count = 0;
						while(count < 10) {
							if (fargeSample[0] < svart2) {
								pilot.rotateRight();
							} else {
								pilot.forward();
							}
							Thread.sleep(100);
							count++;
						}
						hinder++;
						break;
					case 2:
						System.out.println("Hinder 3");
						pilot.forward();
						Thread.sleep(50);
						pilot.rotateRight();
						Thread.sleep(100);
						pilot.forward();
						Thread.sleep(50);
						hinder++;
						break;
				}
			} else if (lysSample[0] < svart1 && fargeSample[0] > svart2) {
				pilot.rotateLeft();
			} else if (lysSample[0] > svart1 && fargeSample[0] < svart2) {
				pilot.rotateRight();
			} else {
				pilot.forward();
			}
		}
	}
}
