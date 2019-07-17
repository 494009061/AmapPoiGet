package com.haotian.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import java.util.Arrays;

public class HttpResponse {

	private int code;

	private Object message;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getStringMessage(){

		if (message == null) {
			return null;
		}

		if (message instanceof String) {
			return String.valueOf(message);
		} else {
			throw new RuntimeException("Type is not an String");
		}

	}


	public <T> T  toObject(Class<T> clazz){

		if(message==null){
			return null;
		}

		if(message instanceof String){
			return JSON.parseObject(String.valueOf(this.message),clazz);
		}

		throw new JSONException("The message must be a string of type json");
	}

	public byte[] getByteArrayMessage(){

		if (message == null) {
			return null;
		}

		if (message.getClass().isArray()) {
			return (byte[]) message;
		} else {
			throw new RuntimeException("Type is not an array");
		}

	}

	public void setMessage(Object message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "HttpResponse [code=" + code + ",message=" + (message==null?null:message instanceof String ? message.toString()
				: Arrays.toString(((byte[]) message))) + "]";
	}

}
