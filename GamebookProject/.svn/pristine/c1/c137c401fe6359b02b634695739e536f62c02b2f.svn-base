package pl.polsl.ptakjakub.gamebook.paragraphs;

import java.util.List;

import pl.polsl.ptakjakub.gamebook.dto.Effect;
import pl.polsl.ptakjakub.gamebook.dto.Path;

/**
 * Represents a "normal" type paragraph.
 *
 * @author Jakub Ptak
 * @version 1.0
 */
public class NormalParagraph extends Paragraph {

    private List<Path> paths;

    private List<Effect> effects;

    /**
     * Returns path of swipe left gesture.
     *
     * @return Path
     */
    public Path getLeftSwipePath() {
        for ( Path p : paths )
            if ( p.getSwipe().equalsIgnoreCase("left") )
                return p;

        return null;
    }

    /**
     * Returns path of swipe right gesture.
     *
     * @return Path
     */
    public Path getRightSwipePath() {
        for ( Path p : paths )
            if ( p.getSwipe().equalsIgnoreCase("right") )
                return p;

        return null;
    }

    /**
     * Gets list of possible paths.
     *
     * @return paths list
     */
    public List<Path> getPaths() {
        return paths;
    }

    /**
     * Sets list of possible paths
     *
     * @param paths list
     */
    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }

    /**
     * Gets list of effects.
     *
     * @return effects list
     */
    public List<Effect> getEffects() {
        return effects;
    }

    /**
     * Sets list of effects.
     *
     * @param effects list
     */
    public void setEffects(List<Effect> effects) {
        this.effects = effects;
    }
}
