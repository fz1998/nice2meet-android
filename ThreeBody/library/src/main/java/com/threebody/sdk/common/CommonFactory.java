package com.threebody.sdk.common;


/**
 * 组件工厂类,单例
 * 
 * @author Sean.xie
 * 
 */
public class CommonFactory {

    private static CommonFactory instance = null;
    private CommonFactory(){}
    public static CommonFactory getInstance(){
        if(instance == null){
            instance = new CommonFactory();
        }
        return instance;
    }
}
