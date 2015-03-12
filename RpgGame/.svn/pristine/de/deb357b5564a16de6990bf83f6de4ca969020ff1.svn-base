package com.dh.handler;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import com.dh.exception.GameException;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.ExceptionProcessor;
import com.dh.util.Tool;

public abstract class AbstractCommandHandler implements ICommandHandler {
	private static Logger logger = Logger.getLogger(AbstractCommandHandler.class);
	private final static TIntObjectMap<Method> map = new TIntObjectHashMap<Method>();
	@Resource
	private ExceptionProcessor exceptionProcessor;

	public AbstractCommandHandler() {
		initCommandCode();
	}

	protected void addCommand(int commandCode, String methodName) {
		Method method = BeanUtils.findMethodWithMinimalParameters(this.getClass(), methodName);
		if (method != null) {
			map.put(commandCode, method);
		} else {
			String errMsg = Tool.concatString("can't find method ", this.getClass().getSimpleName(), ",", methodName);
			System.err.println(errMsg);
			logger.error(errMsg);
		}
	}

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		Method method = map.get(nettyMessageVO.getCommandCode());
		if (method == null) {
			otherCommandCode(nettyMessageVO, commandList);
		} else {
			try {
				method.invoke(this, new Object[] { nettyMessageVO, commandList });
			} catch (GameException ge) {
				logger.error(Tool.concatString(this.getClass().getSimpleName(), ",", method.getName(), "error GameException "));
				throw ge;
			} catch (InvocationTargetException ie) {
				logger.error(Tool.concatString(this.getClass().getSimpleName(), ",", method.getName(), "error InvocationTargetException ",ie.getTargetException().getMessage()));
				throw (Exception) ie.getTargetException();
			} catch (Exception e) {
				logger.error(Tool.concatString(this.getClass().getSimpleName(), ",", method.getName(), "error ", e.getCause()));
			}
		}
	}

	protected void otherCommandCode(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) {
		exceptionProcessor.errCommandPro(nettyMessageVO);
	}

	protected abstract void initCommandCode();

}
