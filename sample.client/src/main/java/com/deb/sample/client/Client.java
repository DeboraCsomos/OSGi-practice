package com.deb.sample.client;

import com.deb.sample.service.definition.Greeter;
import org.osgi.framework.*;

public class Client implements BundleActivator, ServiceListener {

    private BundleContext context;
    private ServiceReference serviceReference;

    public void start(BundleContext bundleContext) throws Exception {
        context = bundleContext;
        try {
            context.addServiceListener(
                    this, "(objectclass=" + Greeter.class.getName() + ")");
        } catch (InvalidSyntaxException ise) {
            ise.printStackTrace();
        }
    }

    public void stop(BundleContext bundleContext) throws Exception {
        if(serviceReference != null) {
            context.ungetService(serviceReference);
        }
    }

    public void serviceChanged(ServiceEvent serviceEvent) {
        int type = serviceEvent.getType();
        switch (type) {
            case(ServiceEvent.REGISTERED):
                System.out.println("Notification of service registered.");
                serviceReference = serviceEvent
                        .getServiceReference();
                Greeter service = (Greeter) context.getService(serviceReference);
                System.out.println( service.sayHiTo("John") );
                break;
            case(ServiceEvent.UNREGISTERING):
                System.out.println("Notification of service unregistered.");
                context.ungetService(serviceReference);
                break;
            default:
                break;
        }
    }
}
