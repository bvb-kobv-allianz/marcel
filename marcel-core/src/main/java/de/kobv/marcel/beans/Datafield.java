package de.kobv.marcel.beans;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;

/**
 * Model class representing a MARC data field.
 */
public class Datafield {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(Datafield.class);

    /**
     *
     */
    private String tag;

    private int tagIdx;

    private char ind1;

    private char ind2;

    private List<Subfield> subfields = new ArrayList<Subfield>();

    public String getTag() {
        return tag;
    }

    // TODO check and report non valid tag
    public void setTag(final String dftag) {
        if (dftag == null || dftag.contains(" ")) {
            LOG.error(" >>> Non-Valid datafield tag: \"" + dftag + "\"");
        }
        this.tag = StringUtils.trimToEmpty(dftag);
    }

    public int getTagIdx() {
        return tagIdx;
    }

    public void setTagIdx(final int value) {
        this.tagIdx = value;
    }

    public char getInd1() {
        return ind1;
    }

    public void setInd1(final char value) {
        this.ind1 = value;
    }

    public char getInd2() {
        return ind2;
    }

    public void setInd2(final char value) {
        this.ind2 = value;
    }

    public List<Subfield> getSubfields() {
        return subfields;
    }

    public void setSubfields(final List<Subfield> sfields) {
        this.subfields = sfields;
    }

    public void addSubfield(final Subfield subfield) {
        this.subfields.add(subfield);
    }

    @Override
    public String toString() {
        return "Datafield [tag=" + tag + ", tagIdx=" + tagIdx + ", ind1=" + ind1 + ", ind2=" + ind2
                + ", subfields=" + subfields + "]";
    }

    public void toMarcXML(final StringBuilder xml) {
        xml.append("<marc:datafield tag=\"");
        xml.append(tag);
        xml.append("\" ind1=\"");
        xml.append(ind1);
        xml.append("\" ind2=\"");
        xml.append(ind2);
        xml.append("\">");
        for (Subfield sf : subfields) {
            sf.toMarcXML(xml);
        }
        xml.append("</marc:datafield>");
    }

}