package com.PJ_s200Testbed.service;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PJ_s200Testbed.Function.Xml;
import com.PJ_s200Testbed.persistence.CatalogueDAO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CatalogueserviceImpl implements Catalogueservice {

	@Autowired // DB사용
	private SqlSession sqlSession;

	CatalogueDAO cDao;

	@Autowired
	public void TestDAO() {
		cDao = sqlSession.getMapper(CatalogueDAO.class);
	}

	@Override
	public int getInt() {
		return 1;
	}

	@Override
	public String getString() {
		String str = cDao.SelectStr();
		return str;
	}

	@Override
	public String createEXCH(String id, String path) {
		String name ="";
		try {
			name = id+". "+cDao.selectDataSetName(id);
			Xml xml = new Xml(cDao);

			xml.writeDataSet(id, path, name);
		} catch (Exception e) {
			return "";
		}
		return path+name+".gml";
	}
}
