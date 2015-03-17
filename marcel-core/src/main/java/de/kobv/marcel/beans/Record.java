package de.kobv.marcel.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

public class Record {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(Record.class);

    /**
     * Unique identifier for record.
     */
    private String uid;

    /**
     * TODO
     */
    private String leader;

    /**
     * TODO
     */
    private boolean hasLibrary = false;

    /**
     * TODO remove
     */
    private boolean isKOBV = false;

    /**
     * TODO woher weiß ich das?
     */
    private boolean isValid = true;

    /**
     * TODO
     */
    // private List<String> validationMessages = new ArrayList<String>();

    private List<Controlfield> controlfields = new ArrayList<Controlfield>();

    private Map<String, Set<Controlfield>> controlFieldsByTag = new HashMap<String, Set<Controlfield>>();

    private List<Datafield> datafields = new ArrayList<Datafield>();

    /**
     * Name of data source.
     */
    private String dataSource;

    /**
     *
     */
    private int tagIndex = 1;

    /**
     * Constructs record for data source.
     *
     * @param dsource
     */
    public Record(final String dsource) {
        this.dataSource = dsource;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(final String value) {
        this.leader = value;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(final String uidval) {
        this.uid = uidval;
    }

    public boolean hasLibrary() {
        return hasLibrary;
    }

    public void setHasLibrary(final boolean hasLibraryVal) {
        this.hasLibrary = hasLibraryVal;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(final boolean isValidVal) {
        this.isValid = isValidVal;
    }

    public List<Controlfield> getControlfields() {
        return controlfields;
    }

    public List<Controlfield> getControlFields(final String tag) {
        return null; // TODO
    }

    public void setControlfields(final ArrayList<Controlfield> cfields) {
        this.controlfields = cfields;
    }

    public void addControlfield(final Controlfield controlfield) {
        this.controlfields.add(controlfield);
        String tag = controlfield.getTag();
        Set<Controlfield> fields = this.controlFieldsByTag.get(tag);
        if (fields == null) {
            fields = new HashSet<Controlfield>();
            controlFieldsByTag.put(tag, fields);
        }
        fields.add(controlfield);
    }

    public List<Datafield> getDatafields() {
        return datafields;
    }

    public void setDatafields(final List<Datafield> dfields) {
        this.datafields = dfields;
    }

    public void addDatafield(final Datafield datafield) {
        this.datafields.add(datafield);
        datafield.setTagIdx(tagIndex++);
    }

    // TODO remove
    public boolean isKOBV() {
        return isKOBV;
    }

    // TODO remove
    public void setKOBV(final boolean value) {
        this.isKOBV = value;
    }

    /**
     * @param field
     * @return TODO optimize storage of datafields and decision if a subfield exists or not (HashMap)
     */
    public boolean containsSubfield(final String field) {
        if (field.length() != 4) {
            // TODO error, throw exception maybe?
            LOG.warn("Incorrect field definition by query containsSubfield: " + field);
        }
        String tag = field.substring(0, 3);
        String code = Character.toString(field.charAt(3));
        for (Datafield datafield : datafields) {
            if (tag.equals(datafield.getTag())) {
                for (Subfield subfield : datafield.getSubfields()) {
                    if (subfield.getCode() == code) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Record [uid=" + uid + ", leader=" + leader + ", controlfields="
                + controlfields + ", subfields=" + datafields + "]";
    }

    public static final String MARC_XML_HEADER =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
                    + "<marc:collection xmlns:marc=\"http://www.loc.gov/MARC21/slim\""
                    + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                    + " xsi:schemaLocation=\"http://www.loc.gov/MARC21/slim"
                    + " http://www.loc.gov/standards/marcxml/schema/MARC21slim.xsd\">";

    public String toMarcXML(final boolean addHeader) {
        StringBuilder xml = new StringBuilder();
        if (addHeader) {
            xml.append(MARC_XML_HEADER);
        }
        xml.append("<marc:record><marc:leader>");
        xml.append(StringEscapeUtils.escapeXml(leader));
        xml.append("</marc:leader>");
        for (Controlfield cf : controlfields) {
            cf.toMarcXML(xml);
        }
        for (Datafield df : datafields) {
            df.toMarcXML(xml);
        }
        xml.append("</marc:record>");
        if (addHeader) {
            xml.append("</marc:collection>");
        }
        return xml.toString();
    }

}
