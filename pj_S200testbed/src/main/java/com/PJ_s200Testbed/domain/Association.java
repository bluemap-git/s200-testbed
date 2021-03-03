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
public class Association {
    public int f_idx;
    public String xid;
    public String xhref;
    public String xrole;
    
	public int getF_idx() {
		return f_idx;
	}
	public void setF_idx(int f_idx) {
		this.f_idx = f_idx;
	}
	public String getXid() {
		return xid;
	}
	public void setXid(String xid) {
		this.xid = xid;
	}
	public String getXhref() {
		return xhref;
	}
	public void setXhref(String xhref) {
		this.xhref = xhref;
	}
	public String getXrole() {
		return xrole;
	}
	public void setXrole(String xrole) {
		this.xrole = xrole;
	}
}
