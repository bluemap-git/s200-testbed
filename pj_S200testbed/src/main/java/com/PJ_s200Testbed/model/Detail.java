package com.PJ_s200Testbed.model;

import java.util.List;

import com.PJ_s200Testbed.domain.FeatureDTO;

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
public class Detail {
	public String idx;
	public String name;
	public List<FeatureDTO> attributes;
	

}
