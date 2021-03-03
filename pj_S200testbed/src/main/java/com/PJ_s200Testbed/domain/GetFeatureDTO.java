package com.PJ_s200Testbed.domain;

import java.util.List;

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
public class GetFeatureDTO {
	public String featureType;
	public String name;
	public String x;
	public String y;
	public String id;
	public String point;
	public List<Integer> numarray;
	public String polylist;
	public String rectlist;
	public String polygone;

}
