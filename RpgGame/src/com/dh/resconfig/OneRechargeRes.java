package com.dh.resconfig;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseOneRechargeVO;
import com.dh.game.vo.base.Reward;

public class OneRechargeRes extends BaseRes<BaseOneRechargeVO> {
	private static final Logger LOGGER = Logger.getLogger(OneRechargeRes.class);
	public static final String Path = filePath + "csv/cfg_singlepenreward.csv";
	private final TreeMap<Integer, List<Reward>> rewardmap = new TreeMap<Integer, List<Reward>>();
	private int[] idarr = null;

	private OneRechargeRes() {
		classz = BaseOneRechargeVO.class;
	}

	private static OneRechargeRes INSTANCE = new OneRechargeRes();

	public static OneRechargeRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("OneRechargeRes.otherInit");
		for (BaseOneRechargeVO baseOneRechargeVO : getDataList()) {
			Reward reward = new Reward();
			reward.setType(baseOneRechargeVO.getType());
			reward.setContent(baseOneRechargeVO.getContent());
			reward.setNumber(baseOneRechargeVO.getNumber());

			List<Reward> list = rewardmap.get(baseOneRechargeVO.getRechargenumber());

			if (list == null) {
				list = new ArrayList<Reward>();
				rewardmap.put(baseOneRechargeVO.getRechargenumber(), list);
			}

			list.add(reward);
		}

		super.clear();

		idarr = new int[rewardmap.size()];

		int n = 0;
		for (Integer value : rewardmap.keySet()) {
			idarr[n++] = value;
		}
	}

	public int findRewardId(int value) {
		int n = -1;
		for (int temp : idarr) {
			if (value > temp) {
				n++;
			} else if (value == temp) {
				n++;
				break;
			} else {
				break;
			}
		}
		return n;
	}

	public List<Reward> findRewardById(int id) {
		return rewardmap.get(id);
	}

	public int[] getIdarr() {
		return idarr;
	}

	@Override
	protected void clear() {
		super.clear();
	}

	public static void main(String[] args) throws Exception {
		OneRechargeRes.getInstance().loadFile(OneRechargeRes.Path);
		ItemRes.getInstance().loadFile(ItemRes.Path);
		for (Integer value : OneRechargeRes.getInstance().getIdarr()) {
			for (Reward reward : OneRechargeRes.getInstance().findRewardById(value)) {
				System.out.println(reward.getContent() + "," + ItemRes.getInstance().getBaseItemVO(reward.getContent()).getName());
			}
		}
		System.out.println(OneRechargeRes.getInstance().findRewardId(400));
	}
}
