package com.PJ_s200Testbed.service;

import java.util.List;

import com.PJ_s200Testbed.domain.GetFeatureDTO;

public interface Pointservice  {
	public List<GetFeatureDTO> search(String point, List<Integer> numarray );
	public List<GetFeatureDTO> line(String polylist, List<Integer> numarray );
	public List<GetFeatureDTO> rect(String rectlist, List<Integer> numarray );
 }
