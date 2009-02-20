package canreg.server;

import canreg.common.Globals;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ervikm
 */
/** Class used for representing an authenticated user in the system. */
public class RMILoginPrincipal
   implements java.security.Principal
{
    private static boolean DEBUG;

    private Globals.UserRightLevels userRightLevel;
    /**
     * Toggles debug status on or off.&ltp>
     * 
     * @param debug flag to set the debug status
     */
    public static void setDebug( boolean debug ) {
		DEBUG = debug;	
    }//end setDebug( boolean )
    
   /** The username */
   private String username;

   ////////////////////////
   /** Class constructor.  @param username The username of the user.
    * @param username 
    */
   public RMILoginPrincipal(String username)
   {
        if(username == null) {
        throw new IllegalArgumentException("Null name");
        }
        this.username = username;
        if( DEBUG )
            Logger.getLogger(RMILoginPrincipal.class.getName()).log(Level.INFO, "Principal " + username + " successfully created.");
   }
   
   /** Returns the username of the user. @return The username. */
   public String getName()
   {
      return username;
   }
   
    @Override
     public String toString() {
        return "CanRegPrincipal: "+username;
  }
     
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(!(obj instanceof RMILoginPrincipal)) return false;
        RMILoginPrincipal another = (RMILoginPrincipal) obj;
        return username.equals(another.getName());
  }
    @Override
      public int hashCode() {
         return username.hashCode();
  }

    /**
     * @return the userRightLevel
     */
    public Globals.UserRightLevels getUserRightLevel() {
        return userRightLevel;
    }

    /**
     * @param userRightLevel the userRightLevel to set
     */
    public void setUserRightLevel(Globals.UserRightLevels userRightLevel) {
        this.userRightLevel = userRightLevel;
    }
}
