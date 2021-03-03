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
public class Feature {
    public int ds_idx;
    public int f_idx;
    public String type;
    public String featuretype;
    
	public int getDs_idx() {
		return ds_idx;
	}
	public void setDs_idx(int ds_idx) {
		this.ds_idx = ds_idx;
	}
	public int getF_idx() {
		return f_idx;
	}
	public void setF_idx(int f_idx) {
		this.f_idx = f_idx;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFeaturetype() {
		return featuretype;
	}
	public void setFeaturetype(String featuretype) {
		this.featuretype = featuretype;
	}
}
