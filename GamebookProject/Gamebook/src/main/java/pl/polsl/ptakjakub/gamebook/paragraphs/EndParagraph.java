package pl.polsl.ptakjakub.gamebook.paragraphs;

/**
 * Represents paragraph of 'end' type.
 *
 * @author Jakub Ptak
 * @version 1.0
 */
public class EndParagraph extends Paragraph {

    private String end;

    /**
     * Gets type of end paragraph.
     *
     * @return type
     */
    public String getEnd() {
        return end;
    }

    /**
     * Sets type of end paragraph
     *
     * @param end type
     */
    public void setEnd(String end) {
        this.end = end;
    }
}
