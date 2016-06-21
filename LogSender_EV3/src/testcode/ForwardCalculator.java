package testcode;
public class ForwardCalculator {
	private SpeedKeeper spKeeper;
	private SpeedMeasure spMeasure;

	private float preDiff;
	public float interDiff;

	long premilliSec;

	public float curspd = 0.0F;

	public ForwardCalculator(EV3Body body){
		spKeeper = new SpeedKeeper();
		spMeasure = new SpeedMeasure(body);

		preDiff = 0.0F;
		interDiff = 0.0F;
		premilliSec = 0;
	}

	public float calForward() {
		float kp = 0.0F;
		float forward;
		forward = kp*(spKeeper.getTarget() - spMeasure.getSpeed()) + 30.0F;

		return forward;
	}

	public float caldelForward(){
		spKeeper.setTarget(0.5F);

		float kp = 0.008F;
		float kd = -0.0F;
		float ki = 0.0F;

		long milliSec = System.currentTimeMillis();
		float timeDiff = (float)((int)milliSec - (int)premilliSec);

		curspd = spMeasure.getSpeed();

		float diff = spKeeper.getTarget() - curspd;

		interDiff += diff * timeDiff;

		float deltaFor = kp * diff + kd * (diff - preDiff) / timeDiff + ki * interDiff ; //PID制御

		preDiff = diff;

		return deltaFor;
	}

}
