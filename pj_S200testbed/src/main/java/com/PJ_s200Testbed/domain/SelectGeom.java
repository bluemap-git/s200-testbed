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
public class SelectGeom {
	public String ds_idx;
	public String geom;
	
	public String getDs_idx() {
		return ds_idx;
	}
	public void setDs_idx(String ds_idx) {
		this.ds_idx = ds_idx;
	}
	public String getGeom() {
		return geom;
	}
	public void setGeom(String geom) {
		this.geom = geom;
	}
}
