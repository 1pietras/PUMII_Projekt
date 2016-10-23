package pl.polsl.ptakjakub.gamebook.paragraphs;

/**
 * Class representing paragraph of an 'itempick' type
 *
 * @author Jakub Ptak
 * @version 1.0
 */
public class ItempickParagraph extends SinglepathParagraph {

    private Integer itemId;

    /**
     * Gets id of an item to be picked during current paragraph.
     *
     * @return itemId
     */
    public Integer getItemId() {
        return itemId;
    }

    /**
     * Sets id of and item to be picked during current paragraph.
     *
     * @param itemId id of an item
     */
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
}
