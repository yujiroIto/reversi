# reversi

docker build -t reversi:1.0.1 .

docker run -p 8080:8080 -d --name reversi reversi:1.0.1
