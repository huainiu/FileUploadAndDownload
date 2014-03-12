package com.duanchao.file.toolsbean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringHandler {
	public static String timeTostr(Date date) {
		String strDate = "";
		if (date != null) {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			strDate = format.format(date);
		}
		return strDate;
	}

	/**
	 * ʱ���ʽת��
	 * @param date
	 * @param index
	 * @return
	 */
	public static String getSerial(Date date, int index) {
		long msel = date.getTime(); // ��ȡ������
		SimpleDateFormat fm = new SimpleDateFormat("MMddyyyyHHmmssSS"); // ����ת������
		msel += index; // ͨ��һ��ֵ�ı������
		date.setTime(msel); // ͨ���ı��ĺ�����������������
		String serials = fm.format(date); // ת������ʱ��������Ϊ��MMddyyyyHHmmssSS����ʽ
		return serials;
	}

	public static String changehtml(String str) {
		String change = "";
		if (str != null && !str.equals("")) {
			change = str.replace("&", "&amp;");
			change = change.replace(" ", "&nbsp;");
			change = change.replace("<", "&lt;");
			change = change.replace(">", "&gt;");
			change = change.replace("\"", "&quot;");
			change = change.replace("\r\n", "<br>");
		}
		return change;
	}
}
