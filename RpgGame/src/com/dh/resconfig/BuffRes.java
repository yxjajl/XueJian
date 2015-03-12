package com.dh.resconfig;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseBuffVO;

public class BuffRes {// extends BaseRes<BaseBuffVO> {
// private static final Logger LOGGER = Logger.getLogger(BuffRes.class);
// public static final String Path = filePath + "csv/cfg_buff.csv";
//
// private BuffRes() {
// classz = BaseBuffVO.class;
// }
//
// private static BuffRes INSTANCE = new BuffRes();
//
// public static BuffRes getInstance() {
// return INSTANCE;
// }
//
// public void otherInit() {
// LOGGER.info("BuffRes.otherInit");
// }
//
// /**
// * 查等级数据
// *
// * @param level
// * @return
// */
// public BaseBuffVO getBaseBuffVO(int id) {
// for (BaseBuffVO baseBuffVO : BuffRes.getInstance().getDataList()) {
// if (baseBuffVO.getId() == id) {
// return baseBuffVO;
// }
// }
// return null;
// }
//
// public static void main(String[] args) throws Exception {
// BuffRes.getInstance().loadFile(BuffRes.Path);
// for (BaseBuffVO baseBuffVO : BuffRes.getInstance().getDataList()) {
// System.out.println(baseBuffVO.getId() + "," + baseBuffVO.getName());
// }
// }
}