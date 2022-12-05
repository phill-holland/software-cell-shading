<b>Software Cell Shading With Perlin Noise Vertex Lines</b>

This application allows the compilation and execution of a simple cell shading and perlin noise example, for the edges of the sample 3D cube.

It uses entirely software rendering, including lighting, camera movements and texture rasterisation (not perspectively correct!)

It is a resurrection of an old Java Applet codebase!

Prerequisites;

<ul>
<li>JDK Installed (Java)</li>
<li>Apache Maven Installed</li>
</ul>

Ubuntu installation instructions for both;

```
wget https://download.java.net/java/GA/jdk13.0.1/cec27d702aa74d5a8630c65ae61e4305/9/GPL/openjdk-13.0.1_linux-x64_bin.tar.gz
tar -xvf openjdk-13.0.1_linux-x64_bin.tar.gz
sudo mv jdk-13.0.1 /opt/
export JAVA_HOME=/opt/jdk-13.0.1
export PATH=$JAVA_HOME/bin:$PATH
wget https://mirrors.estointernet.in/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
tar -xvf apache-maven-3.6.3-bin.tar.gz
sudo mv apache-maven-3.6.3 /opt/
export M2_HOME=/opt/apache-maven-3.6.3
export PATH="$M2_HOME/bin:$PATH"
```

To Build (via terminal);

```
mvn package
```

To Run (via terminal);

```
java -cp target/software-cell-shading-1.0-SNAPSHOT.jar com.phillholland.app.App
```


This application demonstrates the following;

<ul>
<li>Software 3D Graphics</li>
<li>Software Texturing and Rasterisation</li>
<li>Perlin Noise</li>
<li>Cell Shading</li>
<li>3D Camera</li>
<li>Basic 3D Lighting Calculations</li>
<li>3D Mathematics, including matrices and vectors</li>
</ul>

Note!

This project has a build container, but does not have X11 Display Forwarding, it will not run in the container.

To Create a new Maven project (not required here);

```
mvn archetype:generate -DgroupId=com.phillholland.app -DartifactId=software-cell-shading -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false
```

Requirements;

The VSCode development container plugin is installed;

https://code.visualstudio.com/docs/remote/containers

Docker must also be installed;

https://docs.docker.com/get-docker/

This application, however is configured with linux based containers, and will not work correctly on Windows without modification.
