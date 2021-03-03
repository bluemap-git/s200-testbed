package com.PJ_s200Testbed.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import com.PJ_s200Testbed.domain.Association;
import com.PJ_s200Testbed.domain.Attribute;
import com.PJ_s200Testbed.domain.Boundary;
import com.PJ_s200Testbed.domain.BoundedBy;
import com.PJ_s200Testbed.domain.DataSet;
import com.PJ_s200Testbed.domain.DatasetDTO;
import com.PJ_s200Testbed.domain.DatasetIdentificationInformation;
import com.PJ_s200Testbed.domain.DatasetStructureInformation;
import com.PJ_s200Testbed.domain.Feature;
import com.PJ_s200Testbed.domain.FeatureDTO;
import com.PJ_s200Testbed.domain.FitBounds;
import com.PJ_s200Testbed.domain.Geometry;
import com.PJ_s200Testbed.domain.SaveOption;
import com.PJ_s200Testbed.domain.UploadData;



public interface DatasetDAO {

	public List<DatasetDTO> dataselect(@Param("map") Map<String, Object> map);

	// 게시글 갯수 계산
	public int countArticle(@Param("map") Map<String, String> map);

	public List<DatasetDTO> list();

	public void deldata(@Param("value") int delname);

	public int lastrownum();

	public void createdate(@Param("rownum") int rownum, @Param("name") String name);

	public void update(@Param("newname") String newname, @Param("num") int num);

	public List<FeatureDTO> featursearch(@Param("num") int num);

	public int getDataSetIDX(); 
	

	public int insertDataSet(@Param("DataSet" ) DataSet ds);
	
	
	public int insertBoundedBy(@Param("BoundedBy" ) BoundedBy bb) ;
	
	
	public int insertDatasetIdentificationInformation(@Param("DatasetIdentificationInformation" ) DatasetIdentificationInformation di);
	
	public int insertDatasetStructureInformation(@Param("DatasetStructureInformation") DatasetStructureInformation ds);
	
	public int getFeatureIDX();
	
	
	public int insertFeature(@Param("feat") Feature feat);
	
	public int updateFeatureTypeImember(@Param("idx") int idx);
	
	public int insertAssociation(@Param("ass")Association ass);
	
	
	public int updateFeatureTypePoint(@Param("idx") int idx);
	
	public int insertPoint(@Param("geom") Geometry geom);
	
	public int updateFeatureTypePolygon(@Param("idx") int idx);
	
	public int insertPolygon(@Param("geom") Geometry geom);
	
	public int getAttributeIDX();
	
	public int insertAttribute(@Param("attr") Attribute attr);
	
	public String getC_idx(@Param("ds_idx") String ds_idx);
	
	public String getUploadFileName(@Param("ds_idx") String ds_idx);  
	
	public String getUploadFilePath(@Param("ds_idx") String ds_idx); 
	
	
	public BoundedBy selectBoundedBy(@Param("ds_idx") String ds_idx);
	
	public DatasetIdentificationInformation selectDatasetIdentificationInformation(@Param("ds_idx") String ds_idx);
	
	public DatasetStructureInformation selectDatasetStructureInformation(@Param("ds_idx") String ds_idx);
	
	public List<Feature> selectFeature(@Param("ds_idx") String ds_idx);
	
	public List<Association> selectAssociation(@Param("f_idx") String f_idx);
	
	public List<Attribute> selectAttribute(@Param("f_idx") String f_idx);
	
	public String selectPoint(@Param("f_idx") String f_idx);
	
	public String selectPolygon(@Param("f_idx") String f_idx);
	
	public int getSaveOptionIDX();
	
	public SaveOption getSelectSaveOption(@Param("so_idx") int so_idx);
	
	public String getSelectDatasetName(@Param("ds_idx") int ds_idx);
	
	public String getSelectSaveFileName(@Param("ds_idx") int ds_idx);
	
	public int getUploadDataIDX();
	
	public int insertUploadData(@Param("ud") UploadData ud) ;
	
	public com.PJ_s200Testbed.model.DataSet selectDataset(@Param("ds_idx") String ds_idx);

	

	public FitBounds fitbounds(@Param("ds_idx") int data_id);
	
}
