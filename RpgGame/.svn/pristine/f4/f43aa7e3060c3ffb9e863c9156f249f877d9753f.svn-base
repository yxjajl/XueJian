package com.dh.vo.user;

public class UserLegion implements IClear {
	// private List<LegionVO> joinList = new ArrayList<LegionVO>();
	private int quitDate;

	/**
	 * 加入倒计时
	 * 
	 * @return
	 */
	public int getJoinCountDown() {
		if (quitDate > 0) {
			int div = quitDate + 12 * 60 * 60 - (int) (System.currentTimeMillis() / 1000);
			if (div < 0) {
				quitDate = 0;
				return 0;
			} else {
				return div;
			}
		}
		return 0;
	}

	// private LegionVO legionVO = null;
	// private LegionMemVO legionMemVO = null;

	// public MEM_TYPE isReq(int legionId) {
	// for (LegionVO legionVO : joinList) {
	// if (legionVO.getId() == legionId) {
	// return MEM_TYPE.MEM_TYPE_REQ;
	// }
	// }
	// return MEM_TYPE.MEM_TYPE_NONE;
	// }

	@Override
	public void clear() {

	}

	// public LegionMemVO getLegionMemVO() {
	// return legionMemVO;
	// }
	//
	// public void setLegionMemVO(LegionMemVO legionMemVO) {
	// this.legionMemVO = legionMemVO;
	// }

	// public List<LegionVO> getJoinList() {
	// return joinList;
	// }
	//
	// public void setJoinList(List<LegionVO> joinList) {
	// this.joinList = joinList;
	// }

	public int getQuitDate() {
		return quitDate;
	}

	public void setQuitDate(int quitDate) {
		this.quitDate = quitDate;
	}

}
