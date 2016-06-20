package testcode;
public class ForwardCalculator {
	private SpeedKeeper spKeeper;
	private SpeedMeasure spMeasure;

	private float preDiff;

	public float curspeed;
	public float targetspeed;

	public ForwardCalculator(EV3Body body){
		spKeeper = new SpeedKeeper();
		spMeasure = new SpeedMeasure(body);

		preDiff = 0.0F;
		this.curspeed = 0.0F;
		this.targetspeed = 0.0F;
	}

	public float calForward() {
		float kp = 0.0F;
		float forward;
		forward = kp*(spKeeper.getTarget() - spMeasure.getSpeed()) + 30.0F;

		return forward;
	}

	public float caldelForward(){
		float kp = 0.01F;
		float kd = -0.01F;

		curspeed = spMeasure.getSpeed();
		targetspeed = spKeeper.getTarget();

		float diff = targetspeed - curspeed;

		float deltaFor = kp * diff + kd * (diff - preDiff) / 0.004F; //PD制御 diffを微分する際のDeltaTimeは0.004[ms]と仮定してある

		preDiff = diff;

		return deltaFor;
	}

}
