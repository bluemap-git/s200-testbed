package com.PJ_s200Testbed.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.PJ_s200Testbed.Function.Xml;
import com.PJ_s200Testbed.Function.ValidXML;
import com.PJ_s200Testbed.Util.Excel;
import com.PJ_s200Testbed.Util.UploadFileUtils;
import com.PJ_s200Testbed.domain.Association;
import com.PJ_s200Testbed.domain.Attribute;
import com.PJ_s200Testbed.domain.Feature;
import com.PJ_s200Testbed.domain.Geometry;
import com.PJ_s200Testbed.domain.UploadData;
import com.PJ_s200Testbed.model.DataSet;
import com.PJ_s200Testbed.model.ErrJson;
import com.PJ_s200Testbed.persistence.CatalogueDAO;
import com.PJ_s200Testbed.persistence.DatasetDAO;
import com.PJ_s200Testbed.service.Catalogueservice;
import com.PJ_s200Testbed.service.Xmlservice;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class Cataloguecontroller {

    @Autowired // DB사용
    private SqlSession sqlSession;

    @Autowired
    Catalogueservice cService;

    @Autowired
    Xmlservice xService;

    @ResponseBody
    @RequestMapping(value = "/dataset-zip")
    public String selectString(HttpServletRequest request, HttpServletResponse response) {
        String time = "" + System.currentTimeMillis();
        String rTime = time;
        String path = request.getSession().getServletContext().getRealPath("/") + "Download/" + time + "/";
        File folder = new File(path);
        if (!folder.exists())
            folder.mkdirs();
        List<String> sourceFiles = new ArrayList<String>();
        String[] arrayParam = request.getParameterValues("array[]");
        if (arrayParam == null)
            return "null";
        DatasetDAO sDao;
        sDao = sqlSession.getMapper(DatasetDAO.class);
        for (int i = 0; i < arrayParam.length; i++) {
            DataSet ds = sDao.selectDataset(arrayParam[i]);
            String dsName = ds.ds_idx + ". " + ds.id;
            String tempPath = request.getSession().getServletContext().getRealPath("/") + "rData/" + dsName + ".gml";
            File gmlFile = new File(tempPath);
            if (!gmlFile.exists())
                continue;
            if (arrayParam.length == 1) {
                dsName = ds.id;
                time = "@" + dsName;
            }
            String copyPath = path + dsName + ".gml";
            xService.copyXML(tempPath, copyPath);
            sourceFiles.add(copyPath);
        }
        String zipFile = path + time + ".zip";
        try {
            FileOutputStream fout = new FileOutputStream(zipFile);
            ZipOutputStream zout = new ZipOutputStream(fout);
            for (int i = 0; i < sourceFiles.size(); i++) {
                // 본래 파일명 유지, 경로제외 파일압축을 위해 new File로
                ZipEntry zipEntry = new ZipEntry(new File(sourceFiles.get(i)).getName());
                zout.putNextEntry(zipEntry);
                // 경로포함 압축
                // zout.putNextEntry(new ZipEntry(sourceFiles.get(i)));
                FileInputStream fin = new FileInputStream(sourceFiles.get(i));
                byte[] buffer = new byte[1024];
                int length;
                // input file을 1024바이트로 읽음, zip stream에 읽은 바이트를 씀
                while ((length = fin.read(buffer)) > 0) {
                    zout.write(buffer, 0, length);
                }
                zout.closeEntry();
                fin.close();
            }
            zout.close();
        } catch (IOException ioe) {
        	System.out.println(ioe.getMessage());
        }
        return rTime + "/" + time + ".zip";
    }

    @ResponseBody
    @RequestMapping(value = "/dataset-valid", produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrJson[] selectString2(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        DatasetDAO sDao;
        sDao = sqlSession.getMapper(DatasetDAO.class);
        DataSet ds = sDao.selectDataset(id);
        String dsName = ds.ds_idx + ". " + ds.id;
        String gmlPath = request.getSession().getServletContext().getRealPath("/") + "rData/" + dsName + ".gml";
        File gmlFile = new File(gmlPath);
        if (!gmlFile.exists())
            return null;
        String fcPath = request.getSession().getServletContext().getRealPath("/") + "Data/default.xml";

        // String fc = savePath + multi.getFilesystemName("fc");
        // String gml = savePath + multi.getFilesystemName("gml");
        String fc = fcPath;
        String gml = gmlPath;

        ValidXML xml = ValidXML.getInstance();
        xml.clearFcMap();

        boolean check = xml.readFeatureCatalogue(fc);
        xml.readData(gml);
        // request.setAttribute("map", xml.dataErr);
        // xml.clearFcMap();

        JsonArray ja = new JsonArray();
        
        for (String k : xml.dataErr.keySet()) {
                JsonObject jo = new JsonObject();
                jo.addProperty("id", k);
                JsonArray errs = new JsonArray();
                for (String err : xml.dataErr.get(k)) {
                    errs.add(err);
                }
                jo.add("err", errs);
                ja.add(jo);
        }

        Gson gson = new Gson();
        String jValue = ja.toString();//.getAsString();//gson.toJson(xml.dataErr);
        ErrJson[] ej = gson.fromJson(jValue, ErrJson[].class);
        return ej;

        // return null;
    }

    @Resource(name = "uploadPath")
    String uploadPath;

    // 업로드 디렉토리 sevlet-context.xml에 설정되어 있다
    // uploadPath 의존성 주입
    // @Resource는 이름으로 가지고 온다
    // String은 보편적으로 많이쓰기 때문에 이름으로 가져오도록 @Resource 사용 해야 한다
    // upload File 멀티파트 파일에 save
    @ResponseBody // 페이지 이동을 안할때, ajax를 쓸때 ResponseBodya
    @RequestMapping(value = "/filauploadSet", produces = "text/plain;charset=utf-8")
    public String uploadAjax(MultipartFile file, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	
    	
        String rPath = request.getSession().getServletContext().getRealPath("/");
        
        // 사용자가 입력한 파일명
        String originalName = file.getOriginalFilename();
        byte[] fileData = file.getBytes();
        UUID uid = UUID.randomUUID();
        String savedName =  originalName;	
        	 // uid.toString() + "_" + originalName; // 저장 이름
        
        int nameIndex = savedName.indexOf(".");
        
        String tableName= savedName.substring(0, nameIndex);
        
        String savedPath = UploadFileUtils.calcPath(uploadPath); // 저장 경로
        savedPath += "/" + uid.toString() + "/";
        savedPath = savedPath.replace(" ", "");
        File pathF = new File(savedPath);
        pathF.mkdirs();
        File target = new File(savedPath, savedName);
        FileCopyUtils.copy(fileData, target);
        String url = savedPath + savedName;
        String extension = Excel.getExtension(url).toLowerCase();
        if (extension.contains("xls")) {
            try {
                String path = Excel.ExcelToXML(savedPath, savedName);
                if (path == null)
                    return "fail";
                xService.readXml(rPath + "Data/default.xml");
                int f_idx = xService.readFeatureCatalogue();
                String dsCheck = xService.readXml(path);
                UploadData ud = new UploadData();
                ud.fileName = path;
                if (dsCheck.contains("DataSet")) {
                    int dsIdx = xService.readDataSet(f_idx, tableName);
                    ud.ds_idx = dsIdx;// update
                    ud.uploadData_pk = xService.getUploadDataIDX() + 1;
                    xService.insertUploadData(ud);
                    String time = "" + System.currentTimeMillis();
                    String tempPath = request.getSession().getServletContext().getRealPath("/") + "rData/";
                    File folder = new File(tempPath);
                    if (!folder.exists())
                        folder.mkdirs();
                    String gmlPath = cService.createEXCH(dsIdx + "", tempPath);
                }
            } catch (Exception e) {
                request.setAttribute("value", "Excel 에러가 발생하였습니다.");
                return "fail";
                // return "function/FileUpload.jsp";
            }
        } else if (extension.contains("gml")) {
            try {
                String path = savedPath + savedName;
                xService.readXml(rPath + "Data/default.xml");
                int f_idx = xService.readFeatureCatalogue();
                String dsCheck = xService.readXml(path);
                UploadData ud = new UploadData();
                ud.fileName = path;
                if (dsCheck.contains("DataSet")) {
                    int dsIdx = xService.readDataSet(f_idx, tableName);
                    ud.ds_idx = dsIdx;// update
                    ud.uploadData_pk = xService.getUploadDataIDX() + 1;
                    xService.insertUploadData(ud);
                    String time = "" + System.currentTimeMillis();
                    String tempPath = request.getSession().getServletContext().getRealPath("/") + "rData/";
                    File folder = new File(tempPath);
                    if (!folder.exists())
                        folder.mkdirs();
                    String gmlPath = cService.createEXCH(dsIdx + "", tempPath);
                }
            } catch (Exception e) {
                request.setAttribute("value", "GML 에러가 발생하였습니다.");
                return "fail";
                // return "function/FileUpload.jsp";
            }
        }
        String ok = "ok";
        return ok;
    }

    @ResponseBody
    @RequestMapping(value = "/create-feature")
    public boolean createFeature(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Integer> fList = new ArrayList<Integer>();
            String jsonStr = "";
            jsonStr = request.getParameter("json");
            if (jsonStr == null || jsonStr.equals(""))
                return false;
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(jsonStr);
            if (element == null)
                return false;
            DatasetDAO sDao;
            sDao = sqlSession.getMapper(DatasetDAO.class);
            JsonElement dID = element.getAsJsonObject().get("DataSetID");
            JsonElement aNum = element.getAsJsonObject().get("AtoNNumber");
            JsonArray jsonArray = element.getAsJsonObject().getAsJsonArray("member");
            // Buoy Beacon Landmark
            int pID = -1;
            boolean aton = false;
            for (JsonElement jsonElement : jsonArray) {
                int fID = -1;
                String aType = "";
                for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
                    System.out.println("Key = " + entry.getKey() + " Value = " + entry.getValue());
                    if (fID == -1) {
                        // Error
                        if (!entry.getKey().equals("Type"))
                            break;
                        fID = sDao.getFeatureIDX() + 1;
                        fList.add(fID);
                        if (entry.getValue().toString().toLowerCase().contains("buoy")
                                || entry.getValue().toString().toLowerCase().contains("beacon")
                                || entry.getValue().toString().toLowerCase().contains("landmark"))
                            pID = fID; // getIDX Value
                        aType = "S201:" + entry.getValue().toString().replaceAll("\"", "");
                        // CreateFeature
                        Feature feature = new Feature();
                        feature.ds_idx = Integer.parseInt(dID.getAsString());
                        feature.f_idx = fID;
                        feature.featuretype = aType;
                        sDao.insertFeature(feature);
                        aton = false;
                        continue;
                    }
                    if (!entry.getKey().toLowerCase().equals("atonmaintenance") && !aton) {
                        aton = true;
                        Attribute attr = new Attribute();
                        attr.f_idx = fID;
                        attr.a_idx = sDao.getAttributeIDX() + 1;
                        attr.name = "AtoNNumber";
                        attr.value = aNum.getAsString();
                        attr.parents = "null";
                        attr.attributetype = aType;
                        sDao.insertAttribute(attr);
                    }
                    if (entry.getKey().toLowerCase().contains("geometry")) {
                        String[] geomValue = entry.getValue().toString().replaceAll("\"", "").replaceAll(" ", "")
                                .split(",");
                        Geometry geom = new Geometry();
                        geom.idx = fID;
                        geom.srid = "4326";
                        geom.value = geomValue[0] + " " + geomValue[1];
                        geom.type = "POINT";
                        sDao.updateFeatureTypePoint(fID);
                        sDao.insertPoint(geom);
                        continue;
                    }
                    if (entry.getValue().isJsonArray()) {
                        JsonArray attrArray = entry.getValue().getAsJsonArray();
                        for (JsonElement jsonElement2 : attrArray) {
                            Attribute attr = new Attribute();
                            attr.f_idx = fID;
                            attr.a_idx = sDao.getAttributeIDX() + 1;
                            attr.name = entry.getKey().replaceAll("\"", "").trim();
                            attr.value = jsonElement2.getAsString().replace("'", "\'||CHR(39)||\'").replaceAll("\"", "")
                                    .trim();
                            attr.parents = "null";
                            attr.attributetype = aType;
                            sDao.insertAttribute(attr);
                        }
                    } else {
                        Attribute attr = new Attribute();
                        attr.f_idx = fID;
                        attr.a_idx = sDao.getAttributeIDX() + 1;
                        attr.name = entry.getKey().replaceAll("\"", "").trim();
                        attr.value = entry.getValue().toString().replace("'", "\'||CHR(39)||\'").replaceAll("\"", "")
                                .trim();
                        attr.parents = "null";
                        attr.attributetype = aType;
                        sDao.insertAttribute(attr);
                    }
                }
                // Create Association
                if (pID != fID) {
                    Association assci = new Association();
                    assci.f_idx = pID;
                    assci.xhref = "" + fID;
                    assci.xrole = "child";
                    sDao.insertAssociation(assci);
                    Association assci2 = new Association();
                    assci2.f_idx = fID;
                    assci2.xhref = "" + pID;
                    assci2.xrole = "parent";
                    sDao.insertAssociation(assci2);
                }
            }
            DataSet ds = sDao.selectDataset(dID.getAsString());
            String dsName = ds.ds_idx + ". " + ds.id;
            if (dsName != null && !dsName.equals("")) {
                String path = request.getSession().getServletContext().getRealPath("/") + "rData/" + dsName + ".gml";
                File gmlFile = new File(path);
                if (!gmlFile.exists())
                    return false;
                // add xml child
                CatalogueDAO cDao;
                cDao = sqlSession.getMapper(CatalogueDAO.class);
                Xml xml = new Xml(cDao);
                xml.CreateFeature(path, fList);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
