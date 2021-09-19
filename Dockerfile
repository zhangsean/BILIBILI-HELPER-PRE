FROM openjdk:8u302-jre

MAINTAINER itning itning@itning.top

ADD target/BILIBILI-HELPER-*.RELEASE.jar /home/BILIBILI-HELPER.jar

ENTRYPOINT ["java","-jar","/home/BILIBILI-HELPER.jar"]