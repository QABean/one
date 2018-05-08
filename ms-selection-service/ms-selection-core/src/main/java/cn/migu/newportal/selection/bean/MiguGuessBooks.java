package cn.migu.newportal.selection.bean;

import java.util.List;

public class MiguGuessBooks {
    private String bookid;
    private String bookShowName;
    private String cover_90_120;
    private String cover_123_164;
    private String cover_180_240;
    private String cover_270_360;
    private String cover_360_480;
    private String longdata;
    private String shortdata;
    private String shortdesc;
    private String reason;
    private String classnm;
    private String classurl;
    private String pennm;
    private String authorid;
    private String chapterid;
    private String authorurl;
    private String conturl;
    private String readpageurl;
    private String status;
    private String clicks;
    private String comments;
    private String booklevel;
    private String isHot;
    private String categoryId;
    private String prtype;
    private String tag_name;
    private String ontime;
    private String contentType;
    private String sheetDesc;
    private String isFreeForLimit;
    private String isUgc;
    private String isFamous;
    private String isFree;
    private String isFinish;
    private String score;
    private String booknum;
    private String sheetid;
    private String speakerName;
    private String speakerUrl;
    private List<MiguGuessBooks> bookList;
    public String getScore()
    {
        return score;
    }

    public void setScore(String score)
    {
        this.score = score;
    }

    public String getBooknum()
    {
        return booknum;
    }

    public void setBooknum(String booknum)
    {
        this.booknum = booknum;
    }

    public String getSheetid()
    {
        return sheetid;
    }

    public void setSheetid(String sheetid)
    {
        this.sheetid = sheetid;
    }
    
    public String getBookShowName()
    {
        return bookShowName;
    }
    
    public void setBookShowName(String bookShowName)
    {
        this.bookShowName = bookShowName;
    }

    public List<MiguGuessBooks> getBookList()
    {
        return bookList;
    }

    public void setBookList(List<MiguGuessBooks> bookList)
    {
        this.bookList = bookList;
    }

    public String getIsFamous()
    {
        return isFamous;
    }

    public void setIsFamous(String isFamous)
    {
        this.isFamous = isFamous;
    }

    public String getIsFree()
    {
        return isFree;
    }

    public void setIsFree(String isFree)
    {
        this.isFree = isFree;
    }

    public String getIsFinish()
    {
        return isFinish;
    }

    public void setIsFinish(String isFinish)
    {
        this.isFinish = isFinish;
    }

    public String getIsUgc()
    {
        return isUgc;
    }

    public void setIsUgc(String isUgc)
    {
        this.isUgc = isUgc;
    }

    public String getIsFreeForLimit()
    {
        return isFreeForLimit;
    }

    public void setIsFreeForLimit(String isFreeForLimit)
    {
        this.isFreeForLimit = isFreeForLimit;
    }

    private List<SheetBookCont> miguGuessBooksList;
    
    public List<SheetBookCont> getMiguGuessBooksList()
    {
        return miguGuessBooksList;
    }

    public void setMiguGuessBooksList(List<SheetBookCont> miguGuessBooksList)
    {
        this.miguGuessBooksList = miguGuessBooksList;
    }

    public String getSheetDesc()
    {
        return sheetDesc;
    }

    public void setSheetDesc(String sheetDesc)
    {
        this.sheetDesc = sheetDesc;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    private BookSheetDataBean bookSheetDataBean;
    
    public BookSheetDataBean getBookSheetDataBean() {
        return bookSheetDataBean;
    }

    public void setBookSheetDataBean(BookSheetDataBean bookSheetDataBean) {
        this.bookSheetDataBean = bookSheetDataBean;
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

    public void setBooknm(String booknm) {
        this.bookShowName = booknm;
    }

    public String getCover_90_120() {
        return cover_90_120;
    }

    public void setCover_90_120(String cover_90_120) {
        this.cover_90_120 = cover_90_120;
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

    public void setCover_180_240(String bookCoverLogo) {
        this.cover_180_240 = bookCoverLogo;
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
        return shortdata;
    }

    public void setShortdata(String shortdata) {
        this.shortdata = shortdata;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public void setShortdesc(String bookShortDesc) {
        this.shortdesc = bookShortDesc;
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
        return pennm;
    }

    public void setPennm(String pennm) {
        this.pennm = pennm;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getPrtype() {
        return prtype;
    }

    public void setPrtype(String prtype) {
        this.prtype = prtype;
    }

    public String getOntime() {
        return ontime;
    }

    public void setOntime(String ontime) {
        this.ontime = ontime;
    }

    public String getSpeakerName()
    {
        return speakerName;
    }

    public void setSpeakerName(String speakerName)
    {
        this.speakerName = speakerName;
    }

    public String getSpeakerUrl()
    {
        return speakerUrl;
    }

    public void setSpeakerUrl(String speakerUrl)
    {
        this.speakerUrl = speakerUrl;
    }

}
