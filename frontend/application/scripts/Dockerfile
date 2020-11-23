# ------ build container ------
FROM node:alpine as node-build

WORKDIR /usr/src/app

RUN npm install json-server

# ------ build container ------
#FROM node:alpine as node-serve
#
#
#WORKDIR /usr/src/app
COPY . .
#COPY server.ts .
#COPY ../../integration-tests/src/fixtures/userTestData .
#
## static build
#COPY --from=node-build /usr/src/app/ .
#COPY --from=node-build /usr/src/app/node_modules .

RUN ls
RUN pwd

EXPOSE 3030
CMD ["node", "server.ts"]
