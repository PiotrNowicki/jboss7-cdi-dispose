package com.piotrnowicki.cdi.dispose.boundary;

import javax.ejb.Remote;
import javax.ejb.Singleton;

/**
 * This is our marker-holder bean. It must be application-wide, so we're left with CDI Singleton, ApplicationScoped or EJB
 * Singleton. BEcause we want to expose it using remote interface - we're left with EJB's Singleton.
 * 
 * @author Piotr Nowicki
 * 
 */
@Singleton
@Remote
public class MyLoggingBean implements MyLogging {

    private boolean disposed;

    @Override
    public boolean getDisposed() {
        return disposed;
    }

    @Override
    public void setDisposed(boolean disposed) {
        this.disposed = disposed;
    }
}
