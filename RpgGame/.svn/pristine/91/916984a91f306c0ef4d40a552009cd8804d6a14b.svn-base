package com.dh.resconfig;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseComposeVO;

public class ComposeRes extends BaseRes<BaseComposeVO> {
	private static final Logger LOGGER = Logger.getLogger(ComposeRes.class);
	public static final String Path = filePath + "csv/cfg_compose.csv";

	private ComposeRes() {
		classz = BaseComposeVO.class;
	}

	private static ComposeRes INSTANCE = new ComposeRes();

	public static ComposeRes getInstance() {
		return INSTANCE;
	}

	public BaseComposeVO getBaseComposeVO(int cfgId) {
		for (BaseComposeVO baseComposeVO : this.getDataList()) {
			if (baseComposeVO.getItemId() == cfgId) {
				return baseComposeVO;
			}
		}
		return null;
	}

	public void otherInit() {
		LOGGER.info("ComposeRes.otherInit");
		for (BaseComposeVO baseComposeVO : this.getDataList()) {
			int[] material = new int[3];
			int[] number = new int[3];

			material[0] = baseComposeVO.getMaterial1();
			number[0] = baseComposeVO.getMaterial1Number();
			material[1] = baseComposeVO.getMaterial2();
			number[1] = baseComposeVO.getMaterial2Number();
			material[2] = baseComposeVO.getMaterial3();
			number[2] = baseComposeVO.getMaterial3Number();
			baseComposeVO.setMaterial(material);
			baseComposeVO.setNumber(number);
		}
		
	}

	public static void main(String[] args) throws Exception {
		ComposeRes.getInstance().loadFile(ComposeRes.Path);
		BaseComposeVO baseComposeVO = ComposeRes.getInstance().getBaseComposeVO(10035);
		int[] material = baseComposeVO.getMaterial();
		int[] number = baseComposeVO.getNumber();
		System.out.println(material[0] + "," + number[0]);
		System.out.println(material[1] + "," + number[1]);
		System.out.println(material[2] + "," + number[2]);

	}
}
