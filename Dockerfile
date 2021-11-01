FROM openjdk:11
# PORT server
EXPOSE 5000

ENV profiles=local
ARG JAR_FILE=target/Art_Experience-V.1.jar
ADD ${JAR_FILE} /Art_Experience-V.1.jar

CMD java -Djava.security.egd=file:/dev/./urandom -jar Art_Experience-V.1.jar

# OLD pero sirve para pruebas.

## java
#ENV JAVA_VERSION=11
## Maven
#ENV MAVEN_VERSION=latest
#ENV SLEEP 1

##Set java_Home

#ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
#ENV PATH=$PATH:$JAVA_HOME/bin
##Set maven_Home
#ENV MAVEN_HOME=/usr/share/maven
#ENV PATH=${MAVEN_HOME}/bin:$PATH

##Clean de lo que no se usa mas
#RUN unset JAVA_VERSION
#RUN unset MAVEN_VERSION

# Console checkpoints..
#RUN which java

#RUN echo $JAVA_HOME
#RUN echo $MAVEN_HOME
#RUN sh -c 'touch /Art_Experience-V.1.jar'
#EXPOSE 8082
#COPY . /
#ENTRYPOINT ["mvn", "clean", "spring-boot:run"]

#COPY Art_Experience-0.1.jar /app
#CMD ["java","-jar","Art_Experience-0.1.jar"]


#-Djava.security.egd=file:/dev/./urandom" ,
#ARG JAR_FILE=target/Art_Experience-0.1.jar
#ADD ${JAR_FILE} /Art_Experience-0.1.jar
#RUN sh -c 'touch /Art_Experience-0.1.jar'
#CMD tail -f /dev/null
#RUN mkdir -p /home/zoroismymain/Escritorio/Zero/ArtExperience
#WORKDIR /home/zoroismymain/Escritorio/Zero/ArtExperience
#COPY target/Art_Experience-0.1.jar /home/zoroismymain/Escritorio/Zero/ArtExperience



#FROM maven:latest AS builder
#
#ENV spring_datasource_url=jdbc:postgresql://tuffi.db.elephantsql.com:5432/znflcgpt
#ENV spring_datasource_username=znflcgpt
#ENV spring_datasource_password=FdN1_UUcNthaEXwYGc93F-3sJPmkd1ov
#
##Java
#ENV JAVA_VERSION 8u131
#ENV JAVA_BUILD 11
#
##Installacion Java
#RUN wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/${JAVA_VERSION}-b${JAVA_BUILD}/d54c1d3a095b4ff2b6607d096fa80163/jdk-${JAVA_VERSION}-linux-x64.rpm
#RUN alternatives --install /usr/bin/java java /usr/java/latest/bin/java 200000
#RUN alternatives --install /usr/bin/javac javac /usr/java/latest/bin/javac 200000
#RUN alternatives --install /usr/bin/jar jar /usr/java/latest/bin/jar 200000
#RUN clear
##Set java_Home
#ENV JAVA_HOME=/usr/java/latest
#ENV PATH=${JAVA_HOME}/bin:$PATH
#
##Clean de lo que no se usa mas
#RUN rm jdk-${JAVA_VERSION}-linux-x64.rpm
#RUN unset JAVA_VERSION
#
## Maven
#ENV MAVEN_VERSION latest
#
## Installation
#RUN curl -fsSL http://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz | tar xzf - -C /usr/share \
#  && mv /usr/share/apache-maven-$MAVEN_VERSION /usr/share/maven \
#  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn
#
#ENV MAVEN_HOME /usr/share/maven
#
## Cleanup
#RUN unset MAVEN_VERSION
#RUN clear

