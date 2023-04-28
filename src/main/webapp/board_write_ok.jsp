<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model1.BoardTO"%>
<%@ page import="model1.BoardListTO"%>
<%@ page import="model1.BoardDAO"%>
<%@ page import="java.io.File"%>

<%
//cpage값을 요청
String cpage = request.getParameter("cpage");

//파일이 업로드되는 물리적 경로 설정
String uploadPath = "D:\\dev_kyw\\jsp_lesson\\photo\\src\\main\\webapp\\upload";

//첨부파일의 용량을 제한 - 20MB
int maxFileSize = 1024 * 1024 * 20;

//인코딩 타입 설정 - UTF-8
String encType = "UTF-8";

//객체 생성
MultipartRequest multi = new MultipartRequest(request, uploadPath, maxFileSize, encType, new DefaultFileRenamePolicy());
//DefaultFileRenamePolicy() : 동일한 파일명이 존재한다면 파일명 뒤에 숫자를 붙이는 클래스 - 예시) text.jpg/text1.jpg/text2.jpg

String subject = multi.getParameter("subject");
String writer = multi.getParameter("writer");

//필수 입력 항목이 아닌 항목들을 아래와 같이 검사 후 저장
String mail = "";
if(!multi.getParameter("mail1").equals("") && !multi.getParameter("mail2").equals("")){
	mail = multi.getParameter("mail1") + "@" + multi.getParameter("mail2");
}

String password = multi.getParameter("password");
String content = multi.getParameter("content");
String wip = request.getRemoteAddr(); //자바에서 클라이언트의 ip주소를 얻기위해 사용하는 코드

String filename = multi.getFilesystemName("upload");
File file = multi.getFile("upload");

//파일사이즈를 체크하기 위해
long filesize = 0;
if(file != null){
	filesize = file.length();
}

BoardTO to = new BoardTO();
BoardDAO dao = new BoardDAO();

to.setSubject(subject);
to.setWriter(writer);
to.setMail(mail);
to.setPassword(password);
to.setContent(content);
to.setWip(wip);
to.setFilename(filename);
to.setFilesize(filesize);

int flag = dao.boardWriteOK(to);

out.println("<script>");
if(flag == 0){
	out.println("alert('글쓰기에 성공했습니다.');");
	out.println("location.href='board_list.jsp'");
}
else{
	out.println("alert('글쓰기에 실패했습니다.');");
	out.println("history.back()");
}
out.println("</script>");
%>

<!-- 
- enctype 속성값 목록
	- multipart/from-data : 파일 업로드시 사용(인코딩 하지 않음)
	- application x-www-form url encoded : 디폴트 값으로 모든 문자를 인코딩
	- text/plain : 공백을 +기호로 변환함(특수문자는 인코딩하지 않음)

- maxFileSize : 파일 당 최대 파일 크기를 설정(제한없음)
- maxRequestSize : 파일 1개의 용량이 아니라 multipart/from-data 요청당 최대 파일 크기를 설정
				   (여러개의 파일 업로드시 총 크기로 보면 됨)
- fileSizeThreshold : 업로드하는 파일이 임시로 저장되지 않고 메모리에서 바로 스트림으로 전달되는 크기의 한계를 설정
-->