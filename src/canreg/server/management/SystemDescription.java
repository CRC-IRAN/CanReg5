package canreg.server.management;

import canreg.common.DatabaseDictionaryListElement;
import canreg.common.DatabaseGroupsListElement;
import canreg.common.DatabaseVariablesListElement;
import canreg.common.Globals;
import canreg.common.Tools;
import canreg.server.database.QueryGenerator;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author ervikm
 */
public class SystemDescription {

    private static boolean debug = Globals.DEBUG;
    private Document doc;
    private DOMParser parser;
    private String fileName;
    private String namespace = "ns3:";

    ;

    /**
     * 
     * @param fileName
     */
    public SystemDescription(String fileName) {
        this.fileName = fileName;
        try {
            parser = new DOMParser();

            setSystemDescriptionXML(fileName);

            //For debuging purposes
            if (debug) {
                //canreg.server.xml.Tools.writeXmlFile(doc, "test.xml");
                //canreg.server.xml.Tools.flushXMLout(doc);
                debugOut(QueryGenerator.strCreateVariableTable("Patient", doc));
                debugOut(QueryGenerator.strCreateVariableTable("Tumour", doc));
                debugOut(QueryGenerator.strCreateDictionaryTable(doc));
                debugOut(QueryGenerator.strCreateTablesOfDictionaries(doc));
            }
        } catch (SAXException ex) {
            Logger.getLogger(SystemDescription.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SystemDescription.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @param fileName
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    public void setSystemDescriptionXML(String fileName) throws SAXException, IOException {
        parser.parse(new InputSource(new FileInputStream(fileName)));
        doc = parser.getDocument();
    }

    /**
     * 
     * @return
     */
    public Document getSystemDescriptionDocument() {
        return doc;
    }

    /**
     * 
     * @return
     */
    public String getSystemName() {
        String name = null;

        if (doc != null) {
            NodeList nl = doc.getElementsByTagName(namespace + "registry_name");

            if (nl != null) {
                Element element = (Element) nl.item(0);
                name = element.getTextContent();
            }
        }
        return name;
    }

    /**
     *
     * @return
     */
    public String getRegion() {
        String region = null;

        if (doc != null) {
            NodeList nl = doc.getElementsByTagName(namespace + "region_code");

            if (nl != null) {
                Element element = (Element) nl.item(0);
                region = element.getTextContent();
                if (region != null && region.trim().length() > 0) {
                    int regionID = Integer.parseInt(region.trim());
                    try {
                        region = Globals.REGIONS[regionID];
                    } catch (NumberFormatException nfe) {
                        // not a number
                    }
                }
            }
        }

        return region;
    }

    /**
     * 
     * @return
     */
    public String getSystemCode() {
        String name = null;

        if (doc != null) {
            NodeList nl = doc.getElementsByTagName(namespace + "registry_code");

            if (nl != null) {
                Element element = (Element) nl.item(0);
                name = element.getTextContent();
            }
        }
        return name;
    }

    /**
     * 
     * @param systemName
     */
    public void setSystemName(String systemName) {
        if (doc != null) {
            NodeList nl = doc.getElementsByTagName(namespace + "registry_name");

            if (nl != null) {
                Element element = (Element) nl.item(0);
                element.setTextContent(systemName);
            }
        }
    }

    /**
     * 
     * @return
     */
    public Element getVariables() {
        Element element = null;
        if (doc != null) {
            NodeList nl = doc.getElementsByTagName(namespace + "variables");
            if (nl != null) {
                element = (Element) nl.item(0);
            }
        }
        return element;
    }

    public DatabaseVariablesListElement[] getDatabaseVariableListElements() {
        return Tools.getVariableListElements(doc, Globals.NAMESPACE);
    }

    public DatabaseDictionaryListElement[] getDatabaseDictionaryListElements() {
        return Tools.getDictionaryListElements(doc, namespace);
    }

    public DatabaseGroupsListElement[] getDatabaseGroupsListElements() {
        return Tools.getGroupsListElements(doc, namespace);
    }

    /**
     * 
     * @return
     */
    public Element getIndexes() {
        Element element = null;
        if (doc != null) {
            NodeList nl = doc.getElementsByTagName(namespace + "indexes");
            if (nl != null) {
                element = (Element) nl.item(0);
            }
        }
        return element;
    }

    /**
     * 
     * @param var
     */
    public void addVariable(Variable var) {
    }

    private static void debugOut(String msg) {
        if (debug) {
            Logger.getLogger(SystemDescription.class.getName()).log(Level.INFO, msg);
        }
    }
}
