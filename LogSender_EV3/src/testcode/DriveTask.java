package linetrace;

import java.util.TimerTask;

import ev3Viewer.LogSender;
import hardware.Hardware;

public class DriveTask extends TimerTask{

	private int waitcount = 0;
	private TurnCalc tc;
	private WheelMotorCtrl wmc;
	private LogSender ls;

	private static long starttime;

	DriveTask(WheelMotorCtrl wmc,TurnCalc tc, LogSender ls){
		this.wmc = wmc;
		this.tc = tc;
		this.ls = ls;

		starttime = System.nanoTime();
	}

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ

		tailControl(0);

		float forward = 40.0F;
		float turn = tc.calcTurn();

		wmc.setForward(forward);
		wmc.setTurn(turn);

		if(++waitcount > 50){
			float[] sample = new float[Hardware.redMode.sampleSize()];
			Hardware.redMode.fetchSample(sample,0);
			ls.addLog("Bright", (int)(sample[0] * 100), (System.nanoTime()-starttime)/1000000);
			waitcount = 0;
		}

		wmc.controlWheel();
	}

	private static final void tailControl(int angle) {
        float pwm = (float)(angle - Hardware.motorPortT.getTachoCount()) * 2.5F; // 比例制御
        // PWM出力飽和処理
        if (pwm > 60) {
            pwm = 60;
        } else if (pwm < -60) {
            pwm = -60;
        }
        Hardware.motorPortT.controlMotor((int)pwm, 1);
    }

}
