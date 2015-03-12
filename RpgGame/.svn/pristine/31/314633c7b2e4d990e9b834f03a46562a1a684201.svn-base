package com.dh.resconfig;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseEnhanceVO;

/**
 * 装备强化
 * 
 * @author RickYu
 */
public class EnhanceRes extends BaseRes<BaseEnhanceVO> {
	private static final Logger LOGGER = Logger.getLogger(EnhanceRes.class);
	public static final String Path = filePath + "csv/cfg_EquipmentGrowth.csv";

	private EnhanceRes() {
		classz = BaseEnhanceVO.class;
	}

	private static EnhanceRes INSTANCE = new EnhanceRes();

	public static EnhanceRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("EnhanceRes.otherInit");
	}
	
	public BaseEnhanceVO getBaseEnhanceVO(int cfgId) {
		for(BaseEnhanceVO baseEnhanceVO:this.getDataList()) {
			if(baseEnhanceVO.getCfgId() == cfgId) {
				return baseEnhanceVO;
			}
		}
		return null;
	}


	@Override
	protected void clear() {
	}

	public static void main(String[] args) {
		EnhanceRes.getInstance().loadFile(EnhanceRes.Path);
		System.out.println(EnhanceRes.getInstance().getBaseEnhanceVO(10001).getAtk());
	}
}
