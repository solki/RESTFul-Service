package com.ors.bean;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = { "_autoCheckId", "_appId", "resultDetails" })
public class AutoCheckBean {
	private String _autoCheckId;
	private String _appId;
	private String resultDetails;

	public AutoCheckBean() {
		super();
	}

	public AutoCheckBean(String _autoCheckId, String _appId,
			String resultDetails) {
		super();
		this._autoCheckId = _autoCheckId;
		this._appId = _appId;
		this.resultDetails = resultDetails;
	}

	public String get_autoCheckId() {
		return _autoCheckId;
	}

	public void set_autoCheckId(String _autoCheckId) {
		this._autoCheckId = _autoCheckId;
	}

	public String get_appId() {
		return _appId;
	}

	public void set_appId(String _appId) {
		this._appId = _appId;
	}

	public String getResultDetails() {
		return resultDetails;
	}

	public void setResultDetails(String resultDetails) {
		this.resultDetails = resultDetails;
	}

}
