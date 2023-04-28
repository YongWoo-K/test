<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="javax.naming.Context" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.naming.NamingException" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.SQLException" %>
<%
request.setCharacterEncoding("utf-8");

Connection conn = null; //Connection타입의 conn의 초기값을 null로 설정(값을 가지지 않음)
PreparedStatement pstmt = null;

try{
   //커넥션 풀로 DB에 연결하기
   Context initCtx = new InitialContext();
   Context envCtx = (Context)initCtx.lookup("java:comp/env");
   DataSource dataSource = (DataSource)envCtx.lookup("jdbc/mysql");
   conn = dataSource.getConnection();
   
   String sql = "insert into al_board1 values(0,?,?,?,?,?,?,0,0,?,now())";
   pstmt = conn.prepareStatement(sql);
   for(int i=1; i<=10; i++){
      pstmt.setString(1, "제목"+i);
      pstmt.setString(2, "글쓴이"+i);
      pstmt.setString(3, "test@test.com"); //email
      pstmt.setString(4, "1234"); //password
      pstmt.setString(5, "내용"+i);
      pstmt.setString(6, "607979_1.jpg"); //이미지파일명
      pstmt.setString(7, "000.000.000.000"); //ip주소
      pstmt.executeUpdate();
   }
}catch(NamingException e){
   System.out.println("error: "+e.getMessage());
}catch(SQLException e){
   System.out.println("error: "+e.getMessage());
}finally{
   if(pstmt != null)pstmt.close();
   if(conn != null)conn.close();
}
out.println("<script>");
out.println("alert('글쓰기에 성공했습니다')");
out.println("location.href='board_list.jsp'");
out.println("</script>");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.3/jquery.min.js"></script>
  <link rel="stylesheet" href="css/custom.css"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <script src="js/login.js"></script>
<title></title>
</head>
<body>
<div class="container">
   <div class="row">
      <div class="col-md-12">
      </div>
   </div>
</div>

</body>
</html>