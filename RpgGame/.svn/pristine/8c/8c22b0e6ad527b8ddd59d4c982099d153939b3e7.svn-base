package com.dh.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.dh.main.InitLoad;
import com.dh.service.PlayerService;

public class TestConcurrent {
	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = null;
		PropertyConfigurator.configure("config/log4j.properties");
		ctx = new FileSystemXmlApplicationContext("config/applicationContext.xml");
		InitLoad initLoad = (InitLoad) ctx.getBean("initLoad");
		initLoad.init();
		int count = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(count);
		for (int i = 0; i < count; i++)
			executorService.execute(new TestConcurrent().new Task());

		executorService.shutdown();
		while (!executorService.isTerminated()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public class Task implements Runnable {
		@Override
		public void run() {
			try {
				ApplicationContext ctx = null;
				PropertyConfigurator.configure("config/log4j.properties");
				ctx = new FileSystemXmlApplicationContext("config/applicationContext.xml");
				PlayerService playerService = (PlayerService) ctx.getBean("playerService");
				// playerService.registerPlayer("" + System.currentTimeMillis(),
				// "1", "" + System.currentTimeMillis(), 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
