package pl.polsl.ptakjakub.gamebook.paragraphs;

import java.util.List;

import pl.polsl.ptakjakub.gamebook.dto.Effect;
import pl.polsl.ptakjakub.gamebook.dto.Path;

/**
 * Class representing 'food' type paragraph.
 *
 * @author Jakub Ptak
 * @version 1.0
 */
public class FoodParagraph extends Paragraph {

    private Path path;

    private List<Effect> effects;

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

    /**
     * Gets path of current paragraph.
     *
     * @return path
     */
    public Path getPath() {
        return path;
    }

    /**
     * Sets path of current paragraph/
     *
     * @param path next paragraph
     */
    public void setPath(Path path) {
        this.path = path;
    }
}
