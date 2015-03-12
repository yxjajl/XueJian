package com.dh.handler.login;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.login.CheckRoleNameProto.CheckRoleNameRequest;
import com.dh.game.vo.login.GetRoleNameProto.GetRoleNameRequest;
import com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest;
import com.dh.game.vo.login.PlayerLoginProto.GuideUpReq;
import com.dh.game.vo.login.PlayerLoginProto.PlayerLoginRequest;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.ExceptionProcessor;
import com.dh.processor.LoginProcessor;
import com.google.protobuf.InvalidProtocolBufferException;

@Component
public class LoginHandler implements ICommandHandler {
	private static Logger logger = Logger.getLogger(LoginHandler.class);

	@Resource
	private LoginProcessor loginProcessor;
	@Resource
	private ExceptionProcessor exceptionProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.HEARTBEAT:
			heartbeat(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.USER_LOGIN: // 登陆
			processLogin(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.USER_CREATE: // 注册用户
			processRegister(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.GET_ROLENAME: // 随机名字
			getRoleName(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.CHECK_ROLENAME: // 校验名字是否被重用
			checkRoleName(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.USER_LOGOUT:
			processLogout(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.USER_RELOGIN:
			reLogin(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.LOGIN_GUIDE_UP:
			handleGuide(nettyMessageVO, commandList);
			break;
		default:
			logger.error("error Commandcode " + nettyMessageVO.getCommandCode());
			exceptionProcessor.errCommandPro(nettyMessageVO);
			break;
		}
	}

	private void getRoleName(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {

		GetRoleNameRequest req = null;
		try {
			req = GetRoleNameRequest.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			logger.error("数据解析异常", e);
			return;
		}
		loginProcessor.getRoleName(req, nettyMessageVO, commandList);
	}

	private void handleGuide(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		GuideUpReq req = null;
		try {
			req = GuideUpReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			logger.error("数据解析异常", e);
			return;
		}
		loginProcessor.updateGuide(req, nettyMessageVO, commandList);
	}

	/**
	 * 注册
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void processRegister(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		PlayerCreateRequest request = null;
		// String account = null;
		// String password = null;
		// int headIcon = 0;
		// String nick = null;
		try {
			request = PlayerCreateRequest.parseFrom(nettyMessageVO.getData());
			// account = request.getAccount();
			// password = request.getPassword();
			// headIcon = request.getHeadIcon();
			// nick = request.getNick();

		} catch (Exception e) {
			logger.error("数据解析异常", e);
			return;
		}
		loginProcessor.processRegister(request, nettyMessageVO, commandList);

	}

	public void heartbeat(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		commandList.add(nettyMessageVO);
		// CommandQueue.getInstance().put(nettyMessageVO);
	}

	/**
	 * 登陆
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void processLogin(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		PlayerLoginRequest request = null;
		try {
			request = PlayerLoginRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error("数据解析异常", e);
			throw e;
		}
		loginProcessor.processorLogin(request, nettyMessageVO, commandList);
		// CommandQueue.getInstance().put(nettyMessageVO);
	}

	public void reLogin(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		PlayerLoginRequest request = null;
		try {
			request = PlayerLoginRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error("数据解析异常", e);
			throw e;
		}
		loginProcessor.reLogin(request, nettyMessageVO, commandList);
		// CommandQueue.getInstance().put(nettyMessageVO);
	}

	/**
	 * 校验尼称
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void checkRoleName(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		CheckRoleNameRequest request;
		try {
			request = CheckRoleNameRequest.parseFrom(nettyMessageVO.getData());
			loginProcessor.checkRoleName(request, commandList);
		} catch (Exception e) {
			logger.error("数据解析异常", e);
		}

	}

	/**
	 * 离线
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void processLogout(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		loginProcessor.processLogout(nettyMessageVO, commandList);
	}

}
