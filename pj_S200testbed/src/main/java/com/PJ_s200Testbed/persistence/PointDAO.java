package com.PJ_s200Testbed.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.PJ_s200Testbed.domain.FeatureDTO;
import com.PJ_s200Testbed.domain.GetFeatureDTO;

public interface PointDAO {

	public List<GetFeatureDTO> pointsearch(@Param("map") Map<String, Object> map);
}
