package cn.tee3.n2m.biz.domain;


import cn.tee3.n2m.biz.util.StringUtil;

public class BaseDomain {
	
	@Override
	public String toString() {
		return StringUtil.getToString(this);
	}
	protected Double doubleSet(Double d){
		if(d != null){
			return d/100;
		}
		return d;
	}
	protected Double doubleGet(Double d){
		if(d == null){
			return 0.00;
		}else{
			return d/100;
		}
	}
	protected Long getLong(Long l){
		if(l != null){
			return l;
		}
		return (long)0;
	}
}
