# DubboProvider - Remote Service Distribution Provider
ECF OSGi R7 Remote Services Distribution Provider based upon [Apache Dubbo](http://dubbo.apache.org)

ECF is an [Eclipse Foundation](http://www.eclipse.org) project that implements OSGi R7 version of the [Remote Services](https://osgi.org/specification/osgi.cmpn/7.0.0/service.remoteservices.html) and [Remote Service Admin](https://osgi.org/specification/osgi.cmpn/7.0.0/service.remoteserviceadmin.html) specifications. 

[ECF Home page](http://www.eclipse.org/ecf)<br>
[ECF Wiki](https://wiki.eclipse.org/ECF)<br>
[ECF Download page](http://www.eclipse.org/ecf/downloads.php)<br>
[ECF Distribution Providers](https://wiki.eclipse.org/Distribution_Providers)<br>
[ECF Discovery Providers](https://wiki.eclipse.org/Discovery_Providers) 

## Install

### Install into Karaf

1 Start Karaf

2 At karaf@root()> prompt, to add ECF SDK Feature repo and DubboProvider Feature repo
<pre>
karaf@root()> feature:repo-add ecf
</pre>
3 Add Dubbo Provider Features repo by typing:
<pre>
karaf@root()> feature:repo-add https://raw.githubusercontent.com/ECF/DubboProvider/master/build/karaf-features.xml
</pre>
4 Install Dubbo Server feature by typing:
<pre>
karaf@root()> feature:install -v ecf-rs-distribution-dubbo-server
</pre>
This will produce output showing the installation and start of multiple bundles, and complete with:
<pre>
...multiple lines of output...
  org.eclipse.ecf.osgi.services.remoteserviceadmin.console/1.2.0.v20180713-1805
Done.
karaf@root()>
</pre>

The Dubbo Remote Services Distribution Provider Server is now installed!

To install the Dubbo Remote Services Distribution Provider **Client** on either the same or some other Karaf instance

1 Do 1-3 above to add ECF SDK Feature repo and DubboProvider Feature repo
4 Install Dubbo Client feature by typing:
<pre>
karaf@root()> feature:install -v ecf-rs-distribution-dubbo-client
</pre>
This will produce output showing the installation and start of multiple bundles, and complete with:
<pre>
...multiple lines of output...
Starting bundles:
  org.eclipse.ecf.provider.dubbo.client/1.0.0.201908301123
Done.
karaf@root()>
</pre>

#### Installing and Running Demo Remote Service in Karaf

##### Demo Remote Service Host

To install, start, and export the DubboProvider Demo Remote Service Host (avalable in project [here](https://github.com/ECF/DubboProvider/tree/master/examples/org.eclipse.ecf.examples.provider.dubbo.demo.impl)) type:
<pre>
karaf@root()> feature:install -v ecf-rs-distribution-dubbo-demo-host
</pre>
This should produce output similar to the following
<pre>
karaf@root()> feature:install -v ecf-rs-distribution-dubbo-demo-host
Adding features: ecf-rs-distribution-dubbo-demo-host/[1.0.0,1.0.0]
Changes to perform:
  Region: root
    Bundles to install:
      https://raw.githubusercontent.com/ECF/DubboProvider/master/build/plugins/org.eclipse.ecf.examples.provider.dubbo.demo.api_1.0.0.201908301123.jar
      https://raw.githubusercontent.com/ECF/DubboProvider/master/build/plugins/org.eclipse.ecf.examples.provider.dubbo.demo.impl_1.0.0.201908301123.jar
Installing bundles:
  https://raw.githubusercontent.com/ECF/DubboProvider/master/build/plugins/org.eclipse.ecf.examples.provider.dubbo.demo.api_1.0.0.201908301123.jar
  https://raw.githubusercontent.com/ECF/DubboProvider/master/build/plugins/org.eclipse.ecf.examples.provider.dubbo.demo.impl_1.0.0.201908301123.jar
Starting bundles:
  org.eclipse.ecf.examples.provider.dubbo.demo.api/1.0.0.201908301123
  org.eclipse.ecf.examples.provider.dubbo.demo.impl/1.0.0.201908301123
16:32:52.127;EXPORT_REGISTRATION;exportedSR=[org.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService];cID=URIID [uri=dubbo://192.168.0.52:20880/org.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService?anyhost=true&application=ecf-remoteservice-application&bind.ip=192.168.0.52&bind.port=20880&deprecated=false&dubbo=2.0.2&dynamic=true&generic=false&interface=org.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService&methods=sayHello&pid=19968&register=true&release=&side=provider&timestamp=1568590371666];rsId=1
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
</pre>
The output above indicates that the DubboProvider demo Remote Service host bundle was installed and started, and that the Remote Service was exported (EXPORT_REGISTRATION), producing the endpoint-description xml shown in the console output above.

##### Remote Service Consumer (same or separate Karaf instance)

To install and start the DubboProvider Demo Remote Service Consumer (avalable in project [here](https://github.com/ECF/DubboProvider/tree/master/examples/org.eclipse.ecf.examples.provider.dubbo.demo.impl)) type:
<pre>
karaf@root()> feature:install -v ecf-rs-distribution-dubbo-demo-consumer
</pre>

### Install into Eclipse or Target Platform

The ECF Remote Service SDK must be installed first to use of this distribution provider.  See [ECF download page](http://www.eclipse.org/ecf/downloads.php) to install the ECF Remote Services SDK into Eclipse or Target Platform.

To install into Eclipse or Target Platform:

P2 Repo URL: **https://raw.githubusercontent.com/ECF/DubboProvider/master/build/**

Once installed, type the following **importservice** ECF RSA console command to discover and import the remote service

```
karaf@root()> importservice
Waiting for console input.  To complete enter an empty line...
```
Then copy all lines between **--Endpoint Description---** and **---End Endpoint Description** into the clipboard, paste into the console and enter empty an empty line.  This should result in output like the following:

```xml
osgi> importservice
Waiting for console input.   To complete enter an empty line...
<much debug output deleted>
osgi provider responds: Hello osgi consumer, response from provider
<more debug output deleted>
osgi> 
```

The output indicates that the [DemoService](https://github.com/ECF/DubboProvider/blob/master/examples/org.eclipse.ecf.examples.provider.dubbo.demo.api/src/org/eclipse/ecf/examples/provider/dubbo/demo/api/DemoService.java) was discovered and imported by the client runtime, injected into the [DemoConsumer](https://github.com/ECF/DubboProvider/blob/master/examples/org.eclipse.ecf.examples.provider.dubbo.demo.consumer/src/org/eclipse/ecf/examples/provider/dubbo/demo/consumer/DemoConsumer.java) class courtesy of Declarative Services and the [DemoConsumer.bindDemoService](https://github.com/ECF/DubboProvider/blob/master/examples/org.eclipse.ecf.examples.provider.dubbo.demo.consumer/src/org/eclipse/ecf/examples/provider/dubbo/demo/consumer/DemoConsumer.java#L11) method, and the DemoService sayHello remote method was called and returned to print out: **osgi provider responds: Hello osgi consumer, response from provider**


