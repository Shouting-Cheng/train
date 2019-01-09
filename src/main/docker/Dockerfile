FROM registry.cn-hangzhou.aliyuncs.com/choerodon-tools/javabase:0.5.0
COPY app.jar /train.jar
ENTRYPOINT exec java $JAVA_OPTS  -jar /train.jar
