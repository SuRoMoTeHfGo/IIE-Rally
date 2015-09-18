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
		NXTColorSensor fargesensor1 = new NXTColorSensor(s1); // EV3-fargesensor
		EV3ColorSensor fargesensor2 = new EV3ColorSensor(s2); // EV3-fargesensor

		/* Definerer en fargesensor */
		SampleProvider fargeLeser1 = fargesensor1.getMode("RGB");  // svart = 0.01..
		float[] fargeSample1 = new float[fargeLeser1.sampleSize()];  // tabell som innholder avlest verdi

		/* Definerer en fargesensor */
		SampleProvider fargeLeser2 = fargesensor2.getMode("RGB");  // svart = 0.01..
		float[] fargeSample2 = new float[fargeLeser2.sampleSize()];  // tabell som innholder avlest verdi

		// Registrerer differentialPilot
		DifferentialPilot pilot = new DifferentialPilot(56, 120, Motor.B, Motor.C, false);
		pilot.setTravelSpeed(80);
		pilot.setRotateSpeed(150);

	    int color1 = 0;
		int color2 = 0;
	    for (int i = 0; i<100; i++){
	    	fargeLeser1.fetchSample(fargeSample1, 0);
			fargeLeser2.fetchSample(fargeSample2, 0);
	    	color1 += fargeSample1[0] * 100;
			color2 += fargeSample2[0] * 100;
	    }
		color1 /= 100;
		color2 /= 100;

		while (true) {
			fargeLeser1.fetchSample(fargeSample1, 0);
			fargeLeser2.fetchSample(fargeSample2, 0);
			if (fargeSample2[0] * 100 < color2) {
				System.out.println("Høyre!");
				pilot.rotateRight();
			} else if (fargeSample1[0] * 100 < color1) {
				System.out.println("Venstre!");
				pilot.rotateLeft();
			} else {
				System.out.println("Fremover!");
				pilot.forward();
			}
			Thread.sleep(200);
		}
	}
}
