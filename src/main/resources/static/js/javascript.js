﻿"use strict";

var BLACK = 1,
  WHITE = 2;
var data = [];
var myTurn = false;

/**
 * 初期化関数
 */
function init() {
  var b = document.getElementById("board");
  for (var i = 0; i < 8; i++) {
    var tr = document.createElement("tr");
    data[i] = [0, 0, 0, 0, 0, 0, 0, 0];
    for (var j = 0; j < 8; j++) {
      var td = document.createElement("td");
      td.className = "cell";
      td.id = "cell" + i + j;
      td.onclick = clicked;
      tr.appendChild(td);
    }
    b.appendChild(tr);
  }
  put(3, 3, BLACK);
  put(4, 4, BLACK);
  put(3, 4, WHITE);
  put(4, 3, WHITE);
  update();
}

function update() {
  var numWhite = 0,
    numBlack = 0;
  for (var x = 0; x < 8; x++) {
    for (var y = 0; y < 8; y++) {
      if (data[x][y] == WHITE) {
        numWhite++;
      }
      if (data[x][y] == BLACK) {
        numBlack++;
      }
    }
  }
  document.getElementById("numBlack").textContent = numBlack;
  document.getElementById("numWhite").textContent = numWhite;

  var blackFlip = canFlip(BLACK);
  var whiteFlip = canFlip(WHITE);

  if (numWhite + numBlack == 64 || (!blackFlip && !whiteFlip)) {
    showMessage("ゲームオーバー");
  } else if (!blackFlip) {
    showMessage("黒スキップ");
    myTurn = false;
  } else if (!whiteFlip) {
    showMessage("白スキップ");
    myTurn = true;
  } else {
    myTurn = !myTurn;
  }
  if (!myTurn) {
    think();
  }
}

async function think() {
  const url = "/app/think/execute";
  const body = {paramA: data};
  console.log(body);
  try {
    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
    });
    if (!response.ok) {
      throw new Error("Network response was not OK");
    }
    const result = await response.json();
    console.log("Success:", result);

    var px = result.px;
    var py = result.py;
    if (px >= 0 && py >= 0) {
      var flipped = getFlipCells(px, py, WHITE);
      if (flipped.length > 0) {
        for (var k = 0; k < flipped.length; k++) {
          put(flipped[k][0], flipped[k][1], WHITE);
        }
      }
      put(px, py, WHITE);
    }

    update();
  } catch (error) {
    console.error("Error:", error);
  }
}

function showMessage(str) {
  document.getElementById("message").textContent = str;
  setTimeout(function () {
    document.getElementById("message").textContent = "";
  }, 2000);
}

/**
 * 盤上のセルクリック時のコールバック関数
 */
function clicked(e) {
  if (!myTurn) {
    // PC考え中
    return;
  }
  var id = e.target.id;
  var i = parseInt(id.charAt(4));
  var j = parseInt(id.charAt(5));

  var flipped = getFlipCells(i, j, BLACK);
  if (flipped.length > 0) {
    for (var k = 0; k < flipped.length; k++) {
      put(flipped[k][0], flipped[k][1], BLACK);
    }
    put(i, j, BLACK);
    update();
  }
}

/**
 * (i,j)にcolor色の駒を置く
 */
function put(i, j, color) {
  var c = document.getElementById("cell" + i + j);
  c.textContent = "●";
  c.className = "cell " + (color == BLACK ? "black" : "white");
  data[i][j] = color;
}

/**
 * 挟める駒があるか？
 */
function canFlip(color) {
  for (var x = 0; x < 8; x++) {
    for (var y = 0; y < 8; y++) {
      var flipped = getFlipCells(x, y, color);
      if (flipped.length > 0) {
        return true;
      }
    }
  }
  return false;
}

/**
 * (i,j)に駒をおいたときに駒を挟めるか？
 */
function getFlipCells(i, j, color) {
  if (data[i][j] == BLACK || data[i][j] == WHITE) {
    // 既に駒がある
    return [];
  }

  // 相手を挟めるか、左上、上、右上、左、右、左下、下、右下と順番に調査
  var dirs = [
    [-1, -1],
    [0, -1],
    [1, -1],
    [-1, 0],
    [1, 0],
    [-1, 1],
    [0, 1],
    [1, 1],
  ];
  var result = [];
  for (var p = 0; p < dirs.length; p++) {
    var flipped = getFlipCellsOneDir(i, j, dirs[p][0], dirs[p][1], color);
    result = result.concat(flipped);
  }
  return result;
}

/**
 * (i,j)に駒をおいたときに、(dx,dy)方向で駒を挟めるか？
 */
function getFlipCellsOneDir(i, j, dx, dy, color) {
  var x = i + dx;
  var y = j + dy;
  var fliped = [];

  if (x < 0 || y < 0 || x > 7 || y > 7 || data[x][y] == color || data[x][y] == 0) {
    // 盤外、同色、空ならfalse
    return [];
  }
  fliped.push([x, y]);

  while (true) {
    x += dx;
    y += dy;
    if (x < 0 || y < 0 || x > 7 || y > 7 || data[x][y] == 0) {
      // 盤外、空ならfalse
      return [];
    }
    if (data[x][y] == color) {
      // 挟めた！
      return fliped;
    } else {
      fliped.push([x, y]);
    }
  }
}
