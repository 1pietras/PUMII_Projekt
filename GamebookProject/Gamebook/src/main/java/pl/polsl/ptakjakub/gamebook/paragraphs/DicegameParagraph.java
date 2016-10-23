package pl.polsl.ptakjakub.gamebook.paragraphs;

import java.util.List;

import pl.polsl.ptakjakub.gamebook.dto.Range;

/**
 * Class representing 'dicegame' type paragraph.
 *
 * @author Jakub Ptak
 * @version 1.0
 */
public class DicegameParagraph extends Paragraph {

    private List<Range> ranges;

    /**
     * Gets range for a chosen value specified as parameter.
     *
     * @param value from range
     * @return range for given value
     */
    public Range getRangeForValue(int value) {
        for ( Range r : ranges)
            if ( r.getFrom() <= value && r.getTo() >= value )
                return r;

        return null;
    }

    /**
     * Gets ranges of a dicegame.
     *
     * @return ranges
     */
    public List<Range> getRanges() {
        return ranges;
    }

    /**
     * Sets ranges of dicegame.
     *
     * @param ranges list
     */
    public void setRanges(List<Range> ranges) {
        this.ranges = ranges;
    }
}
