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
		SampleProvider fargeLeser = fargeSensor.getRedMode();  // svart = 0.01..
		float[] fargeSample = new float[fargeLeser.sampleSize()];  // tabell som innholder avlest verdi

		// Registrerer differentialPilot
		DifferentialPilot pilot = new DifferentialPilot(56, 120, Motor.B, Motor.C, false);
		pilot.setTravelSpeed(80);
		pilot.setRotateSpeed(150);

	    double svart1 = 0.43;
		double svart2 = 0.03;
		int[] count = {0,0,0};

		while (true) {
			lysLeser.fetchSample(lysSample, 0);
			fargeLeser.fetchSample(fargeSample, 0);
			if (fargeSample[0] < svart2) {
				if(count[2] < 2) {
					System.out.println("Høyre!");
					pilot.rotateRight();
					count[1] = 0;
					count[0]++;
					if(count[0] == 3) {
						count[2]++;
						count[0] = 0;
					}
				} else {
					pilot.travel(50);
					count[2] = 0;
				}
			} else if (lysSample[0] < svart1) {
				if(count[2] < 2) {
					System.out.println("Venstre!");
					pilot.rotateLeft();
					count[0] = 0;
					count[1]++;
					if(count[1] == 3) {
						count[2]++;
						count[1] = 0;
					}
				} else {
					pilot.travel(50);
					count[2] = 0;
				}
			} else {
				System.out.println("Fremover!");
				pilot.forward();
			}
			System.out.println(lysSample[0]);
			Thread.sleep(200);
		}
	}
}
