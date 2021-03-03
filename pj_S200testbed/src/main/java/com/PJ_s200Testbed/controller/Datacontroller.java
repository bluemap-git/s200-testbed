package com.PJ_s200Testbed.controller;

import java.io.Console;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.PJ_s200Testbed.domain.DatasetDTO;
import com.PJ_s200Testbed.domain.FeatureDTO;
import com.PJ_s200Testbed.domain.FitBounds;
import com.PJ_s200Testbed.model.DataSet;
import com.PJ_s200Testbed.persistence.DatasetDAO;
import com.PJ_s200Testbed.service.Catalogueservice;
import com.PJ_s200Testbed.service.Mainservice;
import com.PJ_s200Testbed.service.pager;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class Datacontroller {

    @Autowired
    Mainservice mService;

    @Autowired
    Catalogueservice cService;

    @Autowired // DB사용
    private SqlSession sqlSession;


    @GetMapping("/dataset")
    public String dataselect(@RequestParam(defaultValue = "1") int curPage,
            @RequestParam(defaultValue = "all") String search_option, @RequestParam(defaultValue = "") String keyword,
            Model model) {

        int count = mService.countArticle(keyword);
        pager pager = new pager(count, curPage);
        int start = pager.getPageBegin();
        int end = pager.getPageEnd();
        List<DatasetDTO> list = mService.mainlist(keyword, start, end); // 게시물 목록
        HashMap<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("count", count);
        map.put("pager", pager);
        map.put("search_option", search_option);
        map.put("keyword", keyword);
       model.addAttribute("map", map);
      
              
       return "main/Dataset";
      
    }
    
    
    //return data를 json 형식으로
    @ResponseBody
    @GetMapping("/dataset_json")
    public HashMap<String, Object> dataset_json(@RequestParam(defaultValue = "1") int curPage,
            @RequestParam(defaultValue = "all") String search_option, @RequestParam(defaultValue = "") String keyword,
            Model model) {

        int count = mService.countArticle(keyword);
        pager pager = new pager(count, curPage);
        int start = pager.getPageBegin();
        int end = pager.getPageEnd();
        List<DatasetDTO> list = mService.mainlist(keyword, start, end); // 게시물 목록
        HashMap<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("count", count);
        map.put("pager", pager);
        map.put("search_option", search_option);
        map.put("keyword", keyword);
       model.addAttribute("map", map);
      
              
       return map;
      
    }
    

    @RequestMapping(value = "/deldate")
    public String deldate(HttpServletRequest request, @RequestParam(value = "checkarr[]") List<Integer> deleteList)
            throws Exception {
    	
    	log.info("진행 ");
    	
        DatasetDAO sDao;
        sDao = sqlSession.getMapper(DatasetDAO.class);
        for (int delname : deleteList) {
            DataSet ds = sDao.selectDataset(delname + "");
            String dsName = ds.ds_idx + ". " + ds.id;
            mService.deldata(delname);
            // file Check
            String gmlPath = request.getSession().getServletContext().getRealPath("/") + "rData/" + dsName + ".gml";
            File gmlFile = new File(gmlPath);
            if (!gmlFile.exists())
                continue;
            gmlFile.delete();
        }
        return "main/Dataset";
    };

    @RequestMapping(value = "/create")
    public String create(HttpServletRequest request, @RequestParam(value = "create") String name) {
        int idx = mService.create(name);
        String tempPath = request.getSession().getServletContext().getRealPath("/") + "rData/";
        File folder = new File(tempPath);
        if (!folder.exists())
            folder.mkdirs();
        String gmlPath = cService.createEXCH(idx + "", tempPath);
        return "main/Dataset";
    }

    @RequestMapping(value = "/update")
    public String update(HttpServletRequest request, @RequestParam(value = "newname") String newname,
            @RequestParam(value = "num") int num) {
        // rename
        DatasetDAO sDao;
        sDao = sqlSession.getMapper(DatasetDAO.class);
        // select datasetID
        DataSet ds = sDao.selectDataset(num + "");
        String dsName = ds.ds_idx + ". " + ds.id;
        // file Check
        String gmlPath = request.getSession().getServletContext().getRealPath("/") + "rData/" + dsName + ".gml";
        File gmlFile = new File(gmlPath);
        if (gmlFile.exists()) {
            gmlFile.renameTo(new File(request.getSession().getServletContext().getRealPath("/") + "rData/" + num + ". "
                    + newname + ".gml"));
        }
        mService.update(newname, num);
        return "main/Dataset";
    }

    @ResponseBody
    @RequestMapping(value = "/search")
    public HashMap<String, Object> search(@RequestParam(value = "num") int num, Model model) {
        List<FeatureDTO> list = mService.featursearch(num);
        HashMap<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("num", num);
        return map;
    }
    
    @ResponseBody
    @GetMapping(value = "/dataset/bound")
    public FitBounds fitboudns(HttpServletRequest request){
    	
    	int data_id = Integer.parseInt(request.getParameter("id"));
    	FitBounds fbound = new FitBounds();    	
    	fbound = mService.fitbounds(data_id);
    	return fbound;
       
    }
 }

	