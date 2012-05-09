package com.tyt.bbs.parser;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.Log;

import com.tyt.bbs.entity.ArticleItem;
import com.tyt.bbs.utils.ImageLoader;
import com.tyt.bbs.utils.Net;
import com.tyt.bbs.utils.Property;

public class ArticleParser {

	private ArrayList<ArticleItem> articleLists;
	private String articleUrl;
	private ImageLoader  imageLoader;
	private Handler handler;
	private Boolean isStore;
	private int parserMode;
	
	public ArticleParser(String Url,Handler handler,Boolean isStore) {
		articleLists = new ArrayList<ArticleItem>() ;
		articleUrl=Url;
		this.handler =handler;
		this.isStore= isStore;
		parserMode=0;
		imageLoader= new ImageLoader();
	}

	public ArrayList<ArticleItem> getList(){
		return this.articleLists;
	}

	public void setMode(int mode){
		parserMode=mode;
	}
	
	public String parser() throws Exception{
		if(parserMode==0&&!articleLists.isEmpty())articleLists.clear();
		String fulltext =Net.getInstance().get(articleUrl);
		String sourceString =fulltext;
		Log.v("=ArticleUrl=", articleUrl);
//		String pagetext =sourceString.subSequence(sourceString.indexOf("<hr>")+4, 
//													 sourceString.indexOf("<pre>")).toString();
//		if(pagetext.equalsIgnoreCase("")){
//			
//		}else{
//			Elements els=Jsoup.parse(pagetext).select("a[href]");
//			
//		}
		return parser(sourceString);
	}
	
	public String parser(String sourceString) throws Exception{
		Document doc=Jsoup.parse(sourceString);
//		doc.select("font[color=808080]").remove();
		Elements elements_p=doc.select("pre");
		
		for(Element elements_article:elements_p)
		{
			int headpos=0;
			int tailpos=0;
			final ArticleItem temp=new ArticleItem();
			String articeContent=elements_article.text();

			String link=elements_article.select("a").first().attr("href");
			//			 	System.out.println(link);
			temp.setLink(link);

			headpos = articeContent.indexOf(" ",tailpos);
			tailpos = articeContent.indexOf(" ",headpos + 1);
			String author = articeContent.substring(headpos, tailpos);
			//				System.out.println(author);
			temp.setAuthor(author);

			headpos = tailpos + 1;
			tailpos = articeContent.indexOf("\n",headpos);

			String time = articeContent.substring(headpos, tailpos);
			time=time.trim();				
			temp.setTime(time);



			String articleHtml=elements_article.toString();
			//换行符用<br/>代替  ，解决排版问题
//							System.out.println(articleHtml);
			articleHtml=articleHtml.replace("\n", "<br/>");

			headpos=articleHtml.indexOf(time,0);
			if(-1==(tailpos = articleHtml.indexOf("Re:",headpos))){
				tailpos = articleHtml.indexOf("○",headpos);
			}
			if(articleHtml.contains("</pre>"))
				articleHtml = articleHtml.replace("</pre>", "");
			String article=null;
			if(tailpos!=-1){
				article=articleHtml.substring(tailpos);
			}else{
				article = articleHtml.substring(headpos, articeContent.length());
			}
			while(article.contains("<br/><br/>"))
				article=article.replace("<br/><br/>", "<br/>");
//			System.out.println(article);
			//				article=article.replace("</pre>", "");
			//				article = article.trim();
			String articletext = article.substring(0, article.length()-5);
			if(articletext.contains(" onload=")) articletext = articletext.replace(" onload=\"if(this.width &gt; screen.width - 200){this.width = screen.width - 200}\"", "");
			temp.setArticle(articletext);
			
			Html.fromHtml(articletext,new ImageGetter(){

				private Drawable drawable;
				@Override
				public Drawable getDrawable(String source) {
					// TODO Auto-generated method stub
					final String imageUrl=source.contains("http")?source:Property.Base_URL+source;
					imageLoader.loadDrawable(imageUrl,	isStore,temp.getImageCache(),new ImageLoader.ImageCallback() {
						
						@Override
						public void imageLoaded(Drawable imageDrawable) {
							// TODO Auto-generated method stub
							drawable = imageDrawable;
							Message message  = new Message();
							message.what = 0x1;
							handler.sendMessage(message);
						}
					});
					return drawable;
				}
				
			},null);

			articleLists.add(temp);
		}

		return sourceString;
	}


}
