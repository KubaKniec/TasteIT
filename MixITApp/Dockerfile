FROM node:latest AS build
WORKDIR /app

COPY . .
RUN npm install
RUN npm run build
FROM nginx:alpine
COPY --from=build /app/dist/mixit/ /usr/share/nginx/html
EXPOSE 80
