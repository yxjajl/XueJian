package com.dh.handler;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * handler辅助类<br>
 * 注册handler,服务器启动时调用<br>
 * 获取handler
 * 
 */
public class HandlerProcessor {

	// private static final Logger LOGGER = Logger.getLogger(HandlerProcessor.class);

	private static HandlerProcessor instance = new HandlerProcessor();

	private HandlerProcessor() {
	}

	public static HandlerProcessor getInstance() {
		return instance;
	}

	/** 命令码 与 hanlder 映射 */
	private final static TIntObjectMap<ICommandHandler> COMMAND_HANDLERS = new TIntObjectHashMap<ICommandHandler>();

	/**
	 * 添加handler
	 * 
	 * @param commandCode
	 *            命令码
	 * @param handlerImplClass
	 *            handler实现类
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void addCommandHandler(final int commandCodeHead, ICommandHandler iCommandHandler) {
		HandlerProcessor.COMMAND_HANDLERS.put(commandCodeHead, iCommandHandler);
	}

	public ICommandHandler getCommandHandler(final int commandCode) {
		return HandlerProcessor.COMMAND_HANDLERS.get(commandCode/100);
	}

}
