package org.eclipse.ecf.examples.provider.dubbo.demo.impl;

import org.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true, property = { "service.exported.interfaces=*", 
		"service.exported.configs=ecf.dubbo.server"})
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        System.out.println("Call with name= " + name + " to sayHello impl");
        return "Hello to you " + name;
    }

}
