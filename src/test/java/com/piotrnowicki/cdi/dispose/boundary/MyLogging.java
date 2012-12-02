package com.piotrnowicki.cdi.dispose.boundary;

/**
 * Logging bean - its only purpose is to hold marker bean (was the disposed method invoked?) and expose it to remote clients (@RunAsClient from Arquillian).
 * @author Piotr Nowicki
 *
 */
public interface MyLogging {

    /**
     * Get the actual state of the disposed marker.
     * 
     * @return
     */
    public boolean getDisposed();

    /**
     * Set the disposed marker to given value.
     * 
     * @param disposed
     */
    public void setDisposed(boolean disposed);
}
