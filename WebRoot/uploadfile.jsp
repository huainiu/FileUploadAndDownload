<%@ page contentType="text/html;charset=gb2312"%>
<html>
	<head>
		<title>�ϴ��ļ�</title>
		<link type="text/css" rel="stylesheet" href="css/style.css">
	</head>
	<body bgcolor="#E1E1E1">
		<form action="upload" enctype="multipart/form-data" name="uploadform"
			method="post">
			<center>
				<table border="1" bordercolor="#BFD3E1" cellpadding="0"
					cellspacing="0" rules="cols">
					<tr>
						<td colspan="2">
							<img src="images/top.jpg" width="833">
						</td>
					<tr>
					<tr height="30" bgcolor="black">
						<td colspan="2" style="text-indent: 20" align="right">
							<a style="color: white" href="index.jsp">��ҳ</a>&nbsp;&nbsp;&nbsp;&nbsp;
							<a style="color: white" href="downloadview">�����ļ�</a>&nbsp;&nbsp;
						</td>
					</tr>

					<tr>
						<td width="22%" align="center" valign="top" bgcolor="#F7FAF6"
							style="padding-top: 8">
							<div
								style="background: #177B6C; width: 94%; height: 25; color: white">
								<b>ע�����</b>
							</div>
							���ļ������������Ϊ50�ף�
						</td>
						<td valign="top" style="padding-left: 30; padding-top: 10"
							bgcolor="white">
							<img src="images/title.jpg">
							<br>
							<br>
							�ļ�·��1��
							<br>
							<input type="file" name="file1" size="60">
							<br>
							�ļ�����1��
							<br>
							<textarea rows="5" cols="69" name="fileinfo1"></textarea>
							<br>
							<hr color="black">
							�ļ�·��2��
							<br>
							<input type="file" name="file2" size="60">
							<br>
							�ļ�����2��
							<br>
							<textarea rows="5" cols="69" name="fileinfo2"></textarea>
							<br>
							<input type="button"
								style="border: 0; background: url(images/submit.jpg); width: 74; height: 21"
								value="" onClick="uploadform.submit()">
							<input type="reset"
								style="border: 0; background: url(images/reset.jpg); width: 74; height: 21"
								value="">
							<br>
							<br>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<img src="images/end.jpg" width="833">
						</td>
					<tr>
				</table>
			</center>
		</form>
	</body>
</html>