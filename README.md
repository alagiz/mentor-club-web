# mentor-club-web
##### frontend
[![build status][build badge]][BUILD_URL]
[![coverage status][coverage badge]][COVERAGE_URL]


## development

### to start the app
* run 
  ```sh
  cd application
  yarn install # to install dependencies
  yarn json-server # to start local server with mock data
  yarn start # to start the app
  ```
* login with any credentials

[BUILD_URL]: https://travis-ci.org/ArtemAlagizov/mentor-club-react
[build badge]: https://img.shields.io/travis/ArtemAlagizov/mentor-club-react/master?style=flat-square
[COVERAGE_URL]: https://coveralls.io/github/ArtemAlagizov/mentor-club-react?branch=master
[coverage badge]: https://img.shields.io/coveralls/github/ArtemAlagizov/mentor-club-react.svg?style=flat-square&color=brightgreen
[QUALITY_URL]: https://www.codacy.com/manual/ArtemAlagizov/mentor-club-react
[quality badge]: https://img.shields.io/codacy/grade/cd908732011c47bf831d2b661684babf?style=flat-square

## deployment
the app is deployed as a Docker Swarm stack of services into AWS t2.micro instance 
