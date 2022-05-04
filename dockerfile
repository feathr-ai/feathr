FROM ubuntu:latest
COPY ./ /feathr

RUN apt-get update && \
    apt-get install -y \
    build-essential python3.8 python3.8-dev python3-pip git 

RUN python3.8 -m pip install pip --upgrade
RUN python3.8 -m pip install wheel
EXPOSE 80

WORKDIR /feathr/feathr_project/feathr/api
RUN pip3 install -r requirements.txt


WORKDIR /feathr/feathr_project/feathr/api
CMD [ "uvicorn","app.main:app","--host", "0.0.0.0", "--port", "80" ]