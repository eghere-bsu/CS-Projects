package cs321.search;

/**
 * This class helps with handling the command line argument parsing 
 * for GeneBankSearchDatabase.
 */
public class GeneBankSearchDatabaseArguments {
    private final String databaseFileName;
    private final String queryFileName;
    private final int debugLevel;

    /**
     * Constructor for GeneBankSearchDatabaseArguments parameters
     * @param databaseFileName
     * @param queryFileName
     * @param debugLevel
     */
    public GeneBankSearchDatabaseArguments(String databaseFileName, String queryFileName, int debugLevel) {
        this.databaseFileName = databaseFileName;
        this.queryFileName = queryFileName;
        this.debugLevel = debugLevel;
    }

    /**
     * Compares the arguments for parsing.
     */
    @Override
    public boolean equals(Object obj) {
        // this method was generated using an IDE
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        GeneBankSearchDatabaseArguments other = (GeneBankSearchDatabaseArguments) obj;
        if (debugLevel != other.debugLevel) {
            return false;
        }
        if (databaseFileName == null) {
            if (other.databaseFileName != null) {
                return false;
            }
        } else {
            if (!databaseFileName.equals(other.databaseFileName)) {
                return false;
            }
        }
        if (queryFileName == null) {
            if (other.queryFileName != null) {
                return false;
            }
        } else {
            if (!queryFileName.equals(other.queryFileName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prints the arguments to a string.
     */
    @Override
    public String toString() {
        // this method was generated using an IDE
        return "GeneBankCreateDatabaseArguments{" +
                ", databaseFileName='" + databaseFileName + '\'' +
                ", queryFileName='" + queryFileName + '\'' +
                ", debugLevel=" + debugLevel +
                '}';
    }
}
