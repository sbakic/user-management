#!/bin/sh

export COMPOSE_FILE_PATH="${PWD}/docker-compose.yml"

export GRADLE_EXEC="./gradlew"

start() {
  docker-compose -f "$COMPOSE_FILE_PATH" up --build -d
}

down() {
  if [ -f "$COMPOSE_FILE_PATH" ]; then
    docker-compose -f "$COMPOSE_FILE_PATH" down
  fi
}

build() {
  $GRADLE_EXEC build
}

tail() {
  docker-compose -f "$COMPOSE_FILE_PATH" logs -f
}

case "$1" in
build_start)
  down
  build
  start
  tail
  ;;
start)
  start
  tail
  ;;
stop)
  down
  ;;
tail)
  tail
  ;;
*)
  echo "Usage: $0 {build_start|start|stop|tail}"
  ;;
esac
