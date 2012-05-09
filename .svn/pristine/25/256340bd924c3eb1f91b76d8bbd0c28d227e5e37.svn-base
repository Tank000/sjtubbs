package com.tyt.bbs.parser;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.tyt.bbs.entity.TopicItem;
import com.tyt.bbs.utils.Net;
import com.tyt.bbs.utils.Property;

public class WebTopTenParser {

	 private final int STYLE_TOPTEN=1;
	 private final int STYLE_RECOMMEND=2;
	    
	private ArrayList<TopicItem> topTens;
    private String urlMoreArticle;
    private int mType;
	
	private String select_table_recommend= "table.Bg_Color_Midium table:contains(推荐文章)";
	private String select_table_toptens="table.Bg_Color_Midium table:contains(分类精彩讨论区)";
	private String select_table_alltoptens="table.Bg_Color_Midium table:contains(十大热门话题)";
	private String indexUrl=Property.Base_URL+"/php/bbsindex.html";

	public WebTopTenParser(int type) {
		topTens = new ArrayList<TopicItem>();
		this.mType=type;
	}
	
	public ArrayList<TopicItem> getList(){
		return this.topTens;
	}
	
	
	/**
	 *将对应的<tr>...</tr>解析
	 *对应信息储存为一个TopicItem
	 */
	public TopicItem getTr2TopicItem(Element element_item,int category){
		
		TopicItem temp=new TopicItem();
		
		Elements element_item_colums=element_item.select("td");
		
		Element tempData;
		
		//获取Board的名称以及链接地址
		tempData=element_item_colums.get(0);
		temp.setBoard(tempData.text());
		temp.setBoardLink(Property.Base_URL+tempData.select("a").attr("href"));
		
		//获取Title的名称以及链接地址
		tempData=element_item_colums.get(1);
		temp.setTitle(tempData.text());
		String href=tempData.select("a").attr("href").replace("bbstcon", "bbswaptcon");
		String link=Property.Base_URL+href;
		temp.setLinkUrl(link);
		
		if(STYLE_TOPTEN==mType){
			//获取AuthorId的名称
			tempData=element_item_colums.get(2);
			temp.setAuthorID(tempData.text());
		}
		else if(STYLE_RECOMMEND==mType){
			
			//获取发布时间Time
			tempData=element_item_colums.get(2);
			temp.setTime(tempData.text());
			
			//获取AuthorId的名称
			tempData=element_item_colums.get(3);
			temp.setAuthorID(tempData.text());
		}
		
		temp.setCategory(category);
		return temp;
	}
	
	/**
	 * 解析推荐文章的topten
	 */
	public void paserRecommend(Document doc){
		
		Element elements_table_recommend=doc.select(select_table_recommend)
											.first()
											.select("table tr[valign=top] table")
											.last();
		Elements elements_tr_recommend=elements_table_recommend.select("tr");

		for(Element element_item:elements_tr_recommend)
		{
			TopicItem temp;

			temp=getTr2TopicItem(element_item,TopicItem.CATEGORY_RECOMMEND);
			//添加到topTens列表中
			topTens.add(temp);
		}
		
	}
	
	/**
	 * 解析 分类精彩讨论区
	 * @param doc
	 */
	public void paserTopTen(Document doc){
		
		//获取各分栏的toptens,总共有12个分栏
		//<td>.....</td>
		Elements elements_td_totens=doc.select(select_table_toptens)
											.first()
											.select("td[align=center] ")
											.select("td[bgcolor=#f6f6f6]");
		
		int size=elements_td_totens.size();
		for(int i=0;i<size;i++){
			
			Element element_td=elements_td_totens.get(i);
			Elements elements_tr=element_td.select("td[valign=top]  tr");
			if(!elements_tr.isEmpty()) {
				Boolean isFirst =true;
				for(Element element_item:elements_tr){
					TopicItem temp;
					temp=getTr2TopicItem(element_item,i);
					if(isFirst)temp.setColumId(i+0x10);
					else
						temp.setColumId(i);
					isFirst=false;
					//添加到topTens列表中
	    			topTens.add(temp);
				}
			}
		
		}
		
	}
	
	public void paserAllTopTen(Document doc){
		
		//获取各分栏的toptens,总共有12个分栏
		//<td>.....</td>
		Elements elements_tr_toptens=doc.select(select_table_alltoptens)
											.first()
											.select("td[align=center] ")
											.select("td[bgcolor=#f6f6f6]");
		Elements elements_tr_topten=elements_tr_toptens.select("tr");
		boolean isFirst=true;
		for(Element element_item:elements_tr_topten)
		{
			TopicItem temp;

			temp=getTr2TopicItem(element_item,TopicItem.CATEGORY_RECOMMEND);
			if(isFirst)temp.setColumId(0x10);
			else
				temp.setColumId(0);
			isFirst=false;
			//添加到topTens列表中
			topTens.add(temp);
		}
		
	}
	
	public WebTopTenParser parser() throws Exception {
      
        	//打开连接，获取原html
            String sourceString =Net.getInstance().get(indexUrl);
    		Document doc=Jsoup.parse(sourceString);
    		
    		if(STYLE_TOPTEN==mType){
    			paserAllTopTen(doc);
    			paserTopTen(doc);
    		}
    		else if(STYLE_RECOMMEND==mType){
    			paserRecommend(doc);
    		}
    	
		   return this;
	}
	
}
