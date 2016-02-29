
package au.edu.unsw.soacourse.autocheck;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="pdvcode" type="{http://autocheck.soacourse.unsw.edu.au}PDVResponseCode"/&gt;
 *         &lt;element name="pdvmsg" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="crvcode" type="{http://autocheck.soacourse.unsw.edu.au}CRVResponseCode"/&gt;
 *         &lt;element name="crvmsg" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "pdvcode",
    "pdvmsg",
    "crvcode",
    "crvmsg"
})
@XmlRootElement(name = "AutoCheckResponse")
public class AutoCheckResponse {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected PDVResponseCode pdvcode;
    @XmlElement(required = true)
    protected String pdvmsg;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected CRVResponseCode crvcode;
    @XmlElement(required = true)
    protected String crvmsg;

    /**
     * Gets the value of the pdvcode property.
     * 
     * @return
     *     possible object is
     *     {@link PDVResponseCode }
     *     
     */
    public PDVResponseCode getPdvcode() {
        return pdvcode;
    }

    /**
     * Sets the value of the pdvcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link PDVResponseCode }
     *     
     */
    public void setPdvcode(PDVResponseCode value) {
        this.pdvcode = value;
    }

    /**
     * Gets the value of the pdvmsg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPdvmsg() {
        return pdvmsg;
    }

    /**
     * Sets the value of the pdvmsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPdvmsg(String value) {
        this.pdvmsg = value;
    }

    /**
     * Gets the value of the crvcode property.
     * 
     * @return
     *     possible object is
     *     {@link CRVResponseCode }
     *     
     */
    public CRVResponseCode getCrvcode() {
        return crvcode;
    }

    /**
     * Sets the value of the crvcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CRVResponseCode }
     *     
     */
    public void setCrvcode(CRVResponseCode value) {
        this.crvcode = value;
    }

    /**
     * Gets the value of the crvmsg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCrvmsg() {
        return crvmsg;
    }

    /**
     * Sets the value of the crvmsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCrvmsg(String value) {
        this.crvmsg = value;
    }

}
