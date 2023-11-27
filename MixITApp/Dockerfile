FROM node:latest as build-stage
WORKDIR /app
COPY . .
RUN npm install
CMD ["npm", "start"]