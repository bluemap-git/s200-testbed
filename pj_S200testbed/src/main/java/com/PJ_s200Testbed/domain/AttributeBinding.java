package com.PJ_s200Testbed.domain;

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
public class AttributeBinding {
    public int featureType_fk;
    public int attributeBinding_pk;
    public String lower;
    public String upper;
    public String attribute;
    public String sequential;
    public String nil;
    public String infinite;
    
	public int getFeatureType_fk() {
		return featureType_fk;
	}
	public void setFeatureType_fk(int featureType_fk) {
		this.featureType_fk = featureType_fk;
	}
	public int getAttributeBinding_pk() {
		return attributeBinding_pk;
	}
	public void setAttributeBinding_pk(int attributeBinding_pk) {
		this.attributeBinding_pk = attributeBinding_pk;
	}
	public String getLower() {
		return lower;
	}
	public void setLower(String lower) {
		this.lower = lower;
	}
	public String getUpper() {
		return upper;
	}
	public void setUpper(String upper) {
		this.upper = upper;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getSequential() {
		return sequential;
	}
	public void setSequential(String sequential) {
		this.sequential = sequential;
	}
	public String getNil() {
		return nil;
	}
	public void setNil(String nil) {
		this.nil = nil;
	}
	public String getInfinite() {
		return infinite;
	}
	public void setInfinite(String infinite) {
		this.infinite = infinite;
	}
}
