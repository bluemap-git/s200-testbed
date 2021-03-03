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
public class FeatureType {
    public int catalogue_fk;
    public int featureType_pk;
    public String name;
    public String definition;
    public String code;
    public String remarks;
    public String alias;
    public String featureusertype;
    
	public int getCatalogue_fk() {
		return catalogue_fk;
	}
	public void setCatalogue_fk(int catalogue_fk) {
		this.catalogue_fk = catalogue_fk;
	}
	public int getFeatureType_pk() {
		return featureType_pk;
	}
	public void setFeatureType_pk(int featureType_pk) {
		this.featureType_pk = featureType_pk;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getFeatureusertype() {
		return featureusertype;
	}
	public void setFeatureusertype(String featureusertype) {
		this.featureusertype = featureusertype;
	}
}
