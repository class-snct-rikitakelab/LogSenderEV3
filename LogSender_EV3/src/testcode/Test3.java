package testcode;

import lejos.hardware.Battery;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import ev3Viewer.LogSender;

/**
 * 最低限
 */

public class Test3 {
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
			sender.addLog("rand", (float)Math.random(),time());
			sender.addLog("rand2", (float)Math.random(),time());
			sender.addLog("rand3", (float)Math.random(),time());
			sender.addLog("rand4", (float)Math.random(),time());
			if(i%100 == 0)LCD.drawString("data:"+i, 1, 4);
		}
		LCD.drawString("send start", 1, 2);
		sender.send();
		LCD.drawString("send end  ", 1, 2);
	}
	private static float time(){
		return (float)(System.nanoTime() - startTime) / 1000000000;
	}
}
