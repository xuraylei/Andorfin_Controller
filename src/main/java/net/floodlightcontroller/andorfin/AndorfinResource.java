package net.floodlightcontroller.andorfin;

import java.util.List;

import net.floodlightcontroller.util.MACAddress;

class Application{
	MACAddress  id;
	// we can add more profiles for android apps
}

class Location{
	//TODO: how to denote the location?
	short location;
}

/*Class AndorfinResource stores information to facilitate network operator specify their policy
 * 
 * */

public class AndorfinResource {
	List<Application> trust_apps;
	List<Application> thirdparty_apps;
	
	List<Location> company_location;
	List<Location> home_location;
}
