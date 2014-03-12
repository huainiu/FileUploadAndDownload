package com.duanchao.file.yxq.filexload;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileXLoad {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Parameters params;

	private String message;
	protected byte[] filecontent_b;
	private int maxlen;
	private int current_pos;
	private String filedir;

	public FileXLoad() {
		message = "";
		maxlen = 0;
		current_pos = 0;
		filedir = "";
		params = new Parameters();
	}

	public void setMaxlen(int maxlen) {
		this.maxlen = maxlen;
	}

	public void init(HttpServletRequest request, HttpServletResponse response,
			String filedir) {
		this.request = request;
		this.response = response;
		this.filedir = filedir;
	}

	/**
	 * �ļ��ϴ��ķ���
	 * 
	 * @return
	 */
	public boolean upload() {
		/** �������������ϴ��ļ���Ŀ¼ */
		java.io.File dir = new java.io.File(filedir);
		if (!dir.exists())
			dir.mkdir();

		boolean mark = false;
		String contentType = request.getContentType(); // ��ȡ�ļ���ʽ
		System.out.println(contentType + "----��ȡ�ļ���ʽcontentType");
		if (contentType.indexOf("multipart/form-data", 0) != -1) {
			int len = request.getContentLength(); // �ļ��Ĵ�С
			if (len > maxlen)
				message = "�� �ϴ����ļ��ܳ��������Ϊ50��!";
			else {
				/** ���ļ������ݶ����ֽ�����filecontent_b�� */
				try {
					DataInputStream in = new DataInputStream(request
							.getInputStream());
					filecontent_b = new byte[len];
					int readbyte = 0;
					int totalreadbyte = 0;
					while (totalreadbyte < len) {
						readbyte = in.read(filecontent_b, totalreadbyte, len);
						totalreadbyte += readbyte;
					}
					in.close();
					mark = true;
				} catch (IOException e) {
					e.printStackTrace();
				}

				/** ���ļ�������д����ʱ�ļ�temp.txt�С���ʵ�ʿ����в���Ҫ���ɸ��ļ������δ�����Ϊ�˷�����߲鿴ͨ���ļ����ύ�����ݵĸ�ʽ */
				try {
					java.io.File tempfile = new java.io.File(filedir,
							"temp.txt");
					FileOutputStream tempfile_stream = new FileOutputStream(
							tempfile);
					tempfile_stream.write(filecontent_b);
					tempfile_stream.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else
			message = "�� �ϴ����ļ����Ͳ��� multipart/form-data ��ʽ��<br>";
		return mark;
	}

	@SuppressWarnings("unchecked")
	public List getFiles() {
		List files = new ArrayList();
		int index = 0;
		while (current_pos < filecontent_b.length) {
			File single = getFile(index);
			if (single != null)
				files.add(single);
			index++;
		}
		return files;
	}

	private File getFile(int index) {
		File file = null;

		// ��ȡ�ֽ��ַ���
		String bound = getBound();

		if (!bound.substring(bound.length() - 2).equals("--")) {
			// ��ȡ����������
			String dataHeader = getDateHeader();

			// ����Ҫ��ȡ���ݵĿ�ʼλ�úͽ���λ��:pos[0]Ϊ��ʼλ�ã�pos[1]Ϊ����λ��
			int[] pos = getDataContentSegment(bound);

			// ��ȡ�ֶ�����
			String fieldName = getFieldNameValue(dataHeader, "name=");

			if (dataHeader.indexOf("filename=") > 0) {
				file = new File();
				// ��ȡ�ϴ����ļ�ȫ��
				String filePath = getFieldNameValue(dataHeader, "filename=");

				// ��ȡ�ϴ����ļ�ʵ������
				String fileName = getFileName(filePath);

				// ��ȡ�ļ���׺��
				String fileExt = getFileExt(fileName);

				// ��ȡ�ļ�����
				String fileType = getFileType(dataHeader);

				// ��ȡ�ļ���С
				int fileSize = (pos[1] - pos[0]) + 1;
				if (fileSize <= 0) {
					file.setAvailable(false);
					file.setFilePath(filePath);
				} else {
					file.setAvailable(true);
					file.setParent(this);
					file.setFieldName(fieldName);
					file.setFilePath(filePath);
					file.setFileName(fileName);
					file.setFileExt(fileExt);
					file.setFileType(fileType);
					file.setFileSize(fileSize);
					file.setStart(pos[0]);
					file.setEnd(pos[1]);
				}
			} else { // ��ȡ�����ֶ�����
				String fieldNameContent = new String(filecontent_b, pos[0],
						(pos[1] - pos[0]) + 1);
				params.setFields(fieldName, fieldNameContent);
			}
		} else
			current_pos = filecontent_b.length + 1;
		return file;
	}

	/** @���ܣ���ȡ�ֽ��ַ��� */
	private String getBound() {
		String bound = "";
		for (; filecontent_b[current_pos] != 13; current_pos++)
			bound += (char) filecontent_b[current_pos];
		current_pos += 2;
		return bound;
	}

	/** @���ܣ���ȡ���������� */
	private String getDateHeader() {
		String dateHeader = "";

		int start = current_pos;
		int end = 0;
		for (; !(filecontent_b[current_pos] == 13 && filecontent_b[current_pos + 2] == 13); current_pos++)
			;
		end = current_pos - 1;
		current_pos += 4;

		dateHeader = new String(filecontent_b, start, (end - start) + 1);
		return dateHeader;
	}

	/** @���ܣ���ȡ�ֶ����� */
	private String getFieldNameValue(String dataHeader, String fieldName) {
		int start = dataHeader.indexOf(fieldName) + fieldName.length() + 1;
		int end = dataHeader.indexOf("\"", start);
		String value = dataHeader.substring(start, end);
		return value;
	}

	/** @���ܣ���ȡ�ļ��� */
	private String getFileName(String filePath) {
		String filename = "";
		int start = filePath.lastIndexOf("\\");
		if (start == -1)
			start = filePath.lastIndexOf("/");
		if (start > 0)
			filename = filePath.substring(start + 1);
		else
			filename = filePath;
		return filename;
	}

	/** @���ܣ���ȡ�ļ���׺�� */
	private String getFileExt(String fileName) {
		String fileext = "";
		if (fileName != null && !fileName.equals("")) {
			int start = fileName.lastIndexOf(".");
			if (start > 0)
				fileext = fileName.substring(start);
		}
		return fileext;
	}

	/** @���ܣ���ȡ�ļ����� */
	private String getFileType(String dataHeader) {
		int start = dataHeader.indexOf("Content-Type: ") + 14;
		String filetype = dataHeader.substring(start);
		return filetype;
	}

	/** @���ܣ�����Ҫ��ȡ����(�ֽ�������ʽ)����ʼλ�úͽ���λ�� */
	private int[] getDataContentSegment(String bound) {
		int[] pos = new int[2];
		pos[0] = current_pos; // Ҫ��ȡ���ݵ���ʼλ��
		pos[1] = getDataContentEnd(bound); // Ҫ��ȡ���ݵĽ���λ��
		return pos;
	}

	/** @���ܣ���ȡ�ļ����ݵĽ���λ�� */
	private int getDataContentEnd(String bound) {
		int end = -1;
		boolean found = false;
		int key = 0;
		byte[] bound_b = bound.getBytes();
		while (!found && current_pos < filecontent_b.length) {
			if (filecontent_b[current_pos] == bound_b[key]) {
				if (key == bound_b.length - 1)
					found = true;
				else {
					key++;
					current_pos++;
				}
			} else {
				key = 0;
				current_pos++;
			}
		}
		if (found) {
			current_pos = current_pos - (bound_b.length - 1);
			end = current_pos - 3;
		}
		return end;
	}

	public String getMessage() {
		return this.message;
	}

	public Parameters getParams() {
		return this.params;
	}

	/** @���ܣ������ļ� */
	public void download(String downfilename, String filename, String filetype)
			throws IOException {
		java.io.File file = new java.io.File(filedir, downfilename);
		long len = file.length();
		byte a[] = new byte[600];

		FileInputStream fin = new FileInputStream(file);
		OutputStream outs = response.getOutputStream();

		response.setHeader("Content-Disposition", "attachment; filename="
				+ filename);
		response.setContentType(filetype);
		response.setHeader("Content_Lenght", String.valueOf(len));

		int read = 0;
		while ((read = fin.read(a)) != -1) {
			outs.write(a, 0, read);
		}
		outs.close();
		fin.close();
	}
}