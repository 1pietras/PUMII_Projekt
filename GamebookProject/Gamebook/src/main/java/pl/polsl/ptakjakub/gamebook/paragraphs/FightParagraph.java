package pl.polsl.ptakjakub.gamebook.paragraphs;

import pl.polsl.ptakjakub.gamebook.dto.Creature;
import pl.polsl.ptakjakub.gamebook.dto.Path;

/**
 * Class representing fight paragraph of XML document.
 *
 * @author Jakub Ptak
 * @version 1.0
 */
public class FightParagraph extends Paragraph {

    private Creature creature;

    private Path path;

    /**
     * Gets paragraph creature to fight with.
     *
     * @return creature
     */
    public Creature getCreature() {
        return creature;
    }

    /**
     * Sets paragraph creature to fight with.
     *
     * @param creature enemy
     */
    public void setCreature(Creature creature) {
        this.creature = creature;
    }

    /**
     * Gets path in case of fight win.
     *
     * @return path
     */
    public Path getPath() {
        return path;
    }

    /**
     * Sets path in case of fight win.
     *
     * @param path winning path
     */
    public void setPath(Path path) {
        this.path = path;
    }
}
