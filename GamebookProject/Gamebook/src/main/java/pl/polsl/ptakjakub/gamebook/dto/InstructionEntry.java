package pl.polsl.ptakjakub.gamebook.dto;

/**
 * Class representing single instruction's entry.
 *
 * @author Jakub Ptak
 * @version 1.0
 */
public class InstructionEntry {

    private int id;
    private String entryName;
    private String entryDescription;

    /**
     * Constructor initializes new instruction's entry object.
     *
     * @param id id of entry
     * @param entryName entry name
     * @param entryDescription entry description
     */
    public InstructionEntry(int id, String entryName, String entryDescription) {
        this.id = id;
        this.entryName = entryName;
        this.entryDescription = entryDescription;
    }

    /**
     * Retrieves id of the instruction's entry.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id of the instruction's entry.
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves name of the instruction's entry.
     *
     * @return entryName
     */
    public String getEntryName() {
        return entryName;
    }

    /**
     * Sets name of the instruction's entry.
     *
     * @param entryName
     */
    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    /**
     * Retrieves description of the instruction's entry.
     *
     * @return Entry description
     */
    public String getEntryDescription() {
        return entryDescription;
    }

    /**
     * Sets description of the instruction's entry.
     *
     * @param entryDescription
     */
    public void setEntryDescription(String entryDescription) {
        this.entryDescription = entryDescription;
    }
}
