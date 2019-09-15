# DubboProvider - ECF Remote Service Distribution Provider
ECF OSGi R7 Remote Services Distribution Provider based upon [Apache Dubbo](http://dubbo.apache.org).

ECF is an Eclipse Foundation project that implements OSGi R7 version of the [Remote Services](https://osgi.org/specification/osgi.cmpn/7.0.0/service.remoteservices.html) and [Remote Service Admin](https://osgi.org/specification/osgi.cmpn/7.0.0/service.remoteserviceadmin.html) specifications. 

[ECF Home page](http://www.eclipse.org/ecf) 

[ECF Wiki](https://wiki.eclipse.org/ECF) 

[ECF Download page](http://www.eclipse.org/ecf/downloads.php) 

[ECF Distribution Providers](https://wiki.eclipse.org/Distribution_Providers) 

## Install

### Install into Karaf

The ECF Remote Service SDK must be installed first to use of this distribution provider.  See [this page](https://wiki.eclipse.org/EIG:Install_into_Apache_Karaf) to install the ECF Remote Services SDK into Karaf.

To install this provider here is the URL for Karaf features:

**https://raw.githubusercontent.com/ECF/DubboProvider/master/build/karaf-features.xml**

This repo has the following features:  

**ecf-rs-distribution-dubbo-server** - Dubbo Server (config: **ecf.dubbo.server**)  
**ecf-rs-distribution-dubbo-client** - Dubbo Client (config: **ecf.dubbo.client**)

One and/or the other should be installed to use the server and/or client.

### Install into Eclipse or Target Platform

The ECF Remote Service SDK must be installed first to use of this distribution provider.  See [ECF download page](http://www.eclipse.org/ecf/downloads.php) to install the ECF Remote Services SDK into Eclipse or Target Platform.

To install into Eclipse or Target Platform:

P2 Repo URL: **https://raw.githubusercontent.com/ECF/DubboProvider/master/build/**

### Exporting Demo Service

After installing ecf rs sdk, dubbo server into Karaf, install this feature:  **ecf-rs-distribution-dubbo-demo-host**

this should immediately result in output to the Karaf console like the following

```xml
12:08:04.174;EXPORT_REGISTRATION;exportedSR=[org.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService];cID=URIID [uri=dubbo://192.168.0.52:20880/org.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService?anyhost=true&application=ecf-remoteservice-application&bind.ip=192.168.0.52&bind.port=20880&deprecated=false&dubbo=2.0.2&dynamic=true&generic=false&interface=org.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService&methods=sayHello&pid=5556&register=true&release=&side=provider&timestamp=1567192084134];rsId=2
--Endpoint Description---
<endpoint-descriptions xmlns="http://www.osgi.org/xmlns/rsa/v1.0.0">
  <endpoint-description>
    <property name="ecf.endpoint.id" value-type="String" value="dubbo%3A%2F%2F192.168.0.52%3A20880%2Forg.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService%3Fanyhost%3Dtrue%26application%3Decf-remoteservice-application%26bind.ip%3D192.168.0.52%26bind.port%3D20880%26deprecated%3Dfalse%26dubbo%3D2.0.2%26dynamic%3Dtrue%26generic%3Dfalse%26interface%3Dorg.eclipse.ecf.examples.provider.dubbo.demo.api.DemoService%26methods%3DsayHello%26pid%3D5556%26register%3Dtrue%26release%3D%26side%3Dprovider%26timestamp%3D1567192084134"/>
    <property name="ecf.endpoint.id.ns" value-type="String" value="ecf.namespace.dubbo"/>
    <property name="ecf.endpoint.ts" value-type="Long" value="1567192084116"/>
    <property name="ecf.rsvc.id" value-type="Long" value="2"/>
    <property name="endpoint.framework.uuid" value-type="String" value="4ed138f2-20e9-40ca-b2da-77c8831cb376"/>
    <property name="endpoint.id" value-type="String" value="bf10c9ce-901e-4bf3-b471-ffd22fa4bda3"/>
    <property name="endpoint.package.version.org.eclipse.ecf.examples.provider.dubbo.demo.api" value-type="String" value="1.0.0"/>
    <property name="endpoint.service.id" value-type="Long" value="128"/>
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
```

This indicates that the [DemoServiceImpl](https://github.com/ECF/DubboProvider/blob/master/examples/org.eclipse.ecf.examples.provider.dubbo.demo.impl/src/org/eclipse/ecf/examples/provider/dubbo/demo/impl/DemoServiceImpl.java) was exported as a [DemoService](https://github.com/ECF/DubboProvider/blob/master/examples/org.eclipse.ecf.examples.provider.dubbo.demo.api/src/org/eclipse/ecf/examples/provider/dubbo/demo/api/DemoService.java) remote service.  It is now waiting for clients to discover, import, and use the remote service.   The endpoint-description output can be used to discover the remote service on a separate process and import it.   Note that typically a network-based discovery protocol (e.g. etcd, zeroconf, zookeeper, jslp, see [ECF's discovery impls here](https://wiki.eclipse.org/Discovery_Providers)) will be used to publish and subscribe an endpoint description and it will be unnecessary to cut and paste to import as described below.

### Discovering, Importing and Calling DemoService

In a second Karaf instance install this feature:  **ecf-rs-distribution-dubbo-demo-consumer**

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


