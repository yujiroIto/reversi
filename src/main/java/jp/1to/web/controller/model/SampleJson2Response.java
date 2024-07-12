package jp._1to.web.controller.model;

import lombok.Data;

/**
 * サンプルレスポンス（レスポンスを JSON として返す）
 */
@Data
public class SampleJson2Response {
	private int px;
	private int py;
	private int color;
}
