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
public class FeatureDTO {

	int f_idx;
	int a_idx;
	String featuretype;
	Double x;
	Double y;
	String value;
	String name;
	
	
	
}
