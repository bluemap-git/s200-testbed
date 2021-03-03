
package com.PJ_s200Testbed.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.PJ_s200Testbed.domain.GetFeatureDTO;
import com.PJ_s200Testbed.service.Pointservice;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/searchbtn")
public class Searchcontroller {

	@Autowired
	Pointservice pService;
	@ResponseBody
	@PostMapping("/point")
	public List<GetFeatureDTO> search(@ModelAttribute(value = "GetFeatureDTO") GetFeatureDTO gDto) throws Exception {
		
	
		List<Integer> numarray = gDto.getNumarray();
		String point = gDto.getPoint();
		String polylist = gDto.getPolylist();
		String rectlist = gDto.getRectlist();
		String polygone = gDto.getPolygone();
		List<GetFeatureDTO> data = new ArrayList<>();
		if (point != null) {
			data = pService.search(point, numarray);
			for (GetFeatureDTO getFeatureDTO : data) {
			}
		} else if (polylist != null) {
			data = pService.line(polylist, numarray);
		} else if (rectlist != null) {
			data = pService.rect(rectlist, numarray);
		} else if (polygone != null) {
			data = pService.rect(polygone, numarray);
		}
		return data;
	}
}
