package com.PJ_s200Testbed.service;

import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;
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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.PJ_s200Testbed.domain.Association;
import com.PJ_s200Testbed.domain.Attribute;
import com.PJ_s200Testbed.domain.AttributeBinding;
import com.PJ_s200Testbed.domain.BoundedBy;
import com.PJ_s200Testbed.domain.CatalogueAttribute;
import com.PJ_s200Testbed.domain.ComplexAttribute;
import com.PJ_s200Testbed.domain.ComplexBinding;
import com.PJ_s200Testbed.domain.ComplexPermittedValue;
import com.PJ_s200Testbed.domain.DataSet;
import com.PJ_s200Testbed.domain.DatasetIdentificationInformation;
import com.PJ_s200Testbed.domain.DatasetStructureInformation;
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
import com.PJ_s200Testbed.domain.SimpleAttribute;
import com.PJ_s200Testbed.domain.UploadData;
import com.PJ_s200Testbed.persistence.CatalogueDAO;
import com.PJ_s200Testbed.persistence.DatasetDAO;
import com.PJ_s200Testbed.persistence.ExchangeDao;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class XmlserviceImple implements Xmlservice {

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

	@Override
	public int readFeatureCatalogue() {

		int catalogue_pk = cDao.getCatalogueIDX() + 1;
		int check = cDao.insertCatalogue(catalogue_pk);
		if (check < 0)
			return -1;

		for (int i = 0; i < xmlDoc.getFirstChild().getChildNodes().getLength(); i++) {
			if (xmlDoc.getFirstChild().getChildNodes().item(i).getNodeName().contains("#"))
				continue;
			Element ele = (Element) xmlDoc.getFirstChild().getChildNodes().item(i);

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
					return value;
				}
			}
		}

		return catalogue_pk;
	}
	private void readSimpleAttributes(Element ele, int catalogue_pk) {
		for (int i = 0; i < ele.getChildNodes().getLength(); i++) {
			if (ele.getChildNodes().item(i).getNodeName().contains("#"))
				continue;
			Element child = (Element) ele.getChildNodes().item(i);
			SimpleAttribute sa = new SimpleAttribute();
			sa.catalogue_fk = catalogue_pk;
			sa.simpleAttribute_pk = cDao.getSimpleAttributeIDX() + 1;
			sa.name = elementValue(child, "S100FC:name", null);
			sa.definition = elementValue(child, "S100FC:definition", null);
			sa.code = elementValue(child, "S100FC:code", null);
			sa.alias = elementValue(child, "S100FC:alias", null);
			sa.valueType = elementValue(child, "S100FC:valueType", null);
			sa.remarks = elementValue(child, "S100FC:remarks", null);

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
					lv.label = elementValue(eleLV, "S100FC:label", null);
					lv.definition = elementValue(eleLV, "S100FC:definition", null);
					lv.code = elementValue(eleLV, "S100FC:code", null);
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
			ca.name = elementValue(child, "S100FC:name", null);
			ca.definition = elementValue(child, "S100FC:definition", null);
			ca.code = elementValue(child, "S100FC:code", null);
			ca.remarks = elementValue(child, "S100FC:remarks", null);
			cDao.insertComplexAttribute(ca);

			for (int j = 0; j < child.getChildNodes().getLength(); j++) {
				if (child.getChildNodes().item(j).getNodeName().contains("#"))
					continue;
				Element subAttributeBinding = (Element) child.getChildNodes().item(j);

				if (subAttributeBinding.getNodeName().contains("subAttributeBinding")) {
					ComplexBinding cb = new ComplexBinding();
					cb.complex_fk = ca.complex_pk;
					cb.complexBinding_pk = cDao.getComplexBindingIDX() + 1;
					cb.lower = elementValue(subAttributeBinding, "S100FC:multiplicity", "S100Base:lower");
					cb.upper = elementValue(subAttributeBinding, "S100FC:multiplicity", "S100Base:upper");
					cb.attribute = elementAttribute(subAttributeBinding, "ref", "S100FC:attribute", null);
					cb.sequential = elementAttribute(subAttributeBinding, "sequential", null, null);
					cb.nil = elementAttribute(subAttributeBinding, "xsi:nil", "S100FC:multiplicity", "S100Base:upper");
					cb.infinite = elementAttribute(subAttributeBinding, "infinite", "S100FC:multiplicity","S100Base:upper");

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
			role.name = elementValue(child, "S100FC:name", null);
			role.definition = elementValue(child, "S100FC:definition", null);
			role.code = elementValue(child, "S100FC:code", null);
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
			ia.name = elementValue(child, "S100FC:name", null);
			ia.definition = elementValue(child, "S100FC:definition", null);
			ia.code = elementValue(child, "S100FC:code", null);
			ia.role = elementValue(child, "S100FC:role", null);
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
			fa.name = elementValue(child, "S100FC:name", null);
			fa.definition = elementValue(child, "S100FC:definition", null);
			fa.code = elementValue(child, "S100FC:code", null);

			for (int j = 0; j < child.getChildNodes().getLength(); j++) {
				if (child.getChildNodes().item(j).getNodeName().contains("role")) {
					if (fa.role1 == "" || fa.role1 == null)
						fa.role1 = elementAttribute((Element) child.getChildNodes().item(j), "ref", null, null);
					else if (fa.role2 == "" || fa.role2 == null)
						fa.role2 = elementAttribute((Element) child.getChildNodes().item(j), "ref", null, null);
				}
			}
			fa.remarks = elementValue(child, "S100FC:remarks", null);
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
			it.name = elementValue(child, "S100FC:name", null);
			it.definition = elementValue(child, "S100FC:definition", null);
			it.code = elementValue(child, "S100FC:code", null);

			cDao.insertInformationType(it);

			for (int j = 0; j < child.getChildNodes().getLength(); j++) {
				if (child.getChildNodes().item(j).getNodeName().contains("#"))
					continue;

				Element subAttributeBinding = (Element) child.getChildNodes().item(j);
				if (subAttributeBinding.getNodeName().contains("attributeBinding")) {
					InformationBinding ib = new InformationBinding();
					ib.informationType_fk = it.informationType_pk;
					ib.informationBinding_pk = cDao.getInformationBindingIDX() + 1;
					ib.lower = elementValue(subAttributeBinding, "S100FC:multiplicity", "S100Base:lower");
					ib.upper = elementValue(subAttributeBinding, "S100FC:multiplicity", "S100Base:upper");
					ib.attribute = elementAttribute(subAttributeBinding, "ref", "S100FC:attribute", null);
					ib.sequential = elementAttribute(subAttributeBinding, "sequential", null, null);
					ib.nil = elementAttribute(subAttributeBinding, "xsi:nil", "S100FC:multiplicity", "S100Base:upper");
					ib.infinite = elementAttribute(subAttributeBinding, "infinite", "S100FC:multiplicity","S100Base:upper");
					
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
			ft.name = elementValue(child, "S100FC:name", null);
			ft.definition = elementValue(child, "S100FC:definition", null);
			ft.code = elementValue(child, "S100FC:code", null);
			ft.remarks = elementValue(child, "S100FC:remarks", null);
			ft.featureusertype = elementValue(child, "S100FC:featureUseType", null);
			ft.alias = elementValue(child, "S100FC:alias", null);

			cDao.insertFeatureType(ft);

			for (int j = 0; j < child.getChildNodes().getLength(); j++) {
				if (child.getChildNodes().item(j).getNodeName().contains("#"))
					continue;

				Element subAttributeBinding = (Element) child.getChildNodes().item(j);

				if (subAttributeBinding.getNodeName().contains("attributeBinding")) {
					AttributeBinding ab = new AttributeBinding();
					ab.featureType_fk = ft.featureType_pk;
					ab.attributeBinding_pk = cDao.getAttributeBindingIDX() + 1;
					ab.lower = elementValue(subAttributeBinding, "S100FC:multiplicity", "S100Base:lower");
					ab.upper = elementValue(subAttributeBinding, "S100FC:multiplicity", "S100Base:upper");
					ab.attribute = elementAttribute(subAttributeBinding, "ref", "S100FC:attribute", null);
					ab.sequential = elementAttribute(subAttributeBinding, "sequential", null, null);
					ab.nil = elementAttribute(subAttributeBinding, "xsi:nil", "S100FC:multiplicity", "S100Base:upper");
					ab.infinite = elementAttribute(subAttributeBinding, "infinite", "S100FC:multiplicity","S100Base:upper");

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
					fb.lower = elementValue(subAttributeBinding, "S100FC:multiplicity", "S100Base:lower");
					fb.upper = elementValue(subAttributeBinding, "S100FC:multiplicity", "S100Base:upper");
					fb.association = elementAttribute(subAttributeBinding, "ref", "S100FC:association", null);
					fb.role = elementAttribute(subAttributeBinding, "ref", "S100FC:role", null);
					fb.featureType = elementAttribute(subAttributeBinding, "ref", "S100FC:featureType", null);
					fb.roleType = elementAttribute(subAttributeBinding, "roleType", "S100FC:featureBinding", null);
					fb.nil = elementAttribute(subAttributeBinding, "xsi:nil", "S100FC:multiplicity", "S100Base:upper");
					fb.infinite = elementAttribute(subAttributeBinding, "infinite", "S100FC:multiplicity","S100Base:upper");
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

	@Override
	public boolean deleteNode(String path, ArrayList<List<Integer>> idList) {

		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = f.newDocumentBuilder();
			Document document = parser.parse(path);
			XPath xpath = XPathFactory.newInstance().newXPath();
			for (List<Integer> id : idList) {
				Node col2 = (Node) xpath.evaluate("//*[@id='" + id + "']", document, XPathConstants.NODE);
				Node pCol = col2.getParentNode();
				Node pNode = pCol.getParentNode();
				pNode.removeChild(pCol);
			}
			DOMSource xmlDOM = new DOMSource(document);
			StreamResult xmlFile = new StreamResult(new File(path));
			TransformerFactory.newInstance().newTransformer().transform(xmlDOM, xmlFile);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean copyXML(String path, String copyPath) {
		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = f.newDocumentBuilder();
			Document document = parser.parse(path);
			DOMSource xmlDOM = new DOMSource(document);
			FileWriter fw = new FileWriter(new File(copyPath));
			StreamResult xmlFile = new StreamResult(fw);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number", 4);
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.transform(xmlDOM, xmlFile);

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean UpdateNode(String path, String id, String key, String value) {

		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = f.newDocumentBuilder();
			Document document = parser.parse(path);
			XPath xpath = XPathFactory.newInstance().newXPath();
			Node cols = (Node) xpath.evaluate("//*[@id='" + id + "']/"+key, document, XPathConstants.NODE);
			if(cols != null) {
				cols.setTextContent(value);
			}

			DOMSource xmlDOM = new DOMSource(document);
			StreamResult xmlFile = new StreamResult(new File(path));
			TransformerFactory.newInstance().newTransformer().transform(xmlDOM, xmlFile);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
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

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	@Override
	public int readDataSet(int catalogue_fk, String tableName) {
		Element eleDS = xmlDoc.getDocumentElement();
		DataSet dataSet = new DataSet();
		dataSet.catalogue_fk = catalogue_fk;
		dataSet.ds_idx = sDao.getDataSetIDX() + 1;
		//dataSet.id = eleDS.getAttribute("gml:id");
		dataSet.id = tableName;

		int queryCheck = sDao.insertDataSet(dataSet);
		if (queryCheck < 0)
			return -1;

		Map<Integer, Integer> idxList = new HashMap<Integer, Integer>();
		Map<String, String> emp = new HashMap<String, String>();

		boolean check = false;
		int length = eleDS.getChildNodes().getLength() - 1;
		for (int i = 0; i < eleDS.getChildNodes().getLength(); i++) {

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
		if (di == null)
			return;

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
		if (ds == null)
			return;
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
		int startIdx = 0;
		int f_idx = 0;
		if (node.getChildNodes().item(startIdx).getNodeType() != 1)
			startIdx++;

		if (node.getChildNodes().item(startIdx).getNodeName().contains("#comment"))
			startIdx++;

		if (node.getChildNodes().item(startIdx).getNodeType() != 1)
			startIdx++;

		if (!check) {
			f_idx = sDao.getFeatureIDX() + 1;
			idxList.put(i, f_idx);
			Feature feature = new Feature();
			feature.ds_idx = ds_idx;
			feature.f_idx = f_idx;
			feature.featuretype = node.getChildNodes().item(startIdx).getNodeName();
			sDao.insertFeature(feature);
			emp.put(node.getChildNodes().item(startIdx).getAttributes().getNamedItem("gml:id") != null
					? node.getChildNodes().item(startIdx).getAttributes().getNamedItem("gml:id").getNodeValue()
					: null, String.valueOf(f_idx));
		}

		if (node.getNodeName().contains("imember") & !check)
			sDao.updateFeatureTypeImember(f_idx);

		for (int j = 0; j < (node.getChildNodes().item(startIdx)).getChildNodes().getLength(); j++) {
			Node _node = node.getChildNodes().item(startIdx).getChildNodes().item(j);

			if (_node.getNodeName().contains("#text"))
				continue;

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
				assci.xhref = _node.getAttributes().getNamedItem("xlink:href") != null
						? emp.get(_node.getAttributes().getNamedItem("xlink:href").getNodeValue().replace("#", ""))
						: "null";
				assci.xrole = _node.getNodeName();
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
		if (node.getNodeName().equals("#text"))
			return;
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

	@Override
	public int getUploadDataIDX() {
		return sDao.getUploadDataIDX();
	}

	@Override
	public int insertUploadData(UploadData ud) {
		return sDao.insertUploadData(ud);
	}
	
	
}
