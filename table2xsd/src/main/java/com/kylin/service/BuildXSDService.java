package com.kylin.service;

import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kylin.dao.FieldDao;
import com.kylin.entities.Field;

@Service
public class BuildXSDService {
	
	@Autowired 
	private ConvertService convertService;
	
	@Autowired
	FieldDao fieldDao;
	
	public void buildXSDContent(){
		List<Field> fields = fieldDao.findAll();
		Document document = DocumentHelper.createDocument();
		Element schemaElement = convertService.setSchemaElement(document);
		Element squenceElement2 = convertService.setComplexType(schemaElement);
		convertService.setSimpleTypeNode(squenceElement2, fields);
		try {
			convertService.write(document);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
