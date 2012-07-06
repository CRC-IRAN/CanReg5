/**
 * CanReg5 - a tool to input, store, check and analyse cancer registry data.
 * Copyright (C) 2008-2011  International Agency for Research on Cancer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Morten Johannes Ervik, CIN/IARC, ervikm@iarc.fr
 */
package canreg.client.analysis;

import canreg.client.analysis.TableBuilderInterface.FileTypes;
import canreg.client.analysis.Tools.KeyCancerGroupsEnum;
import canreg.common.Globals;
import canreg.common.Globals.StandardVariableNames;
import canreg.common.database.PopulationDataset;
import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartTheme;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultKeyedValuesDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author ervikm
 */
public class TopNChartTableBuilder implements TableBuilderInterface, JChartTableBuilderInterface {

    // private static int YEAR_COLUMN = 0;
    private static int SEX_COLUMN = 1;
    private static int ICD10_COLUMN = 2;
    private static int MORPHOLOGY_COLUMN = 3;
    // private static int BEHAVIOUR_COLUMN = 4;
    private static int CASES_COLUMN = 5;
    private static Globals.StandardVariableNames[] variablesNeeded = {
        Globals.StandardVariableNames.Sex,
        Globals.StandardVariableNames.ICD10,
        Globals.StandardVariableNames.Morphology,
        Globals.StandardVariableNames.Behaviour,};
    private static FileTypes[] fileTypesGenerated = {
        FileTypes.png,
        FileTypes.svg,
        FileTypes.jchart
    };

    public static enum ChartType {
        PIE,
        BAR
    }
    private LinkedList[] cancerGroupsLocal;
    private String[] sexLabel;
    private JFreeChart[] charts = new JFreeChart[2];
    private String[] icdLabel;
    private NumberFormat format;
    private String[] icd10GroupDescriptions;
    private int numberOfCancerGroups;
    private int numberOfSexes = 2;
    private int DONT_COUNT = -999;
    private EnumMap<KeyCancerGroupsEnum, Integer> keyGroupsMap;
    private int otherCancerGroupsIndex;
    private Integer skinCancerGroupIndex;
    private int allCancerGroupsIndex;
    private int allCancerGroupsButSkinIndex;
    private int topNLimit = 10;
    private ChartType chartType;

    public TopNChartTableBuilder() {
        ChartTheme chartTheme = new StandardChartTheme("sansserif");
        ChartFactory.setChartTheme(chartTheme);
    }

    @Override
    public StandardVariableNames[] getVariablesNeeded() {
        return variablesNeeded;
    }

    @Override
    public FileTypes[] getFileTypesGenerated() {
        return fileTypesGenerated;
    }

    @Override
    public LinkedList<String> buildTable(String tableHeader, String reportFileName,
            int startYear, int endYear,
            Object[][] incidenceData,
            PopulationDataset[] populations, // can be null
            PopulationDataset[] standardPopulations,
            LinkedList<ConfigFields> configList,
            String[] engineParameters,
            FileTypes fileType) throws NotCompatibleDataException {
        String footerString = java.util.ResourceBundle.getBundle("canreg/client/analysis/resources/AgeSpecificCasesPerHundredThousandTableBuilder").getString("TABLE BUILT ") + new Date() + java.util.ResourceBundle.getBundle("canreg/client/analysis/resources/AgeSpecificCasesPerHundredThousandTableBuilder").getString(" BY CANREG5.");

        LinkedList<String> generatedFiles = new LinkedList<String>();

        if (Arrays.asList(engineParameters).contains("barchart")) {
            chartType = ChartType.BAR;

        } else {
            chartType = ChartType.PIE;
        }

        icdLabel = ConfigFieldsReader.findConfig("ICD_groups_labels",
                configList);

        // sexLabel = ConfigFieldsReader.findConfig("sex_label", configList);

        sexLabel = new String[]{java.util.ResourceBundle.getBundle("canreg/client/analysis/resources/AbstractEditorialTableBuilder").getString("MALE"), java.util.ResourceBundle.getBundle("canreg/client/analysis/resources/AbstractEditorialTableBuilder").getString("FEMALE")};

        icd10GroupDescriptions = ConfigFieldsReader.findConfig(
                "ICD10_groups",
                configList);

        cancerGroupsLocal = EditorialTableTools.generateICD10Groups(icd10GroupDescriptions);

        // indexes
        keyGroupsMap = new EnumMap<KeyCancerGroupsEnum, Integer>(KeyCancerGroupsEnum.class);

        keyGroupsMap.put(KeyCancerGroupsEnum.allCancerGroupsIndex, EditorialTableTools.getICD10index("ALL", icd10GroupDescriptions));
        keyGroupsMap.put(KeyCancerGroupsEnum.leukemiaNOSCancerGroupIndex, EditorialTableTools.getICD10index(950, cancerGroupsLocal));
        keyGroupsMap.put(KeyCancerGroupsEnum.skinCancerGroupIndex, EditorialTableTools.getICD10index("C44", icd10GroupDescriptions));
        keyGroupsMap.put(KeyCancerGroupsEnum.bladderCancerGroupIndex, EditorialTableTools.getICD10index("C67", icd10GroupDescriptions));
        keyGroupsMap.put(KeyCancerGroupsEnum.mesotheliomaCancerGroupIndex, EditorialTableTools.getICD10index("C45", icd10GroupDescriptions));
        keyGroupsMap.put(KeyCancerGroupsEnum.kaposiSarkomaCancerGroupIndex, EditorialTableTools.getICD10index("C46", icd10GroupDescriptions));
        keyGroupsMap.put(KeyCancerGroupsEnum.myeloproliferativeDisordersCancerGroupIndex, EditorialTableTools.getICD10index("MPD", icd10GroupDescriptions));
        keyGroupsMap.put(KeyCancerGroupsEnum.myelodysplasticSyndromesCancerGroupIndex, EditorialTableTools.getICD10index("MDS", icd10GroupDescriptions));
        keyGroupsMap.put(KeyCancerGroupsEnum.allCancerGroupsButSkinIndex, EditorialTableTools.getICD10index("ALLbC44", icd10GroupDescriptions));
        keyGroupsMap.put(KeyCancerGroupsEnum.brainAndCentralNervousSystemCancerGroupIndex, EditorialTableTools.getICD10index("C70-72", icd10GroupDescriptions));
        keyGroupsMap.put(KeyCancerGroupsEnum.ovaryCancerGroupIndex, EditorialTableTools.getICD10index(569, cancerGroupsLocal));
        keyGroupsMap.put(KeyCancerGroupsEnum.otherCancerGroupsIndex, EditorialTableTools.getICD10index("O&U", icd10GroupDescriptions));

        otherCancerGroupsIndex = keyGroupsMap.get(KeyCancerGroupsEnum.otherCancerGroupsIndex);
        skinCancerGroupIndex = keyGroupsMap.get(KeyCancerGroupsEnum.skinCancerGroupIndex);
        allCancerGroupsIndex = keyGroupsMap.get(KeyCancerGroupsEnum.allCancerGroupsIndex);
        allCancerGroupsButSkinIndex = keyGroupsMap.get(KeyCancerGroupsEnum.allCancerGroupsButSkinIndex);

        numberOfCancerGroups = cancerGroupsLocal.length;

        double[] line;
        double[] casesLine;


        if (incidenceData != null) {
            String sexString, icdString, casesString;
            String morphologyString;
            double casesArray[][] = new double[numberOfCancerGroups][numberOfSexes];

            int sex, icdNumber, icdIndex, cases;
            List<Integer> dontCount = new LinkedList<Integer>();
            // all sites but skin?
            if (Arrays.asList(engineParameters).contains("noC44")) {
                dontCount.add(skinCancerGroupIndex);
                tableHeader += ", excluding C44";
            }

            for (Object[] dataLine : incidenceData) {

                // Set default
                icdIndex = -1;
                cases = 0;

                // Unknown sex group = 3
                sex = 3;
                // Extract data
                sexString = (String) dataLine[SEX_COLUMN];
                sex = Integer.parseInt(sexString.trim());

                // sex = 3 is unknown sex

                if (sex > 2) {
                    sex = 3;
                }

                morphologyString = (String) dataLine[MORPHOLOGY_COLUMN];
                icdString = (String) dataLine[ICD10_COLUMN];

                icdIndex = Tools.assignICDGroupIndex(keyGroupsMap, icdString, morphologyString, cancerGroupsLocal);

                if (!dontCount.contains(icdIndex) && icdIndex != DONT_COUNT) {
                    // Extract cases
                    cases = (Integer) dataLine[CASES_COLUMN];

                    if (sex <= numberOfSexes && icdIndex >= 0
                            && icdIndex <= cancerGroupsLocal.length) {
                        casesArray[icdIndex][sex - 1] += cases;
                    } else {
                        if (otherCancerGroupsIndex >= 0) {
                            casesArray[otherCancerGroupsIndex][sex
                                    - 1] += cases;
                        }
                    }
                    if (allCancerGroupsIndex >= 0) {
                        casesArray[allCancerGroupsIndex][sex - 1] += cases;
                    }
                    if (allCancerGroupsButSkinIndex >= 0
                            && skinCancerGroupIndex >= 0
                            && icdIndex != skinCancerGroupIndex) {
                        casesArray[allCancerGroupsButSkinIndex][sex
                                - 1] += cases;
                    }
                }
            }

            // separate top 10 and the rest
            TreeSet<CancerCasesCount> topNMale = new TreeSet<CancerCasesCount>(new Comparator<CancerCasesCount>() {

                @Override
                public int compare(CancerCasesCount o1, CancerCasesCount o2) {
                    if (o1.count == o2.count) {
                        return -o1.toString().compareTo(o2.toString());
                    } else {
                        return -(o1.count.compareTo(o2.count));
                    }
                }
            });
            LinkedList<CancerCasesCount> theRestMale = new LinkedList<CancerCasesCount>();

            TreeSet<CancerCasesCount> topNFemale = new TreeSet<CancerCasesCount>(new Comparator<CancerCasesCount>() {

                @Override
                public int compare(CancerCasesCount o1, CancerCasesCount o2) {
                    if (o1.count == o2.count) {
                        return -o1.toString().compareTo(o2.toString());
                    } else {
                        return -(o1.count.compareTo(o2.count));
                    }
                }
            });
            LinkedList<CancerCasesCount> theRestFemale = new LinkedList<CancerCasesCount>();

            CancerCasesCount otherElement = null;
            CancerCasesCount thisElement = null;

            TreeSet<CancerCasesCount> topN;
            LinkedList<CancerCasesCount> theRest;

            format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(1);

            for (int icdGroupNumber = 0; icdGroupNumber < casesArray.length; icdGroupNumber++) {
                casesLine = casesArray[icdGroupNumber];
                for (int sexNumber = 0; sexNumber < 2; sexNumber++) {

                    if (sexNumber == 0) {
                        topN = topNMale;
                        theRest = theRestMale;
                    } else {
                        topN = topNFemale;
                        theRest = theRestFemale;
                    }

                    if (casesLine[sexNumber] > 0) {
                        thisElement = new CancerCasesCount(
                                icd10GroupDescriptions[icdGroupNumber],
                                icdLabel[icdGroupNumber].substring(3),
                                casesLine[sexNumber],
                                icdGroupNumber);

                        // if this is the "other" group - add it immediately to "the rest"
                        if (icdGroupNumber == otherCancerGroupsIndex) {
                            theRest.add(thisElement);
                            // if not we check if this is one of the collection groups
                        } else if (icdGroupNumber != allCancerGroupsButSkinIndex
                                && icdGroupNumber != allCancerGroupsIndex) {
                            // if it is less than N cancers in top N - add it
                            if (topN.size() < topNLimit) {
                                topN.add(thisElement);
                            } else {
                                // otherwise we need to compare it to the last element in the top 10
                                otherElement = topN.last();
                                if (thisElement.compareTo(otherElement) < 0) {
                                    topN.remove(otherElement);
                                    theRest.add(otherElement);
                                    topN.add(thisElement);
                                } else {
                                    theRest.add(thisElement);
                                }
                            }
                        }
                    }

                }
            }
            for (int sexNumber : new int[] {0, 1}) {
                int position = 0;
                if (chartType == ChartType.BAR) {
                    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                    TreeSet<CancerCasesCount> topNTemp;
                    if (sexNumber == 0) {
                        topNTemp = topNMale;
                    } else {
                        topNTemp = topNFemale;
                    }
                    for (CancerCasesCount count : topNTemp) {
                        dataset.addValue(count.getCount(),
                                count.getLabel(),
                                count.toString());
                    }
                    charts[sexNumber] = ChartFactory.createStackedBarChart(
                            tableHeader + ", " + sexLabel[sexNumber],
                            "Site",
                            "Cases",
                            dataset,
                            PlotOrientation.HORIZONTAL,
                            false, true, false);
                } else { // assume piechart
                    DefaultPieDataset dataset = new DefaultKeyedValuesDataset();
                    TreeSet<CancerCasesCount> topNTemp;
                    double restCount = sumUpTheRest(theRestMale, dontCount);
                    if (sexNumber == 0) {
                        topNTemp = topNMale;
                    } else {
                        topNTemp = topNFemale;
                    }
                    for (CancerCasesCount count : topNTemp) {
                        // System.out.println(count);
                        // restCount -= count.getCount();

                        dataset.insertValue(position++,
                                count.toString()
                                + " (" + format.format(count.getCount() / casesArray[allCancerGroupsIndex][sexNumber] * 100) + "%)",
                                count.getCount());
                    }
                    dataset.insertValue(position++, "Other", restCount);
                    charts[sexNumber] = ChartFactory.createPieChart(
                            tableHeader + ", " + sexLabel[sexNumber],
                            dataset, true, false, Locale.getDefault());
                    if (sexNumber == 0) {
                        setPlotColours((PiePlot) charts[sexNumber].getPlot(), topNLimit + 1, Color.BLUE.brighter());

                    } else {
                        setPlotColours((PiePlot) charts[sexNumber].getPlot(), topNLimit + 1, Color.RED.brighter());
                    }
                }

                String fileName = reportFileName + "-" + sexLabel[sexNumber];
                File file = new File(fileName + "." + fileType.toString());

                try {
                    if (fileType.equals(FileTypes.svg)) {
                        Tools.exportChartAsSVG(charts[sexNumber], new Rectangle(1000, 1000), file);
                    } else if (fileType.equals(FileTypes.jchart)) {
                        generatedFiles.add("OK - " + sexLabel[sexNumber]);
                    } else {
                        ChartUtilities.saveChartAsPNG(file, charts[sexNumber], 1000, 1000);
                    }
                    generatedFiles.add(file.getPath());
                } catch (IOException ex) {
                    Logger.getLogger(TopNChartTableBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return generatedFiles;
    }

    @Override
    public boolean areThesePopulationDatasetsCompatible(PopulationDataset[] populations) {
        return true;
    }

    @Override
    public JFreeChart[] getCharts() {

        // set the plots circular before returning them if we have a pie chart
        if (chartType == ChartType.PIE) {
            for (JFreeChart chart : charts) {
                PiePlot plot = (PiePlot) chart.getPlot();
                plot.setCircular(true);
            }
        }

        return charts;
    }

    private double sumUpTheRest(LinkedList<CancerCasesCount> theRestList, List<Integer> dontCountIndexes) {
        double theRest = 0;
        for (CancerCasesCount count : theRestList) {
            if (!dontCountIndexes.contains((Integer) count.getIndex())) {
                theRest += count.getCount();
            } else {
                System.out.println("Found...");
            }
        }
        return theRest;
    }

    private void setPlotColours(PiePlot plot, int numberOfSections, Color baseColor) {
        Color color = baseColor;
        for (int i = 0; i < numberOfSections; i++) {
            plot.setSectionOutlinePaint(i, baseColor.darker().darker().darker());
            color = darken(color);
            plot.setSectionPaint(i, color);
        }
    }

    private Color darken(Color color) {
        return new Color(
                (int) Math.floor(color.getRed() * .9),
                (int) Math.floor(color.getGreen() * .9),
                (int) Math.floor(color.getBlue() * .9));
    }

    private class CancerCasesCount implements Comparable {

        private String icd10;
        private String label;
        private Double count;
        private int index;
        private NumberFormat format;

        private CancerCasesCount(String icd10, String label, Double count, int index) {
            this.label = label;
            this.count = count;
            this.icd10 = icd10;
            this.index = index;
        }

        @Override
        public int compareTo(Object o) {
            if (o instanceof CancerCasesCount) {
                CancerCasesCount other = (CancerCasesCount) o;
                return -getCount().compareTo(other.getCount());
            } else {
                return 0;
            }
        }

        /**
         * @return the icd10
         */
        public String getIcd10() {
            return icd10;
        }

        /**
         * @param icd10 the icd10 to set
         */
        public void setIcd10(String icd10) {
            this.icd10 = icd10;
        }

        /**
         * @return the label
         */
        public String getLabel() {
            return label;
        }

        /**
         * @param label the label to set
         */
        public void setLabel(String label) {
            this.label = label;
        }

        /**
         * @return the count
         */
        public Double getCount() {
            return count;
        }

        /**
         * @param count the count to set
         */
        public void setCount(Double count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return label + " (" + icd10 + "): " + count.intValue();
        }

        /**
         * @return the index
         */
        public int getIndex() {
            return index;
        }

        /**
         * @param index the index to set
         */
        public void setIndex(int index) {
            this.index = index;
        }
    }
}
