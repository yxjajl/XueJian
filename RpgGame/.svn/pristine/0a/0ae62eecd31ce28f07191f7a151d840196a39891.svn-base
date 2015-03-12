package com.dh.resconfig;

import java.util.Random;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseTipsVO;

/** 游戏玩法提示 */
public class TipsRes extends BaseRes<BaseTipsVO> {
	private static final Logger LOGGER = Logger.getLogger(TipsRes.class);
	public static final String Path = filePath + "csv/cfg_prompt.csv";

	private TipsRes() {
		classz = BaseTipsVO.class;
	}

	private static TipsRes INSTANCE = new TipsRes();

	public static TipsRes getInstance() {
		return INSTANCE;
	}

	public BaseTipsVO getRandomTip() {
		return this.getDataList().get(new Random().nextInt(getDataList().size()));
	}

	public void otherInit() {

	}

	public static void main(String[] args) throws Exception {
		TipsRes.getInstance().loadFile(TipsRes.Path);
		for (int i = 0; i < 100; i++) {
			BaseTipsVO baseComposeVO = TipsRes.getInstance().getRandomTip();
			System.out.println(i + " : " + baseComposeVO.getContent());
		}
	}
}
