# TODO: persist the SQLite file in the volumes
# TODO: predownload the maven packages
# TODO: make sure redis can use without password
# TODO: download the notebook to the workdir
# TODO: initialize the sql database schema



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
RUN apt-get update -y && apt-get install -y nginx freetds-dev sqlite3 libsqlite3-dev lsb-release redis gnupg redis-server lsof
COPY ./registry /usr/src/registry
WORKDIR /usr/src/registry/sql-registry
RUN pip install -r requirements.txt

## Remove default nginx index page and copy ui static bundle files
RUN rm -rf /usr/share/nginx/html/*
COPY --from=ui-build /usr/src/ui/build /usr/share/nginx/html
COPY ./deploy/nginx.conf /etc/nginx/nginx.conf

## Start service and then start nginx
WORKDIR /usr/src/registry
COPY ./deploy/start.sh /usr/src/registry/

# always install feathr from main
COPY ./feathr_project /tmp/feathr_project
RUN python -m pip install /tmp/feathr_project/
# RUN python -m pip install feathr

# RUN curl -fsSL https://packages.redis.io/gpg | gpg --dearmor -o /usr/share/keyrings/redis-archive-keyring.gpg

# RUN echo "deb [signed-by=/usr/share/keyrings/redis-archive-keyring.gpg] https://packages.redis.io/deb $(lsb_release -cs) main" | tee /etc/apt/sources.list.d/redis.list

# set redis password, since currently Feathr require the password to be set.
RUN sed -i 's/# requirepass foobared/requirepass foobared/g' /etc/redis/redis.conf


WORKDIR /home/jovyan/work
USER jovyan
ADD --chown=jovyan https://raw.githubusercontent.com/xiaoyongzhu/feathr/feathr-sandbox/docs/samples/local_quickstart_nyc_taxi_demo.ipynb .


USER root
WORKDIR /usr/src/registry
RUN ["chmod", "+x", "/usr/src/registry/start.sh"]
COPY ./docker/feathr_init_sql.py .
RUN python feathr_init_sql.py
# remove ^M chars in Linux to make sure the script can run
RUN sed -i "s/\r//g" /usr/src/registry/start.sh
CMD ["/bin/bash", "/usr/src/registry/start.sh"]

WORKDIR /home/jovyan/work


