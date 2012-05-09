package com.tyt.bbs.parser;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.util.Log;

import com.tyt.bbs.entity.PostItem;
import com.tyt.bbs.utils.Net;

public class PostParser {	    

	private String baseUrl="https://bbs.sjtu.edu.cn/bbswapdoc?board=";
	private String themeUrl="https://bbs.sjtu.edu.cn/bbswaptdoc,board,";
	private String UrlPrefix="https://bbs.sjtu.edu.cn/";
	private String board;
	private String BoardMasters;
	private String prelink;
	private String nextlink;
	private int parserMode;
	private ArrayList<PostItem> post;

	public PostParser(String board) {
		this.board=board;
		post = new ArrayList<PostItem>();
		parserMode = 0;
	}

	public void setMode(int mode){
		parserMode=mode;
	}


	private String getUrl(int parserMode,Boolean isTheme){
		switch(parserMode){
		case 0: return isTheme?themeUrl+board+".html":baseUrl+board;
		case 1: return prelink;
		case 2: return nextlink;
		}
		return null;
	}

	public ArrayList<PostItem> parser(Boolean isTheme) throws Exception {
		if(parserMode==0&&!post.isEmpty())post.clear();
		String sourceString = Net.getInstance().get(getUrl(parserMode,isTheme));
		Log.v("=URL=", getUrl(parserMode,isTheme));
		Document doc=null;
		int pos1,pos2;
		if(isTheme){
			// 获得板主名单和上下页
			pos1 = sourceString.indexOf("</br>");
		}else{
			// 获得板主名单和上下页
			pos1 = sourceString.indexOf("<hr>");
		}
		pos2 = sourceString.indexOf("</br>",pos1+5);
		//		Log.v("",pos1+"  "+pos2);
		doc=Jsoup.parse(sourceString.substring(pos1, pos2));
		setBoardMasters(doc.text());


		pos1 = sourceString.indexOf("</br>",pos2);
		pos2 = sourceString.indexOf("<hr>",pos1);
		doc=Jsoup.parse(sourceString.substring(pos1, pos2));
		Elements els=doc.select("a[href]");
		prelink = UrlPrefix+els.first().attr("href");
		if(els.size()>1)
			nextlink=  UrlPrefix+els.first().nextElementSibling().attr("href");
		else
			nextlink ="";
//		Log.v("", " prelink="+prelink+" nextlink"+nextlink);


		// 获得文章列表
		pos1 = sourceString.indexOf("<hr>",pos2)+4;
		if(isTheme)pos2 = sourceString.indexOf("<hr>",pos1);
		else pos2 = sourceString.indexOf("<hr/>",pos1);
		int lastpos=pos2;

		while((pos1 < lastpos)&&(lastpos-pos1>10)) 
		{
			PostItem tmp=new PostItem();
			pos2 = sourceString.indexOf("<a",pos1);
			String PostIndex =sourceString.substring(pos1,pos2);
			if (PostIndex.startsWith("<font")) 
			{
				int p = PostIndex.indexOf(">",0)+1;
				int q = PostIndex.indexOf("</",p);
				PostIndex = PostIndex.substring(p+1,q-1);
				tmp.setIsBottom(false);
			}
			else {
				PostIndex = PostIndex.replace("&nbsp;", " ").trim();
				tmp.setIsBottom(true);
			}
			tmp.setPostIndex(PostIndex);
			//			Log.v("=Index=", PostIndex);

			pos1 = sourceString.indexOf(">",pos2)+1;
			pos2 = sourceString.indexOf("</a>",pos1);
			tmp.setAuthor(sourceString.substring(pos1,pos2));

			//			Log.v("=Author=", sourceString.substring(pos1,pos2));

			pos1 = pos2 + 4;
			pos2 = sourceString.indexOf("</br>",pos1);
			String postTime = sourceString.substring(pos1,pos2);
			postTime = postTime.replaceAll("&nbsp;", " ").trim();
			tmp.setTime(postTime);

			//			Log.v("=Time=", postTime);

			pos1 = sourceString.indexOf("href=",pos2)+5;
			pos2 = sourceString.indexOf(">",pos1);
			String link = sourceString.substring(pos1,pos2);
//			if(link.contains("bbstopcon"))
//				link = link.replace("bbstopcon", "bbswaptopcon");
			tmp.setLink(UrlPrefix+link);

//			Log.v("=Link=", UrlPrefix+link);

			pos1 = pos2+1;
			pos2 = sourceString.indexOf("</a>",pos1);
			String title = sourceString.substring(pos1,pos2);
			if (title.startsWith("<font"))
			{
				int p = title.indexOf("</font>",0);
				title = title.substring(p+7);	
			}
			title = title.replace("&amp;","&");
			if(title.contains("</font>")){
				title = Jsoup.parse(title).text();
			}
			tmp.setTitle(title);
			post.add(tmp);

			pos1 = sourceString.indexOf("<p>",pos2)+3;
			//			Log.v("=Title=", title);
		}
		return post;
	}

	public void setBoardMasters(String boardMasters) {
		BoardMasters = boardMasters;
	}

	public String getBoardMasters() {
		return BoardMasters;
	}

	/**
	 * @return the prelink
	 */
	public String getPrelink() {
		return prelink;
	}

	/**
	 * @return the nextlink
	 */
	public String getNextlink() {
		return nextlink;
	}

}
