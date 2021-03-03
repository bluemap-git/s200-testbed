package com.PJ_s200Testbed.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.PJ_s200Testbed.domain.DatasetDTO;
import com.PJ_s200Testbed.service.Mainservice;
import com.PJ_s200Testbed.service.pager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller

public class Maincontroller {

	@Autowired
	Mainservice mService;
	
	 @GetMapping("/") 
	public String maingPage(
			@RequestParam(defaultValue="1") int curPage,
			@RequestParam(defaultValue="all") String search_option,
			@RequestParam(defaultValue="") String keyword,
			Model model) {
		return "main/Mainpage";
	}
}
