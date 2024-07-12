package jp._1to.web.result;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public enum ResponseCode {
	NORMAL("0"), ERROR("1"),;

	String resultCode;

	public String getResultCode() {
		return this.resultCode;
	}

	// 処理結果コードからenumを取得
	public static ResponseCode getEnumByResultCode(String resultCode) {
		return Arrays.stream(ResponseCode.values()).filter(v -> v.getResultCode().equals(resultCode)).findFirst()
				.orElse(null);
	}
}
