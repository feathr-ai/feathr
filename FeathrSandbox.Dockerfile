# TODO: persist the SQLite file in the volumes
# TODO: always use feathr from current folder 
# TODO: predownload the maven packages

# docker run -it --rm -p 8888:8888 jupyter/all-spark-notebook

# You will see some prompt as http://127.0.0.1:8888/lab?token=97ba62366598f233acc830103b41221d2d12cfcd583fc028

# login using this token in jupyter

# Stage 1: build frontend ui
FROM node:16-alpine as ui-build
WORKDIR /usr/src/ui
COPY ./ui .

## Use api endpoint from same host and build production static bundle
RUN echo 'REACT_APP_API_ENDPOINT=' >> .env.production
RUN npm install && npm run build


FROM jupyter/all-spark-notebook

USER root



## Install dependencies
RUN apt-get update -y && apt-get install -y nginx freetds-dev sqlite3 libsqlite3-dev lsb-release redis gnupg redis-server
COPY ./registry /usr/src/registry
WORKDIR /usr/src/registry/sql-registry
RUN pip install -r requirements.txt
WORKDIR /usr/src/registry/purview-registry
RUN pip install -r requirements.txt

## Remove default nginx index page and copy ui static bundle files
RUN rm -rf /usr/share/nginx/html/*
COPY --from=ui-build /usr/src/ui/build /usr/share/nginx/html
COPY ./deploy/nginx.conf /etc/nginx/nginx.conf

## Start service and then start nginx
WORKDIR /usr/src/registry
COPY ./deploy/start.sh .

# COPY ./feathr_project .
# RUN pip install -e ./feathr_project
# RUN rm -rf ./feathr_project
RUN pip install feathr

RUN curl -fsSL https://packages.redis.io/gpg | gpg --dearmor -o /usr/share/keyrings/redis-archive-keyring.gpg

RUN echo "deb [signed-by=/usr/share/keyrings/redis-archive-keyring.gpg] https://packages.redis.io/deb $(lsb_release -cs) main" | tee /etc/apt/sources.list.d/redis.list

RUN redis-server &
RUN ["chmod", "+x", "./start.sh"]
# CMD ["/bin/sh", "-c", "./start.sh"]