package testcode;

import lejos.hardware.Battery;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import ev3Viewer.LogSender;

/**
 * 送受信用
 */

public class Test5 {
	static long startTime;
	public static void main(String[] args) {
		LogSender sender = new LogSender();
		LCD.clear();
		LCD.drawString("ready", 1, 2);
		sender.connect();
		LCD.drawString("connected", 1, 2);
		startTime = System.nanoTime();
		for(int i = 0; i <= 10000; ++i){
			sender.addLog("battery", Battery.getVoltageMilliVolt(), time());
			sender.addLog("random", (float)Math.random(),time());
			sender.send();
			LCD.drawString(sender.recieve(), 1, 4);
			Delay.msDelay(1000);
		}
	}
	private static float time(){
		return (float)(System.nanoTime() - startTime) / 1000000000;
	}
}
