package testcode;
public class ForwardCalculator {
	private SpeedKeeper spKeeper;
	private SpeedMeasure spMeasure;

	public ForwardCalculator(EV3Body body){
		spKeeper = new SpeedKeeper();
		spMeasure = new SpeedMeasure(body);
	}

	public float calForward() {
		float kp = 200.0F;
		float forward;
		forward = kp*(spKeeper.getTarget() - spMeasure.getSpeed()) + 30.0F;
		return forward;
	}

}
