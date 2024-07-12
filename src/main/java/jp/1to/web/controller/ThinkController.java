package jp._1to.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp._1to.web.result.ResponseCode;
import jp._1to.web.request.Request;
import jp._1to.web.service.ThinkService;
import jp._1to.web.controller.model.SampleJson2Response;
import jp._1to.web.controller.model.SampleJsonRequest;

@RestController
@RequestMapping("/think")
public class ThinkController {

	@Autowired
	ThinkService service;

	@ResponseBody
	@PostMapping(path="/execute", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SampleJson2Response> execute(@RequestBody SampleJsonRequest request){
		if (request == null) {
			// リクエストに問題がある場合のレスポンス
			return this.errorBadRequestResponse();
		}
		// API実行処理
		try {
			int[][] paramA = request.getParamA();
			SampleJson2Response response = service.think(paramA);
			if (response != null) {
				return ResponseEntity.ok(response);
			}
			// リクエストに問題がある場合のレスポンス
			return this.errorBadRequestResponse();
			// ResponseCode response = service.think();
			// if(response.getResultCode().equals("0")) {
			// 	System.out.println("API連携成功");
			// 	System.out.println("処理結果コード：" + response.getResultCode());
			// } else {
			// 	System.out.println("API連携失敗");
			// 	System.out.println("処理結果コード：" + response.getResultCode());
			// }
		} catch(Exception e ) {
			System.out.println("API連携でエラーが発生しました。");
			return this.errorBadRequestResponse();
		}
	}

	/**
	 * リクエストに問題がある場合のレスポンス
	 * @return
	 */
	private ResponseEntity<SampleJson2Response> errorBadRequestResponse() {
		SampleJson2Response response = new SampleJson2Response();
		response.setPx(-1);
		response.setPy(-1);
		response.setColor(0);

		return ResponseEntity.badRequest().body(response);
	}
}
