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
public class UploadData {
	public int uploadData_pk;
	public int ds_idx;
	public String fileName;
	public String filePath;
	
	public int getUploadData_pk() {
		return uploadData_pk;
	}
	public void setUploadData_pk(int uploadData_pk) {
		this.uploadData_pk = uploadData_pk;
	}
	public int getDs_idx() {
		return ds_idx;
	}
	public void setDs_idx(int ds_idx) {
		this.ds_idx = ds_idx;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
