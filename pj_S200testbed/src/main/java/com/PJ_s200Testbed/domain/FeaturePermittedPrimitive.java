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
public class FeaturePermittedPrimitive {
    public int featureType_fk;
    public String permittedPrimitives;
    
	public int getFeatureType_fk() {
		return featureType_fk;
	}
	public void setFeatureType_fk(int featureType_fk) {
		this.featureType_fk = featureType_fk;
	}
	public String getPermittedPrimitives() {
		return permittedPrimitives;
	}
	public void setPermittedPrimitives(String permittedPrimitives) {
		this.permittedPrimitives = permittedPrimitives;
	}
}
