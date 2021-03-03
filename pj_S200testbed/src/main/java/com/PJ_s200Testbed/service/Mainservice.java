package com.PJ_s200Testbed.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.PJ_s200Testbed.domain.DatasetDTO;
import com.PJ_s200Testbed.domain.FeatureDTO;
import com.PJ_s200Testbed.domain.FitBounds;

public interface Mainservice {
	public List<DatasetDTO> mainlist(String keyword, int start, int end);
	public List<DatasetDTO> list();
	public int countArticle( String keyword);
	public void deldata(int delname);
	public int create(String name);
	public void update(String newname, int num);
	public List<FeatureDTO> featursearch(int num);	
	public FitBounds fitbounds(int data_id);
	
}
