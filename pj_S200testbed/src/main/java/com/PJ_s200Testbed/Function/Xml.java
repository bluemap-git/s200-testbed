package com.PJ_s200Testbed.Function;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.PJ_s200Testbed.model.Association;
import com.PJ_s200Testbed.model.Attribute;
import com.PJ_s200Testbed.model.BoundedBy;
import com.PJ_s200Testbed.model.ComplexA;
import com.PJ_s200Testbed.model.ComplexChild;
import com.PJ_s200Testbed.model.ComplexChildAttribute;
import com.PJ_s200Testbed.model.ComplexGeometry;
import com.PJ_s200Testbed.model.DataSet;
import com.PJ_s200Testbed.model.DatasetIdentificationInformation;
import com.PJ_s200Testbed.model.DatasetStructureInformation;
import com.PJ_s200Testbed.model.ExchangeCatalogue;
import com.PJ_s200Testbed.model.ExchangeCatalogueAttribute;
import com.PJ_s200Testbed.model.Feature;
import com.PJ_s200Testbed.model.Geometry;
import com.PJ_s200Testbed.model.SimpleA;
import com.PJ_s200Testbed.persistence.CatalogueDAO;

public class Xml {
	private Document xmlDoc;
	private String imgPath;
	private String saveImgPath;

	private CatalogueDAO cDao;

	public Xml(CatalogueDAO cDao) {
		this.cDao = cDao;
		xmlDoc = null;
		// dataSetDao = DataSetDao.getInstance();
		// catalogueDao = CatalogueDao.getInstance();
		// exchangeDao = ExchangeDao.getInstance();

		// dataSetDao.closeSession();
		// catalogueDao.closeSession();
		// exchangeDao.closeSession();
		// imgPath = "";
	}

//	//
////	private void CreateImgPath(String _imgPath1, String _imgPath2, String _saveImgPath) {
////		String[] temp = _imgPath1.split("/");
////		String[] temp2 = _imgPath2.split("/");
////		imgPath = _imgPath1.substring(0, _imgPath1.length() - temp[temp.length - 1].length()) + temp2[temp2.length - 1]
////				+ "/";
////		saveImgPath = _saveImgPath + "img";
////
////		File folder = new File(saveImgPath);
////
////		if (!folder.mkdirs()) {
////			return;
////		}
////	}
////
////	private void CopyImg(String fileName) {
////		try {
////			FileInputStream fis = new FileInputStream(imgPath + fileName);
////			FileOutputStream fos = new FileOutputStream(saveImgPath + "/" + fileName);
////
////			int data = 0;
////			while ((data = fis.read()) != -1) {
////				fos.write(data);
////			}
////			fis.close();
////			fos.close();
////
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////	}
//
//	//
//	public String readXml(String path) {
//		try {
//			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
//
//			DocumentBuilder parser = f.newDocumentBuilder();
//
//			xmlDoc = parser.parse(path);
//
//			Element root = xmlDoc.getDocumentElement();
//
//			return root.getTagName();
//
//		} catch (Exception e) {
//
//			
//			  return null;
//			 
//		}
//	}
//
//	// Read _ DataSet
//	public int readDataSet(int catalogue_fk) {
//
//		dataSetDao.openSession();
//
//		Element eleDS = xmlDoc.getDocumentElement();
//
//		DataSet dataSet = new DataSet();
//		dataSet.catalogue_fk = catalogue_fk;
//		dataSet.ds_idx = dataSetDao.getDataSetIDX() + 1;
//		dataSet.id = eleDS.getAttribute("gml:id");
//		
//		int queryCheck = dataSetDao.insertDataSet(dataSet);
//
//		if (queryCheck < 0)
//			return -1;
//
//		Map<Integer, Integer> idxList = new HashMap<Integer, Integer>();
//		Map<String, String> emp = new HashMap<String, String>();
//
//		boolean check = false;
//		
//		int length = eleDS.getChildNodes().getLength() - 1;
//		for (int i = 0; i < eleDS.getChildNodes().getLength(); i++) {
//
//			/*
//			 * if (eleDS.getChildNodes().item(i).getNodeType() != 1) continue;
//			 */
//
//			// Element bookEle = (Element)bookNode;
//			// Element eleChild = (Element) eleDS.getChildNodes().item(i);
//
//			Node node = eleDS.getChildNodes().item(i);
//			String str = node.getNodeName();
//			if(str == "#text") {
//				if (i == length && !check) {
//					i = 0;
//					check = true;
//				}
//				continue;
//			}
//			if (str.contains("boundedBy") && !check) {
//				readBoundedBy(node, dataSet.ds_idx);
//			} else if (str.contains("DatasetIdentificationInformation") && !check) {
//				readDatasetIdentificationInformation(node, dataSet.ds_idx);
//			} else if (str.contains("DatasetStructureInformation") && !check) {
//				readDatasetStructureInformation(node, dataSet.ds_idx);
//			} else if (str.contains("member") || str.contains("imember")) {
//				readFeature(node, dataSet.ds_idx, check, idxList, emp, i);
//			}
//
//			if (i == length&& !check) {
//				i = 0;
//				check = true;
//			}
//		}
//
//		dataSetDao.closeSession();
//
//		return dataSet.ds_idx;
//	}
//
//	private void readBoundedBy(Node node, int ds_idx) {
//		Node _node;
//
//		if (node.getChildNodes().item(0).getNodeName().contains("#"))
//			_node = node.getChildNodes().item(1);
//		else
//			_node = node.getChildNodes().item(0);
//
//		BoundedBy bb = new BoundedBy();
//		bb.ds_idx = ds_idx;
//		bb.srsname = _node.getAttributes().getNamedItem("srsName").getNodeValue();
//
//		for (int i = 0; i < _node.getChildNodes().getLength(); i++) {
//			Node childNode = _node.getChildNodes().item(i);
//
//			if (childNode.getNodeName().equals("gml:lowerCorner"))
//				bb.lowercorner = childNode.getFirstChild().getNodeValue();
//			else if (childNode.getNodeName().equals("gml:upperCorner"))
//				bb.uppercorner = childNode.getFirstChild().getNodeValue();
//		}
//
//		dataSetDao.insertBoundedBy(bb);
//	}
//
//	private void readDatasetIdentificationInformation(Node node, int ds_idx) {
//		DatasetIdentificationInformation di = new DatasetIdentificationInformation();
//		di.ds_idx = ds_idx;
//
//		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
//			if (node.getChildNodes().item(i).getNodeName().equals("S100:encodingSpecification"))
//				di.encodingSpecification = node.getChildNodes().item(i).getTextContent();
//			else if (node.getChildNodes().item(i).getNodeName().equals("S100:encodingSpecificationEdition"))
//				di.encodingSpecificationEdition = node.getChildNodes().item(i).getTextContent();
//			else if (node.getChildNodes().item(i).getNodeName().equals("S100:productIdentifier"))
//				di.productIdentifier = node.getChildNodes().item(i).getTextContent();
//			else if (node.getChildNodes().item(i).getNodeName().equals("S100:productEdition"))
//				di.productEdition = node.getChildNodes().item(i).getTextContent();
//			else if (node.getChildNodes().item(i).getNodeName().equals("S100:applicationProfile"))
//				di.applicationProfile = node.getChildNodes().item(i).getTextContent();
//			else if (node.getChildNodes().item(i).getNodeName().equals("S100:datasetFileIdentifier"))
//				di.datasetFileIdentifier = node.getChildNodes().item(i).getTextContent();
//			else if (node.getChildNodes().item(i).getNodeName().equals("S100:datasetTitle"))
//				di.datasetTitle = node.getChildNodes().item(i).getTextContent();
//			else if (node.getChildNodes().item(i).getNodeName().equals("S100:datasetReferenceDate"))
//				di.datasetReferenceDate = node.getChildNodes().item(i).getTextContent();
//			else if (node.getChildNodes().item(i).getNodeName().equals("S100:datasetLanguage"))
//				di.datasetLanguage = node.getChildNodes().item(i).getTextContent();
//			else if (node.getChildNodes().item(i).getNodeName().equals("S100:datasetTopicCategory"))
//				di.datasetTopicCategory = node.getChildNodes().item(i).getTextContent();
//		}
//
//		dataSetDao.insertDatasetIdentificationInformation(di);
//	}
//
//	private void readDatasetStructureInformation(Node node, int ds_idx) {
//		DatasetStructureInformation ds = new DatasetStructureInformation();
//		ds.ds_idx = ds_idx;
//
//		ds.datasetCoordOriginX = "null";
//		ds.datasetCoordOriginY = "null";
//		ds.datasetCoordOriginZ = "null";
//		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
//			if (node.getChildNodes().item(i).getNodeName().equals("S100:datasetCoordOriginX"))
//				ds.datasetCoordOriginX = node.getChildNodes().item(i).getTextContent();
//			else if (node.getChildNodes().item(i).getNodeName().equals("S100:datasetCoordOriginY"))
//				ds.datasetCoordOriginY = node.getChildNodes().item(i).getTextContent();
//			else if (node.getChildNodes().item(i).getNodeName().equals("S100:datasetCoordOriginZ"))
//				ds.datasetCoordOriginZ = node.getChildNodes().item(i).getTextContent();
//			else if (node.getChildNodes().item(i).getNodeName().equals("S100:coordMultFactorX"))
//				ds.coordMultFactorX = node.getChildNodes().item(i).getTextContent();
//			else if (node.getChildNodes().item(i).getNodeName().equals("S100:coordMultFactorY"))
//				ds.coordMultFactorY = node.getChildNodes().item(i).getTextContent();
//			else if (node.getChildNodes().item(i).getNodeName().equals("S100:coordMultFactorZ"))
//				ds.coordMultFactorZ = node.getChildNodes().item(i).getTextContent();
//		}
//
//		dataSetDao.insertDatasetStructureInformation(ds);
//	}
//
//	private void readFeature(Node node, int ds_idx, Boolean check, Map<Integer, Integer> idxList,
//			Map<String, String> emp, int i) {
//		// �ּ��� ������ ù��° �ڽĳ�尡 �ּ��� �Ǳ� ������ �������� �Ѱ��ش�.
//		int startIdx = 0;
//		int f_idx = 0;
//
//		if (node.getChildNodes().item(startIdx).getNodeType() != 1)
//			startIdx++;
//
//		if (node.getChildNodes().item(startIdx).getNodeName().contains("#comment"))
//			startIdx++;
//
//		if (node.getChildNodes().item(startIdx).getNodeType() != 1)
//			startIdx++;
//
//		// feature Insert
//		if (!check) {
//			f_idx = dataSetDao.getFeatureIDX() + 1;
//			idxList.put(i, f_idx);
//			Feature feature = new Feature();
//			feature.ds_idx = ds_idx;
//			feature.f_idx = f_idx;
//			feature.featuretype = node.getChildNodes().item(startIdx).getNodeName();
//			dataSetDao.insertFeature(feature);
//
//			// feature�� ID���� f_idx�� ����
//			emp.put(node.getChildNodes().item(startIdx).getAttributes().getNamedItem("gml:id") != null
//					? node.getChildNodes().item(startIdx).getAttributes().getNamedItem("gml:id").getNodeValue()
//					: null, String.valueOf(f_idx));
//		}
//
//		// imember�� geometry�� imember�� �ִ´�
//		if (node.getNodeName().contains("imember") & !check)
//			dataSetDao.updateFeatureTypeImember(f_idx);
//
//		// feature Child
//		for (int j = 0; j < (node.getChildNodes().item(startIdx)).getChildNodes().getLength(); j++) {
//			// Element subElement =
//			// (Element)((Element)ele.getChildNodes().item(startIdx)).getChildNodes().item(j);
//			Node _node = node.getChildNodes().item(startIdx).getChildNodes().item(j);
//
//			if (_node.getNodeName().contains("Association") & check) {
//				Association assci = new Association();
//
//				assci.f_idx = idxList.get(i);
//				assci.xid = _node.getAttributes().getNamedItem("gml:id") != null
//						? _node.getAttributes().getNamedItem("gml:id").getNodeValue()
//						: "null";
//				assci.xhref = _node.getAttributes().getNamedItem("xlink:href") != null
//						? emp.get(_node.getAttributes().getNamedItem("xlink:href").getNodeValue().replace("#", ""))
//						: "null";
//				assci.xrole = _node.getAttributes().getNamedItem("xlink:role") != null
//						? _node.getAttributes().getNamedItem("xlink:role").getNodeValue()
//						: "null";
//
//				dataSetDao.insertAssociation(assci);
//			} else if ((_node.getNodeName().contains("parent") || _node.getNodeName().contains("child")) & check) {
//				Association assci = new Association();
//
//				assci.f_idx = idxList.get(i);
//				assci.xid = _node.getAttributes().getNamedItem("xlink:title") != null
//						? _node.getAttributes().getNamedItem("xlink:title").getNodeValue()
//						: "null";
//				assci.xhref = _node.getAttributes().getNamedItem("xlink:href") != null
//						? emp.get(_node.getAttributes().getNamedItem("xlink:href").getNodeValue().replace("#", ""))
//						: "null";
//				assci.xrole = _node.getAttributes().getNamedItem("xlink:role") != null
//						? _node.getAttributes().getNamedItem("xlink:role").getNodeValue()
//						: "null";
//
//				dataSetDao.insertAssociation(assci);
//			}
//
//			/**
//			 * ����� ����Լ����� ó���ص� �ɰͰ���. ������ DB�� ���� �ʹ� ���� ���°� �����Ƿ�
//			 * OutXml���� ���������� ó���Ѵ�. ���� ���� else if geometry�� ������̶��
//			 * OutXml���� ������� �ٲ�� �Ұ�. ����� update�� ���ܾ��ϳ�..?
//			 **/
//			else if (_node.getNodeName().contains("geometry") & !check) {
//				String[] token = geomLoop(_node.getLastChild().getParentNode());
//
//				if (token != null && token.length > 1) {
//					String value = "";
//					String startValue = "";
//					String endValue = "";
//
//					int k = 0;
//
//					while (true) {
//						if (k >= token.length - 1) {
//							if (!startValue.contains(endValue))
//								value += ", " + startValue;
//							break;
//						}
//
//						if (token[k].equals("") || token[k].equals(" ")) {
//							k++;
//							continue;
//						}
//
//						if (value.equals("")) {
//							value += token[k++] + " " + token[k++];
//							startValue = token[k - 2] + " " + token[k - 1];
//						} else {
//							value += ", " + token[k++] + " " + token[k++];
//							endValue = token[k - 2] + " " + token[k - 1];
//						}
//
//					}
//
//					if (value.equals(startValue)) { // ���� ������ �ٸ��� ������ 3���� ���� �������Ѵ�..
//
//						Geometry geom = new Geometry();
//						geom.idx = f_idx;
//						geom.srid = "4326";
//						geom.value = value;
//						geom.type = "POINT";
//
//						dataSetDao.updateFeatureTypePoint(f_idx);
//
//						dataSetDao.insertPoint(geom);
//					} else {
//						Geometry geom = new Geometry();
//						geom.idx = f_idx;
//						geom.srid = "4326";
//						geom.value = value;
//						geom.type = "POLYGON";
//
//						dataSetDao.updateFeatureTypePolygon(f_idx);
//
//						dataSetDao.insertPolygon(geom);
//					}
//					/*
//					 * } else if (_node.getNodeName().contains("Polyline")) {
//					 * //db.updateFeature(f_idx, "Polyline");
//					 * 
//					 * //db.insertGeom("POINT", "26910", value, f_idx); //sql = string.
//					 * Format("INSERT INTO  polyline (f_idx, geom) VALUES ({0}, ST_GeomFromText('LINESTRING({1})', 4269))"
//					 * , f_idx, value); //startSQL(sql); }
//					 */
//				}
//				// continue;
//			}
//
//			else if (!check) {
//				int a_idx = dataSetDao.getAttributeIDX() + 1;
//				for (int k = 0; k < _node.getChildNodes().getLength(); k++) {
//					if (k == 0) {
//						Attribute attr = new Attribute();
//						attr.f_idx = f_idx;
//						attr.a_idx = a_idx;
//						attr.name = _node.getNodeName();
//						if (_node.getChildNodes().getLength() == 1)
//							attr.value = _node.getFirstChild().getNodeValue().replace("'", "\'||CHR(39)||\'");
//						else if (_node.getFirstChild().getNodeType() != 1)
//							attr.value = _node.getChildNodes().item(1).getFirstChild() != null
//									? _node.getChildNodes().item(1).getFirstChild().getNodeValue().replace("'",
//											"\'||CHR(39)||\'")
//									: _node.getFirstChild().getNodeValue().replace("'", "\'||CHR(39)||\'");
//						else
//							attr.value = _node.getFirstChild().getFirstChild().getNodeValue().replace("'",
//									"\'||CHR(39)||\'");
//						attr.parents = "null";
//						attr.attributetype = _node.getParentNode().getNodeName();
//
//						dataSetDao.insertAttribute(attr);
//					}
//					attributeLoop(f_idx, _node.getChildNodes().item(k), a_idx);
//				}
//			}
//
//		}
//	}
//
//	private String[] geomLoop(Node node) {
//		String[] geom;
//
//		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
//			Node _node = node.getChildNodes().item(i);
//
//			if (_node.getNodeValue() != null) {
//				String temp = _node.getNodeValue().replace("\n", " ");
//				temp = temp.replace("\t", " ");
//				geom = temp.split(" ");
//
//				if (geom != null && geom.length >= 2)
//					return geom;
//			}
//
//			else if (_node.getChildNodes().getLength() > 0) {
//				geom = geomLoop(_node);
//
//				if (geom != null && geom.length >= 2)
//					return geom;
//			}
//		}
//		return null;
//	}
//
//	private void attributeLoop(int f_idx, Node node, int parents) {
//		int a_idx = dataSetDao.getAttributeIDX() + 1;
//
//		Attribute attr = new Attribute();
//		attr.f_idx = f_idx;
//		attr.a_idx = a_idx;
//		attr.name = node.getNodeName();
//		attr.value = node.getFirstChild() != null ? (node.getFirstChild().getNodeValue() != null
//				? node.getFirstChild().getNodeValue().replace("'", "\'||CHR(39)||\'")
//				: "") : "";
//		attr.parents = String.valueOf(parents);
//		attr.attributetype = node.getParentNode().getNodeName();
//
//		dataSetDao.insertAttribute(attr);
//
//		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
//			attributeLoop(f_idx, node.getChildNodes().item(i), a_idx);
//		}
//		if (node.getChildNodes().getLength() == 0)
//			return;
//	}
//	////

	// Write _ DataSet
	public void writeDataSet(String datasetIDX, String filePath, String fileName) {

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

			// XML 파일로 쓰기
//			TransformerFactory transformerFactory = TransformerFactory.newInstance();
//
//			Transformer transformer = transformerFactory.newTransformer();
//			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // 정렬 스페이스4칸
//			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // 들여쓰기
//			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes"); // doc.setXmlStandalone(true); 했을때 붙어서
//																				// 출력되는부분 개행

			// DOMSource source = new DOMSource(doc);
			// StreamResult result = new StreamResult(new FileOutputStream(new
			// File("D://tmp/book.xml")));

			//transformer.transform(xmlDOM, xmlFile);
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
		BoundedBy bb = cDao.selectBoundedBy(fileID);

		if (bb == null)
			return;
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
		DatasetIdentificationInformation di = cDao.selectDatasetIdentificationInformation(fileID);
		if (di == null)
			return;
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
		DatasetStructureInformation ds = cDao.selectDatasetStructureInformation(fileID);
		if (ds == null)
			return;

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

	public void writeFeature(Document newDoc, Element dataSet, String fileID) {
		List<Feature> list = cDao.selectFeature(fileID);

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

			List<Association> aList = cDao.selectAssociation(String.valueOf(feat.f_idx));

			for (Association associ : aList) {
				Element ass;// = newDoc.createElement("S100:informationAssociation");

				if (associ.xrole.contains("child")) {
					ass = newDoc.createElement("child");
					// ass.setAttribute("xlink:title", associ.xid);
				} else if (associ.xrole.contains("parent")) {
					ass = newDoc.createElement("parent");
					// ass.setAttribute("xlink:title", associ.xid);
				} else {
					ass = newDoc.createElement("S100:informationAssociation");
					ass.setAttribute("gml:id", associ.xid);
				}
				// ass.setAttribute("xlink:role", associ.xrole);
				ass.setAttribute("xlink:href", associ.xhref);

				_feat.appendChild(ass);
			}

			// newDoc ... new Node? new Element?
			List<Attribute> attributeList = cDao.selectAttribute(String.valueOf(feat.f_idx));

			for (int i = 0; i < attributeList.size(); i++) {
				if (!attributeList.get(i).name.contains("#")) {
					attributeList.get(i).xml = newDoc.createElement(attributeList.get(i).name);

					if (attributeList.get(i).value != null) {
						attributeList.get(i).xml.setTextContent(attributeList.get(i).value);

						// if (attributeList.get(i).value.contains(".jpg"))
						// CopyImg(attributeList.get(i).value);
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
				String pointTemp = cDao.selectPoint(String.valueOf(feat.f_idx));

				if (pointTemp == null)
					continue;

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
				String polygonTemp = cDao.selectPolygon(String.valueOf(feat.f_idx));

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

	public void CreateFeature(String path, List<Integer> fList) {
		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();

			DocumentBuilder parser = f.newDocumentBuilder();

			Document newDoc = parser.parse(path);

			// xpath 생성
			XPath xpath = XPathFactory.newInstance().newXPath();

			// id 가 c2 인 Node의 val attribute 값 가져오기
			Node dataSet = (Node) xpath.evaluate("/DataSet", newDoc, XPathConstants.NODE);

			for (Integer integer : fList) {
				Feature feat = cDao.selectFeatureOne(integer + "");

				if (feat == null || feat.type == null)
					return;

				Element mem;
				if (feat.type.contains("imember"))
					mem = newDoc.createElement("imember");
				else
					mem = newDoc.createElement("member");

				Element _feat = newDoc.createElement(feat.featuretype);
				_feat.setAttribute("gml:id", String.valueOf(feat.f_idx));

				dataSet.appendChild(mem);
				mem.appendChild(_feat);

				List<Association> aList = cDao.selectAssociation(String.valueOf(feat.f_idx));

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
				List<Attribute> attributeList = cDao.selectAttribute(String.valueOf(feat.f_idx));

				for (int i = 0; i < attributeList.size(); i++) {
					if (!attributeList.get(i).name.contains("#")) {
						attributeList.get(i).xml = newDoc.createElement(attributeList.get(i).name);

						if (attributeList.get(i).value != null) {
							attributeList.get(i).xml.setTextContent(attributeList.get(i).value);

							/*
							 * if (attributeList.get(i).value.contains(".jpg"))
							 * CopyImg(attributeList.get(i).value);
							 */
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
					String pointTemp = cDao.selectPoint(String.valueOf(feat.f_idx));

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

					String polygonTemp = cDao.selectPolygon(String.valueOf(feat.f_idx));

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

			// text = text.replaceAll("\\R", " ");
			// text.replace( System.getProperty("line.separator").toString(), "")
			newDoc.getDocumentElement().normalize();
			DOMSource xmlDOM = new DOMSource(newDoc);
			StreamResult xmlFile = new StreamResult(new File(path));

//        TransformerFactory transformerFactory = TransformerFactory.newInstance();
//        
//        Transformer transformer =  transformerFactory.newTransformer(new StreamSource("C:\\Users\\Administrator\\Desktop\\bb\\sample.xsl"));//transformerFactory.newTransformer();
//        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); //정렬 스페이스4칸
//        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //들여쓰기
//        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes"); //doc.setXmlStandalone(true); 했을때 붙어서 출력되는부분 개행

			// transformer.transform(xmlDOM, xmlFile);
			TransformerFactory.newInstance().newTransformer().transform(xmlDOM, xmlFile);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

	}
	/////

//	public void writeExchangeCatalogue(List<Integer> idxList, String savePath) {
//		int so_idx = DataDao.getInstance().getSaveOptionIDX();
//
//		if (so_idx < 1)
//			return;
//
//		SaveOption so = DataDao.getInstance().getSelectSaveOption(so_idx);
//		Element eleEC = null;
//
//		List<String> dsList = new ArrayList<String>();
//		for (int i = 0; i < idxList.size(); i++) {
//			dsList.add(DataDao.getInstance().getSelectDatasetName(idxList.get(i)));
//		}
//
//		List<String> ecList = new ArrayList<String>();
//		List<Node> ddmList = new ArrayList<Node>();
//
//		for (int i = 0; i < idxList.size(); i++) {
//			String fileName = DataDao.getInstance().getSelectSaveFileName(idxList.get(i));
//			String[] token = fileName.split("/");
//
//			String path = fileName.substring(0, fileName.length() - token[token.length - 1].length());
//
//			File folder = new File(path);
//
//			File[] files = folder.listFiles();
//
//			Boolean check = true;
//
//			for (File f : files) {
//				if (f.isFile()) {
//					if (f.getPath().toLowerCase().contains(".xml")) {
//						String type = readXml(f.getPath());
//
//						for (String str : ecList) {
//							if (str == type)
//								check = false;
//						}
//
//						if (type.contains("ExchangeCatalogue") && check) {
//							check = false;
//							ecList.add(type);
//							eleEC = xmlDoc.getDocumentElement();
//						}
//					}
//				}
//			}
//
//			if (!check) {
//				if (eleEC == null)
//					continue;
//
//				for (int j = 0; j < eleEC.getChildNodes().getLength(); j++) {
//					Node node = eleEC.getChildNodes().item(j);
//
//					if (node.getNodeName().toLowerCase().contains("datasetdiscoverymetadata")) {
//						for (int k = 0; k < node.getChildNodes().getLength(); k++) {
//							Node _node = node.getChildNodes().item(k);
//							if (_node.getNodeName().toLowerCase().contains("filename")) {
//								for (String str : dsList) {
//									if (str.equals(_node.getFirstChild().getNodeValue())) {
//										ddmList.add(node);
//										dsList.remove(str);
//										break;
//									}
//								}
//								break;
//							}
//						}
//					}
//				}
//			}
//		}
//
//		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
//
//		DocumentBuilder parser;
//		try {
//			parser = f.newDocumentBuilder();
//			Document newDoc = parser.newDocument();
//
//			Element exchangeCatalogue = newDoc.createElement("S101_ExchangeCatalogue");
//			exchangeCatalogue.setAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema");
//			exchangeCatalogue.setAttribute("xmlns:S100XC", "http://www.iho.int/s100/xc");
//			exchangeCatalogue.setAttribute("xmlns:gmd", "http://www.isotc211.org/2005/gmd");
//			exchangeCatalogue.setAttribute("xmlns:gco", "http://www.isotc211.org/2005/gco");
//			exchangeCatalogue.setAttribute("xmlns:gmx", "http://www.isotc211.org/2005/gmx");
//			exchangeCatalogue.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
//			exchangeCatalogue.setAttribute("xmlns:gml", "http://www.opengis.net/gml/3.2");
//			exchangeCatalogue.setAttribute("xmlns:gss", "http://www.isotc211.org/2005/gss");
//			exchangeCatalogue.setAttribute("xmlns:gsr", "http://www.isotc211.org/2005/gsr");
//			exchangeCatalogue.setAttribute("xmlns:gts", "http://www.isotc211.org/2005/gts");
//			exchangeCatalogue.setAttribute("schemaLocation", "http://www.iho.int/s100/xc ./S101_ExchangeCatalogue.xsd");
//
//			//
//			Element identifier = newDoc.createElement("S100XC:identifier");
//
//			Element identifierChild1 = newDoc.createElement("S100XC:identifier");
//			Element identifierChild2 = newDoc.createElement("S100XC:editionNumber");
//			Element identifierChild3 = newDoc.createElement("S100XC:date");
//
//			identifierChild1.setTextContent(so.identifier);
//			identifierChild2.setTextContent(so.editionnumber);
//			identifierChild3.setTextContent(so.i_date);
//
//			identifier.appendChild(identifierChild1);
//			identifier.appendChild(identifierChild2);
//			identifier.appendChild(identifierChild3);
//
//			exchangeCatalogue.appendChild(identifier);
//
//			//
//			Element contact = newDoc.createElement("S100XC:contact");
//
//			Element organization = newDoc.createElement("S100XC:organization");
//			organization.setTextContent(so.organization);
//			contact.appendChild(organization);
//
//			Element phone = newDoc.createElement("S100XC:phone");
//			Element phoneChild1 = newDoc.createElement("gmd:voice");
//			Element phoneChild1_child = newDoc.createElement("gco:CharacterString");
//			Element phoneChild2 = newDoc.createElement("gmd:facsimile");
//			Element phoneChild2_child = newDoc.createElement("gco:CharacterString");
//
//			phone.setAttribute("isoType", "gmd:CI_Telephone");
//
//			if (so.voice != "" || so.facsimile != "") {
//				phoneChild1_child.setTextContent(so.voice);
//				phoneChild1.appendChild(phoneChild1_child);
//				phone.appendChild(phoneChild1);
//
//				phoneChild2_child.setTextContent(so.facsimile);
//				phoneChild2.appendChild(phoneChild2_child);
//				phone.appendChild(phoneChild2);
//
//				contact.appendChild(phone);
//			}
//
//			Element address = newDoc.createElement("S100XC:address");
//			address.setAttribute("isoType", "gmd:CI_Address");
//
//			Element addressChild1 = newDoc.createElement("gmd:deliveryPoint");
//			Element addressChild1_1 = newDoc.createElement("gco:CharacterString");
//			Element addressChild2 = newDoc.createElement("gmd:city");
//			Element addressChild2_1 = newDoc.createElement("gco:CharacterString");
//			Element addressChild3 = newDoc.createElement("gmd:administrativeArea");
//			Element addressChild3_1 = newDoc.createElement("gco:CharacterString");
//			Element addressChild4 = newDoc.createElement("gmd:postalCode");
//			Element addressChild4_1 = newDoc.createElement("gco:CharacterString");
//			Element addressChild5 = newDoc.createElement("gmd:country");
//			Element addressChild5_1 = newDoc.createElement("gco:CharacterString");
//			Element addressChild6 = newDoc.createElement("gmd:electronicMailAddress");
//			Element addressChild6_1 = newDoc.createElement("gco:CharacterString");
//
//			if (so.deliverypoint != "" || so.city != "" || so.administrativearea != "" || so.postalcode != ""
//					|| so.country != "" || so.electronicmailaddress != "") {
//
//				addressChild1_1.setTextContent(so.deliverypoint);
//				addressChild1.appendChild(addressChild1_1);
//				address.appendChild(addressChild1);
//
//				addressChild2_1.setTextContent(so.city);
//				addressChild2.appendChild(addressChild2_1);
//				address.appendChild(addressChild2);
//
//				addressChild3_1.setTextContent(so.administrativearea);
//				addressChild3.appendChild(addressChild3_1);
//				address.appendChild(addressChild3);
//
//				addressChild4_1.setTextContent(so.postalcode);
//				addressChild4.appendChild(addressChild4_1);
//				address.appendChild(addressChild4);
//
//				addressChild5_1.setTextContent(so.country);
//				addressChild5.appendChild(addressChild5_1);
//				address.appendChild(addressChild5);
//
//				addressChild6_1.setTextContent(so.electronicmailaddress);
//				addressChild6.appendChild(addressChild6_1);
//				address.appendChild(addressChild6);
//
//				contact.appendChild(address);
//			}
//
//			exchangeCatalogue.appendChild(contact);
//
//			//
//			Element productSpecification = newDoc.createElement("S100XC:productSpecification");
//
//			Element productSpecificationChild1 = newDoc.createElement("S100XC:name");
//			Element productSpecificationChild2 = newDoc.createElement("S100XC:version");
//			Element productSpecificationChild3 = newDoc.createElement("S100XC:date");
//
//			if (so.name != "" || so.version != "" || so.p_date != "") {
//				productSpecificationChild1.setTextContent(so.name);
//				productSpecificationChild2.setTextContent(so.version);
//				productSpecificationChild3.setTextContent(so.p_date);
//
//				productSpecification.appendChild(productSpecificationChild1);
//				productSpecification.appendChild(productSpecificationChild2);
//				productSpecification.appendChild(productSpecificationChild3);
//
//				exchangeCatalogue.appendChild(productSpecification);
//			}
//
//			//
//			Element exchangeCatalogueName = newDoc.createElement("S100XC:exchangeCatalogueName");
//			Element exchangeCatalogueDescription = newDoc.createElement("S100XC:exchangeCatalogueDescription");
//			Element exchangeCatalogueComment = newDoc.createElement("S100XC:exchangeCatalogueComment");
//			Element compressionFlag = newDoc.createElement("S100XC:compressionFlag");
//			Element algorithmMethod = newDoc.createElement("S100XC:algorithmMethod");
//			Element sourceMedia = newDoc.createElement("S100XC:sourceMedia");
//			Element replacedData = newDoc.createElement("S100XC:replacedData");
//			Element dataReplacement = newDoc.createElement("S100XC:dataReplacement");
//
//			exchangeCatalogueName.setTextContent(so.exchangecataloguename);
//			exchangeCatalogueDescription.setTextContent(so.exchangecataloguedescription);
//			exchangeCatalogue.appendChild(exchangeCatalogueName);
//			exchangeCatalogue.appendChild(exchangeCatalogueName);
//
//			if (so.exchangecataloguecomment != "") {
//				exchangeCatalogueComment.setTextContent(so.exchangecataloguecomment);
//				exchangeCatalogue.appendChild(exchangeCatalogueComment);
//			}
//			if (so.compressionFlag != "") {
//				compressionFlag.setTextContent(so.compressionFlag);
//				exchangeCatalogue.appendChild(compressionFlag);
//			}
//			if (so.algorithmMethod != "") {
//				algorithmMethod.setTextContent(so.algorithmMethod);
//				exchangeCatalogue.appendChild(algorithmMethod);
//			}
//			if (so.sourceMedia != "") {
//				sourceMedia.setTextContent(so.sourceMedia);
//				exchangeCatalogue.appendChild(sourceMedia);
//			}
//			if (so.replacedData != "") {
//				replacedData.setTextContent(so.replacedData);
//				exchangeCatalogue.appendChild(replacedData);
//			}
//			if (so.dataReplacement != "") {
//				dataReplacement.setTextContent(so.dataReplacement);
//				exchangeCatalogue.appendChild(dataReplacement);
//			}
//
//			for (int i = 0; i < ddmList.size(); i++) {
//				Node tempNode = newDoc.importNode(ddmList.get(i), true);
//				exchangeCatalogue.appendChild(tempNode);
//			}
//			//
//			newDoc.appendChild(exchangeCatalogue);
//
//			DOMSource xmlDOM = new DOMSource(newDoc);
//			StreamResult xmlFile = new StreamResult(new File(savePath + so.exchangecataloguename));
//			TransformerFactory.newInstance().newTransformer().transform(xmlDOM, xmlFile);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	public int readExchangeCatalogue() {
//
//		ExchangeDao dao = exchangeDao.getInstance();
//
//		Element eleEC = xmlDoc.getDocumentElement();
//
//		ExchangeCatalogue ec = new ExchangeCatalogue();
//
//		ec.ec_pk = dao.getExchangeIDX() + 1;
//
//		ec.title = eleEC.getNodeName();
//
//		//
//		int queryCheck = dao.insertExchangeCatalogue(ec);
//		if (queryCheck < 0) {
//			dao.rollback();
//			return -1;
//		}
//
//		// ExchangeCatlaouge Namespace..
//		NamedNodeMap ecAttrMap = eleEC.getAttributes();
//		for (int i = 0; i < ecAttrMap.getLength(); i++) {
//			Node ecAttr = ecAttrMap.item(i);
//
//			ExchangeCatalogueAttribute eca = new ExchangeCatalogueAttribute();
//
//			eca.ec_fk = ec.ec_pk;
//			eca.title = ecAttr.getNodeName();
//			eca.value = ecAttr.getNodeValue();
//
//			queryCheck = dao.insertExchangeCatalogueAttribute(eca);
//			if (queryCheck < 0) {
//				dao.rollback();
//				return -1;
//			}
//		}
//
//		// CHILD NODE
//		for (int i = 0; i < eleEC.getChildNodes().getLength(); i++) {
//
//			if (eleEC.getChildNodes().item(i).getNodeType() != 1)
//				continue;
//
//			Node node = eleEC.getChildNodes().item(i);
//
//			if (node.getChildNodes().getLength() == 1) {
//				SimpleA simple = new SimpleA();
//				simple.ec_fk = ec.ec_pk;
//				simple.title = node.getNodeName();
//				simple.value = node.getChildNodes().item(0).getNodeValue();
//
//				if (dao.insertSimpleA(simple) < 0) {
//					dao.rollback();
//					return -1;
//				}
//			} else if (node.getChildNodes().getLength() > 1) {
//				ComplexA complex = new ComplexA();
//				complex.c_pk = dao.getComplexaIDX() + 1;
//				complex.ec_fk = ec.ec_pk;
//				complex.title = node.getNodeName();
//
//				if (dao.insertComplexA(complex) < 0) {
//					dao.rollback();
//					return -1;
//				}
//
//				ComplexChildLoop(dao, complex.c_pk, -1, node);
//			}
//		}
//
//		dao.closeSession();
//		return 0;
//	}
//
//	int ComplexChildLoop(ExchangeDao dao, int c_fk, int parentID, Node node) {
//		boolean bCheck = true;
//
//		if (node.getNodeName().toLowerCase().contains("boundingbox")) {
//			String west, east, south, north;
//			west = east = south = north = "";
//
//			for (int j = 0; j < node.getChildNodes().getLength(); j++) {
//				Node bNode = node.getChildNodes().item(j);
//				if (bNode.getNodeName().toLowerCase().contains("west"))
//					west = bNode.getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
//				else if (bNode.getNodeName().toLowerCase().contains("east"))
//					east = bNode.getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
//				else if (bNode.getNodeName().toLowerCase().contains("south"))
//					south = bNode.getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
//				else if (bNode.getNodeName().toLowerCase().contains("north"))
//					north = bNode.getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
//			}
//			if (west != null && east != null && south != null && north != null && west != "" && east != ""
//					&& south != "" && north != "") {
//				ComplexGeometry cg = new ComplexGeometry();
//				cg.cc_fk = parentID;
//				cg.geom = String.format("%s %s, %s %s, %s %s, %s %s, %s %s", west, south, east, south, east, north,
//						west, north, west, south);
//
//				if (dao.insertComplexGeometry(cg) < 0)
//					return -1;
//
//				bCheck = false;
//			}
//
//		}
//		if (bCheck) {
//			for (int i = 0; i < node.getChildNodes().getLength(); i++) {
//				Node child = node.getChildNodes().item(i);
//
//				ComplexChild cc = new ComplexChild();
//				cc.cc_pk = dao.getComplexChildIDX() + 1;
//				cc.c_fk = c_fk;
//				cc.cc_fk = parentID;
//				cc.title = child.getNodeName();
//				cc.value = child.getNodeValue();
//
//				if (parentID == -1) {
//					if (dao.insertComplexChild1(cc) < 0)
//						return -1;
//				} else {
//					if (dao.insertComplexChild2(cc) < 0)
//						return -1;
//				}
//
//				// Insert Attribute
//				NamedNodeMap ccAttrMap = child.getAttributes();
//				if (ccAttrMap != null)
//					for (int j = 0; j < ccAttrMap.getLength(); j++) {
//						Node ccAttr = ccAttrMap.item(j);
//
//						ComplexChildAttribute cca = new ComplexChildAttribute();
//
//						cca.cc_fk = cc.cc_pk;
//						cca.title = ccAttr.getNodeName();
//						cca.value = ccAttr.getNodeValue();
//
//						if (dao.insertComplexChildAttribute(cca) < 0)
//							return -1;
//					}
//
//				// Insert Child
//				if (ComplexChildLoop(dao, c_fk, cc.cc_pk, child) == -1)
//					return -1;
//			}
//		}
//		return 0;
//	}
//
//	public int writeExchangeCatalogue(int ec_pk) {
//		ExchangeDao dao = exchangeDao.getInstance();
//		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
//		DocumentBuilder parser = null;
//
//		try {
//			parser = f.newDocumentBuilder();
//			Document newDoc = parser.newDocument();
//
//			String title = dao.getExchangeCatalogueTitle(ec_pk);
//			Element exchangeCatalogue = newDoc.createElement(title);
//
//			List<ExchangeCatalogueAttribute> ecaList = dao.getExchangeCatalogueAttribute(ec_pk);
//			for (ExchangeCatalogueAttribute eca : ecaList) {
//				exchangeCatalogue.setAttribute(eca.title, eca.value);
//			}
//
//			// simple
//			List<SimpleA> sList = dao.getSimpleA(ec_pk);
//			for (SimpleA sim : sList) {
//				Element simEle = newDoc.createElement(sim.title);
//				simEle.setTextContent(sim.value);
//
//				exchangeCatalogue.appendChild(simEle);
//			}
//
//			// complex
//			List<ComplexA> cList = dao.getComplexA(ec_pk);
//			for (ComplexA com : cList) {
//				if (com.title.contains("#comment"))
//					continue;
//
//				Element comEle = newDoc.createElement(com.title);
//
//				List<ComplexChild> ccList = dao.getComplexChildRoot(com.c_pk);
//				for (ComplexChild cc : ccList) {
//					if (cc.title.contains("#comment"))
//						continue;
//
//					Element ccEle = newDoc.createElement(cc.title);
//
//					// attribute..
//					List<ComplexChildAttribute> caList = dao.getComplexChildAttribute(cc.cc_pk);
//					for (ComplexChildAttribute ca : caList) {
//						ccEle.setAttribute(ca.title, ca.value);
//					}
//
//					// loop
//					getComplexChild(dao, newDoc, ccEle, cc);
//
//					comEle.appendChild(ccEle);
//				}
//				exchangeCatalogue.appendChild(comEle);
//			}
//
//			newDoc.appendChild(exchangeCatalogue);
//
//			DOMSource xmlDOM = new DOMSource(newDoc);
//			StreamResult xmlFile = new StreamResult(
//					new File("C:\\\\Users\\\\SHD\\\\eclipse-workspace\\\\_Ex\\\\WebContent\\\\temp.xml"));
//			TransformerFactory.newInstance().newTransformer().transform(xmlDOM, xmlFile);
//
//		} catch (Exception e) {
//			// TODO: handle exception
//			dao.rollback();
//
//			return -1;
//		}
//
//		dao.closeSession();
//
//		return 0;
//
//	}
//
//	void getComplexChild(ExchangeDao dao, Document doc, Element ele, ComplexChild cc) {
//		if (cc.title.toLowerCase().contains("boundingbox"))
//		{
//			Boundary boundary =dao.getBoundary(cc.cc_pk);
//			Element westEle = doc.createElement("gmd:westBoundLongitude");
//			Element wdecEle = doc.createElement("gco:Decimal");
//			wdecEle.setTextContent(boundary.minX);
//			westEle.appendChild(wdecEle);
//			
//			Element eastEle = doc.createElement("gmd:eastBoundLongitude");
//			Element edecEle = doc.createElement("gco:Decimal");
//			edecEle.setTextContent(boundary.maxX);
//			eastEle.appendChild(edecEle);
//			
//			Element southEle = doc.createElement("gmd:southBoundLatitude");
//			Element sdecEle = doc.createElement("gco:Decimal");
//			sdecEle.setTextContent(boundary.minY);
//			southEle.appendChild(sdecEle);
//			
//			Element northEle = doc.createElement("gmd:northBoundLatitude");
//			Element ndecEle = doc.createElement("gco:Decimal");
//			ndecEle.setTextContent(boundary.maxY);
//			northEle.appendChild(ndecEle);
//			
//			ele.appendChild(westEle);
//			ele.appendChild(eastEle);
//			ele.appendChild(southEle);
//			ele.appendChild(northEle);
//			return;
//		}
//
//		List<ComplexChild> ccList = dao.getComplexChild(cc.cc_pk);
//
//		for (ComplexChild complexChild : ccList) {
//			if (complexChild.title.contains("#text") && complexChild.value == "")
//				continue;
//			else if (complexChild.title.contains("#text") && complexChild.value != "") {
//				ele.setTextContent(complexChild.value);
//				continue;
//			}
//
//			Element ccEle = doc.createElement(complexChild.title);
//
//			// attribute..
//			List<ComplexChildAttribute> caList = dao.getComplexChildAttribute(complexChild.cc_pk);
//			for (ComplexChildAttribute ca : caList) {
//				ccEle.setAttribute(ca.title, ca.value);
//			}
//
//			getComplexChild(dao, doc, ccEle, complexChild);
//
//			ele.appendChild(ccEle);
//		}
//
//	}

}
