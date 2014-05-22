package com.darlen.xml.validation;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.util.XMLErrorHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import java.io.*;

/**
 * 本类用于验证XML的有效性（DTD/Schema）
 * User: darlenliu
 * Date: 14-5-21
 * Time: 下午3:58
 */
public class XmlValidationTest1 {
	private static Logger logger = Logger.getLogger(XmlValidationTest1.class);
	public static final int VALID_DTD = 0;
	public static final int VALID_XSD = 1;

	public static void main(String[] args) throws Exception {
		XmlValidationTest1 test1 = new XmlValidationTest1();
		//这里的路劲有点问题，总是在流读取的时候说找不到，没办法，只能写绝对路径了
		boolean isValid = test1.testXML("/resources/test1.xml", "E:\\develop_workspace\\XML_Info\\XML_Validation\\src\\resources\\test1.xsd", 1);
		//test1.validateXMLBySchema("/resources/test1.xml","E:\\develop_workspace\\XML_Info\\XML_Validation\\src\\resources\\test1.xsd");///resources/test1.xml
		if (isValid) {
			System.out.println("The Xml is verified successful.");
		} else {
			System.out.println("The Xml file has some error.");
		}
	}

	/**
	 * @param xmlFile        xml 文件路径
	 * @param validationFile 校验文件路径
	 * @param mode           校验类型
	 * @return 校验是否成功
	 */
	public boolean testXML(String xmlFile, String validationFile, int mode) throws Exception {
		if (VALID_DTD == mode) {
			return validateXMLByDTD(xmlFile, validationFile);
		} else if (VALID_XSD == mode) {
			return validateXMLBySchema(xmlFile, validationFile);
		}
		return true;
	}

	/**
	 * 利用dom4j通过dtd或者是schema验证XML
	 *
	 * @param xmlFile        文件路径
	 * @param validationFile 校验文件路径
	 * @return
	 * @throws Exception
	 */
	public boolean validateXMLBySchema(String xmlFile, String validationFile) throws Exception {
		SAXReader reader = new SAXReader(true);// 创建SAXReader对象并制定需要验证,也可通过reader.setValidation(true)来指定

		InputStream fis = null;
		XMLErrorHandler errorHandler = new XMLErrorHandler();
		try {
			//setFeature:http://kickjava.com/src/com/sun/org/apache/xerces/internal/impl/Constants.java.htm
			reader.setFeature("http://xml.org/sax/features/validation", true);// 设置功能标志的值name -功能名称，它是一个完全限定 URI。value - 请求的功能值（true 或false）。
			reader.setFeature("http://apache.org/xml/features/validation/schema", true);
			reader.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
			// 设置属性的值 name - 属性名称，它是一个完全限定 URI。value - 请求的属性值
			reader.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", validationFile);

			reader.setErrorHandler(errorHandler);
			return validate(xmlFile, reader);
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
	}

	/**
	 * 校验 dtd 的方法
	 *
	 * @param xmlFile        xml文件路径
	 * @param validationFile 校验文件路径
	 * @return 校验是否成功
	 */
	private boolean validateXMLByDTD(final String xmlFile, final String validationFile) throws IOException {

        /*
		 * 此类实体包括在 DTD 内引用的外部 DTD
         * 子集和外部参数实体（无论哪种情形，仅在在解析器都读取外部参数实体时）和在文档元素内引用的外部通用实体（如果解析器读取外部通用实体）
         */
		EntityResolver resolver = new EntityResolver() {// 应用程序解析外部实体
			public InputSource resolveEntity(String publicId, String systemId) {
				InputStream is = null;
				try {
					is = new FileInputStream(validationFile);// 读取dtd文档
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return null;
				}
				InputSource ins = new InputSource(is);
				ins.setPublicId(publicId);
				ins.setSystemId(systemId);
				return ins;// 返回 InputSource实例
			}
		};
		SAXReader reader = new SAXReader(true);
		reader.setEntityResolver(resolver); // 向SAX 驱动器注册一EntityResolver个实例。
		boolean flag = validate(xmlFile, reader);// 调用验证方法
		return flag;
	}

	/**
	 * @param xmlFile xml文件路径
	 * @param reader  SAXReader 对象
	 * @return 校验是否成功
	 */
	private boolean validate(final String xmlFile, final SAXReader reader) throws IOException {
		XMLErrorHandler errorHandle = new XMLErrorHandler();// 错误处理类实例
		reader.setErrorHandler(errorHandle);// 向 XML 阅读器注册一个实例
//		File file = new File(xmlFile);
		InputStream is = null;
//		if (file.exists() && file.isFile()) {
		try {
			is = this.getClass().getResourceAsStream(xmlFile);// 读取xml
			//is =new FileInputStream(xmlFile);// 读取xml
			InputStreamReader in = new InputStreamReader(is, "utf-8");
			reader.read(in);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (DocumentException e) {
			e.printStackTrace();
			return false;
		}
//		} else {
//			return false;
//		}

		if (errorHandle.getErrors().hasContent()) {// 如果错误处理类实例中包含错误信息返回false;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			XMLWriter errorWriter = new XMLWriter(out, OutputFormat.createPrettyPrint());
			errorWriter.write(errorHandle.getErrors());
			String sErrorMsg = new String(out.toByteArray(), "UTF-8");
			System.out.println(sErrorMsg);
		}

		return true;
	}
}
