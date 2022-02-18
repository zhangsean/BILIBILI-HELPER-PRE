FROM openjdk:8-jre-alpine

MAINTAINER ZhangSean zxf2342@qq.com

ENV CONFIG="" \
    TZ="Asia/Shanghai"

ADD target/BILIBILI-HELPER-*.jar /opt/BILIBILI-HELPER.jar

CMD ["java","-jar","/opt/BILIBILI-HELPER.jar"]