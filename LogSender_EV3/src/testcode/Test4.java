package testcode;

import java.util.Timer;

import ev3Viewer.LogSender;
import lejos.hardware.Battery;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.BasicMotorPort;
import lejos.utility.Delay;

public class Test4 {
	//定数
	private static final float GYRO_OFFSET          = 0.0F;
	private static final int   TAIL_ANGLE_STAND_UP  = 94;
	private static final int   TAIL_ANGLE_BALANCE     = 3;
	private static final double WHEEL_DISTANCE = 0.175; // 車輪間の距離
	private static final double WHEEL_DIAMETER = 0.085; // 車輪の直径
	private static final float P_GAIN               = 2.5F;
	private static final int   PWM_ABS_MAX          = 60;

	private static EV3Body body = new EV3Body();
	private static LogSender sender = new LogSender();

	// 走行状態
	private static double distanceDefault = 0;
	private static int timeDefault = 0;

	/** 走行速度 */
	private static float speed = 0;
	/** 回転速度     +が右回転 */
	private static float turn = 0;
	/** 尻尾角度 */
	private static int tail = 0;
	/** ライントレースしきい値 */
	private static float LTthreshold = 0.2f;
	/** ライントレース乗数 */
	private static float LTmultiplier = (150);


	// 経過時間関連
	private static long time4ms;
	private static long starttime;
	//private static char waitint = 0;


	public static void main(String[] args){
		init();

		body.motorPortL.resetTachoCount();   // left motor encoder reset
		body.motorPortR.resetTachoCount();   // right motor encoder reset
		Balancer.init();            // inverted pendulum control initialization
		Sound.beep();
		sender.connect();
		for(int i=0;i<1500;i++){
			sender.addLog("Speed4", -1, (System.nanoTime()-starttime)/1000000);
		}
		sender.clear();
		LCD.drawString("ready", 0, 0);
		Sound.beep();

		int hogecount = 0;

		boolean touchPressed = false;
		for (;;) {
			tailControl(body, TAIL_ANGLE_STAND_UP); //complete stop for angle control

			if(hogecount > 100){
				break;
			}
			hogecount++;

			Delay.msDelay(20);
		}

		DriveTask driveTask = new DriveTask(sender,body);
		Timer timer = new Timer();

		timer.scheduleAtFixedRate(driveTask, 0, 4);

		int count = 0;

		while(true){
			count++;
			if(body.touchSensorIsPressed()){
				if(count > 200000){
					driveTask.cancel();
					timer.cancel();
					break;
				}
			}

		}

//		speed = 0;
//		turn = 0;
//		tail = TAIL_ANGLE_BALANCE;
//		runByTail();
		sender.send();
	}



	private static void init(){
		body.gyro.reset();
		body.sonar.enable();
		body.motorPortL.setPWMMode(BasicMotorPort.PWM_BRAKE);
		body.motorPortR.setPWMMode(BasicMotorPort.PWM_BRAKE);
		body.motorPortT.setPWMMode(BasicMotorPort.PWM_BRAKE);

		for (int i=0; i < 1500; i++) {
			body.motorPortL.controlMotor(0, 0);
			body.motorPortR.controlMotor(0, 0);
			body.motorPortT.controlMotor(0, 0);
			body.getBrightness();
			body.getSonarDistance();
			body.getGyroValue();
			Battery.getVoltageMilliVolt();
			Balancer.control(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 8000);
			System.nanoTime();
		}
		Delay.msDelay(1000);       // In another thread wait until the wonder time that HotSpot is complete.

		body.motorPortL.controlMotor(0, 0);
		body.motorPortR.controlMotor(0, 0);
		body.motorPortT.controlMotor(0, 0);
		body.motorPortL.resetTachoCount();   // left motor encoder reset
		body.motorPortR.resetTachoCount();   // right motor encoder reset
		body.motorPortT.resetTachoCount();   // tail motor encoder reset
		Balancer.init();            // inverted pendulum control initialization
	}

	private static final void tailControl(EV3Body body, int angle) {
		float pwm = (float)(angle - body.motorPortT.getTachoCount()) * P_GAIN; // proportional control
		// PWM output saturation processing
		if (pwm > PWM_ABS_MAX) {
			pwm = PWM_ABS_MAX;
		} else if (pwm < -PWM_ABS_MAX) {
			pwm = -PWM_ABS_MAX;
		}
		body.motorPortT.controlMotor((int)pwm, 1);
	}

	/** カラーセンサの値からturn値を決定する */
	private static void setTurnByLT(){
		turn = (LTthreshold - body.getBrightness()) * LTmultiplier;
	}

	/** 倒立振子を用いてモータを動作させる */
	private static void runByBalance(){
		Balancer.control(speed, turn, body.getGyroValue(), GYRO_OFFSET, body.motorPortL.getTachoCount(), body.motorPortR.getTachoCount(), Battery.getVoltageMilliVolt());
		body.motorPortL.controlMotor(Balancer.getPwmL(), 1);
		body.motorPortR.controlMotor(Balancer.getPwmR(), 1);
		tailControl(body, tail);
	}

	/** 倒立振子を用いずにモータを動作させる */
	private static void runByTail(){
		body.motorPortL.controlMotor((int) clamp(speed-turn,100,-100), 1);
		body.motorPortR.controlMotor((int) clamp(speed+turn,100,-100), 1);
		tailControl(body, tail);
	}

	/** checkDistanceInit()が呼び出されてからの距離が目標距離より大きいか
	 * @param target 目標距離 必ず正
	 * @return 目標値より測った距離のほうが大きい時true */
	private static boolean checkDistance(double target){
		/*if(distanceTarget > 0){
			if(((Encoder.encoderL() + Encoder.encoderR()) / 2 - distanceDefault) > distanceTarget)return true;
		} else{
			if(((Encoder.encoderL() + Encoder.encoderR()) / 2 - distanceDefault) < distanceTarget)return true;
		}*/
		if(Math.abs(calcDistance() - distanceDefault) > target)return true;
		return false;
	}

	/** 目標位置との差を計算する
	 * @return 目標位置との差
	 * 目標値より進んでいる場合は正、 後ろの場合は負*/
	private static double checkDistanceDifference(double target){
		return calcDistance() - distanceDefault - target;
	}

	/** 距離リセット */
	private static void checkDistanceInit(){
		distanceDefault = calcDistance();
	}

	private static boolean checkTime(int target){
		if((System.nanoTime() / 1000000 - timeDefault) > target)return true;
		return false;
	}

	private static void checkTimeInit(){
		timeDefault = (int) (System.nanoTime() / 1000000);
	}
/** 最後にこのメソッドが呼ばれてから4ms経過するまで待機 */
	private static void wait4ms(){
		long time = System.nanoTime();
		if((time - time4ms) > 4000000 ){
			time4ms = time;
			return;
		}
		while(true){
			time = System.nanoTime();
			if((time - time4ms) > 4000000 ){
				time4ms += 4000000;
				break;
			}
		}
	}

/** 走行距離の計算 */
	private static double calcDistance(){
		return Math.PI * WHEEL_DIAMETER * (body.motorPortL.getTachoCount() + body.motorPortR.getTachoCount()) / 2;
	}

/** 旋回角度の計算 */
	private static double calcRotation(){
		return (body.motorPortL.getTachoCount() - body.motorPortR.getTachoCount()) * 0.245;
	}

	private static double clamp(double value, double max, double min){
		return Math.max(min, Math.min(value,max));
	}

	private static double clamp(double value, double maxmin){
		double mm = Math.abs(maxmin);
		return clamp(value,mm,-mm);
	}


}