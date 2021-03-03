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
public class FitBounds {
	
	
	public int id;
	public double lowercorner_x;
	public double lowercorner_y;
	public double uppercorner_x;
	public double uppercorner_y ;
	

}
