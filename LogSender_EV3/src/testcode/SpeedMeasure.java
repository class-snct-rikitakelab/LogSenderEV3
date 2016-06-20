package testcode;

public class SpeedMeasure {

	private EV3Body body;

	private int oldTachoL;
	private int oldTachoR;
	//private long oldmilliSec;

	public SpeedMeasure(EV3Body body){
		this.body = body;
		oldTachoL = oldTachoR = 0;
		//oldmilliSec = System.currentTimeMillis();
	}

	public float getSpeed() {
		float speed;
		int tachoL;
		int tachoR;
		long milliSec;
		float dTacho;

		tachoL = body.motorPortL.getTachoCount();
		tachoR = body.motorPortR.getTachoCount();
		//milliSec = System.currentTimeMillis();

		dTacho = (float)((tachoL - oldTachoL) + (tachoR - oldTachoR)) / 2;
		//speed = (float)dTacho / ((float)milliSec - (float)oldmilliSec);

		speed = (float)dTacho/10;


		oldTachoL = tachoL;
		oldTachoR = tachoR;
		//oldmilliSec = milliSec;


		return speed;
	}

}