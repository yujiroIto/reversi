# reversi

docker build -t reversi:1.0 .
docker container run -p 8080:8081 -d --name reversi reversi:1.0
