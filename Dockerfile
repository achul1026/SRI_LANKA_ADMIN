# Use the official CentOS image as a base
FROM centos:7.9.2009

# Install necessary packages
RUN yum -y update && \
    yum -y install \
        java-11-openjdk-devel \
        wget \
        tar \
        which && \
    yum clean all

# Set environment variables for Tomcat
ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH

# Download and install Tomcat
RUN wget https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.62/bin/apache-tomcat-9.0.62.tar.gz -O /tmp/tomcat.tar.gz && \
    mkdir -p $CATALINA_HOME && \
    tar -xvf /tmp/tomcat.tar.gz -C $CATALINA_HOME --strip-components=1 && \
    rm /tmp/tomcat.tar.gz

# Copy configuration files (WAR file and server.xml will be mounted)
#COPY server.xml $CATALINA_HOME/conf/server.xml

# Expose ports
EXPOSE 8080 8009

# Start Tomcat
CMD ["catalina.sh", "run"]
