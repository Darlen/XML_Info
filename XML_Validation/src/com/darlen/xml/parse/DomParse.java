package com.darlen.xml.parse;

import org.dom4j.DocumentHelper;
import org.dom4j.io.DOMReader;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Dom4j parse XML
 * User: darlen liu
 * Date: 14-5-22
 * Time: 下午3:01
 */
public class DomParse {
	private static final String filePath = "XML_Validation\\src\\resources\\parse.xml";

	public static void main(String[] args) throws IOException {
		DomParse domParse = new DomParse();
		Document document = domParse.getDocument(filePath);
		domParse.parseDocument(document);
		domParse.createXML();
	}

	/**
	 * first read the file as a document
	 * @param filePath
	 * @return
	 */
	public Document getDocument(String filePath){
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		Document document = null;

		try{
			//DOM parse instance
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			//parse an XML file into a DOM tree
			document =  builder.parse(filePath);//this.getClass().getResourceAsStream(filePath));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return  document;
	}

	/**
	 * 由W3C Document 转成DOM4J的document然后再解析Element
	 * @param document
	 */
	public void parseDocument(Document document){
		//转化w3cDocument to DOM4j Document
		DOMReader reader = new DOMReader();
		org.dom4j.Document dom4jDoc = reader.read(document);
		org.dom4j.Element root = dom4jDoc.getRootElement();
		System.out.println("Root Element Name:"+root.getName());
		List childList = root.elements();
		System.out.println("Size:"+childList.size());
		traverElement(root,0);
	}

	/**
	 * 遍历Element，并取出相应的Element的Name,attribute和value
	 * @param root
	 * @param i
	 */
	private void traverElement(org.dom4j.Element root,int i){
		for(Iterator<org.dom4j.Element> iter = root.elementIterator();iter.hasNext();){
			System.out.println(++i);
			org.dom4j.Element element = iter.next();
			System.out.println("Name:"+element.getName()+";Attribute ID:"+element.attributeValue("id")+";Value:"+element.getText());
			traverElement(element,i);
		}
	}

	/**
	 * W3C Document解析Element
	 * @param document
	 */
	public void parseDocument2(Document document){
		//get root element
		Element rootElement = document.getDocumentElement();

		//traverse child elements
		NodeList nodes = rootElement.getChildNodes();
		for (int i=0; i < nodes.getLength(); i++)
		{
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element child = (Element) node;
				//process child element
			}
		}

		NodeList nodeList = rootElement.getElementsByTagName("book");
		if(nodeList != null)
		{
			for (int i = 0 ; i < nodeList.getLength(); i++)
			{
				Element element = (Element)nodeList.item(i);
				String id = element.getAttribute("id");
				System.out.println(id);
			}
		}
	}

	/**
	 * 创建XML
	 * @throws IOException
	 */
	public void createXML() throws IOException {
		// 创建文档并设置文档的根元素节点 ：第一种方式
		// Document document = DocumentHelper.createDocument();
		//
		// Element root = DocumentHelper.createElement("student");
		//
		// document.setRootElement(root);

		// 创建文档并设置文档的根元素节点 ：第二种方式
		org.dom4j.Element root = DocumentHelper.createElement("student");
		org.dom4j.Document document = DocumentHelper.createDocument(root);

		root.addAttribute("name", "zhangsan");

		org.dom4j.Element helloElement = root.addElement("hello");
		org.dom4j.Element worldElement = root.addElement("world");

		helloElement.setText("hello");
		worldElement.setText("world");

		helloElement.addAttribute("age", "20");

		XMLWriter xmlWriter = new XMLWriter();
		xmlWriter.write(document);

		OutputFormat format = new OutputFormat("    ", true);

		XMLWriter xmlWriter2 = new XMLWriter(new FileOutputStream("student2.xml"), format);
		xmlWriter2.write(document);

		xmlWriter2.close();
	}
}
