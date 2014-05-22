package com.darlen.xml.parse;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

/**
 * SAX parse XML
 * User: darlen liu
 * Date: 14-5-22
 * Time: 下午5:09
 * 参考文章：
 * http://www.cnblogs.com/nerxious/archive/2013/05/03/3056588.html
 * http://www.mkyong.com/java/how-to-read-xml-file-in-java-sax-parser/
 */
public class SAXParse extends DefaultHandler {
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		// 1.实例化SAXParserFactory对象
		SAXParserFactory factory = SAXParserFactory.newInstance();
		// 2.创建解析器
		SAXParser parser = factory.newSAXParser();
		// 3.获取需要解析的文档，生成解析器,最后解析文档
		File file = new File("XML_Validation\\src\\resources\\parse.xml");
		SAXParse dh = new SAXParse();
		parser.parse(file, dh);
	}

	@Override
	public void startDocument() throws SAXException {
		System.out.println("…………开始解析文档…………\n");
		super.startDocument();
	}

	/**
	 *
	 * @param arg0 是名称空间
	 * @param arg1 是包含名称空间的标签，如果没有名称空间，则为空
	 * @param arg2 是不包含名称空间的标签
	 * @param arg3 很明显是属性的集合
	 * @throws SAXException
	 */
	@Override
	public void startElement(String arg0, String arg1, String arg2,
							 Attributes arg3) throws SAXException {
		System.out.println("开始解析元素 " + arg2);
		if (arg3 != null) {
			for (int i = 0; i < arg3.getLength(); i++) {
				// getQName()是获取属性名称，
				System.out.print(arg3.getQName(i) + "=\"" + arg3.getValue(i) + "\"");
			}
		}
		System.out.print(arg2 + ":");
		super.startElement(arg0, arg1, arg2, arg3);
	}


	/**
	 * @param arg0 是传回来的字符数组，其包含元素内容
	 * @param arg1 arg1和arg2分别是数组的开始位置 和结束位置
	 * @param arg2 结束位置
	 * @throws SAXException
	 */
	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		String content = new String(arg0, arg1, arg2);
		System.out.println("Character:"+content);
		super.characters(arg0, arg1, arg2);
	}

	/**
	 *
	 * @param arg0 是名称空间
	 * @param arg1 是包含名称空间的标签，如果没有名称空间，则为空
	 * @param arg2 是不包含名称空间的标签
	 * @throws SAXException
	 */
	@Override
	public void endElement(String arg0, String arg1, String arg2)
			throws SAXException {
		System.out.println("结束解析元素  " + arg2);
		super.endElement(arg0, arg1, arg2);
	}

	@Override
	public void endDocument() throws SAXException {
		System.out.println("\n…………结束解析文档…………");
		super.endDocument();
	}







}
