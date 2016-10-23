package pl.polsl.ptakjakub.gamebook.xml;

import android.os.Parcelable;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import pl.polsl.ptakjakub.gamebook.dto.Creature;
import pl.polsl.ptakjakub.gamebook.dto.Effect;
import pl.polsl.ptakjakub.gamebook.dto.Item;
import pl.polsl.ptakjakub.gamebook.dto.Path;
import pl.polsl.ptakjakub.gamebook.dto.Range;
import pl.polsl.ptakjakub.gamebook.exceptions.GamebookException;
import pl.polsl.ptakjakub.gamebook.paragraphs.CheckluckParagraph;
import pl.polsl.ptakjakub.gamebook.paragraphs.CrossroadParagraph;
import pl.polsl.ptakjakub.gamebook.paragraphs.DicegameParagraph;
import pl.polsl.ptakjakub.gamebook.paragraphs.EndParagraph;
import pl.polsl.ptakjakub.gamebook.paragraphs.FightParagraph;
import pl.polsl.ptakjakub.gamebook.paragraphs.FoodParagraph;
import pl.polsl.ptakjakub.gamebook.paragraphs.ItemlossParagraph;
import pl.polsl.ptakjakub.gamebook.paragraphs.ItempickParagraph;
import pl.polsl.ptakjakub.gamebook.paragraphs.NormalParagraph;
import pl.polsl.ptakjakub.gamebook.paragraphs.SinglepathParagraph;

/**
 * Class for xml parsing operation using the XPath.
 *
 * @author Jakub Ptak
 * @version 1.0
 */
public class XPathGamebookParser {

    private static String GET_PARAGRAPH_TYPE = "/gamebook/paragraph[@id='?']/@type";
    private static String GET_NORMAL_PARAGRAPH = "/gamebook/paragraph[@type='normal'][@id='?']";
    private static String GET_SINGLEPATH_PARAGRAPH = "/gamebook/paragraph[@type='singlepath'][@id='?']";
    private static String GET_FOOD_PARAGRAPH = "/gamebook/paragraph[@type='food'][@id='?']";
    private static String GET_DICEGAME_PARAGRAPH = "/gamebook/paragraph[@type='dicegame'][@id='?']";
    private static String GET_END_PARAGRAPH = "/gamebook/paragraph[@type='end'][@id='?']";
    private static String GET_ITEMPICK_PARAGRAPH = "/gamebook/paragraph[@type='itempick'][@id='?']";
    private static String GET_ITEMLOSS_PARAGRAPH = "/gamebook/paragraph[@type='itemloss'][@id='?']";
    private static String GET_CROSSROAD_PARAGRAPH = "/gamebook/paragraph[@type='crossroad'][@id='?']";
    private static String GET_CHECKLUCK_PARAGRAPH = "/gamebook/paragraph[@type='checkluck'][@id='?']";
    private static String GET_FIGHT_PARAGRAPH = "/gamebook/paragraph[@type='fight'][@id='?']";
    private static String GET_GAME_ITEMS = "/gamebook/items/item";
    private static String GET_ITEM = "/gamebook/items/item[@id='?']";
    private static String GET_GAME_CREATURES = "/gamebook/creatures/creature";
    private static String GET_CREATURE = "/gamebook/creatures/creature[@id='?']";

    private static String TEXT_NODE = "text";
    private static String PATHS_NODE = "paths";
    private static String PATH_NODE = "path";
    private static String ITEM_NODE = "item";
    private static String CREATURE_NODE = "creature";
    private static String EFFECTS_NODE = "effects";
    private static String EFFECT_NODE = "effect";
    private static String LUCKYPATH_NODE = "luckypath";
    private static String UNLUCKYPATH_NODE = "unluckypath";
    private static String RANGES_NODE = "ranges";
    private static String RANGE_NODE = "range";
    private static String END_NODE = "end";
    private static String FIGHT_NODE = "fight";

    private static String ID_ATTR = "id";
    private static String TYPE_ATTR = "type";
    private static String ATTR_ATTR = "attr";
    private static String VALUE_ATTR = "value";
    private static String AGI_ATTR = "agi";
    private static String VIT_ATTR = "vit";
    private static String SWIPE_ATTR = "swipe";
    private static String FROM_ATTR = "from";
    private static String TO_ATTR = "to";
    private static String REQUIRE_ATTR = "require";

    private XPath xPath;
    private Document xmlDocument;

    /**
     * Initializes gamebook XPath parser.
     *
     * @param is InputStream of an XML document
     * @throws GamebookException
     */
    public XPathGamebookParser(InputStream is) throws GamebookException {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = builderFactory.newDocumentBuilder();

            xmlDocument = builder.parse(is);

            xPath = XPathFactory.newInstance().newXPath();
        } catch (ParserConfigurationException pce) {
            Log.e(this.getClass().getName(), pce.getMessage());
            throw new GamebookException(pce);
        } catch (IOException ioe) {
            Log.e(this.getClass().getName(), ioe.getMessage());
            throw new GamebookException(ioe);
        } catch (SAXException saxe) {
            Log.e(this.getClass().getName(), saxe.getMessage());
            throw new GamebookException(saxe);
        }
    }

    /**
     * Gets a paragraph type under a specified id.
     *
     * @param id paragraph's id
     * @return type
     * @throws GamebookException
     */
    public String getParagraphType(Integer id) throws GamebookException {

        try {
            String expression = GET_PARAGRAPH_TYPE.replace("?", id.toString());
            return xPath.compile(expression).evaluate(xmlDocument).toLowerCase();
        } catch (XPathExpressionException xpathee) {
            Log.e(this.getClass().getName(), xpathee.getMessage());
            throw new GamebookException(xpathee);
        }
    }

    /**
     * Gets item object from XML document.
     *
     * @param id
     * @return Item
     * @throws GamebookException
     */
    public Item getItem(Integer id) throws GamebookException {
        try {
            String expression = GET_ITEM.replace("?", id.toString());
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
            if (nodeList.getLength() > 0) {
                Node node = nodeList.item(0);

                Item item = new Item();
                item.setId(id);

                String name = node.getFirstChild().getTextContent().trim();
                if (name != null && !name.equals(""))
                    item.setName(name);

                String type = node.getAttributes().getNamedItem(TYPE_ATTR).getNodeValue().trim();
                if (type != null && !type.equals(""))
                    item.setType(type);

                String attr = node.getAttributes().getNamedItem(ATTR_ATTR) != null ? node.getAttributes().getNamedItem(ATTR_ATTR).getNodeValue().trim() : null;
                if (attr != null && !attr.equals(""))
                    item.setAttribute(attr);

                String value = node.getAttributes().getNamedItem(VALUE_ATTR) != null ? node.getAttributes().getNamedItem(VALUE_ATTR).getNodeValue().trim() : null;
                if (value != null && !value.equals(""))
                    item.setValue(Integer.parseInt(value));

                return item;
            } else {
                return null;
            }
        } catch (XPathExpressionException xpathee) {
            Log.e(this.getClass().getName(), xpathee.getMessage());
            throw new GamebookException(xpathee);
        } catch (NumberFormatException nfe) {
            Log.e(this.getClass().getName(), nfe.getMessage());
            throw new GamebookException(nfe);
        }
    }

    /**
     * Retrieves FightParagraph object from XML document.
     *
     * @param id paragraph's id
     * @return FightParagraph object
     * @throws GamebookException
     */
    public FightParagraph getFightParagraph(Integer id) throws GamebookException {

        FightParagraph paragraph = new FightParagraph();
        paragraph.setId(id);

        try {
            String expression = GET_FIGHT_PARAGRAPH.replace("?", id.toString());
            Node pNode = (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);

            if (pNode != null) {

                NodeList nodeList = pNode.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node childNode = nodeList.item(i);
                    if (childNode.getNodeName().equalsIgnoreCase(TEXT_NODE)) {
                        paragraph.setText(getParagraphText(childNode));

                    } else if (childNode.getNodeName().equalsIgnoreCase(PATH_NODE)) {
                        paragraph.setPath(getParagraphPath(childNode));

                    } else if (childNode.getNodeName().equalsIgnoreCase(CREATURE_NODE)) {
                        paragraph.setCreature(getParagraphCreature(childNode));
                    }

                }
            }

            return paragraph;
        } catch (XPathExpressionException xpathee) {
            Log.e(this.getClass().getName(), xpathee.getMessage());
            throw new GamebookException(xpathee);
        } catch (NumberFormatException nfe) {
            Log.e(this.getClass().getName(), nfe.getMessage());
            throw new GamebookException(nfe);
        }
    }


    /**
     * Retrieves ItempickParagraph object from XML document.
     *
     * @param id paragraph's id
     * @return ItempickParagraph object
     * @throws GamebookException
     */
    public ItempickParagraph getItempickParagraph(Integer id) throws GamebookException {

        ItempickParagraph paragraph = new ItempickParagraph();
        paragraph.setId(id);

        try {
            String expression = GET_ITEMPICK_PARAGRAPH.replace("?", id.toString());
            Node pNode = (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);

            if (pNode != null) {

                NodeList nodeList = pNode.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node childNode = nodeList.item(i);
                    if (childNode.getNodeName().equalsIgnoreCase(TEXT_NODE)) {
                        paragraph.setText(getParagraphText(childNode));

                    } else if (childNode.getNodeName().equals(PATH_NODE)) {
                        paragraph.setPath(getParagraphPath(childNode));

                    } else if (childNode.getNodeName().equals(EFFECTS_NODE)) {
                        paragraph.setEffects(getParagraphEffects(childNode));

                    } else if (childNode.getNodeName().equals(ITEM_NODE)) {
                        String itemId = childNode.getFirstChild().getTextContent().trim();
                        if (itemId != null && !itemId.equals(""))
                            paragraph.setItemId(Integer.parseInt(itemId));
                    }

                }
            }

            return paragraph;
        } catch (XPathExpressionException xpathee) {
            Log.e(this.getClass().getName(), xpathee.getMessage());
            throw new GamebookException(xpathee);
        } catch (NumberFormatException nfe) {
            Log.e(this.getClass().getName(), nfe.getMessage());
            throw new GamebookException(nfe);
        }
    }

    /**
     * Retrieves ItemlossParagraph object from XML document.
     *
     * @param id paragraph's id
     * @return ItemlossParagraph object
     * @throws GamebookException
     */
    public ItemlossParagraph getItemlossParagraph(Integer id) throws GamebookException {

        ItemlossParagraph paragraph = new ItemlossParagraph();
        paragraph.setId(id);

        try {
            String expression = GET_ITEMLOSS_PARAGRAPH.replace("?", id.toString());
            Node pNode = (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);

            if (pNode != null) {

                NodeList nodeList = pNode.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node childNode = nodeList.item(i);
                    if (childNode.getNodeName().equalsIgnoreCase(TEXT_NODE)) {
                        paragraph.setText(getParagraphText(childNode));

                    } else if (childNode.getNodeName().equals(PATH_NODE)) {
                        paragraph.setPath(getParagraphPath(childNode));

                    } else if (childNode.getNodeName().equals(EFFECTS_NODE)) {
                        paragraph.setEffects(getParagraphEffects(childNode));

                    } else if (childNode.getNodeName().equals(ITEM_NODE)) {
                        String itemId = childNode.getFirstChild().getTextContent().trim();
                        if (itemId != null && !itemId.equals(""))
                            paragraph.setItemId(Integer.parseInt(itemId));
                    }

                }
            }

            return paragraph;
        } catch (XPathExpressionException xpathee) {
            Log.e(this.getClass().getName(), xpathee.getMessage());
            throw new GamebookException(xpathee);
        } catch (NumberFormatException nfe) {
            Log.e(this.getClass().getName(), nfe.getMessage());
            throw new GamebookException(nfe);
        }

    }

    /**
     * Retrieves SinglepathParagraph object from XML document.
     *
     * @param id paragraph's id
     * @return SinglepathParagraph object
     * @throws GamebookException
     */
    public SinglepathParagraph getSinglepathParagraph(Integer id) throws GamebookException {

        SinglepathParagraph paragraph = new SinglepathParagraph();
        paragraph.setId(id);

        try {
            String expression = GET_SINGLEPATH_PARAGRAPH.replace("?", id.toString());
            Node pNode = (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);

            if (pNode != null) {

                NodeList nodeList = pNode.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node childNode = nodeList.item(i);
                    if (childNode.getNodeName().equalsIgnoreCase(TEXT_NODE)) {
                        paragraph.setText(getParagraphText(childNode));

                    } else if (childNode.getNodeName().equals(PATH_NODE)) {
                        paragraph.setPath(getParagraphPath(childNode));

                    } else if (childNode.getNodeName().equals(EFFECTS_NODE)) {
                        paragraph.setEffects(getParagraphEffects(childNode));

                    }

                }
            }

            return paragraph;
        } catch (XPathExpressionException xpathee) {
            Log.e(this.getClass().getName(), xpathee.getMessage());
            throw new GamebookException(xpathee);
        } catch (NumberFormatException nfe) {
            Log.e(this.getClass().getName(), nfe.getMessage());
            throw new GamebookException(nfe);
        }
    }

    /**
     * Retrieves FoodParagraph object from XML document.
     *
     * @param id paragraph's id
     * @return FoodParagraph object
     * @throws GamebookException
     */
    public FoodParagraph getFoodParagraph(Integer id) throws GamebookException {

        FoodParagraph paragraph = new FoodParagraph();
        paragraph.setId(id);

        try {
            String expression = GET_FOOD_PARAGRAPH.replace("?", id.toString());
            Node pNode = (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);

            if (pNode != null) {

                NodeList nodeList = pNode.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node childNode = nodeList.item(i);
                    if (childNode.getNodeName().equalsIgnoreCase(TEXT_NODE)) {
                        paragraph.setText(getParagraphText(childNode));

                    } else if (childNode.getNodeName().equals(PATH_NODE)) {
                        paragraph.setPath(getParagraphPath(childNode));

                    } else if (childNode.getNodeName().equals(EFFECTS_NODE)) {
                        paragraph.setEffects(getParagraphEffects(childNode));

                    }

                }
            }

            return paragraph;
        } catch (XPathExpressionException xpathee) {
            Log.e(this.getClass().getName(), xpathee.getMessage());
            throw new GamebookException(xpathee);
        } catch (NumberFormatException nfe) {
            Log.e(this.getClass().getName(), nfe.getMessage());
            throw new GamebookException(nfe);
        }
    }

    /**
     * Retrieves a NormalParagraph object from XML document.
     *
     * @param id paragraph's id
     * @return NormalParagraph object
     * @throws GamebookException
     */
    public NormalParagraph getNormalParagraph(Integer id) throws GamebookException {

        NormalParagraph paragraph = new NormalParagraph();
        paragraph.setId(id);

        try {
            String expression = GET_NORMAL_PARAGRAPH.replace("?", id.toString());
            Node pNode = (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);

            if (pNode != null) {

                NodeList nodeList = pNode.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node childNode = nodeList.item(i);
                    if (childNode.getNodeName().equalsIgnoreCase(TEXT_NODE)) {
                        paragraph.setText(getParagraphText(childNode));

                    } else if (childNode.getNodeName().equals(PATHS_NODE)) {
                        paragraph.setPaths(getParagraphPaths(childNode));

                    } else if (childNode.getNodeName().equals(EFFECTS_NODE)) {
                        paragraph.setEffects(getParagraphEffects(childNode));
                    }
                }
            }

            return paragraph;
        } catch (XPathExpressionException xpathee) {
            Log.e(this.getClass().getName(), xpathee.getMessage());
            throw new GamebookException(xpathee);
        } catch (NumberFormatException nfe) {
            Log.e(this.getClass().getName(), nfe.getMessage());
            throw new GamebookException(nfe);
        }
    }

    /**
     * Retrieves a CrossroadParagraph object from XML document;
     *
     * @param id paragraph's id
     * @return CrossroadParagraph object
     * @throws GamebookException
     */
    public CrossroadParagraph getCrossroadParagraph(Integer id) throws GamebookException {

        CrossroadParagraph paragraph = new CrossroadParagraph();
        paragraph.setId(id);

        try {
            String expression = GET_CROSSROAD_PARAGRAPH.replace("?", id.toString());
            Node pNode = (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);

            if (pNode != null) {

                NodeList nodeList = pNode.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node childNode = nodeList.item(i);
                    if (childNode.getNodeName().equalsIgnoreCase(TEXT_NODE)) {
                        paragraph.setText(getParagraphText(childNode));

                    } else if (childNode.getNodeName().equals(PATHS_NODE)) {
                        paragraph.setPaths(getParagraphPaths(childNode));

                    } else if (childNode.getNodeName().equals(EFFECTS_NODE)) {
                        paragraph.setEffects(getParagraphEffects(childNode));
                    }
                }
            }

            return paragraph;
        } catch (XPathExpressionException xpathee) {
            Log.e(this.getClass().getName(), xpathee.getMessage());
            throw new GamebookException(xpathee);
        } catch (NumberFormatException nfe) {
            Log.e(this.getClass().getName(), nfe.getMessage());
            throw new GamebookException(nfe);
        }
    }

    /**
     * Retrieves CheckluckParagraph object from XML document.
     *
     * @param id paragraph's id
     * @return CheckluckParagraph object.
     * @throws GamebookException
     */
    public CheckluckParagraph getCheckluckParagraph(Integer id) throws GamebookException {

        CheckluckParagraph paragraph = new CheckluckParagraph();
        paragraph.setId(id);

        try {
            String expression = GET_CHECKLUCK_PARAGRAPH.replace("?", id.toString());
            Node pNode = (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);

            if (pNode != null) {

                NodeList nodeList = pNode.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node childNode = nodeList.item(i);
                    if (childNode.getNodeName().equalsIgnoreCase(TEXT_NODE)) {
                        paragraph.setText(getParagraphText(childNode));

                    } else if (childNode.getNodeName().equals(LUCKYPATH_NODE)) {
                        paragraph.setLuckyPath(getParagraphPath(childNode));

                    } else if (childNode.getNodeName().equals(UNLUCKYPATH_NODE)) {
                        paragraph.setUnluckPath(getParagraphPath(childNode));
                    }
                }
            }

            return paragraph;
        } catch (XPathExpressionException xpathee) {
            Log.e(this.getClass().getName(), xpathee.getMessage());
            throw new GamebookException(xpathee);
        } catch (NumberFormatException nfe) {
            Log.e(this.getClass().getName(), nfe.getMessage());
            throw new GamebookException(nfe);
        }
    }

    /**
     * Retrieves DicegameParagraph object from XML document.
     *
     * @param id paragraph's id
     * @return DicegameParagraph object
     * @throws GamebookException
     */
    public DicegameParagraph getDicegameParagraph(Integer id) throws GamebookException {

        DicegameParagraph paragraph = new DicegameParagraph();
        paragraph.setId(id);

        try {
            String expression = GET_DICEGAME_PARAGRAPH.replace("?", id.toString());
            Node pNode = (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);

            if (pNode != null) {

                NodeList nodeList = pNode.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node childNode = nodeList.item(i);
                    if (childNode.getNodeName().equalsIgnoreCase(TEXT_NODE)) {
                        paragraph.setText(getParagraphText(childNode));

                    } else if (childNode.getNodeName().equals(RANGES_NODE)) {
                        paragraph.setRanges(getParagraphRanges(childNode));

                    }
                }
            }

            return paragraph;
        } catch (XPathExpressionException xpathee) {
            Log.e(this.getClass().getName(), xpathee.getMessage());
            throw new GamebookException(xpathee);
        } catch (NumberFormatException nfe) {
            Log.e(this.getClass().getName(), nfe.getMessage());
            throw new GamebookException(nfe);
        }
    }

    /**
     * Retrieves EndParagraph object from XML document.
     *
     * @param id paragraph's id
     * @return EndParagraph object
     * @throws GamebookException
     */
    public EndParagraph getEndParagraph(Integer id) throws GamebookException {

        EndParagraph paragraph = new EndParagraph();
        paragraph.setId(id);

        try {
            String expression = GET_END_PARAGRAPH.replace("?", id.toString());
            Node pNode = (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);

            if (pNode != null) {

                NodeList nodeList = pNode.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node childNode = nodeList.item(i);
                    if (childNode.getNodeName().equalsIgnoreCase(TEXT_NODE)) {
                        paragraph.setText(getParagraphText(childNode));

                    } else if (childNode.getNodeName().equals(END_NODE)) {
                        String endType = childNode.getFirstChild().getTextContent().toLowerCase().trim();
                        if (endType != null && !endType.equals(""))
                            paragraph.setEnd(endType);
                    }
                }
            }

            return paragraph;
        } catch (XPathExpressionException xpathee) {
            Log.e(this.getClass().getName(), xpathee.getMessage());
            throw new GamebookException(xpathee);
        } catch (NumberFormatException nfe) {
            Log.e(this.getClass().getName(), nfe.getMessage());
            throw new GamebookException(nfe);
        }
    }

    /**
     * Gets list of a game items stored in game XML document.
     *
     * @return items list
     * @throws GamebookException
     */
    public List<Item> getGameItemList() throws GamebookException {

        List<Item> itemList = new ArrayList<Item>();

        try {
            NodeList nodeList = (NodeList) xPath.compile(GET_GAME_ITEMS).evaluate(xmlDocument, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Item item = new Item();
                Node node = nodeList.item(i);

                String id = node.getAttributes().getNamedItem(ID_ATTR).getNodeValue().trim();
                if (id != null && !id.equals(""))
                    item.setId(Integer.parseInt(id));

                String name = node.getFirstChild().getTextContent().trim();
                if (name != null && !name.equals(""))
                    item.setName(name);

                String type = node.getAttributes().getNamedItem(TYPE_ATTR).getNodeValue().trim();
                if (type != null && !type.equals(""))
                    item.setType(type);

                String attr = node.getAttributes().getNamedItem(ATTR_ATTR) != null ? node.getAttributes().getNamedItem(ATTR_ATTR) .getNodeValue().trim() : null;
                if (attr != null && !attr.equals(""))
                    item.setAttribute(attr);

                String value = node.getAttributes().getNamedItem(VALUE_ATTR)!= null ? node.getAttributes().getNamedItem(VALUE_ATTR).getNodeValue().trim() : null;
                if (value != null && !value.equals(""))
                    item.setValue(Integer.parseInt(value));

                itemList.add(item);
            }

            return itemList;
        } catch (XPathExpressionException xpathee) {
            Log.e(this.getClass().getName(), xpathee.getMessage());
            throw new GamebookException(xpathee);
        } catch (NumberFormatException nfe) {
            Log.e(this.getClass().getName(), nfe.getMessage());
            throw new GamebookException(nfe);
        }
    }

    /**
     * Retrives list of game's creatures stored in XML document.
     *
     * @return creatures list
     * @throws GamebookException
     */
    public List<Creature> getGameCreatureList() throws GamebookException {
        List<Creature> creaturesList = new ArrayList<Creature>();

        try {
            NodeList nodeList = (NodeList) xPath.compile(GET_GAME_CREATURES).evaluate(xmlDocument, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeName().equalsIgnoreCase(CREATURE_NODE)) {
                    creaturesList.add(getParagraphCreature(node));
                }
            }

            return creaturesList;
        } catch (XPathExpressionException xpathee) {
            Log.e(this.getClass().getName(), xpathee.getMessage());
            throw new GamebookException(xpathee);
        } catch (NumberFormatException nfe) {
            Log.e(this.getClass().getName(), nfe.getMessage());
            throw new GamebookException(nfe);
        }
    }

    /**
     * Retrieves creature from node.
     *
     * @param node selected node
     * @return creature
     * @throws GamebookException
     */
    private Creature getParagraphCreature(Node node) throws GamebookException{
        try {
            Creature creature = new Creature();

            String id = node.getFirstChild().getTextContent();
            if (id != null && !id.equals(""))
                creature.setId(Integer.parseInt(id));

            String expression = GET_CREATURE.replace("?", id);
            Node creatureNode = (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);
            String name = creatureNode.getFirstChild().getTextContent().trim();
            if (name != null && !name.equals(""))
                creature.setName(name);

            String agility = creatureNode.getAttributes().getNamedItem(AGI_ATTR).getNodeValue().trim();
            if (agility != null && !agility.equals(""))
                creature.setAgility(Integer.parseInt(agility));

            String vitality = creatureNode.getAttributes().getNamedItem(VIT_ATTR).getNodeValue().trim();
            if (vitality != null && !vitality.equals(""))
                creature.setVitality(Integer.parseInt(vitality));

            return creature;
        } catch (XPathExpressionException xpathee) {
            Log.e(this.getClass().getName(), xpathee.getMessage());
            throw new GamebookException(xpathee);
        } catch (NumberFormatException nfe) {
            Log.e(this.getClass().getName(), nfe.getMessage());
            throw new GamebookException(nfe);
        }
    }

    /**
     * Retrieves ranges from paragraph.
     *
     * @param node selected node
     * @return list of ranges
     */
    private List<Range> getParagraphRanges(Node node) {
        List<Range> ranges = new ArrayList<Range>();
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {

            Node childNode = nodeList.item(i);
            if (childNode.getNodeName().equalsIgnoreCase(RANGE_NODE)) {
                Range range = new Range();

                String path = childNode.getFirstChild().getTextContent().trim();
                if (path != null && !path.equals(""))
                    range.setNextParagraphId(Integer.parseInt(path));

                String from = childNode.getAttributes().getNamedItem(FROM_ATTR).getNodeValue().trim();
                if (from != null && !from.equals(""))
                    range.setFrom(Integer.parseInt(from));

                String to = childNode.getAttributes().getNamedItem(TO_ATTR).getNodeValue().trim();
                if (to != null && !to.equals(""))
                    range.setTo(Integer.parseInt(to));

                ranges.add(range);
            }
        }

        return ranges;
    }

    /**
     * Retrieves path from paragraph.
     *
     * @param node selected node
     * @return Path
     */
    private Path getParagraphPath(Node node) {
        Path path = new Path();

        Node swipeNode = node.getAttributes().getNamedItem(SWIPE_ATTR);
        String swipe;
        if (swipeNode != null) {
            swipe = swipeNode.getNodeValue().trim();
            if (swipe != null && !swipe.equals(""))
                path.setSwipe(swipe);
        }

        String nextParagraph = node.getFirstChild().getTextContent().trim();
        if (nextParagraph != null && !nextParagraph.equals(""))
            path.setTargetParagraph(Integer.parseInt(nextParagraph));

        Node requirementNode = node.getAttributes().getNamedItem(REQUIRE_ATTR);
        if (requirementNode != null) {
            String requirementId = requirementNode.getNodeValue().trim();
            if (requirementId != null && !requirementId.equals(""))
                path.setRequirement(Integer.parseInt(requirementId));

        }
        return path;
    }

    /**
     * Retrieves list of effects of paragraph.
     *
     * @param node selected node
     * @return lift of effects
     */
    private List<Effect> getParagraphEffects(Node node) {
        List<Effect> effects = new ArrayList<Effect>();
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {

            Node effectNode = nodeList.item(i);
            if (effectNode.getNodeName().equalsIgnoreCase(EFFECT_NODE)) {
                Effect effect = new Effect();

                String attribute = effectNode.getAttributes().getNamedItem(ATTR_ATTR).getNodeValue().toLowerCase().trim();
                if (attribute != null && !attribute.equals(""))
                    effect.setAttribute(attribute);

                String value = effectNode.getFirstChild().getTextContent().trim();
                if (value != null && !value.equals(""))
                    effect.setValue(Integer.parseInt(value));

                effects.add(effect);
            }
        }

        return effects;
    }

    /**
     * Retrieves list of paths from paragraph.
     *
     * @param node selected node
     * @return list of paths
     */
    private List<Path> getParagraphPaths(Node node) {
        List<Path> paths = new ArrayList<Path>();
        NodeList childNodeList = node.getChildNodes();
        for (int j = 0; j < childNodeList.getLength(); j++) {

            Node pathNode = childNodeList.item(j);
            if (pathNode.getNodeName().equalsIgnoreCase(PATH_NODE))
                paths.add(getParagraphPath(pathNode));
        }

        return paths;
    }

    /**
     * Retrieves text from a node.
     *
     * @param n node
     * @return text
     */
    private String getParagraphText(Node n) {
        return n.getFirstChild().getTextContent().trim();
    }
}
