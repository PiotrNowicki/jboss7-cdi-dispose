package com.piotrnowicki.cdi.dispose;

import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

public class Resources {

	private static final Logger LOG = Logger.getLogger(Resources.class.getName());
	
	@Produces @Preferred @RequestScoped
	MyBean getMyBean() {
		MyBean result = new MyBean();
		
		LOG.info("##### Creating MyBean: " + result);
		
		return result;
	}
	
	void disposeMyBean(@Disposes @Preferred MyBean myBean) {
		LOG.info("##### Disposing MyBean: " + myBean);
	}
}
