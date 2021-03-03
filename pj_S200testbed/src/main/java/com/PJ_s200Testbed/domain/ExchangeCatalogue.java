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
public class ExchangeCatalogue {
	public int ec_pk;
	public String title;
	
	public int getEc_pk() {
		return ec_pk;
	}
	public void setEc_pk(int ec_pk) {
		this.ec_pk = ec_pk;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
