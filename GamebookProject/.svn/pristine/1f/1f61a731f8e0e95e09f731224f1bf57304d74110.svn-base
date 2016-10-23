package pl.polsl.ptakjakub.gamebook.paragraphs;

import pl.polsl.ptakjakub.gamebook.dto.Path;

/**
 * Represents a "crossroad" type paragraph.
 *
 * @author Jakub Ptak
 * @version 1.0
 */
public class CrossroadParagraph extends NormalParagraph {

    /**
     * Returns path of swipe up gesture.
     *
     * @return Path
     */
    public Path getUpSwipePath(){
        for ( Path p : getPaths())
            if ( p.getSwipe().equalsIgnoreCase("up") )
                return p;

        return null;
    }

    /**
     * Returns path of swipe down gesture.
     *
     * @return Path
     */
    public Path getDownSwipePath() {
        for ( Path p : getPaths() )
            if ( p.getSwipe().equalsIgnoreCase("down") )
                return p;

        return null;
    }
}
