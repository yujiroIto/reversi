package jp._1to.web.service;

import java.util.List;
import java.util.ArrayList;
import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jp._1to.web.result.Result;
import jp._1to.web.result.ResponseCode;
import jp._1to.web.controller.model.SampleJson2Response;
import jp._1to.web.request.Request;
import jp._1to.web.result.Response;

@Service
public class ThinkService {

	private int[][] WeightData = {
		{30, -12, 0, -1, -1, 0, -12, 30},
		{-12, -15, -3, -3, -3, -3, -15, -12},
		{0, -3, 0, -1, -1, 0, -3, 0},
		{-1, -3, -1, -1, -1, -1, -3, -1},
		{-1, -3, -1, -1, -1, -1, -3, -1},
		{0, -3, 0, -1, -1, 0, -3, 0},
		{-12, -15, -3, -3, -3, -3, -15, -12},
		{30, -12, 0, -1, -1, 0, -12, 30},
	};
	public static int BLACK = 1;
    public static int WHITE = 2;
	private int data[][] = new int[8][8];

	/**
	 * (i,j)にcolor色の駒を置く
	 */
	private void put(int i, int j, int color) {
		data[i][j] = color;
	}

	/**
	 * コンピュータ思考関数
	 */
	public SampleJson2Response think(int[][] paramA) {
		data = paramA;
		int highScore = -1000;
		int px = -1,
			py = -1;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				int[][] tmpData = copyData();
				List<int[]> flipped = getFlipCells(x, y, WHITE);
				if (flipped != null && !flipped.isEmpty()) {
					for(int[] pos : flipped) {
						int p = pos[0];
						int q = pos[1];
						tmpData[p][q] = WHITE;
						tmpData[x][y] = WHITE;
					}
					int score = calcWeightData(tmpData);
					if (score > highScore) {
						highScore = score;
						px = x;
						py = y;
					}
				}
			}
		}

		if (px >= 0 && py >= 0) {
			SampleJson2Response response = new SampleJson2Response();
			response.setPx(px);
			response.setPy(py);
			response.setColor(WHITE);
			return response;
		}
		return null;
	}

	/**
	 * 重みづけ計算
	 */
	private int calcWeightData(int[][] tmpData) {
		int score = 0;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (tmpData[x][y] == WHITE) {
					score += WeightData[x][y];
				}
			}
		}
		return score;
	}

	/**
	 * 駒テーブルデータをコピー
	 */
	private int[][] copyData() {
		int[][] tmpData = new int[8][8];
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				tmpData[x][y] = data[x][y];
			}
		}
		return tmpData;
	}

	/**
	 * (i,j)に駒をおいたときに駒を挟めるか？
	 */
	private List<int[]> getFlipCells(int i, int j, int color) {
		if (data[i][j] == BLACK || data[i][j] == WHITE) {
			// 既に駒がある
			return null;
		}

		// 相手を挟めるか、左上、上、右上、左、右、左下、下、右下と順番に調査
		int[][] dirs = {
			{-1, -1},
			{0, -1},
			{1, -1},
			{-1, 0},
			{1, 0},
			{-1, 1},
			{0, 1},
			{1, 1},
		};
		List<int[]> result = new ArrayList();
		for (int p = 0; p < dirs.length; p++) {
			List<int[]> flipped = getFlipCellsOneDir(i, j, dirs[p][0], dirs[p][1], color);
			if (flipped != null) {
				result.addAll(flipped);
			}
		}
		return result;
	}

	/**
	 * (i,j)に駒をおいたときに、(dx,dy)方向で駒を挟めるか？
	 */
	private List<int[]> getFlipCellsOneDir(int i, int j, int dx, int dy, int color) {
		int x = i + dx;
		int y = j + dy;
		List<int[]> fliped = new ArrayList();

		if (x < 0 || y < 0 || x > 7 || y > 7 || data[x][y] == color || data[x][y] == 0) {
			// 盤外、同色、空ならfalse
			return null;
		}
		int[] pos = {x, y};
		fliped.add(pos);

		while (true) {
			x += dx;
			y += dy;
			if (x < 0 || y < 0 || x > 7 || y > 7 || data[x][y] == 0) {
				// 盤外、空ならfalse
				return null;
			}
			if (data[x][y] == color) {
				// 挟めた！
				return fliped;
			} else {
				pos[0] = x;
				pos[1] = y;
				fliped.add(pos);
			}
		}
	}
}
