package com.dh.game.constant;

public class TaskConstant {
	// 任务类型
	public final static int TASK_TYPE_MAIN = 1; //主线
	public final static int TASK_TYPE_BRANCH = 2; //支线
	public final static int TASK_TYPE_GRESS = 4; //进度
	public final static int TASK_TYPE_DAILY = 5; //日常

	public final static int TASK_CAN_GET = 0;// 可接
	public final static int TASK_UNDERWAY = 1;// 进行中
	public final static int TASK_COMPLIETE = 2;// 完成未领奖
	public final static int TASK_REWARD = 3; // 已领奖

	public final static int TASK_ROLE_UPLEVEL = 100;

	public final static int TASK_RAID = 200; // 通关难度nun的副本
	public final static int TASK_ELIRAID = 201; // 精英通关难度nun的副本
	public final static int TASK_TAKE_EQUIP = 210;// 角色穿戴上某件装备
	// public final static int TASK_USE_ITEM = 220;// 使用**个某物品
	// public final static int TASK_HAVA_ITEM = 221;// 拥有num个某个物品
	// public final static int TASK_HAVE_EQUIP = 222;// 拥有1件num级num品质的装备
	// public final static int TASK_HAVE_POS_EQUIP = 223;// 拥有1件num级某部位装备
	// public final static int TASK_HAVE_GEM = 224;// 拥有num个num级的宝石
	// public final static int TASK_RAID = 225;// 拥有num件某部位装备
	// public final static int TASK_RAID = 230;// 进行num次强化操作
	public final static int TASK_ENHANLV_EQ = 231;// 拥有一件强化等级为num的英雄装备
	// public final static int TASK_ENHANLV_EQ = 232;// 拥有一件强化等级为num的某等级装备
	// public final static int TASK_RAID = 233;// 拥有num件强化+num装备
	public final static int TASK_EQUIP_UPSTAR = 234;// 装备升星
	// public final static int TASK_RAID = 240;// 任意装备镶嵌了num个某等级宝石
	// public final static int TASK_RAID = 241;// 拥有num件镶嵌num个宝石的装备
	// public final static int TASK_RAID = 250;// 通过合成获得num个某物品
	public final static int TASK_COMPOSE_EQ = 251;// 通过合成获得N件num品质的装备
	// public final static int TASK_RAID = 252;// 通过合成获得num个num级的宝石
	// public final static int TASK_RAID = 260;// 招募到某个英雄
	// public final static int TASK_RAID = 261;// num个英雄战斗力达到num值
	public final static int TASK_ANY_LEVELHERO = 262;// 当前拥有num个num级英雄
	public final static int TASK_TAKE_EQUIP_POSITION = 263;// 某个英雄穿戴上某件装备在指令位置
	public final static int TASK_HERO_UPSTAR = 264;// 某个英雄星级升到num级
	public final static int TASK_HERO_UPLEVEL = 265;// 某英雄等级升到num级
	public final static int TASK_SKILL_UPLEVEL = 266;// 升级主动技能至3级
	public final static int TASK_BUILD_UPLEVEL = 290;// 领地建筑等级达到num级
	public final static int TASK_BUILD_SALARY = 291;// 在主建筑进行num次征收
	// public final static int TASK_RAID = 300;// 在商城购买num个物品
	// public final static int TASK_RAID = 310;// 完成num次日常任务
	// public final static int TASK_RAID = 311;// 进行num次领奖操作
	// public final static int TASK_RAID = 312;// 进行num次签到操作
	// public final static int TASK_RAID = 320;// 在养心殿进行num次休息
	// public final static int TASK_RAID = 330;// 进行num次跑商
	// public final static int TASK_RAID = 340;// 进行num次精英副本挑战
	public final static int TASK_JJC_BATTLE = 350;// 进行num次竞技场
	public final static int TASK_NHERO_NSTAR = 351; // M个N星英雄
	public final static int TASK_NEQ_NSTAR = 352; // 当前拥有num个num星以上装备
	public final static int TASK_NEQ_NHUYOU = 353; // 当前拥有num个护佑等级为X阶X级以上装备
	public final static int TASK_NPSKILL = 354;// 某个英雄拥有num个被动技能
	public final static int TASK_GRESS_REWARD = 355; // 任务进度奖励　任务
}
