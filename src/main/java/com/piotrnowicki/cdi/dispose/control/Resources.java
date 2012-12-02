package com.piotrnowicki.cdi.dispose.control;

import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

import com.piotrnowicki.cdi.dispose.boundary.MyLogging;

/**
 * 
 * Class that creates and disposes of CDI managed beans.
 * 
 * @author Piotr Nowicki
 *
 */
@ApplicationScoped
public class Resources {

    private static final Logger LOG = Logger.getLogger(Resources.class.getName());

    /**
     * This bean is just for testing purposes. It makes sure we (from Arquililan / Unit Test) will be able to find out if the dispose method was correctly fired. 
     */
    @EJB
    private MyLogging myLoggingBean;
    
    /**
     * Produces out request-scoped CDI bean.
     * @return
     */
	@Produces @Preferred @RequestScoped
	public MyBean getMyBean() {
		MyBean result = new MyBean();
		
		LOG.info("##### Creating MyBean: " + result);

		// We set disposed marker to false (we created a new bean and haven't disposed it yet)
		myLoggingBean.setDisposed(false);
        
		return result;
	}
	
	/**
	 * Method that should be invoked by the CDI implementation when bean is out of scope.
	 * 
	 * @param myBean bean that is supposed to be disposed.
	 */
	void disposeMyBean(@Disposes @Preferred  MyBean myBean) {
		LOG.info("##### Disposing MyBean: " + myBean);

		// Disposal method was fired - set the marker.
		myLoggingBean.setDisposed(true);
	}
}
