<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model1.BoardTO"%>
<%@ page import="model1.BoardDAO"%>
<%@ page import="java.io.File"%>

<%
//물리적 업로드 경로
String uploadPath = "D:/dev_kyw/jsp_lesson/photo/src/main/webapp/upload";

//첨부파일 용량 제한 - 20mb
int maxFileSize = 1024 * 1024 * 20;

//인코딩 타입
String encType = "UTF-8";

//MultipartRequest의 객체 생성
MultipartRequest multi = new MultipartRequest(request, uploadPath, maxFileSize, encType, new DefaultFileRenamePolicy());
//DefaultFileRenamePolicy() : 동일한 파일명이 존재한다면 파일명 뒤에 숫자를 붙이는 클래스 - 예시) text.jpg/text1.jpg/text2.jpg

String seq = multi.getParameter("seq");
String cpage = multi.getParameter("cpage");
String password = multi.getParameter("password");
String subject = multi.getParameter("subject");
String content = multi.getParameter("content");

//필수 입력 항목이 아닌 항목들을 아래와 같이 검사 후 저장
String mail = "";
if(multi.getParameter("mail1") != null && multi.getParameter("mail2") != null){
	mail = multi.getParameter("mail1") + "@" + multi.getParameter("mail2");
}

String newFilename = multi.getFilesystemName("upload");
File file = multi.getFile("upload");

BoardTO to = new BoardTO();
BoardDAO dao = new BoardDAO();

to.setSeq(seq);
to.setPassword(password);
to.setSubject(subject);
to.setContent(content);
to.setMail(mail);
to.setFilename(newFilename);

int flag = dao.boardModifyOK(to);

out.println("<script>");
if(flag == 0){
	out.println("alert('글 수정에 성공했습니다.');");
	out.println("location.href='board_list.jsp?sep="+seq+"&cpage="+cpage+"'");
}
else if(flag == 1){
	out.println("alert('비밀번호가 틀립니다. 다시 입력해주세요.');");
	out.println("history.back()");
}
else{
	out.println("alert('글 수정에 실패했습니다.');");
	out.println("history.back()");
}
out.println("</script>");

%>