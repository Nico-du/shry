package net.chinanets.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;


public class ChinaNetsUtil {
	
	private static Map tableName = new HashMap();
	
	static {
		tableName.put("介质", "AssetJz");
		tableName.put("PC机", "AssetComputer");
	}
	
	public static void writXml(Document document) {
	
		try {
			FileOutputStream fos = new FileOutputStream("c:/log.xml");
			OutputFormat of = new OutputFormat("    ", true);
			XMLWriter xw = new XMLWriter(fos, of);
			xw.write(document);
			xw.close();
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	public static String getTableName(String zclx) {
		return (String) tableName.get(zclx);
	}
}
