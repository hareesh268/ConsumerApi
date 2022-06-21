FROM docker.repo1.uhc.com/link-utilities/java-base:11-nr-750
ADD build/libs/ds-consumer-api.jar .

RUN mkdir -p /opt/optum/uhcp/contrast
RUN chmod -R 777 /opt/optum/uhcp/contrast

RUN curl -X GET https://optum.contrastsecurity.com/Contrast/api/ng/37472d1b-d5b7-474c-9011-314a144464c2/agents/default/JAVA -H 'Authorization: dmFkaGFyaWFfbXVydHV6YUBvcHR1bS5jb206MFVLNlZJT0IxMjM5U0lPVg==' -H 'API-Key: kZy66BYR6lIf5EwlN50AMNh2fxet6Xx1' -o /opt/optum/uhcp/contrast/contrast.jar
# COPY contrast/contrast.jar /opt/optum/uhcp/contrast/contrast.jar
COPY contrast/contrast_security_dev.yaml /opt/optum/uhcp/contrast/contrast_security_dev.yaml
COPY contrast/contrast_security_test.yaml /opt/optum/uhcp/contrast/contrast_security_test.yaml
COPY contrast/contrast_security_stage.yaml /opt/optum/uhcp/contrast/contrast_security_stage.yaml

EXPOSE 8080
CMD java $BASE_JAVA_OPTS $JAVA_OPTS -jar ds-consumer-api.jar
