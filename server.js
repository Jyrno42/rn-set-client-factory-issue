const http = require('http');

const requestListener = function (req, res) {
  res.writeHead(200);
  res.end(`fromjava:${req.headers.fromjava}`);
  console.log(`Got request ${req.url} - ${req.headers.fromjava}`);
};

const server = http.createServer(requestListener);
server.listen(8080);
