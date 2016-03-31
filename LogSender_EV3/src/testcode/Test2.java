package testcode;

import java.util.Timer;
import java.util.TimerTask;

import ev3Viewer.LogSender;
import lejos.hardware.Battery;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

/**
 * モーター回さない方
 */
public class Test2 {
	private static LogSender sender;

	static boolean touch;
	static float distance;
	static float color;
	static float gyro; // gyro sensor value
	static int tachoL; // left motor rotation angle
	static int tachoR; // right motor rotation angle
	static int tachoT; // tail motor rotation angle
	private static final int LOG_INTERVAL = (int) (0.5 * 1000000000);

	private static boolean isConnected;

	private static long lastTakeTime;

	private static long startTime;
	private static long lastSendTime;

	/**
	 * Main
	 */
	public static void main(String[] args) {

		LCD.drawString("Please Wait...  ", 0, 4);

		for (int i = 0; i < 1500; i++) {
			Battery.getVoltageMilliVolt();
		}

		// Remote connection
		Timer rcTimer = new Timer();
		TimerTask rcTask = new TimerTask() { // remote command task
			@Override
			public void run() {
			}
		};
		rcTimer.schedule(rcTask, 0, 20);

		//Sound.beep();

		sender = new LogSender();
		while (true) {
			Delay.msDelay(100);
			LCD.clear();
			LCD.drawString("Connect Ready", 0, 4);
			isConnected = sender.connect();

			if (isConnected)
				break;
		}

		// sender.define(log,
		// "touch,B","distance,F","color,F","gyro,I","tachoL,F","tachoR,F","tachoT,F","Time,I");

		// sender.hotspot(1000);

		// Waiting for the start of
		LCD.drawString("LogSender init", 0, 4);
		for (int i = 0; i < 1500; ++i){
			sender.addLog("1",1,1);
			sender.addLog("2",2.0f,2);
			sender.clear();
			sender.send();
		}

		rcTimer.cancel();
		rcTask.cancel();

		LCD.drawString("Running       ", 0, 4);
		startTime = System.nanoTime();
		//sender.setGlaph("battery", "min_y", "6000");

		Timer driveTimer = new Timer();
		TimerTask driveTask = new TimerTask() {

			@Override
			public void run() {
				// tailControl(body, TAIL_ANGLE_DRIVE); // balance for running
				// angle to control

				int battery = Battery.getVoltageMilliVolt(); // battery voltage
																// [mV]
				sender.addLog("battery", battery, nowTime());
				// logSend();
			}
		};
		driveTimer.scheduleAtFixedRate(driveTask, 0, 200);

		for (;;) {
			if (lastSendTime < System.nanoTime() - LOG_INTERVAL) {
				logSend();
			}
		}
	}

	private static void logSend() {
		sender.addLog("sendTime", (float) lastTakeTime / 1000000000, nowTime());
		lastSendTime = System.nanoTime();
		if (isConnected)
			sender.send();
		lastTakeTime = System.nanoTime() - lastSendTime;
	}
	private static float nowTime(){
		return (float)(System.nanoTime() - startTime) / 1000000000;
	}


}
