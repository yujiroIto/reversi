package jp._1to.web.controller.model;

import lombok.Data;

/**
 * サンプルリクエスト（JSON でリクエストを受け取る）
 */
@Data
public class SampleJsonRequest {
	public int[][] paramA;
}
