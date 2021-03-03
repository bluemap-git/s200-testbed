package com.PJ_s200Testbed.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PJ_s200Testbed.domain.GetFeatureDTO;
import com.PJ_s200Testbed.persistence.DatasetDAO;
import com.PJ_s200Testbed.persistence.PointDAO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PointserviceImpl implements Pointservice{
	@Autowired // DB사용
	private SqlSession sqlSession;
	
	PointDAO pDao;
	
	@Autowired
	public void TestDAO() {
		pDao = sqlSession.getMapper(PointDAO.class);
	}

//	circle 
	@Override
	public List<GetFeatureDTO> search(String point, List<Integer> numarray) {
		String type = "circle";
		Map <String, Object> map = new HashMap<>();
		map.put("type", type);
		map.put("point", point);
		map.put("numarray", numarray);
		return pDao.pointsearch(map);
	}

//	line 
	@Override
	public List<GetFeatureDTO> line(String polylist, List<Integer> numarray) {
		String type = "line";
		Map <String, Object> map = new HashMap<>();
		map.put("type", type);
		map.put("polylist", polylist);
		map.put("numarray", numarray);
		return pDao.pointsearch(map);
	}
	@Override
	public List<GetFeatureDTO> rect(String rectlist, List<Integer> numarray) {
		String type = "rect";
		Map <String, Object> map = new HashMap<>();
		map.put("type", type);
		map.put("rectlist", rectlist);
		map.put("numarray", numarray);
		return pDao.pointsearch(map);
	}
}
