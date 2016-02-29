
package au.edu.unsw.soacourse.crv;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CRVResponseCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CRVResponseCode"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="CLEAN"/&gt;
 *     &lt;enumeration value="DIRTY"/&gt;
 *     &lt;enumeration value="UNKNOWN"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CRVResponseCode")
@XmlEnum
public enum CRVResponseCode {

    CLEAN,
    DIRTY,
    UNKNOWN;

    public String value() {
        return name();
    }

    public static CRVResponseCode fromValue(String v) {
        return valueOf(v);
    }

}
