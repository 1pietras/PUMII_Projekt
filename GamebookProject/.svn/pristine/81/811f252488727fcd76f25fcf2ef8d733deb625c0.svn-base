package pl.polsl.ptakjakub.gamebook.dto;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.polsl.ptakjakub.gamebook.dto.Item;

/**
 * Class representing character in gamebook.
 *
 * @author Jakub Ptak
 * @version 1.0
 */
public class Player {

    private List<Item> items;

    private int vitality;

    private int maxVitality;

    private int agility;

    private int luck;

    private int foodAmount;


    /**
     * Constructor which initializes items list and food amount.
     */
    public Player() {
        items = new ArrayList<Item>();
        foodAmount = 4;
    }

    /**
     * Eats food from the bag and restores vitality points.
     * It returns 0 if there is no need for eating food.
     * Returns 1 if there is no food left.
     * Returns 2 if vitality points amount were increased by 4.
     * Returns 3 if vitality points were restored to the maximal level.
     *
     * @return restoring status
     */
    public int eatFood() {
        if ( vitality < maxVitality ) {
            if ( foodAmount > 0 ) {
                foodAmount--;
                vitality += 4;
                if ( vitality > maxVitality) {
                    vitality = maxVitality;
                    return 3;
                }
                return 2;
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }

    /**
     * Modifies player's agility by adding value passed as parameter.
     *
     * @param change value
     */
    public void modifyAgility(int change) {
        this.agility += change;
    }

    /**
     * Modifies player's vitality by adding value passed as parameter.
     * If change value is positive, maximal vitality is also increased.
     *
     * @param change value
     */
    public void modifyVitality(int change) {
        if ( change > 0 ) {
            this.maxVitality += change;
            this.vitality += change;
        } else {
            this.vitality += change;
        }
    }

    /**
     * Creates a description of player's statistics and items in bag.
     *
     * @return description
     */
    public String getPlayerDescription() {

        String description = "Your character has " + agility + " points of agility, " +
                vitality + " of " + maxVitality + " points of vitality, " +
                luck + " points of luck...";
        if ( foodAmount > 0 )
            description += "There is " + foodAmount + " food left in your bag. ";
        else
            description += "There is no more food left in your bag. ";

        if ( items.size() > 0 ) {
            if ( items.size() == 1 )
                description += " Your character is equipped with one item. ";
            else
                description += " Your character is equipped with " + items.size() + " items. ";

            for ( Item i : items ) {
                description += i.getName() + " of " + i.getType() + " type. ";
                if ( i.getType().equalsIgnoreCase("weapon") ) {
                    description += " It gives you " + i.getValue() + " points of agility. ";
                } else if ( i.getType().equalsIgnoreCase("armor") ) {
                    description += " It gives you " + i.getValue() + " points of vitality. ";
                } else if ( i.getType().equalsIgnoreCase("apparel") ) {
                    description += " It gives you " + i.getValue() + " points of luck. ";
                }
                description += " .. ";
            }
        }

        return description;
    }

    /**
     * Modifies player's luck by adding value passed as parameter.
     *
     * @param change value
     */
    public void modifyLuck(int change) {
        this.luck += change;
    }


    /**
     * Adds item to character's items list.
     *
     * @param item new item
     */
    public void addItem( Item item ) {
        items.add(item);
    }

    /**
     * Removes item from character's items list.
     *
     * @param item item to remove.
     */
    public void removeItem(Item item) {
        for( Item i : items ) {
            if ( i.getId() == item.getId() ) {
                items.remove(item);
                break;
            }
        }
    }

    /**
     * Check whether character owns item with specified id.
     *
     * @param id item id
     * @return true if owns an item, false otherwise
     */
    public boolean hasItem(int id) {
        for (Item i : items) {
            if ( i.getId() == id )
                return true;
        }

        return false;
    }

    /**
     * Gets character's vitality points.
     *
     * @return vitality
     */
    public int getVitality() {
        return vitality;
    }

    /**
     * Sets character's vitality points.
     *
     * @param vitality vitality points
     */
    public void setVitality(int vitality) {
        this.vitality = vitality;
    }

    /**
     * Gets character's agility points.
     *
     * @return agility
     */
    public int getAgility() {
        return agility;
    }

    /**
     * Sets character's agility points.
     *
     * @param agility agility points
     */
    public void setAgility(int agility) {
        this.agility = agility;
    }

    /**
     * Gets character's luck points.
     *
     * @return luck
     */
    public int getLuck() {
        return luck;
    }

    /**
     * Sets character's luck points.
     *
     * @param luck louck points
     */
    public void setLuck(int luck) {
        this.luck = luck;
    }

    /**
     * Gets list of characters items.
     *
     * @return items list
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Gets character's maximal vitality points number.
     *
     * @return maximal vitality
     */
    public int getMaxVitality() {
        return maxVitality;
    }

    /**
     * Sets character's maximal vitality points number.
     *
     * @param maxVitality vitality points number
     */
    public void setMaxVitality(int maxVitality) {
        this.maxVitality = maxVitality;
    }

    /**
     * Gets food amount in the bag.
     *
     * @return food amount
     */
    public int getFoodAmount() {
        return foodAmount;
    }

    /**
     * Sets food amount in the bag.
     *
     * @param foodAmount food amount
     */
    public void setFoodAmount(int foodAmount) {
        this.foodAmount = foodAmount;
    }
}
