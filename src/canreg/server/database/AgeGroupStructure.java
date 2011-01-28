package canreg.server.database;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * The structure of the age groups used in the population datasets are governed by 4 numbers
 * - the regular size of the groups
 * - the highest age
 * - the size of the lowest age group
 * - the cut-off age
 * 
 * @author ervikm
 */
public class AgeGroupStructure implements Serializable {

    private int sizeOfGroups;
    private int maxAge;
    private int sizeOfFirstGroup;
    private int cutOfAge;
    private String[] ageGroupNames;
    private String constructor;

    /**
     * 
     * @param sizeOfGroups
     * @param maxAge
     */
    public AgeGroupStructure(int sizeOfGroups, int maxAge) {
        this(sizeOfGroups, maxAge, sizeOfGroups, Integer.MAX_VALUE);
        this.constructor = sizeOfGroups + "," + maxAge;
    }

    /**
     * 
     * @param sizeOfGroups
     * @param maxAge
     * @param sizeOfFirstGroup
     */
    public AgeGroupStructure(int sizeOfGroups, int maxAge, int sizeOfFirstGroup) {
        this(sizeOfGroups, maxAge, sizeOfFirstGroup, Integer.MAX_VALUE);
        this.constructor = sizeOfGroups + "," + maxAge + "," + sizeOfFirstGroup;
    }

    /**
     * 
     * @param sizeOfGroups
     * @param maxAge
     * @param sizeOfFirstGroup
     * @param cutOfAge
     */
    public AgeGroupStructure(int sizeOfGroups, int maxAge, int sizeOfFirstGroup, int cutOfAge) {
        this.cutOfAge = cutOfAge;
        this.sizeOfFirstGroup = sizeOfFirstGroup;
        this.sizeOfGroups = sizeOfGroups;
        this.maxAge = maxAge;
        this.ageGroupNames = generateAgeGroupNames();
        this.constructor = sizeOfGroups + "," + maxAge + "," + sizeOfFirstGroup + "," + cutOfAge;
    }

    /**
     * 
     * @param constructor
     */
    public AgeGroupStructure(String constructor) {
        this.constructor = constructor;

        String[] arguments = constructor.split(",");

        // Set defaults
        this.sizeOfGroups = 5;
        this.maxAge = 85;
        this.sizeOfFirstGroup = 5;
        this.cutOfAge = Integer.MAX_VALUE;

        // Overwrite defaults depending on input
        if (arguments.length > 1) {
            this.sizeOfGroups = Integer.parseInt(arguments[0].trim());
            this.maxAge = Integer.parseInt(arguments[1].trim());
            if (arguments.length > 2) {
                this.sizeOfFirstGroup = Integer.parseInt(arguments[2].trim());
                if (arguments.length > 3) {
                    this.cutOfAge = Integer.parseInt(arguments[3].trim());
                }
            }
        }

    }
    
    /**
     * 
     * @return
     */
    public String getConstructor(){
        return constructor;
    }

    /**
     * 
     * @return
     */
    public String[] getAgeGroupNames() {
        return ageGroupNames;
    }

    /**
     * 
     * @return
     */
    public int getNumberOfAgeGroups() {
        return ageGroupNames.length;
    }

    /**
     * 
     * @param age
     * @return
     */
    public int whatAgeGroupIsThisAge(int age) {
        int group = 0;
        if (age < getSizeOfFirstGroup()) {
            group = 0;
        } else if (age >= getCutOfAge()) {
            group = -1;
        } else {
            if (age >= getMaxAge()) {
                age = getMaxAge();
            }
            if (getSizeOfFirstGroup() == 1) {
                group = age / getSizeOfGroups() + 1;
            } else {
                group = (age - getSizeOfFirstGroup()) / getSizeOfGroups() + 1;
            }
        }
        return group;
    }

    private String[] generateAgeGroupNames() {
        LinkedList<String> strings = new LinkedList<String>();
        int year = 0;
        if (getSizeOfFirstGroup() == 1) {
            strings.add("0");
            strings.add("1-4");
            year = 5;
        } else {
            String string = year + "-";
            year = getSizeOfFirstGroup();
            string += (year - 1);
            strings.add(string);
        }
        while (year < getMaxAge()) {
            String string = year + "-";
            year += getSizeOfGroups();
            string += (year - 1);
            strings.add(string);
        }
        if (year < getCutOfAge()) {
            if (getCutOfAge() == Integer.MAX_VALUE) {
                strings.add(year + "+");
            } else {
                strings.add(year + "-" + (getCutOfAge() - 1));
            }
        }
        return strings.toArray(new String[0]);

    }

    @Override
    public String toString() {
        String string = new String();
        int year = 0;
        if (getSizeOfFirstGroup() == 1) {
            string += "0, 1-4";
            year = 5;
        } else {
            string += year + "-";
            year = getSizeOfFirstGroup();
            string += (year - 1);
        }
        int loop = 0;
        while (year < getMaxAge() && loop < 3) {
            string += ", " + year + "-";
            year += getSizeOfGroups();
            string += (year - 1);
            loop++;
        }
        if (loop >= 3) {
            string += ", ...";
            year = getMaxAge();
        }
        if (year < getCutOfAge()) {
            if (getCutOfAge() == Integer.MAX_VALUE) {
                string += ", " + year + "+";
            } else {
                string += ", " + year + "-" + (getCutOfAge() - 1);
            }
        }
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AgeGroupStructure) {
            AgeGroupStructure ags = (AgeGroupStructure) o;
            return (getSizeOfGroups() == ags.getSizeOfGroups() &&
                    getSizeOfFirstGroup() == ags.getSizeOfFirstGroup() &&
                    getMaxAge() == ags.getMaxAge() &&
                    getCutOfAge() == ags.getCutOfAge());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.getSizeOfGroups();
        hash = 17 * hash + this.getMaxAge();
        hash = 17 * hash + this.getSizeOfFirstGroup();
        hash = 17 * hash + this.getCutOfAge();
        return hash;
    }

    /**
     * @return the sizeOfGroups
     */
    public int getSizeOfGroups() {
        return sizeOfGroups;
    }

    /**
     * @return the maxAge
     */
    public int getMaxAge() {
        return maxAge;
    }

    /**
     * @return the sizeOfFirstGroup
     */
    public int getSizeOfFirstGroup() {
        return sizeOfFirstGroup;
    }

    /**
     * @return the cutOfAge
     */
    public int getCutOfAge() {
        return cutOfAge;
    }
}
