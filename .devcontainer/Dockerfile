FROM ubuntu:latest
RUN apt-get update && apt-get install software-properties-common -y
RUN apt-get update && apt-get install build-essential wget -y
RUN apt-get update && apt-get install libxext6 libxrender-dev libxtst-dev -y
RUN wget https://download.java.net/java/GA/jdk13.0.1/cec27d702aa74d5a8630c65ae61e4305/9/GPL/openjdk-13.0.1_linux-x64_bin.tar.gz
RUN tar -xvf openjdk-13.0.1_linux-x64_bin.tar.gz
RUN mv jdk-13.0.1 /opt/
ENV JAVA_HOME=/opt/jdk-13.0.1
ENV PATH=$JAVA_HOME/bin:$PATH
RUN export PATH
RUN wget https://mirrors.estointernet.in/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
RUN tar -xvf apache-maven-3.6.3-bin.tar.gz
RUN mv apache-maven-3.6.3 /opt/
ENV M2_HOME=/opt/apache-maven-3.6.3
ENV PATH="$M2_HOME/bin:$PATH"
RUN export PATH