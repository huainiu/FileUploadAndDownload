package com.duanchao.file.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.duanchao.file.dao.FileDao;
import com.duanchao.file.toolsbean.StringHandler;
import com.duanchao.file.valuebean.FileBean;
import com.duanchao.file.yxq.filexload.File;
import com.duanchao.file.yxq.filexload.FileXLoad;
import com.duanchao.file.yxq.filexload.Parameters;

@SuppressWarnings("serial")
public class MyFileServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// ��ȡ����ʽ
		String servletPath = request.getServletPath();
		if ("/upload".equals(servletPath))
			upload(request, response);
		else if ("/downloadview".equals(servletPath))
			downloadview(request, response);
		else if ("/downloadrun".equals(servletPath))
			downloadrun(request, response);
	}

	/**
	 * �ļ��ϴ�
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void upload(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String message = "";
		int maxlen = 1024 * 1024 * 50;  //�ļ������Ϊ 50MB
		String filedir = getServletContext().getRealPath("/files"); // �ļ����浽�����ϵ�·��
		System.out.println("�ļ����浽�����ϵ�·��filedir--" + filedir);
		FileXLoad myxload = new FileXLoad(); // ʵ����FileXLoad
		myxload.init(request, response, filedir);
		myxload.setMaxlen(maxlen); // �����ļ����Ĵ�С
		boolean start = myxload.upload(); // �����ļ��ϴ��ķ���

		if (start) {
			List files = myxload.getFiles();
			//�ж��ļ��Ƿ����
			if (files != null && files.size() != 0) {
				Date date = new Date();
				FileDao fileDao = new FileDao();
				Parameters params = myxload.getParams();
				for (int i = 0; i < files.size(); i++) {
					File file = (File) files.get(i);
					//�ж��ļ��Ƿ���Ч
					if (file.isAvailable()) {
						String filepath = file.getFilePath(); // ��ȡ�ļ�·��
						String fileinfo = params.getFieldValue("fileinfo"
								+ (i + 1)); // ��ȡ�ļ�����
						
						// ��ȡ�ļ����浽���ݿ������
						// file.getFileExt():�ļ���չ��������"."�ַ�
						String savename = StringHandler.getSerial(date, i)
								+ file.getFileExt();
						System.out.println(savename + "---savename---"
								+ file.getFileExt());
						String filename = file.getFileName(); // ��ȡ�ϴ��ļ�������
						String filetype = file.getFileType(); // ��ȡ�ϴ��ļ�������
						int filesize = file.getFileSize(); // ��ȡ�ϴ��ļ��Ĵ�С
						String uptime = StringHandler.timeTostr(date); // ��ȡ�ļ��ϴ���ʱ��

						FileBean filebean = new FileBean();
						filebean.setFilePath(filepath);
						filebean.setFileName(filename);
						filebean.setSaveName(savename);
						filebean.setFileType(filetype);
						filebean.setFileSize(filesize);
						filebean.setFileInfo(fileinfo);
						filebean.setUptime(uptime);

						int k = fileDao.addFileInfo(filebean); // �����ļ���Ϣ�����ݿ���
						if (k <= 0)
							message += "�� �ļ� <b><font color='red'>"
									+ file.getFilePath()
									+ "</font></b> �ϴ�ʧ�ܣ�<br>";
						else {
							//filedir�����浽�����ϵ�·��
							//savename�����浽�����ϵ��ļ���
							boolean savetodisk = file.saveFileToDisk(filedir,
									savename); // �����ļ���������
							if (savetodisk)
								message += "�� �ļ� <b><font color='red'>"
										+ file.getFilePath()
										+ "</font></b> �ϴ��ɹ���<br>";
							else
								message += "�� �ļ� <b><font color='red'>"
										+ file.getFilePath()
										+ "</font></b> �ϴ�ʧ�ܣ�<br>";
						}
					} else
						message += "�� �ļ� <b><font color='red'>"
								+ file.getFilePath()
								+ "</font></b> ��Ч���СΪ0��<br>";
				}
				fileDao.closed();
			} else
				message += "�� ������ѡ��һ��Ҫ�ϴ����ļ�������ѡ����ļ���СΪ0��<br>";
		} else
			message = myxload.getMessage();

		message += "<br>>> <a href='uploadfile.jsp'>����</a>";
		message += "<br>>> <a href='index.jsp'>��ҳ</a>";

		request.setAttribute("message", message);
		RequestDispatcher rd = request.getRequestDispatcher("/message.jsp");
		rd.forward(request, response);
	}

	/**
	 * ��ʾ���е��ļ���Ϣ
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void downloadview(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		FileDao fileDao = new FileDao();
		try {
			List filelist = fileDao.getFileList();
			request.setAttribute("filelist", filelist); // �������е��ļ�������Ϣ
		} catch (SQLException e) {
			e.printStackTrace();
		}

		RequestDispatcher rd = request
				.getRequestDispatcher("/downloadfile.jsp");
		rd.forward(request, response);
	}

	/**
	 * �ļ�����
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void downloadrun(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		FileBean file = null;
		try {
			String filedir = getServletContext().getRealPath("/files"); // ��ȡ�ļ����ڵĴ���·��
			System.out.println(filedir + "---��ȡ�ļ����ڵĴ���·��");
			String downfilename = request.getParameter("downname"); // ��ȡ�����ڴ����ϵ��ļ���
			System.out.println(downfilename + "---downfilename");

			FileDao fileDao = new FileDao();
			file = fileDao.getFileSingle(downfilename);
			fileDao.closed();
			if (file != null) {
				String filename = new String(file.getFileName().getBytes(
						"gb2312"), "ISO-8859-1"); // ���д�������������ص��ļ���������
				FileXLoad myxload = new FileXLoad();
				myxload.init(request, response, filedir);
				myxload.download(downfilename, filename, file.getFileType());
			}
		} catch (Exception e) {
			e.printStackTrace();
			String message = "�� ����ʧ�ܣ��ļ� <b><font color='red'>"
					+ file.getFileName() + "</font></b> �����ڻ��Ѿ���ɾ����<br>";
			message += "<a href='javascript:window.history.go(-1)'>����</a>";
			request.setAttribute("message", message);
			RequestDispatcher rd = request.getRequestDispatcher("/message.jsp");
			rd.forward(request, response);
		}
	}
}
