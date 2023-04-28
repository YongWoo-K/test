<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model1.BoardTO"%>
<%@ page import="model1.BoardDAO"%>
<%
request.setCharacterEncoding("UTF-8");

String cpage = request.getParameter("cpage");
String seq = request.getParameter("seq");

BoardTO to = new BoardTO();
BoardDAO dao = new BoardDAO();
to.setSeq(seq);
to = dao.boardModify(to);

String subject = to.getSubject();
String writer = to.getWriter();
String content = to.getContent();
String mail[] = null;
if(to.getMail().equals("")){
	mail = new String[]{"",""};
}
else{
	mail = to.getMail().split("@");
}
String filename = to.getFilename();

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css">
<script src="js/modify.js"></script>
<title></title>
</head>
<body>
<div class="container">
   <div class="row">
      <div class="col-12">
      
      	<ul class="breadcrumb my-3">
	      	<li class="breadcrumb-item"><img src="img/home.svg" alt="홈아이콘"/> &gt;</li>
	      	<li class="breadcrumb-item">게시판 &gt;</li>
	      	<li class="breadcrumb-item active">update</li>
      	</ul>
      	
      	<form action="board_modify_ok.jsp" method="post" name="mfrm" enctype="multipart/form-data">
      		<input type="hidden" name="seq" value="<%=seq%>">
      		<input type="hidden" name="cpage" value="<%=cpage%>">
      		
      		<table class="table">
      			<tr>
      				<th>작성자</th>
      				<td>
      					<input 
      					type="text" 
      					name="writer"
      					value="<%=writer%>"
      					class="form-control"
      					maxlength="5"
      					/>
      				</td>
      			</tr>
      			
      			<tr>
      				<th>제목</th>
      				<td>
      					<input 
      					type="text" 
      					name="subject"
      					value="<%=subject%>"
      					class="form-control"
      					maxlength="20"
      					/>
      				</td>
      			</tr>
      			
      			<tr>
      				<th>비밀번호</th>
      				<td>
      					<input 
      					type="password" 
      					name="password"
      					value=""
      					class="form-control"
      					/>
      				</td>
      			</tr>
      			
      			<tr>
      				<th>내용</th>
      				<td>
      					<textarea name="content" class="form-control" rows="10" maxlength="2048"><%=content%></textarea>
      				</td>
      			</tr>
      			
      			<tr>
      				<th>파일첨부</th>
      				<td>
      					<h3>기존파일 : <%=filename%>></h3>
      					<input type="file" name="upload" value="">
      				</td>
      			</tr>
      			
      			<tr>
      				<th>이메일</th>
      				<td>
      					<div class="input-group align-items-center">
      						<input type="text" name="mail1" value="<%=mail[0]%>" class="form-control"/>
      						&nbsp;@&nbsp;
      						<input type="text" name="mail2" value="<%=mail[1]%>" class="form-control"/>
      					</div>
      				</td>
      			</tr>
      		</table>
      		
      		<div class="d-flex justify-content-end">
      			<div class="btn-group">
      				<input type="button" value="목록" class="btn btn-primary" onclick="location.href='board_list.jsp?cpage=<%=cpage%>'"/>
      				<input type="button" value="글 확인" class="btn btn-secondary" onclick="location.href='board_view.jsp?cpage=<%=cpage%>&seq=<%=seq%>'"/>
      				<input type="submit" id="submit1" value="수정" class="btn btn-success"/>
      			</div>
      		</div>
      	</form>
      
      </div>
   </div>
</div>

</body>
</html>