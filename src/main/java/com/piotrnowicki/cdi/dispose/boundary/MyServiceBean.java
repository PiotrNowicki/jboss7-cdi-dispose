package com.piotrnowicki.cdi.dispose.boundary;

import java.util.logging.Logger;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.piotrnowicki.cdi.dispose.MyBean;
import com.piotrnowicki.cdi.dispose.Preferred;

@Stateless
@Remote
public class MyServiceBean implements MyService {

	private static final Logger LOG = Logger.getLogger(MyServiceBean.class.getName());

	@Inject @Preferred
	MyBean myBean;
	
	public void invoke() {
		LOG.info("######## Invoking MyBean#invoke(). Creating bean:: " + myBean.toString());
	}
}
