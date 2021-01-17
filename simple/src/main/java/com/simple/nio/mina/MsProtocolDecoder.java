//package com.simple.nio.mina;
//
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.apache.mina.core.buffer.IoBuffer;
//import org.apache.mina.core.session.IoSession;
//import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
//import org.apache.mina.filter.codec.ProtocolDecoderOutput;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.io.Serializable;
//import java.io.UnsupportedEncodingException;
//import java.net.HttpURLConnection;
//import java.net.InetAddress;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLConnection;
//import java.net.UnknownHostException;
//import java.util.Calendar;
//import java.util.Properties;
//import java.io.Serializable;
//
//import java.io.UnsupportedEncodingException;
//import java.util.Calendar;
//import java.util.zip.CRC32;
//import org.apache.mina.core.buffer.IoBuffer;
//
//public class MsProtocolDecoder extends CumulativeProtocolDecoder {
//	protected final Log log = LogFactory.getLog(getClass());
//
//	// 开始标志
//	byte startFlag = 0x5b;
//	// 结束标志
//	byte endFlag = 0x5d;
//
//	protected boolean doDecode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput out) throws Exception {
//		int remaining = ioBuffer.remaining();
//		int startPosition = ioBuffer.position();
//		if (remaining < 26)
//			return false;
//		byte header = ioBuffer.get();
//		if (header != startFlag) {
//			ioBuffer.position(startPosition + remaining);
//			return true;
//		}
//		int flag = 0;
//		int len = 0;
//		while (ioBuffer.hasRemaining()) {
//			len++;
//			if (ioBuffer.get() != endFlag)
//				continue;
//			flag = 1;
//		}
//		if (flag == 0) {
//			ioBuffer.position(startPosition);
//			return false;
//		}
//		ioBuffer.position(startPosition + 1);
//		byte[] bs = new byte[len - 1];
//		ioBuffer.get(bs);
//		if (this.log.isDebugEnabled())
//			this.log.debug("recv data:" + NetUtil.format(bs, " ", bs.length));
//		bs = MSTool.unescapeCharacter(bs);
//		if (checkData(bs)) {
//			/*MsMessage msMessage = new MsMessage(bs);
//			if (msMessage != null)
//				out.write(msMessage);*/
//			out.write(new String("XXX"));
//		} else {
//			this.log.warn(NetUtil.format(bs, " ", bs.length) + ", check fail");
//		}
//		ioBuffer.position(startPosition + len + 1);
//		return true;
//	}
//
//	private boolean checkData(byte[] data) {
//		int crc = MSTool.crc16CCITT(data);
//		if (crc == 0)
//			return true;
//		return false;
//	}
//	public static final class NetUtil {
//		public static Object cloneSerialData(Serializable object) {
//			try {
//				ByteArrayOutputStream ba = new ByteArrayOutputStream();
//				ObjectOutputStream oo = new ObjectOutputStream(ba);
//				oo.writeObject(object);
//				oo.flush();
//				byte[] buf = ba.toByteArray();
//				ObjectInputStream ii = new ObjectInputStream(new ByteArrayInputStream(buf));
//				Serializable a = (Serializable)ii.readObject();
//				ii.close();
//				oo.close();
//				return a;
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//		}
//
//		public static int computSerialDataSize(Serializable object) {
//			try {
//				ByteArrayOutputStream ba = new ByteArrayOutputStream();
//				ObjectOutputStream oo = new ObjectOutputStream(ba);
//				oo.writeObject(object);
//				oo.flush();
//				byte[] buf = ba.toByteArray();
//				return buf.length;
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//		}
//
//		public static String readLine() throws IOException {
//			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//			String str = in.readLine();
//			return str;
//		}
//
//		public static byte[] changeIPStr(String ip) {
//			String[] temp = ip.split("[.]");
//			byte[] b = new byte[temp.length];
//			for (int i = 0; i < b.length; i++)
//				b[i] = (byte)Integer.parseInt(temp[i]);
//			return b;
//		}
//
//		public static InetAddress changeIPStrToAddress(String ip) {
//			byte[] b = changeIPStr(ip);
//			InetAddress address = null;
//			try {
//				address = InetAddress.getByAddress(b);
//			} catch (UnknownHostException e) {
//				throw new RuntimeException(e);
//			}
//			return address;
//		}
//
//		public static Properties readProperiesFile(InputStream in) {
//			Properties properties = new Properties();
//			try {
//				properties.load(in);
//			} catch (IOException e) {
//				throw new RuntimeException(e);
//			}
//			return properties;
//		}
//
//		public static InputStream transStrToInputStream(String str) {
//			ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
//			return in;
//		}
//
//		public static String transInputStreamToStr(InputStream in) throws IOException {
//			StringBuffer sb = new StringBuffer();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//			String str = null;
//			try {
//				while ((str = reader.readLine()) != null)
//					sb.append(str);
//			} catch (IOException e) {
//				throw e;
//			} finally {
//				reader.close();
//			}
//			return sb.toString();
//		}
//
//		public static InputStream sendHttpRequest(String url, String xml) {
//			URL u = null;
//			HttpURLConnection con = null;
//			PrintWriter out = null;
//			InputStream ret = null;
//			try {
//				u = new URL(url);
//				con = (HttpURLConnection)u.openConnection();
//				con.setRequestProperty("content-type", "text/html;");
//				con.setUseCaches(false);
//				con.addRequestProperty("Connection", "close");
//				con.setRequestMethod("POST");
//				con.setConnectTimeout(30000);
//				con.setReadTimeout(30000);
//				con.setDoInput(true);
//				con.setDoOutput(true);
//				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(con.getOutputStream())));
//				out.println(xml);
//				out.flush();
//				ret = con.getInputStream();
//			} catch (MalformedURLException e) {
//				throw new RuntimeException(e);
//			} catch (IOException e) {
//				throw new RuntimeException(e);
//			} finally {
//				if (out != null)
//					out.close();
//			}
//			return ret;
//		}
//
//		public static InputStream sendHttpRequest2(String url, String formData) {
//			URL u = null;
//			URLConnection con = null;
//			PrintWriter out = null;
//			InputStream ret = null;
//			try {
//				u = new URL(url);
//				con = u.openConnection();
//				con.setDoInput(true);
//				con.setDoOutput(true);
//				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(con.getOutputStream())));
//				out.println(formData);
//				out.flush();
//				ret = con.getInputStream();
//			} catch (MalformedURLException e) {
//				throw new RuntimeException(e);
//			} catch (IOException e) {
//				throw new RuntimeException(e);
//			} finally {
//				if (out != null)
//					out.close();
//			}
//			return ret;
//		}
//
//		public static InputStream sendHttpRequest(String url, boolean cache, String formData, String language) {
//			URL u = null;
//			URLConnection con = null;
//			PrintWriter out = null;
//			InputStream ret = null;
//			try {
//				u = new URL(url);
//				con = u.openConnection();
//				con.setUseCaches(cache);
//				if (!language.isEmpty())
//					con.setRequestProperty("Accept-Language", language);
//				con.setDoInput(true);
//				con.setDoOutput(true);
//				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(con.getOutputStream())));
//				if (!formData.isEmpty())
//					out.println(formData);
//				out.flush();
//				ret = con.getInputStream();
//			} catch (MalformedURLException e) {
//				throw new RuntimeException(e);
//			} catch (IOException e) {
//				throw new RuntimeException(e);
//			} finally {
//				if (out != null)
//					out.close();
//			}
//			return ret;
//		}
//
//		public static String sendHttpRequest(String httpUrl) {
//			StringBuffer sb = new StringBuffer();
//			URL url = null;
//			URLConnection connection = null;
//			BufferedReader reader = null;
//			try {
//				url = new URL(httpUrl);
//				connection = url.openConnection();
//				connection.setDefaultUseCaches(true);
//				connection.setUseCaches(true);
//				connection.setDoInput(true);
//				connection.setDoOutput(true);
//				reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "GBK"));
//				String line;
//				while ((line = reader.readLine()) != null)
//					sb.append(line);
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			} finally {
//				if (reader != null)
//					try {
//						reader.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//			}
//			return sb.toString();
//		}
//
//		public static StringBuffer readHttpData(String httpUrl) {
//			StringBuffer sb = new StringBuffer();
//			URL url = null;
//			URLConnection connection = null;
//			BufferedReader reader = null;
//			try {
//				url = new URL(httpUrl);
//				connection = url.openConnection();
//				connection.setDefaultUseCaches(false);
//				connection.setUseCaches(false);
//				connection.setDoInput(true);
//				connection.setDoOutput(true);
//				reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "GBK"));
//				String line;
//				while ((line = reader.readLine()) != null)
//					sb.append(line);
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			} finally {
//				if (reader != null)
//					try {
//						reader.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//			}
//			return sb;
//		}
//
//		public static String sendHttpPostRequest(String httpUrl) {
//			StringBuffer sb = new StringBuffer();
//			String[] stres = httpUrl.split("[?]");
//			URL url = null;
//			URLConnection connection = null;
//			BufferedReader reader = null;
//			try {
//				url = new URL(stres[0]);
//				connection = url.openConnection();
//				connection.setDefaultUseCaches(true);
//				connection.setUseCaches(true);
//				connection.setDoInput(true);
//				connection.setDoOutput(true);
//				connection.getOutputStream().write(stres[1].getBytes());
//				connection.getOutputStream().flush();
//				reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "GBK"));
//				String line;
//				while ((line = reader.readLine()) != null)
//					sb.append(line);
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			} finally {
//				if (reader != null)
//					try {
//						reader.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//			}
//			return sb.toString();
//		}
//
//		public static StringBuffer readHttpData(String httpUrl, String formate) {
//			StringBuffer sb = new StringBuffer();
//			URL url = null;
//			URLConnection connection = null;
//			BufferedReader reader = null;
//			try {
//				url = new URL(httpUrl);
//				connection = url.openConnection();
//				connection.setDefaultUseCaches(false);
//				connection.setUseCaches(false);
//				connection.setDoInput(true);
//				connection.setDoOutput(true);
//				reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), formate));
//				String line;
//				while ((line = reader.readLine()) != null)
//					sb.append(line + "\r\n");
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			} finally {
//				if (reader != null)
//					try {
//						reader.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//			}
//			return sb;
//		}
//
//		public static BufferedReader readHttpDataToReader(String httpUrl, String formate) {
//			URL url = null;
//			URLConnection connection = null;
//			BufferedReader reader = null;
//			try {
//				url = new URL(httpUrl);
//				connection = url.openConnection();
//				connection.setDefaultUseCaches(false);
//				connection.setUseCaches(false);
//				connection.setDoInput(true);
//				connection.setDoOutput(true);
//				reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), formate));
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			} finally {}
//			return reader;
//		}
//
//		public static InputStream readHttpDataToStream(String httpUrl) throws Exception {
//			URL url = null;
//			URLConnection connection = null;
//			url = new URL(httpUrl);
//			connection = url.openConnection();
//			connection.setDefaultUseCaches(false);
//			connection.setUseCaches(false);
//			connection.setDoInput(true);
//			connection.setDoOutput(true);
//			return connection.getInputStream();
//		}
//
//		public static String format(byte[] byteVal, String flag, int len) {
//			StringBuffer buf = new StringBuffer(13);
//			for (int i = 0; i < byteVal.length && i < len; i++) {
//				String str = Integer.toHexString(byteVal[i]);
//				if (str.length() < 2) {
//					buf.append("0" + str);
//				} else {
//					buf.append(str.substring(str.length() - 2));
//				}
//				if (i < byteVal.length - 1)
//					buf.append(flag);
//			}
//			return buf.toString().toUpperCase();
//		}
//
//		public static long parseSecBy2000(Calendar cal) {
//			long x = cal.getTimeInMillis();
//			cal.set(2000, 0, 1, 0, 0, 0);
//			long y = cal.getTimeInMillis();
//			return (x - y) / 1000L;
//		}
//
//		public static long parseLongByBytes(byte[] b) {
//			long ret = 0L;
//			for (int i = b.length - 1; i >= 0; i--) {
//				ret <<= 8L;
//				ret |= (b[i] & 0xFF);
//			}
//			return ret;
//		}
//
//		public static byte[] parseDegreeToBCD(float degree) {
//			long intVal = (long)(Math.abs(degree) * 1000.0F);
//			intVal = intVal / 60000L * 100000L + intVal % 60000L;
//			String s = String.valueOf(Math.abs(intVal));
//			if (s.length() % 2 != 0)
//				s = "0" + s;
//			byte[] bcd = new byte[s.length() / 2];
//			int i;
//			for (i = 0; i < s.length(); i += 2) {
//				int j = i / 2;
//				bcd[j] = (byte)(s.charAt(i) & 0xF);
//				bcd[j] = (byte)(bcd[j] << 4);
//				bcd[j] = (byte)(bcd[j] | (byte)(s.charAt(i + 1) & 0xF));
//			}
//			if (degree < 0.0F)
//				bcd[0] = (byte)(bcd[0] | 0x80);
//			return bcd;
//		}
//
//		public static byte[] parseStrToBCD(String s) {
//			if (s.length() % 2 != 0)
//				s = "0" + s;
//			byte[] bcd = new byte[s.length() / 2];
//			for (int i = 0; i < s.length(); i += 2) {
//				int j = i / 2;
//				bcd[j] = (byte)(s.charAt(i) & 0xF);
//				bcd[j] = (byte)(bcd[j] << 4);
//				bcd[j] = (byte)(bcd[j] | (byte)(s.charAt(i + 1) & 0xF));
//			}
//			return bcd;
//		}
//
//		public static String parseBCDToStr(byte[] bs) {
//			String ret = "";
//			for (int i = 0; i < bs.length; i++) {
//				byte tmp = (byte)(bs[i] >> 4 & 0xF);
//				ret = ret + String.valueOf(tmp);
//				tmp = (byte)(bs[i] & 0xF);
//				ret = ret + String.valueOf(tmp);
//			}
//			return ret;
//		}
//
//		public static float parseBCDToDegree(byte[] bs) {
//			byte minus = (byte)(bs[0] & 0x80);
//			bs[0] = (byte)(bs[0] & Byte.MAX_VALUE);
//			Float fl = new Float((((bs[0] >> 4 & 0xF) * 10 + (bs[0] & 0xF)) * 10 + (bs[1] >> 4 & 0xF)));
//			float f = fl.floatValue();
//			f = (float)(f + ((bs[1] & 0xF) * 10 + (bs[2] >> 4 & 0xF)) / 60.0D);
//			f += ((bs[2] & 0xF) * 100 + (bs[3] >> 4 & 0xF) * 10 + (bs[3] & 0xF)) / 60000.0F;
//			if (minus != 0)
//				f = -f;
//			return f;
//		}
//
//		public static byte[] parseDegreeToDDDMMSSS(float degree) {
//			Float fl = new Float(Math.abs(degree));
//			int d = fl.intValue();
//			int m = (int)((fl.floatValue() - d) * 60.0F);
//			int s = (int)(((fl.floatValue() - d) * 60.0F - m) * 1000.0F);
//			String temp = String.format("%03d%02d%03d", new Object[] { Integer.valueOf(d), Integer.valueOf(m), Integer.valueOf(s) });
//			byte[] ret = parseStrToBCD(temp);
//			if (degree < 0.0F)
//				ret[0] = (byte)(ret[0] | 0x80);
//			return ret;
//		}
//
//		public static byte[] parseLongToBytes(long l) {
//			byte[] b4 = new byte[4];
//			b4[0] = (byte)(int)(l & 0xFFL);
//			b4[1] = (byte)(int)(l >> 8L & 0xFFL);
//			b4[2] = (byte)(int)(l >> 16L & 0xFFL);
//			b4[3] = (byte)(int)(l >> 24L & 0xFFL);
//			return b4;
//		}
//
//		public static byte[] parse16bitToByte(int i) {
//			byte[] b2 = new byte[2];
//			b2[1] = (byte)(i & 0xFF);
//			b2[0] = (byte)(i >> 8 & 0xFF);
//			return b2;
//		}
//
//		public static byte[] parseIntToBytes(int i) {
//			byte[] b4 = new byte[4];
//			b4[0] = (byte)(i & 0xFF);
//			b4[1] = (byte)(i >> 8 & 0xFF);
//			b4[2] = (byte)(i >> 16 & 0xFF);
//			b4[3] = (byte)(i >> 24 & 0xFF);
//			return b4;
//		}
//
//		public static int parseIntByBytes(byte[] b) {
//			int ret = 0;
//			for (int i = b.length - 1; i >= 0; i--) {
//				ret <<= 8;
//				ret |= b[i] & 0xFF;
//			}
//			return ret;
//		}
//
//		public static byte[] parseCalTo6Bytes(Calendar calendar) {
//			byte[] bs = new byte[7];
//			bs[0] = (byte)calendar.get(13);
//			bs[1] = (byte)calendar.get(12);
//			bs[2] = (byte)calendar.get(11);
//			bs[3] = (byte)calendar.get(5);
//			bs[4] = (byte)calendar.get(2);
//			bs[5] = (byte)calendar.get(1);
//			return bs;
//		}
//
//		public static byte[] parseLongToByte4(long l) {
//			byte[] bs = new byte[4];
//			bs[0] = (byte)(int)(l >> 24L & 0xFFL);
//			bs[1] = (byte)(int)(l >> 16L & 0xFFL);
//			bs[2] = (byte)(int)(l >> 8L & 0xFFL);
//			bs[3] = (byte)(int)(l >> 0L & 0xFFL);
//			return bs;
//		}
//
//		private static byte uniteBytes(String src0, String src1) {
//			byte b0 = Byte.decode("0x" + src0).byteValue();
//			b0 = (byte)(b0 << 4);
//			byte b1 = Byte.decode("0x" + src1).byteValue();
//			byte ret = (byte)(b0 | b1);
//			return ret;
//		}
//
//		public static byte[] hexStr2Bytes(String src) {
//			int m = 0, n = 0;
//			int l = src.length() / 2;
//			byte[] ret = new byte[l];
//			for (int i = 0; i < l; i++) {
//				m = i * 2 + 1;
//				n = m + 1;
//				ret[i] = uniteBytes(src.substring(i * 2, m), src.substring(m, n));
//			}
//			return ret;
//		}
//
//		public static byte[] toByteArray(int iSource, int iArrayLen) {
//			byte[] bLocalArr = new byte[iArrayLen];
//			for (int i = 0; i < 4 && i < iArrayLen; i++)
//				bLocalArr[i] = (byte)(iSource >> 8 * i & 0xFF);
//			return bLocalArr;
//		}
//
//		public static int toInt(byte[] bRefArr, int start, int end) {
//			int iOutcome = 0;
//			for (int i = start; i < end; i++) {
//				byte bLoop = bRefArr[i];
//				iOutcome += (bLoop & 0xFF) << 8 * i;
//			}
//			return iOutcome;
//		}
//
//		public static void main(String[] args) throws Exception {
//			try {
//				String aa = new String(".".getBytes("ISO8859-1"), "utf-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//			byte[] data = changeIPStr("192.168.0.1");
//			System.out.println(format(data, " ", data.length));
//			String httpUrl = "http://b.map.icttic.cn:81/SE_RGC2?st=Rgc2&point=114.045739,22.562709&type=11&uid=gdxsk";
//			BufferedReader reader = readHttpDataToReader(httpUrl, "utf-8");
//			String s;
//			while ((s = reader.readLine()) != null)
//				System.out.println(s);
//		}
//
//
//	}
//
//	public static final class MSTool {
//		public static void encrypt(long M1, long IA1, long IC1, long key, byte[] buffer, int size) {
//			int idx = 0;
//			if (key == 0L)
//				key = 1L;
//			long mkey = M1;
//			if (0L == mkey)
//				mkey = 1L;
//			while (idx < size) {
//				key = IA1 * key % mkey + IC1 & 0xFFFFFFFFL;
//				buffer[idx++] = (byte)(buffer[idx++] ^ (byte)(int)(key >> 20L & 0xFFL));
//			}
//		}
//
//		public static void main222(String[] args) {
//			long M1 = 4210752250L;
//			long IA1 = 4160223223L;
//			long IC1 = 4126537205L;
//			long key = 6334L;
//			key = IA1 * key % M1 + IC1;
//			System.out.println("key=" + String.format("0x%x", new Object[] { Long.valueOf(key) }));
//		}
//
//		public static byte[] unescapeCharacter(byte[] bytes) {
//			int length = bytes.length;
//			IoBuffer ioBuffer = IoBuffer.allocate(length + 1000);
//			for (int i = 0; i < length; i++) {
//				if (bytes[i] == 90)
//					if (i + 1 != length) {
//						if (bytes[i + 1] == 1) {
//							ioBuffer.put((byte)91);
//							i++;
//							continue;
//						}
//						if (bytes[i + 1] == 2) {
//							ioBuffer.put((byte)90);
//							i++;
//							continue;
//						}
//					}
//				if (bytes[i] == 94)
//					if (i + 1 != length) {
//						if (bytes[i + 1] == 1) {
//							ioBuffer.put((byte)93);
//							i++;
//							continue;
//						}
//						if (bytes[i + 1] == 2) {
//							ioBuffer.put((byte)94);
//							i++;
//							continue;
//						}
//					}
//				ioBuffer.put(bytes[i]);
//				continue;
//			}
//			ioBuffer.flip();
//			byte[] byteArray = new byte[ioBuffer.remaining()];
//			ioBuffer.get(byteArray);
//			ioBuffer.clear();
//			return byteArray;
//		}
//
//		public static byte[] escapeCharacter(byte[] bytes) {
//			int length = bytes.length;
//			IoBuffer ioBuffer = IoBuffer.allocate(length + 1000);
//			ioBuffer.setAutoExpand(true);
//			for (int i = 0; i < length; i++) {
//				if (bytes[i] == 91) {
//					ioBuffer.put((byte)90);
//					ioBuffer.put((byte)1);
//				} else if (bytes[i] == 90) {
//					ioBuffer.put((byte)90);
//					ioBuffer.put((byte)2);
//				} else if (bytes[i] == 93) {
//					ioBuffer.put((byte)94);
//					ioBuffer.put((byte)1);
//				} else if (bytes[i] == 94) {
//					ioBuffer.put((byte)94);
//					ioBuffer.put((byte)2);
//				} else {
//					ioBuffer.put(bytes[i]);
//				}
//			}
//			ioBuffer.flip();
//			byte[] byteArray = new byte[ioBuffer.remaining()];
//			ioBuffer.get(byteArray);
//			ioBuffer.clear();
//			return byteArray;
//		}
//
//		public static int crc16CCITT(byte[] bytes) {
//			int crc = 65535;
//			int polynomial = 4129;
//			for (byte b : bytes) {
//				for (int i = 0; i < 8; i++) {
//					boolean bit = ((b >> 7 - i & 0x1) == 1);
//					boolean c15 = ((crc >> 15 & 0x1) == 1);
//					crc <<= 1;
//					if (c15 ^ bit)
//						crc ^= polynomial;
//				}
//			}
//			return crc & 0xFFFF;
//		}
//
//		public static int crc16CCITT2(byte[] bytes) {
//			String buf = new String(bytes);
//			int crc = 65535;
//			int polynomial = 4129;
//			for (int j = 0; j < buf.length(); j++) {
//				char b = buf.charAt(j);
//				for (int i = 0; i < 8; i++) {
//					boolean bit = ((b >> 7 - i & 0x1) == 1);
//					boolean c15 = ((crc >> 15 & 0x1) == 1);
//					crc <<= 1;
//					if (c15 ^ bit)
//						crc ^= polynomial;
//				}
//			}
//			crc &= 0xFFFF;
//			return crc;
//		}
//
//		public static Long crc32(byte[] bytes) {
//			CRC32 crc32 = new CRC32();
//			crc32.update(bytes);
//			return Long.valueOf(crc32.getValue());
//		}
//
//		public static int crc16(byte[] bytes) {
//			int[] table = {
//					0, 49345, 49537, 320, 49921, 960, 640, 49729, 50689, 1728,
//					1920, 51009, 1280, 50625, 50305, 1088, 52225, 3264, 3456, 52545,
//					3840, 53185, 52865, 3648, 2560, 51905, 52097, 2880, 51457, 2496,
//					2176, 51265, 55297, 6336, 6528, 55617, 6912, 56257, 55937, 6720,
//					7680, 57025, 57217, 8000, 56577, 7616, 7296, 56385, 5120, 54465,
//					54657, 5440, 55041, 6080, 5760, 54849, 53761, 4800, 4992, 54081,
//					4352, 53697, 53377, 4160, 61441, 12480, 12672, 61761, 13056, 62401,
//					62081, 12864, 13824, 63169, 63361, 14144, 62721, 13760, 13440, 62529,
//					15360, 64705, 64897, 15680, 65281, 16320, 16000, 65089, 64001, 15040,
//					15232, 64321, 14592, 63937, 63617, 14400, 10240, 59585, 59777, 10560,
//					60161, 11200, 10880, 59969, 60929, 11968, 12160, 61249, 11520, 60865,
//					60545, 11328, 58369, 9408, 9600, 58689, 9984, 59329, 59009, 9792,
//					8704, 58049, 58241, 9024, 57601, 8640, 8320, 57409, 40961, 24768,
//					24960, 41281, 25344, 41921, 41601, 25152, 26112, 42689, 42881, 26432,
//					42241, 26048, 25728, 42049, 27648, 44225, 44417, 27968, 44801, 28608,
//					28288, 44609, 43521, 27328, 27520, 43841, 26880, 43457, 43137, 26688,
//					30720, 47297, 47489, 31040, 47873, 31680, 31360, 47681, 48641, 32448,
//					32640, 48961, 32000, 48577, 48257, 31808, 46081, 29888, 30080, 46401,
//					30464, 47041, 46721, 30272, 29184, 45761, 45953, 29504, 45313, 29120,
//					28800, 45121, 20480, 37057, 37249, 20800, 37633, 21440, 21120, 37441,
//					38401, 22208, 22400, 38721, 21760, 38337, 38017, 21568, 39937, 23744,
//					23936, 40257, 24320, 40897, 40577, 24128, 23040, 39617, 39809, 23360,
//					39169, 22976, 22656, 38977, 34817, 18624, 18816, 35137, 19200, 35777,
//					35457, 19008, 19968, 36545, 36737, 20288, 36097, 19904, 19584, 35905,
//					17408, 33985, 34177, 17728, 34561, 18368, 18048, 34369, 33281, 17088,
//					17280, 33601, 16640, 33217, 32897, 16448 };
//			int crc = 0;
//			for (byte b : bytes)
//				crc = crc >>> 8 ^ table[(crc ^ b) & 0xFF];
//			return crc;
//		}
//
//		public static void main(String[] args) throws UnsupportedEncodingException {
//			byte[] d1 = new byte[32];
//			for (int i = 0; i < d1.length; i++)
//				d1[i] = (byte)i;
//			System.out.println("crc16CCITT(d1)==" + crc16CCITT(d1));
//			System.out.println("crc16CCITT(d2)==" + crc16CCITT2(d1));
//			String testString = "123456789";
//			byte[] bytes = testString.getBytes();
//			byte[] bss = new byte[bytes.length + 2];
//			System.arraycopy(bytes, 0, bss, 0, bytes.length);
//			bss[bytes.length] = 41;
//			bss[bytes.length + 1] = -79;
//			System.out.println("CRC16-CCITT = " + Integer.toHexString(crc16CCITT(bss)));
//			CRC32 crc32 = new CRC32();
//			crc32.update(bytes);
//			byte[] testBytes = {
//					91, 90, 93, 94, 90, 93, 94, 90, 93, 94,
//					94, 90, 93, 90, 93, 94 };
//			System.out.printf("testBytes:", new Object[] { "\t" });
//			for (byte b : testBytes) {
//				System.out.printf(Byte.toString(b) + " ", new Object[] { "\t" });
//			}
//			System.out.println("\n----------------------------");
//			byte[] newBytes = escapeCharacter(testBytes);
//			byte[] unBytes = unescapeCharacter(newBytes);
//			System.out.printf("newBytes:", new Object[] { "\t" });
//			for (byte b : newBytes) {
//				System.out.printf(Byte.toString(b) + " ", new Object[] { "\t" });
//			}
//			System.out.println("\n----------------------------");
//			System.out.printf("unBytes:", new Object[] { "\t" });
//			for (byte b : unBytes) {
//				System.out.printf(Byte.toString(b) + " ", new Object[] { "\t" });
//			}
//			System.out.println("\n----------------------------");
//			encrypt(111L, 111L, 111L, 551266522L, testBytes, 4);
//			for (byte b : testBytes) {
//				System.out.printf(Byte.toString(b) + " ", new Object[] { "\t" });
//			}
//			System.out.println("\n----------------------------");
//			encrypt(111L, 111L, 111L, 551266522L, testBytes, 4);
//			for (byte b : testBytes) {
//				System.out.printf(Byte.toString(b) + " ", new Object[] { "\t" });
//			}
//			byte[] bs = {
//					0, 0, 39, 16, 49, 50, 51, 52, 53, 54,
//					0, 0, 49, 57, 50, 46, 49, 54, 56, 46,
//					48, 46, 55, 53, 0, 0, 0, 0, 0, 0,
//					0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//					0, 0 };
//			long m1 = 4210752250L;
//			System.out.println(String.format("m1,0x%x", new Object[] { Long.valueOf(m1) }));
//			long ia1 = 4160223223L;
//			System.out.println(String.format("ia1,0x%6x", new Object[] { Long.valueOf(ia1) }));
//			long ic1 = 4126537205L;
//			System.out.println(String.format("ic1,0x%x", new Object[] { Long.valueOf(ic1) }));
//			System.out.println(String.format("l,0x%x", new Object[] { Integer.valueOf(bs.length) }));
//			encrypt(m1, ia1, ic1, 6334L, bs, bs.length);
//			System.out.println("\ndddddd::::" + NetUtil.format(bs, " ", bs.length));
//			encrypt(m1, ia1, ic1, 6334L, bs, bs.length);
//			System.out.println("\ndddddd::::" + NetUtil.format(bs, " ", bs.length));
//			byte[] ttt = {
//					0, 0, 0, 72, 0, 0, 0, 1, 16, 1,
//					0, 3, 91, 96, 1, 2, 15, 0, 0, 0,
//					0, 0, 0, 1, -121, 14, 49, 50, 51, 52,
//					53, 54, 55, 56, 49, 57, 50, 46, 49, 54,
//					56, 46, 48, 46, 50, 0, 0, 0, 0, 0,
//					0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//					0, 0, 0, 0, 0, 0, 4, 0 };
//			int d = crc16(ttt);
//			System.out.println(String.format("%x", new Object[] { Integer.valueOf(d) }));
//		}
//
//		public static Calendar transCalendarByUTCddMMyyhhmmss(byte[] date, byte[] time) {
//			byte d = date[0];
//			byte M = date[1];
//			int y = ((date[2] & 0xFF) << 8 | date[3] & 0xFF) & 0xFFFF;
//			byte h = time[0];
//			byte m = time[1];
//			byte s = time[2];
//			Calendar calendar = Calendar.getInstance();
//			calendar.set(y, M - 1, d, h, m, s);
//			return calendar;
//		}
//
//	}
//}
