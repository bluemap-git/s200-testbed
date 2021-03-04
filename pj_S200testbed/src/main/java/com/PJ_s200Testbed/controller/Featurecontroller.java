package com.PJ_s200Testbed.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.PJ_s200Testbed.domain.FeatureDTO;
import com.PJ_s200Testbed.model.Attribute;
import com.PJ_s200Testbed.model.DataSet;
import com.PJ_s200Testbed.model.Detail;
import com.PJ_s200Testbed.service.Featureservie;
import com.PJ_s200Testbed.service.Xmlservice;
import com.PJ_s200Testbed.service.pager;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class Featurecontroller {

    @Autowired
    Featureservie fService;

    @Autowired
    Xmlservice xService;

    @GetMapping("/featureset")
    public String featurelist(Model model,
        @RequestParam(value = "num") int num,
        @RequestParam(value = "name") String name,
        @RequestParam(defaultValue = "1") int curPage,
        @RequestParam(defaultValue = "") String keyword,
        @RequestParam(defaultValue = "") int mtitle) {

        int count = fService.countArticle(num, keyword);
        // select xhref from asso... where xrole = 'parent'
        int href = fService.selectFeatureHref(mtitle);
        // idx is -1 true 진행 else featureid = idx;
        if (href > -1)
            mtitle = href;
        pager pager = new pager(count, curPage);
        int start = pager.getPageBegin();
        int end = pager.getPageEnd();
        List < FeatureDTO > list = fService.featureselect(num, keyword, start, end);
        log.info("list" + list);
        HashMap < String, Object > map = new HashMap<>();
        map.put("list", list);
        map.put("count", count);
        map.put("pager", pager);
        map.put("name", name);
        map.put("num", num);
        map.put("mtitle", mtitle);
        model.addAttribute("map", map);
        return "main/Featuredatalist";
    }

    @ResponseBody
    @RequestMapping(value = "/featuresearch")
    public List<FeatureDTO> featuresearch(
        @RequestParam(value = "featureid") int featureid,
        @RequestParam(value = "num") int num) {
        List < FeatureDTO > list = fService.featuresearch(num, featureid);
        return list;
    }

    @RequestMapping(value = "/featureDetail")
    public String featureDetail(@RequestParam(value = "featureid") int featureid, Model model) {
    	
    	
    	
        int href = fService.selectFeatureHref(featureid);
        if (href > -1)
            featureid = href;
        List < Integer > aList = new ArrayList<Integer>();
        List < Integer > tList = fService.selectAssociation(featureid);
        aList.add(featureid);
        for (Integer integer : tList) {
            if (integer != null)
                aList.add(integer);
        }
        // model select
        List < Detail > list = new ArrayList<Detail>();
        for (Integer integer : aList) {
            Detail d = new Detail();
            d.name = fService.selectFeatureType(integer);
            d.idx = integer + "";
            d.attributes = fService.featureDetail(integer);
            list.add(d);
        }
        model.addAttribute("list", list);
        return "main/FeatureDetail";
    }

    @ResponseBody
    @RequestMapping(value = "/featureDelete")
    public void featureDelete( HttpServletRequest request, @RequestParam(value = "featureDelArray[]") List<Integer> featureDelArray) {
    	
	    HashSet < Integer > tArr = new HashSet<Integer>(featureDelArray);
	    featureDelArray = new ArrayList<Integer>(tArr);
	    // [1,2,3] [4,5,6] 
	    ArrayList<List<Integer>> delarray = fService.childarray(featureDelArray);
	    

		DataSet ds = fService.selectDataset(featureDelArray.get(0));
		String dsName = ds.ds_idx + ". " + ds.id;
		if (dsName != null && !dsName.equals("")) {
			String path = request.getSession().getServletContext().getRealPath("/") + "rData/" + dsName + ".gml";
			File gmlFile = new File(path);
			if (!gmlFile.exists())
				return;
			xService.deleteNode(path, delarray);
		}
	    
	 
		fService.featureDelete(delarray);
}

    @ResponseBody
    @RequestMapping(value = "/DeatilUpdate")
    public String DeatilUpdate( HttpServletRequest request, 
                				@RequestParam(value = "newFeaturvalue") String value,
                				@RequestParam(value = "FeaturePkid") int FeaturePk) {
	    // select Attribute
	    Attribute attr = fService.selectAttribute(FeaturePk);
	    // select datasetID
	    DataSet ds = fService.selectDataset(attr.f_idx);
	    String dsName = ds.ds_idx + ". " + ds.id;
	    // file Check
	    String path = request.getSession().getServletContext().getRealPath("/") + "rData/" + dsName + ".gml";
	    File gmlFile = new File(path);
	    if (!gmlFile.exists())
	        return "";
	    xService.UpdateNode(path, attr.f_idx + "", attr.name, value);
	    // update xml
	    fService.DeatilUpdate(value, FeaturePk);
	    String ok = "ok";
	    return ok;
    }
}
