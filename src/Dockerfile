FROM openjdk:11
RUN apt-get update
RUN apt-get install -y make 

RUN mkdir /code
WORKDIR /code
ADD . /code/
RUN chmod +x run.sh

