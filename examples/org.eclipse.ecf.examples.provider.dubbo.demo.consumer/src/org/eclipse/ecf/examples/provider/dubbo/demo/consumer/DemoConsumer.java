package org.eclipse.ecf.examples.provider.dubbo.demo.consumer;

import org.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true)
public class DemoConsumer {

	@Reference(target = "(service.imported=*)")
	void bindDemoService(DemoService svc) {
		// Make remote call
		String response = svc.sayHello("osgi consumer");
		System.out.println("osgi provider responds: " + response);
	}
}
