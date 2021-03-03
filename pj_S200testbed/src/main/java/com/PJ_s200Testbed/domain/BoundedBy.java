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
public class BoundedBy {
	public int ds_idx;
	public String srsname;
	public String lowercorner;
	public String uppercorner;
	public String lx;
	public String ly;
	public String ux;
	public String uy;
	
	public int getDs_idx() {
		return ds_idx;
	}
	public void setDs_idx(int ds_idx) {
		this.ds_idx = ds_idx;
	}
	public String getSrsname() {
		return srsname;
	}
	public void setSrsname(String srsname) {
		this.srsname = srsname;
	}
	public String getLowercorner() {
		return lowercorner;
	}
	public void setLowercorner(String lowercorner) {
		this.lowercorner = lowercorner;
	}
	public String getUppercorner() {
		return uppercorner;
	}
	public void setUppercorner(String uppercorner) {
		this.uppercorner = uppercorner;
	}
	public String getLx() {
		return lx;
	}
	public void setLx(String lx) {
		this.lx = lx;
	}
	public String getLy() {
		return ly;
	}
	public void setLy(String ly) {
		this.ly = ly;
	}
	public String getUx() {
		return ux;
	}
	public void setUx(String ux) {
		this.ux = ux;
	}
	public String getUy() {
		return uy;
	}
	public void setUy(String uy) {
		this.uy = uy;
	}
	
}
