package com.piotrnowicki.cdi.dispose.web;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.piotrnowicki.cdi.dispose.boundary.MyService;

/**
 * RESTful WS entrypoint. Note it is a SLSB!
 * 
 * @author Piotr Nowicki
 *
 */
@Stateless
@Path("/")
public class Entrypoint {

	@EJB
	private MyService myService;
	
	@Path("invoke")
	@GET
	public String invoke() {
		myService.invoke();
		
		return "OK - check server logs.";
	}
}
