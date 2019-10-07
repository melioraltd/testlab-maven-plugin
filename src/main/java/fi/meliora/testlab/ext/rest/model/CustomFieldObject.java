package fi.meliora.testlab.ext.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * A custom field object with 10 fields included.
 *
 * @author Marko Kanala
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomFieldObject extends ModelObject {

    /**
     * custom field value
     */
    private String custom1;
    /**
     * custom field value
     */
    private String custom2;
    /**
     * custom field value
     */
    private String custom3;
    /**
     * custom field value
     */
    private String custom4;
    /**
     * custom field value
     */
    private String custom5;
    /**
     * custom field value
     */
    private String custom6;
    /**
     * custom field value
     */
    private String custom7;
    /**
     * custom field value
     */
    private String custom8;
    /**
     * custom field value
     */
    private String custom9;
    /**
     * custom field value
     */
    private String custom10;

    public String getCustom1() {
        return custom1;
    }

    public void setCustom1(String custom1) {
        this.custom1 = custom1;
    }

    public String getCustom2() {
        return custom2;
    }

    public void setCustom2(String custom2) {
        this.custom2 = custom2;
    }

    public String getCustom3() {
        return custom3;
    }

    public void setCustom3(String custom3) {
        this.custom3 = custom3;
    }

    public String getCustom4() {
        return custom4;
    }

    public void setCustom4(String custom4) {
        this.custom4 = custom4;
    }

    public String getCustom5() {
        return custom5;
    }

    public void setCustom5(String custom5) {
        this.custom5 = custom5;
    }

    public String getCustom6() {
        return custom6;
    }

    public void setCustom6(String custom6) {
        this.custom6 = custom6;
    }

    public String getCustom7() {
        return custom7;
    }

    public void setCustom7(String custom7) {
        this.custom7 = custom7;
    }

    public String getCustom8() {
        return custom8;
    }

    public void setCustom8(String custom8) {
        this.custom8 = custom8;
    }

    public String getCustom9() {
        return custom9;
    }

    public void setCustom9(String custom9) {
        this.custom9 = custom9;
    }

    public String getCustom10() {
        return custom10;
    }

    public void setCustom10(String custom10) {
        this.custom10 = custom10;
    }

    /**
     * Returns a custom field value from this object by field index.
     *
     * @param index field index
     * @return field value
     */
    public String getCustomFieldValue(int index) {
        switch(index) {
            case 1:
                return getCustom1();
            case 2:
                return getCustom2();
            case 3:
                return getCustom3();
            case 4:
                return getCustom4();
            case 5:
                return getCustom5();
            case 6:
                return getCustom6();
            case 7:
                return getCustom7();
            case 8:
                return getCustom8();
            case 9:
                return getCustom9();
            case 10:
                return getCustom10();
        }
        return null;
    }

    /**
     * Sets a custom field value to this object by field index.
     *
     * @param index field index
     * @param value field value
     */
    public void setCustomFieldValue(int index, String value) {
        switch(index) {
            case 1:
                setCustom1(value);
                break;
            case 2:
                setCustom2(value);
                break;
            case 3:
                setCustom3(value);
                break;
            case 4:
                setCustom4(value);
                break;
            case 5:
                setCustom5(value);
                break;
            case 6:
                setCustom6(value);
                break;
            case 7:
                setCustom7(value);
                break;
            case 8:
                setCustom8(value);
                break;
            case 9:
                setCustom9(value);
                break;
            case 10:
                setCustom10(value);
                break;
            default:
                throw new RuntimeException("setCustomFieldValue called for invalid index: " + index);
        }
    }

}
