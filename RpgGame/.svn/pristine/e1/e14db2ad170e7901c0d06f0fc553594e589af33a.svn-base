package com.dh.main.test;

import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.dh.dao.CDKeyMapper;
import com.dh.dao.PlayerArenaMapper;
import com.dh.dao.PlayerHeroDefMapper;
import com.dh.dao.PlayerMapper;
import com.dh.game.vo.base.BaseCdKeyVO;
import com.dh.game.vo.user.PlayerArenaVO;
import com.dh.game.vo.user.PlayerHeroDefVO;
import com.dh.game.vo.user.PlayerVO;
import com.dh.util.RandomUtil;

@Component
public class MyLoad {
	@Resource
	private PlayerArenaMapper playerArenaMapper;
	@Resource
	private PlayerHeroDefMapper playerHeroDefMapper;
	@Resource
	private PlayerMapper playerMapper;
	@Resource
	private CDKeyMapper cDKeyMapper;

	private static String[] nameList = { "百里芷", "司徒嫣", "花萱冷", "花薰然", "沫汐泠", "秦落衣", "沈千寻", "溪澈影", "梦依然", "流烟", "芊苡尘", "舞倾城", "苏叶熙", "叶筱凌", "苏珞漓", "洛溪", "苏泠澈", "苏泠冰", "幕秋", "沫楹", "茗清然", "舞汐羽", "椛纶玥",
			"安流烟", "冉雪笑", "夜箜铭", "夜莞辰", "苍瞳凯", "黔漓泫", "洛染澈", "尹瑾墨", "夜瑾一", "夜阑荇", "简钰", "凌亦封", "君清夜", "左寻萧", "江听雨", "上官玄曦", "安枫墨", "玄邪雨", "谭雨坤", "敖亓颙", "百里晟轩", "封玄奕", "简玉珩", "季舒玄", "容云鹤", "柳奚笙",
			"司雪衣", "温子然", "莫习凛", "阮灏君", "傅凌天", "柯孜墨", "楚夫晏", "白黎轩", "苏兼默", "独孤求败", "东方不败", "轩辕无敌", "令狐长胜", "灭绝苍生", "灭世天魔", "创世天尊", "魔神之陨", "唯我独尊", "疾风独狼", "乱世之巅", "原始狂魔", "创世狂神", "寂寥魅影", "烈火柔魂",
			"水蓝剑月", "恋梦奇侠", "帅的乱七八糟", "哼杀你没商量", "PK不乱发型", "死神通缉令", "残影·魅", "祁·影寒", "凉夜枫晚", "战·赤魂", "凉城执念", "遥寄一个拥抱", "凉城冷巷", "花絮纷飞", "蝶成双", "安凉", "耀眼耀心", "情感洁癖", "赠我空欢囍", "你若不来", "奈何桥", "等旧人",
			"萌面超人", "跪地唱征服", "似水往昔", "浮流年", "终不悔", "奥特曼的蛋", "无爱一身轻", "不复当初", "心悦君兮", "決芣冋頭", "那一抹触动", "咖啡不解酒醉", "陪你到婚纱", "一往情深", "时光清浅", "蒍祢变乖", "枫の叶", "对你微笑", "曦兮", "有痣青年", "墨城", "聽兲甴掵", "时光不及",
			"浅眸", "予你痴情", "再见如陌", "离荒", "嘟嘟嘴卖卖萌", "冰糖葫芦娃", "狼心狗肺", "醉荭顔", "承蒙时光不弃", "安朵", "奉旨泡妞", "离莪远点", "24K纯屌丝", "糖果果", "梦在深巷", "堇年", "街角哼情歌", "时光斑驳", "浅巷墨漓", "哆啦A梦", "人善被狗欺", "时光深存", "笑看狗装逼",
			"此人多半有病", "站在坟前勾鬼", "起床困难户", "农村混血妞", "懒得取名", "此號作廢", "冷巷雨未停", "从深爱到敷衍", "久囍不腻", "冒牌的淑女", "浅伤", "暴走的草泥马", "安城如沫", "无视一切", "度他余生", "奶丝凸咪优", "风掠幽蓝", "一招断命根", "悲伤逆流成河", "心慌", "柠檬没我萌",
			"东京巷尾青苔", "床上不败", "愿久伴", "樱花落人离去", "孤木不成林", "北岛余音", "我能说脏话么", "宅到发霉长蘑菇", "丑到灵魂深处", "琉璃梦忆", "空城旧梦凉", "时光慢些吧", "智取其乳", "微笑向暖", "浅瞳", "安然入梦", "见到本宫下跪", "后来无人像你", "墨染半世苍凉", "古佛孤灯", "精神病院的你",
			"含笑九拳", "帅当饭吃", "逗逼轰炸机", "心盲眼瞎", "毒瘾", "嗳情佷远", "小澎湃", "加勒比海带", "低头打飞机", "冷眼看狗装", "情到深处", "乳来伸掌", "长发及腰", "怪我眼瞎", "穷得只剩作业", "容嬷嬷也是花", "蒙牛没我牛", "只可遇见", "始终在路上", "独一妩二", "青丝蘸白雪", "陌上烟雨",
			"一缕阳光", "古城深巷旧年", "呛了眼凉了心", "毁心葬情", "伤了誰在乎", "青葵", "卖萌的西瓜", "青柠", "一腔孤勇", "孤痞。", "换一颗红豆", "青衫不改", "人来人往", "初晨", "若情能自控", "自愈心暖", "情歌越老", "一场空欢喜", "思念不说话", "爱久见人心", "西凉", "凉兮", "耗尽余生",
			"不欢而散", "深知你是梦", "凉了心湿了眼", "婉若清风", "薄荷少女", "暖夕夏", "倾城月光", "淡如水﹏", "回忆越美", "瘋言瘋语", "倾国倾城", "逾期不候", "心若动", "半岛微凉", "再也不贱", "狗不理先森", "浅笑嫣然", "懒喵喵", "且行且珍惜", "海哭碎心", "残颜", "一夕一夏", "繁华落尽",
			"相思碎", "浅末年代", "正在加载", "那年初夏", "墨染素年", "欧耶", "怪叔叔", "孤岛亡鱼", "初凉", "彼岸花幵", "拒绝勾引", "早熟", "乱了心", "槿色素颜", "空城殇", "心里迷了路", "梦在深巷", "老衲法号乱来", "木槿暖夏", "墨染青衣颜", "夜的旋律", "寂若流年", "静听细水长流",
			"忆挽青笙尽", "堇色安年", "暮色上浓妆", "月夜浅吟", "人比烟花寂寞", "弦谁听", "墨城烟柳", "琴断弦奈何", "花落相思起" };

	public void jingjichangData() throws Exception {
		LoadHeroRes.getInstance().loadFile(LoadHeroRes.Path);
		List<PlayerArenaVO> list = playerArenaMapper.getAllPlayerArena();

		boolean changeName = true;
		boolean isture = true;
		int namex = 0;
		for (PlayerArenaVO playerArenaVO : list) {
			System.out.println("====process=========" + playerArenaVO.getPlayerId());
			List<LoadHeroVO> dd = null;
			int combat = 0;
			playerHeroDefMapper.delHeroDef(playerArenaVO.getPlayerId());
			PlayerVO playerVO = playerMapper.getPlayerVOById(playerArenaVO.getPlayerId());
			while (isture) {
				combat = 0;
				dd = LoadHeroRes.getInstance().random();

				for (LoadHeroVO loadHeroVO : dd) {
					combat += loadHeroVO.getCombat();
				}

				if (playerArenaVO.getOrdernum() <= 10 && combat <= 450000) {
					combat = 0;
					playerVO.setLevel(RandomUtil.roundRandom(50, 56));
					playerVO.setVip(RandomUtil.roundRandom(4, 5));
				} else if (playerArenaVO.getOrdernum() > 10 && playerArenaVO.getOrdernum() <= 50 && combat <= 300000) {
					combat = 0;
					playerVO.setLevel(RandomUtil.roundRandom(45, 49));
					playerVO.setVip(RandomUtil.roundRandom(1, 3));
				} else if (playerArenaVO.getOrdernum() > 50 && combat > 300000) {
					combat = 0;
				} else {
					playerVO.setLevel(RandomUtil.roundRandom(10, 30));
					playerVO.setVip(RandomUtil.roundRandom(0, 1));
					isture = false;
				}
			}

			int n = 1;
			combat = 0;
			for (LoadHeroVO loadHeroVO : dd) {
				combat += loadHeroVO.getCombat();
				playerHeroDefMapper.insertHeroDef(LoadHeroVOToPlayerHeroDefVO(loadHeroVO, n, playerArenaVO.getPlayerId()));
				n++;
			}
			playerArenaVO.setCombat(combat);
			isture = true;
			combat = 0;
			playerVO.setLegionId(0);
			if (namex < nameList.length - 1) {
				playerVO.setName(nameList[namex]);

			}
			try {
				playerMapper.updatePlayer(playerVO);
			} catch (Exception e) {
				System.out.println(nameList[namex]);
			}

			namex++;
		}

		// Collections.sort(list, new ArenaComparator());
		// int n = 1;
		for (PlayerArenaVO playerArenaVO : list) {
			// playerArenaVO.setOrdernum(n++);
			playerArenaMapper.updatePlayerArena(playerArenaVO);
		}

	}

	public static String createCdKey(String head) {
		String ddemo = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		int length = ddemo.length();
		StringBuilder sb = new StringBuilder();
		sb.append(head);
		for (int i = 0; i < 3; i++) {
			sb.append(ddemo.charAt(RandomUtil.randomInt(length)));
		}
		sb.append("-");
		for (int i = 0; i < 4; i++) {
			sb.append(ddemo.charAt(RandomUtil.randomInt(length)));
		}
		sb.append("-");
		for (int i = 0; i < 6; i++) {
			sb.append(ddemo.charAt(RandomUtil.randomInt(length)));
		}
		sb.append("-");
		for (int i = 0; i < 4; i++) {
			sb.append(ddemo.charAt(RandomUtil.randomInt(length)));
		}
		sb.append("-");
		for (int i = 0; i < 6; i++) {
			sb.append(ddemo.charAt(RandomUtil.randomInt(length)));
		}

		return sb.toString();
		// W79YYX-E7N4-Y9N24D-WEZR-FVNGTJ
	}

	public void createCDKEY(String head, int size, int type) {
		for (int i = 0; i < size; i++) {
			BaseCdKeyVO baseCdKeyVO = new BaseCdKeyVO();
			baseCdKeyVO.setKeyId(createCdKey(head));
			baseCdKeyVO.setState(0);
			baseCdKeyVO.setType(type);
			try {
				cDKeyMapper.insertCdKey(baseCdKeyVO);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	// public void changeName() {
	// NameRes.getInstance().loadFile(NameRes.Path);
	// List<PlayerVO> list = playerMapper.getAllPlayerVO();
	// for (PlayerVO playerVO : list) {
	// playerVO.setName(NameRes.getInstance().createNick());
	// playerMapper.updatePlayerNick(playerVO);
	// }
	// }

	public static PlayerHeroDefVO LoadHeroVOToPlayerHeroDefVO(LoadHeroVO loadHeroVO, int id, int playerId) {
		PlayerHeroDefVO playerHeroDefVO = new PlayerHeroDefVO();

		playerHeroDefVO.setId(id);
		playerHeroDefVO.setPlayerId(playerId);
		playerHeroDefVO.setName(loadHeroVO.getName());
		playerHeroDefVO.setCfgId(loadHeroVO.getCfgid());
		playerHeroDefVO.setLevel(loadHeroVO.getLevel());
		playerHeroDefVO.setStar(loadHeroVO.getStar());
		playerHeroDefVO.setHp(loadHeroVO.getHp());
		playerHeroDefVO.setDef(loadHeroVO.getDef());
		playerHeroDefVO.setMdef(loadHeroVO.getMdef());
		playerHeroDefVO.setAtk(loadHeroVO.getAtk());
		playerHeroDefVO.setMatk(loadHeroVO.getMatk());
		playerHeroDefVO.setHit(loadHeroVO.getHit());
		playerHeroDefVO.setDodge(loadHeroVO.getDodge());
		playerHeroDefVO.setCir_rate(loadHeroVO.getCir_rate());
		playerHeroDefVO.setCombat(loadHeroVO.getCombat());
		playerHeroDefVO.setPassivesSkill("");
		playerHeroDefVO.setSkillLevel(loadHeroVO.getSkillLevel());

		playerHeroDefVO.setDefendStatus(0);

		playerHeroDefVO.setYzhp(loadHeroVO.getHp());
		playerHeroDefVO.setYzanger(0);

		return playerHeroDefVO;
	}

	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = null;
		PropertyConfigurator.configure("config/log4j.properties");
		ctx = new FileSystemXmlApplicationContext("config/applicationContext.xml");
		MyLoad myLoad = (MyLoad) ctx.getBean("myLoad");
		// myLoad.createCDKEY("A70", 5000, 0); // 07073
		// myLoad.createCDKEY("A71", 5000, 1);
		// myLoad.createCDKEY("A72", 5000, 2);
		// myLoad.createCDKEY("B17", 5000, 3); // 17173
		// myLoad.createCDKEY("C21", 5000, 4); // 265G
		// myLoad.createCDKEY("C22", 5000, 5);
		// myLoad.createCDKEY("C23", 5000, 6);
		// myLoad.createCDKEY("D11", 5000, 7);
		// myLoad.createCDKEY("E11", 5000, 8); //平台新手礼包
		myLoad.createCDKEY("YY0", 200000, 9);

		// myLoad.jingjichangData();
		// myLoad.changeName();j
	}

}

class ArenaComparator implements Comparator<PlayerArenaVO> {

	@Override
	public int compare(PlayerArenaVO o1, PlayerArenaVO o2) {
		// TODO Auto-generated method stub
		if (o1.getCombat() > o2.getCombat()) {
			return -1;
		}

		if (o1.getCombat() == o2.getCombat()) {
			return 0;
		}
		return 1;
	}

}
