package com.dh.handler;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.handler.activity.ActivityHandler;
import com.dh.handler.area.AreaOperationHandler;
import com.dh.handler.arena.ArenaHandler;
import com.dh.handler.build.BuildHandler;
import com.dh.handler.cdkey.CDKeyHandler;
import com.dh.handler.chat.ChatHandler;
import com.dh.handler.common.CommonHandler;
import com.dh.handler.friend.FriendHandler;
import com.dh.handler.grab.GrabHandler;
import com.dh.handler.hero.HeroHandler;
import com.dh.handler.hero.HeroHungryHandler;
import com.dh.handler.hero.HeroSkillHandler;
import com.dh.handler.item.GemHandler;
import com.dh.handler.item.ItemEquipHandler;
import com.dh.handler.legion.LegionHandler;
import com.dh.handler.login.LoginHandler;
import com.dh.handler.mail.MailHandler;
import com.dh.handler.property.PropertyHandler;
import com.dh.handler.raid.CleanOutHandler;
import com.dh.handler.raid.RaidHandler;
import com.dh.handler.raid.YuanZhenHandler;
import com.dh.handler.recharge.RechargeHandler;
import com.dh.handler.shop.ShopHandler;
import com.dh.handler.street.StreetHandler;
import com.dh.handler.task.TaskHandler;
import com.dh.handler.timer.TimerHandler;
import com.dh.handler.welfare.WelfareHandler;

@Component
public class HandlerRegister {
	public final static int LOGIN_MODEL = 10; // 登陆模块
	public final static int GM_MODEL = 11; // 重新加载redis

	public final static int PROPERTY_UPDATE_MODEL = 12;// 属性更新

	public final static int SYSTIMER = 14; // 系统定时器

	public final static int COMMON_MODEL = 15;// 通用模块.

	public final static int ACTIVIY_MODEL = 16;// 每日签到
	public final static int CDKEY_MODEL = 17;// cdkey

	public final static int MAIL_MODE = 20; // 消息邮件模块
	public final static int MESSAGE_MODEL = 21; // 消息邮件模块
	public final static int FRIEND_MODE = 22;// 好友模块

	public final static int LEGION_MODEL = 30;// 军团模块

	public final static int HERO_MODEL = 35; // hero模块
	public final static int HERO_HUNGRY_MODEL = 36; // hero模块
	public final static int HERO_SKILL_MODEL = 37; // 英雄技能

	public final static int RAID_MODEL = 40; // 副本模块
	public final static int CLEAN_MODEL = 42; // 扫荡

	public final static int AREA_MODEL = 50; // 场景模块
	// 60领地模块
	public final static int BUILD_MODEL = 60; // 科技
	public final static int STREET_MODEL = 61;// 江湖

	public final static int ITEM_MODEL = 70; // 物品装备强化等
	public final static int GEM_MODEL = 71;// 宝石

	public final static int SHOP_MODEL = 80; // 商城模块
	public final static int ARENA_MODEL = 81; // 竞技场
	public final static int DUOBAO_MODEL = 83; // 夺宝
	public final static int YUZHEN_MODEL = 84; // 远征
	public final static int TASK_MODEL = 90; // 任务模块

	public final static int WELFARE_MODEL = 92; // 福利大厅

	public final static int RECHARGE_MODEL = 98; // 充值

	@Resource
	private LoginHandler loginHandler;
	@Resource
	private ShopHandler shopHandler;
	@Resource
	private MailHandler mailHandler;
	@Resource
	private AreaOperationHandler areaOperationHandler;
	@Resource
	private CommonHandler commonHandler;
	@Resource
	private ActivityHandler activityHandler;
	@Resource
	private RaidHandler raidHandler;
	@Resource
	private HeroHandler heroHandler;
	@Resource
	private HeroHungryHandler heroHungryHandler;
	@Resource
	private ItemEquipHandler itemEquipHandler;
	@Resource
	private TaskHandler taskHandler;
	@Resource
	private BuildHandler buildHandler;
	@Resource
	private PropertyHandler propertyHandler;
	@Resource
	private TimerHandler timerHandler;
	@Resource
	private ArenaHandler arenaHandler;
	@Resource
	private GrabHandler grabHandler;
	@Resource
	private GemHandler gemHandler;
	@Resource
	private StreetHandler streetHandler;
	@Resource
	private HeroSkillHandler heroSkillHandler;
	@Resource
	private WelfareHandler welfareHandler;
	@Resource
	private RechargeHandler rechargeHandler;
	@Resource
	private GMHandler gmHandler;
	@Resource
	private YuanZhenHandler yuanZhenHandler;
	@Resource
	private ChatHandler chatHandler;
	@Resource
	private FriendHandler friendHandler;
	@Resource
	private CleanOutHandler cleanOutHandler;
	@Resource
	private LegionHandler legionHandler;
	@Resource
	private CDKeyHandler cDKeyHandler;

	/**
	 * 注册handler
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void registerCommandHandlers() throws Exception {
		HandlerProcessor handlerProcessor = HandlerProcessor.getInstance();

		// 登陆 10
		handlerProcessor.addCommandHandler(LOGIN_MODEL, loginHandler);
		handlerProcessor.addCommandHandler(GM_MODEL, gmHandler);
		// 属性更新
		handlerProcessor.addCommandHandler(PROPERTY_UPDATE_MODEL, propertyHandler);

		// 通用模块15
		handlerProcessor.addCommandHandler(COMMON_MODEL, commonHandler);
		// 活动模块16
		handlerProcessor.addCommandHandler(ACTIVIY_MODEL, activityHandler);

		// 消息
		handlerProcessor.addCommandHandler(MAIL_MODE, mailHandler);
		// 聊天21
		handlerProcessor.addCommandHandler(MESSAGE_MODEL, chatHandler);
		// 好友
		handlerProcessor.addCommandHandler(FRIEND_MODE, friendHandler);
		// 军团
		handlerProcessor.addCommandHandler(LEGION_MODEL, legionHandler);

		// 士兵35
		handlerProcessor.addCommandHandler(HERO_MODEL, heroHandler);
		handlerProcessor.addCommandHandler(HERO_HUNGRY_MODEL, heroHungryHandler);
		handlerProcessor.addCommandHandler(HERO_SKILL_MODEL, heroSkillHandler);

		// 副本40
		handlerProcessor.addCommandHandler(RAID_MODEL, raidHandler);
		// 副本扫荡
		handlerProcessor.addCommandHandler(CLEAN_MODEL, cleanOutHandler);
		// 场景 50
		handlerProcessor.addCommandHandler(AREA_MODEL, areaOperationHandler);
		// 建筑领地60
		handlerProcessor.addCommandHandler(BUILD_MODEL, buildHandler);
		// 江湖
		handlerProcessor.addCommandHandler(STREET_MODEL, streetHandler);

		// 物品和装备70
		handlerProcessor.addCommandHandler(ITEM_MODEL, itemEquipHandler);
		handlerProcessor.addCommandHandler(GEM_MODEL, gemHandler);

		// 商城 80
		handlerProcessor.addCommandHandler(SHOP_MODEL, shopHandler);
		// 任务90
		handlerProcessor.addCommandHandler(TASK_MODEL, taskHandler);

		// 系统定时器处理模块　
		handlerProcessor.addCommandHandler(SYSTIMER, timerHandler);
		// 竞技场
		handlerProcessor.addCommandHandler(ARENA_MODEL, arenaHandler);
		// 夺宝
		handlerProcessor.addCommandHandler(DUOBAO_MODEL, grabHandler);
		// 远征
		handlerProcessor.addCommandHandler(YUZHEN_MODEL, yuanZhenHandler);

		// 福利大厅
		handlerProcessor.addCommandHandler(WELFARE_MODEL, welfareHandler);
		// 充值
		handlerProcessor.addCommandHandler(RECHARGE_MODEL, rechargeHandler);
		handlerProcessor.addCommandHandler(CDKEY_MODEL, cDKeyHandler);

	}

}
