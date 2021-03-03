package com.PJ_s200Testbed.domain;

import org.w3c.dom.Element;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Attribute {

	 public int f_idx;
	    public int a_idx;
	    public String name;
	    public String value;
	    public String parents;
	    public String attributetype;
	    public Element xml;
	    
	    public int getF_idx() {
			return f_idx;
		}
		public void setF_idx(int f_idx) {
			this.f_idx = f_idx;
		}
		public int getA_idx() {
			return a_idx;
		}
		public void setA_idx(int a_idx) {
			this.a_idx = a_idx;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getParents() {
			return parents;
		}
		public void setParents(String parents) {
			this.parents = parents;
		}
		public String getAttributetype() {
			return attributetype;
		}
		public void setAttributetype(String attributetype) {
			this.attributetype = attributetype;
		}
		public Element getXml() {
			return xml;
		}
		public void setXml(Element xml) {
			this.xml = xml;
		}
}
