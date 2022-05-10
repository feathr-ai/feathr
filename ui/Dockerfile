FROM node:16-alpine as ui-build
WORKDIR /usr/src/app
COPY . .
RUN npm install && npm run build

FROM nginx:alpine

## Remove default nginx index page
RUN rm -rf /usr/share/nginx/html/*

# Copy from the stage 1
COPY --from=ui-build /usr/src/app/build/ /usr/share/nginx/html

ENTRYPOINT ["nginx", "-g", "daemon off;"]

