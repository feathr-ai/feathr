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

# UI Sectioin
## Remove default nginx index page and copy ui static bundle files
RUN rm -rf /usr/share/nginx/html/*
COPY --from=ui-build /usr/src/ui/build /usr/share/nginx/html
COPY ./deploy/nginx.conf /etc/nginx/nginx.conf


# Feathr Package Installation Section
# always install feathr from main
WORKDIR /home/jovyan/work
COPY --chown=1000:100 ./feathr_project ./feathr_project
RUN python -m pip install  -e ./feathr_project


# Registry Section
# install registry
COPY ./registry /usr/src/registry
WORKDIR /usr/src/registry/sql-registry
RUN pip install -r requirements-sandbox.txt



## Start service and then start nginx
WORKDIR /usr/src/registry
COPY ./deploy/start_local.sh /usr/src/registry/


# default dir by the jupyter image
WORKDIR /home/jovyan/work
USER jovyan
# copy as the jovyan user
# UID is like this: uid=1000(jovyan) gid=100(users) groups=100(users)
COPY --chown=1000:100 ./docs/samples/local_quickstart_nyc_taxi_demo.ipynb .
COPY --chown=1000:100 ./docker/feathr_init_script.py .

# Run the script so that maven cache can be added for better experience. Otherwise users might have to wait for some time for the maven cache to be ready.
RUN python feathr_init_script.py

USER root
WORKDIR /usr/src/registry
RUN ["chmod", "+x", "/usr/src/registry/start_local.sh"]

# remove ^M chars in Linux to make sure the script can run
RUN sed -i "s/\r//g" /usr/src/registry/start_local.sh

ENV JUPYTER_TOKEN=feathr
# run the service so we can initialize
# RUN  ["/bin/bash", "/usr/src/registry/start.sh"]
CMD ["/bin/bash", "/usr/src/registry/start_local.sh"]


WORKDIR /home/jovyan/work