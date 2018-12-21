package br.unisinos.simcop.impl.transformations;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.core.ContextPair;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.core.SimilarityResult;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.data.model.EntityAttribute;
import br.unisinos.simcop.data.model.LocationDescription;
import br.unisinos.simcop.data.model.Situation;
import br.unisinos.simcop.data.model.TimeDescription;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

/**
 *
 * @author tiago
 */
public class ResultFileOutput extends AbstractOutputTransformation {

    public static final String TYPE_CSV = "csv";
    public static final String TYPE_XML = "xml";
    public static final String FILE_TYPE = "fileType";
    public static final String CSV_QUOTE = "csvQuote";
    public static final String CSV_DELIMITER = "csvDelimiter";
    public static final String XML_IDENT = "xmlIdent";
    public static final String OUTPUT_FOLDER = "outputFolder";
    public static final String FILE_NAME = "fileName";
    public static final String XML_VERSION = "xmlVersion";
    public static final String XML_ENCODING = "xmlEncoding";
    public static final String FILE_COMMAND = "openFileCommand";

    public Parameters getDefaultParameters() {
        Parameters pars = new Parameters();
        pars.addParameter(FILE_TYPE, "csv");
        pars.addParameter(CSV_QUOTE, "true");
        pars.addParameter(CSV_DELIMITER, ";");
        pars.addParameter(XML_IDENT, "true");
        pars.addParameter(XML_VERSION, "1.0");
        pars.addParameter(XML_ENCODING, "utf-8");
        pars.addParameter(OUTPUT_FOLDER, System.getProperty("user.home"));
        pars.addParameter(FILE_NAME, "similarityResult");
        pars.addParameter(FILE_COMMAND, "");
        return pars;
    }

    private File createOutputFile(String folder, String fileName, String extension) {
        File result = new File(folder + fileName + "." + extension);
        int seq = 0;
        while (result.exists()) {
            seq++;
            result = new File(folder + fileName + "_" + seq + "." + extension);
        }
        return result;
    }

    public SimilarityResult transform(SimilarityResult similarityResult) {
        String folder = getSimpleParameter(OUTPUT_FOLDER);
        String fileName = getSimpleParameter(FILE_NAME);
        if (!Utils.isEmpty(folder) && !Utils.isEmpty(fileName)) {
            try {
                if (!folder.endsWith("/")) {
                    folder = folder + "/";
                }

                String fileType = getSimpleParameter("fileType");
                File outputFile = createOutputFile(folder, fileName, fileType);
                Writer outputWriter = new BufferedWriter(new FileWriter(outputFile));

                if (TYPE_CSV.equals(fileType)) {
                    saveToCSV(similarityResult, outputWriter);
                } else if (TYPE_XML.equals(fileType)) {
                    saveToXML(similarityResult, outputWriter);
                }

                String cmd = getSimpleParameter(FILE_COMMAND);
                if (!Utils.isEmpty(cmd)) {
                    Runtime.getRuntime().exec(cmd + " \"" + outputFile.getAbsolutePath() + "\"" );
                }
            } catch (Exception e) {
                Utils.log(e);
            }
        }
        return similarityResult;
    }

    public void saveToCSV(SimilarityResult sr, Writer out) throws Exception {
        String delim = getSimpleParameter(CSV_DELIMITER);
        boolean quote = "true".equalsIgnoreCase(getSimpleParameter(CSV_QUOTE));
        out.write("similarityValue=" + quote(quote, Double.toString(sr.getCalculatedValue())));
        out.write("\n");
        for (ContextPair pair : sr.getContextPairs()) {
            out.write( quote(quote, Double.toString(pair.getCalculatedValue()) ) );
            out.write(delim);
            printContextToCSV(out, "1", pair.getC1(), quote, delim);
            printContextToCSV(out, "2", pair.getC2(), quote, delim);
            out.write("\n");
        }
    }

    protected void printContextToCSV(Writer out, String index, Context ctx, boolean quote, String delim) throws Exception {
        out.write(quote(quote, "C" + index));
        out.write(delim);
        out.write(quote(quote, Long.toString(ctx.getIndex())));
        if (ctx.getTime() != null) {
            out.write(delim);
            out.write(quote(quote, ctx.getTime().toString()));
        }
        if (ctx.getLocation() != null) {
            out.write(delim);
            out.write( quote(quote, ctx.getLocation().toString()) );
        }

        for (Situation situation : ctx.getSituations()) {
            out.write(delim);
            StringBuilder sb = new StringBuilder();
            if (!Utils.isEmpty(situation.getSource())) {
                sb.append(situation.getSource());
                sb.append(".");
            }
            if (!Utils.isEmpty(situation.getAuxiliary())) {
                sb.append(situation.getAuxiliary());
                sb.append(".");
            }
            if (!Utils.isEmpty(situation.getPredicate())) {
                sb.append(situation.getPredicate());
                sb.append("=");
            }
            sb.append(quote(quote, situation.getValue().printValue(false)));
            out.write(sb.toString());
        }


    }

    private String quote(boolean quote, String value) {
        if (quote) {
            return "\"" + value + "\"";
        } else {
            return value;
        }
    }

    public void saveToXML(SimilarityResult sr, Writer out) throws Exception {
        String xmlIdent = getSimpleParameter(XML_IDENT);
        boolean ident = xmlIdent != null && xmlIdent.trim().equalsIgnoreCase("true");
        String xmlVersion = getSimpleParameter(XML_VERSION);
        String xmlEncoding = getSimpleParameter(XML_ENCODING);
        if (!Utils.isEmpty(xmlVersion) || !Utils.isEmpty(xmlEncoding)) {
            out.write("<?xml");
            if (!Utils.isEmpty(xmlVersion)) {
                out.write(" version='" + xmlVersion + "'");
            }
            if (!Utils.isEmpty(xmlEncoding)) {
                out.write(" encoding='" + xmlEncoding + "'");
            }
            out.write("?>");
        }

        if (ident) {
            out.write("\n");
        }

        int identLevel = 0;
        beginTag(out, "similarityResult", ident, identLevel);
        identLevel++;
        addTag(out, "similarityValue", Double.toString(sr.getCalculatedValue()), ident, identLevel);

        addSequenceTag(out, "sequenceA", sr.getS1(), ident, identLevel);
        addSequenceTag(out, "sequenceB", sr.getS2(), ident, identLevel);
        //sr.getCalculatedValue()

        for (ContextPair pair : sr.getContextPairs()) {
            beginTag(out, "contextPair index='" + pair.getIndex() + "'", ident, identLevel);
            identLevel++;

            addTag(out, "similarity", Double.toString(pair.getCalculatedValue()), ident, identLevel);
            Object add = pair.getAdditionalInfo();
            if (add != null) {
                String notes = "<![CDATA[" + add.toString() + "]]>";
                addTag(out, "additionalInfo", notes, ident, identLevel);
            }
            if (pair.getC1() != null) {
                addContextTag(out, pair.getC1(), "1", ident, identLevel);
            }
            if (pair.getC2() != null) {
                addContextTag(out, pair.getC2(), "2", ident, identLevel);
            }

            identLevel--;
            endTag(out, "contextPair", ident, identLevel);
        }

        identLevel--;
        endTag(out, "similarityResult", ident, identLevel);
    }

    protected void addTimeTag(Writer out, TimeDescription td, boolean ident, int identLevel) throws Exception {
        beginTag(out, "time", ident, identLevel);
        identLevel++;

        addTag(out, "index", td.getIndex(), ident, identLevel);
        addTag(out, "weekday", td.getWeekDay(), ident, identLevel);
        addTag(out, "day", td.getDay(), ident, identLevel);
        addTag(out, "month", td.getMonth(), ident, identLevel);
        addTag(out, "year", td.getYear(), ident, identLevel);
        addTag(out, "season", td.getSeason(), ident, identLevel);
        addTag(out, "age", td.getAge(), ident, identLevel);
        addTag(out, "hour", td.getHour(), ident, identLevel);
        addTag(out, "minute", td.getMinute(), ident, identLevel);
        addTag(out, "second", td.getSecond(), ident, identLevel);
        addTag(out, "millisecond", td.getMillisecond(), ident, identLevel);

        identLevel--;
        endTag(out, "time", ident, identLevel);
    }

    protected void addLocationTag(Writer out, LocationDescription ld, boolean ident, int identLevel) throws Exception {
        beginTag(out, "location", ident, identLevel);
        identLevel++;
        addTag(out, "uid", Long.toString(ld.getUid()), ident, identLevel);
        addTag(out, "index", ld.getIndex(), ident, identLevel);
        if (ld.getPosition() != null && ld.getPosition().length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ld.getPosition().length; i++) {
                sb.append(ld.getPosition()[i]);
                if (i < ld.getPosition().length - 1) {
                    sb.append(",");
                }
            }
            addTag(out, "position", sb.toString(), ident, identLevel);
        }
        addTag(out, "category", ld.getCategory(), ident, identLevel);
        addTag(out, "name", ld.getName(), ident, identLevel);
        addTag(out, "ontologyUID", ld.getOntologyUID(), ident, identLevel);
        addTag(out, "ontologyClass", ld.getOntologyClass(), ident, identLevel);
        identLevel--;
        endTag(out, "location", ident, identLevel);
    }

    protected void addSituationTag(Writer out, Situation situation, boolean ident, int identLevel) throws Exception {
        if (situation.getValue() != null) {
            StringBuilder attr = new StringBuilder();
            if (!Utils.isEmpty(situation.getSource())) {
                attr.append(" source='").append(situation.getSource()).append("'");
            }
            if (!Utils.isEmpty(situation.getAuxiliary())) {
                attr.append(" auxiliary='").append(situation.getAuxiliary()).append("'");
            }
            if (!Utils.isEmpty(situation.getPredicate())) {
                attr.append(" predicate='").append(situation.getPredicate()).append("'");
            }
            addTag(out, "situation", attr.toString().trim(), situation.getValue().printValue(false), ident, identLevel);
        }
    }

    protected void addContextTag(Writer out, Context context, String index, boolean ident, int identLevel) throws Exception {
        beginTag(out, "context" + index + " sequenceIndex='" + context.getIndex() + "'", ident, identLevel);
        identLevel++;
        addTimeTag(out, context.getTime(), ident, identLevel);
        addLocationTag(out, context.getLocation(), ident, identLevel);
        for (Situation situation : context.getSituations()) {
            addSituationTag(out, situation, ident, identLevel);
        }
        identLevel--;
        endTag(out, "context" + index, ident, identLevel);
    }

    protected void addSequenceTag(Writer out, String label, ContextSequence seq, boolean ident, int identLevel) throws Exception {
        beginTag(out, label, ident, identLevel);
        identLevel++;
        if (seq.getEntity().getUid() != null) {
            addTag(out, "entityUID", seq.getEntity().getUid().toString(), ident, identLevel);
        }
        addTag(out, "entityName", seq.getEntity().getName(), ident, identLevel);
        if (!seq.getEntity().getAttributes().isEmpty()) {
            beginTag(out, "entityAttributes", ident, identLevel);
            identLevel++;
            for (String key : seq.getEntity().getAttributes().keySet()) {
                EntityAttribute attr = seq.getEntity().getAttributes().get(key);
                addTag(out, "attr", "uid='" + attr.getUid() + "' label='" + attr.getLabel() + "'", attr.getValue().printValue(false), ident, identLevel);
            }
            identLevel--;
            endTag(out, "entityAttributes", ident, identLevel);
        }
        addTag(out, "sequenceSize", Integer.toString(seq.size()), ident, identLevel);
        String notes = seq.getNotes();
        if (!Utils.isEmpty(notes)) {
            notes = "<![CDATA[" + notes + "]]>";
            addTag(out, "notes", notes, ident, identLevel);
        }

        identLevel--;
        endTag(out, label, ident, identLevel);
    }

    protected void addIdent(StringBuilder sb, boolean ident, int identLevel) {
        if (ident) {
            for (int il = 0; il < identLevel; il++) {
                sb.append("   ");
            }
        }
    }

    protected void addTag(Writer out, String tag, String value, boolean ident, int identLevel) throws Exception {
        addTag(out, tag, null, value, ident, identLevel);
    }

    protected void addTag(Writer out, String tag, String attr, String value, boolean ident, int identLevel) throws Exception {
        StringBuilder sb = new StringBuilder();
        addIdent(sb, ident, identLevel);
        sb.append("<");
        sb.append(tag);
        if (attr != null) {
            sb.append(" ");
            sb.append(attr);
        }
        sb.append(">");
        sb.append(value);
        sb.append("</");
        sb.append(tag);
        sb.append(">");
        if (ident) {
            sb.append("\n");
        }
        out.write(sb.toString());
    }

    protected void beginTag(Writer out, String tag, boolean ident, int identLevel) throws Exception {
        StringBuilder sb = new StringBuilder();
        addIdent(sb, ident, identLevel);
        sb.append("<");
        sb.append(tag);
        sb.append(">");
        if (ident) {
            sb.append("\n");
        }
        out.write(sb.toString());
    }

    protected void endTag(Writer out, String tag, boolean ident, int identLevel) throws Exception {
        StringBuilder sb = new StringBuilder();
        addIdent(sb, ident, identLevel);
        sb.append("</");
        sb.append(tag);
        sb.append(">");
        if (ident) {
            sb.append("\n");
        }
        out.write(sb.toString());
    }

    private void addTag(Writer out, String tag, Integer value, boolean ident, int identLevel) throws Exception {
        if (value != null) {
            addTag(out, tag, Integer.toString(value), ident, identLevel);
        }
    }
}
