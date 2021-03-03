package com.PJ_s200Testbed.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.PJ_s200Testbed.domain.FeatureDTO;
import com.PJ_s200Testbed.model.Attribute;
import com.PJ_s200Testbed.model.DataSet;

public interface FeatureDAO {

	int[][] selectchild = null;


	public int countArticle(@Param("map") Map<String, Object> map);
	
	
	public List<FeatureDTO> featureselect(@Param("map") Map<String, Object> map);
	
	public List<FeatureDTO> featuresearch(@Param("map") Map<String, Object> map);
	
	
	public List<FeatureDTO> featureDetail(@Param("featureid")int featureid);
	
	public void featureDelete(@Param("featureDelArray") List<Integer> featureDelArray);
	
	public void DeatilUpdate(@Param("map") Map<String, Object> map);
	
	public DataSet selectDataset(@Param("featureid")int featureid);
	
	public Attribute selectAttribute(@Param("attributeid")int attributeID);
	

	public List<Integer> selectAssociation(@Param("featureid")int featureid);
	
	public String selectFeatureType(@Param("featureid")int featureid);
	

	public int selectFeatureHref(@Param("featureid")int featureid);


	public List<Integer> selectchild(@Param("test") int test);
}
