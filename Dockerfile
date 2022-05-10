FROM ubuntu:20.04
COPY ./ /feathr_project

RUN apt-get update && \
    apt-get install -y \
    build-essential python3 python3-dev python3-pip librdkafka-dev 

EXPOSE 80

WORKDIR /feathr_project
RUN python3 -m pip install -e .

WORKDIR /feathr_project/feathr/api
RUN pip3 install -r requirements.txt

CMD [ "uvicorn","app.main:app","--host", "0.0.0.0", "--port", "80" ]