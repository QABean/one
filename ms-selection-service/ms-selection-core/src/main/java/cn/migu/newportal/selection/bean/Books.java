package cn.migu.newportal.selection.bean;

public class Books {
	
	private String bookid; // 图书ID
	
	private String bookShowName; // 图书名
	
	private String bookCoverLogo; // 封面 : 90 * 120 分辨率
	
	private String cover_123_164; // 封面 : 123 * 164 分辨率 
	
	private String cover_180_240; // 封面 : 180 * 240 分辨率
	
	private String cover_270_360; // 封面 : 270 * 360 分辨率
	
	private String cover_360_480; // 封面 : 360 * 480 分辨率
	
	private String longdata; // 长推内容
	
	private String bookShortDesc; // 短推内容
	
	private String shortdesc; // 短简介
	
	private String reason; // 推荐语
	
	private String classnm; // 分类名
	
	private String classurl; // 分类url
	
	private String authorName; // 作者笔名
	
	private String authorid; // 作者ID
	
	private String chapterid; // 章节ID
	
	private String authorurl; // 作者url
	
	private String conturl; // 内容url
	
	private String readpageurl; // 读页url
	
	private int status; // 图书状态
	
	private String clicks; // 点击数
	
	private String comments; // 评论数
	
	private String booklevel; // 图书等级
	
	private String isHot; // 是否热门
	
	private String categoryId; // 分类图书推荐ID

	private String bookCategory; // 分类图书推荐
	
	private String co_percent; // 贡献百分比
	
	private String bookDetailUrl; // 图书详情页地址

	private String contentType; // 图书类型

	private String cornerShowType; //角标展示（0:无;1:免费;2:限免;3:会员;4:完本;5:名家;6:上传）
	
	public String getBookCategory() {
		return bookCategory;
	}

	public void setBookCategory(String bookCategory) {
		this.bookCategory = bookCategory;
	}
	
	public String getBookDetailUrl() {
		return bookDetailUrl;
	}

	public void setBookDetailUrl(String bookDetailUrl) {
		this.bookDetailUrl = bookDetailUrl;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getCornerShowType() {
		return cornerShowType;
	}

	public void setCornerShowType(String cornerShowType) {
		this.cornerShowType = cornerShowType;
	}

	public String getBookid() {
		return bookid;
	}

	public void setBookid(String bookid) {
		this.bookid = bookid;
	}

	public String getBooknm() {
		return bookShowName;
	}

	public void setBooknm(String bookShowName) {
		this.bookShowName = bookShowName;
	}

	public String getCover_90_120() {
		return bookCoverLogo;
	}

	public void setCover_90_120(String cover_90_120) {
		this.bookCoverLogo = cover_90_120;
	}

	public String getCover_123_164() {
		return cover_123_164;
	}

	public void setCover_123_164(String cover_123_164) {
		this.cover_123_164 = cover_123_164;
	}

	public String getCover_180_240() {
		return cover_180_240;
	}

	public void setCover_180_240(String cover_180_240) {
		this.cover_180_240 = cover_180_240;
	}

	public String getCover_270_360() {
		return cover_270_360;
	}

	public void setCover_270_360(String cover_270_360) {
		this.cover_270_360 = cover_270_360;
	}

	public String getCover_360_480() {
		return cover_360_480;
	}

	public void setCover_360_480(String cover_360_480) {
		this.cover_360_480 = cover_360_480;
	}

	public String getLongdata() {
		return longdata;
	}

	public void setLongdata(String longdata) {
		this.longdata = longdata;
	}

	public String getShortdata() {
		return bookShortDesc;
	}

	public void setShortdata(String shortdata) {
		this.bookShortDesc = shortdata;
	}

	public String getShortdesc() {
		return shortdesc;
	}

	public void setShortdesc(String shortdesc) {
		this.shortdesc = shortdesc;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getClassnm() {
		return classnm;
	}

	public void setClassnm(String classnm) {
		this.classnm = classnm;
	}

	public String getClassurl() {
		return classurl;
	}

	public void setClassurl(String classurl) {
		this.classurl = classurl;
	}

	public String getPennm() {
		return authorName;
	}

	public void setPennm(String pennm) {
		this.authorName = pennm;
	}

	public String getAuthorid() {
		return authorid;
	}

	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}

	public String getChapterid() {
		return chapterid;
	}

	public void setChapterid(String chapterid) {
		this.chapterid = chapterid;
	}

	public String getAuthorurl() {
		return authorurl;
	}

	public void setAuthorurl(String authorurl) {
		this.authorurl = authorurl;
	}

	public String getConturl() {
		return conturl;
	}

	public void setConturl(String conturl) {
		this.conturl = conturl;
	}

	public String getReadpageurl() {
		return readpageurl;
	}

	public void setReadpageurl(String readpageurl) {
		this.readpageurl = readpageurl;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getClicks() {
		return clicks;
	}

	public void setClicks(String clicks) {
		this.clicks = clicks;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getBooklevel() {
		return booklevel;
	}

	public void setBooklevel(String booklevel) {
		this.booklevel = booklevel;
	}

	public String getIsHot() {
		return isHot;
	}

	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCo_percent() {
		return co_percent;
	}

	public void setCo_percent(String co_percent) {
		this.co_percent = co_percent;
	}

}
