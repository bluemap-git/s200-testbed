package com.PJ_s200Testbed.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.PJ_s200Testbed.domain.DatasetDTO;
import com.PJ_s200Testbed.domain.FeatureDTO;
import com.PJ_s200Testbed.domain.FitBounds;
import com.PJ_s200Testbed.persistence.DatasetDAO;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class MainserviceImpl implements Mainservice {
	
	@Autowired // DB사용
	private SqlSession sqlSession;
	
	DatasetDAO sDao;
	
	@Autowired
	public void TestDAO() {
		sDao = sqlSession.getMapper(DatasetDAO.class);
	}
	@Override
	public List<DatasetDTO> mainlist(String keyword, int start, int end) {
		Map<String, Object> map = new HashMap<>();
		map.put("start", start);
		map.put("end", end);
		map.put("keyword", "%"+keyword+"%");
		return sDao.dataselect(map);
	}
	@Override
	public int countArticle(String keyword) {
		Map<String, String> map = new HashMap<>();
		map.put("keyword", "%"+keyword+"%");
		return sDao.countArticle(map);
	}
	@Override
	public List<DatasetDTO> list() {
		return sDao.list();
	}
	@Override
	public void deldata(int delname) {
		sDao.deldata(delname);
	}
	@Override
	public int create(String name) {
		int lastrow = sDao.lastrownum()+1;
		sDao.createdate(lastrow, name);
		return lastrow;
	}
	@Override
	public void update(String newname, int num) {
		sDao.update(newname, num);
	}
	@Override
	public List<FeatureDTO> featursearch(int num) {
		return sDao.featursearch(num);
	}
	
	
	@Override
	public FitBounds fitbounds(int data_id) {
		return sDao.fitbounds(data_id);
	}
	
}
