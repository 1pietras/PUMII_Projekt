package pl.polsl.ptakjakub.gamebook.dto;


import java.util.List;

/**
 * Class representing instruction of the game.
 *
 * @author Jakub Ptak
 * @version 1.0
 */
public class Instruction {

    private String vesion;
    private String name;
    private List<InstructionEntry> instructionEntries;

    /**
     * Retrieves version of the instruction.
     *
     * @return version
     */
    public String getVesion() {
        return vesion;
    }

    /**
     * Sets version of the instruction.
     *
     * @param vesion
     */
    public void setVesion(String vesion) {
        this.vesion = vesion;
    }

    /**
     * Retrieves name of the instrucion.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of the instrucion.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves list of instruction's entries.
     *
     * @return instructionEntries
     */
    public List<InstructionEntry> getInstructionEntries() {
        return instructionEntries;
    }

    /**
     * Sets list of instruction entries.
     *
     * @param instructionEntries
     */
    public void setInstructionEntries(List<InstructionEntry> instructionEntries) {
        this.instructionEntries = instructionEntries;
    }
}
