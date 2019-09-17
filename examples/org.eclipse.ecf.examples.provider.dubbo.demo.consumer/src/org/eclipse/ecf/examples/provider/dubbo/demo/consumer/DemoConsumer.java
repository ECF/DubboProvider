package org.eclipse.ecf.examples.provider.dubbo.demo.consumer;

import org.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(immediate = true)
public class DemoConsumer {

	@Reference(target = "(service.imported=*)",policy=ReferencePolicy.DYNAMIC)
	void bindDemoService(DemoService svc) {
		System.out.println("Bind demo service");
		// Make remote call
		String response = svc.sayHello("osgi consumer");
		System.out.println("service response was: " + response);
	}
	
	void unbindDemoService(DemoService svc) {
		System.out.println("unbindDemoService");
	}
}
