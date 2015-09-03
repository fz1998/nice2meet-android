package com.threebody.sdk.http.entity;


import com.threebody.sdk.domain.BaseDomain;
import com.threebody.sdk.util.StringUtil;

public class BaseResponse extends BaseDomain {
	protected int status;
	protected String code;
	protected String msg;
	protected int hasNext;
	protected String st;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public int getHasNext() {
		return hasNext;
	}
	public void setHasNext(int hasNext) {
		this.hasNext = hasNext;
	}
	
	public String getSt() {
		return st;
	}
	public void setSt(String st) {
		this.st = st;
	}
	public boolean isHasNext(){
		if(hasNext == 1){
			return true;
		}
		return false;
	}
	public Long getLongErrorCode(){
		if(StringUtil.isNull(code)){
            try {
                return Long.parseLong(code);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		return (long)-11111;
	}
	
}
