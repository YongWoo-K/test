<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model1.BoardTO"%>
<%@ page import="model1.BoardDAO"%>
<%
request.setCharacterEncoding("UTF-8");

String cpage = request.getParameter("cpage");
String seq = request.getParameter("seq");

String subject = "";
String writer = "";

BoardTO to = new BoardTO();
BoardDAO dao = new BoardDAO();

to.setSeq(seq);
to.setSubject(subject);
to.setWriter(writer);

to = dao.boardDelete(to);

subject = to.getSubject();
writer = to.getWriter();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css">
<script src="js/delete.js"></script>
<title>Board Delete</title>
</head>
<body>
<div class="container">
   <div class="row">
      <div class="col-12">
      
      	<ul class="breadcrumb my-3">
	      	<li class="breadcrumb-item"><img src="img/home.svg" alt="홈아이콘"/> &gt;</li>
	      	<li class="breadcrumb-item">게시판 &gt;</li>
	      	<li class="breadcrumb-item active">Delete</li>
      	</ul>
      	
      	<form action="board_delete_ok.jsp" method="post" name="dfrm">
      		<input type="hidden" name="seq" value="<%=seq%>">
      		
      		<table class="table">
      			<tr>
      				<th>작성자</th>
      				<td>
      					<input type="text" name="writer" value="<%=writer%>" class="form-control">
      				</td>
      				<th>제목</th>
      				<td>
      					<input type="text" name="subject" value="<%=subject%>" class="form-control">
      				</td>
      				<th>비밀번호</th>
      				<td>
      					<input type="password" name="password" value="" class="form-control">
      				</td>
      			</tr>
      		</table>
      		
      		<div class="btn-group">
      			<input type="button" value="목록" class="btn btn-primary" onclick="location.href='board_list.jsp?cpage=<%=cpage%>'"/>
      			<input type="button" value="글 확인" class="btn btn-success" onclick="location.href='board_view.jsp?cpage=<%=cpage%>&seq=<%=seq%>'"/>
      			<input type="submit" id="submit1" value="삭제" class="btn btn-danger"/>
      		</div>	
      	</form>
      
      </div>
   </div>
</div>

</body>
</html>