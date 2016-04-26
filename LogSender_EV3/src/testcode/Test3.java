package testcode;

import lejos.hardware.Battery;
import lejos.utility.Delay;
import ev3Viewer.LogSender;

/**
 * 最低限
 */

public class Test3 {
	static long startTime;
	public static void main(String[] args) {
		LogSender sender = new LogSender();
		sender.connect();
		startTime = System.nanoTime();
		for(int i = 0; i < 1000; ++i){
			sender.addLog("battery", Battery.getVoltageMilliVolt(), time());
			sender.addLog("rand", (float)Math.random(),time());
			sender.send();
		}
	}
	private static float time(){
		return (float)(System.nanoTime() - startTime) / 1000000000;
	}
}
