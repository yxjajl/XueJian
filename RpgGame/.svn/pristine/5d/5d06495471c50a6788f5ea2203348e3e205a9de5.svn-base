package com.dh.constants;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MailConstants {

	public final static int MAIL_LIST_LIFE_TIME = (int) TimeUnit.HOURS.toMillis(2);// 邮件列表生存时间,过期则重新更新数据库,与birthTime对应
	public final static long M_PT_READED_LIFETIME = TimeUnit.DAYS.toMillis(5);// 纯文本(PLAIN_TEXT)已读存活期
	public final static long M_PT_UNREAD_LIFETIME = TimeUnit.DAYS.toMillis(10);// 纯文本(PLAIN_TEXT)未读存活期
	public final static long M_MAX_LIFETIME = TimeUnit.DAYS.toMillis(30);// 所有邮件最大存活期
	public final static int PAGE_MAILS = 6;// 每一页的邮件数目

	public final static Pattern REWARD_SPLIT_CHAR = Pattern.compile(";");
	public final static Pattern REWARD_SPLIT_TYPE_CHAR = Pattern.compile(",");// 奖励具体类型分隔符

	public final static int MAIL_READ = 1;
	public final static int MAIL_UNREAD = 0;
	public final static int MAIL_REWARD = 1;
	public final static int MAIL_UNREWARD = 0;

	public final static String MAIL_SENDER_SYS = "系统邮件";
	public final static String MAIL_SENDER_KNAP_FULL = "背包已满";
	public final static String MAIL_SENDER_GM = "GM";
	public final static String MAIL_SENDER_WORLD_BOSS = "世界boss";
	public final static String MAIL_SENDER_ACTY_ANS = "翰林院";
	public final static long MAIL_LOAD_PERRIOD = TimeUnit.MINUTES.toMillis(5);
	// public final static String BOSS1_REWARD_ALL =
	// MonsterRes.getInstance().getBaseMonsterVO();

}
