package com.tyt.bbs.entity;

public class TopicItem {
    
	public static int CATEGORY_RECOMMEND=-1;
	public static int CATEGORY_ALLTOPTEN=-2;
	
	private String board;
	private String boardLink;
	private String linkUrl;
	private String title;
	private String authorID;
	private String time;
	private int category;
	private int columId;
	public TopicItem(){};
	
	public String getBoard(){
		return this.board;
	}
	
	public void setBoard(String board){
		this.board=board;
	}
	
	public String getBoardLink(){
		return this.boardLink;
	}
	
	public void setBoardLink(String url){
		this.boardLink=url;
	}
	
	public String getLinkUrl(){
		return this.linkUrl;
	}
	
	
	public void setLinkUrl(String url){
		this.linkUrl=url;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public void setTitle(String title){
		this.title=title;
	}
	
	public String getAuthorID(){
		return this.authorID;
	}
	
	public void setAuthorID(String authorID){
		this.authorID=authorID;
	}
	
	public void setTime(String time){
		this.time=time;
	}
	
	public String getTime(){
		return this.time;
	}
	
	public int getCategory(){
		return this.category;
	}
	
	public void setCategory(int category){
		this.category=category;
	}

	public void setColumId(int colums) {
		this.columId = colums;
	}

	public int getColumId() {
		return columId;
	}
}
