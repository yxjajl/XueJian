package com.dh.resconfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseOpenActVO;
import com.dh.game.vo.base.Reward;
import com.dh.util.CodeTool;

public class OpenActRes extends BaseRes<BaseOpenActVO> {
	private static final Logger LOGGER = Logger.getLogger(OpenActRes.class);
	public static final String Path = filePath + "csv/cfg_openserviceactivities.csv";

	public static final Map<String, List<Reward>> openActRewardList = new HashMap<String, List<Reward>>();
	public static final TreeSet<Integer> combatSet = new TreeSet<Integer>();

	private OpenActRes() {
		classz = BaseOpenActVO.class;
	}

	private static OpenActRes INSTANCE = new OpenActRes();

	public static OpenActRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("OpenActRes.otherInit");
		for (BaseOpenActVO baseOpenActVO : getDataList()) {
			Reward reward = new Reward();
			reward.setType(baseOpenActVO.getRewardtype());
			reward.setContent(baseOpenActVO.getContent());
			reward.setNumber(baseOpenActVO.getNumber());
			CodeTool.putListValue(openActRewardList, getKey(baseOpenActVO.getNum(), baseOpenActVO.getStage(), baseOpenActVO.getLowerlimit()), reward);

			if (baseOpenActVO.getNum() == 4) {
				combatSet.add(baseOpenActVO.getLowerlimit());
			}
		}

	}

	public List<Reward> findReward(int actid, int step, int rank) {

		return openActRewardList.get(getKey(actid, step, rank));
	}

	public int getCombatIndex(int combat) {
		int n = -1;
		for (int value : combatSet) {
			if (combat < value) {
				break;
			}
			n++;
		}
		return n;
	}

	private String getKey(int actid, int step, int rank) {
		StringBuffer key = new StringBuffer();
		key.append(actid).append(",");
		key.append(step).append(",");
		key.append(rank);
		return key.toString();
	}

	@Override
	protected void clear() {
		super.clear();
		openActRewardList.clear();
	}

	public static void main(String[] args) throws Exception {
		OpenActRes.getInstance().loadFile(OpenActRes.Path);
		ItemRes.getInstance().loadFile(ItemRes.Path);
		List<Reward> list = OpenActRes.getInstance().findReward(3, 0, 1);
		for (Reward reward : list) {
			System.out.println(reward.getContent() + "," + ItemRes.getInstance().getBaseItemVO(reward.getContent()).getName());
		}
		System.out.println(OpenActRes.getInstance().getCombatIndex(100000));
	}
}