package com.dh.resconfig;

import java.util.List;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseOnlineReward;
import com.dh.game.vo.base.Reward;

public class OnlineRewardRes extends BaseRes<BaseOnlineReward> {
	private static final Logger LOGGER = Logger.getLogger(FaZhenRes.class);
	public static final String Path = filePath + "csv/cfg_onlinereward.csv";
	private static OnlineRewardRes INSTANCE = new OnlineRewardRes();;

	private OnlineRewardRes() {
		classz = BaseOnlineReward.class;
	}

	public void otherInit() {
		for (BaseOnlineReward baseOnlineReward : this.getDataList()) {
			List<Reward> rewards = RewardRes.getInstance().getRewardRateGroup(baseOnlineReward.getRewardid());
			baseOnlineReward.setRewards(rewards);
			this.getDataMap().put(baseOnlineReward.getId(), baseOnlineReward);
		}

		LOGGER.info("onlineReward.otherInit");
	}

	public static OnlineRewardRes getInstance() {
		return INSTANCE;
	}

	public BaseOnlineReward getOnlineRewardById(int id) {
		return this.getDataMap().get(id);
	}
	
	@Override
	protected void clear() {
		super.clear();
	}

	public static void main(String[] args) {
		RewardRes.getInstance().loadFile(RewardRes.Path);
		OnlineRewardRes.getInstance().loadFile(OnlineRewardRes.Path);
		for (BaseOnlineReward baseOnlineReward : OnlineRewardRes.getInstance().getDataList()) {
			System.out.println("a:"+baseOnlineReward.getId() +"," + baseOnlineReward.getRewardid());
			for(Reward reward:baseOnlineReward.getRewards()) {
				if(reward.getNumber() < 0)
				System.out.println("============"+reward.getNumber() + "," + reward.getContent() + "," + reward.getType());
			}
			System.out.println();
		}
		System.out.println(OnlineRewardRes.getInstance().getOnlineRewardById(0).getTime());
	}

}
