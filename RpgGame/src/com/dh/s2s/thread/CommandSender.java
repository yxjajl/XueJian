package com.dh.s2s.thread;

import org.springframework.stereotype.Component;

import com.dh.s2s.queue.CommandQueue;
import com.dh.s2s.queue.CommandRunnable;

@Component
public class CommandSender {
	private CommandRunnable commandRunnable = new CommandRunnable(CommandQueue.getInstance(), "CommandSender");

	public void start() {
		new Thread(commandRunnable).start();
		// new Thread(chatRunnable).start();
	}
}
