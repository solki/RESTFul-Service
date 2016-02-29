
package au.edu.unsw.soacourse.autocheck;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PDVResponseCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PDVResponseCode"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AllMatch"/&gt;
 *     &lt;enumeration value="PartialMatch"/&gt;
 *     &lt;enumeration value="NoMatch"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "PDVResponseCode")
@XmlEnum
public enum PDVResponseCode {

    @XmlEnumValue("AllMatch")
    ALL_MATCH("AllMatch"),
    @XmlEnumValue("PartialMatch")
    PARTIAL_MATCH("PartialMatch"),
    @XmlEnumValue("NoMatch")
    NO_MATCH("NoMatch");
    private final String value;

    PDVResponseCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PDVResponseCode fromValue(String v) {
        for (PDVResponseCode c: PDVResponseCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
