package com.dh.vo.user;

import java.util.List;

import com.dh.game.vo.user.PlayerShopVO;
import com.dh.util.CodeTool;

public class UserShop implements IClear {
	private List<PlayerShopVO> shopList;

	public List<PlayerShopVO> getShopList() {
		return shopList;
	}

	public void setShopList(List<PlayerShopVO> shopList) {
		this.shopList = shopList;
	}

	/**
	 * 取某样物品已购买的数量
	 * 
	 * @param serialId
	 * @return
	 */
	public int getShopSingle(int serialId, String strDate) {
		int result = 0;
		if (CodeTool.isNotEmpty(shopList)) {
			for (PlayerShopVO playerShopVO : shopList) {
				if (playerShopVO.getItemId() == serialId && strDate.equals(playerShopVO.getLastUpdateDate())) {
					result = playerShopVO.getNum();
					break;
				}
			}
		}

		return result;
	}

	/**
	 * 寻找物品
	 * 
	 * @param serialId
	 * @param strDate
	 * @return
	 */
	public PlayerShopVO findPlayerShopVO(int serialId, String strDate) {
		if (CodeTool.isNotEmpty(shopList)) {
			for (PlayerShopVO playerShopVO : shopList) {
				if (playerShopVO.getItemId() == serialId && strDate.equals(playerShopVO.getLastUpdateDate())) {
					return playerShopVO;
				}
			}
		}

		return null;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		if (shopList != null) {
			shopList.clear();
		}

		shopList = null;
	}

}
