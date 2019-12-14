package com.heiduo.ts.test.Utils;

import com.heiduo.ts.test.BuilderPattern.AngleBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/12/14
 */
public class XMLUtils {
    public static Object getBean(){
        try {
            DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dFactory.newDocumentBuilder();
            Document doc = builder.parse(new File("test\\","config.xml"));

            //获取包含类名的文本节点
            NodeList nl = doc.getElementsByTagName("className");
            Node classNode = nl.item(0).getFirstChild();
            String cName = classNode.getNodeValue();
            Class c = Class.forName(cName);
            return c.newInstance();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
