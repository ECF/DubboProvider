# Dubbo OSGi R7 Remote Service Distribution Provider
ECF OSGi R7 Remote Services Distribution Provider based upon [Apache Dubbo](http://dubbo.apache.org)

ECF is an [Eclipse Foundation](http://www.eclipse.org) open source project that implements the OSGi R7 version of the [Remote Services](https://osgi.org/specification/osgi.cmpn/7.0.0/service.remoteservices.html) and [Remote Service Admin](https://osgi.org/specification/osgi.cmpn/7.0.0/service.remoteserviceadmin.html) specifications. 

This repo holds both a Distribution provider that can be plugged in underneath ECF's Remote Services implementation.  The following links provide access to resources about the Remote Services and Remote Service Admin (RSA) implementations themselves, along with linkt to other available Distribution and Discovery providers.

[ECF Home page](http://www.eclipse.org/ecf)<br>
[ECF Wiki](https://wiki.eclipse.org/ECF)<br>
[ECF Download page](http://www.eclipse.org/ecf/downloads.php)<br>
[ECF Distribution Providers](https://wiki.eclipse.org/Distribution_Providers)<br>
[ECF Discovery Providers](https://wiki.eclipse.org/Discovery_Providers) 

### Installing and Running DemoService in Karaf

Note:  By default, the Dubbo Distribution Provider features include the [Hazelcast Discovery Provider](https://github.com/ECF/HazelcastProvider) in their install.  The default configuration of this provider requires multicast be available on the LAN used to run the demos below.  If multicast is not available, then the Hazelcast-based discovery will not occur for the consumer to discover the expored demo service as described below.

To install, start, and export the Demo Remote Service (available src in project [here](https://github.com/ECF/DubboProvider/tree/master/examples/org.eclipse.ecf.examples.provider.dubbo.demo.impl)) type:
	
	karaf@root()> feature:install ecf-rs-distribution-dubbo-demo-host

This should produce output similar to the following
	
    16:32:52.127;EXPORT_REGISTRATION;exportedSR=[org.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService];cID=URIID     [uri=dubbo://192.168.0.52:20880/org.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService?anyhost=true&application=ecf-remoteservice-application&bind.ip=192.168.0.52&bind.port=20880&deprecated=false&dubbo=2.0.2&dynamic=true&generic=false&interface=org.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService&methods=sayHello&pid=19968&register=true&release=&side=provider&timestamp=1568590371666];rsId=1
    --Endpoint Description---
    <endpoint-descriptions xmlns="http://www.osgi.org/xmlns/rsa/v1.0.0">
      <endpoint-description>
        <property name="ecf.endpoint.id" value-type="String" value="dubbo%3A%2F%2F192.168.0.52%3A20880%2Forg.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService%3Fanyhost%3Dtrue%26application%3Decf-remoteservice-application%26bind.ip%3D192.168.0.52%26bind.port%3D20880%26deprecated%3Dfalse%26dubbo%3D2.0.2%26dynamic%3Dtrue%26generic%3Dfalse%26interface%3Dorg.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService%26methods%3DsayHello%26pid%3D19968%26register%3Dtrue%26release%3D%26side%3Dprovider%26timestamp%3D1568590371666"/>
        <property name="ecf.endpoint.id.ns" value-type="String" value="ecf.namespace.dubbo"/>
	    <property name="ecf.endpoint.ts" value-type="Long" value="1568590371527"/>
	    <property name="ecf.rsvc.id" value-type="Long" value="1"/>
	    <property name="endpoint.framework.uuid" value-type="String" value="9e7d1ffe-d2cd-4342-8ccf-7c8a70a24a1f"/>
	    <property name="endpoint.id" value-type="String" value="7ad1afb8-c89a-417e-a77f-cd964f7544bf"/>
	    <property name="endpoint.package.version.org.eclipse.ecf.examples.provider.dubbo.demo.api" value-type="String" value="1.0.0"/>
	    <property name="endpoint.service.id" value-type="Long" value="127"/>
	    <property name="objectClass" value-type="String">
	      <array>
	        <value>org.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService</value>
	      </array>
	    </property>
	    <property name="remote.configs.supported" value-type="String">
	      <array>
	        <value>ecf.dubbo.server</value>
	      </array>
	    </property>
	    <property name="remote.intents.supported" value-type="String">
	      <array>
	        <value>osgi.basic</value>
	        <value>passByValue</value>
	        <value>exactlyOnce</value>
	        <value>ordered</value>
	        <value>osgi.async</value>
	        <value>osgi.private</value>
	        <value>dubbo</value>
	      </array>
	    </property>
	    <property name="service.imported" value-type="String" value="true"/>
	    <property name="service.imported.configs" value-type="String">
	      <array>
	        <value>ecf.dubbo.server</value>
	      </array>
	    </property>
	  </endpoint-description>
	</endpoint-descriptions>
	---End Endpoint Description
	Done.
	karaf@root()> 
    
The output above indicates that a DS-created [DemoServiceImpl instance](https://github.com/ECF/DubboProvider/blob/master/examples/org.eclipse.ecf.examples.provider.dubbo.demo.impl/src/org/eclipse/ecf/examples/provider/dubbo/demo/impl/DemoServiceImpl.java) was 

- installed and started
- the DemoServiceImpl remote service was created, registered locally, and exported (EXPORT_REGISTRATION) by the ECF RSA implementation, producing the ENDPOINT-DESCRIPTION xml shown in the console output as shown above
- If Hazelcast is installed and able to connect to a multicast group on your LAN, the endpoint-description will be published via multicast.  At that point consumers on the same LAN can discover, import, and remotely use the DemoService.

### Installing and Running Consumer in Karaf (using a separate Karaf instance on same LAN)

To install and start the Demo Remote Service Consumer (avalable in project [here](https://github.com/ECF/DubboProvider/tree/master/examples/org.eclipse.ecf.examples.provider.dubbo.demo.consumer)) type:

	karaf@root()> feature:install -v ecf-rs-distribution-dubbo-demo-consumer
	
After a few seconds for installation and start of the bundles, if you have exported as shown above in an other Karaf instance, and are on same LAN you should see output similar to the following

	karaf@root()> Bind demo service
	service response was: Hello to you osgi consumer
	15:21:00.254;IMPORT_REGISTRATION;importedSR=[org.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService];cID=URIID [uri=dubbo://192.168.11.180:20880/org.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService?anyhost=true&application=ecf-dubbo-app&bind.ip=192.168.11.180&bind.port=20880&deprecated=false&dubbo=2.0.2&dynamic=true&generic=false&interface=org.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService&methods=sayHello&pid=19416&register=true&release=&side=provider&timestamp=1568845180829];rsId=1
	--Endpoint Description---
	<endpoint-descriptions xmlns="http://www.osgi.org/xmlns/rsa/v1.0.0">
	  <endpoint-description>
	    <property name="ecf.endpoint.id" value-type="String" value="dubbo%3A%2F%2F192.168.11.180%3A20880%2Forg.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService%3Fanyhost%3Dtrue%26application%3Decf-dubbo-app%26bind.ip%3D192.168.11.180%26bind.port%3D20880%26deprecated%3Dfalse%26dubbo%3D2.0.2%26dynamic%3Dtrue%26generic%3Dfalse%26interface%3Dorg.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService%26methods%3DsayHello%26pid%3D19416%26register%3Dtrue%26release%3D%26side%3Dprovider%26timestamp%3D1568845180829"/>
	    <property name="ecf.endpoint.id.ns" value-type="String" value="ecf.namespace.dubbo"/>
	    <property name="ecf.endpoint.ts" value-type="Long" value="1568845180632"/>
	    <property name="ecf.rsvc.id" value-type="Long" value="1"/>
	    <property name="endpoint.framework.uuid" value-type="String" value="32c3fc06-9220-4948-aabf-60f58d56fd13"/>
	    <property name="endpoint.id" value-type="String" value="df97e7c2-17ec-420e-9fa3-13b60d67f2ff"/>
	    <property name="endpoint.package.version.org.eclipse.ecf.examples.provider.dubbo.demo.api" value-type="String" value="1.0.0"/>
	    <property name="endpoint.service.id" value-type="Long" value="104"/>
	    <property name="objectClass" value-type="String">
	      <array>
	        <value>org.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService</value>
	      </array>
	    </property>
	    <property name="remote.configs.supported" value-type="String">
	      <array>
	        <value>ecf.dubbo.server</value>
	      </array>
	    </property>
	    <property name="remote.intents.supported" value-type="String">
	      <array>
	        <value>osgi.basic</value>
	        <value>passByValue</value>
	        <value>exactlyOnce</value>
	        <value>ordered</value>
	        <value>osgi.async</value>
	        <value>osgi.private</value>
	        <value>dubbo</value>
	      </array>
	    </property>
	    <property name="service.imported.configs" value-type="String">
	      <array>
	        <value>ecf.dubbo.client</value>
	      </array>
	    </property>
	  </endpoint-description>
	</endpoint-descriptions>
	---End Endpoint Description
		
The output in between --Endpoint Description-- and --End Endpoint Description is debug output from RSA indicating that the remote service was imported (IMPORT_REGISTRATION in above output).  Prior to the IMPORT_REGISTRATION is this output

	karaf@root()> Bind demo service
	service response was: Hello to you osgi consumer

The 'Bind demo service' output indicates that the remote service was bound to the consumer via DS - see [here](https://github.com/ECF/DubboProvider/blob/master/examples/org.eclipse.ecf.examples.provider.dubbo.demo.consumer/src/org/eclipse/ecf/examples/provider/dubbo/demo/consumer/DemoConsumer.java) for the consumer source code.  

The [consumer](https://github.com/ECF/DubboProvider/blob/master/examples/org.eclipse.ecf.examples.provider.dubbo.demo.consumer/src/org/eclipse/ecf/examples/provider/dubbo/demo/consumer/DemoConsumer.java) then calls the DemoService.sayHello method, which makes the remote call to the DemoServiceImpl exported from the other Karaf instance and prints out the result of 'Hello to you osgi consumer'.

If you go back to the other (DemoServiceImpl) Karaf instance, you should see this in the console output

	Call with name= osgi consumer to sayHello impl

This is the code in the [DemoServiceImpl class](https://github.com/ECF/DubboProvider/blob/master/examples/org.eclipse.ecf.examples.provider.dubbo.demo.impl/src/org/eclipse/ecf/examples/provider/dubbo/demo/impl/DemoServiceImpl.java) showing that the consumer remotely called the [DemoServiceImpl](https://github.com/ECF/DubboProvider/blob/master/examples/org.eclipse.ecf.examples.provider.dubbo.demo.impl/src/org/eclipse/ecf/examples/provider/dubbo/demo/impl/DemoServiceImpl.java) and it constructs and returns a value.

To summarize, [this DemoServiceImpl](https://github.com/ECF/DubboProvider/blob/master/examples/org.eclipse.ecf.examples.provider.dubbo.demo.impl/src/org/eclipse/ecf/examples/provider/dubbo/demo/impl/DemoServiceImpl.java) (remote service host) is exported by one Karaf instance (EXPORT_REGISTRATION), and the consumer then discovers the remote DemoService, creates and binds the proxy into the [consumer](https://github.com/ECF/DubboProvider/blob/master/examples/org.eclipse.ecf.examples.provider.dubbo.demo.consumer/src/org/eclipse/ecf/examples/provider/dubbo/demo/consumer/DemoConsumer.java) and the consumer calls the DemoService.sayHello remote service method, and prints out the result.

### Install Dubbo Distribution Provider into Karaf without Installing and Running Demo

Note:  By default, the Dubbo Distribution Provider features include the [Hazelcast Discovery Provider](https://github.com/ECF/HazelcastProvider) in their install

1 Start Karaf

2 Add Dubbo Provider Features repo by typing:
	
	karaf@root()> feature:repo-add https://raw.githubusercontent.com/ECF/DubboProvider/master/build/karaf-features.xml

3 Install Dubbo Server feature by typing:

	karaf@root()> feature:install -v ecf-rs-distribution-dubbo-server

This will produce output showing the installation and start of multiple bundles, and complete with:

	...multiple lines of output...
	  org.eclipse.ecf.osgi.services.remoteserviceadmin.console/1.2.0.v20180713-1805
	Done.
	karaf@root()>

The Dubbo Remote Services Distribution Provider Server is now installed!

To install the Dubbo Remote Services Distribution Provider **Client** on either the same or some other Karaf instance

Install Dubbo Client feature by typing:

	karaf@root()> feature:install -v ecf-rs-distribution-dubbo-client

This will produce output showing the installation and start of multiple bundles, and complete with:

	...multiple lines of output...
	Starting bundles:
	  org.eclipse.ecf.provider.dubbo.client/1.0.0.201908301123
	Done.
	karaf@root()>

