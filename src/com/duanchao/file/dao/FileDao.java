package com.duanchao.file.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.duanchao.file.toolsbean.DB;
import com.duanchao.file.toolsbean.StringHandler;
import com.duanchao.file.valuebean.FileBean;

public class FileDao {
	private DB mydb = null;

	public FileDao() {
		mydb = new DB();
	}

	/**
	 * �ϴ��ļ������ݿ�
	 * @param file
	 * @return
	 */
	public int addFileInfo(FileBean file) {
		int i = -1;
		String sql = "insert into tb_file values(?,?,?,?,?,?)";
		// SaveName���ļ����浽�������õ�������
		// FileName���ļ�����ʵ����
		// FileType���ļ�����
		// FileSize���ļ���С
		// FileInfo���ļ�����
		// Uptime���ϴ�ʱ��
		Object[] params = { file.getSaveName(), file.getFileName(),
				file.getFileType(), file.getFileSize(), file.getFileInfo(),
				file.getUptime() };
		mydb.doPstm(sql, params);
		try {
			i = mydb.getCount();
		} catch (SQLException e) {
			i = -1;
			e.printStackTrace();
		}
		return i;
	}

	/**
	 * ��ѯ����Ҫ���ص��ļ�
	 * @param savename
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public FileBean getFileSingle(String savename) throws SQLException {
		FileBean single = null;
		String sql = "select * from tb_file where file_savename=?";
		Object[] params = { savename };
		List list = getList(sql, params);
		if (list != null && list.size() != 0)
			single = (FileBean) list.get(0);
		return single;
	}

	/**
	 * ��ѯ�����ϴ����ļ�
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public List getFileList() throws SQLException {
		String sql = "select * from tb_file order by file_savename desc";
		List list = getList(sql, null);
		return list;
	}

	/**
	 * ��ȡ�ϴ����ļ�����
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	private List getList(String sql, Object[] params) throws SQLException {
		List list = null;
		mydb.doPstm(sql, params);
		ResultSet rs = mydb.getRs();
		if (rs != null) {
			list = new ArrayList();
			while (rs.next()) {
				FileBean single = new FileBean();
				single.setId(rs.getInt(1));
				single.setSaveName(rs.getString(2));
				single.setFileName(rs.getString(3));
				single.setFileType(rs.getString(4));
				single.setFileSize(rs.getInt(5));
				single.setFileInfo(rs.getString(6));
				single.setUptime(StringHandler.timeTostr(rs.getTimestamp(7)));
				list.add(single);
			}
			rs.close();
		}
		return list;
	}

	public void closed() {
		mydb.closed();
	}
}
