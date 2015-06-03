package com.xxl.action.hex.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xxl.core.util.HexUtil;

public class ResponseStreamFactory {
	private static transient Logger logger = LoggerFactory.getLogger(ResponseStreamFactory.class);
	private ByteBuffer m_byteBuf = null;	
	public ResponseStreamFactory() {
		m_byteBuf = ByteBuffer.allocate(1024 * 4);
	}
	
	/**
	 * 写入整型数值
	 * @param iInt
	 */
	public void writeInt(int iInt) {
		byte[] intBytes = new byte[4];
		for (int index = 0; index < 4; index++) {
			intBytes[index] = (byte) (iInt >>> (index * 8));
		}
		m_byteBuf.put(intBytes);
	}
	
	/**
	 * 写入整型数组
	 * @param iIntBytes
	 */
	public void write(int[] iIntBytes) {
		for (int index = 0; index < iIntBytes.length; index++) {
			writeInt(iIntBytes[index]);
		}
	}
	
	/**
	 * 写入二进制数组
	 * @param bytes
	 */
	public void write(byte[] bytes) {
		m_byteBuf.put(bytes);
	}
	
	/**
	 * 写字符串
	 * @param msg
	 * @param iBytes
	 */
	public void writeString(String msg, int iBytes) {
		byte[] bytes = new byte[iBytes];
		if (msg != null && msg.trim().length() > 0) {
			try {
				byte[] infoBytes = msg.getBytes("UTF-8");
				int len = infoBytes.length < iBytes ? infoBytes.length : iBytes;			
				System.arraycopy(infoBytes, 0, bytes, 0, len);
			} catch (UnsupportedEncodingException e) {
				logger.error("[response stream factory encoding exception.]", e);
			}
		}	
		
		m_byteBuf.put(bytes);	
	}
	
	/**
	 * 获取十六进制输出
	 * @return
	 */
	public String getHexResponse() {
		m_byteBuf.flip();
		if (m_byteBuf.limit() == 0) {
			return "";
		}
		
		byte[] bytes = new byte[m_byteBuf.limit()];
		m_byteBuf.get(bytes);
		
		String hex = HexUtil.getHex(bytes);
		return hex;
	}
	
	/**
	 * 发送二进制流
	 * @param response
	 */
	public void flushBytes(HttpServletResponse response) {
		m_byteBuf.flip();
		if (m_byteBuf.limit() == 0) {
			return;
		}
		
		byte[] bytes = new byte[m_byteBuf.limit()];
		m_byteBuf.get(bytes);
		
		response.setContentLength(bytes.length);
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			out.write(bytes);
			out.flush();
		} catch (IOException e) {
			logger.error("[response stream factory io exception.]", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error("[response stream factory io exception.]", e);
				}
			}
		}
	}
	
	/**
	 * 获取byte数组
	 * @return
	 */
	public byte[] toBytes() {
		m_byteBuf.flip();
		if (m_byteBuf.limit() == 0) {
			return null;
		}
		
		byte[] bytes = new byte[m_byteBuf.limit()];
		m_byteBuf.get(bytes);
		
		return bytes;
	}
}
