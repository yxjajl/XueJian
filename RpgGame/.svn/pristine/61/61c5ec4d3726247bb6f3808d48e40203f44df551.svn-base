package com.dh.test;

import java.nio.ByteBuffer;

import com.dh.game.vo.activity.ActivityProto.SignReq;
import com.dh.game.vo.gm.GMProto.ReloadRedisReq;
import com.dh.game.vo.item.ShopProto.ItemSellRequest;
import com.dh.game.vo.item.ShopProto.ItemUseRequest;
import com.dh.game.vo.item.YunZiFuProto.GoldIngotsRequest;
import com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest;
import com.dh.game.vo.login.PlayerLoginProto.GuideUpReq;
import com.dh.game.vo.login.PlayerLoginProto.PlayerLoginRequest;
import com.dh.game.vo.message.MessageProto.RewardedRequest;
import com.dh.game.vo.raid.RaidProto.BATTLE_TYPE;
import com.dh.game.vo.raid.RaidProto.RaidEndRequest;
import com.dh.game.vo.raid.RaidProto.RaidStartRequest;
import com.dh.game.vo.raid.YuanZhenProto.YuanZhenDetailRequest;
import com.dh.game.vo.recharge.RechargeProto.RechargeRequest;
import com.dh.game.vo.street.StreetProto.HuntReq;
import com.dh.game.vo.street.StreetProto.OpenGridReq;
import com.dh.game.vo.street.StreetProto.StreetDefendReq;
import com.dh.game.vo.task.PlayerTask.ReceiveTaskRequest;
import com.dh.util.Tool;

public class TestData {
	/**
	 * 登陆请求
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public static byte[] LoginData(String username, String password) {
		PlayerLoginRequest.Builder builder = PlayerLoginRequest.newBuilder();
		builder.setAccount(username);
		builder.setPassword(password);
		return builder.build().toByteArray();
	}

	/**
	 * 注册
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public static byte[] RegisterRole(String username, String password) {
		PlayerCreateRequest.Builder builder = PlayerCreateRequest.newBuilder();
		builder.setAccount(username);
		builder.setPassword(password);
		builder.setHeadIcon(1);
		return builder.build().toByteArray();
	}

	public static byte[] ReceiveTask(int taskId) {
		ReceiveTaskRequest.Builder builder = ReceiveTaskRequest.newBuilder();
		builder.setTaskId(taskId);
		return builder.build().toByteArray();
	}

	// 副本开始 10101
	public static byte[] RaidStart(int raidid) {
		RaidStartRequest.Builder builder = RaidStartRequest.newBuilder();
		builder.setRaidid(raidid);
		builder.setType(BATTLE_TYPE.RAID);
		return builder.build().toByteArray();
	}

	// 副本结束
	public static byte[] RaidEnd(int raidid, String validateKey) {
		RaidEndRequest.Builder builder = RaidEndRequest.newBuilder();
		builder.setRaidid(raidid);
		String value = Tool.MD5(raidid + "&" + validateKey);
		builder.setValidateCode(value);//
		builder.setIsSucc(1);
		builder.setScore(1);
		builder.setType(BATTLE_TYPE.RAID);
		return builder.build().toByteArray();
	}

	//
	public static byte[] getUrlParameter() {
		GoldIngotsRequest.Builder goldIngotsRequest = GoldIngotsRequest.newBuilder();
		goldIngotsRequest.setSerialId(109);
		goldIngotsRequest.setCount(1);
		return goldIngotsRequest.build().toByteArray();
	}

	public static byte[] Recharge(int playerId, int number) {
		RechargeRequest.Builder builder = RechargeRequest.newBuilder();
		builder.setPlayerId(playerId);
		builder.setNumber(number);
		return builder.build().toByteArray();
	}

	public static byte[] YuanZhenData(int layout) {
		YuanZhenDetailRequest.Builder builder = YuanZhenDetailRequest.newBuilder();
		builder.setLayer(layout);
		return builder.build().toByteArray();
	}

	public static byte[] ItemUse(int ItemId, int count) {
		ItemUseRequest.Builder builder = ItemUseRequest.newBuilder();
		builder.setItemId(ItemId);
		builder.setCount(count);
		return builder.build().toByteArray();
	}

	public static byte[] ffff3() {
		ReloadRedisReq.Builder builder = ReloadRedisReq.newBuilder();
		builder.setPlayerId(1626003);
		builder.setUsername("8F47907790A3B356AB4D329290A34309");
		return builder.build().toByteArray();
	}

	public static byte[] sellItem(int itemId, int count) {
		ItemSellRequest.Builder builder = ItemSellRequest.newBuilder();
		builder.setItemId(itemId);
		builder.setCount(count);
		return builder.build().toByteArray();
	}

	public static byte[] opendData(int i) {
		OpenGridReq.Builder req = OpenGridReq.newBuilder().setId(i);

		return req.build().toByteArray();
	}

	public static byte[] getMailReward() {
		int[] arr = new int[] { 17, 18, 19, 20, 21, 22 };
		RewardedRequest.Builder builder = RewardedRequest.newBuilder();

		for (int value : arr) {
			builder.addId(value);
		}

		return builder.build().toByteArray();
	}

	public static byte[] rechargeTest(int playerId, int goldIngot) {
		ByteBuffer bf = ByteBuffer.allocate(1024);
		bf.putInt(goldIngot);
		bf.putInt(playerId);
		bf.put("aDmin".getBytes());

		bf.flip();

		return bf.array();

	}

	public static byte[] huntData(int i) {
		// TODO Auto-generated method stub
		return HuntReq.newBuilder().setResId(i).build().toByteArray();
	}

	public static byte[] configDef(int i) {
		return StreetDefendReq.newBuilder().setResId(i).addHeroId(1).addHeroId(2).addHeroId(3).build().toByteArray();
	}

	public static byte[] reloadRedis() {
		return ReloadRedisReq.newBuilder().setUsername("vt22").build().toByteArray();
	}

	public static byte[] guide() {
		return GuideUpReq.newBuilder().setIndex(119).build().toByteArray();
	}

	public static byte[] sign() {
		return SignReq.newBuilder().setIsResign(true).build().toByteArray();
	}

}
