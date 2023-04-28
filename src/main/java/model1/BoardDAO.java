package model1;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class BoardDAO {
	//db연동
	private DataSource dataSource;
	
	/*
	JNDI(Java Naming and Directory Interface)
	- 디렉토리 서비스에서 제공하는 데이터 및 객체를 발견하고 참고하기 위한 Java API
	- 외부에 있는 객체를 가져오기 위한 기술
	- tomcat과 같은 was를 보면 특정 폴더에 데이터 소스 라이브러리가 존재하는데 그것을 사용하기 위해
	  JNDI를 사용하여 가져온다. 
	  
	DBCP(DataBase Connection Pool)  
	- 데이터베이스와 연결된 커넥션을 미리 만들어서 저장해두고 있다가 필요할 때 저장된 공간(pool)을 
	  가져다 쓰고 반환하는 기법
	 */
	public BoardDAO() {
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context)initCtx.lookup("java:comp/env");
			//-> initCtx의 lookup() 메소드를 시용해서 "java:comp/env"에 해당하는 객체를 찾아 envCtx에 삽입	
			/*
			 java:comp/env는 웹 어플에 구성된 엔트리와 리소스들이 배치되어있는 부분
			 	-> 따라서 이것에 접근하여 web.xml의 <resource-env-ref>에 설정한 jdbc/mysql과 맵핑되는 리소스를 가져온다.
			 */
			
			this.dataSource = (DataSource)envCtx.lookup("jdbc/mysql");
			//-> envCtx의 lookup() 메소드를 이용해서 jdbc/mysql에 해당하는 객체를 찾아서 dataSource에 삽입
		}catch(NamingException e) {
			System.out.println("[error] : " + e.getMessage()); //-> 에러가 생길 경우 출력
		}
	}
	
	
	 
	//write() 
	public void boardWrite() {
		
	}
	
	//write ok
	public int boardWriteOK(BoardTO to) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		//정상처리 또는 비정상처리 변수
		int flag = 1;
		/*
		 정상/비정상 처리
		 	- 정상/비정상처리 변수가 0 이면 설정되지 않음을 의미, 1이면 설정됨을 의미
		 	- 산술계산을 수행했는데 결과가 0이면 flag = 1, 다른숫자는 0이 된다.
		 */
		
		try {
			conn = dataSource.getConnection();
			//데이터베이스에 데이터 집어넣기
			String sql = "insert into al_board1 values (0,?,?,?,?,?,?,?,0,?,now())";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getSubject());//제목
			pstmt.setString(2, to.getWriter());//글쓴이
			pstmt.setString(3, to.getMail());//메일
			pstmt.setString(4, to.getPassword());//패스워드
			pstmt.setString(5, to.getContent());//내용
			pstmt.setString(6, to.getFilename());//파일명
			pstmt.setLong(7, to.getFilesize());//파일크기
			pstmt.setString(8, to.getWip());//ip
			int result = pstmt.executeUpdate();
			
			if(result == 1) {
				flag = 0;
			}
		}catch(SQLException e) {
			System.out.println("error : " + e.getMessage());
		}finally {
			if(pstmt != null) {try {pstmt.close();}catch(SQLException e) {}}
			if(conn != null) {try {conn.close();}catch(SQLException e) {}}
		}
		return flag;
	}
	
	//list
	public ArrayList<BoardTO> boardList(){
		Connection conn = null; //연결해주는 객체를 생성
		//-> Connection = 연결
		PreparedStatement pstmt = null; //값을 나중에 입력한다.
		//-> PreparedStatement = 설정 및 실행
		ResultSet rs = null; 
		//-> ResultSet = 결과값
		
		ArrayList<BoardTO> lists = new ArrayList<BoardTO>();
		/*
		  ArrayList를 사용하는 이유
		    1. 원소를 추가/삭제할 수 있다.
		    2. null값을 가질 수 있다.
		    
		    배열의 사이즈가 변하면 안되거나 변할 필요가 없을 경우에는 list.of를 사용
		    list에서는 ArratList를 사용한다.
		 */
		try {
			conn = dataSource.getConnection();
			String sql = "select seq, subject, filename, writer, date_format(wdate,'%Y-%m-%d') wdate, hit, datediff(now(),wdate)wgap from al_board1 order by seq desc";
			pstmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			/*
			  ResultSet.TYPE_SCROLL_INSENSITIVE
			  	- ResultSet에서 re.next()를 사용하면 다음 결과 row를 가져오고 다음에는 이전값을 사용 못하게된다.
			  	- 이 옵션으로 ResultSet을 만들면 한번 커서가 지나간 다음에 다시 되돌릴 수 있다.
			  
			  ResultSet.CONCUR_READ_ONLY
			  	- ResultSet으로 가져온 row의 값을 다시 insert나 update로 사용하지 않겠다는 의미(읽기전용)
			 */
			rs = pstmt.executeQuery(); //셋팅이 끝난 쿼리문을 실행시키고, 나온 결과값을 rs에 저장 
			
			//데이터베이스에서 글 목록을 얻어와서 리스트 나타내기
			while(rs.next()) {
				BoardTO to = new BoardTO();
				String seq = rs.getString("seq");
				String subject = rs.getString("subject");
				String writer = rs.getString("writer");
				String filename = rs.getString("filename");
				String wdate = rs.getString("wdate");
				String hit = rs.getString("hit");
				int wgap = rs.getInt("wgap");
				
				to.setSeq(seq);
				to.setSubject(subject);
				to.setWriter(writer);
				to.setFilename(filename);
				to.setWdate(wdate);
				to.setHit(hit);
				to.setWgap(wgap);
				lists.add(to);
			}
		}catch(SQLException e) {
			System.out.println("error : " + e.getMessage());
		}finally {
			if(rs != null) {try {rs.close();}catch(SQLException e) {}}
			if(pstmt != null) {try {pstmt.close();}catch(SQLException e) {}}
			if(conn != null) {try {conn.close();}catch(SQLException e) {}}
		}
		return lists;
	}
	
	//paging list
	public BoardListTO boardList(BoardListTO listTO) {
		Connection conn = null; //연결해주는 객체를 생성
		//-> Connection = 연결
		PreparedStatement pstmt = null; //값을 나중에 입력한다.
		//-> PreparedStatement = 설정 및 실행
		ResultSet rs = null; 
		//-> ResultSet = 결과값
		
		//페이지를 위한 기본요소
		int cpage = listTO.getCpage();
		int recordPerPage = listTO.getRecordPerPage(); //한 페이지에 보이는 글의 개수 설정 - 5개
		int blockPerpage = listTO.getBlockPerPage(); //한 화면에 보일 페이지의 수 설정 - 3개
		
		try {
			conn = dataSource.getConnection();
			String sql = "select seq, subject, filename, writer, date_format(wdate,'%Y-%m-%d') wdate, hit, datediff(now(),wdate)wgap from al_board1 order by seq desc";
			pstmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
			
			//모든 글의 수 얻기
			rs.last(); //커서의 위치를 조회 결과 값의 마지막으로 이동
			listTO.setTotalRecord(rs.getRow()); //현재 커서가 가르키고있는 row번호 값을 BoardListTO의 totalRecord 값으로 설정
			rs.beforeFirst();
			/*
			 ResultSet에 존재하는 커서를 움직일 수 있는 메소드
			    last() : 레코드의 맨 마지막 row로 이동한다.
			    first() : 레코드의 첫번째 row 로 이동한다.
			    next() : 다음 레코드로 이동한다. (다음 row로 이동)
			    previous() : 이전 레코드로 이동한다. (이전 row 로 이동)
			    beforeFirst() : 커서를 처음위치로 이동한다.
			    afterLast() : 커서를 마지막 위치로 이동한다.
			    absolute(int row) : 커서를 특정 row 위치로 이동한다.
				getRow() : 현재 커서가 가르키고 있는 row번호
				isFirst() : 커서의 위치가 맨 처음 인지
				isLast() : 커서의 위치가 마지막인지
			 */
			
			//모든 페이지 수 얻기
			listTO.setTotalPage(((listTO.getTotalRecord()-1) / recordPerPage) + 1);
			int skip = (cpage * recordPerPage) - recordPerPage;
			if(skip != 0) {rs.absolute(skip);}
			
			/*
			 페이징 처리 방법
			 1. 한 화면(한 장)에 몇개의 글을 보여줄지 설정(5개)
			 2. 몇 개의 페이지를 보여줄건지 설정(3개)
			 ex) 42개의 글이 있을 경우 (42-1/ 3) + 1, <<  <  1  2  3  >  >> 
			 */
			ArrayList<BoardTO> lists = new ArrayList<BoardTO>();
			for(int i = 0; i < recordPerPage && rs.next(); i++) {
				BoardTO to = new BoardTO();
				String seq = rs.getString("seq");
				String subject = rs.getString("subject");
				String writer = rs.getString("writer");
				String filename = rs.getString("filename");
				String wdate = rs.getString("wdate");
				String hit = rs.getString("hit");
				int wgap = rs.getInt("wgap");
				
				to.setSeq(seq);
				to.setSubject(subject);
				to.setWriter(writer);
				to.setFilename(filename);
				to.setWdate(wdate);
				to.setHit(hit);
				to.setWgap(wgap);
				lists.add(to);
			}
			listTO.setBoardLists(lists);
			//시작블록과 종료블록 설정
			listTO.setStartBlock(((cpage-1) / blockPerpage) * blockPerpage + 1);
			listTO.setEndBlock(((cpage-1) / blockPerpage) * blockPerpage + blockPerpage);
			
			//종료블록이 토탈페이지보다 크거나 같을 경우
			if(listTO.getEndBlock() >= listTO.getTotalPage()) {
				//페이징 리스트의 종료블록을 토탈페이지와 같게 설정
				listTO.setEndBlock(listTO.getTotalPage());
			}
			
		}catch(SQLException e) {
			System.out.println("error : " + e.getMessage());
		}finally {
			if(rs != null) {try {rs.close();}catch(SQLException e) {}}
			if(pstmt != null) {try {pstmt.close();}catch(SQLException e) {}}
			if(conn != null) {try {conn.close();}catch(SQLException e) {}}
		}
		return listTO;
	}
	
	//view 디테일 또는 상세페이지
	public BoardTO boardView(BoardTO to) {
		Connection conn = null; //연결해주는 객체를 생성
		//-> Connection = 연결
		PreparedStatement pstmt = null; //값을 나중에 입력한다.
		//-> PreparedStatement = 설정 및 실행
		ResultSet rs = null; 
		//-> ResultSet = 결과값
		
		try {
			//조회수 증가시키고 데이터베이스에서 해당 글 내용을 가져오고 sql실행문에서 각 컬럼을 가져와서 변수에 저장
			conn = dataSource.getConnection();
			
			//조회수 증가시키기
			String sql = "update al_board1 set hit = hit+1 where seq = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getSeq());
			rs = pstmt.executeQuery();
		
			//데이터베이스에서 해당 글 내용 가져오기
			sql = "select subject, writer, mail, content, filename, hit, wip, wdate from al_board1 where seq = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getSeq());
			rs = pstmt.executeQuery();
			
			//데이터베이스에서 sql실행문의 각 컬럼을 가져와서 변수에 저장
			if(rs.next()) {
				String subject = rs.getString("subject");
				String writer = rs.getString("writer");
				String mail = rs.getString("mail");
				String content = rs.getString("content");
				String filename = rs.getString("filename");
				String hit = rs.getString("hit");
				String wip = rs.getString("wip");
				String wdate = rs.getString("wdate");
				
				to.setSubject(subject);
				to.setSubject(writer);
				to.setSubject(mail);
				to.setSubject(content);
				to.setSubject(filename);
				to.setSubject(hit);
				to.setSubject(wip);
				to.setSubject(wdate);
			}
			
		}catch(SQLException e) {
			System.out.println("error : " + e.getMessage());
		}finally {
			if(rs != null) {try {rs.close();}catch(SQLException e) {}}
			if(pstmt != null) {try {pstmt.close();}catch(SQLException e) {}}
			if(conn != null) {try {conn.close();}catch(SQLException e) {}}
		}
		return to;
	}
	
	//view 이전글
	public BoardTO boardView_before(BoardTO to_before) {
		Connection conn = null; //연결해주는 객체를 생성
		//-> Connection = 연결
		PreparedStatement pstmt = null; //값을 나중에 입력한다.
		//-> PreparedStatement = 설정 및 실행
		ResultSet rs = null; 
		//-> ResultSet = 결과값
		
		try {
			conn = dataSource.getConnection();
			
			//데이터베이스에서 해당 글 내용 가져오기
			String sql = "select seq, subject from al_board1 where seq = (select max(seq) from al_board1 where seq < ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to_before.getSeq());
			rs = pstmt.executeQuery();
			
			//이전글이 있다면 가져오고 아니라면 메세지 실행
			if(rs.next()) {
				//데이터베이스에서 sql실행문의 각 컬럼을 가져와서 변수에 저장
				String subject = rs.getString("subject");
				String seq = rs.getString("seq");
				
				to_before.setSubject(subject);
				to_before.setSeq(seq);
			}
			else {
				to_before.setSubject("이전글이 없습니다.");
			}
		}catch(SQLException e) {
			System.out.println("error : " + e.getMessage());
		}finally {
			if(rs != null) {try {rs.close();}catch(SQLException e) {}}
			if(pstmt != null) {try {pstmt.close();}catch(SQLException e) {}}
			if(conn != null) {try {conn.close();}catch(SQLException e) {}}
		}
		return to_before;
	}
	
	//view 다음글
	public BoardTO boardView_next(BoardTO to_next) {
		Connection conn = null; //연결해주는 객체를 생성
		//-> Connection = 연결
		PreparedStatement pstmt = null; //값을 나중에 입력한다.
		//-> PreparedStatement = 설정 및 실행
		ResultSet rs = null; 
		//-> ResultSet = 결과값
		
		try {
			conn = dataSource.getConnection();
			
			//데이터베이스에서 해당 글 내용 가져오기
			String sql = "select seq, subject from al_board1 where seq = (select min(seq) from al_board1 where seq > ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to_next.getSeq());
			rs = pstmt.executeQuery();
			
			//다음글이 있다면 가져오고 아니라면 메세지 실행
			if(rs.next()) {
				//데이터베이스에서 sql실행문의 각 컬럼을 가져와서 변수에 저장
				String subject = rs.getString("subject");
				String seq = rs.getString("seq");
				
				to_next.setSubject(subject);
				to_next.setSeq(seq);
			}
			else {
				to_next.setSubject("다음글이 없습니다.");
			}
		}catch(SQLException e) {
			System.out.println("error : " + e.getMessage());
		}finally {
			if(rs != null) {try {rs.close();}catch(SQLException e) {}}
			if(pstmt != null) {try {pstmt.close();}catch(SQLException e) {}}
			if(conn != null) {try {conn.close();}catch(SQLException e) {}}
		}
		return to_next;
	}
	
	//삭제할 글의 내용 가져오는 메소드
	public BoardTO boardDelete(BoardTO to) {
		Connection conn = null; //연결해주는 객체를 생성
		//-> Connection = 연결
		PreparedStatement pstmt = null; //값을 나중에 입력한다.
		//-> PreparedStatement = 설정 및 실행
		ResultSet rs = null; 
		//-> ResultSet = 결과값
		
		try {
			conn = dataSource.getConnection();
			
			//데이터베이스에서 삭제할 글의 내용 가져오기
			String sql = "select subject, writer from al_board1 where seq = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getSeq());
			rs = pstmt.executeQuery();
			
			//데이터베이스에서 sql실행문의 각 컬럼을 가져와서 변수에 저장
			if(rs.next()) {
				String subject = rs.getString("subject");
				String writer = rs.getString("writer");
				
				to.setSubject(subject);
				to.setWriter(writer);
			}
		}catch(SQLException e) {
			System.out.println("error : " + e.getMessage());
		}finally {
			if(rs != null) {try {rs.close();}catch(SQLException e) {}}
			if(pstmt != null) {try {pstmt.close();}catch(SQLException e) {}}
			if(conn != null) {try {conn.close();}catch(SQLException e) {}}
		}
		return to;
	}
	/*
	글을 삭제하는 영역 
		- 첨부파일(사진)이 들어가 있어서 파일명을 가져오고 해당 글 내용을 가져온다.
	 	- 패스워드가 초기에 등록했던 것과 맞아야 삭제가 진행된다.
	 */
	public int boardDeleteOK(BoardTO to) {
		Connection conn = null; //연결해주는 객체를 생성
		//-> Connection = 연결
		PreparedStatement pstmt = null; //값을 나중에 입력한다.
		//-> PreparedStatement = 설정 및 실행
		ResultSet rs = null; 
		//-> ResultSet = 결과값
		
		// 정상/비정상처리 변수
		int flag = 2;
		
		try {
			conn = dataSource.getConnection();
			String sql = "select filename from al_board1 where seq = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getSeq());
			rs = pstmt.executeQuery();
			String filename = null;
			if(rs.next()) {
				filename = rs.getString("filename");
			}
			
			//데이터베이스에서 해당 글 내용 삭제하기 - 동일한 seq와 알맞은 비밀번호이어야 함
			sql = "delete from al_board1 where seq = ? and password = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getSeq());
			pstmt.setString(2, to.getPassword());
			int result = pstmt.executeUpdate();
			if(result == 0) {
				flag = 1;
			}else if(result == 1) {
				flag = 0;
				if(filename != null) {
					File file = new File("D:/dev_kyw/jsp_lesson/photo/src/main/webapp/upload/" + filename);
					file.delete();
				}
			}
		}catch(SQLException e) {
			System.out.println("error : " + e.getMessage());
		}finally {
			if(rs != null) {try {rs.close();}catch(SQLException e) {}}
			if(pstmt != null) {try {pstmt.close();}catch(SQLException e) {}}
			if(conn != null) {try {conn.close();}catch(SQLException e) {}}
		}
		return flag;
	}
	
	//modify(수정) : 글과 올려놓은 사진을 바꿔치기
	public BoardTO boardModify(BoardTO to) {
		Connection conn = null; //연결해주는 객체를 생성
		//-> Connection = 연결
		PreparedStatement pstmt = null; //값을 나중에 입력한다.
		//-> PreparedStatement = 설정 및 실행
		ResultSet rs = null; 
		//-> ResultSet = 결과값
		
		try {
			conn = dataSource.getConnection();
			
			//데이터베이스에서 수정할 글의 내용 가져오기
			String sql = "select writer, subject, content, mail, filename from al_board1 where seq = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getSeq());
			rs = pstmt.executeQuery();
			
			//데이터베이스에서 sql실행문의 각 컬럼을 가져와서 변수에 저장
			if(rs.next()) {
				String subject = rs.getString("subject");
				String writer = rs.getString("writer");
				String content = rs.getString("content");
				String mail = rs.getString("mail");
				String filename = rs.getString("filename");
				
				to.setSubject(subject);
				to.setWriter(writer);
				to.setContent(content);
				to.setMail(mail);
				to.setFilename(filename);
			}
		}catch(SQLException e) {
			System.out.println("error : " + e.getMessage());
		}finally {
			if(rs != null) {try {rs.close();}catch(SQLException e) {}}
			if(pstmt != null) {try {pstmt.close();}catch(SQLException e) {}}
			if(conn != null) {try {conn.close();}catch(SQLException e) {}}
		}
		return to;
	}
	
	//수정하는 영역 - 첨부파일이 있을때와 없을떄
	public int boardModifyOK(BoardTO to) {
		Connection conn = null; //연결해주는 객체를 생성
		//-> Connection = 연결
		PreparedStatement pstmt = null; //값을 나중에 입력한다.
		//-> PreparedStatement = 설정 및 실행
		ResultSet rs = null; 
		//-> ResultSet = 결과값
		
		// 정상/비정상처리 변수
		int flag = 2;
		
		try {
			conn = dataSource.getConnection();
			String sql = "select filename from al_board1 where seq = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, to.getSeq());
			rs = pstmt.executeQuery();
			String oldfilename = null;
			if(rs.next()) {
				oldfilename = rs.getString("filename");
			}
			
			//수정 과정에서 첨부파일이 있을 경우
			if(to.getFilename() != null) {
				sql = "update al_board1 set subject = ?, content = ?, mail = ?, filename = ? where seq = ? and password = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, to.getSubject());
				pstmt.setString(2, to.getContent());
				pstmt.setString(3, to.getMail());
				pstmt.setString(4, to.getFilename());
				pstmt.setString(5, to.getSeq());
				pstmt.setString(6, to.getPassword());
			}
			//수정 과정에서 첨부파일이 없을 경우
			else {
				sql = "update al_board1 set subject = ?, content = ?, mail = ? where seq = ? and password = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, to.getSubject());
				pstmt.setString(2, to.getContent());
				pstmt.setString(3, to.getMail());
				pstmt.setString(4, to.getSeq());
				pstmt.setString(5, to.getPassword());
			}
			int result = pstmt.executeUpdate();
			if(result == 0) {
				flag = 1;
			}else if(result == 1) {
				flag = 0;
				//기존에 첨부파일이 있고 추가된 첨부파일이 있을 경우 기존 파일은 삭제한다
				if(to.getFilename() != null && oldfilename != null) {
					File file = new File("D:/dev_kyw/jsp_lesson/photo/src/main/webapp/upload/" + oldfilename);
					file.delete();
				}
			}
		}catch(SQLException e) {
			System.out.println("error : " + e.getMessage());
		}finally {
			if(rs != null) {try {rs.close();}catch(SQLException e) {}}
			if(pstmt != null) {try {pstmt.close();}catch(SQLException e) {}}
			if(conn != null) {try {conn.close();}catch(SQLException e) {}}
		}
		return flag;
	} 
}
