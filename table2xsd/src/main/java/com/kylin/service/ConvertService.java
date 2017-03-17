package com.kylin.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Service;

import com.kylin.entities.Field;

@Service
public class ConvertService {

	// 设置schema
	public Element setSchemaElement(Document outDocument) {
		Element schemaElement = outDocument.addElement("xs:schema");
		schemaElement.addNamespace("xs", "http://www.w3.org/2001/XMLSchema");
		schemaElement.addAttribute("targetNamespace", "http://www.w3school.com.cn");
		schemaElement.addAttribute("xmlns", "http://www.w3school.com.cn");
		schemaElement.addAttribute("elementFormDefault", "qualified");
		return schemaElement;
	}

	// 设置complexType，即field字段及其祖先节点
	public Element setComplexType(Element schemaElement) {
		Element configElement = schemaElement.addElement("xs:element");
		configElement.addAttribute("name", "config");
		Element complexTypeElement = configElement.addElement("xs:complexType");
		Element sequenceElement = complexTypeElement.addElement("xs:sequence");
		Element fieldsElement = sequenceElement.addElement("xs:element");
		fieldsElement.addAttribute("name", "field");
		fieldsElement.addAttribute("maxOccurs", "unbounded");
		Element complexTypeElement2 = fieldsElement.addElement("xs:complexType");
		Element sequenceElement2 = complexTypeElement2.addElement("xs:sequence");
		return sequenceElement2;
	}

	public void reflect(Object obj) {
		Map<String, String> map = null;
		if (obj == null)
			return;
		java.lang.reflect.Field[] fields = obj.getClass().getDeclaredFields();
		String[] types1 = { "int", "java.lang.String", "boolean", "char", "float", "double", "long", "short", "byte" };
		String[] types2 = { "Integer", "java.lang.String", "java.lang.Boolean", "java.lang.Character",
				"java.lang.Float", "java.lang.Double", "java.lang.Long", "java.lang.Short", "java.lang.Byte" };
		for (int j = 0; j < fields.length; j++) {
			fields[j].setAccessible(true);
			// 字段名
			System.out.print(fields[j].getName() + ":");
			// 字段值
			for (int i = 0; i < types1.length; i++) {
				if (fields[j].getType().getName().equalsIgnoreCase(types1[i])
						|| fields[j].getType().getName().equalsIgnoreCase(types2[i])) {
					try {
						System.out.print(fields[j].get(obj) + "     ");
						map.put(fields[j].getName(), (String) fields[j].get(obj));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}



	// 设置SimpleType的元素
	public void setSimpleTypeNode(Element tarElement, List<Field> fields) {

		for (Field field : fields) {
			if (field.getName() != null) {
				Element element = tarElement.addElement("xs:element");
				setSimpleTypeAttrbute(element, fields);
			} else {
				throw new RuntimeException("该变量不存在");
			}

		}
	}

	// 设置简易类型的属性
	public void setSimpleTypeAttrbute(Element tarElement, List<Field> fields) {
		for (Field field : fields) {
			if (field.getClass().getName().equals("name")) {
				tarElement.addAttribute("name", field.getName());
			} else if (field.getType().equals("type")) {
				tarElement.addAttribute("type", field.getType());
			} else if (field.getMinOccurs().equals("minOccurs")) {
				tarElement.addAttribute("minOccurs", field.getMinOccurs());
			} else if (field.getMaxOccurs().equals("maxOccurs")) {
				tarElement.addAttribute("maxOccurs", field.getMaxOccurs());
			} else if (field.getEnumeration().equals("enumeration")) {
				setEmumNodes(tarElement, field.getEnumeration());
			} else if (field.getIntegerRange().equals("integerRange")) {
				setIntegerRange(tarElement, field.getIntegerRange());
			}
		}
	}

	// 输出xml
	public void write(Document document) throws IOException {
		// 指定文件
		XMLWriter writer = new XMLWriter(new FileWriter("ouput.xsd"));
		writer.write(document);
		writer.close();
		// 美化格式
		OutputFormat format = OutputFormat.createPrettyPrint();
		writer = new XMLWriter(System.out, format);
		writer.write(document);
		// 缩减格式
		format = OutputFormat.createCompactFormat();
		writer = new XMLWriter(System.out, format);
		writer.write(document);
	}

	// 设置限定（整数的范围）
	public void setIntegerRange(Element tarElement, String range) {
		String[] rangeArray = range.split(",");
		String max = rangeArray[1];
		String min = rangeArray[0];
		Element simpleTypeElement = tarElement.addElement("xs:simpleType");
		Element restrictionElement = simpleTypeElement.addElement("xs:restriction");
		restrictionElement.addAttribute("base", "xs:integer");
		Element minInclusiveElement = restrictionElement.addElement("xs:minInclusive");
		minInclusiveElement.addAttribute("value", min);
		Element maxInclusiveElement = restrictionElement.addElement("xs:maxInclusive");
		maxInclusiveElement.addAttribute("value", max);
	}

	// 设置限定（枚举限定）
	public void setEmumNodes(Element curElement, String enums) {
		Element simpleTypeElement = curElement.addElement("xs:simpleType");
		Element restrictionElement = simpleTypeElement.addElement("xs:restriction");
		restrictionElement.addAttribute("base", "xs:string");
		String[] enumsArray = enums.split(",");
		for (String tem : enumsArray) {
			Element enumerationElement = restrictionElement.addElement("xs:enumeration");
			enumerationElement.addAttribute("value", tem.trim());
		}
	}

}
