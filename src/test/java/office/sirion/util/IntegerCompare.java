package office.sirion.util;

import office.sirion.base.TestBase;

public class IntegerCompare extends TestBase{
	
	public void IntegerDemo() {

		// compares two Integer objects numerically
		      Integer obj1 = new Integer("25");
		      Integer obj2 = new Integer("10");
		      int retval =  obj1.compareTo(obj2);
		    
		      if(retval > 0) {
		         Logger.debug("obj1 is greater than obj2");
		      } else if(retval < 0) {
		    	  Logger.debug("obj1 is less than obj2");
		      } else {
		    	  Logger.debug("obj1 is equal to obj2");
		      }
		   }
		} 


