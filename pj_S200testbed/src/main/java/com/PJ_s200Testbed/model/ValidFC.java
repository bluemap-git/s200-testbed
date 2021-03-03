package com.PJ_s200Testbed.model;
import java.util.HashMap;

public class ValidFC {
	public HashMap<String, ValidNode> FeatureType;
	public HashMap<String, ValidNode> ComplexAttribute;
	public HashMap<String, ValidNode> SimpleAttribute;
	
	public ValidFC() {
		FeatureType = new HashMap<String, ValidNode>();
		ComplexAttribute = new HashMap<String, ValidNode>();
		SimpleAttribute = new HashMap<String, ValidNode>();
	}
}
