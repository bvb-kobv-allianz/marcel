package de.kobv.marcel.beans;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Model class representing a MARC control field.
 */
public class Controlfield {

    /**
     * Control field tag.
     */
    private String tag;

    /**
     * Value of control field.
     */
    private String value;

    public String getTag() {
        return tag;
    }

    public void setTag(final String cftag) {
        this.tag = cftag;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String cfvalue) {
        this.value = cfvalue;
    }

    @Override
    public String toString() {
        return super.toString() + " [tag=" + getTag() + ", value=" + getValue() + "]";
    }

    public void toMarcXML(final StringBuilder xml) {
        xml.append("<marc:controlfield tag=\"");
        xml.append(tag);
        xml.append("\">");
        xml.append(StringEscapeUtils.escapeXml(value));
        xml.append("</marc:controlfield>");
    }

}