package com.kylin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kylin.service.BuildXSDService;

@RestController
public class XsdController {
	@Autowired
	private BuildXSDService buildXSDService;

	@RequestMapping("/getXsdFile")
	public void getXsdFile() {
		buildXSDService.buildXSDContent();
		System.out.println("ok");
		
	}
}
