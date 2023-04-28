<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model1.BoardTO"%>
<%@ page import="model1.BoardListTO"%>
<%@ page import="model1.BoardDAO"%>
<%@ page import="java.util.ArrayList"%>
<%--
- 컴포넌트
	- 재사용가능한 웹의 구성요소라는 뜻으로 웹 응용프로그램에서 재사용가능한 구성요소를 만들 수 있게 해주는
	  일련의 표준기반 웹 플랫폼 API세트
	  
	- react. vue에서 많이 사용함
--%>
<%
//내용이 있다면 cpage 구성
int cpage = 1;
if(request.getParameter("cpage") != null && !request.getParameter("cpage").equals("")){
	cpage = Integer.parseInt(request.getParameter("cpage"));
}

BoardListTO listTO = new BoardListTO();
listTO.setCpage(cpage);

BoardDAO dao = new BoardDAO();
listTO = dao.boardList(listTO);

//페이징 설정
int recordPerpage = listTO.getRecordPerPage();
int totalRecord = listTO.getTotalRecord();
int totalPage = listTO.getTotalPage();
int blockPerPage = listTO.getBlockPerPage();
int blockRecord = listTO.getBlockRecord();
int startBlock = listTO.getStartBlock();
int endBlock = listTO.getEndBlock();

/*
StringBuffer => 문자열을 추가하거나 변경할 때 주로 사용하는 자료형이다.
	- 사용 예시
		: StringBuffer sb = new StringBuffer(); - 객체 sb생성
		  sb.append("hello")
		  sb.append("world")
		  String result = sb.toString()
*/
//컴포넌트 작성
StringBuffer sbHtml = new StringBuffer();
for(BoardTO to : listTO.getBoardLists()){
	blockRecord++;
	sbHtml.append("<td>");
		sbHtml.append("<div>");
			sbHtml.append("<table>");
			
				sbHtml.append("<tr>");
					sbHtml.append("<td>");
					
						sbHtml.append("<div class='card' style='width:236px'>");
							//사진이 올라가있는 썸네일 -> 클릭시 board_view.jsp로 이동
							sbHtml.append("<a href='board_view.jsp?cpage=" + cpage + "&seq=" + to.getSeq() + "'><img src='upload/" + to.getFilename() + "' class='card-img-top'/></a>");

							//텍스트 오버플로우 선언하는 곳
							sbHtml.append("<div class='card-body'>");
								sbHtml.append("<div class='card-title'>");
									//제목 가져오기
									sbHtml.append("<span class='badge bg-danger me-2'>new</span><strong>" + to.getSubject() + "</strong>");
							
									//제목이 너무 짧을 경우에 사용
									if(to.getWgap() == 0){
										sbHtml.append(" ~~ ");
									}
								sbHtml.append("</div>"); //card-title-end

					
								sbHtml.append("<div class='card-text'>");
									//글쓴이 가져오기
									sbHtml.append("<span class='mt-3 mb-3'>작성자 : " + to.getWriter() + "</span>");
									
									sbHtml.append("<br>");
									
									//hit 가져오기
									sbHtml.append("<div class='d-flex justify-content-end fs10 mt-3 mb-3'>" + to.getWdate() + "&nbsp;|&nbsp;Hit" + to.getHit() + "</div>");
			
								sbHtml.append("</div>"); //card-text-end
								
								sbHtml.append("<div class='d-flex justify-content-end'>");
									//more클릭 시 board_view.jsp로 이동
									sbHtml.append("<a href='board_view.jsp?cpage=" + cpage + "&seq=" + to.getSeq() + "'>more</a>");	
								sbHtml.append("</div>");
								
							sbHtml.append("</div>"); //card-body-end
						sbHtml.append("</div>"); //card-end
					
					sbHtml.append("</td>");	
				sbHtml.append("</tr>");				
				
			sbHtml.append("</table>");
		sbHtml.append("</div>");
	sbHtml.append("</td>");
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="css/custom.css">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="css/custom.css">
<title>Board List</title>
</head>
<body>
<div class="container">
   <div class="row">
      <div class="col-md-12">
      
      <div class="d-flex mt-3 mb-3">
      	<p class="mx-3">
      		<img alt="홈 아이콘" src="img/home.svg">
      	</p>
      	
      	<p>
      	총<span class="text-danger">
      	<%=blockRecord%>
      	</span>건
      	</p>
      </div>
      
      <%-- sbHtml이 들어가는 곳 --%>
      <div>
      	<table class="table">
      		<tr>
      		<%=sbHtml%>	
      		</tr>
      	</table>
      </div>
      
      <%-- 페이징 --%>
      <div class="d-flex justify-content-between mt-3 align-items-center">
      	<ul class="pagination"> 
      	<%
      	//이전페이지 이동
      	//-> 맨 처음 페이지로 이동
      	if(startBlock == 1){
      		out.println("<li class='off page-item'><a class='page-link'>&lt;&lt;</a></li>");
      	}
      	else{
      		out.println("<li class='off page-item'><a class='page-link' href='board_list.jsp?cpage=" + (startBlock-blockPerPage) + "'>&lt;</a></li>");
      	}
      	//바로 이전페이지로 이동
      	if(cpage == 1){
      		out.println("<li class='off page-item'><a class='page-link'>&lt;</a></li>");
      	}
      	else{
      		out.println("<li class='off page-item'><a class='page-link' href='board_list.jsp?cpage=" + (cpage - 1) + "'>&lt;</a></li>");
      	}
      	
      	//현재 페이지 표시
      	for(int i = startBlock; i <= endBlock; i++){
      		if(cpage == i){
      			out.println("<li class='off page-item'><a class='page-link'>["+ i +"]</a></li>");
      		}
      		else{
      			out.println("<li class='off page-item'><a class='page-link' href='board_list.jsp?cpage="+ i +"'>["+ i +"]</a></li>");
      		}
      	}
      	
      	//다음페이지 이동
      	//-> 바로 다음페이지로 이동
      	if(cpage == totalPage){
      		out.println("<li class='off page-item'><a class='page-link'>&gt;</a></li>");
      	}
      	else{
      		out.println("<li class='off page-item'><a class='page-link' href='board_list.jsp?cpage="+ (cpage+1) +"'>&gt;</a></li>");
      	}
      	//-> 맨 마지막 페이지로 이동
      	if(endBlock == totalPage){
      		out.println("<li class='off page-item'><a class='page-link'>&gt;&gt;</a></li>");
      	}
      	else{
      		out.println("<li class='off page-item'><a class='page-link' href='board_list.jsp?cpage="+ (startBlock+blockPerPage) +"'>&gt;&gt;</a></li>");
      	}
      	%>
      	</ul>
      	
      	<input type="button" value="쓰기" class="btn btn-primary" onclick="location.href='board_write.jsp?cpage=<%=cpage%>'"/>
      </div>
      
      
      </div>
   </div>
</div>

</body>
</html>