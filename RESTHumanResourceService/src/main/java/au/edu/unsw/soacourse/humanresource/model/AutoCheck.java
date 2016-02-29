package au.edu.unsw.soacourse.humanresource.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement
@XmlType(propOrder={"_autoCheckId", "_appId", "resultDetails"})
public class AutoCheck {
	
	private String _autoCheckId;
	private String _appId;
	private String resultDetails;
	public AutoCheck() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AutoCheck(String _autoCheckId, String _appId, String resultDetails) {
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
