package org.first.itsApex.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class commonController {
	@GetMapping("/firstApi")
	public String firstApi() {
		return "hello";
	}

}
