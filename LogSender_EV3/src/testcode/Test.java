package testcode;

import java.util.Timer;
import java.util.TimerTask;

import ev3Viewer.LogSender;
import lejos.hardware.Battery;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.BasicMotorPort;
import lejos.utility.Delay;

/**
 * Java sample program for leJOS EV3 of two-wheel inverted pendulum line trace robot.
 */
public class Test {
	// The following parameters will need to be tuned to suit the individual sensor / environment
	private static final float GYRO_OFFSET          = 0.0F; //gyro sensor offset value
	private static final int   TAIL_ANGLE_STAND_UP  = 94;   //  complete stop when the angle [degrees]
	private static final int   TAIL_ANGLE_DRIVE     = 3;    // balance during running of the angle [degrees]
	private static final float P_GAIN               = 2.5F; //  complete stop motor control proportionality coefficient
	private static final int   PWM_ABS_MAX          = 60;   // complete stop motor control PWM absolute maximum
	private static final float KP = 100.0F;
	private static final int   LOG_INTERVAL =  (int) (0.5 * 1000000000);

	private static EV3Body         body    = new EV3Body();
	private static boolean         isRight = true;
	private static boolean         isFront = false;
	private static int			turncounter = 0;
	private static int 			stopcounter = 0;
	private static int 			uscounter = 0;
	private static int 			seekrange = 0;
	private static float 		lastdistance = 0;
	private static boolean isConnected = false;
	private static long nowTime = 0;
	private static long lastTakeTime = 0;
	private static LogSender view;

	static boolean touch;
	static float distance;
	static float color;
	static float gyro;              // gyro sensor value
	static int tachoL;     //  left motor rotation angle
	static int tachoR;     // right motor rotation angle
	static int tachoT;     //  tail motor rotation angle
	//static LogData log = new LogData();

	/**
	 *  Main
	 */
	public static void main(String[] args) {

		LCD.drawString("Please Wait...  ", 0, 4);
		body.gyro.reset();
		body.sonar.enable();
		body.motorPortL.setPWMMode(BasicMotorPort.PWM_BRAKE);
		body.motorPortR.setPWMMode(BasicMotorPort.PWM_BRAKE);
		body.motorPortT.setPWMMode(BasicMotorPort.PWM_BRAKE);


		// Initial execution performance of Java is bad, it is not possible to obtain sufficient real-time to an inverted pendulum.
		// About the methods frequently used to traveling, to empty run until HotSpot is converted to native code.
		// Default number of executions that HotSpot occurs 1500.
		for (int i=0; i < 1500; i++) {
			body.motorPortL.controlMotor(0, 0);
			body.motorPortR.controlMotor(0, 0);
			body.motorPortT.controlMotor(0, 0);
			body.getBrightness();
			body.getSonarDistance();
			body.getGyroValue();
			Battery.getVoltageMilliVolt();
			Balancer.control(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 8000);
		}
		Delay.msDelay(1000);       // In another thread wait until the wonder time that HotSpot is complete.

		body.motorPortL.controlMotor(0, 0);
		body.motorPortR.controlMotor(0, 0);
		body.motorPortT.controlMotor(0, 0);
		body.motorPortL.resetTachoCount();   // left motor encoder reset
		body.motorPortR.resetTachoCount();   // right motor encoder reset
		body.motorPortT.resetTachoCount();   // tail motor encoder reset
		Balancer.init();            // inverted pendulum control initialization


		// Remote connection
		Timer rcTimer = new Timer();
		TimerTask rcTask = new TimerTask() {  //  remote command task
				@Override
				public void run() {
				}
			};
		rcTimer.schedule(rcTask, 0, 20);

		view = new LogSender();
		while(true){
			Delay.msDelay(500);
			LCD.clear();
			LCD.drawString("Connect Ready", 0, 4);
			isConnected = view.connect();

			if(isConnected)break;
		}


		//view.define(log, "touch,B","distance,F","color,F","gyro,I","tachoL,F","tachoR,F","tachoT,F","Time,I");

		//view.hotspot(1000);

		// Waiting for the start of
		LCD.drawString("Touch to START", 0, 4);
		boolean touchPressed = false;
		for (;;) {
			tailControl(body, TAIL_ANGLE_STAND_UP); //complete stop for angle control
			if (body.touchSensorIsPressed()) {
				touchPressed = true;          // touch sensor is pressed
			} else {
				if (touchPressed) break;      // touch sensor I was released after being pressed
			}
			Delay.msDelay(20);
		}
		body.motorPortL.resetTachoCount();   // left motor encoder reset
		body.motorPortR.resetTachoCount();   // right motor encoder reset
		Balancer.init();            // inverted pendulum control initialization

		rcTimer.cancel();
		rcTask.cancel();

		LCD.drawString("Running       ", 0, 4);

		Timer driveTimer = new Timer();
		TimerTask driveTask = new TimerTask() {

				@Override
				public void run() {
					//tailControl(body, TAIL_ANGLE_DRIVE); // balance for running angle to control

					touch = body.touchSensorIsPressed();
					distance = 0;// body.getSonarDistance();
					color = 0;//body.getBrightness();
					gyro = body.getGyroValue();              // gyro sensor value
					tachoL = body.motorPortL.getTachoCount();     //  left motor rotation angle
					tachoR = body.motorPortR.getTachoCount();     // right motor rotation angle
					tachoT = body.motorPortT.getTachoCount();     //  tail motor rotation angle

					/*log.add("touch", touch);
					log.add("distance", distance);
					log.add("color", color);
					log.add("gyro", gyro);
					log.add("tachoL", tachoL);
					log.add("tachoR", tachoR);
					log.add("tachoT", tachoT);
					log.add("Time", lastTakeTime/1000000);
*/

					float forward =  0.0F; // forward-reverse instruction
					float turn    =  0.0F; // turning instruction


					if(touch)stopcounter=300;
					if(distance < 0.50f)uscounter = 400;

					if(stopcounter >= 0){//リセット後安定させるために少し停止(forword,turnの初期値を使う)
						stopcounter--;
					}
					else if(uscounter <= 0){
						if(isRight^isFront){//超音波センサーが感知していない時は回る
							turn = 40.0F;
						}
						else{
							turn = -40.0F;
						}
						if(++turncounter - seekrange * 50 > 250){
							if(isFront){
								isRight = !isRight;++seekrange;
							}
							isFront = !isFront;
							turncounter = 0;
						}

					}
					else{//超音波センサーが感知してる時とその直後は前に動く



						if(distance < 0.50F){
							lastdistance = distance;
						}

						forward = KP*(lastdistance - 0.20F);
						if(forward < -40.0F){

							forward = -40.0F;
						}
						else if(forward > 40.0F){
							forward =40.0F;
						}
						  if(uscounter <= 200){//前に動いてから回るとき、その前に減速してその後の動作を安定させる
							   forward *= 1 - uscounter/200;
						}

						isFront = false;
						seekrange = 0;
						--uscounter;
					}


					int battery = Battery.getVoltageMilliVolt();      //  battery voltage [mV]
					if(stopcounter>200){// タッチセンサーが押されている時とその直後は停止してしっぽを立てる
						seekrange = 0;
						body.motorPortL.resetTachoCount();   // left motor encoder reset
						body.motorPortR.resetTachoCount();   // right motor encoder reset
						Balancer.init();            // inverted pendulum control initialization
						Balancer.control (0, 0, gyro, GYRO_OFFSET, tachoL, tachoR, battery); // inverted pendulum control
						body.motorPortL.controlMotor(0, 1); // left motor PWM output set
						body.motorPortR.controlMotor(0, 1); // right motor PWM output set
						tailControl(body, TAIL_ANGLE_STAND_UP);
					}else{
						Balancer.control (forward, turn, gyro, GYRO_OFFSET, tachoL, tachoR, battery); // inverted pendulum control
						body.motorPortL.controlMotor(Balancer.getPwmL(), 1); // left motor PWM output set
						body.motorPortR.controlMotor(Balancer.getPwmR(), 1); // right motor PWM output set
						tailControl(body, TAIL_ANGLE_DRIVE);
					}
					//logSend();
				}
			};
		driveTimer.scheduleAtFixedRate(driveTask, 0, 4);


		for (;;) {
			if(nowTime < System.nanoTime() - LOG_INTERVAL){
				logSend();
			}
		}
	}

	private static void logSend(){
		nowTime = System.nanoTime();
		if(isConnected)view.send();
		lastTakeTime = System.nanoTime() - nowTime;
	}



	/*
	 *  Traveling body full stop motor of angle control
	 * @param angle motor target angle [degree]
	 */
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
}
