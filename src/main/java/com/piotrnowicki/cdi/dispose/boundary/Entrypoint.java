package com.piotrnowicki.cdi.dispose.boundary;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class Entrypoint {

	@EJB
	MyService myService;
	
	@Path("invoke")
	@GET
	public String invoke() {
		myService.invoke();
		
		return "OK - check server logs.";
	}
}
