FROM maven:3.3.9-alpine

ADD webeedevices /opt/webeedevices

WORKDIR /opt/webeedevices

RUN mvn package

ENTRYPOINT ["mvn"]
CMD ["exec:java"]
