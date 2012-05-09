package com.tyt.bbs.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

import com.tyt.bbs.entity.TopicItem;

public class XmlOperate {
	
	private static XmlOperate xml;
	private XmlOperate(){}
	public static XmlOperate getInstance() 
	{
		if (xml == null) 
		{
			xml = new XmlOperate();
		}
		return xml;
	}
	
	
	public String writeXml(ArrayList<TopicItem> topTens){
		if(topTens==null) return null;
	    XmlSerializer serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    try {
	        serializer.setOutput(writer);
	        serializer.startDocument("UTF-8", true);
	        serializer.startTag("", "topitems");
	        serializer.attribute("", "number",""+topTens.size());
	        serializer.attribute("", "date",""+new Date().toLocaleString());
	        for (TopicItem item: topTens){
	            serializer.startTag("", "topicItem");
	            serializer.attribute("", "authorID", item.getAuthorID());
	            serializer.attribute("", "columId", item.getColumId()+"");
	            
	            serializer.startTag("", "board");
	            serializer.attribute("", "link", item.getBoardLink());
	            serializer.text(item.getBoard());
	            serializer.endTag("", "board");
	            
	            serializer.startTag("", "title");
	            serializer.attribute("", "link", item.getLinkUrl());
	            serializer.text(item.getTitle());	           
	            serializer.endTag("", "title");
	            
	            serializer.endTag("", "topicItem");
	        }
	        serializer.endTag("", "topitems");
	        serializer.endDocument();
	        return writer.toString();
	        
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    } 
	}
	
	public String writeFavXml(ArrayList<String> itemObj ){
	    XmlSerializer serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    try {
	        serializer.setOutput(writer);
	        serializer.startDocument("UTF-8", true);
	        serializer.startTag("", "FavList");
	        serializer.attribute("", "number",""+itemObj.size());
	        for (String item: itemObj){
	            serializer.startTag("", "item");  
	            serializer.text(item);
	            serializer.endTag("", "item");
	        }
	        serializer.endTag("", "FavList");
	        serializer.endDocument();
	        return writer.toString();
	        
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    } 
	}
	
	public ArrayList<TopicItem> readXML(java.io.File file){
		
		ArrayList<TopicItem> topTens = new ArrayList<TopicItem>() ;
		 try {
			 SAXParserFactory factory = SAXParserFactory.newInstance();
			 SAXParser parser = factory.newSAXParser();
			 TopTenHandler handler=new TopTenHandler(topTens);
			 parser.parse(file, handler);
			 
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return topTens;
	}

	private class TopTenHandler extends DefaultHandler{
		 
		private TopicItem currentTopicItem;
		ArrayList<TopicItem> topTens;
		private String tagName;
		
		public TopTenHandler(ArrayList<TopicItem> topTens) {
			// TODO Auto-generated constructor stub
			this.topTens=topTens;
		}


		public void startElement(String uri, String localName, String qName,Attributes atts){ 
			if("topicItem".equals(localName)){
				currentTopicItem=new TopicItem();
				currentTopicItem.setAuthorID(atts.getValue("authorID"));
				currentTopicItem.setColumId(Integer.valueOf(atts.getValue("columId")));
			}
			else if("board".equals(localName)){
				currentTopicItem.setBoardLink(atts.getValue("link"));
			}
			else if("title".equals(localName)){
				currentTopicItem.setLinkUrl(atts.getValue("link"));
			}
			
			tagName=localName;
		}
		
		
		 public void characters(char[] ch, int start, int length){
			 if(tagName!=null){
				 String data =new String(ch,start,length);
				 
				 if("board".equals(tagName)){
					 currentTopicItem.setBoard(data);
				 }
				 else if("title".equals(tagName)){
					 currentTopicItem.setTitle(data);
				 }
			 }
		 }
		 
		 
		  @Override

	       public void endElement (String uri, String localName, String name){
			  
			  if("topicItem".equals(localName)){
				  topTens.add(currentTopicItem);
				  currentTopicItem=null;
			  }
			  tagName=null;
		  }
	}

}
