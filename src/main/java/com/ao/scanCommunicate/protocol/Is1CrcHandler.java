package com.ao.scanCommunicate.protocol;

import ahh.swallowIotServer.protocol.DataCrcHandler;

/**
 * 驿联协议的CRC处理器
 * @author aohan
 *
 */
public class Is1CrcHandler implements DataCrcHandler {

	@Override
	public boolean checkDataSign(byte[] data, int start, int len, Object signData) {
		byte crc=this.generateCrcData(data, start, len);
		byte checkCrc=(byte)signData;
		
		
		return crc==checkCrc;
	}

	@Override
	public byte[] generateData(byte[] data, int start, int len) {
		
		return new byte[] {generateCrcData(data,start,len)};
	}
	
	/**
	 * 从字节数组生成Crc数据
	 * @param data
	 * @return
	 */
	private byte generateCrcData(byte[] data, int start, int len) {
		byte crc = 0;
		for (int i = start; i < start+len; i++) {
			crc = (byte) (crc ^ data[i]);
		}
		return crc;		
	}

}
