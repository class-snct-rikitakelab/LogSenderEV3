package testcode;

import java.util.TimerTask;

import ev3Viewer.LogSender;
import lejos.hardware.Battery;

public class DriveTask extends TimerTask{

	private int waitcount = 0;
	private static EV3Body body;
	private LogSender ls;

	private static long starttime;

	DriveTask(LogSender ls, EV3Body body){
		this.body = body;
		this.ls = ls;
		this.waitcount = 0;

		starttime = System.nanoTime();
	}

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		tailControl(0);

		float forward = 40.0F;
		float turn = 0.0F;

		if(body.getBrightness() > 0.2){
			turn = 50.0F;
		}else{
			turn = -50.0F;
		}

		if(++waitcount > 50){
			ls.addLog("Bright", body.getBrightness(), (System.nanoTime()-starttime)/1000000);
			waitcount = 0;
		}

		Balancer.control(forward, turn, body.getGyroValue(),0.0F, body.motorPortL.getTachoCount(), body.motorPortR.getTachoCount(), Battery.getVoltageMilliVolt());

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
