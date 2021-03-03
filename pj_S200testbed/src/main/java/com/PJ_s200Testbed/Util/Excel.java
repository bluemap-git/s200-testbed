package com.PJ_s200Testbed.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;



public class Excel {
	private static final DateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");

	public static String getExtension(String fileName) {
		char ch;
		int len;
		if (fileName == null || (len = fileName.length()) == 0 || (ch = fileName.charAt(len - 1)) == '/' || ch == '\\'
				|| // in the case of a directory
				ch == '.') // in the case of . or ..
			return "";
		int dotInd = fileName.lastIndexOf('.'),
				sepInd = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
		if (dotInd <= sepInd)
			return "";
		else
			return fileName.substring(dotInd + 1).toLowerCase();
	}

	public static String ExcelToXML(String path, String fileName) {
		try {
			File file = new File(path+"\\"+ fileName);
			
			Workbook wb = null;
			if (getExtension(path) == "xls")
				wb = new HSSFWorkbook(new FileInputStream(file));
			else
				wb = new XSSFWorkbook(new FileInputStream(file));

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			 int exIdx = fileName .lastIndexOf(".");
			fileName = fileName.substring(0, exIdx );

			String datasetId = fileName;//"S201_" + sdf.format(new Date());

			// 루트 엘리먼트
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("S201:DataSet");
			rootElement.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
			rootElement.setAttribute("xmlns:S100", "http://www.iho.int/s100gml/1.0");
			rootElement.setAttribute("xmlns:gml", "http://www.opengis.net/gml/3.2");
			rootElement.setAttribute("xmlns:S201", "http://www.iho.int/S201/gml/1.0");
			rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			rootElement.setAttribute("gml:id", datasetId);
			rootElement.setAttribute("xsi:schemaLocation",
					"http://www.w3.org/2001/XMLSchema-instance http://www.iho.int/S100FC S100FC.xsd");

			// 메타
			Element boundedBy = doc.createElement("gml:boundedBy");
			Element envelope = doc.createElement("gml:Envelope");
			Element lowerCorner = doc.createElement("gml:lowerCorner");
			Element upperCorner = doc.createElement("gml:upperCorner");
			boundedBy.appendChild(envelope);
			envelope.appendChild(lowerCorner);
			envelope.appendChild(upperCorner);
			envelope.setAttribute("srsName", "EPSG:4326");
			lowerCorner.setTextContent("0 0");
			upperCorner.setTextContent("131.919416666667 39.1613888888889");
			rootElement.appendChild(boundedBy);

			Element datasetIdentificationInformation = doc.createElement("DatasetIdentificationInformation");
			Element encodingSpecification = doc.createElement("S100:encodingSpecification");
			encodingSpecification.setTextContent("S-100 Part 10b");
			Element encodingSpecificationEdition = doc.createElement("S100:encodingSpecificationEdition");
			encodingSpecificationEdition.setTextContent("1.0");
			Element productIdentifier = doc.createElement("S100:productIdentifier");
			productIdentifier.setTextContent("S-201");
			Element productEdition = doc.createElement("S100:productEdition");
			productEdition.setTextContent("1.0.0");
			Element applicationProfile = doc.createElement("S100:applicationProfile");
			Element datasetFileIdentifier = doc.createElement("S100:datasetFileIdentifier");
			datasetFileIdentifier.setTextContent("S-201_TDX");
			Element datasetTitle = doc.createElement("S100:datasetTitle");
			datasetTitle.setTextContent(datasetId);
			Element datasetReferenceDate = doc.createElement("S100:datasetReferenceDate");
			datasetReferenceDate.setTextContent("2019-01-17");
			Element datasetLanguage = doc.createElement("S100:datasetLanguage");
			datasetLanguage.setTextContent("kr");
			Element datasetAbstract = doc.createElement("S100:datasetAbstract");
			datasetAbstract.setTextContent("This is testdataset. Do not use in public.");
			Element datasetTopicCategory = doc.createElement("S100:datasetTopicCategory");
			datasetTopicCategory.setTextContent("utilitiesCommunication");

			datasetIdentificationInformation.appendChild(encodingSpecification);
			datasetIdentificationInformation.appendChild(encodingSpecificationEdition);
			datasetIdentificationInformation.appendChild(productIdentifier);
			datasetIdentificationInformation.appendChild(productEdition);
			datasetIdentificationInformation.appendChild(applicationProfile);
			datasetIdentificationInformation.appendChild(datasetFileIdentifier);
			datasetIdentificationInformation.appendChild(datasetTitle);
			datasetIdentificationInformation.appendChild(datasetReferenceDate);
			datasetIdentificationInformation.appendChild(datasetLanguage);
			datasetIdentificationInformation.appendChild(datasetAbstract);
			datasetIdentificationInformation.appendChild(datasetTopicCategory);

			rootElement.appendChild(datasetIdentificationInformation);

			doc.appendChild(rootElement);

			HashMap<String, List<Element>> mrnMap = new HashMap<String, List<Element>>();

			int pointIdx = 0;
			
			//for (Sheet sheet : wb) {
			for (int sheetIdx = 0 ; sheetIdx < wb.getNumberOfSheets(); sheetIdx++) {
				Sheet sheet = wb.getSheetAt(sheetIdx);
				
				if (sheet.getSheetName().equals("RepMRN")) {
					boolean first = true;
					for (Row row : sheet) {
						if (first) {
							first = false;
							continue;
						}

						if (row.getCell(1) == null)
							continue;

						String value = row.getCell(1).getStringCellValue();

						if (value != null && value != "")
							mrnMap.put(value, new ArrayList<Element>());

					}
					continue;
				}

				HashMap<Integer, Model> map = new HashMap<Integer, Model>();
				boolean first = true;
				for (Row row : sheet) {
					if (first) // 엑셀의 첫줄을 읽어서 enumeration을 담는다
					{
						int idx = 0;
						String str = "";
						while (row.getCell(idx) != null) {
							str = row.getCell(idx).getStringCellValue();

							map.put(idx, new Model());

							if (str == "")
								break;

							String[] token = str.split("\n");

							try {
								for (int i = 0; i < token.length; i++) { // enumeration의 값은 3부터 나온다.
									if (i == 0)
										map.get(idx).name = token[i].trim();
									else if (i == 1)
										map.get(idx).type = token[i].trim();
									else if (i == 2)
										map.get(idx).setMultiplicity(token[i].trim());
									else if (token[i].contains("ex)"))
										break;
									else {
										if (token[i].equals(""))
											continue;
										if (map.get(idx).type.equals("Type: enumeration")) {

											String[] eToken = token[i].split(":");

											int eIdx = Integer.parseInt(eToken[0]);
											String eValue = eToken[1].trim();
											map.get(idx).enumeration.put(eIdx, eValue);
										}
									}
								}
							} catch (Exception e) {
								break;
							}

							idx++;
						}
						first = false;
					} else {
						if (row.getCell(0) == null)
							break;

						Element member = doc.createElement("member");
						Element obj = doc.createElement("S201:" + sheet.getSheetName().replaceAll(" ", ""));
						member.appendChild(obj);
						for (int i = 2; i < map.size(); i++) {
							if (row.getCell(i) == null)
								continue;

							String value = row.getCell(i).toString();
							if (map.get(i).type.equals("Type: enumeration")) {
								try {
									String[] token = value.split(",");
									value = "";
									for (int j = 0; j < token.length; j++) {
										int eIdx = (int) Double.parseDouble(token[j].trim());
										value += map.get(i).enumeration.get(eIdx);
										if (j < token.length - 1)
											value += ", ";
									}
								} catch (Exception e) {
									continue;
								}
							}

							if (value == "")
								continue;

							if (map.get(i).name.equals(obj.getNodeName().replace("S201:", "") + "Type")) {
								
								member.removeChild(obj);
								obj = doc.createElement("S201:" + value);
								member.appendChild(obj);
								continue;
							} else if (map.get(i).name.equals("AtoNNumber")) {
								mrnMap.get(value).add(member);
							}

							Element child = doc.createElement(map.get(i).name.replaceAll(" ", ""));

							if (map.get(i).name.equals("geometry")) {
								String[] geom = value.split(",");

								if (geom.length == 2) {
									Element pointProperty = doc.createElement("pointProperty");
									Element point = doc.createElement("S100:Point");
									Element pos = doc.createElement("gml:pos");
									point.setAttribute("gml:id", String.format("P.%05d", ++pointIdx));
									pos.setTextContent(geom[0].replaceAll(" ", "") + " " + geom[1].replaceAll(" ", ""));

									child.appendChild(pointProperty);
									pointProperty.appendChild(point);
									point.appendChild(pos);
								}
							} else {
								child.setTextContent(value);
							}

							obj.appendChild(child);

						}
					}

				}
			}

			// 루트에 넣기
			mrnMap.forEach((k, v) -> {
				int pIdx = -1;
				for (int i = 0; i < v.size(); i++) {
					if (pIdx == -1) {
						String name = v.get(i).getFirstChild().getNodeName().toLowerCase();
						if (name.contains("buoy") || name.contains("beacon") || name.contains("landmark")) {
							pIdx = i;
							i = -1;
							continue;
						}
					} else {
						if (i != pIdx) {
							// Parent에 Child 넣기
							String cMrn = v.get(i).getElementsByTagName("idCode").item(0).getTextContent();
							Element child = doc.createElement("child");
							child.setAttribute("xlink:href", cMrn);
							v.get(pIdx).getFirstChild().appendChild(child);
							rootElement.appendChild(v.get(pIdx));

							// Child에 Parent 넣기
							String pMrn = v.get(pIdx).getElementsByTagName("idCode").item(0).getTextContent();
							Element parent = doc.createElement("parent");
							parent.setAttribute("xlink:href", pMrn);
							v.get(i).getFirstChild().appendChild(parent);
							rootElement.appendChild(v.get(i));

							// member에 id값 넣기
							// ((Element)v.get(i).getFirstChild()).setAttribute("gml:id", cMrn);
						}
						// member에 id값 넣기
						String mrn = v.get(i).getElementsByTagName("idCode").item(0).getTextContent();
						((Element) v.get(i).getFirstChild()).setAttribute("gml:id", mrn);

					}
				}
			});

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			//transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			//transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new FileOutputStream(new File(path+datasetId+".gml")));
			transformer.transform(source, result);
			
			
			
			return path+datasetId+".gml";
		} catch (Exception e) {
			return null;
		}	
	}
}
