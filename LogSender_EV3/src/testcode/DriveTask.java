package testcode;

import java.util.TimerTask;

import ev3Viewer.LogSender;
import lejos.hardware.Battery;

public class DriveTask extends TimerTask{

	private int waitcount = 0;
	private int spdCtrlcount = 0;

	private static EV3Body body;
	private LogSender ls;
	private SpeedKeeper spKeeper;
	private ForwardCalculator forCalc;

	private float forward = 0.0F;

	private static long starttime;

	DriveTask(LogSender ls, EV3Body body){
		this.body = body;
		this.ls = ls;
		this.waitcount = 0;
		this.spdCtrlcount = 0;

		starttime = System.nanoTime();
		spKeeper = new SpeedKeeper();
		forCalc = new ForwardCalculator(this.body);
		spKeeper.setTarget(0.5F);
	}

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		tailControl(0);

		float turn = 0.0F;

//		if(body.getBrightness() > 0.2){
//			turn = 50.0F;
//		}else{
//			turn = -50.0F;
//		}

		if(++this.spdCtrlcount > 5){
			this.spdCtrlcount = 0;

			float delta = forCalc.caldelForward();
			forward += delta;
		}

		//forward = 20.0F;


		if(++waitcount > 100){
			waitcount = 0;
			float time = (System.nanoTime()-starttime)/1000000;
			ls.addLog("speed", forCalc.curspd,time);
		}

		if(forward > 50.0F){
			forward = 50.0F;
		}else if(forward < -50.0F){
			forward = 50.0F;
		}

		Balancer.control(forward, turn, body.getGyroValue(),0.0F, body.motorPortL.getTachoCount(), body.motorPortR.getTachoCount(), Battery.getVoltageMilliVolt());
		body.motorPortL.controlMotor(Balancer.getPwmL(), 1);
    	body.motorPortR.controlMotor(Balancer.getPwmR(), 1);


    	//Delay.msDelay(4);

	}

	private static final void tailControl(int angle) {
        float pwm = (float)(angle - body.motorPortT.getTachoCount()) * 2.5F; // 比例制御
        // PWM出力飽和処理
        if (pwm > 60) {
            pwm = 60;
        } else if (pwm < -60) {
            pwm = -60;
        }
        body.motorPortT.controlMotor((int)pwm, 1);
    }

}
