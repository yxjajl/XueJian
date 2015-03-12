package com.dh.resconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.TotalStarVO;

//TotalStarVO
public class TotalStarRes extends BaseRes<TotalStarVO> {
	private static final Logger LOGGER = Logger.getLogger(TotalStarRes.class);
	public static final String Path = filePath + "csv/cfg_totalstar.csv";

	private Map<Integer, List<TotalStarVO>> map = new HashMap<Integer, List<TotalStarVO>>();

	private TotalStarRes() {
		classz = TotalStarVO.class;
	}

	private static TotalStarRes INSTANCE = new TotalStarRes();

	public static TotalStarRes getInstance() {
		return INSTANCE;
	}
	
	@Override
	protected void clear() {
		super.clear();
		map.clear();
	}


	public void otherInit() {
		LOGGER.info("TotalStarRes.otherInit");
		List<TotalStarVO> list = null;
		for (TotalStarVO totalStarVO : getDataList()) {
			list = map.get(totalStarVO.getId());
			if (list == null) {
				list = new ArrayList<TotalStarVO>();
				map.put(totalStarVO.getId(), list);
			}
			list.add(totalStarVO);
		}
	}

	/**
	 * 查等级数据
	 * 
	 * @param level
	 * @return
	 */
	public List<TotalStarVO> getTotalStarVO(int id) {
		return map.get(id);
	}

	public static void main(String[] args) throws Exception {
		TotalStarRes.getInstance().loadFile(TotalStarRes.Path);
		RewardRes.getInstance().loadFile(RewardRes.Path);
		for (TotalStarVO totalStarVO : TotalStarRes.getInstance().getDataList()) {
			System.out.println(totalStarVO.getId() + "," + totalStarVO.getRewardid());
			if (null == RewardRes.getInstance().getRewardRateGroup(totalStarVO.getRewardid())) {
				System.out.println("不存在的奖励组id");
			}
		}

		System.out.println(TotalStarRes.getInstance().getTotalStarVO(1));
	}
}
