package com.kylin.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kylin.service.BuildXSDService;

@RestController
public class XsdController {
	@Autowired
	private BuildXSDService buildXSDService;

	@RequestMapping("/getXsdFile")
	public ResponseEntity<InputStreamSource> getXsdFile() {
		String xml = buildXSDService.buildXSDContent().asXML();
		InputStream inputStream = new ByteArrayInputStream(xml.getBytes());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", "xsd.xsd"));
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		return ResponseEntity.ok().headers(headers).contentLength(xml.length())
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(new InputStreamResource(inputStream));
	}
}
