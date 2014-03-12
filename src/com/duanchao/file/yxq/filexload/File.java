package com.duanchao.file.yxq.filexload;

import java.io.FileOutputStream;

public class File {
	private FileXLoad parent; // ���ø�������Ҫ��Ϊ����File��ʵ���з���FileXLoadʵ���е�filecontent_b����ֵ
	private String fieldName; // �ļ��ֶ���
	private String filePath; // �ļ�·���������ļ�����
	private String fileName; // �ļ�����
	private String fileExt; // �ļ���չ����������.���ַ�
	private String fileType; // �ļ�����
	private int fileSize; // �ļ���С
	private int start; // �ļ����ݵ���ʼλ��
	private int end; // �ļ����ݵĽ���λ��
	private boolean available; // �Ƿ�Ϊ��Ч�ļ��ı�ʶ

	public File() {
		parent = null;
		filePath = "";
		fileName = "";
		fileExt = "";
		fileType = "";
		fileSize = 0;
		start = 0;
		end = 0;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileExt() {
		return fileExt;
	}

	public String getFileType() {
		return fileType;
	}

	public int getFileSize() {
		return fileSize;
	}

	protected int getStart() {
		return start;
	}

	protected int getEnd() {
		return end;
	}

	public boolean isAvailable() {
		return available;
	}

	protected void setParent(FileXLoad parent) {
		this.parent = parent;
	}

	protected void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	protected void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	protected void setFileName(String fileName) {
		this.fileName = fileName;
	}

	protected void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	protected void setFileType(String fileType) {
		this.fileType = fileType;
	}

	protected void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	protected void setStart(int start) {
		this.start = start;
	}

	protected void setEnd(int end) {
		this.end = end;
	}

	protected void setAvailable(boolean available) {
		this.available = available;
	}

	/**
	 * �����ļ���������
	 * 
	 * @param filedir
	 *            :���浽�����ϵ�·��
	 * @param savename�����浽�����ϵ��ļ���
	 * @return
	 */
	public boolean saveFileToDisk(String filedir, String savename) {
		boolean mark = false;
		FileOutputStream fos = null; // �ļ������
		try {
			java.io.File upfile = new java.io.File(filedir, savename);
			fos = new FileOutputStream(upfile);
			fos.write(parent.filecontent_b, start, (end - start) + 1);
			fos.close();
			mark = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mark;
	}
}
