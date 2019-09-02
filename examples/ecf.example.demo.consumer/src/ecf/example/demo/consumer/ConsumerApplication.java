package ecf.example.demo.consumer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService;

public class ConsumerApplication {

	static String getUrlFromFirstEndpointDescription(InputStream ins) throws Exception {
		EndpointDescriptionParser parser = new EndpointDescriptionParser();
		parser.parse(ins);
		String urlString = (String) parser.getEndpointDescriptions().get(0).getProperties().get("ecf.endpoint.id");
		return URLDecoder.decode(urlString, StandardCharsets.UTF_8.name());
	}
	
	static InputStream getInputStream(String[] args) throws Exception {
		InputStream ins = null;
		if (args.length > 0) {
			ins = new FileInputStream(args[0]);
		} else {
			System.out.println("Waiting for console input of endpointdescription.xml.  To complete input, enter empty line");
			StringBuffer buf = new StringBuffer();
	        BufferedReader reader =
	                   new BufferedReader(new InputStreamReader(System.in));
			while (true) {
					String line = reader.readLine();
					if (line != null && line.length() > 0)
						buf.append(line).append("\n");
					else
						break;
			}
			ins = new ByteArrayInputStream(buf.toString().getBytes());
		}
		return ins;
	}
	
	public static void main(String[] args) throws Exception {
		String urlString = getUrlFromFirstEndpointDescription(getInputStream(args));
		System.out.println("Parsed endpoint description and got dubbo url="+urlString);
		// Now we have dubbo url, we setup ReferenceConfig
		ReferenceConfig<DemoService> reference = new ReferenceConfig<DemoService>();
        reference.setApplication(new ApplicationConfig("dubbo-demo-api-consumer"));
        reference.setUrl(urlString);
        reference.setInterface(DemoService.class);
        // get service
        DemoService service = reference.get();
        // Make remote sayHello call
        String message = service.sayHello("ecf dubbo java client");
        // Print out message
        System.out.println("Received message: "+message);
	}

}
