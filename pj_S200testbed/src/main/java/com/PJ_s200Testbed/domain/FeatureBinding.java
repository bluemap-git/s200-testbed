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
public class FeatureBinding {
    public int featureType_fk;
    public String lower;
    public String upper;
    public String association;
    public String role;
    public String featureType;
    public String roleType;
    public String nil;
    public String infinite;
    
	public int getFeatureType_fk() {
		return featureType_fk;
	}
	public void setFeatureType_fk(int featureType_fk) {
		this.featureType_fk = featureType_fk;
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
	public String getAssociation() {
		return association;
	}
	public void setAssociation(String association) {
		this.association = association;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getFeatureType() {
		return featureType;
	}
	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
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
