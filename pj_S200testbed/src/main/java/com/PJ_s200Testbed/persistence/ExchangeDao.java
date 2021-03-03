package com.PJ_s200Testbed.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.PJ_s200Testbed.domain.Boundary;
import com.PJ_s200Testbed.domain.ComplexA;
import com.PJ_s200Testbed.domain.ComplexChild;
import com.PJ_s200Testbed.domain.ComplexChildAttribute;
import com.PJ_s200Testbed.domain.ComplexGeometry;
import com.PJ_s200Testbed.domain.ExchangeCatalogue;
import com.PJ_s200Testbed.domain.ExchangeCatalogueAttribute;
import com.PJ_s200Testbed.domain.SimpleA;

public interface ExchangeDao {

	public int getExchangeIDX();
	
	public int insertExchangeCatalogue(@Param("ec") ExchangeCatalogue ec);
	
	public int insertExchangeCatalogueAttribute(@Param("eca") ExchangeCatalogueAttribute eca);
	
	public int insertSimpleA(@Param("simple") SimpleA simple);
	
	public int getComplexaIDX();
	
	public int insertComplexA(@Param("Complex") ComplexA Complex);
	
	public int insertComplexGeometry(@Param("cg") ComplexGeometry cg);
	
	public int getComplexChildIDX();
	
	public int insertComplexChild1(@Param("cc") ComplexChild cc);
	
	public int insertComplexChild2(@Param("cc") ComplexChild cc);
	
	public int insertComplexChildAttribute(@Param("cca") ComplexChildAttribute cca);
	
	public String getExchangeCatalogueTitle(@Param("ec_pk") int ec_pk);
	
	public List<ExchangeCatalogueAttribute> getExchangeCatalogueAttribute(@Param("ec_fk") int ec_fk);
	
	public List<SimpleA> getSimpleA(@Param("ec_fk") int ec_fk);
	
	public List<ComplexA> getComplexA(@Param("ec_fk") int ec_fk);
	
	public List<ComplexChild> getComplexChildRoot(@Param("c_fk") int c_fk);
	
	public List<ComplexChildAttribute> getComplexChildAttribute(@Param("cc_fk") int cc_fk);
	
	public Boundary getBoundary(@Param("cc_fk") int cc_fk);
	
	public List<ComplexChild> getComplexChild(@Param("c_fk") int c_fk);
	
	
}
