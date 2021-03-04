package com.PJ_s200Testbed.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import com.PJ_s200Testbed.domain.UploadData;

public interface Xmlservice {
	public int readFeatureCatalogue();
	public String readXml(String path);
	public boolean deleteNode(String path, ArrayList<List<Integer>> getArray);
	@Transactional
	public int readDataSet(int catalogue_fk, String tableName) ;
	public int getUploadDataIDX();
	public int insertUploadData( UploadData ud);
	public boolean UpdateNode(String path, String id, String key, String value);
	public boolean copyXML(String path, String copyPath);
}
