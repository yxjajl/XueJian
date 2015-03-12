package com.dh.resconfig;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.NameVO;
import com.dh.util.RandomUtil;

public class NameRes extends BaseRes<NameVO> {
	private static final Logger LOGGER = Logger.getLogger(LevelRes.class);
	public static final String Path = filePath + "csv/names.csv";
	private List<String> surNameList = new ArrayList<String>();
	private List<String> firstList = new ArrayList<String>();
	private List<String> secondList = new ArrayList<String>();

	private NameRes() {
		classz = NameVO.class;
	}

	private static NameRes INSTANCE = new NameRes();

	public static NameRes getInstance() {
		return INSTANCE;
	}

	@Override
	protected void clear() {
		// TODO Auto-generated method stub
		super.clear();
		surNameList.clear();
		firstList.clear();
		secondList.clear();
	}

	public void otherInit() {
		LOGGER.info("NameRes.otherInit");
		for (NameVO nameVO : this.getDataList()) {
			if (nameVO.getSurname() != null && nameVO.getSurname().trim().length() > 0) {
				surNameList.add(nameVO.getSurname());
			}

			if (nameVO.getFirstName() != null && nameVO.getFirstName().trim().length() > 0) {
				firstList.add(nameVO.getFirstName());
			}

			if (nameVO.getSecondName() != null && nameVO.getSecondName().trim().length() > 0) {
				secondList.add(nameVO.getSecondName());
			}
		}
	}

	public String createRandomNick() {
		int sex = RandomUtil.randomInt(2);
		String nick = null;
		int r = RandomUtil.randomInt(surNameList.size());
		nick = surNameList.get(r);
		if (sex == 1) {
			r = RandomUtil.randomInt(firstList.size());
			nick = nick + firstList.get(r);
		} else {
			r = RandomUtil.randomInt(secondList.size());
			nick = nick + secondList.get(r);
		}

		return nick;
	}

	public String createNick(int sex) {
		String nick = null;
		int r = RandomUtil.randomInt(surNameList.size());
		nick = surNameList.get(r);
		if (sex == 1) {
			r = RandomUtil.randomInt(firstList.size());
			nick = nick + firstList.get(r);
		} else {
			r = RandomUtil.randomInt(secondList.size());
			nick = nick + secondList.get(r);
		}

		return nick;
	}

	// public static String createName() {
	// String str = null;
	// int hightPos, lowPos; // 定义高低位
	// Random random = new Random();
	// hightPos = (176 + Math.abs(random.nextInt(39)));// 获取高位值
	// lowPos = (161 + Math.abs(random.nextInt(93)));// 获取低位值
	// byte[] b = new byte[2];
	// b[0] = (new Integer(hightPos).byteValue());
	// b[1] = (new Integer(lowPos).byteValue());
	// try {
	// str = new String(b, "GBk");
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }// 转成中文
	// return str;
	// }

	public static void main(String[] args) {
		NameRes.getInstance().loadFile(NameRes.Path);
		for (int i = 0; i < 5000; i++) {
			System.out.println("NO.: " + i + NameRes.getInstance().createNick(2));

		}
		// for (NameRes name : list) {
		// String[] firStrings = name.firstName.split(name.firstName);
		// Random random = new Random();
		// int fstName = random.nextInt(firStrings.length);
		// System.out.println(firStrings[fstName]);
		// }
		// if ((NameRes.getIntance().getKNameRes())!=null) {
		// }
	}

}
