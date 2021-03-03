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
public class DatasetIdentificationInformation {
    public int ds_idx;
    public String encodingSpecification;
    public String encodingSpecificationEdition;
    public String productIdentifier;
    public String productEdition;
    public String applicationProfile;
    public String datasetFileIdentifier;
    public String datasetTitle;
    public String datasetReferenceDate;
    public String datasetLanguage;
    
    public int getDs_idx() {
		return ds_idx;
	}
	public void setDs_idx(int ds_idx) {
		this.ds_idx = ds_idx;
	}
	public String getEncodingSpecification() {
		return encodingSpecification;
	}
	public void setEncodingSpecification(String encodingSpecification) {
		this.encodingSpecification = encodingSpecification;
	}
	public String getEncodingSpecificationEdition() {
		return encodingSpecificationEdition;
	}
	public void setEncodingSpecificationEdition(String encodingSpecificationEdition) {
		this.encodingSpecificationEdition = encodingSpecificationEdition;
	}
	public String getProductIdentifier() {
		return productIdentifier;
	}
	public void setProductIdentifier(String productIdentifier) {
		this.productIdentifier = productIdentifier;
	}
	public String getProductEdition() {
		return productEdition;
	}
	public void setProductEdition(String productEdition) {
		this.productEdition = productEdition;
	}
	public String getApplicationProfile() {
		return applicationProfile;
	}
	public void setApplicationProfile(String applicationProfile) {
		this.applicationProfile = applicationProfile;
	}
	public String getDatasetFileIdentifier() {
		return datasetFileIdentifier;
	}
	public void setDatasetFileIdentifier(String datasetFileIdentifier) {
		this.datasetFileIdentifier = datasetFileIdentifier;
	}
	public String getDatasetTitle() {
		return datasetTitle;
	}
	public void setDatasetTitle(String datasetTitle) {
		this.datasetTitle = datasetTitle;
	}
	public String getDatasetReferenceDate() {
		return datasetReferenceDate;
	}
	public void setDatasetReferenceDate(String datasetReferenceDate) {
		this.datasetReferenceDate = datasetReferenceDate;
	}
	public String getDatasetLanguage() {
		return datasetLanguage;
	}
	public void setDatasetLanguage(String datasetLanguage) {
		this.datasetLanguage = datasetLanguage;
	}
	public String getDatasetTopicCategory() {
		return datasetTopicCategory;
	}
	public void setDatasetTopicCategory(String datasetTopicCategory) {
		this.datasetTopicCategory = datasetTopicCategory;
	}
	public String datasetTopicCategory;
}
