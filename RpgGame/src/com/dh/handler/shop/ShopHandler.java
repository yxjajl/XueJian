package com.dh.handler.shop;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.exception.GameException;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.item.ShopProto.ItemBuyReq;
import com.dh.game.vo.item.ShopProto.ItemBuyResp;
import com.dh.game.vo.item.ShopProto.ItemSellRequest;
import com.dh.game.vo.item.ShopProto.ItemSellResponse;
import com.dh.game.vo.item.ShopProto.ItemUseRequest;
import com.dh.game.vo.item.ShopProto.ItemUseResponse;
import com.dh.game.vo.item.ShopProto.RESULT;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.ExceptionProcessor;
import com.dh.processor.ShopProcessor;
import com.google.protobuf.InvalidProtocolBufferException;

@Component
public class ShopHandler implements ICommandHandler {
	private static Logger logger = Logger.getLogger(ShopHandler.class);
	@Resource
	private ShopProcessor shopProcessor;
	@Resource
	private ExceptionProcessor exceptionProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.SHOP_LIST: // 折扣商品列表
			shopList(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ITEM_SELL:
			handleItemSell(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ITEM_USE:
			handleItemUse(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ITEM_SPLIT:
			handleItemSplit(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.SHOP_ITME_BUY: // 购买物品
			handlerItemBuy(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.SHOP_EXPLOIT_LIST: // 功勋商店
			// exploitList(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.SHOP_BUY_POWER:
			buyPower(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.SHOP_EXPLOIT_RESET:
			shopProcessor.refreshExploit(nettyMessageVO, commandList);
		default:
			exceptionProcessor.errCommandPro(nettyMessageVO);
			break;
		}

	}

	private void buyPower(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		shopProcessor.buyPower(nettyMessageVO, commandList);
	}

	// private void exploitList(NettyMessageVO nettyMessageVO,
	// List<NettyMessageVO> commandList) throws Exception {
	// shopProcessor.exploitList(nettyMessageVO, commandList);
	// }

	/**
	 * 折扣商品列表
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 */
	private void shopList(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		shopProcessor.shopList(nettyMessageVO, commandList);
	}

	/**
	 * type 0 普通商店 1折扣商城 3 功勋商城 4华山论剑商城(炼心石)
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	private void handlerItemBuy(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		ItemBuyReq req = null;
		try {
			req = ItemBuyReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			logger.error("协议解码异常", e);
			throw e;
		}
		boolean isSucc = false;
		ItemBuyResp.Builder resp = ItemBuyResp.newBuilder().setSerialId(req.getSerialId());
		resp.setType(req.getType());
		try {
			resp.setCount(req.getCount());
			if (req.getType() == 3) { // 功勋
				isSucc = shopProcessor.shoppingExploit(req.getSerialId(), Math.abs(req.getCount()), req.getType(), nettyMessageVO, commandList);
				// shopProcessor.exploitList(nettyMessageVO, commandList);
			} else if (req.getType() == 4) { // 远征商店
				isSucc = shopProcessor.shoppingYuanZhen(req.getSerialId(), Math.abs(req.getCount()), req.getType(), nettyMessageVO, commandList);
			} else if (req.getType() == 5) { // 积分商店
				isSucc = shopProcessor.scoreShop(req.getSerialId(), 1, nettyMessageVO, commandList);
			} else {
				isSucc = shopProcessor.itemBuy(req.getSerialId(), Math.abs(req.getCount()), req.getType(), nettyMessageVO, commandList);
			}
			resp.setResult(RESULT.OK);
		} catch (Exception e) {
			logger.error("" + e.getMessage(), e);
			if (!isSucc) {
				resp.setResult(RESULT.FAILURE);
			}
			throw e;
		} finally {

			if (req.getType() == 1) { // 折扣购买
				shopProcessor.shopList(nettyMessageVO, commandList);
			}

			nettyMessageVO.setData(resp.build().toByteArray());
			commandList.add(nettyMessageVO);
		}
	}

	/**
	 * 拆分
	 * 
	 * @date 2014年3月14日
	 */
	private void handleItemSplit(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) {
		// ItemSplitReq req = null;
		// try {
		// req = ItemSplitReq.parseFrom(nettyMessageVO.getData());
		// } catch (InvalidProtocolBufferException e) {
		// logger.error("协议解码异常");
		// }
		// shopProcessor.itemSplit(req.getItemId(), nettyMessageVO,
		// commandList);

	}

	/**
	 * 使用
	 * 
	 * @throws Exception
	 * 
	 * @date 2014年3月14日
	 */
	private void handleItemUse(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		ItemUseRequest request = null;
		try {
			request = ItemUseRequest.parseFrom(nettyMessageVO.getData());
			if (request.getCount() <= 0) {
				return;
			}
		} catch (InvalidProtocolBufferException e) {
			logger.error("协议解码异常");
			throw e;
		}
		boolean isSucc = false;
		ItemUseResponse.Builder resp = ItemUseResponse.newBuilder().setItemId(request.getItemId());
		resp.setCount(request.getCount());
		resp.setResult(RESULT.FAILURE);
		try {
			isSucc = shopProcessor.itemUse(request.getItemId(), Math.abs(request.getCount()), nettyMessageVO, commandList);
			resp.setResult(RESULT.OK);
		} catch (GameException ge) {
			resp.setResult(RESULT.FAILURE);
			throw ge;
		} catch (Exception e) {
			logger.error(e);
			if (!isSucc) {
				resp.setResult(RESULT.FAILURE);
			}
			throw e;
		} finally {
			nettyMessageVO.setData(resp.build().toByteArray());
			commandList.add(nettyMessageVO);
		}
	}

	/**
	 * 出售
	 * 
	 * @throws Exception
	 * 
	 * @date 2014年3月14日
	 */
	private void handleItemSell(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		ItemSellRequest req = null;
		try {
			req = ItemSellRequest.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			logger.error("协议解码异常");
			throw e;
		}
		boolean isSucc = false;
		ItemSellResponse.Builder resp = ItemSellResponse.newBuilder().setItemId(req.getItemId());
		resp.setCount(req.getCount());
		try {
			int num = req.getCount();
			isSucc = shopProcessor.itemSell(req.getItemId(), num, nettyMessageVO, commandList);

			resp.setResult(RESULT.OK);
		} catch (Exception e) {
			if (!isSucc) {
				resp.setResult(RESULT.FAILURE);
				resp.setCount(req.getCount());
			}
			throw e;
		} finally {
			nettyMessageVO.setData(resp.build().toByteArray());
			commandList.add(nettyMessageVO);
		}

	}
	// public void shopping(NettyMessageVO nettyMessageVO, List<NettyMessageVO>
	// commandList) throws Exception {
	// ShoppingRequest request = null;
	// try {
	// request = ShoppingRequest.parseFrom(nettyMessageVO.getData());
	// System.out.println(request.getItemCfgId());
	// System.out.println(request.getNumber());
	// } catch (Exception e) {
	// logger.error(ProperytiesUtil.getAlertMsg("数据解析异常"));
	// }
	// int playerid = ServerHandler.get(nettyMessageVO.getChannel());
	// UserCached userCached = ServerHandler.getUserCached(playerid);
	//
	// shopProcessor.shopping(userCached, request.getItemCfgId(),
	// request.getNumber(), commandList);
	// }

}
