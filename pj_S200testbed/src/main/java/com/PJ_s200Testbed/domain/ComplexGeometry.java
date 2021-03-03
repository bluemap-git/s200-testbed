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
public class ComplexGeometry {
	public int cc_fk;
	public String geom;
	
	public int getCc_fk() {
		return cc_fk;
	}
	public void setCc_fk(int cc_fk) {
		this.cc_fk = cc_fk;
	}
	public String getGeom() {
		return geom;
	}
	public void setGeom(String geom) {
		this.geom = geom;
	}
}
