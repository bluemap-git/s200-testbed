package com.PJ_s200Testbed.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.PJ_s200Testbed.model.*;

public class ValidXML {
	private static final ValidXML INSTANCE = new ValidXML();

	private ValidFC fc = null;

	public HashMap<String, List<String>> dataErr;

	private ValidXML() {

	}

	public static ValidXML getInstance() {
		return INSTANCE;
	}

	public void clearFcMap() {
		if (fc == null)
			return;

		fc.FeatureType.clear();
		fc.ComplexAttribute.clear();
		fc.SimpleAttribute.clear();
	}

	public boolean readFeatureCatalogue(String path) {
		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();

			DocumentBuilder parser = f.newDocumentBuilder();

			Document xmlDoc = parser.parse(path);

			Element root = xmlDoc.getDocumentElement();

			fc = new ValidFC();

			loop_FC(root);

			if (fc != null)
				return true;

			return false;

		} catch (Exception e) {

			return false;

		}
	}

	public ValidNode loop_FC(Node ele) {
		try {
			ValidNode node = new ValidNode();
			node.name = ele.getNodeName();
			String code = null;

			if (ele.getChildNodes() == null || ele.getChildNodes().getLength() == 0)
				node.value = ele.getNodeValue();
			else {
				node.childs = new ArrayList<ValidNode>();
				node.value = null;

				for (int i = 0; i < ele.getChildNodes().getLength(); i++) {
					ValidNode child;
					Node childele = ele.getChildNodes().item(i);

					if (childele.getChildNodes().getLength() == 1
							&& childele.getChildNodes().item(0).getNodeName().equals("#text")) {
						child = new ValidNode();
						child.name = childele.getNodeName();
						child.value = childele.getChildNodes().item(0).getNodeValue();

						if (child.name.contains("code"))
							code = child.value;
					} else
						child = loop_FC(childele);

					if (child == null || child.name.equals("#text") || child.name.equals("#comment"))
						continue;

					node.childs.add(child);
				}
			}

			if (ele.getAttributes() != null && ele.getAttributes().getLength() > 0) {
				node.attrs = new ArrayList<ValidAttribute>();

				for (int i = 0; i < ele.getAttributes().getLength(); i++) {
					ValidAttribute attr = new ValidAttribute();
					attr.name = ele.getAttributes().item(i).getNodeName();
					attr.value = ele.getAttributes().item(i).getNodeValue();
					node.attrs.add(attr);
				}
			}

			if (code != null) {
				if (node.name.contains("FeatureType"))
					fc.FeatureType.put(code, node);
				else if (node.name.contains("SimpleAttribute"))
					fc.SimpleAttribute.put(code, node);
				else if (node.name.contains("ComplexAttribute"))
					fc.ComplexAttribute.put(code, node);
			}

			return node;

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public void readData(String path) {
		try {
			dataErr = new HashMap<String, List<String>>();

			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();

			DocumentBuilder parser = f.newDocumentBuilder();

			Document xmlDoc = parser.parse(path);

			Element root = xmlDoc.getDocumentElement();

			loopData(root);

		} catch (Exception e) {

		}
	}

	public ValidNode loopData(Node ele) {
		return loopData(ele, false);
	}

	public ValidNode loopData(Node ele, boolean member) {
		try {
			// member is true

			ValidNode node = new ValidNode();
			node.name = ele.getNodeName();

			if (ele.getAttributes() != null && ele.getAttributes().getLength() > 0) {
				node.attrs = new ArrayList<ValidAttribute>();

				for (int i = 0; i < ele.getAttributes().getLength(); i++) {
					ValidAttribute attr = new ValidAttribute();
					attr.name = ele.getAttributes().item(i).getNodeName();
					attr.value = ele.getAttributes().item(i).getNodeValue();
					node.attrs.add(attr);
				}
			}

			if (ele.getChildNodes() == null || ele.getChildNodes().getLength() == 0)
				node.value = ele.getNodeValue();
			else {
				node.childs = new ArrayList<ValidNode>();
				node.value = null;
				////// --------------
				if (member) {
					// String key = childele.getNodeName();
					String[] token = node.name.split(":");
					String key = token.length > 1 ? token[1] : token[0];
					List<String> typeList = new ArrayList<String>();
					ValidNode n = fc.FeatureType.get(key);

					if (n != null) {
						for (int j = 0; j < n.childs.size(); j++) {
							ValidNode n2 = n.childs.get(j);
							if (n2.name.contains("attributeBinding")) {
								String nodeName = "";
								String lower = "";
								String upper = "";
								boolean check = false;

								for (int k = 0; k < n2.childs.size(); k++) {
									ValidNode n3 = n2.childs.get(k);
									if (n3.name.contains("multiplicity")) {
										lower = n3.childs.get(0).value;
										upper = n3.childs.get(1).value;

										if (upper == null || upper.equals(""))
											upper = "*";

									} else if (n3.name.contains("attribute"))
										nodeName = n3.attrs.get(0).value;

								}

								typeList.add(nodeName);
								////
								for (int i = 0; i < ele.getChildNodes().getLength(); i++) {
									ValidNode child;
									Node childele = ele.getChildNodes().item(i);

									if (childele.getChildNodes().getLength() == 1
											&& childele.getChildNodes().item(0).getNodeName().equals("#text")) {
										child = new ValidNode();
										child.name = childele.getNodeName();
										child.value = childele.getChildNodes().item(0).getNodeValue();
									} else {
										if (node.name.equals("member"))
											child = loopData(childele, true);
										else
											child = loopData(childele);
									}
									if (child == null || child.name.equals("#text") || child.name.equals("#comment"))
										continue;

									if (child.name.equals(nodeName)) {
										if (!check || upper.equals("*"))
											check = true;
										else {
											if (!dataErr.containsKey(node.attrs.get(0).value))
												;// dataErr.put(node.attrs.get(0).value, new ArrayList<String>());
											;// dataErr.get(node.attrs.get(0).value).add(nodeName + " 해당 항목은 하나 이상일 수
												// 없습니다.");
										}
									}
									node.childs.add(child);

								}

								if (!check && lower.equals("1")) {
									if (!dataErr.containsKey(node.attrs.get(0).value))
										dataErr.put(node.attrs.get(0).value, new ArrayList<String>());
									dataErr.get(node.attrs.get(0).value)
											.add("Attribute '" + nodeName + "' is mandatory");
								}

								/////
							}
						}

						//
						for (int i = 0; i < ele.getChildNodes().getLength(); i++) {
							Node childele = ele.getChildNodes().item(i);

							String[] token2 = childele.getNodeName().split(":");
							String val = token2.length == 1 ? token2[0] : token2[1];

							if (val.equals("#text") || val.equals("#comment") || val.equals("featureObjectIdentifier")
									|| val.equals("IALA_aidAvailabilityCategory"))
								continue;
							String geom = "";

							if (dataErr.containsKey(node.attrs.get(0).value)) {
								if (val.equals("geometry")) {
									geom = loopGeom(childele);
									
									if(geom != "")
										dataErr.get(node.attrs.get(0).value).add(0, geom+"&");
								}								
								
							}
							

							if (!typeList.contains(val)) {
								if (!dataErr.containsKey(node.attrs.get(0).value))
									;// dataErr.put(node.attrs.get(0).value, new ArrayList<String>());
								;// dataErr.get(node.attrs.get(0).value).add(childele.getNodeName() + " 해당 항목은
									// 입력할 수 없습니다.");
							}
							// child.value = childele.getChildNodes().item(0).getNodeValue();

						}
					}

				} else {
					for (int i = 0; i < ele.getChildNodes().getLength(); i++) {
						ValidNode child;
						Node childele = ele.getChildNodes().item(i);

						if (childele.getChildNodes().getLength() == 1
								&& childele.getChildNodes().item(0).getNodeName().equals("#text")) {
							child = new ValidNode();
							child.name = childele.getNodeName();
							child.value = childele.getChildNodes().item(0).getNodeValue();
						} else {
							if (node.name.equals("member"))
								child = loopData(childele, true);
							else
								child = loopData(childele);
						}
						if (child == null || child.name.equals("#text") || child.name.equals("#comment"))
							continue;

						node.childs.add(child);
					}
				}

				/////// -------------
			}

			return node;

		} catch (Exception e) { // TODO: handle exception
			return null;
		}

	}

	public String loopGeom(Node ele) {
		String geom = "";
		for (int i = 0; i < ele.getChildNodes().getLength(); i++) {
			Node childele = ele.getChildNodes().item(i);
			if (childele.getNodeName().equals("gml:pos")) {
				if (childele.getChildNodes().getLength() == 1
						&& childele.getChildNodes().item(0).getNodeName().equals("#text")) {
					geom = childele.getChildNodes().item(0).getNodeValue();
				}
			} else
				geom = loopGeom(childele);

			if (geom != "")
				return geom;

		}
		return "";
	}

	/*
	 * public Node setNode(Node ele) { try { Node node = new
	 * Node(); node.name = ele.getNodeName();
	 * 
	 * if (ele.getChildNodes() == null || ele.getChildNodes().getLength() == 0)
	 * node.value = ele.getNodeValue(); else { node.childs = new
	 * ArrayList<Node>(); node.value = null;
	 * 
	 * for (int i = 0; i < ele.getChildNodes().getLength(); i++) { Node
	 * child; Node childele = ele.getChildNodes().item(i);
	 * 
	 * if (childele.getChildNodes().getLength() == 1 &&
	 * childele.getChildNodes().item(0).getNodeName() == "#text") { child = new
	 * Node(); child.name = childele.getNodeName(); child.value =
	 * childele.getChildNodes().item(0).getNodeValue(); } else child =
	 * setNode(childele);
	 * 
	 * if(child == null || child.name == "#text" || child.name =="#comment")
	 * continue;
	 * 
	 * node.childs.add(child); } }
	 * 
	 * if (ele.getAttributes() != null && ele.getAttributes().getLength() > 0) {
	 * node.attrs = new ArrayList<Attribute>();
	 * 
	 * for (int i = 0; i < ele.getAttributes().getLength(); i++) {
	 * Attribute attr = new Attribute(); attr.name =
	 * ele.getAttributes().item(i).getNodeName(); attr.value =
	 * ele.getAttributes().item(i).getNodeValue(); node.attrs.add(attr); } }
	 * 
	 * return node;
	 * 
	 * } catch (Exception e) { // TODO: handle exception return null; } }
	 */
}
