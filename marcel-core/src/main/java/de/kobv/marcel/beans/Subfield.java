package de.kobv.marcel.beans;

import java.text.Normalizer;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Model for MARC subfield.
 */
public class Subfield {

    /**
     * Subfield code.
     */
    private String code;

    /**
     * Subfield value;
     */
    private String value;

    public String getCode() {
        return code;
    }

    public void setCode(final String sfcode) {
        this.code = sfcode;
    }

    /**
     *
     * @return
     * TODO normalize once?
     */
    public String getValue() {
        return Normalizer.normalize(value, Normalizer.Form.NFC);
    }

    public void setValue(final String sfvalue) {
        this.value = sfvalue;
    }

    @Override
    public String toString() {
        return "Subfield [code=" + code + ", value=" + getValue() + "]";
    }

    public void toMarcXML(final StringBuilder xml) {
        xml.append("<marc:subfield code=\"");
        xml.append(code);
        xml.append("\">");
        xml.append(StringEscapeUtils.escapeXml(getValue()));
        xml.append("</marc:subfield>");
    }

}
