// eslint-disable-next-line @typescript-eslint/no-var-requires
const jsonServer = require("json-server");
const server = jsonServer.create();
const router = jsonServer.router(require("./db.js")());
const middleware = jsonServer.defaults();

const endpoints = {
  "/mentor-requests": "/mentorRequestList",
  "/authenticate": "/userTestData",
  "/validate-jwt": "/validateToken",
  "/mentor-requests/new": "/createMentorRequest"
};

const port = 3030;

server.use((req, res, next) => {
  if (req.method === "POST") {
    req.method = "GET";
  }
  next();
});

server.use((req, res, next) => {
  setTimeout(() => next(), 200);
});

server.use(middleware);
server.use(jsonServer.rewriter(endpoints));
server.use(router);

server.listen(port, function() {
  console.log(`JSON Server is running at http://localhost:${port}\n`);
  console.log("Available endpoints:");
  Object.keys(endpoints).map(endpoint =>
    console.log(`- http://localhost:${port}${endpoint.split("?")[0]}`)
  );
});
