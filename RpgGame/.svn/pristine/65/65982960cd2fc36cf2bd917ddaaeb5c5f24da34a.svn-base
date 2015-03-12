package com.dh.resconfig;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseSalaryVO;
import com.dh.game.vo.base.Reward;
import com.dh.service.RewardService;

public class SalaryRes extends BaseRes<BaseSalaryVO> {// BaseSalaryVO
	private static final Logger LOGGER = Logger.getLogger(SalaryRes.class);
	public static final String Path = filePath + "csv/cfg_wage.csv";
//	public static final long[] SALARCD = {0, 300000L, 600000L, 900000L, 3600000L, 7200000L };
	public static final long[] SALARCD = {0, 30000L, 60000L, 90000L, 120000L, 240000L };

	private SalaryRes() {
		classz = BaseSalaryVO.class;
	}

	private static SalaryRes INSTANCE = new SalaryRes();

	public static SalaryRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("SalaryRes.otherInit");

		for (BaseSalaryVO baseSalaryVO : SalaryRes.getInstance().getDataList()) {

			int[] salary = new int[5];
			salary[0] = baseSalaryVO.getSalary1();
			salary[1] = baseSalaryVO.getSalary2();
			salary[2] = baseSalaryVO.getSalary3();
			salary[3] = baseSalaryVO.getSalary4();
			salary[4] = baseSalaryVO.getSalary5();

			baseSalaryVO.setSalary(salary);

			Reward reward = new Reward();
			reward.setType(RewardService.REWARD_TYPE_MONEY);
			reward.setContent(0);
			reward.setNumber(baseSalaryVO.getSalaryVip());

			baseSalaryVO.getVipRewardList().add(reward);

			if (baseSalaryVO.getItem1() > 0) {
				reward = new Reward();
				reward.setType(RewardService.REWARD_TYPE_GOODS);
				reward.setContent(baseSalaryVO.getItem1());
				reward.setNumber(baseSalaryVO.getNum1());

				baseSalaryVO.getVipRewardList().add(reward);
			}

			if (baseSalaryVO.getItem2() > 0) {
				reward = new Reward();
				reward.setType(RewardService.REWARD_TYPE_GOODS);
				reward.setContent(baseSalaryVO.getItem2());
				reward.setNumber(baseSalaryVO.getNum2());

				baseSalaryVO.getVipRewardList().add(reward);
			}

			if (baseSalaryVO.getItem3() > 0) {
				reward = new Reward();
				reward.setType(RewardService.REWARD_TYPE_GOODS);
				reward.setContent(baseSalaryVO.getItem3());
				reward.setNumber(baseSalaryVO.getNum3());

				baseSalaryVO.getVipRewardList().add(reward);
			}

			if (baseSalaryVO.getItem4() > 0) {
				reward = new Reward();
				reward.setType(RewardService.REWARD_TYPE_GOODS);
				reward.setContent(baseSalaryVO.getItem4());
				reward.setNumber(baseSalaryVO.getNum4());

				baseSalaryVO.getVipRewardList().add(reward);
			}

			if (baseSalaryVO.getItem5() > 0) {
				reward = new Reward();
				reward.setType(RewardService.REWARD_TYPE_GOODS);
				reward.setContent(baseSalaryVO.getItem5());
				reward.setNumber(baseSalaryVO.getNum5());

				baseSalaryVO.getVipRewardList().add(reward);
			}

		}
	}

	/**
	 * 根据议事堂等级查数据
	 * 
	 * @param level
	 * @return
	 */
	public BaseSalaryVO getBaseSalaryVO(int level) {
		for (BaseSalaryVO baseSalaryVO : SalaryRes.getInstance().getDataList()) {
			if (baseSalaryVO.getLevel() == level) {
				return baseSalaryVO;
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		SalaryRes.getInstance().loadFile(SalaryRes.Path);
		System.out.println(SalaryRes.getInstance().getBaseSalaryVO(1).getVipRewardList());
		// for (BaseSalaryVO baseSalaryVO : SalaryRes.getInstance().getDataList()) {
		// System.out.println(baseSalaryVO.getLevel() + "," + baseSalaryVO.getSalary()[1]);
		// }
	}
}