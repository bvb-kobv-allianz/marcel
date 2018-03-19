package de.kobv.marcel.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import de.kobv.marcel.beans.Controlfield;
import de.kobv.marcel.beans.Datafield;
import de.kobv.marcel.beans.Record;
import de.kobv.marcel.beans.Subfield;

import org.apache.commons.lang3.StringUtils;

public class MarcXMLParser implements IMarcParser {

    static final String TAG_RECORD = "record";
    static final String TAG_LEADER = "leader";
    static final String TAG_CONTROLFIELD = "controlfield";
    static final String TAG_DATAFIELD = "datafield";
    static final String TAG_SUBFIELD = "subfield";

    String encoding = "utf8";

    /**
     * Currently parsed record.
     */
    private Record record;

    private IMarcParserHandler handler;

    private String dataSourceName;

    public MarcXMLParser() {
    }

    /**
     * Method parses the given XML-inputStream.
     *
     * @param inputStream - the XML inputStream
     * @throws IOException if an input error occurs
     * @throws MarcXMLParserException if an error while parsing the XML occurs
     */
    public void parse(final InputStream inputStream) throws IOException, MarcXMLParserException {
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLStreamReader streamReader = inputFactory.createXMLStreamReader(
                    new InputStreamReader(inputStream, Charset.forName(getEncoding())));

            handler.init();

            while (streamReader.hasNext()) {
                streamReader.next();
                if (streamReader.isStartElement()) {
                    processStartElement(streamReader);
                }
                else if (streamReader.isEndElement()) {
                    processEndElement(streamReader);
                }
            }

            handler.parsingFinished();
        }
        catch (XMLStreamException xmlse) {
            throw new MarcXMLParserException(record.getUid(), xmlse);
        }
        catch (NullPointerException nEx) {
            throw new MarcXMLParserException("Invalid charset " + getEncoding(), nEx);
        }
    }

    private void processStartElement(final XMLStreamReader streamReader) throws XMLStreamException {
        String localName = streamReader.getLocalName();
        if (localName.equals(TAG_RECORD)) {
            record = new Record(dataSourceName);
        }
        else if (localName.equals(TAG_LEADER)) {
            record.setLeader(streamReader.getElementText());
        }
        else if (localName.equals(TAG_CONTROLFIELD)) {
            Controlfield controlfield = parseControlField(streamReader);
            record.addControlfield(controlfield);
            // Set uid
            if ("001".equals(controlfield.getTag())) {
                record.setUid(controlfield.getValue());
            }
        }
        else if (localName.equals(TAG_DATAFIELD)) {
            Datafield datafield = parseDataField(streamReader);
            record.addDatafield(datafield);
        }
    }

    private void processEndElement(final XMLStreamReader streamReader) throws XMLStreamException, IOException {
        if (streamReader.getLocalName().equals(TAG_RECORD)) {
            handler.processRecord(record);
        }
    }

    private Controlfield parseControlField(final XMLStreamReader streamReader) throws XMLStreamException {
        Controlfield controlfield = new Controlfield();
        controlfield.setTag(streamReader.getAttributeValue(null, "tag"));
        controlfield.setValue(streamReader.getElementText());
        return controlfield;
    }

    private Datafield parseDataField(final XMLStreamReader streamReader) throws XMLStreamException {
        Datafield datafield = new Datafield();
        String tag = streamReader.getAttributeValue(null, "tag");
        datafield.setTag(tag);

        String indOne = streamReader.getAttributeValue(null, "ind1");
        if (StringUtils.isBlank(indOne)) {
            // TODO count as error
            indOne = " ";
        }
        datafield.setInd1(indOne.charAt(0));

        String indTwo = streamReader.getAttributeValue(null, "ind2");
        if (StringUtils.isBlank(indTwo)) {
            // TODO count as error
            indTwo = " ";
        }
        datafield.setInd2(indTwo.charAt(0));

        parseSubfields(streamReader, datafield);

        return datafield;
    }

    /**
     * Method parses the subfields for a given datafield. If subfields are found they will be added
     * to the datafield.
     *
     * @param streamReader the XML streamReader
     * @param datafield the datafield where the corresponding subfields should be added
     * @throws XMLStreamException
     *
     * TODO error handling (was wenn StartElement ist nicht SUB oder EndElement ist nicht DATA)
     */
    private void parseSubfields(final XMLStreamReader streamReader, final Datafield datafield)
            throws XMLStreamException {
        while (streamReader.hasNext()) {
            streamReader.next();
            if (streamReader.isStartElement() && streamReader.getLocalName().equals(TAG_SUBFIELD)) {
                Subfield subfield = new Subfield();
                subfield.setCode(streamReader.getAttributeValue(null, "code"));
                subfield.setValue(streamReader.getElementText());
                datafield.addSubfield(subfield);
            }
            else if (streamReader.isEndElement() && streamReader.getLocalName().equals(TAG_DATAFIELD)) {
                return;
            }
        }
    }

    public IMarcParserHandler getHandler() {
        return handler;
    }

    public void setHandler(final IMarcParserHandler phandler) {
        this.handler = phandler;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(final String dsourceName) {
        this.dataSourceName = dsourceName;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(final String encoding) {
        this.encoding = encoding;
    }
}
