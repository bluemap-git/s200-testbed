package com.PJ_s200Testbed.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.PJ_s200Testbed.domain.FeatureDTO;
import com.PJ_s200Testbed.model.Association;
import com.PJ_s200Testbed.model.Attribute;
import com.PJ_s200Testbed.model.DataSet;

public interface Featureservie {
	public int countArticle(int num, String keyword);
	public List<FeatureDTO> featureselect(int num, String keyword, int start, int end);
	public List<FeatureDTO> featuresearch(int num, int featureid);
	public List<FeatureDTO> featureDetail(int featureid);
	public void featureDelete(List<Integer> featureDelArray);
	public void DeatilUpdate( String value ,  int FeaturePk );
	public DataSet selectDataset(int featureID);
	public Attribute selectAttribute(int attributeID);
	public List<Integer> selectAssociation(int featureid);
	public String selectFeatureType(int featureid);
	public int selectFeatureHref(int featureid);
	public  ArrayList<List<Integer>>  childarray(List<Integer> featureDelArray);
}
