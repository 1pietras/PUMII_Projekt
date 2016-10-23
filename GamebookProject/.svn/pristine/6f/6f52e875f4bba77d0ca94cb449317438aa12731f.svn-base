package pl.polsl.ptakjakub.gamebook.xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import pl.polsl.ptakjakub.gamebook.dto.Instruction;
import pl.polsl.ptakjakub.gamebook.dto.InstructionEntry;

/**
 * Class responsible for parsing instruction XML document.
 *
 * @author Jakub Ptak
 * @version 1.0
 */
public class InstructionXmlParser {

    private static String INSTRUCTION_TAG = "instruction";
    private static String INSTRUCTION_VERSION_TAG = "version";
    private static String INSTRUCTION_NAME_TAG = "name";
    private static String INSTRUCTION_ENTRY_TAG = "entry";
    private static String ENTRY_ID_TAG = "id";
    private static String ENTRY_NAME_TAG = "name";
    private static String ENTRY_DESCRIPTION_TAG = "description";

    private String namespace = "";

    /**
     * Parses instruction XML document.
     *
     * @param is file InputStream
     * @return Intruction object
     *
     * @throws XmlPullParserException
     * @throws IOException
     */
    public Instruction parse(InputStream is) throws  XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(is, null);
            parser.nextTag();
            return readInstruction(parser);
        } finally {
            is.close();
        }
    }

    /**
     * Reads instruction XML document.
     *
     * @param parser
     * @return Intruction object
     *
     * @throws IOException
     * @throws XmlPullParserException
     */
    private Instruction readInstruction(XmlPullParser parser) throws IOException, XmlPullParserException {
        Instruction instruction = new Instruction();
        LinkedList<InstructionEntry> instructionEntries = new LinkedList<InstructionEntry>();

        parser.require(XmlPullParser.START_TAG, namespace, INSTRUCTION_TAG);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tagName = parser.getName();
            namespace = parser.getNamespace();
            if (tagName.equals(INSTRUCTION_VERSION_TAG)) {
                instruction.setVesion(readInstructionVersion(parser));
            } else if (tagName.equals(INSTRUCTION_NAME_TAG)) {
                instruction.setName(readInstructionName(parser));
            } else if (tagName.equals(INSTRUCTION_ENTRY_TAG)) {
                instructionEntries.add(readEntry(parser));
            } else {
                skipTag(parser);
            }
        }
        instruction.setInstructionEntries(instructionEntries);

        return instruction;
    }

    /**
     * Reads entry tag from instruction XML document.
     *
     * @param parser
     * @return IntructionEntry object
     * @throws IOException
     * @throws XmlPullParserException
     */
    private InstructionEntry readEntry(XmlPullParser parser) throws IOException, XmlPullParserException {
        int id = 0;
        String name = null;
        String description = null;

        parser.require(XmlPullParser.START_TAG, null, INSTRUCTION_ENTRY_TAG);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tagName = parser.getName();
            namespace = parser.getNamespace();
            if (tagName.equals(ENTRY_ID_TAG)) {
                id = readEntryId(parser);
            } else if (tagName.equals(ENTRY_NAME_TAG)) {
                name = readEntryName(parser);
            } else if (tagName.equals(ENTRY_DESCRIPTION_TAG)) {
                description = readEntryDescription(parser);
            } else {
                skipTag(parser);
            }
        }

        return new InstructionEntry(id, name, description);
    }

    /**
     * Reads description tag of instruction's entry.
     *
     * @param parser
     * @return String containing description of entry
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readEntryDescription(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, namespace, ENTRY_DESCRIPTION_TAG);
        String description = readString(parser);
        parser.require(XmlPullParser.END_TAG, namespace, ENTRY_DESCRIPTION_TAG);

        return description;
    }

    /**
     * Reads name tag of instruction's entry
     *
     * @param parser
     * @return String containing name of entry
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readEntryName(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, namespace, ENTRY_NAME_TAG);
        String name = readString(parser);
        parser.require(XmlPullParser.END_TAG, namespace, ENTRY_NAME_TAG);

        return name;
    }

    /**
     * Reads id tag of instruction's entry
     *
     * @param parser
     * @return Id of entry as integer
     * @throws IOException
     * @throws XmlPullParserException
     */
    private int readEntryId(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, ENTRY_ID_TAG);
        int id = readInteger(parser);
        parser.require(XmlPullParser.END_TAG, null, ENTRY_ID_TAG);

        return id;
    }

    /**
     * Reads instruction's version string.
     *
     * @param parser
     * @return version string
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readInstructionVersion(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, namespace, INSTRUCTION_VERSION_TAG);
        String version = readString(parser);
        parser.require(XmlPullParser.END_TAG, namespace, INSTRUCTION_VERSION_TAG);

        return version;
    }

    /**
     * Reads instruction's name.
     *
     * @param parser
     * @return name of instruction
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readInstructionName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, namespace, INSTRUCTION_NAME_TAG);
        String name = readString(parser);
        parser.require(XmlPullParser.END_TAG, namespace, INSTRUCTION_NAME_TAG);

        return name;
    }

    /**
     * Reads Integer from XML document node.
     *
     * @param parser
     * @return Integer
     * @throws IOException
     * @throws XmlPullParserException
     */
    private Integer readInteger(XmlPullParser parser) throws IOException, XmlPullParserException {
        Integer result = null;
        if (parser.next() == XmlPullParser.TEXT) {
            result = Integer.parseInt(parser.getText());
            parser.nextTag();
        }
        return result;
    }

    /**
     * Reads String from XML document node.
     *
     * @param parser
     * @return String
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readString(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    /**
     * Skips tag in XML document.
     *
     * @param parser
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void skipTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }

        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
