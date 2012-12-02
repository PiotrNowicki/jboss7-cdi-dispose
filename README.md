CDI @Dispose Method And Remote EJB
=====================================

This app shows some problem with remote EJB invocations and RequestScoped CDI resources.

According to `@RequestScoped` javadoc: 

> The request scope is active:  
> (...)  
> during any remote method invocation of any EJB, during any asynchronous method invocation of any EJB, during 
> any call to an EJB timeout method and during message delivery to any EJB message-driven bean,  
> (...)  
> The request context is destroyed:  
> (...)  
> after the EJB remote method invocation, asynchronous method invocation, timeout or message delivery completes, or


It seems that even when remote EJB call has finished the `@Dispose` method for depenend `@RequestScoped` bean **is 
not called.** If we change the invocation from remote EJB to RESTful WS - the `@Dispose` method is properly called.

Start JBoss AS 7.1 with default configuration (localhost, 8080, etc.) and run the provided tests. Take a look at
test methods and their description.