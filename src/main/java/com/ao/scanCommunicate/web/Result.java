package com.ao.scanCommunicate.web;

import com.ao.scanElectricityBis.base.ScanElectricityException;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * ���ؽ���ṹ
 * @author aohanhe
 *
 */
@ApiModel("���ؽ����")
public class Result<T> extends BaseResult {
	

	@ApiModelProperty(name="��������")
	private T data = null;
	
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
	public Result(int code, String message) {
		super(code, message);		
	}
	
	/**
	 * ����һ��ʧ�ܽ��
	 * @param code
	 * @param message
	 * @return
	 * @throws ScanElectricityException 
	 */
	public static<T> Result fail(int code,String message) throws ScanElectricityException{
		
		if(code==0) throw new ScanElectricityException("ʧ�ܽ����������code Ϊ0");
		
		return new Result(code,message);
	}
	
	/**
	 * ����һ����׼����
	 * @param message
	 * @return
	 * @throws ScanElectricityException
	 */
	public static<T> Result fail(String message) throws ScanElectricityException{
		return new Result(500,message);		
	}
	
	/**
	 * ����һ���ɹ����
	 * @param data
	 * @return
	 */
	public static<T> Result success(T data) {
		Result<T> re=new Result<>(0, "�ɹ�");
		re.setData(data);
		return re;
	}
}
