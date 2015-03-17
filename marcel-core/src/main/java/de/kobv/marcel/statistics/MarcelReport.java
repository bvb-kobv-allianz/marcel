package de.kobv.marcel.statistics;

import de.kobv.marcel.parser.DefaultMarcParserHandler;
import de.kobv.marcel.util.Util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * Created by jens on 2/21/14.
 *
 * TODO split into modules
 */
public class MarcelReport {

    /**
     * Missing fields.
     */
    private MissingFields missingFields;

    /**
     * MARC field statistics.
     */
    private FieldStatistics fieldStatistics;

    private ValidateDataField validation;

    private DefaultMarcParserHandler handler;

    private String filename;

    public void writeReport() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(getFilename() + "_report.txt"));
        createStatisticsDocument(writer);
        writer.close();
    }

    /**
     * Gibt Statistik Report aus.
     *
     * TODO überarbeiten
     * TODO Sinnvolles Ausgabeformat + in Datei schreiben
     * TODO use outputstream (Bildschirm, Datei, alles möglich)
     *
     */
    public void createStatisticsDocument(final PrintWriter writer) {
        writer.println("Number of Records:");
        writer.println("  Total      : " + handler.getNumRecordsTotal());
        writer.println("  Processed  : " + handler.getNumRecordsProcessed());
        writer.println("  Without UID: " + handler.getNumRecordsWithoutUid());
        writer.println("  Invalid    : " + validation.getNonValidRecords().size());

        if (validation.getNonValidRecords().size() > 0) {
            writer.println("Invalid Records:");
            addMapValues(writer, validation.getNonValidRecords());
        }

        writer.println("Missing fields:");
        addMapValues(writer, missingFields.getMissingFields());

        writer.println("a2 allocations:");

        AllocationFormat format = new AllocationFormat(3, handler.getNumRecordsProcessed());

        List<String> a2List = Util.asSortedList(fieldStatistics.getA2Allocations().keySet());
        for (String field : a2List) {
            format.format(writer, fieldStatistics.getA2Allocations().get(field));
        }

        writer.println("Field allocations:");
        List<String> fieldList = Util.asSortedList(fieldStatistics.getFieldAllocations().keySet());
        int maxTotal = 0;
        for (String field : fieldList) {
            int total = fieldStatistics.getFieldAllocations().get(field).getTotal();
            if (total > maxTotal) {
                maxTotal = total;
            }
        }
        int totalDigits = Integer.toString(maxTotal).length();
        format = new AllocationFormat(totalDigits, handler.getNumRecordsProcessed());

        for (String field : fieldList) {
            format.format(writer, fieldStatistics.getFieldAllocations().get(field));
        }
    }

    /**
     * Outputs a Map.
     * @param writer
     * @param map
     */
    private void addMapValues(final PrintWriter writer, final Map<String, List<String>> map) {
        for (String key : map.keySet()) {
            writer.println(key + ": " + map.get(key).toString());
        }
    }

    public MissingFields getMissingFields() {
        return missingFields;
    }

    public void setMissingFields(final MissingFields missing) {
        this.missingFields = missing;
    }

    public FieldStatistics getFieldStatistics() {
        return fieldStatistics;
    }

    public void setFieldStatistics(final FieldStatistics fstatistics) {
        this.fieldStatistics = fstatistics;
    }

    public ValidateDataField getValidation() {
        return validation;
    }

    public void setValidation(final ValidateDataField dfValidation) {
        this.validation = dfValidation;
    }

    public DefaultMarcParserHandler getHandler() {
        return handler;
    }

    public void setHandler(final DefaultMarcParserHandler parserHandler) {
        this.handler = parserHandler;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(final String fname) {
        this.filename = fname;
    }

}
