package com.PJ_s200Testbed.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.PJ_s200Testbed.domain.Association;
import com.PJ_s200Testbed.domain.Attribute;
import com.PJ_s200Testbed.domain.AttributeBinding;
import com.PJ_s200Testbed.domain.Boundary;
import com.PJ_s200Testbed.domain.BoundedBy;
import com.PJ_s200Testbed.domain.CatalogueAttribute;
import com.PJ_s200Testbed.domain.ComplexA;
import com.PJ_s200Testbed.domain.ComplexAttribute;
import com.PJ_s200Testbed.domain.ComplexBinding;
import com.PJ_s200Testbed.domain.ComplexChild;
import com.PJ_s200Testbed.domain.ComplexChildAttribute;
import com.PJ_s200Testbed.domain.ComplexGeometry;
import com.PJ_s200Testbed.domain.ComplexPermittedValue;
import com.PJ_s200Testbed.domain.DataSet;
import com.PJ_s200Testbed.domain.DatasetIdentificationInformation;
import com.PJ_s200Testbed.domain.DatasetStructureInformation;
import com.PJ_s200Testbed.domain.ExchangeCatalogue;
import com.PJ_s200Testbed.domain.ExchangeCatalogueAttribute;
import com.PJ_s200Testbed.domain.Feature;
import com.PJ_s200Testbed.domain.FeatureAssociation;
import com.PJ_s200Testbed.domain.FeatureBinding;
import com.PJ_s200Testbed.domain.FeaturePermittedPrimitive;
import com.PJ_s200Testbed.domain.FeaturePermittedValue;
import com.PJ_s200Testbed.domain.FeatureType;
import com.PJ_s200Testbed.domain.Geometry;
import com.PJ_s200Testbed.domain.InformationAssociation;
import com.PJ_s200Testbed.domain.InformationBinding;
import com.PJ_s200Testbed.domain.InformationPermittedValue;
import com.PJ_s200Testbed.domain.InformationType;
import com.PJ_s200Testbed.domain.ListedValue;
import com.PJ_s200Testbed.domain.Role;
import com.PJ_s200Testbed.domain.SaveOption;
import com.PJ_s200Testbed.domain.SimpleA;
import com.PJ_s200Testbed.domain.SimpleAttribute;
import com.PJ_s200Testbed.persistence.CatalogueDAO;
import com.PJ_s200Testbed.persistence.DatasetDAO;
import com.PJ_s200Testbed.persistence.ExchangeDao;

import lombok.extern.slf4j.Slf4j;

@Slf4j

public class Xml {
	private Document xmlDoc;

	private String imgPath;
	private String saveImgPath;

	@Autowired // DB사용
	private SqlSession sqlSession;

	@Autowired
	private void init() {

		sDao = sqlSession.getMapper(DatasetDAO.class);
		cDao = sqlSession.getMapper(CatalogueDAO.class);
		eDao = sqlSession.getMapper(ExchangeDao.class);

	}

	DatasetDAO sDao;
	CatalogueDAO cDao;
	ExchangeDao eDao;

	//
	private void CreateImgPath(String _imgPath1, String _imgPath2, String _saveImgPath) {
		String[] temp = _imgPath1.split("/");
		String[] temp2 = _imgPath2.split("/");
		imgPath = _imgPath1.substring(0, _imgPath1.length() - temp[temp.length - 1].length()) + temp2[temp2.length - 1]
				+ "/";
		saveImgPath = _saveImgPath + "img";

		File folder = new File(saveImgPath);

		if (!folder.mkdirs()) {
			return;
		}
	}

	private void CopyImg(String fileName) {
		try {
			FileInputStream fis = new FileInputStream(imgPath + fileName);
			FileOutputStream fos = new FileOutputStream(saveImgPath + "/" + fileName);

			int data = 0;
			while ((data = fis.read()) != -1) {
				fos.write(data);
			}
			fis.close();
			fos.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//
	public String readXml(String path) {
		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();

			DocumentBuilder parser = f.newDocumentBuilder();

			xmlDoc = parser.parse(path);

			Element root = xmlDoc.getDocumentElement();

			return root.getTagName();

		} catch (Exception e) {

			return null;

		}
	}

	// Read _ DataSet
	public int readDataSet(int catalogue_fk) {

		Element eleDS = xmlDoc.getDocumentElement();

		DataSet dataSet = new DataSet();
		dataSet.catalogue_fk = catalogue_fk;
		dataSet.ds_idx = sDao.getDataSetIDX() + 1;

		dataSet.id = eleDS.getAttribute("gml:id");

		int queryCheck = sDao.insertDataSet(dataSet);

		if (queryCheck < 0)
			return -1;

		Map<Integer, Integer> idxList = new HashMap<Integer, Integer>();
		Map<String, String> emp = new HashMap<String, String>();

		boolean check = false;

		int length = eleDS.getChildNodes().getLength() - 1;
		for (int i = 0; i < eleDS.getChildNodes().getLength(); i++) {

			/*
			 * if (eleDS.getChildNodes().item(i).getNodeType() != 1) continue;
			 */

			// Element bookEle = (Element)bookNode;
			// Element eleChild = (Element) eleDS.getChildNodes().item(i);

			Node node = eleDS.getChildNodes().item(i);
			String str = node.getNodeName();
			if (str == "#text") {
				if (i == length && !check) {
					i = 0;
					check = true;
				}
				continue;
			}
			if (str.contains("boundedBy") && !check) {
				readBoundedBy(node, dataSet.ds_idx);
			} else if (str.contains("DatasetIdentificationInformation") && !check) {
				readDatasetIdentificationInformation(node, dataSet.ds_idx);
			} else if (str.contains("DatasetStructureInformation") && !check) {
				readDatasetStructureInformation(node, dataSet.ds_idx);
			} else if (str.contains("member") || str.contains("imember")) {
				readFeature(node, dataSet.ds_idx, check, idxList, emp, i);
			}

			if (i == length && !check) {
				i = 0;
				check = true;
			}
		}

		return dataSet.ds_idx;
	}

	private void readBoundedBy(Node node, int ds_idx) {
		Node _node;

		if (node.getChildNodes().item(0).getNodeName().contains("#"))
			_node = node.getChildNodes().item(1);
		else
			_node = node.getChildNodes().item(0);

		BoundedBy bb = new BoundedBy();
		bb.ds_idx = ds_idx;
		bb.srsname = _node.getAttributes().getNamedItem("srsName").getNodeValue();

		for (int i = 0; i < _node.getChildNodes().getLength(); i++) {
			Node childNode = _node.getChildNodes().item(i);

			if (childNode.getNodeName().equals("gml:lowerCorner"))
				bb.lowercorner = childNode.getFirstChild().getNodeValue();
			else if (childNode.getNodeName().equals("gml:upperCorner"))
				bb.uppercorner = childNode.getFirstChild().getNodeValue();
		}

		sDao.insertBoundedBy(bb);
	}

	private void readDatasetIdentificationInformation(Node node, int ds_idx) {
		DatasetIdentificationInformation di = new DatasetIdentificationInformation();
		di.ds_idx = ds_idx;

		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			if (node.getChildNodes().item(i).getNodeName().equals("S100:encodingSpecification"))
				di.encodingSpecification = node.getChildNodes().item(i).getTextContent();
			else if (node.getChildNodes().item(i).getNodeName().equals("S100:encodingSpecificationEdition"))
				di.encodingSpecificationEdition = node.getChildNodes().item(i).getTextContent();
			else if (node.getChildNodes().item(i).getNodeName().equals("S100:productIdentifier"))
				di.productIdentifier = node.getChildNodes().item(i).getTextContent();
			else if (node.getChildNodes().item(i).getNodeName().equals("S100:productEdition"))
				di.productEdition = node.getChildNodes().item(i).getTextContent();
			else if (node.getChildNodes().item(i).getNodeName().equals("S100:applicationProfile"))
				di.applicationProfile = node.getChildNodes().item(i).getTextContent();
			else if (node.getChildNodes().item(i).getNodeName().equals("S100:datasetFileIdentifier"))
				di.datasetFileIdentifier = node.getChildNodes().item(i).getTextContent();
			else if (node.getChildNodes().item(i).getNodeName().equals("S100:datasetTitle"))
				di.datasetTitle = node.getChildNodes().item(i).getTextContent();
			else if (node.getChildNodes().item(i).getNodeName().equals("S100:datasetReferenceDate"))
				di.datasetReferenceDate = node.getChildNodes().item(i).getTextContent();
			else if (node.getChildNodes().item(i).getNodeName().equals("S100:datasetLanguage"))
				di.datasetLanguage = node.getChildNodes().item(i).getTextContent();
			else if (node.getChildNodes().item(i).getNodeName().equals("S100:datasetTopicCategory"))
				di.datasetTopicCategory = node.getChildNodes().item(i).getTextContent();
		}

		sDao.insertDatasetIdentificationInformation(di);
	}

	private void readDatasetStructureInformation(Node node, int ds_idx) {
		DatasetStructureInformation ds = new DatasetStructureInformation();
		ds.ds_idx = ds_idx;

		ds.datasetCoordOriginX = "null";
		ds.datasetCoordOriginY = "null";
		ds.datasetCoordOriginZ = "null";
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			if (node.getChildNodes().item(i).getNodeName().equals("S100:datasetCoordOriginX"))
				ds.datasetCoordOriginX = node.getChildNodes().item(i).getTextContent();
			else if (node.getChildNodes().item(i).getNodeName().equals("S100:datasetCoordOriginY"))
				ds.datasetCoordOriginY = node.getChildNodes().item(i).getTextContent();
			else if (node.getChildNodes().item(i).getNodeName().equals("S100:datasetCoordOriginZ"))
				ds.datasetCoordOriginZ = node.getChildNodes().item(i).getTextContent();
			else if (node.getChildNodes().item(i).getNodeName().equals("S100:coordMultFactorX"))
				ds.coordMultFactorX = node.getChildNodes().item(i).getTextContent();
			else if (node.getChildNodes().item(i).getNodeName().equals("S100:coordMultFactorY"))
				ds.coordMultFactorY = node.getChildNodes().item(i).getTextContent();
			else if (node.getChildNodes().item(i).getNodeName().equals("S100:coordMultFactorZ"))
				ds.coordMultFactorZ = node.getChildNodes().item(i).getTextContent();
		}

		sDao.insertDatasetStructureInformation(ds);
	}

	private void readFeature(Node node, int ds_idx, Boolean check, Map<Integer, Integer> idxList,
			Map<String, String> emp, int i) {
		// �ּ��� ������ ù��° �ڽĳ�尡 �ּ��� �Ǳ� ������ �������� �Ѱ��ش�.
		int startIdx = 0;
		int f_idx = 0;

		if (node.getChildNodes().item(startIdx).getNodeType() != 1)
			startIdx++;

		if (node.getChildNodes().item(startIdx).getNodeName().contains("#comment"))
			startIdx++;

		if (node.getChildNodes().item(startIdx).getNodeType() != 1)
			startIdx++;

		// feature Insert
		if (!check) {
			f_idx = sDao.getFeatureIDX() + 1;
			idxList.put(i, f_idx);
			Feature feature = new Feature();
			feature.ds_idx = ds_idx;
			feature.f_idx = f_idx;
			feature.featuretype = node.getChildNodes().item(startIdx).getNodeName();
			sDao.insertFeature(feature);

			// feature�� ID���� f_idx�� ����
			emp.put(node.getChildNodes().item(startIdx).getAttributes().getNamedItem("gml:id") != null
					? node.getChildNodes().item(startIdx).getAttributes().getNamedItem("gml:id").getNodeValue()
					: null, String.valueOf(f_idx));
		}

		// imember�� geometry�� imember�� �ִ´�
		if (node.getNodeName().contains("imember") & !check)
			sDao.updateFeatureTypeImember(f_idx);

		// feature Child
		for (int j = 0; j < (node.getChildNodes().item(startIdx)).getChildNodes().getLength(); j++) {
			// Element subElement =
			// (Element)((Element)ele.getChildNodes().item(startIdx)).getChildNodes().item(j);
			Node _node = node.getChildNodes().item(startIdx).getChildNodes().item(j);

			if (_node.getNodeName().contains("Association") & check) {
				Association assci = new Association();

				assci.f_idx = idxList.get(i);
				assci.xid = _node.getAttributes().getNamedItem("gml:id") != null
						? _node.getAttributes().getNamedItem("gml:id").getNodeValue()
						: "null";
				assci.xhref = _node.getAttributes().getNamedItem("xlink:href") != null
						? emp.get(_node.getAttributes().getNamedItem("xlink:href").getNodeValue().replace("#", ""))
						: "null";
				assci.xrole = _node.getAttributes().getNamedItem("xlink:role") != null
						? _node.getAttributes().getNamedItem("xlink:role").getNodeValue()
						: "null";

				sDao.insertAssociation(assci);
			} else if ((_node.getNodeName().contains("parent") || _node.getNodeName().contains("child")) & check) {
				Association assci = new Association();

				assci.f_idx = idxList.get(i);
				assci.xid = _node.getAttributes().getNamedItem("xlink:title") != null
						? _node.getAttributes().getNamedItem("xlink:title").getNodeValue()
						: "null";
				assci.xhref = _node.getAttributes().getNamedItem("xlink:href") != null
						? emp.get(_node.getAttributes().getNamedItem("xlink:href").getNodeValue().replace("#", ""))
						: "null";
				assci.xrole = _node.getAttributes().getNamedItem("xlink:role") != null
						? _node.getAttributes().getNamedItem("xlink:role").getNodeValue()
						: "null";

				sDao.insertAssociation(assci);
			}

			else if (_node.getNodeName().contains("geometry") & !check) {
				String[] token = geomLoop(_node.getLastChild().getParentNode());

				if (token != null && token.length > 1) {
					String value = "";
					String startValue = "";
					String endValue = "";

					int k = 0;

					while (true) {
						if (k >= token.length - 1) {
							if (!startValue.contains(endValue))
								value += ", " + startValue;
							break;
						}

						if (token[k].equals("") || token[k].equals(" ")) {
							k++;
							continue;
						}

						if (value.equals("")) {
							value += token[k++] + " " + token[k++];
							startValue = token[k - 2] + " " + token[k - 1];
						} else {
							value += ", " + token[k++] + " " + token[k++];
							endValue = token[k - 2] + " " + token[k - 1];
						}

					}

					if (value.equals(startValue)) {

						Geometry geom = new Geometry();
						geom.idx = f_idx;
						geom.srid = "4326";
						geom.value = value;
						geom.type = "POINT";

						sDao.updateFeatureTypePoint(f_idx);

						sDao.insertPoint(geom);
					} else {
						Geometry geom = new Geometry();
						geom.idx = f_idx;
						geom.srid = "4326";
						geom.value = value;
						geom.type = "POLYGON";

						sDao.updateFeatureTypePolygon(f_idx);

						sDao.insertPolygon(geom);
					}
					/*
					 * } else if (_node.getNodeName().contains("Polyline")) {
					 * //db.updateFeature(f_idx, "Polyline");
					 * 
					 * //db.insertGeom("POINT", "26910", value, f_idx); //sql = string.
					 * Format("INSERT INTO  polyline (f_idx, geom) VALUES ({0}, ST_GeomFromText('LINESTRING({1})', 4269))"
					 * , f_idx, value); //startSQL(sql); }
					 */
				}
				// continue;
			}

			else if (!check) {
				int a_idx = sDao.getAttributeIDX() + 1;
				for (int k = 0; k < _node.getChildNodes().getLength(); k++) {
					if (k == 0) {
						Attribute attr = new Attribute();
						attr.f_idx = f_idx;
						attr.a_idx = a_idx;
						attr.name = _node.getNodeName();
						if (_node.getChildNodes().getLength() == 1)
							attr.value = _node.getFirstChild().getNodeValue().replace("'", "\'||CHR(39)||\'");
						else if (_node.getFirstChild().getNodeType() != 1)
							attr.value = _node.getChildNodes().item(1).getFirstChild() != null
									? _node.getChildNodes().item(1).getFirstChild().getNodeValue().replace("'",
											"\'||CHR(39)||\'")
									: _node.getFirstChild().getNodeValue().replace("'", "\'||CHR(39)||\'");
						else
							attr.value = _node.getFirstChild().getFirstChild().getNodeValue().replace("'",
									"\'||CHR(39)||\'");
						attr.parents = "null";
						attr.attributetype = _node.getParentNode().getNodeName();

						sDao.insertAttribute(attr);
					}
					attributeLoop(f_idx, _node.getChildNodes().item(k), a_idx);
				}
			}

		}
	}

	private String[] geomLoop(Node node) {
		String[] geom;

		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			Node _node = node.getChildNodes().item(i);

			if (_node.getNodeValue() != null) {
				String temp = _node.getNodeValue().replace("\n", " ");
				temp = temp.replace("\t", " ");
				geom = temp.split(" ");

				if (geom != null && geom.length >= 2)
					return geom;
			}

			else if (_node.getChildNodes().getLength() > 0) {
				geom = geomLoop(_node);

				if (geom != null && geom.length >= 2)
					return geom;
			}
		}
		return null;
	}

	private void attributeLoop(int f_idx, Node node, int parents) {
		int a_idx = sDao.getAttributeIDX() + 1;

		Attribute attr = new Attribute();
		attr.f_idx = f_idx;
		attr.a_idx = a_idx;
		attr.name = node.getNodeName();
		attr.value = node.getFirstChild() != null ? (node.getFirstChild().getNodeValue() != null
				? node.getFirstChild().getNodeValue().replace("'", "\'||CHR(39)||\'")
				: "") : "";
		attr.parents = String.valueOf(parents);
		attr.attributetype = node.getParentNode().getNodeName();

		sDao.insertAttribute(attr);

		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			attributeLoop(f_idx, node.getChildNodes().item(i), a_idx);
		}
		if (node.getChildNodes().getLength() == 0)
			return;
	}
	////

	// Write _ DataSet
	public void writeDataSet(String datasetIDX, String filePath, String fileName) {

		writeFeatureCatalogue(sDao.getC_idx(datasetIDX), filePath);

		CreateImgPath(sDao.getUploadFileName(datasetIDX), sDao.getUploadFilePath(datasetIDX), filePath);

		// DOM ���� ����
		// XmlDocument newDoc = new XmlDocument();
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();

		DocumentBuilder parser;

		try {
			parser = f.newDocumentBuilder();
			Document newDoc = parser.newDocument();

			// ����
			// XmlDeclaration dec = newDoc.CreateXmlDeclaration("1.0", "ISO-8859-1", "yes");
			// newDoc.AppendChild(dec);

			Element eleDataSet = writeDataSet(newDoc, datasetIDX);

			writeBoundedBy(newDoc, eleDataSet, datasetIDX);

			writeDatasetIdentificationInformation(newDoc, eleDataSet, datasetIDX);

			writeDatasetStructureInformation(newDoc, eleDataSet, datasetIDX);

			writeFeature(newDoc, eleDataSet, datasetIDX);

			DOMSource xmlDOM = new DOMSource(newDoc);
			StreamResult xmlFile = new StreamResult(new File(filePath + fileName + ".gml"));
			TransformerFactory.newInstance().newTransformer().transform(xmlDOM, xmlFile);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	private Element writeDataSet(Document newDoc, String datasetIDX) {
		Element dataSet = newDoc.createElement("DataSet");
		dataSet.setAttribute("xmlns:S100", "http://www.iho.int/s100gml/1.0");
		dataSet.setAttribute("xmlns:S100EXT", "http://www.iho.int/s100gml/1.0+EXT");
		dataSet.setAttribute("xmlns:gml", "http://www.opengis.net/gml/3.2");
		dataSet.setAttribute("xmlns:s100_profile", "http://www.iho.int/S-100/profile/s100_gmlProfile");
		dataSet.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
		dataSet.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		

		dataSet.setAttribute("xmlns:S201", "http://www.iala-aism.org/S201/gml/1.0");
		dataSet.setAttribute("xsi:schemaLocation", "http://www.iala-aism.org/S201/gml/1.0 S201.xsd");
		dataSet.setAttribute("gml:id", datasetIDX);

		newDoc.appendChild(dataSet);

		return dataSet;
	}

	private void writeBoundedBy(Document newDoc, Element dataSet, String fileID) {
		BoundedBy bb = sDao.selectBoundedBy(fileID);

		Element boundedBy = newDoc.createElement("gml:boundedBy");
		Element Envelope = newDoc.createElement("gml:Envelope");
		Element lowerCorner = newDoc.createElement("gml:lowerCorner");
		Element upperCorner = newDoc.createElement("gml:upperCorner");

		Envelope.setAttribute("srsName", bb.srsname);

		lowerCorner.setTextContent(bb.lx + " " + bb.ly);
		upperCorner.setTextContent(bb.ux + " " + bb.uy);

		Envelope.appendChild(lowerCorner);
		Envelope.appendChild(upperCorner);
		boundedBy.appendChild(Envelope);
		dataSet.appendChild(boundedBy);
	}

	private void writeDatasetIdentificationInformation(Document newDoc, Element dataSet, String fileID) {
		DatasetIdentificationInformation di = sDao.selectDatasetIdentificationInformation(fileID);

		Element DatasetIdentificationInformation = newDoc.createElement("DatasetIdentificationInformation");
		Element encodingSpecification = newDoc.createElement("S100:encodingSpecification");
		Element encodingSpecificationEdition = newDoc.createElement("S100:encodingSpecificationEdition");
		Element productIdentifier = newDoc.createElement("S100:productIdentifier");
		Element productEdition = newDoc.createElement("S100:productEdition");
		Element applicationProfile = newDoc.createElement("S100:applicationProfile");
		Element datasetFileIdentifier = newDoc.createElement("S100:datasetFileIdentifier");
		Element datasetTitle = newDoc.createElement("S100:datasetTitle");
		Element datasetReferenceDate = newDoc.createElement("S100:datasetReferenceDate");
		Element datasetLanguage = newDoc.createElement("S100:datasetLanguage");
		Element datasetTopicCategory = newDoc.createElement("S100:datasetTopicCategory");

		encodingSpecification.setTextContent(di.encodingSpecification);
		encodingSpecificationEdition.setTextContent(di.encodingSpecificationEdition);
		productIdentifier.setTextContent(di.productIdentifier);
		productEdition.setTextContent(di.productEdition);
		applicationProfile.setTextContent(di.applicationProfile);
		datasetFileIdentifier.setTextContent(di.datasetFileIdentifier);
		datasetTitle.setTextContent(di.datasetTitle);
		datasetReferenceDate.setTextContent(di.datasetReferenceDate);
		datasetLanguage.setTextContent(di.datasetLanguage);
		datasetTopicCategory.setTextContent(di.datasetTopicCategory);

		DatasetIdentificationInformation.appendChild(encodingSpecification);
		DatasetIdentificationInformation.appendChild(encodingSpecificationEdition);
		DatasetIdentificationInformation.appendChild(productIdentifier);
		DatasetIdentificationInformation.appendChild(productEdition);
		DatasetIdentificationInformation.appendChild(applicationProfile);
		DatasetIdentificationInformation.appendChild(datasetFileIdentifier);
		DatasetIdentificationInformation.appendChild(datasetTitle);
		DatasetIdentificationInformation.appendChild(datasetReferenceDate);
		DatasetIdentificationInformation.appendChild(datasetLanguage);
		DatasetIdentificationInformation.appendChild(datasetTopicCategory);
		dataSet.appendChild(DatasetIdentificationInformation);
	}

	private void writeDatasetStructureInformation(Document newDoc, Element dataSet, String fileID) {
		DatasetStructureInformation ds = sDao.selectDatasetStructureInformation(fileID);

		Element DatasetStructureInformation = newDoc.createElement("DatasetStructureInformation");
		Element datasetCoordOriginX = newDoc.createElement("S100:datasetCoordOriginX");
		Element datasetCoordOriginY = newDoc.createElement("S100:datasetCoordOriginY");
		Element datasetCoordOriginZ = newDoc.createElement("S100:datasetCoordOriginZ");
		Element coordMultFactorX = newDoc.createElement("S100:coordMultFactorX");
		Element coordMultFactorY = newDoc.createElement("S100:coordMultFactorY");
		Element coordMultFactorZ = newDoc.createElement("S100:coordMultFactorZ");

		if (ds.datasetCoordOriginX != null)
			datasetCoordOriginX.setTextContent(ds.datasetCoordOriginX);
		if (ds.datasetCoordOriginY != null)
			datasetCoordOriginY.setTextContent(ds.datasetCoordOriginY);
		if (ds.datasetCoordOriginZ != null)
			datasetCoordOriginZ.setTextContent(ds.datasetCoordOriginZ);

		coordMultFactorX.setTextContent(ds.coordMultFactorX);
		coordMultFactorY.setTextContent(ds.coordMultFactorY);
		coordMultFactorZ.setTextContent(ds.coordMultFactorZ);

		DatasetStructureInformation.appendChild(datasetCoordOriginX);
		DatasetStructureInformation.appendChild(datasetCoordOriginY);
		DatasetStructureInformation.appendChild(datasetCoordOriginZ);
		DatasetStructureInformation.appendChild(coordMultFactorX);
		DatasetStructureInformation.appendChild(coordMultFactorY);
		DatasetStructureInformation.appendChild(coordMultFactorZ);
		dataSet.appendChild(DatasetStructureInformation);
	}

	private void writeFeature(Document newDoc, Element dataSet, String fileID) {
		List<Feature> list = sDao.selectFeature(fileID);

		for (Feature feat : list) {

			if (feat == null || feat.type == null)
				continue;

			Element mem;
			if (feat.type.contains("imember"))
				mem = newDoc.createElement("imember");
			else
				mem = newDoc.createElement("member");

			Element _feat = newDoc.createElement(feat.featuretype);
			_feat.setAttribute("gml:id", String.valueOf(feat.f_idx));

			dataSet.appendChild(mem);
			mem.appendChild(_feat);

			List<Association> aList = sDao.selectAssociation(String.valueOf(feat.f_idx));

			for (Association associ : aList) {
				Element ass;// = newDoc.createElement("S100:informationAssociation");

				if (associ.xrole.contains("child")) {
					ass = newDoc.createElement("child");
					ass.setAttribute("xlink:title", associ.xid);
				} else if (associ.xrole.contains("parent")) {
					ass = newDoc.createElement("parent");
					ass.setAttribute("xlink:title", associ.xid);
				} else {
					ass = newDoc.createElement("S100:informationAssociation");
					ass.setAttribute("gml:id", associ.xid);
				}
				ass.setAttribute("xlink:role", associ.xrole);
				ass.setAttribute("xlink:href", associ.xhref);

				_feat.appendChild(ass);
			}

			// newDoc ... new Node? new Element?
			List<Attribute> attributeList = sDao.selectAttribute(String.valueOf(feat.f_idx));

			for (int i = 0; i < attributeList.size(); i++) {
				if (!attributeList.get(i).name.contains("#")) {
					attributeList.get(i).xml = newDoc.createElement(attributeList.get(i).name);

					if (attributeList.get(i).value != null) {
						attributeList.get(i).xml.setTextContent(attributeList.get(i).value);

						if (attributeList.get(i).value.contains(".jpg"))
							CopyImg(attributeList.get(i).value);
					}
				}
			}

			for (int i = 0; i < attributeList.size(); i++) {
				boolean check = true;
				for (int j = 0; j < attributeList.size(); j++) {
					if (!attributeList.get(i).name.contains("#") && !attributeList.get(j).name.contains("#")
							&& attributeList.get(j).parents != null && attributeList.get(i) != attributeList.get(j)
							&& attributeList.get(i).a_idx == Integer.parseInt(attributeList.get(j).parents)
							&& attributeList.get(i).xml != null && attributeList.get(j).xml != null) {
						if (check) {
							attributeList.get(i).xml.setTextContent(null);
							check = !check;
						}
						attributeList.get(i).xml.appendChild(attributeList.get(j).xml);
					}
				}
				if (attributeList.get(i).parents == null || Integer.parseInt(attributeList.get(i).parents) == -1)
					_feat.appendChild(attributeList.get(i).xml);
			}

			switch (feat.type) {
			case "Point":
				String pointTemp = sDao.selectPoint(String.valueOf(feat.f_idx));

				pointTemp = pointTemp.replace('(', '#');
				pointTemp = pointTemp.replace(')', '#');
				String[] pointToken = pointTemp.split("#");

				Element _geom = newDoc.createElement("geometry");
				Element _pointProperty = newDoc.createElement("S100:pointProperty");
				Element _Point = newDoc.createElement("S100:Point");
				Element _pos = newDoc.createElement("gml:pos");
				_pos.setTextContent(pointToken.length > 1 ? pointToken[1] : "");
				_Point.appendChild(_pos);
				_pointProperty.appendChild(_Point);
				_geom.appendChild(_pointProperty);
				_feat.appendChild(_geom);
				break;
			case "Polygon":

				String polygonTemp = sDao.selectPolygon(String.valueOf(feat.f_idx));

				polygonTemp = polygonTemp.replace('(', '#');
				polygonTemp = polygonTemp.replace(')', '#');
				String[] polygonToken = polygonTemp.split("#");

				Element _geometry = newDoc.createElement("geometry");
				Element _surfaceProperty = newDoc.createElement("S100:surfaceProperty");
				Element _Surface = newDoc.createElement("gml:Surface");
				Element _patches = newDoc.createElement("gml:patches");
				Element _PolygonPatch = newDoc.createElement("gml:PolygonPatch");
				Element _exterior = newDoc.createElement("gml:exterior");
				Element _LinearRing = newDoc.createElement("gml:LinearRing");
				Element _posList = newDoc.createElement("gml:posList");
				_posList.setTextContent(polygonToken.length > 1 ? polygonToken[2].replace(',', ' ') : "");
				_LinearRing.appendChild(_posList);
				_exterior.appendChild(_LinearRing);
				_PolygonPatch.appendChild(_exterior);
				_patches.appendChild(_PolygonPatch);
				_Surface.appendChild(_patches);
				_surfaceProperty.appendChild(_Surface);
				_geometry.appendChild(_surfaceProperty);
				_feat.appendChild(_geometry);

				break;
			case "Polyline":
				// sql = string.Format("select ST_AsText(geom) as geom from polyline where f_idx
				// = {0}", feat.f_idx);
				// NpgsqlDataReader polyline = main.getDataReader(sql);
				// string polylineTemp = polyline.Read() ? polyline["geom"].ToString() : "";
				// polyline.Close();
				// polylineTemp = polylineTemp.Replace('(', '#');
				// polylineTemp = polylineTemp.Replace(')', '#');
				// string[] polylineToken = polylineTemp.Split('#');
				// XmlElement polylineGeom = newDoc.CreateElement("geometry");
				// XmlElement polylineG = newDoc.CreateElement("Polyline");
				// polylineG.InnerText = polylineToken.Length > 1 ? polylineToken[2] : "";
				// polylineGeom.AppendChild(polylineG);
				// _feat.AppendChild(polylineGeom);
				break;
			}

		}
	}
	/////

	// Read _ FeatureCatalogue
	public int readFeatureCatalogue() {

		int catalogue_pk = cDao.getCatalogueIDX() + 1;

		int check = cDao.insertCatalogue(catalogue_pk);

		if (check < 0)
			return -1;

		// ��ü �ڽ��� �� ��ŭ
		for (int i = 0; i < xmlDoc.getFirstChild().getChildNodes().getLength(); i++) {
			if (xmlDoc.getFirstChild().getChildNodes().item(i).getNodeName().contains("#"))
				continue;

			Element ele = (Element) xmlDoc.getFirstChild().getChildNodes().item(i);

			// Node node = xmlDoc.getFirstChild().getChildNodes().item(i);

			if (ele.getNodeName().contains("SimpleAttributes"))
				readSimpleAttributes(ele, catalogue_pk);
			else if (ele.getNodeName().contains("ComplexAttributes"))
				readComplexAttributes(ele, catalogue_pk);
			else if (ele.getNodeName().contains("Roles"))
				readRoles(ele, catalogue_pk);
			else if (ele.getNodeName().contains("InformationAssociations"))
				readInformationAssociations(ele, catalogue_pk);
			else if (ele.getNodeName().contains("FeatureAssociations"))
				readFeatureAssociations(ele, catalogue_pk);
			else if (ele.getNodeName().contains("InformationTypes"))
				readInformationTypes(ele, catalogue_pk);
			else if (ele.getNodeName().contains("FeatureTypes"))
				readFeatureTypes(ele, catalogue_pk);
			else {
				int value = 0;
				value = readCatalogueAttribute(ele, catalogue_pk);
				if (value > 0) {
					/* cDao.rollback(); */
					return value;
				}
			}
		}

		return catalogue_pk;
	}

	private String elementValue(Element ele, String tagName, String childTagName) {
		String value = "null";
		if (tagName == null)
			value = ele.getTextContent();
		else if (ele.getElementsByTagName(tagName) != null && ele.getElementsByTagName(tagName).getLength() > 0) {
			Element child = (Element) ele.getElementsByTagName(tagName).item(0);

			if (childTagName == null) {
				value = child.getTextContent();
			} else if (child.getElementsByTagName(childTagName) != null
					&& child.getElementsByTagName(childTagName).getLength() > 0) {
				Element subChild = (Element) child.getElementsByTagName(childTagName).item(0);
				value = subChild.getTextContent();
			}

			value = value.replace("\n", "");
			value = value.replace("\t", "");
			value = value.replace("'", "\'||CHR(39)||\'");

			if (value.equals(""))
				value = "null";
		}

		return value;
	}

	private String elementAttribute(Element ele, String AttrName, String tagName, String childTagName) {
		String value = "null";

		if (tagName == null)
			value = ele.getAttribute(AttrName);
		else if (ele.getElementsByTagName(tagName) != null && ele.getElementsByTagName(tagName).getLength() > 0) {
			Element child = (Element) ele.getElementsByTagName(tagName).item(0);

			if (childTagName == null) {
				value = child.getAttribute(AttrName);
			} else if (child.getElementsByTagName(childTagName) != null
					&& child.getElementsByTagName(childTagName).getLength() > 0) {
				Element subChild = (Element) child.getElementsByTagName(childTagName).item(0);
				value = subChild.getAttribute(AttrName);
			}
		}

		value = value.replace("\n", "");
		value = value.replace("\t", "");
		value = value.replace("'", "\'||CHR(39)||\'");

		if (value.equals(""))
			value = "null";

		return value;
	}

	private void readSimpleAttributes(Element ele, int catalogue_pk) {
		for (int i = 0; i < ele.getChildNodes().getLength(); i++) {
			if (ele.getChildNodes().item(i).getNodeName().contains("#"))
				continue;

			Element child = (Element) ele.getChildNodes().item(i);
			SimpleAttribute sa = new SimpleAttribute();
			sa.catalogue_fk = catalogue_pk;
			sa.simpleAttribute_pk = cDao.getSimpleAttributeIDX() + 1;
			sa.name = elementValue(child, "S100FC:name", null);// child["S100FC:name"] != null ?
																// child["S100FC:name"].InnerText : "null";
			sa.definition = elementValue(child, "S100FC:definition", null);// child["S100FC:definition"] != null ?
																			// child["S100FC:definition"].InnerText :
																			// "null";
			sa.code = elementValue(child, "S100FC:code", null);// child["S100FC:code"] != null ?
																// child["S100FC:code"].InnerText : "null";
			sa.alias = elementValue(child, "S100FC:alias", null);// child["S100FC:alias"] != null ?
																	// child["S100FC:alias"].InnerText : "null";
			sa.valueType = elementValue(child, "S100FC:valueType", null);// child["S100FC:valueType"] != null ?
																			// child["S100FC:valueType"].InnerText :
																			// "null";
			sa.remarks = elementValue(child, "S100FC:remarks", null);// child["S100FC:remarks"] != null ?
																		// child["S100FC:remarks"].InnerText : "null";

			cDao.insertSimpleAttribute(sa);

			if (child.getElementsByTagName("S100FC:listedValues") != null
					&& child.getElementsByTagName("S100FC:listedValues").getLength() > 0) {
				for (int j = 0; j < child.getElementsByTagName("S100FC:listedValues").item(0).getChildNodes()
						.getLength(); j++) {
					if (child.getElementsByTagName("S100FC:listedValues").item(0).getChildNodes().item(j).getNodeName()
							.contains("#"))
						continue;

					Element eleLV = (Element) child.getElementsByTagName("S100FC:listedValues").item(0).getChildNodes()
							.item(j);

					ListedValue lv = new ListedValue();

					lv.simpleAttribute_fk = sa.simpleAttribute_pk;
					lv.label = elementValue(eleLV, "S100FC:label", null);// listedValue["S100FC:label"] != null ?
																			// listedValue["S100FC:label"].InnerText :
																			// "null";
					lv.definition = elementValue(eleLV, "S100FC:definition", null);// listedValue["S100FC:definition"]
																					// != null ?
																					// listedValue["S100FC:definition"].InnerText
																					// : "null";
					lv.code = elementValue(eleLV, "S100FC:code", null);// listedValue["S100FC:code"] != null ?
																		// listedValue["S100FC:code"].InnerText :
																		// "null";

					cDao.insertListedValue(lv);
				}
			}
		}
	}

	private void readComplexAttributes(Element ele, int catalogue_pk) {
		for (int i = 0; i < ele.getChildNodes().getLength(); i++) {
			if (ele.getChildNodes().item(i).getNodeName().contains("#"))
				continue;

			Element child = (Element) ele.getChildNodes().item(i);

			ComplexAttribute ca = new ComplexAttribute();
			ca.catalogue_fk = catalogue_pk;
			ca.complex_pk = cDao.getComplexAttributeIDX() + 1;
			ca.name = elementValue(child, "S100FC:name", null);// child["S100FC:name"] != null ?
																// child["S100FC:name"].InnerText : "null";
			ca.definition = elementValue(child, "S100FC:definition", null);// child["S100FC:definition"] != null ?
																			// child["S100FC:definition"].InnerText :
																			// "null";
			ca.code = elementValue(child, "S100FC:code", null);// child["S100FC:code"] != null ?
																// child["S100FC:code"].InnerText : "null";
			ca.remarks = elementValue(child, "S100FC:remarks", null);// child["S100FC:remarks"] != null ?
																		// child["S100FC:remarks"].InnerText : "null";

			cDao.insertComplexAttribute(ca);

			for (int j = 0; j < child.getChildNodes().getLength(); j++) {
				if (child.getChildNodes().item(j).getNodeName().contains("#"))
					continue;

				Element subAttributeBinding = (Element) child.getChildNodes().item(j);

				if (subAttributeBinding.getNodeName().contains("subAttributeBinding")) {
					ComplexBinding cb = new ComplexBinding();
					cb.complex_fk = ca.complex_pk;
					cb.complexBinding_pk = cDao.getComplexBindingIDX() + 1;
					cb.lower = elementValue(subAttributeBinding, "S100FC:multiplicity", "S100Base:lower");// subAttributeBinding["S100FC:multiplicity"]
																											// != null ?
																											// subAttributeBinding["S100FC:multiplicity"]["S100Base:lower"].InnerText
																											// : "null";
					cb.upper = elementValue(subAttributeBinding, "S100FC:multiplicity", "S100Base:upper");// subAttributeBinding["S100FC:multiplicity"]
																											// != null ?
																											// subAttributeBinding["S100FC:multiplicity"]["S100Base:upper"].InnerText
																											// : "null";
					cb.attribute = elementAttribute(subAttributeBinding, "ref", "S100FC:attribute", null);// subAttributeBinding["S100FC:attribute"]
																											// != null ?
																											// subAttributeBinding["S100FC:attribute"].Attributes["ref"].Value
																											// : "null";
					cb.sequential = elementAttribute(subAttributeBinding, "sequential", null, null);// subAttributeBinding.Attributes["sequential"]
																									// != null ?
																									// subAttributeBinding.Attributes["sequential"].Value
																									// : "null";
					cb.nil = elementAttribute(subAttributeBinding, "xsi:nil", "S100FC:multiplicity", "S100Base:upper");// subAttributeBinding["S100FC:multiplicity"]
																														// !=
																														// null
																														// ?
																														// subAttributeBinding["S100FC:multiplicity"]["S100Base:upper"].Attributes["xsi:nil"].Value
																														// :
																														// "null";
					cb.infinite = elementAttribute(subAttributeBinding, "infinite", "S100FC:multiplicity",
							"S100Base:upper");// subAttributeBinding["S100FC:multiplicity"] != null ?
												// subAttributeBinding["S100FC:multiplicity"]["S100Base:upper"].Attributes["infinite"].Value
												// : "null";

					cDao.insertComplexBinding(cb);

					if (subAttributeBinding.getElementsByTagName("S100FC:permittedValues") != null
							&& subAttributeBinding.getElementsByTagName("S100FC:permittedValues").getLength() > 0) {
						for (int k = 0; k < subAttributeBinding.getElementsByTagName("S100FC:permittedValues").item(0)
								.getChildNodes().getLength(); k++) {
							if (subAttributeBinding.getElementsByTagName("S100FC:permittedValues").item(0)
									.getChildNodes().item(k).getNodeName().contains("#"))
								continue;
							Element eleCB = (Element) subAttributeBinding.getElementsByTagName("S100FC:permittedValues")
									.item(0).getChildNodes().item(k);

							ComplexPermittedValue cp = new ComplexPermittedValue();
							cp.complexBinding_fk = cb.complexBinding_pk;
							cp.value = elementValue(eleCB, null, null);

							cDao.insertComplexPermittedValue(cp);
						}
					}

				}
			}
		}
	}

	private void readRoles(Element ele, int catalogue_pk) {
		for (int i = 0; i < ele.getChildNodes().getLength(); i++) {
			if (ele.getChildNodes().item(i).getNodeName().contains("#"))
				continue;

			Element child = (Element) ele.getChildNodes().item(i);

			Role role = new Role();
			role.catalogue_fk = catalogue_pk;
			role.name = elementValue(child, "S100FC:name", null);// child["S100FC:name"] != null ?
																	// child["S100FC:name"].InnerText : "null";
			role.definition = elementValue(child, "S100FC:definition", null);// child["S100FC:definition"] != null ?
																				// child["S100FC:definition"].InnerText
																				// : "null";
			role.code = elementValue(child, "S100FC:code", null);// child["S100FC:code"] != null ?
																	// child["S100FC:code"].InnerText : "null";

			cDao.insertRole(role);
		}
	}

	private void readInformationAssociations(Element ele, int catalogue_pk) {
		for (int i = 0; i < ele.getChildNodes().getLength(); i++) {
			if (ele.getChildNodes().item(i).getNodeName().contains("#"))
				continue;

			Element child = (Element) ele.getChildNodes().item(i);
			InformationAssociation ia = new InformationAssociation();
			ia.catalogue_fk = catalogue_pk;
			ia.name = elementValue(child, "S100FC:name", null);// child["S100FC:name"] != null ?
																// child["S100FC:name"].InnerText : "null";
			ia.definition = elementValue(child, "S100FC:definition", null);// child["S100FC:definition"] != null ?
																			// child["S100FC:definition"].InnerText :
																			// "null";
			ia.code = elementValue(child, "S100FC:code", null);// child["S100FC:code"] != null ?
																// child["S100FC:code"].InnerText : "null";
			ia.role = elementValue(child, "S100FC:role", null);// child["S100FC:role"] != null ?
																// child["S100FC:role"].Attributes["ref"].Value :
																// "null";

			cDao.insertInformationAssociation(ia);
		}
	}

	private void readFeatureAssociations(Element ele, int catalogue_pk) {
		for (int i = 0; i < ele.getChildNodes().getLength(); i++) {
			if (ele.getChildNodes().item(i).getNodeName().contains("#"))
				continue;

			Element child = (Element) ele.getChildNodes().item(i);
			FeatureAssociation fa = new FeatureAssociation();
			fa.catalogue_fk = catalogue_pk;
			fa.name = elementValue(child, "S100FC:name", null);// child["S100FC:name"] != null ?
																// child["S100FC:name"].InnerText : "null";
			fa.definition = elementValue(child, "S100FC:definition", null);// child["S100FC:definition"] != null ?
																			// child["S100FC:definition"].InnerText :
																			// "null";
			fa.code = elementValue(child, "S100FC:code", null);// child["S100FC:code"] != null ?
																// child["S100FC:code"].InnerText : "null";

			for (int j = 0; j < child.getChildNodes().getLength(); j++) {
				if (child.getChildNodes().item(j).getNodeName().contains("role")) {
					if (fa.role1 == "" || fa.role1 == null)
						fa.role1 = elementAttribute((Element) child.getChildNodes().item(j), "ref", null, null);// child.ChildNodes[i]
																												// !=
																												// null
																												// ?
																												// child.ChildNodes[i].Attributes["ref"].Value
																												// :
																												// "null";
					else if (fa.role2 == "" || fa.role2 == null)
						fa.role2 = elementAttribute((Element) child.getChildNodes().item(j), "ref", null, null);// child.ChildNodes[i]
																												// !=
																												// null
																												// ?
																												// child.ChildNodes[i].Attributes["ref"].Value
																												// :
																												// "null";
				}
			}
			fa.remarks = elementValue(child, "S100FC:remarks", null);// child["S100FC:remarks"] != null ?
																		// child["S100FC:remarks"].InnerText : "null";
			cDao.insertFeatureAssociation(fa);
		}
	}

	private void readInformationTypes(Element ele, int catalogue_pk) {
		for (int i = 0; i < ele.getChildNodes().getLength(); i++) {
			if (ele.getChildNodes().item(i).getNodeName().contains("#"))
				continue;

			Element child = (Element) ele.getChildNodes().item(i);

			InformationType it = new InformationType();
			it.catalogue_fk = catalogue_pk;
			it.informationType_pk = cDao.getInformationTypeIDX() + 1;
			it.name = elementValue(child, "S100FC:name", null);// child["S100FC:name"] != null ?
																// child["S100FC:name"].InnerText : "null";
			it.definition = elementValue(child, "S100FC:definition", null);// child["S100FC:definition"] != null ?
																			// child["S100FC:definition"].InnerText :
																			// "null";
			it.code = elementValue(child, "S100FC:code", null);// child["S100FC:code"] != null ?
																// child["S100FC:code"].InnerText : "null";

			cDao.insertInformationType(it);

			for (int j = 0; j < child.getChildNodes().getLength(); j++) {
				if (child.getChildNodes().item(j).getNodeName().contains("#"))
					continue;

				Element subAttributeBinding = (Element) child.getChildNodes().item(j);

				if (subAttributeBinding.getNodeName().contains("attributeBinding")) {
					InformationBinding ib = new InformationBinding();
					ib.informationType_fk = it.informationType_pk;
					ib.informationBinding_pk = cDao.getInformationBindingIDX() + 1;
					ib.lower = elementValue(subAttributeBinding, "S100FC:multiplicity", "S100Base:lower");// subAttributeBinding["S100FC:multiplicity"]
																											// != null ?
																											// subAttributeBinding["S100FC:multiplicity"]["S100Base:lower"].InnerText
																											// : "null";
					ib.upper = elementValue(subAttributeBinding, "S100FC:multiplicity", "S100Base:upper");// subAttributeBinding["S100FC:multiplicity"]
																											// != null ?
																											// subAttributeBinding["S100FC:multiplicity"]["S100Base:upper"].InnerText
																											// : "null";
					ib.attribute = elementAttribute(subAttributeBinding, "ref", "S100FC:attribute", null);// subAttributeBinding["S100FC:attribute"]
																											// != null ?
																											// subAttributeBinding["S100FC:attribute"].Attributes["ref"].Value
																											// : "null";
					ib.sequential = elementAttribute(subAttributeBinding, "sequential", null, null);// subAttributeBinding.Attributes["sequential"]
																									// != null ?
																									// subAttributeBinding.Attributes["sequential"].Value
																									// : "null";
					ib.nil = elementAttribute(subAttributeBinding, "xsi:nil", "S100FC:multiplicity", "S100Base:upper");// subAttributeBinding["S100FC:multiplicity"]
																														// !=
																														// null
																														// ?
																														// subAttributeBinding["S100FC:multiplicity"]["S100Base:upper"].Attributes["xsi:nil"].Value
																														// :
																														// "null";
					ib.infinite = elementAttribute(subAttributeBinding, "infinite", "S100FC:multiplicity",
							"S100Base:upper");// subAttributeBinding["S100FC:multiplicity"] != null ?
												// subAttributeBinding["S100FC:multiplicity"]["S100Base:upper"].Attributes["infinite"].Value
												// : "null";

					cDao.insertInformationBinding(ib);

					if (subAttributeBinding.getElementsByTagName("S100FC:permittedValues") != null
							&& subAttributeBinding.getElementsByTagName("S100FC:permittedValues").getLength() > 0) {
						for (int k = 0; k < subAttributeBinding.getElementsByTagName("S100FC:permittedValues").item(0)
								.getChildNodes().getLength(); k++) {
							if (subAttributeBinding.getElementsByTagName("S100FC:permittedValues").item(0)
									.getChildNodes().item(k).getNodeName().contains("#"))
								continue;
							Element eleIB = (Element) subAttributeBinding.getElementsByTagName("S100FC:permittedValues")
									.item(0).getChildNodes().item(k);

							InformationPermittedValue ip = new InformationPermittedValue();
							ip.informationBinding_fk = ib.informationBinding_pk;
							ip.value = elementValue(eleIB, null, null);

							cDao.insertInformationPermittedValue(ip);
						}
					}

				}
			}
		}
	}

	private void readFeatureTypes(Element ele, int catalogue_pk) {
		for (int i = 0; i < ele.getChildNodes().getLength(); i++) {
			if (ele.getChildNodes().item(i).getNodeName().contains("#"))
				continue;

			Element child = (Element) ele.getChildNodes().item(i);

			FeatureType ft = new FeatureType();
			ft.catalogue_fk = catalogue_pk;
			ft.featureType_pk = cDao.getFeatureTypesIDX() + 1;
			ft.name = elementValue(child, "S100FC:name", null);// child["S100FC:name"] != null ?
																// child["S100FC:name"].InnerText : "null";
			ft.definition = elementValue(child, "S100FC:definition", null);// child["S100FC:definition"] != null ?
																			// child["S100FC:definition"].InnerText :
																			// "null";
			ft.code = elementValue(child, "S100FC:code", null);// child["S100FC:code"] != null ?
																// child["S100FC:code"].InnerText : "null";
			ft.remarks = elementValue(child, "S100FC:remarks", null);// child["S100FC:remarks"] != null ?
																		// child["S100FC:remarks"].InnerText : "null";
			ft.featureusertype = elementValue(child, "S100FC:featureUseType", null);// child["S100FC:featureUseType"] !=
																					// null ?
																					// child["S100FC:featureUseType"].InnerText
																					// : "null";
			ft.alias = elementValue(child, "S100FC:alias", null);// child["S100FC:alias"] != null ?
																	// child["S100FC:alias"].InnerText : "null";

			cDao.insertFeatureType(ft);

			for (int j = 0; j < child.getChildNodes().getLength(); j++) {
				if (child.getChildNodes().item(j).getNodeName().contains("#"))
					continue;

				Element subAttributeBinding = (Element) child.getChildNodes().item(j);

				if (subAttributeBinding.getNodeName().contains("attributeBinding")) {
					AttributeBinding ab = new AttributeBinding();
					ab.featureType_fk = ft.featureType_pk;
					ab.attributeBinding_pk = cDao.getAttributeBindingIDX() + 1;
					ab.lower = elementValue(subAttributeBinding, "S100FC:multiplicity", "S100Base:lower");// subAttributeBinding["S100FC:multiplicity"]
																											// != null ?
																											// subAttributeBinding["S100FC:multiplicity"]["S100Base:lower"].InnerText
																											// : "null";
					ab.upper = elementValue(subAttributeBinding, "S100FC:multiplicity", "S100Base:upper");// subAttributeBinding["S100FC:multiplicity"]
																											// != null ?
																											// subAttributeBinding["S100FC:multiplicity"]["S100Base:upper"].InnerText
																											// : "null";
					ab.attribute = elementAttribute(subAttributeBinding, "ref", "S100FC:attribute", null);// subAttributeBinding["S100FC:attribute"]
																											// != null ?
																											// subAttributeBinding["S100FC:attribute"].Attributes["ref"].Value
																											// : "null";
					ab.sequential = elementAttribute(subAttributeBinding, "sequential", null, null);// subAttributeBinding.Attributes["sequential"]
																									// != null ?
																									// subAttributeBinding.Attributes["sequential"].Value
																									// : "null";
					ab.nil = elementAttribute(subAttributeBinding, "xsi:nil", "S100FC:multiplicity", "S100Base:upper");// subAttributeBinding["S100FC:multiplicity"]
																														// !=
																														// null
																														// ?
																														// subAttributeBinding["S100FC:multiplicity"]["S100Base:upper"].Attributes["xsi:nil"].Value
																														// :
																														// "null";
					ab.infinite = elementAttribute(subAttributeBinding, "infinite", "S100FC:multiplicity",
							"S100Base:upper");// subAttributeBinding["S100FC:multiplicity"] != null ?
												// subAttributeBinding["S100FC:multiplicity"]["S100Base:upper"].Attributes["infinite"].Value
												// : "null";

					cDao.insertAttributeBinding(ab);

					if (subAttributeBinding.getElementsByTagName("S100FC:permittedValues") != null
							&& subAttributeBinding.getElementsByTagName("S100FC:permittedValues").getLength() > 0) {
						for (int k = 0; k < subAttributeBinding.getElementsByTagName("S100FC:permittedValues").item(0)
								.getChildNodes().getLength(); k++) {
							if (subAttributeBinding.getElementsByTagName("S100FC:permittedValues").item(0)
									.getChildNodes().item(k).getNodeName().contains("#"))
								continue;
							Element eleAB = (Element) subAttributeBinding.getElementsByTagName("S100FC:permittedValues")
									.item(0).getChildNodes().item(k);

							FeaturePermittedValue fp = new FeaturePermittedValue();
							fp.attributeBinding_fk = ab.attributeBinding_pk;
							fp.value = elementValue(eleAB, null, null);

							cDao.insertFeaturePermittedValue(fp);
						}
					}
				} else if (subAttributeBinding.getNodeName().contains("featureBinding")) {
					FeatureBinding fb = new FeatureBinding();
					fb.featureType_fk = ft.featureType_pk;
					fb.lower = elementValue(subAttributeBinding, "S100FC:multiplicity", "S100Base:lower");// subNode["S100FC:multiplicity"]
																											// != null ?
																											// subNode["S100FC:multiplicity"]["S100Base:lower"].InnerText
																											// : "null";
					fb.upper = elementValue(subAttributeBinding, "S100FC:multiplicity", "S100Base:upper");// subNode["S100FC:multiplicity"]
																											// != null ?
																											// subNode["S100FC:multiplicity"]["S100Base:upper"].InnerText
																											// : "null";
					fb.association = elementAttribute(subAttributeBinding, "ref", "S100FC:association", null);// subNode["S100FC:association"]
																												// !=
																												// null
																												// ?
																												// subNode["S100FC:association"].Attributes["ref"].Value
																												// :
																												// "null";
					fb.role = elementAttribute(subAttributeBinding, "ref", "S100FC:role", null);// subNode["S100FC:role"]
																								// != null ?
																								// subNode["S100FC:role"].Attributes["ref"].Value
																								// : "null";
					fb.featureType = elementAttribute(subAttributeBinding, "ref", "S100FC:featureType", null);// subNode["S100FC:featureType"]
																												// !=
																												// null
																												// ?
																												// subNode["S100FC:featureType"].Attributes["ref"].Value
																												// :
																												// "null";
					fb.roleType = elementAttribute(subAttributeBinding, "roleType", "S100FC:featureBinding", null);// subNode["S100FC:featureBinding"]
																													// !=
																													// null
																													// ?
																													// subNode["S100FC:featureBinding"].Attributes["roleType"].Value
																													// :
																													// "null";
					fb.nil = elementAttribute(subAttributeBinding, "xsi:nil", "S100FC:multiplicity", "S100Base:upper");// subNode["S100FC:multiplicity"]
																														// !=
																														// null
																														// ?
																														// subNode["S100FC:multiplicity"]["S100Base:upper"].Attributes["xsi:nil"].Value
																														// :
																														// "null";
					fb.infinite = elementAttribute(subAttributeBinding, "infinite", "S100FC:multiplicity",
							"S100Base:upper");// subNode["S100FC:multiplicity"] != null ?
												// subNode["S100FC:multiplicity"]["S100Base:upper"].Attributes["infinite"].Value
												// : "null";

					cDao.insertFeatureBinding(fb);
				} else if (subAttributeBinding.getNodeName().contains("permittedPrimitives")) {
					FeaturePermittedPrimitive fpp = new FeaturePermittedPrimitive();
					fpp.featureType_fk = ft.featureType_pk;
					fpp.permittedPrimitives = elementValue(subAttributeBinding, null, null);
					cDao.insertFeaturePermittedPrimitive(fpp);
				}
			}
		}
	}

	private int readCatalogueAttribute(Element ele, int catalogue_pk) {
		if (ele.getNodeName().contains("#"))
			return 0;

		CatalogueAttribute ca = new CatalogueAttribute();
		ca.catalogue_fk = catalogue_pk;
		ca.catalogueAttribute_pk = cDao.getCatalogueAttributeIDX() + 1;
		ca.camecase = ele.getNodeName();
		ca.value = elementValue(ele, null, null);
		ca.parents = "null";

		int check = 0;

		if (ca.camecase.contains("versionNumber"))
			check = cDao.getCatalogueAttributeVersionCheck(ca.value);

		if (check > 0)
			return check;
		else
			cDao.insertCatalogueAttribute(ca);

		int parents = ca.catalogueAttribute_pk;// db.insertCatalogAttribute(ca);

		for (int j = 0; j < ele.getChildNodes().getLength(); j++) {
			if (ele.getChildNodes().item(j).getNodeName().contains("#"))
				continue;
			catalogueChildLoop(catalogue_pk, (Element) ele.getChildNodes().item(j), parents);
		}

		return 0;
	}

	private void catalogueChildLoop(int catalog_fk, Element ele, int parents) {
		if (ele.getNodeName().contains("#"))
			return;

		CatalogueAttribute ca = new CatalogueAttribute();
		ca.catalogue_fk = catalog_fk;
		ca.catalogueAttribute_pk = cDao.getCatalogueAttributeIDX() + 1;
		ca.camecase = ele.getNodeName();
		ca.value = elementValue(ele, null, null);
		ca.parents = String.valueOf(parents);

		cDao.insertCatalogueAttribute(ca);

		for (int m = 0; m < ele.getChildNodes().getLength(); m++) {
			if (ele.getChildNodes().item(m).getNodeName().contains("#"))
				continue;
			catalogueChildLoop(catalog_fk, (Element) ele.getChildNodes().item(m), parents + 1);
		}
		if (ele.getChildNodes().getLength() == 0)
			return;
	}
	///////////////////

	// Write _ FeatureCatalogue
	public int writeFeatureCatalogue(String fileID, String path) {
		// DOM ���� ����
		// XmlDocument newDoc = new XmlDocument();
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();

		DocumentBuilder parser;

		try {
			parser = f.newDocumentBuilder();
			Document newDoc = parser.newDocument();

			// ����
			// XmlDeclaration dec = newDoc.CreateXmlDeclaration("1.0", "ISO-8859-1", "yes");
			// newDoc.AppendChild(dec);

			Element fc = writeCatalogue(newDoc);

			writeCatalogueAttribute(newDoc, fc, fileID);

			writeSimpleAttributes(newDoc, fc, fileID);

			writeComplexAttributes(newDoc, fc, fileID);

			writeRoles(newDoc, fc, fileID);

			writeInformationAssociations(newDoc, fc, fileID);

			writeFeatureAssociations(newDoc, fc, fileID);

			writeInformationTypes(newDoc, fc, fileID);

			writeFeatureTypes(newDoc, fc, fileID);

			DOMSource xmlDOM = new DOMSource(newDoc);
			StreamResult xmlFile = new StreamResult(new File(path + fileID + "_FC.xml"));
			TransformerFactory.newInstance().newTransformer().transform(xmlDOM, xmlFile);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		// DOM ���� ����

		return 1;
	}

	private Element writeCatalogue(Document newDoc) {
		Element featureCatalogue = newDoc.createElement("S100FC:S100_FC_FeatureCatalogue");
		featureCatalogue.setAttribute("xmlns:S100FC", "http://www.iho.int/S100FC");
		featureCatalogue.setAttribute("xmlns:S100Base", "http://www.iho.int/S100Base");
		featureCatalogue.setAttribute("xmlns:S100CI", "http://www.iho.int/S100CI");
		featureCatalogue.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
		featureCatalogue.setAttribute("xmlns:S100FD", "http://www.iho.int/S100FD");
		featureCatalogue.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		featureCatalogue.setAttribute("xsi:schemaLocation",
				"http://www.w3.org/2001/XMLSchema-instance http://www.iho.int/S100FC S100FC.xsd");

		newDoc.appendChild(featureCatalogue);

		return featureCatalogue;
	}

	private void writeCatalogueAttribute(Document newDoc, Element featureCatalogue, String fileID) {
		List<CatalogueAttribute> list = cDao.selectCatalogueAttribute(fileID);
		for (CatalogueAttribute ca : list) {
			if (ca.parents == null || ca.parents.equals("")) {
				Element catalogueAttribute = newDoc.createElement(ca.camecase);

				for (CatalogueAttribute subCA : list) {
					if ((subCA.parents != null && !subCA.parents.equals(""))
							&& String.valueOf(ca.catalogueAttribute_pk).equals(subCA.parents)) {
						Element subAttribute = newDoc.createElement(subCA.camecase);
						subAttribute.setTextContent(subCA.value);
						catalogueAttribute.appendChild(subAttribute);
					}
				}

				if (catalogueAttribute.getChildNodes().getLength() == 0)
					catalogueAttribute.setTextContent(ca.value);

				featureCatalogue.appendChild(catalogueAttribute);

			}
		}
	}

	private void writeSimpleAttributes(Document newDoc, Element featureCatalogue, String fileID) {
		List<SimpleAttribute> list = cDao.selectSimpleAttribute(fileID);

		Element simpleAttributes = newDoc.createElement("S100FC:S100_FC_SimpleAttributes");

		for (SimpleAttribute sa : list) {
			Element simpleAttribute = newDoc.createElement("S100FC:S100_FC_SimpleAttribute");

			if (!sa.name.equals("") && !sa.name.equals("null")) {
				Element name = newDoc.createElement("S100FC:name");
				name.setTextContent(sa.name);
				simpleAttribute.appendChild(name);
			}

			if (!sa.definition.equals("") && !sa.definition.equals("null")) {
				Element definition = newDoc.createElement("S100FC:definition");
				definition.setTextContent(sa.definition);
				simpleAttribute.appendChild(definition);
			}

			if (!sa.code.equals("") && !sa.code.equals("null")) {
				Element code = newDoc.createElement("S100FC:code");
				code.setTextContent(sa.code);
				simpleAttribute.appendChild(code);
			}

			if (!sa.alias.equals("") && !sa.alias.equals("null")) {
				Element alias = newDoc.createElement("S100FC:alias");
				alias.setTextContent(sa.alias);
				simpleAttribute.appendChild(alias);
			}

			if (!sa.valueType.equals("") && !sa.valueType.equals("null")) {
				Element valueType = newDoc.createElement("S100FC:valueType");
				valueType.setTextContent(sa.valueType);
				simpleAttribute.appendChild(valueType);
			}

			if (!sa.remarks.equals("") && !sa.remarks.equals("null")) {
				Element remarks = newDoc.createElement("S100FC:remarks");
				remarks.setTextContent(sa.remarks);
				simpleAttribute.appendChild(remarks);
			}

			List<ListedValue> subList = cDao.selectListedValue(sa.simpleAttribute_pk);

			Element listedValues = newDoc.createElement("S100FC:listedValues");

			for (int i = 0; i < subList.size(); i++) {
				Element listedValue = newDoc.createElement("S100FC:listedValue");

				if (subList.get(i).label != null && !subList.get(i).label.equals("")
						&& !subList.get(i).label.equals("null")) {
					Element label = newDoc.createElement("S100FC:label");
					label.setTextContent(subList.get(i).label);
					listedValue.appendChild(label);
				}
				if (subList.get(i).definition != null && !subList.get(i).definition.equals("")
						&& !subList.get(i).definition.equals("null")) {
					Element definition = newDoc.createElement("S100FC:definition");
					definition.setTextContent(subList.get(i).definition);
					listedValue.appendChild(definition);
				}
				if (subList.get(i).code != null && !subList.get(i).code.equals("")
						&& !subList.get(i).code.equals("null")) {
					Element code = newDoc.createElement("S100FC:code");
					code.setTextContent(subList.get(i).code);
					listedValue.appendChild(code);
				}
				listedValues.appendChild(listedValue);

				if (i == subList.size() - 1)
					simpleAttribute.appendChild(listedValues);
			}

			simpleAttributes.appendChild(simpleAttribute);
		}

		featureCatalogue.appendChild(simpleAttributes);
	}

	private void writeComplexAttributes(Document newDoc, Element featureCatalogue, String fileID) {
		List<ComplexAttribute> list = cDao.selectComplexAttribute(fileID);

		Element complexAttributes = newDoc.createElement("S100FC:S100_FC_ComplexAttributes");

		for (ComplexAttribute ca : list) {
			Element complexAttribute = newDoc.createElement("S100FC:S100_FC_ComplexAttribute");

			if (ca.name != null && !ca.name.equals("") && !ca.name.equals("null")) {
				Element name = newDoc.createElement("S100FC:name");
				name.setTextContent(ca.name);
				complexAttribute.appendChild(name);
			}

			if (ca.definition != null && !ca.definition.equals("") && !ca.definition.equals("null")) {
				Element definition = newDoc.createElement("S100FC:definition");
				definition.setTextContent(ca.definition);
				complexAttribute.appendChild(definition);
			}

			if (ca.code != null && !ca.code.equals("") && !ca.code.equals("null")) {
				Element code = newDoc.createElement("S100FC:code");
				code.setTextContent(ca.code);
				complexAttribute.appendChild(code);
			}

			if (ca.remarks != null && !ca.remarks.equals("") && !ca.remarks.equals("null")) {
				Element remarks = newDoc.createElement("S100FC:remarks");
				remarks.setTextContent(ca.remarks);
				complexAttribute.appendChild(remarks);
			}

			List<ComplexBinding> subList = cDao.selectComplexBinding(ca.complex_pk);

			for (ComplexBinding cb : subList) {
				Element subAttributeBinding = newDoc.createElement("S100FC:subAttributeBinding");
				subAttributeBinding.setAttribute("sequential",
						cb.sequential.toLowerCase().equals("t") ? "true" : "false");

				Element multiplicity = newDoc.createElement("S100FC:multiplicity");
				Element lower = newDoc.createElement("S100Base:lower");
				if (cb.lower != null && !cb.lower.equals(""))
					lower.setTextContent(cb.lower);
				Element upper = newDoc.createElement("S100Base:upper");
				if (cb.upper != null && !cb.upper.equals(""))
					upper.setTextContent(cb.upper);
				upper.setAttribute("xsi:nil", cb.nil.toLowerCase().equals("t") ? "true" : "false");
				upper.setAttribute("infinite", cb.infinite.toLowerCase().equals("t") ? "true" : "false");

				Element attribute = newDoc.createElement("S100FC:attribute");
				attribute.setAttribute("ref", cb.attribute);

				List<ComplexPermittedValue> permittedList = cDao.selectComplexPermittedValue(cb.complexBinding_pk);

				Element permittedValues = newDoc.createElement("S100FC:permittedValues");

				for (int i = 0; i < permittedList.size(); i++) {
					Element value = newDoc.createElement("S100FC:value");
					value.setTextContent(permittedList.get(i).value);

					permittedValues.appendChild(value);

					if (i == subList.size() - 1)
						subAttributeBinding.appendChild(permittedValues);
				}

				multiplicity.appendChild(lower);
				multiplicity.appendChild(upper);

				subAttributeBinding.appendChild(multiplicity);
				subAttributeBinding.appendChild(attribute);

				complexAttribute.appendChild(subAttributeBinding);
			}

			complexAttributes.appendChild(complexAttribute);
		}

		featureCatalogue.appendChild(complexAttributes);
	}

	private void writeRoles(Document newDoc, Element featureCatalogue, String fileID) {
		List<Role> list = cDao.selectRole(fileID);

		Element roles = newDoc.createElement("S100FC:S100_FC_Roles");
		for (Role ro : list) {
			Element role = newDoc.createElement("S100FC:S100_FC_Role");

			if (ro.name != null && !ro.name.equals("") && !ro.name.equals("null")) {
				Element name = newDoc.createElement("S100FC:name");
				name.setTextContent(ro.name);
				role.appendChild(name);
			}

			if (ro.definition != null && !ro.definition.equals("") && !ro.definition.equals("null")) {
				Element definition = newDoc.createElement("S100FC:definition");
				definition.setTextContent(ro.definition);
				role.appendChild(definition);
			}

			if (ro.code != null && !ro.code.equals("") && !ro.code.equals("null")) {
				Element code = newDoc.createElement("S100FC:code");
				code.setTextContent(ro.code);
				role.appendChild(code);
			}
			roles.appendChild(role);

		}

		featureCatalogue.appendChild(roles);

	}

	private void writeInformationAssociations(Document newDoc, Element featureCatalogue, String fileID) {
		List<InformationAssociation> list = cDao.selectInformationAssociation(fileID);

		Element informationAssociations = newDoc.createElement("S100FC:S100_FC_InformationAssociations");
		for (InformationAssociation ia : list) {
			Element informationAssociation = newDoc.createElement("S100FC:S100_FC_InformationAssociation");

			if (ia.name != null && !ia.name.equals("") && !ia.name.equals("null")) {
				Element name = newDoc.createElement("S100FC:name");
				name.setTextContent(ia.name);
				informationAssociation.appendChild(name);
			}

			if (ia.definition != null && !ia.definition.equals("") && !ia.definition.equals("null")) {
				Element definition = newDoc.createElement("S100FC:definition");
				definition.setTextContent(ia.definition);
				informationAssociation.appendChild(definition);
			}

			if (ia.code != null && !ia.code.equals("") && !ia.code.equals("null")) {
				Element code = newDoc.createElement("S100FC:code");
				code.setTextContent(ia.code);
				informationAssociation.appendChild(code);
			}

			if (ia.role != null && !ia.role.equals("") && !ia.role.equals("null")) {
				Element role = newDoc.createElement("S100FC:role");
				role.setAttribute("ref", ia.role);
				informationAssociation.appendChild(role);
			}
			informationAssociations.appendChild(informationAssociation);

		}

		featureCatalogue.appendChild(informationAssociations);

	}

	private void writeFeatureAssociations(Document newDoc, Element featureCatalogue, String fileID) {
		List<FeatureAssociation> list = cDao.selectFeatureAssociation(fileID);

		Element featureAssociations = newDoc.createElement("S100FC:S100_FC_FeatureAssociations");
		for (FeatureAssociation fa : list) {
			Element featureAssociation = newDoc.createElement("S100FC:S100_FC_FeatureAssociation");

			if (fa.name != null && !fa.name.equals("") && !fa.name.equals("null")) {
				Element name = newDoc.createElement("S100FC:name");
				name.setTextContent(fa.name);
				featureAssociation.appendChild(name);
			}

			if (fa.definition != null && !fa.definition.equals("") && !fa.definition.equals("null")) {
				Element definition = newDoc.createElement("S100FC:definition");
				definition.setTextContent(fa.definition);
				featureAssociation.appendChild(definition);
			}

			if (fa.code != null && !fa.code.equals("") && !fa.code.equals("null")) {
				Element code = newDoc.createElement("S100FC:code");
				code.setTextContent(fa.code);
				featureAssociation.appendChild(code);
			}

			if (!fa.remarks.equals("") && !fa.remarks.equals("null")) {
				Element remarks = newDoc.createElement("S100FC:remarks");
				remarks.setTextContent(fa.remarks);
				featureAssociation.appendChild(remarks);
			}

			if (fa.role1 != null && !fa.role1.equals("") && !fa.role1.equals("null")) {
				Element role1 = newDoc.createElement("S100FC:role");
				role1.setAttribute("ref", fa.role1);
				featureAssociation.appendChild(role1);
			}

			if (fa.role2 != null && !fa.role2.equals("") && !fa.role2.equals("null")) {
				Element role2 = newDoc.createElement("S100FC:role");
				role2.setAttribute("ref", fa.role2);
				featureAssociation.appendChild(role2);
			}

			featureAssociations.appendChild(featureAssociation);

		}

		featureCatalogue.appendChild(featureAssociations);

	}

	private void writeInformationTypes(Document newDoc, Element featureCatalogue, String fileID) {
		List<InformationType> list = cDao.selectInformationType(fileID);

		Element InformationTypes = newDoc.createElement("S100FC:S100_FC_InformationTypes");

		for (InformationType it : list) {
			Element InformationType = newDoc.createElement("S100FC:S100_FC_InformationType");

			if (it.name != null && !it.name.equals("") && !it.name.equals("null")) {
				Element name = newDoc.createElement("S100FC:name");
				name.setTextContent(it.name);
				InformationType.appendChild(name);
			}

			if (it.definition != null && !it.definition.equals("") && !it.definition.equals("null")) {
				Element definition = newDoc.createElement("S100FC:definition");
				definition.setTextContent(it.definition);
				InformationType.appendChild(definition);
			}

			if (!it.code.equals("") && !it.code.equals("null")) {
				Element code = newDoc.createElement("S100FC:code");
				code.setTextContent(it.code);
				InformationType.appendChild(code);
			}

			List<InformationBinding> subList = cDao.selectInformationBinding(it.informationType_pk);

			for (InformationBinding ib : subList) {
				Element attributeBinding = newDoc.createElement("S100FC:attributeBinding");
				attributeBinding.setAttribute("sequential", ib.sequential.toLowerCase().equals("t") ? "true" : "false");

				Element multiplicity = newDoc.createElement("S100FC:multiplicity");

				Element lower = newDoc.createElement("S100Base:lower");
				if (ib.lower != null && !ib.lower.equals(""))
					lower.setTextContent(ib.lower);

				Element upper = newDoc.createElement("S100Base:upper");
				if (ib.upper != null && !ib.upper.equals(""))
					upper.setTextContent(ib.upper);
				upper.setAttribute("xsi:nil", ib.nil.toLowerCase().equals("t") ? "true" : "false");
				upper.setAttribute("infinite", ib.infinite.toLowerCase().equals("t") ? "true" : "false");

				Element attribute = newDoc.createElement("S100FC:attribute");
				attribute.setAttribute("ref", ib.attribute);

				List<InformationPermittedValue> permittedList = cDao
						.selectInformationPermittedValue(ib.informationBinding_pk);

				Element permittedValues = newDoc.createElement("S100FC:permittedValues");

				for (int i = 0; i < permittedList.size(); i++) {
					Element value = newDoc.createElement("S100FC:value");
					value.setTextContent(permittedList.get(i).value);

					permittedValues.appendChild(value);

					if (i == subList.size() - 1)
						attributeBinding.appendChild(permittedValues);
				}

				multiplicity.appendChild(lower);
				multiplicity.appendChild(upper);

				attributeBinding.appendChild(multiplicity);
				attributeBinding.appendChild(attribute);

				InformationType.appendChild(attributeBinding);
			}

			InformationTypes.appendChild(InformationType);
		}

		featureCatalogue.appendChild(InformationTypes);
	}

	private void writeFeatureTypes(Document newDoc, Element featureCatalogue, String fileID) {
		List<FeatureType> list = cDao.selectFeatureType(fileID);

		Element featureTypes = newDoc.createElement("S100FC:S100_FC_FeatureTypes");
		for (FeatureType ft : list) {
			Element featureType = newDoc.createElement("S100FC:S100_FC_FeatureType");

			if (ft.name != null && !ft.name.equals("") && !ft.name.equals("null")) {
				Element name = newDoc.createElement("S100FC:name");
				name.setTextContent(ft.name);
				featureType.appendChild(name);
			}

			if (ft.definition != null && !ft.definition.equals("") && !ft.definition.equals("null")) {
				Element definition = newDoc.createElement("S100FC:definition");
				definition.setTextContent(ft.definition);
				featureType.appendChild(definition);
			}

			if (ft.code != null && !ft.code.equals("") && !ft.code.equals("null")) {
				Element code = newDoc.createElement("S100FC:code");
				code.setTextContent(ft.code);
				featureType.appendChild(code);
			}

			if (ft.remarks != null && !ft.remarks.equals("") && !ft.remarks.equals("null")) {
				Element remarks = newDoc.createElement("S100FC:remarks");
				remarks.setTextContent(ft.remarks);
				featureType.appendChild(remarks);
			}

			if (ft.alias != null && !ft.alias.equals("") && !ft.alias.equals("null")) {
				Element alias = newDoc.createElement("S100FC:alias");
				alias.setTextContent(ft.alias);
				featureType.appendChild(alias);
			}

			if (ft.featureusertype != null && !ft.featureusertype.equals("") && !ft.featureusertype.equals("null")) {
				Element featureUseType = newDoc.createElement("S100FC:featureUseType");
				featureUseType.setTextContent(ft.featureusertype);
				featureType.appendChild(featureUseType);
			}

			List<FeaturePermittedPrimitive> subList = cDao.selectFeaturePermittedPrimitive(ft.featureType_pk);

			for (FeaturePermittedPrimitive fp : subList) {
				Element permittedPrimitives = newDoc.createElement("S100FC:permittedPrimitives");
				permittedPrimitives.setTextContent(fp.permittedPrimitives);
				featureType.appendChild(permittedPrimitives);
			}

			List<AttributeBinding> attBindingList = cDao.selectAttributeBinding(ft.featureType_pk);

			for (AttributeBinding ab : attBindingList) {
				Element attributeBinding = newDoc.createElement("S100FC:attributeBinding");
				attributeBinding.setAttribute("sequential", ab.sequential.toLowerCase().equals("t") ? "true" : "false");

				Element multiplicity = newDoc.createElement("S100FC:multiplicity");
				Element lower = newDoc.createElement("S100Base:lower");
				if (ab.lower != null && !ab.lower.equals(""))
					lower.setTextContent(ab.lower);
				Element upper = newDoc.createElement("S100Base:upper");
				if (ab.upper != null && !ab.upper.equals(""))
					upper.setTextContent(ab.upper);
				upper.setAttribute("xsi:nil", ab.nil.toLowerCase().equals("t") ? "true" : "false");
				upper.setAttribute("infinite", ab.infinite.toLowerCase().equals("t") ? "true" : "false");

				Element attribute = newDoc.createElement("S100FC:attribute");
				attribute.setAttribute("ref", ab.attribute);

				List<FeaturePermittedValue> permittedList = cDao.selectFeaturePermittedValue(ab.attributeBinding_pk);

				Element permittedValues = newDoc.createElement("S100FC:permittedValues");

				for (int i = 0; i < permittedList.size(); i++) {
					Element value = newDoc.createElement("S100FC:value");
					value.setTextContent(permittedList.get(i).value);

					permittedValues.appendChild(value);

					if (i == permittedList.size() - 1)
						attributeBinding.appendChild(permittedValues);
				}

				multiplicity.appendChild(lower);
				multiplicity.appendChild(upper);

				attributeBinding.appendChild(multiplicity);
				attributeBinding.appendChild(attribute);

				featureType.appendChild(attributeBinding);
			}

			////// 1
			List<FeatureBinding> featBindingList = cDao.selectFeatureBinding(ft.featureType_pk);

			for (FeatureBinding fb : featBindingList) {
				Element featureBinding = newDoc.createElement("S100FC:featureBinding");
				featureBinding.setAttribute("roleType", fb.roleType);

				Element multiplicity = newDoc.createElement("S100FC:multiplicity");
				Element lower = newDoc.createElement("S100Base:lower");
				if (fb.lower != null && !fb.lower.equals(""))
					lower.setTextContent(fb.lower);
				Element upper = newDoc.createElement("S100Base:upper");
				if (fb.upper != null && !fb.upper.equals(""))
					upper.setTextContent(fb.upper);
				upper.setAttribute("xsi:nil", fb.nil.toLowerCase().equals("t") ? "true" : "false");
				upper.setAttribute("infinite", fb.infinite.toLowerCase().equals("t") ? "true" : "false");

				Element association = newDoc.createElement("S100FC:association");
				association.setAttribute("ref", fb.association);

				Element role = newDoc.createElement("S100FC:role");
				role.setAttribute("ref", fb.role);

				Element featuretype = newDoc.createElement("S100FC:featureType");
				featuretype.setAttribute("ref", fb.featureType);

				multiplicity.appendChild(lower);
				multiplicity.appendChild(upper);

				featureBinding.appendChild(multiplicity);
				featureBinding.appendChild(association);
				featureBinding.appendChild(role);
				featureBinding.appendChild(featuretype);

				featureType.appendChild(featureBinding);
			}

			///// 1

			featureTypes.appendChild(featureType);

		}

		featureCatalogue.appendChild(featureTypes);

	}

	/////////////////////

	public void writeExchangeCatalogue(List<Integer> idxList, String savePath) {
		int so_idx = sDao.getSaveOptionIDX();

		if (so_idx < 1)
			return;

		SaveOption so = sDao.getSelectSaveOption(so_idx);
		Element eleEC = null;

		List<String> dsList = new ArrayList<String>();
		for (int i = 0; i < idxList.size(); i++) {
			dsList.add(sDao.getSelectDatasetName(idxList.get(i)));
		}

		List<String> ecList = new ArrayList<String>();
		List<Node> ddmList = new ArrayList<Node>();

		for (int i = 0; i < idxList.size(); i++) {
			String fileName = sDao.getSelectSaveFileName(idxList.get(i));
			String[] token = fileName.split("/");

			String path = fileName.substring(0, fileName.length() - token[token.length - 1].length());

			File folder = new File(path);

			File[] files = folder.listFiles();

			Boolean check = true;

			for (File f : files) {
				if (f.isFile()) {
					if (f.getPath().toLowerCase().contains(".xml")) {
						String type = readXml(f.getPath());

						for (String str : ecList) {
							if (str == type)
								check = false;
						}

						if (type.contains("ExchangeCatalogue") && check) {
							check = false;
							ecList.add(type);
							eleEC = xmlDoc.getDocumentElement();
						}
					}
				}
			}

			if (!check) {
				if (eleEC == null)
					continue;

				for (int j = 0; j < eleEC.getChildNodes().getLength(); j++) {
					Node node = eleEC.getChildNodes().item(j);

					if (node.getNodeName().toLowerCase().contains("datasetdiscoverymetadata")) {
						for (int k = 0; k < node.getChildNodes().getLength(); k++) {
							Node _node = node.getChildNodes().item(k);
							if (_node.getNodeName().toLowerCase().contains("filename")) {
								for (String str : dsList) {
									if (str.equals(_node.getFirstChild().getNodeValue())) {
										ddmList.add(node);
										dsList.remove(str);
										break;
									}
								}
								break;
							}
						}
					}
				}
			}
		}

		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();

		DocumentBuilder parser;
		try {
			parser = f.newDocumentBuilder();
			Document newDoc = parser.newDocument();

			Element exchangeCatalogue = newDoc.createElement("S101_ExchangeCatalogue");
			exchangeCatalogue.setAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema");
			exchangeCatalogue.setAttribute("xmlns:S100XC", "http://www.iho.int/s100/xc");
			exchangeCatalogue.setAttribute("xmlns:gmd", "http://www.isotc211.org/2005/gmd");
			exchangeCatalogue.setAttribute("xmlns:gco", "http://www.isotc211.org/2005/gco");
			exchangeCatalogue.setAttribute("xmlns:gmx", "http://www.isotc211.org/2005/gmx");
			exchangeCatalogue.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
			exchangeCatalogue.setAttribute("xmlns:gml", "http://www.opengis.net/gml/3.2");
			exchangeCatalogue.setAttribute("xmlns:gss", "http://www.isotc211.org/2005/gss");
			exchangeCatalogue.setAttribute("xmlns:gsr", "http://www.isotc211.org/2005/gsr");
			exchangeCatalogue.setAttribute("xmlns:gts", "http://www.isotc211.org/2005/gts");
			exchangeCatalogue.setAttribute("schemaLocation", "http://www.iho.int/s100/xc ./S101_ExchangeCatalogue.xsd");

			//
			Element identifier = newDoc.createElement("S100XC:identifier");

			Element identifierChild1 = newDoc.createElement("S100XC:identifier");
			Element identifierChild2 = newDoc.createElement("S100XC:editionNumber");
			Element identifierChild3 = newDoc.createElement("S100XC:date");

			identifierChild1.setTextContent(so.identifier);
			identifierChild2.setTextContent(so.editionnumber);
			identifierChild3.setTextContent(so.i_date);

			identifier.appendChild(identifierChild1);
			identifier.appendChild(identifierChild2);
			identifier.appendChild(identifierChild3);

			exchangeCatalogue.appendChild(identifier);

			//
			Element contact = newDoc.createElement("S100XC:contact");

			Element organization = newDoc.createElement("S100XC:organization");
			organization.setTextContent(so.organization);
			contact.appendChild(organization);

			Element phone = newDoc.createElement("S100XC:phone");
			Element phoneChild1 = newDoc.createElement("gmd:voice");
			Element phoneChild1_child = newDoc.createElement("gco:CharacterString");
			Element phoneChild2 = newDoc.createElement("gmd:facsimile");
			Element phoneChild2_child = newDoc.createElement("gco:CharacterString");

			phone.setAttribute("isoType", "gmd:CI_Telephone");

			if (so.voice != "" || so.facsimile != "") {
				phoneChild1_child.setTextContent(so.voice);
				phoneChild1.appendChild(phoneChild1_child);
				phone.appendChild(phoneChild1);

				phoneChild2_child.setTextContent(so.facsimile);
				phoneChild2.appendChild(phoneChild2_child);
				phone.appendChild(phoneChild2);

				contact.appendChild(phone);
			}

			Element address = newDoc.createElement("S100XC:address");
			address.setAttribute("isoType", "gmd:CI_Address");

			Element addressChild1 = newDoc.createElement("gmd:deliveryPoint");
			Element addressChild1_1 = newDoc.createElement("gco:CharacterString");
			Element addressChild2 = newDoc.createElement("gmd:city");
			Element addressChild2_1 = newDoc.createElement("gco:CharacterString");
			Element addressChild3 = newDoc.createElement("gmd:administrativeArea");
			Element addressChild3_1 = newDoc.createElement("gco:CharacterString");
			Element addressChild4 = newDoc.createElement("gmd:postalCode");
			Element addressChild4_1 = newDoc.createElement("gco:CharacterString");
			Element addressChild5 = newDoc.createElement("gmd:country");
			Element addressChild5_1 = newDoc.createElement("gco:CharacterString");
			Element addressChild6 = newDoc.createElement("gmd:electronicMailAddress");
			Element addressChild6_1 = newDoc.createElement("gco:CharacterString");

			if (so.deliverypoint != "" || so.city != "" || so.administrativearea != "" || so.postalcode != ""
					|| so.country != "" || so.electronicmailaddress != "") {

				addressChild1_1.setTextContent(so.deliverypoint);
				addressChild1.appendChild(addressChild1_1);
				address.appendChild(addressChild1);

				addressChild2_1.setTextContent(so.city);
				addressChild2.appendChild(addressChild2_1);
				address.appendChild(addressChild2);

				addressChild3_1.setTextContent(so.administrativearea);
				addressChild3.appendChild(addressChild3_1);
				address.appendChild(addressChild3);

				addressChild4_1.setTextContent(so.postalcode);
				addressChild4.appendChild(addressChild4_1);
				address.appendChild(addressChild4);

				addressChild5_1.setTextContent(so.country);
				addressChild5.appendChild(addressChild5_1);
				address.appendChild(addressChild5);

				addressChild6_1.setTextContent(so.electronicmailaddress);
				addressChild6.appendChild(addressChild6_1);
				address.appendChild(addressChild6);

				contact.appendChild(address);
			}

			exchangeCatalogue.appendChild(contact);

			//
			Element productSpecification = newDoc.createElement("S100XC:productSpecification");

			Element productSpecificationChild1 = newDoc.createElement("S100XC:name");
			Element productSpecificationChild2 = newDoc.createElement("S100XC:version");
			Element productSpecificationChild3 = newDoc.createElement("S100XC:date");

			if (so.name != "" || so.version != "" || so.p_date != "") {
				productSpecificationChild1.setTextContent(so.name);
				productSpecificationChild2.setTextContent(so.version);
				productSpecificationChild3.setTextContent(so.p_date);

				productSpecification.appendChild(productSpecificationChild1);
				productSpecification.appendChild(productSpecificationChild2);
				productSpecification.appendChild(productSpecificationChild3);

				exchangeCatalogue.appendChild(productSpecification);
			}

			//
			Element exchangeCatalogueName = newDoc.createElement("S100XC:exchangeCatalogueName");
			Element exchangeCatalogueDescription = newDoc.createElement("S100XC:exchangeCatalogueDescription");
			Element exchangeCatalogueComment = newDoc.createElement("S100XC:exchangeCatalogueComment");
			Element compressionFlag = newDoc.createElement("S100XC:compressionFlag");
			Element algorithmMethod = newDoc.createElement("S100XC:algorithmMethod");
			Element sourceMedia = newDoc.createElement("S100XC:sourceMedia");
			Element replacedData = newDoc.createElement("S100XC:replacedData");
			Element dataReplacement = newDoc.createElement("S100XC:dataReplacement");

			exchangeCatalogueName.setTextContent(so.exchangecataloguename);
			exchangeCatalogueDescription.setTextContent(so.exchangecataloguedescription);
			exchangeCatalogue.appendChild(exchangeCatalogueName);
			exchangeCatalogue.appendChild(exchangeCatalogueName);

			if (so.exchangecataloguecomment != "") {
				exchangeCatalogueComment.setTextContent(so.exchangecataloguecomment);
				exchangeCatalogue.appendChild(exchangeCatalogueComment);
			}
			if (so.compressionFlag != "") {
				compressionFlag.setTextContent(so.compressionFlag);
				exchangeCatalogue.appendChild(compressionFlag);
			}
			if (so.algorithmMethod != "") {
				algorithmMethod.setTextContent(so.algorithmMethod);
				exchangeCatalogue.appendChild(algorithmMethod);
			}
			if (so.sourceMedia != "") {
				sourceMedia.setTextContent(so.sourceMedia);
				exchangeCatalogue.appendChild(sourceMedia);
			}
			if (so.replacedData != "") {
				replacedData.setTextContent(so.replacedData);
				exchangeCatalogue.appendChild(replacedData);
			}
			if (so.dataReplacement != "") {
				dataReplacement.setTextContent(so.dataReplacement);
				exchangeCatalogue.appendChild(dataReplacement);
			}

			for (int i = 0; i < ddmList.size(); i++) {
				Node tempNode = newDoc.importNode(ddmList.get(i), true);
				exchangeCatalogue.appendChild(tempNode);
			}
			//
			newDoc.appendChild(exchangeCatalogue);

			DOMSource xmlDOM = new DOMSource(newDoc);
			StreamResult xmlFile = new StreamResult(new File(savePath + so.exchangecataloguename));
			TransformerFactory.newInstance().newTransformer().transform(xmlDOM, xmlFile);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int readExchangeCatalogue() {

		Element eleEC = xmlDoc.getDocumentElement();

		ExchangeCatalogue ec = new ExchangeCatalogue();

		ec.ec_pk = eDao.getExchangeIDX() + 1;

		ec.title = eleEC.getNodeName();

		//
		int queryCheck = eDao.insertExchangeCatalogue(ec);
		if (queryCheck < 0) {

			return -1;
		}

		// ExchangeCatlaouge Namespace..
		NamedNodeMap ecAttrMap = eleEC.getAttributes();
		for (int i = 0; i < ecAttrMap.getLength(); i++) {
			Node ecAttr = ecAttrMap.item(i);

			ExchangeCatalogueAttribute eca = new ExchangeCatalogueAttribute();

			eca.ec_fk = ec.ec_pk;
			eca.title = ecAttr.getNodeName();
			eca.value = ecAttr.getNodeValue();

			queryCheck = eDao.insertExchangeCatalogueAttribute(eca);
			if (queryCheck < 0) {

				return -1;
			}
		}

		// CHILD NODE
		for (int i = 0; i < eleEC.getChildNodes().getLength(); i++) {

			if (eleEC.getChildNodes().item(i).getNodeType() != 1)
				continue;

			Node node = eleEC.getChildNodes().item(i);

			if (node.getChildNodes().getLength() == 1) {
				SimpleA simple = new SimpleA();
				simple.ec_fk = ec.ec_pk;
				simple.title = node.getNodeName();
				simple.value = node.getChildNodes().item(0).getNodeValue();

				if (eDao.insertSimpleA(simple) < 0) {
					return -1;
				}
			} else if (node.getChildNodes().getLength() > 1) {
				ComplexA complex = new ComplexA();
				complex.c_pk = eDao.getComplexaIDX() + 1;
				complex.ec_fk = ec.ec_pk;
				complex.title = node.getNodeName();

				if (eDao.insertComplexA(complex) < 0) {
					return -1;
				}

				ComplexChildLoop(eDao, complex.c_pk, -1, node);
			}
		}

		return 0;
	}

	int ComplexChildLoop(ExchangeDao dao, int c_fk, int parentID, Node node) {
		boolean bCheck = true;

		if (node.getNodeName().toLowerCase().contains("boundingbox")) {
			String west, east, south, north;
			west = east = south = north = "";

			for (int j = 0; j < node.getChildNodes().getLength(); j++) {
				Node bNode = node.getChildNodes().item(j);
				if (bNode.getNodeName().toLowerCase().contains("west"))
					west = bNode.getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
				else if (bNode.getNodeName().toLowerCase().contains("east"))
					east = bNode.getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
				else if (bNode.getNodeName().toLowerCase().contains("south"))
					south = bNode.getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
				else if (bNode.getNodeName().toLowerCase().contains("north"))
					north = bNode.getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
			}
			if (west != null && east != null && south != null && north != null && west != "" && east != ""
					&& south != "" && north != "") {
				ComplexGeometry cg = new ComplexGeometry();
				cg.cc_fk = parentID;
				cg.geom = String.format("%s %s, %s %s, %s %s, %s %s, %s %s", west, south, east, south, east, north,
						west, north, west, south);

				if (eDao.insertComplexGeometry(cg) < 0)
					return -1;

				bCheck = false;
			}

		}
		if (bCheck) {
			for (int i = 0; i < node.getChildNodes().getLength(); i++) {
				Node child = node.getChildNodes().item(i);

				ComplexChild cc = new ComplexChild();
				cc.cc_pk = eDao.getComplexChildIDX() + 1;
				cc.c_fk = c_fk;
				cc.cc_fk = parentID;
				cc.title = child.getNodeName();
				cc.value = child.getNodeValue();

				if (parentID == -1) {
					if (eDao.insertComplexChild1(cc) < 0)
						return -1;
				} else {
					if (eDao.insertComplexChild2(cc) < 0)
						return -1;
				}

				// Insert Attribute
				NamedNodeMap ccAttrMap = child.getAttributes();
				if (ccAttrMap != null)
					for (int j = 0; j < ccAttrMap.getLength(); j++) {
						Node ccAttr = ccAttrMap.item(j);

						ComplexChildAttribute cca = new ComplexChildAttribute();

						cca.cc_fk = cc.cc_pk;
						cca.title = ccAttr.getNodeName();
						cca.value = ccAttr.getNodeValue();

						if (eDao.insertComplexChildAttribute(cca) < 0)
							return -1;
					}

				// Insert Child
				if (ComplexChildLoop(eDao, c_fk, cc.cc_pk, child) == -1)
					return -1;
			}
		}
		return 0;
	}

	public int writeExchangeCatalogue(int ec_pk) {
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = null;

		try {
			parser = f.newDocumentBuilder();
			Document newDoc = parser.newDocument();

			String title = eDao.getExchangeCatalogueTitle(ec_pk);
			Element exchangeCatalogue = newDoc.createElement(title);

			List<ExchangeCatalogueAttribute> ecaList = eDao.getExchangeCatalogueAttribute(ec_pk);
			for (ExchangeCatalogueAttribute eca : ecaList) {
				exchangeCatalogue.setAttribute(eca.title, eca.value);
			}

			// simple
			List<SimpleA> sList = eDao.getSimpleA(ec_pk);
			for (SimpleA sim : sList) {
				Element simEle = newDoc.createElement(sim.title);
				simEle.setTextContent(sim.value);

				exchangeCatalogue.appendChild(simEle);
			}

			// complex
			List<ComplexA> cList = eDao.getComplexA(ec_pk);
			for (ComplexA com : cList) {
				if (com.title.contains("#comment"))
					continue;

				Element comEle = newDoc.createElement(com.title);

				List<ComplexChild> ccList = eDao.getComplexChildRoot(com.c_pk);
				for (ComplexChild cc : ccList) {
					if (cc.title.contains("#comment"))
						continue;

					Element ccEle = newDoc.createElement(cc.title);

					// attribute..
					List<ComplexChildAttribute> caList = eDao.getComplexChildAttribute(cc.cc_pk);
					for (ComplexChildAttribute ca : caList) {
						ccEle.setAttribute(ca.title, ca.value);
					}

					// loop
					getComplexChild(eDao, newDoc, ccEle, cc);

					comEle.appendChild(ccEle);
				}
				exchangeCatalogue.appendChild(comEle);
			}

			newDoc.appendChild(exchangeCatalogue);

			DOMSource xmlDOM = new DOMSource(newDoc);
			StreamResult xmlFile = new StreamResult(
					new File("C:\\\\Users\\\\SHD\\\\eclipse-workspace\\\\_Ex\\\\WebContent\\\\temp.xml"));
			TransformerFactory.newInstance().newTransformer().transform(xmlDOM, xmlFile);

		} catch (Exception e) {
			// TODO: handle exception

			return -1;
		}

		return 0;

	}

	void getComplexChild(ExchangeDao dao, Document doc, Element ele, ComplexChild cc) {
		if (cc.title.toLowerCase().contains("boundingbox")) {
			Boundary boundary = eDao.getBoundary(cc.cc_pk);
			Element westEle = doc.createElement("gmd:westBoundLongitude");
			Element wdecEle = doc.createElement("gco:Decimal");
			wdecEle.setTextContent(boundary.minX);
			westEle.appendChild(wdecEle);

			Element eastEle = doc.createElement("gmd:eastBoundLongitude");
			Element edecEle = doc.createElement("gco:Decimal");
			edecEle.setTextContent(boundary.maxX);
			eastEle.appendChild(edecEle);

			Element southEle = doc.createElement("gmd:southBoundLatitude");
			Element sdecEle = doc.createElement("gco:Decimal");
			sdecEle.setTextContent(boundary.minY);
			southEle.appendChild(sdecEle);

			Element northEle = doc.createElement("gmd:northBoundLatitude");
			Element ndecEle = doc.createElement("gco:Decimal");
			ndecEle.setTextContent(boundary.maxY);
			northEle.appendChild(ndecEle);

			ele.appendChild(westEle);
			ele.appendChild(eastEle);
			ele.appendChild(southEle);
			ele.appendChild(northEle);
			return;
		}

		List<ComplexChild> ccList = eDao.getComplexChild(cc.cc_pk);

		for (ComplexChild complexChild : ccList) {
			if (complexChild.title.contains("#text") && complexChild.value == "")
				continue;
			else if (complexChild.title.contains("#text") && complexChild.value != "") {
				ele.setTextContent(complexChild.value);
				continue;
			}

			Element ccEle = doc.createElement(complexChild.title);

			// attribute..
			List<ComplexChildAttribute> caList = eDao.getComplexChildAttribute(complexChild.cc_pk);
			for (ComplexChildAttribute ca : caList) {
				ccEle.setAttribute(ca.title, ca.value);
			}

			getComplexChild(dao, doc, ccEle, complexChild);

			ele.appendChild(ccEle);
		}

	}


}
