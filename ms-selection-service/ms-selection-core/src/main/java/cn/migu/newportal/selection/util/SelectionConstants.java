package cn.migu.newportal.selection.util;


public interface SelectionConstants
{
    /**
     * pg 的域名前缀 返回地址类似于 http://10.211.95.78:8081/pg/
     */
    String DOMAIN_PREFIX = "pg.server.url.prefix";
    
    /**
     * 图书详情预置页前缀bookdetail.html
     */
    String BOOK_PREFIX = "newportal.view.book.detail.url";
    
    /**
     * 正式
     */
    String ENV_P = "p/";
    
    /**
     * 测试
     */
    String ENV_T = "t/";
    
    /**
     * 测试
     */
    String DEFAULT_SITEID = "1";
    
    String ADAPT_SCREEN_WIDTH = "240";
    
    /**书库轮循和精品轮循标签不轮循排序方式 1-图书更新时间  */
    public static final String BOOK_UPDATE = "1";
    
    /**书库轮循和精品轮循标签不轮循排序方式  2-图书上节点时间 */
    public static final String BOOK_ON_NODE = "2";
    
    /**书库轮循和精品轮循标签不轮循排序方式 3-图书入库时间 */
    public static final String BOOK_IN_STACK = "3";
    
    public static final String BOOK_IN_PROVINCE = "4";
    
    public static final String BOOK_IN_CITY = "5";
    
    /**不轮循的时候，按照排序值排序*/
    public static final String BOOK_IN_CHOOSE = "6";
    
    /** 轮循栏目标签显示类型 1：图书名称 */
    public static final String SHOW_TYPE_BOOK_NAME = "1";
    
    /** 轮循栏目标签显示类型 2：图书长推荐语 */
    public static final String SHOW_TYPE_LONG_RECOMMEND_WORDS = "2";
    
    /** 轮循栏目标签显示类型 3：图书封面 */
    public static final String SHOW_TYPE_BOOK_IMAGE = "3";
    
    /** 轮循栏目标签显示类型 4：图书短推荐语 */
    public static final String SHOW_TYPE_SHORT_RECOMMEND_WORDS = "4";
    
    public static final String SHOW_TYPE_WAP2_RECOMMEND_WORDS = "5";
    
    /** 轮循栏目标签显示类型 6：屏宽适配推荐语 */
    public static final String SHOW_TYPE_ADAPT_RECOMMEND_WORDS = "6";
    
    /** 是否轮询  0：不轮询*/
    public static final String IS_NOT_CYCLE = "0";
    
    /** 是否轮询  1：轮询*/
    public static final String IS_CYCLE = "1";
    
    /** 是否展示下划线  1：展示*/
    public static final String  IS_SHOWLINE = "1";
    
    /** 是否展示下划线  0：不展示*/
    public static final String  IS_NOT_SHOWLINE = "0";
    
    /** 展示种类 1:书名*/
    public static final String SHOW_TYPE_BOOKNAME = "1";
    
    /** 展示种类 2:书名+作者名*/
    public static final String SHOW_TYPE_AUTHORNAME = "2";
    
    /** 展示种类 3:书名+点击量*/
    public static final String SHOW_TYPE_CLICK = "3";
    
    /** 展示种类 4:书名+章节数*/
    public static final String SHOW_TYPE_CHAPTERSIZE = "4";
    
    /** 展示种类 5:书名+文案(链接描述)*/
    public static final String SHOW_TYPE_LINKDESC = "5";
    
    /**
     * 腰封展示类型 1 ：不展示
     */
    public static final String IS_SHOWYF_NULL = "1";
    
    /**
     * 腰封展示类型 2 ：蓝色
     */
    public static final String IS_SHOWYF_BLUE = "2";
    
    /**
     * 腰封展示类型 3 ：红色
     */
    public static final String IS_SHOWYF_RED = "3";
    
    /**
     * 腰封展示类型 4 ：绿色
     */
    public static final String IS_SHOWYF_GREEN = "4";
    
    /**
     * 投票活动投票数放大倍数1：1倍
     */
    public static final String VOTE_LARGIZE_ONE = "1";
    
    /**
     * 投票活动投票数放大倍数2：2倍
     */
    public static final String VOTE_LARGIZE_TWO = "2";
    
    /**
     * 投票活动投票数放大倍数3：3倍
     */
    public static final String VOTE_LARGIZE_THREE = "3";
    
    /**
     * 投票活动投票数放大倍数4：4倍
     */
    public static final String VOTE_LARGIZE_FOUR = "4";
    
    /**
     * 投票活动投票数放大倍数5：5倍
     */
    public static final String VOTE_LARGIZE_FIVE = "5";
    
    /**
     * 上边框或者上边界的样式类型展示  1：展示
     */
    public static final String IS_MARGINORPADDINGTYPE = "1"; 
    /**
     * 上边框或者上边界的样式类型展示  0：不展示
     */
    public static final String IS_NOT_MARGINORPADDINGTYPE = "0"; 
    
    /**
     * 主播详情预置页前缀speakerdetail.html
     */
    String SPEAKER_PREFIX = "newportal.view.speaker.detail.url";
    
    /** 论坛吧id */
    String forumId = "forumId";
    
    /** 作者id */
    String authorId = "aid";
    
    /** 作者url */
    String authorUrl = "/l/author.jsp";
    
    /** 专区ID key */
    String nodeId = "nid";
    
    /** 图书id key */
    String bookId = "bid";
    
    /**
     * 主播id key
     */
    String speakerId = "aid";
    
    /** 摇一摇2 **/
    public static final String SHAKE_LOTTERY = "2";
    
    /** 抽奖 */
    public static final String DRAW_LOTTERY = "1";
    
    /** 是否支持关联推荐 1：是 */
    public static final String IS_RECOMMOND = "1";
    
    /** 是否支持关联推荐 2：否 */
    public static final String NOT_RECOMMOND = "2";
    
    /** 价格描述样式 -已订购 */
    public static final String PRICEDESCSTYLE_ORDERED = "1";
    
    /** 价格描述样式 -永久免费 */
    public static final String PRICEDESCSTYLE_FREE = "2";
    
    /** 价格描述样式 -会员专享优惠 */
    public static final String PRICEDESCSTYLE_MEMBER = "3";
    
    /** 价格描述样式 -其他普通优惠 */
    public static final String PRICEDESCSTYLE_NORMAL = "4";
    
    /** 一级图书 */
    public static final String BOOK_LEVEL_1 = "1";
    
    /** 活动类型-特价 */
    public static final String ACTIVITYTYPE_SPECIALOFFER = "2";
    
    /** 活动类型-特价 */
    public static final String ACTIVITYTYPE_LIMITFREE = "5";
    
    /** 分册状态-未分册 */
    public static final String FASCICULESTATUS_NONE = "0";
    
    /** 分册状态-有分册 */
    public static final String FASCICULESTATUS_TRUE = "1";
    
    /** 分册状态-停用 */
    public static final String FASCICULESTATUS_STOP = "2";
    
    /**图书实体价格-0*/
    public static final String ACTUALPRICE_0 = "0";
    /**图书实体价格- -1*/
    public static final String ACTUALPRICE_NONE= "-1";
    
    /**登陆账号名类型-identityId*/
    public static final String ACCOUNTTYPE_IDENTITYID = "0";
    
    /**登陆账号名类型-自定义用户名*/
    public static final String ACCOUNTTYPE_USERDEFINED = "1";
    
    /**登陆账号名类型-Email*/
    public static final String ACCOUNTTYPE_EMAIL = "2";
    
    /**登陆账号名类型-手机号码*/
    public static final String ACCOUNTTYPE_MOBILE = "3";
    
    /**cdkPackage类型-京东E卡*/
    public static final String CDKPKGTYPE_JDECARD = "0";
    
    /**cdkPackage类型-优惠券*/
    public static final String CDKPKGTYPE_COUPON = "1";
    
    /**cdkPackage类型-新年卡*/
    public static final String CDKPKGTYPE_NEWYEARCARD = "3"; 
}
