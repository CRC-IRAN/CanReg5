/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package canreg.client.dataentry;

import canreg.client.CanRegClientApp;
import canreg.common.DatabaseDictionaryListElement;
import canreg.common.Globals;
import canreg.server.CanRegServerInterface;
import canreg.server.database.DictionaryEntry;
import java.rmi.RemoteException;
// import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Some static tools to work with dictionaries
 * @author ervikm
 */
public class DictionaryHelper {
       
    /**
     * 
     * @param dictionary
     * @return
     */
    public static Map<String, DictionaryEntry> buildDictionaryEntriesFromMap(Map<String, String> dictionary) {
        Map<String, DictionaryEntry> dictionaryEntries = new LinkedHashMap <String, DictionaryEntry> ();
        Iterator <String> iterator = dictionary.keySet().iterator();
        int i = 0;
        while (iterator.hasNext()){
            String code = iterator.next().toString();
            String description = dictionary.get(code);
            dictionaryEntries.put(code,new DictionaryEntry(0, code, description));
        }
        return dictionaryEntries;
    }
    
    /**
     * 
     * @param doc
     * @param name
     * @return
     */
    public static int getDictionaryIDbyName(Document doc, String name){
        int id = -1;
        
        // Get the variables node in the XML
        NodeList nodes = doc.getElementsByTagName(Globals.NAMESPACE + "dictionaries");
        Element variablesElement = (Element) nodes.item(0);

        NodeList dictionaries = variablesElement.getElementsByTagName(Globals.NAMESPACE + "dictionary");

        // Go through all the variable definitions
        boolean found = false;
        int i = 0;
        while (!found && i < dictionaries.getLength()) {
            // Get element
            Element element = (Element) dictionaries.item(i++);
            found = name.equalsIgnoreCase(element.getElementsByTagName(Globals.NAMESPACE + "name").item(0).getTextContent());          
        }
        if (found) {
            id = i-1;
        }
        return id;
    }
    
    /**
     * 
     * @param dictionaryID
     * @param app
     * @return
     * @throws java.rmi.RemoteException
     */
    public static boolean clearDictionary(int dictionaryID, CanRegClientApp app) throws RemoteException {
        return app.deleteDictionaryEntries(dictionaryID);
    }

    /**
     * 
     * @param dictionaryEntry
     * @param server
     * @return
     * @throws java.lang.SecurityException
     * @throws java.rmi.RemoteException
     */
    public static int saveDictionaryEntry(DictionaryEntry dictionaryEntry, CanRegServerInterface server) throws SecurityException, RemoteException {
        return server.saveDictionaryEntry(dictionaryEntry);
    }

    private static Vector<DictionaryEntry> parseDictionaryText(int dictionaryID, String str) {
        Vector dictionaryEntries = new Vector<DictionaryEntry>();
        String[] stringArray = str.split("[\\n|\\r]");
        if (stringArray[0].trim().length() > 0) {
            for (String string : stringArray) {
                String[] lineArray = string.split("\\t");
                if (lineArray.length > 1) {
                    dictionaryEntries.add(new DictionaryEntry(dictionaryID, lineArray[0], lineArray[1]));
                }
            }
        }
        return dictionaryEntries;
    }
    
    /**
     * 
     * @param dictionary
     * @param str
     * @return
     */
    public static Map<Integer, String> testDictionary(DatabaseDictionaryListElement dictionary, String str){
        return testDictionary(dictionary, parseDictionaryText(dictionary.getDictionaryID(), str));
    }
    
    private static Map<Integer, String> testDictionary(DatabaseDictionaryListElement dictionary, Vector<DictionaryEntry> contents){
        Map<Integer, String> errors = new LinkedHashMap<Integer,String>();
        Set<String> codes = new LinkedHashSet();
        int codeLength = dictionary.getCodeLength();
        int fullCodeLength = dictionary.getFullDictionaryCodeLength();
        if (!"Compound".equalsIgnoreCase(dictionary.getType())){
            codeLength=-1;
        }
        int i = 1;
        for (DictionaryEntry de:contents){
            // first check length of code
            String code = de.getCode();
            if (code.length()!=codeLength&&code.length()!=fullCodeLength){
                errors.put(i, "Line "+i+" - Wrong length: "+code);
            } 
            
            // Then we check if it is a duplicate
            
            else if (!codes.add(code)){
                errors.put(i, "Line "+i+" - Duplicate code: "+code);
            }
            i++;
        }
        return errors;
    }

    /**
     * 
     * @param dictionaryID
     * @param str
     * @param app
     * @throws java.rmi.RemoteException
     */
    public static void replaceDictionary(int dictionaryID, String str, CanRegClientApp app) throws RemoteException {
        Vector<DictionaryEntry> dictionaryEntries = parseDictionaryText(dictionaryID, str);
        Map<String, String> dictionaryEntriesMap = new LinkedHashMap<String, String>();

        boolean removed = clearDictionary(dictionaryID, app);

        for (DictionaryEntry entry : dictionaryEntries) {
            app.saveDictionaryEntry(entry);
            dictionaryEntriesMap.put(entry.getCode(),entry.getDescription());
        }
    }    
}
