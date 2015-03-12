package com.dh.resconfig;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseAnsVO;

/**
 * 答题
 */
public class AnsRes extends BaseRes<BaseAnsVO> {
	private static final Logger LOGGER = Logger.getLogger(AnsRes.class);
	public static final String Path = filePath + "csv/cfg_race.csv";
	private final static TIntObjectMap<BaseAnsVO> ANS_MAP = new TIntObjectHashMap<BaseAnsVO>(500);
	public static final int QUETIONS_SIZE = 25;// 题目数

	private AnsRes() {
		classz = BaseAnsVO.class;
	}

	private static AnsRes INSTANCE = new AnsRes();

	public static AnsRes getInstance() {
		return INSTANCE;
	}

	public BaseAnsVO getBaseAnsById(int id) {
		return ANS_MAP.get(id);

	}

	public List<BaseAnsVO> createQuestions() {
		List<BaseAnsVO> list = getDataList();
		Collections.shuffle(list);
		int start = new Random().nextInt(list.size() - QUETIONS_SIZE);
		List<BaseAnsVO> ansList = new ArrayList<BaseAnsVO>(list.subList(start, start + QUETIONS_SIZE));
		// for (BaseAnsVO baseAnsVO : ansList) {
		// System.out.println("id:" + baseAnsVO.getId() + "question" +
		// baseAnsVO.getQuestion() + "\n\t " + baseAnsVO.getAns1() + " " +
		// baseAnsVO.getAns2() + " " + baseAnsVO.getAns3() + " "
		// + baseAnsVO.getAns4() + "right:" + baseAnsVO.getRight());
		// }
		return ansList;
	}

	public void otherInit() {
		LOGGER.info(this.getClass().getName() + ".otherInit");
		ANS_MAP.clear();
		for (BaseAnsVO baseAnsVO : this.getDataList()) {
			// baseAnsVO.setRight(baseAnsVO.getRight() - 1);
			ANS_MAP.put(baseAnsVO.getId(), baseAnsVO);
		}

	}

	public static void main(String[] args) throws Exception {
		AnsRes.getInstance().loadFile(AnsRes.Path);
		for (int i = 0; i < 3; i++) {
			List<BaseAnsVO> list = AnsRes.getInstance().createQuestions();
			for (BaseAnsVO baseAnsVO : list) {
				System.out.println("id:" + baseAnsVO.getId());
				// System.out.println("id:" + baseAnsVO.getId() + "question" +
				// baseAnsVO.getQuestion() + "\n\t\t " + baseAnsVO.getAns1() +
				// " " + baseAnsVO.getAns2() + " " + baseAnsVO.getAns3() + " "
				// + baseAnsVO.getAns4() + "right:" + baseAnsVO.getRight());
			}
			System.out.println("============================================end-" + i + "====================================");
		}

	}
}
