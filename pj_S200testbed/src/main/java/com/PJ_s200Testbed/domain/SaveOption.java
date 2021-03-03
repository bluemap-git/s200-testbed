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
public class SaveOption {
    public int so_idx;
    public String identifier;
    public String editionnumber;
    public String i_date;
    public String organization;
    public String voice;
    public String facsimile;
    public String deliverypoint;
    public String city;
    public String administrativearea;
    public String postalcode;
    public String country;
    public String electronicmailaddress;
    public String name;
    public String version;
    public String p_date;
    public String exchangecataloguename;
    public String exchangecataloguedescription;
    public String exchangecataloguecomment;
    
    public String compressionFlag;
    public String algorithmMethod;
    public String sourceMedia;
    public String replacedData;
    public String dataReplacement;
    
    
	public String getCompressionFlag() {
		return compressionFlag;
	}
	public void setCompressionFlag(String compressionFlag) {
		this.compressionFlag = compressionFlag;
	}
	public String getAlgorithmMethod() {
		return algorithmMethod;
	}
	public void setAlgorithmMethod(String algorithmMethod) {
		this.algorithmMethod = algorithmMethod;
	}
	public String getSourceMedia() {
		return sourceMedia;
	}
	public void setSourceMedia(String sourceMedia) {
		this.sourceMedia = sourceMedia;
	}
	public String getReplacedData() {
		return replacedData;
	}
	public void setReplacedData(String replacedData) {
		this.replacedData = replacedData;
	}
	public String getDataReplacement() {
		return dataReplacement;
	}
	public void setDataReplacement(String dataReplacement) {
		this.dataReplacement = dataReplacement;
	}
	public int getSo_idx() {
		return so_idx;
	}
	public void setSo_idx(int so_idx) {
		this.so_idx = so_idx;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getEditionnumber() {
		return editionnumber;
	}
	public void setEditionnumber(String editionnumber) {
		this.editionnumber = editionnumber;
	}
	public String getI_date() {
		return i_date;
	}
	public void setI_date(String i_date) {
		this.i_date = i_date;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getVoice() {
		return voice;
	}
	public void setVoice(String voice) {
		this.voice = voice;
	}
	public String getFacsimile() {
		return facsimile;
	}
	public void setFacsimile(String facsimile) {
		this.facsimile = facsimile;
	}
	public String getDeliverypoint() {
		return deliverypoint;
	}
	public void setDeliverypoint(String deliverypoint) {
		this.deliverypoint = deliverypoint;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAdministrativearea() {
		return administrativearea;
	}
	public void setAdministrativearea(String administrativearea) {
		this.administrativearea = administrativearea;
	}
	public String getPostalcode() {
		return postalcode;
	}
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getElectronicmailaddress() {
		return electronicmailaddress;
	}
	public void setElectronicmailaddress(String electronicmailaddress) {
		this.electronicmailaddress = electronicmailaddress;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getP_date() {
		return p_date;
	}
	public void setP_date(String p_date) {
		this.p_date = p_date;
	}
	public String getExchangecataloguename() {
		return exchangecataloguename;
	}
	public void setExchangecataloguename(String exchangecataloguename) {
		this.exchangecataloguename = exchangecataloguename;
	}
	public String getExchangecataloguedescription() {
		return exchangecataloguedescription;
	}
	public void setExchangecataloguedescription(String exchangecataloguedescription) {
		this.exchangecataloguedescription = exchangecataloguedescription;
	}
	public String getExchangecataloguecomment() {
		return exchangecataloguecomment;
	}
	public void setExchangecataloguecomment(String exchangecataloguecomment) {
		this.exchangecataloguecomment = exchangecataloguecomment;
	}
    
    
}
