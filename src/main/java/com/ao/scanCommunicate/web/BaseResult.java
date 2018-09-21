package com.ao.scanCommunicate.web;

import com.ao.scanElectricityBis.base.ScanElectricityException;

import io.swagger.annotations.ApiModelProperty;

public class BaseResult {
	@ApiModelProperty(name="����״̬��",value="����״̬��,0 ��ʾ�ɹ�")
	private int code= 0;
	@ApiModelProperty(name="������Ϣ",value="����״̬��Ϣ")
	private String  message;
	
	
	public BaseResult(int code,String message) {
		this.code = code;
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * ����һ��ʧ�ܽ��
	 * @param code
	 * @param message
	 * @return
	 * @throws ScanElectricityException 
	 */
	public static BaseResult failResult(int code,String message) throws ScanElectricityException{
		
		if(code==0) throw new ScanElectricityException("ʧ�ܽ����������code Ϊ0");
		
		return new BaseResult(code,message);
	}
	
	/**
	 * ����һ���ɹ����
	 * @param data
	 * @return
	 */
	public static BaseResult successResult() {
		BaseResult re=new BaseResult(0, "�ɹ�");
		
		return re;
	}

}
