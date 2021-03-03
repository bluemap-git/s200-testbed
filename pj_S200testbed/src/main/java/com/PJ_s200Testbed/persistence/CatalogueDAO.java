package com.PJ_s200Testbed.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.PJ_s200Testbed.domain.AttributeBinding;
import com.PJ_s200Testbed.domain.CatalogueAttribute;
import com.PJ_s200Testbed.domain.ComplexAttribute;
import com.PJ_s200Testbed.domain.ComplexBinding;
import com.PJ_s200Testbed.domain.ComplexPermittedValue;
import com.PJ_s200Testbed.domain.FeatureAssociation;
import com.PJ_s200Testbed.domain.FeatureBinding;
import com.PJ_s200Testbed.domain.FeaturePermittedPrimitive;
import com.PJ_s200Testbed.domain.FeaturePermittedValue;
import com.PJ_s200Testbed.domain.FeatureType;
import com.PJ_s200Testbed.domain.InformationAssociation;
import com.PJ_s200Testbed.domain.InformationBinding;
import com.PJ_s200Testbed.domain.InformationPermittedValue;
import com.PJ_s200Testbed.domain.InformationType;
import com.PJ_s200Testbed.domain.ListedValue;
import com.PJ_s200Testbed.domain.Role;
import com.PJ_s200Testbed.domain.SimpleAttribute;
import com.PJ_s200Testbed.model.Association;
import com.PJ_s200Testbed.model.Attribute;
import com.PJ_s200Testbed.model.BoundedBy;
import com.PJ_s200Testbed.model.DatasetIdentificationInformation;
import com.PJ_s200Testbed.model.DatasetStructureInformation;
import com.PJ_s200Testbed.model.Feature;

public interface CatalogueDAO {

	public String SelectStr();

	public BoundedBy selectBoundedBy(String id);
	
	public DatasetIdentificationInformation selectDatasetIdentificationInformation(String id);
	
	public DatasetStructureInformation selectDatasetStructureInformation(String id);

	public List<Feature> selectFeature(String id);
	
	public Feature selectFeatureOne(String id);
	
	public List<Association> selectAssociation(String id);

	public List<Attribute> selectAttribute(String id);

	public String selectPoint(String id);

	public String selectPolygon(String id);
	
	public String selectDataSetName(String id);
	
	public int getCatalogueIDX();
	
	public int insertCatalogue(@Param("idx") int idx);
	
	public int getSimpleAttributeIDX();
	
	public int insertSimpleAttribute(@Param("sa") SimpleAttribute sa); 
	
	public int insertListedValue(@Param("lv") ListedValue lv);
	
	public int getComplexAttributeIDX();
	
	public int insertComplexAttribute(@Param("ca") ComplexAttribute ca) ;
	
	public int getComplexBindingIDX();
	
	public int insertComplexBinding(@Param("cb") ComplexBinding cb);
	
	public int insertComplexPermittedValue(@Param("cp") ComplexPermittedValue cp);
	
	public int insertRole(@Param("role") Role role);
	
	public int insertInformationAssociation(@Param("ia") InformationAssociation ia);
	
	public int insertFeatureAssociation(@Param("fa") FeatureAssociation fa);
	
	public int getInformationTypeIDX();
	
	public int insertInformationType(@Param("it") InformationType it);
	
	public int getInformationBindingIDX();
	
	public int insertInformationBinding(@Param("ib") InformationBinding ib) ;
	
	public int insertInformationPermittedValue(@Param("ipv") InformationPermittedValue ipv);
	
	public int getFeatureTypesIDX();
	
	public int insertFeatureType(@Param("fy") FeatureType fy);
	
	public int getAttributeBindingIDX();
	
	public int insertAttributeBinding(@Param("ab") AttributeBinding ab);
	
	public int insertFeaturePermittedValue(@Param("fpv") FeaturePermittedValue fpv) ;
	
	public int insertFeatureBinding(@Param("fb") FeatureBinding fb);
	
	public int insertFeaturePermittedPrimitive(@Param("fpp") FeaturePermittedPrimitive fpp);
	
	public int getCatalogueAttributeIDX();
	
	public int getCatalogueAttributeVersionCheck(@Param("fpp") String value);
	
	public int insertCatalogueAttribute(@Param("ca")CatalogueAttribute ca);
	
	public List<CatalogueAttribute> selectCatalogueAttribute(@Param("featureID") String featureID);
	
	public List<SimpleAttribute> selectSimpleAttribute(@Param("featureID") String featureID);
	
	public List<ListedValue> selectListedValue(@Param("simpleID") int simpleID);
	
	public List<ComplexAttribute> selectComplexAttribute(@Param("featureID") String featureID);
	
	public List<ComplexBinding> selectComplexBinding(@Param("complexID") int complexID);
	
	public List<ComplexPermittedValue> selectComplexPermittedValue(@Param("complexbindingID") int complexbindingID);
	
	public List<Role> selectRole(@Param("featureID") String featureID);
	
	public List<InformationAssociation> selectInformationAssociation(@Param("featureID") String featureID);
	
	public List<FeatureAssociation> selectFeatureAssociation(@Param("featureID") String featureID);
	
	public List<InformationType> selectInformationType(@Param("featureID") String featureID);
	
	public List<InformationBinding> selectInformationBinding(@Param("informationID") int informationID);
	
	public List<InformationPermittedValue> selectInformationPermittedValue(@Param("informationID") int informationID);
	
	public List<FeatureType> selectFeatureType(@Param("featureID") String featureID);
	
	public List<FeaturePermittedPrimitive> selectFeaturePermittedPrimitive(@Param("featureID") int featureID);
	
	public List<AttributeBinding> selectAttributeBinding(@Param("featureID") int featureID);
	
	public List<FeaturePermittedValue> selectFeaturePermittedValue(@Param("attributeBindingID") int attributeBindingID);
	
	public List<FeatureBinding> selectFeatureBinding(@Param("featureID") int featureID);
	
}
