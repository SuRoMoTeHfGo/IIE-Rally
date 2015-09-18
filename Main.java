import lejos.hardware.motor.*;
import lejos.hardware.lcd.*;
import lejos.hardware.*;
import lejos.hardware.port.Port;
import lejos.hardware.ev3.EV3;
import lejos.robotics.SampleProvider;
import lejos.hardware.sensor.*;


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
		
	    double gray = 0;
	    for (int i = 0; i<100; i++){
	    	fargeLeser1.fetchSample(fargeSample1, 0);
			fargeLeser2.fetchSample(fargeSample2, 0);
	    	gray = (fargeSample1[0] + fargeSample2[0]) / 2 * 100;
			System.out.println(String.format("Venstre side: %.2f", fargeSample1[0] * 100));
			System.out.println(String.format("Høyre side: %.2f", fargeSample2[0] * 100));
			System.out.println(String.format("Gjennomsnitt: %.2f", gray));
			Thread.sleep(1500);
	    }
	}
}