FROM node:16-alpine as ui-build
WORKDIR /usr/src
COPY . .
RUN npm install && npm run build

FROM nginx:alpine

## Remove default nginx index page
RUN rm -rf /usr/share/nginx/html/*

# Copy production bundle from stage 1
COPY --from=ui-build /usr/src/build/ /usr/share/nginx/html

ENTRYPOINT ["nginx", "-g", "daemon off;"]

