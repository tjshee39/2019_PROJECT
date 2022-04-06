package bbsinsect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import bbsanimal.BbsAnimal;

public class BbsInsectDAO {
	private Connection conn;   // connection: 데이터베이스에 접근 가능하게 해주는 객체
	private ResultSet rs;   // 어떠한 정보를 담을 수 있는 객체(DB data), ctrl + shift + o = auto import

	public BbsInsectDAO() {  //class이름과 method이름 동일, DB연결
		try {
			String dbURL = "jdbc:mysql://localhost:3306/BBS?serverTimezone=UTC";
			String dbID = "root";
			String dbPassword = "1234";   // 자신의 root계정의 비밀번호
			Class.forName("com.mysql.jdbc.Driver");   // mysql 드라이버: mysql에 접속할 수 있도록 해주는 매개체의 라이브러리
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);   //dbRUL에 dbID와 dbPassword를 이용하여 접속
			} catch (Exception e) {   // 예외처리
				e.printStackTrace();   // 오류발생
				}
		}
	
	public String getDate() {  //글 작성 시 현재 서버의 시간 넣어줌
		String SQL = "SELECT NOW()"; //현재 시간을 가져오는 sql문장
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql문자를 실행준비단계 상태로
			rs = pstmt.executeQuery();  //실제로 실행했을 때 나오는 결과 가져옴
			if (rs.next()) {
				return rs.getString(1);  //현재의 날짜 그대로 반환
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";  //데이터베이스 오류
	}
	
	public int getNext() {  
		String SQL = "SELECT bbsInsectID FROM BBSINSECT ORDER BY bbsInsectID DESC"; //BBS테이블에서 내림차순(bbsID기준)으로 가장 마지막에 쓰인 글의 번호 가져옴
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql문자를 실행준비단계 상태로
			rs = pstmt.executeQuery();  //실제로 실행했을 때 나오는 결과 가져옴
			if (rs.next()) {
				return rs.getInt(1) +1;  //다음 게시글의 번호
			}
			return 1;  //첫 번재 게시글인 경우
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  //데이터베이스 오류
	}
	
	public int write(String bbsInsectTitle, String userID, String bbsInsectContent) {  //게시글 db에 삽입
		String SQL = "INSERT INTO BBSINSECT (bbsInsectID, bbsInsectTitle, UserID, bbsInsectDate, bbsInsectContent, bbsInsectAvailable) VALUES (?, ?, ?, ?, ?, ?)"; 
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql문자를 실행준비단계 상태로
			pstmt.setInt(1, getNext());  //게시글 번호
			pstmt.setString(2, bbsInsectTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsInsectContent);
			pstmt.setInt(6, 1);  //available = 1: 삭제 안됨
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  //데이터베이스 오류
		}

	
	
	public ArrayList<BbsInsect> getList(int pageNumber) {  //DB에서 글 목록 가져오는 소스코드는 BbsDAO.java에 리스트에 담아 반환

		String SQL = "SELECT * FROM BBSINSECT WHERE bbsInsectID < ? AND bbsInsectAvailable = 1 "
				+ "ORDER BY bbsInsectID DESC LIMIT 10"; //한페이지에 글 최대 10개
		ArrayList<BbsInsect> list  = new ArrayList<BbsInsect>();  //Bbs라는 클래스에서 나온 인스턴스를 보관할 수 있는 리스트
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql문자를 실행준비단계 상태로
			pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
			rs = pstmt.executeQuery();  //실제로 실행했을 때 나오는 결과 가져옴
			while (rs.next()) {
				BbsInsect bbsinsect = new BbsInsect();
				bbsinsect.setBbsInsectID(rs.getInt(1));
				bbsinsect.setBbsInsectTitle(rs.getString(2));
				bbsinsect.setUserID(rs.getString(3));
				bbsinsect.setBbsInsectDate(rs.getString(4));
				bbsinsect.setBbsInsectContent(rs.getString(5));
				bbsinsect.setBbsInsectAvailable(rs.getInt(6));
				bbsinsect.setBbsInsectHit(rs.getInt(7));
				list.add(bbsinsect);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;  //게시글 리스트 출력
	}
	
	public boolean nextPage(int pageNumber) {  //페이징 처리
		String SQL = "SELECT * FROM BBSINSECT WHERE bbsInsectID < ? AND bbsInsectAvailable = 1"; //한페이지에 글 최대 10개
		ArrayList<BbsInsect> list  = new ArrayList<BbsInsect>();  //Bbs라는 클래스에서 나온 인스턴스를 보관할 수 있는 리스트
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql문자를 실행준비단계 상태로
			pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
			rs = pstmt.executeQuery();  //실제로 실행했을 때 나오는 결과 가져옴
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;  
	}
	
	public BbsInsect getBbsInsect(int bbsInsectID) {  //게시글의 내용 불러오는 함수 (특정한 id에 해당하는 게시글 가져옴)
		String SQL = "SELECT * FROM BBSINSECT WHERE bbsInsectID = ?";  //bbsID가 특정한 숫자인 경우
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql문자를 실행준비단계 상태로
			pstmt.setInt(1, bbsInsectID);
			rs = pstmt.executeQuery();  //실제로 실행했을 때 나오는 결과 가져옴
			if (rs.next()) {
				BbsInsect bbsinsect = new BbsInsect();
				bbsinsect.setBbsInsectID(rs.getInt(1));
				bbsinsect.setBbsInsectTitle(rs.getString(2));
				bbsinsect.setUserID(rs.getString(3));
				bbsinsect.setBbsInsectDate(rs.getString(4));
				bbsinsect.setBbsInsectContent(rs.getString(5));
				bbsinsect.setBbsInsectAvailable(rs.getInt(6));
				bbsinsect.setBbsInsectHit(rs.getInt(7));
				updateHit(bbsInsectID);
				
				return bbsinsect;  //여섯개의 변수를 받은 후 bbs인스턴스에 넣어줌
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;  
	}
	
	public int updateHit(int bbsInsectID) {
		String SQL = "UPDATE BBSINSECT SET bbsInsectHit = bbsInsectHit+1 WHERE bbsInsectID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsInsectID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int updateHitMod(int bbsInsectID) {
		String SQL = "UPDATE BBSINSECT SET bbsInsectHit = bbsInsectHit-2 WHERE bbsInsectID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsInsectID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int update(int bbsInsectID, String bbsInsectTitle, String bbsInsectContent) {  
		String SQL = "UPDATE BBSINSECT SET bbsInsectTitle = ?, bbsInsectContent = ?, bbsInsectDate = ? WHERE bbsInsectID = ?";  //특정한 bbsID의 글의 제목과 내용 변경 
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql문자를 실행준비단계 상태로
			pstmt.setString(1, bbsInsectTitle);  
			pstmt.setString(2, bbsInsectContent);
			pstmt.setString(3, getDate());
			pstmt.setInt(4, bbsInsectID);
			updateHitMod(bbsInsectID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  //데이터베이스 오류
	}
	
	public int delete(int bbsInsectID) {
		String SQL = "UPDATE BBSINSECT SET bbsInsectAvailable = 0 WHERE bbsInsectID = ?";  //글 삭제해도 기록은 남음
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql문자를 실행준비단계 상태로
			pstmt.setInt(1, bbsInsectID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  //데이터베이스 오류
	}
}
	
	


