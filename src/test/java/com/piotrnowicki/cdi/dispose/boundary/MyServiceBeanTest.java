package com.piotrnowicki.cdi.dispose.boundary;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.base.Strings;
import com.piotrnowicki.cdi.dispose.control.MyBean;
import com.piotrnowicki.cdi.dispose.web.Application;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@RunWith(Arquillian.class)
public class MyServiceBeanTest {

    private static final String appName = "myArchive";

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, appName + ".war").addPackage(MyService.class.getPackage())
                .addPackage(MyBean.class.getPackage()).addPackage(Application.class.getPackage())
                .addClasses(MyLoggingBean.class, MyLogging.class).addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    /**
     * Test RESTful EJB client. The CDI Request scoped should be created during method invocation and should be destroyed when
     * method finishes.
     * 
     * @throws NamingException
     */
    @Test
    @RunAsClient
    public void testREST() throws NamingException {
        MyLogging logger = getLoggingBean(createInitialContext());
        
        Client client = Client.create();
        WebResource webResource = client.resource("http://localhost:8080/" + appName + "/rest/invoke");
        ClientResponse response = webResource.get(ClientResponse.class);

        // We received correct response from REST.
        assertThat(response.getStatus()).isEqualTo(200);

        // Dispose method of our dependent CDI request-scoped bean has been called (the RESTful method invocation finished)
        assertThat(logger.getDisposed()).isTrue();
    }

    /**
     * Test Remote EJB client. The CDI Request scoped should be created during method invocation and should be destroyed when
     * method finishes.
     * 
     * @throws NamingException
     */
    @Test
    @RunAsClient
    public void testRemoteEJB() throws NamingException {
        InitialContext context = createInitialContext();
        MyLogging myLogging = getLoggingBean(context);

        // Obtain connection with remote EJB
        MyService myService = (MyService) context.lookup(getJNDICoordinates("", appName, MyServiceBean.class.getSimpleName(),
                MyService.class.getName()));

        // Invoke exemplary remote EJB method that uses the CDI bean as its collaborator.
        myService.invoke();

        // Dispose method of our dependent CDI request-scoped bean has been called (the remote EJB method invocation finished)
        assertThat(myLogging.getDisposed()).isTrue();
    }
    
    /**
     * Helper method - get logging bean (it's the bean that provide access to the "disposed" marker that tells us if the dispose CDI method was properly called.
     * 
     * @param ctx context obtained with {@link MyServiceBeanTest#createInitialContext()}
     * @return
     * @throws NamingException
     */
    private MyLogging getLoggingBean(InitialContext ctx) throws NamingException {
        InitialContext context = createInitialContext();
        MyLogging myLogger = (MyLogging) context.lookup(getJNDICoordinates("", appName, MyLoggingBean.class.getSimpleName(),
                MyLogging.class.getName()));
        
        return myLogger;
    }

    /**
     * Helper method - create initial context for JBoss AS 7.1 remote EJB's.
     *  
     * @return
     * @throws NamingException
     */
    private InitialContext createInitialContext() throws NamingException {
        Properties jndiProperties = new Properties();

        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        InitialContext context = new InitialContext(jndiProperties);

        return context;
    }

    /**
     * Constructs the JNDI coordinates for the given EJB.
     * <p>
     * This method uses the EJB Client API coordinates (starting with <code>ejb:</code>) and doesn't do any lookup - it merely
     * provides you with the JNDI coordinates.
     * </p>
     * <p>
     * The format of the EJB Client API is as follows: <br/>
     * <code>ejb:[appName]/[moduleName]/[beanName][!interfaceName][?stateful]</code>
     * </p>
     * 
     * @param appName application name
     * @param moduleName module name
     * @param beanName bean name
     * @param interfaceName interface name
     * 
     * @return JNDI coordinates
     */
    String getJNDICoordinates(String appName, String moduleName, String beanName, String interfaceName) {
        String result = "ejb:" + appName + "/" + moduleName + "/" + beanName;

        if (!Strings.isNullOrEmpty(interfaceName)) {
            result += "!" + interfaceName;
        }

        return result;
    }
}
