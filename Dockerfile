# Stage 1: build frontend ui
FROM node:16-alpine as ui-build
WORKDIR /usr/src/ui
COPY ./ui .

## Use api endpoint from same host and build production static bundle
RUN echo 'REACT_APP_API_ENDPOINT=' >> .env.production
RUN npm install && npm run build

# Stage 2: build backend and start nginx to as reserved proxy for both ui and backend
FROM python:3.9

## Install dependencies
RUN apt-get update -y && apt-get install -y nginx

WORKDIR /usr/src/backend
COPY ./registry/sql-registry /usr/src/backend
RUN pip install -r requirements.txt

## Remove default nginx index page and copy ui static bundle files
RUN rm -rf /usr/share/nginx/html/*
COPY --from=ui-build /usr/src/ui/build /usr/share/nginx/html
COPY ../deploy/nginx.conf /etc/nginx/nginx.conf

# Start
WORKDIR /usr/src/backend
COPY ../deploy/env.sh .
CMD ["/bin/sh", "-c", "./env.sh && nginx -g \"daemon off;\" && uvicorn main:app --host 0.0.0.0 --port 8000"]