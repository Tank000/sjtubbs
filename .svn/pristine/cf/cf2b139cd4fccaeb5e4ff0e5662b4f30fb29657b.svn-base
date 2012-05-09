package com.tyt.bbs.parser;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.tyt.bbs.entity.TopicItem;
import com.tyt.bbs.utils.Net;
import com.tyt.bbs.utils.Property;

public class TopTenParser {
	
	private static final String TAG = "TopTenParser";
	private static TopTenParser mTopTenParser;
	
	private String select_table= "table.Bg_Color_Midium table:contains(十大热门话题) table";
	private String topTenUrl=Property.Base_URL+"/file/bbs/mobile/top100.html";

	public TopTenParser() {}
	public static TopTenParser getInstance() 
	{
		if (mTopTenParser == null) 
			mTopTenParser = new TopTenParser();
		return mTopTenParser;
	}
	public ArrayList<TopicItem> parser() throws Exception {
		ArrayList<TopicItem> topTens = new ArrayList<TopicItem>();
        	//打开连接，获取原html
            String sourceString =Net.getInstance().get(topTenUrl);
            
    		if(!topTens.isEmpty())topTens.clear();
    		String[] topten=sourceString.split("<br>");
			Document doc;
			boolean isFirst=true;
			for(int i=0;i<10;i++){
				
				doc=Jsoup.parse(topten[i]);
				doc.select("font.title").remove();
				
				TopicItem temp=new TopicItem();
				Elements element_item_colums=doc.select("a[href]");

    			//获取Board的名称以及链接地址
    			temp.setBoard(element_item_colums.first().text());
    			temp.setBoardLink(Property.Base_URL+element_item_colums.first().attr("href"));
				
    			//获取Title的名称以及链接地址
    			temp.setTitle(element_item_colums.last().text());
    			temp.setLinkUrl(Property.Base_URL+element_item_colums.last().attr("href"));
    			
    			//获取AuthorId的名称
    			element_item_colums.remove();
    			temp.setAuthorID(doc.text().replace(">", "")
			               .replace("[]", "")
			               .replace("[ ]", "")
			               .replace("<", ""));
    			temp.setCategory(-1);
				if(isFirst)temp.setColumId(0x10);
				else
					temp.setColumId(0);
				isFirst=false;
    			//添加到topTens列表中
    			topTens.add(temp);
    			
			}
		   return topTens;
	}
		
}
