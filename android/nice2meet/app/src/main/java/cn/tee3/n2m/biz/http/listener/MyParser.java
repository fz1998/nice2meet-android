package cn.tee3.n2m.biz.http.listener;

import cn.tee3.n2m.biz.http.entity.BaseResponse;

public interface MyParser {
	public void onFinish(BaseResponse result);
}
