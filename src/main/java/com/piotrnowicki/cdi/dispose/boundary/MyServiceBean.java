package com.piotrnowicki.cdi.dispose.boundary;

import java.util.logging.Logger;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.piotrnowicki.cdi.dispose.control.MyBean;
import com.piotrnowicki.cdi.dispose.control.Preferred;

/**
 * 
 * Actual EJB implementation which exposes remote business interface.
 * 
 * @author Piotr Nowicki
 *
 */
@Stateless
@Remote
public class MyServiceBean implements MyService {

	private static final Logger LOG = Logger.getLogger(MyServiceBean.class.getName());

	/**
	 * We're injecting request-scoped MyBean.
	 */
	@Inject @Preferred 
	private MyBean myBean;
	
	/**
	 * Business method which uses the injected request-scoped CDI managed bean.
	 */
	public void invoke() {
		LOG.info("######## Invoking MyServiceBean#invoke(). Dependend myBean is : " + myBean.toString());
	}
}
