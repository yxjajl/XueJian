package com.dh.s2s.queue;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.dh.s2s.socket.S2sClient;
import com.dh.s2s.vo.ServerVO;

@Component
public class CommandProcessThread {

	private CommandRunnable commandRunnable = new CommandRunnable(CommandQueue.getInstance(), "s2s");

	public void start(HashMap<String, ServerVO> map) {
		for (String str : map.keySet()) {
			ServerVO serverVO = map.get(str);
			if (serverVO != null) {
				// commandRunnable.addServers(new S2sClient(serverVO));
				commandRunnable.addServers(new S2sClient());
			}
		}
		new Thread(commandRunnable).start();
	}
}
