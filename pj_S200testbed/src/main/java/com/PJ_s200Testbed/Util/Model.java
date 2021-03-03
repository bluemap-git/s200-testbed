package com.PJ_s200Testbed.Util;

import java.util.HashMap;

public class Model {
	public String name;
	public String type;
	public int minOccurs;
	public int maxOccurs;	//-1 is Err, 2 is unbounded(*)
	public HashMap<Integer,String> enumeration;
	
	public Model(){
		name ="";
		type = "";
		minOccurs = 0;
		maxOccurs = 0;
		enumeration = new HashMap<Integer, String>();
	}

	public void setMultiplicity(String value) {
		try {
			String[] token = value.split(",");
			
			minOccurs = Integer.parseInt(token[0].trim());
			
			if(token[1].trim().equals("0") ||token[1].trim().equals("1"))
				maxOccurs = Integer.parseInt(token[1].trim());	
			else if(token[1].trim().equals("*") ||token[1].trim().equals("unbounded"))
				maxOccurs = 2;	
			else
				throw new Exception();
		}catch (Exception e) {
			// TODO: handle exception
			minOccurs = -1;
			maxOccurs = -1;
		}
	}	
}