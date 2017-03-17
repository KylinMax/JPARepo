package com.kylin.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.border.EtchedBorder;

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
		fieldsElement.addAttribute("minOccurs", "0");
		Element complexTypeElement2 = fieldsElement.addElement("xs:complexType");
		Element sequenceElement2 = complexTypeElement2.addElement("xs:sequence");
		return sequenceElement2;
	}

	/******************************************* 获取一个字段的所有properties ************************************************************/
	public Map<String, String> reflect(Object obj) {
		Map<String, String> map = new LinkedHashMap<>();
		if (obj == null)
			return null;
		java.lang.reflect.Field[] fields = obj.getClass().getDeclaredFields();
		String[] types1 = { "int", "java.lang.String", "boolean", "char", "float", "double", "long", "short", "byte" };
		String[] types2 = { "Integer", "java.lang.String", "java.lang.Boolean", "java.lang.Character",
				"java.lang.Float", "java.lang.Double", "java.lang.Long", "java.lang.Short", "java.lang.Byte" };
		for (int j = 0; j < fields.length; j++) {
			fields[j].setAccessible(true);
			if (fields[j].getName().equals("id")) {
				continue;
			}
			for (int i = 0; i < types1.length; i++) {
				if (fields[j].getType().getName().equalsIgnoreCase(types1[i])
						|| fields[j].getType().getName().equalsIgnoreCase(types2[i])) {
					try {
						if (fields[j].get(obj) != null) {
							map.put(fields[j].getName(), (String) fields[j].get(obj));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return map;
	}

	/******************************************* 设置简易类型元素 ************************************************************/
	// 设置SimpleType的元素
	public void setSimpleTypeNode(Element tarElement, List<Field> fields) {

		for (Field field : fields) {
			if (field.getName() != null) {
				Element element = tarElement.addElement("xs:element");
				setSimpleTypeAttrbute(element, field);
			} else {
				throw new RuntimeException("该变量不存在");
			}

		}
	}

	/******************************************* 设置简易类型的属性 ************************************************************/
	// 设置简易类型的属性
	public void setSimpleTypeAttrbute(Element tarElement, Field field) {
		Map<String, String> map = reflect(field);
		String type = null;
		Element restrictionElement = null;
		for (Entry<String, String> entry : map.entrySet()) {
			System.err.println(entry.getKey() + " " + entry.getValue());
			if (entry.getKey().equals("name")) {
				if (entry.getValue() != null) {
					tarElement.addAttribute("name", entry.getValue());
				}
			} else if (entry.getKey().equals("type")) {
				type = entry.getValue();
				if (entry.getValue() != null && map.size() <= 4) {
					tarElement.addAttribute("type", "xs:" + entry.getValue().toLowerCase());
				}
				if (entry.getValue() != null && map.size() > 4) {
					restrictionElement = setSimpletypeAndRestrictionElement(tarElement, type);
				}
			} else if (entry.getKey().equals("minOccurs")) {
				if (entry.getValue() != null) {
					tarElement.addAttribute("minOccurs", entry.getValue());
				}
			} else if (entry.getKey().equals("maxOccurs")) {
				if (entry.getValue() != null) {
					tarElement.addAttribute("maxOccurs", entry.getValue());
				}
			} else if (entry.getKey().equals("enumeration")) {
				if (entry.getValue() != null) {
					// 设置SimpleType和Restriction元素
					setEnumerationRestriction(restrictionElement, entry.getValue());
				}
			} else if (entry.getKey().equals("integerRange")) {
				if (entry.getValue() != null) {
					// 设置SimpleType和Restriction元素
					setIntegerRange(restrictionElement, entry.getValue());
				}
			} else if (entry.getKey().equals("length")) {
				if (entry.getValue() != null) {
					setLengthRestriction(restrictionElement, entry.getValue());
				}
			} else if (entry.getKey().equals("lengthRange")) {
				if (entry.getValue() != null) {
					setLengthRangeRestriction(restrictionElement, entry.getValue());
				}
			} else if (entry.getKey().equals("whiteSpace")) {
				if (entry.getValue() != null) {
					setWhiteSpaceRestriction(restrictionElement, entry.getValue());
				}
			} else if (entry.getKey().equals("fractionDigits")) {
				if (entry.getValue() != null) {
					setFractionDigits(restrictionElement, entry.getValue().trim());
				}
			} else {
				throw new RuntimeException("未知属性" + entry.getKey());
			}

		}
	}

	/*****************************************
	 * 设置<simpleType> 和 <restriction>
	 *************************************/

	public Element setSimpletypeAndRestrictionElement(Element tarElement, String type) {
		Element simpleTypeElement = tarElement.addElement("xs:simpleType");
		Element restrictionElement = simpleTypeElement.addElement("xs:restriction");
		restrictionElement.addAttribute("base", "xs:" + type.toLowerCase().trim());
		return restrictionElement;
	}

	/******************************************* 数值范围约束 ************************************************************/

	// 设置限定（整数的范围）
	public void setIntegerRange(Element restrictionElement, String range) {
		String[] rangeArray = range.split(",");
		if (rangeArray.length == 2) {
			String max = rangeArray[1];
			String min = rangeArray[0];
			// 设置具体值
			Element minInclusiveElement = restrictionElement.addElement("xs:minInclusive");
			minInclusiveElement.addAttribute("value", min.trim());
			Element maxInclusiveElement = restrictionElement.addElement("xs:maxInclusive");
			maxInclusiveElement.addAttribute("value", max.trim());
		}
	}

	/******************************************* 枚举约束 ************************************************************/

	// 设置限定（枚举限定）
	public void setEnumerationRestriction(Element restrictionElement, String enums) {
		String[] enumsArray = enums.split(",");
		for (String tem : enumsArray) {
			Element enumerationElement = restrictionElement.addElement("xs:enumeration");
			enumerationElement.addAttribute("value", tem.trim());
		}
	}

	/******************************************* 字符长度约束 ************************************************************/

	public void setLengthRestriction(Element restrictionElement, String length) {
		Element lengthElement = restrictionElement.addElement("xs:length");
		lengthElement.addAttribute("value", length.trim());
	}

	/******************************************* 字符长度范围约束 ************************************************************/

	public void setLengthRangeRestriction(Element restrictionElement, String lengthRange) {
		String[] lengthRangeArray = lengthRange.split(",");
		if (lengthRangeArray.length == 2) {
			String min = lengthRangeArray[0];
			String max = lengthRangeArray[1];
			Element minLengthElement = restrictionElement.addElement("xs:minLength");
			minLengthElement.addAttribute("value", min.trim());
			Element maxLengthElement = restrictionElement.addElement("xs:maxLength");
			maxLengthElement.addAttribute("value", max.trim());
		}

	}

	/******************************************* 空白字符处理约束 ************************************************************/

	public void setWhiteSpaceRestriction(Element restrictionElement, String string) {
		Element whiteSpaceElement = restrictionElement.addElement("xs:whiteSpace");
		whiteSpaceElement.addAttribute("value", string.trim());
	}

	/******************************************* 小数位数约束 ************************************************************/
	public void setFractionDigits(Element restrictionElement, String digitNum) {
		Element fractionDigits = restrictionElement.addElement("xs:fractionDigits");
		fractionDigits.addAttribute("value", digitNum.trim());
	}

	/******************************************* 输出文件 ************************************************************/
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

}
