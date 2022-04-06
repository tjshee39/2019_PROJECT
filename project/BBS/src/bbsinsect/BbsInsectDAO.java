package bbsinsect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import bbsanimal.BbsAnimal;

public class BbsInsectDAO {
	private Connection conn;   // connection: �����ͺ��̽��� ���� �����ϰ� ���ִ� ��ü
	private ResultSet rs;   // ��� ������ ���� �� �ִ� ��ü(DB data), ctrl + shift + o = auto import

	public BbsInsectDAO() {  //class�̸��� method�̸� ����, DB����
		try {
			String dbURL = "jdbc:mysql://localhost:3306/BBS?serverTimezone=UTC";
			String dbID = "root";
			String dbPassword = "1234";   // �ڽ��� root������ ��й�ȣ
			Class.forName("com.mysql.jdbc.Driver");   // mysql ����̹�: mysql�� ������ �� �ֵ��� ���ִ� �Ű�ü�� ���̺귯��
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);   //dbRUL�� dbID�� dbPassword�� �̿��Ͽ� ����
			} catch (Exception e) {   // ����ó��
				e.printStackTrace();   // �����߻�
				}
		}
	
	public String getDate() {  //�� �ۼ� �� ���� ������ �ð� �־���
		String SQL = "SELECT NOW()"; //���� �ð��� �������� sql����
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql���ڸ� �����غ�ܰ� ���·�
			rs = pstmt.executeQuery();  //������ �������� �� ������ ��� ������
			if (rs.next()) {
				return rs.getString(1);  //������ ��¥ �״�� ��ȯ
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";  //�����ͺ��̽� ����
	}
	
	public int getNext() {  
		String SQL = "SELECT bbsInsectID FROM BBSINSECT ORDER BY bbsInsectID DESC"; //BBS���̺����� ��������(bbsID����)���� ���� �������� ���� ���� ��ȣ ������
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql���ڸ� �����غ�ܰ� ���·�
			rs = pstmt.executeQuery();  //������ �������� �� ������ ��� ������
			if (rs.next()) {
				return rs.getInt(1) +1;  //���� �Խñ��� ��ȣ
			}
			return 1;  //ù ���� �Խñ��� ���
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  //�����ͺ��̽� ����
	}
	
	public int write(String bbsInsectTitle, String userID, String bbsInsectContent) {  //�Խñ� db�� ����
		String SQL = "INSERT INTO BBSINSECT (bbsInsectID, bbsInsectTitle, UserID, bbsInsectDate, bbsInsectContent, bbsInsectAvailable) VALUES (?, ?, ?, ?, ?, ?)"; 
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql���ڸ� �����غ�ܰ� ���·�
			pstmt.setInt(1, getNext());  //�Խñ� ��ȣ
			pstmt.setString(2, bbsInsectTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsInsectContent);
			pstmt.setInt(6, 1);  //available = 1: ���� �ȵ�
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  //�����ͺ��̽� ����
		}

	
	
	public ArrayList<BbsInsect> getList(int pageNumber) {  //DB���� �� ��� �������� �ҽ��ڵ�� BbsDAO.java�� ����Ʈ�� ��� ��ȯ

		String SQL = "SELECT * FROM BBSINSECT WHERE bbsInsectID < ? AND bbsInsectAvailable = 1 "
				+ "ORDER BY bbsInsectID DESC LIMIT 10"; //���������� �� �ִ� 10��
		ArrayList<BbsInsect> list  = new ArrayList<BbsInsect>();  //Bbs��� Ŭ�������� ���� �ν��Ͻ��� ������ �� �ִ� ����Ʈ
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql���ڸ� �����غ�ܰ� ���·�
			pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
			rs = pstmt.executeQuery();  //������ �������� �� ������ ��� ������
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
		return list;  //�Խñ� ����Ʈ ���
	}
	
	public boolean nextPage(int pageNumber) {  //����¡ ó��
		String SQL = "SELECT * FROM BBSINSECT WHERE bbsInsectID < ? AND bbsInsectAvailable = 1"; //���������� �� �ִ� 10��
		ArrayList<BbsInsect> list  = new ArrayList<BbsInsect>();  //Bbs��� Ŭ�������� ���� �ν��Ͻ��� ������ �� �ִ� ����Ʈ
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql���ڸ� �����غ�ܰ� ���·�
			pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
			rs = pstmt.executeQuery();  //������ �������� �� ������ ��� ������
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;  
	}
	
	public BbsInsect getBbsInsect(int bbsInsectID) {  //�Խñ��� ���� �ҷ����� �Լ� (Ư���� id�� �ش��ϴ� �Խñ� ������)
		String SQL = "SELECT * FROM BBSINSECT WHERE bbsInsectID = ?";  //bbsID�� Ư���� ������ ���
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql���ڸ� �����غ�ܰ� ���·�
			pstmt.setInt(1, bbsInsectID);
			rs = pstmt.executeQuery();  //������ �������� �� ������ ��� ������
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
				
				return bbsinsect;  //�������� ������ ���� �� bbs�ν��Ͻ��� �־���
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
		String SQL = "UPDATE BBSINSECT SET bbsInsectTitle = ?, bbsInsectContent = ?, bbsInsectDate = ? WHERE bbsInsectID = ?";  //Ư���� bbsID�� ���� ����� ���� ���� 
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql���ڸ� �����غ�ܰ� ���·�
			pstmt.setString(1, bbsInsectTitle);  
			pstmt.setString(2, bbsInsectContent);
			pstmt.setString(3, getDate());
			pstmt.setInt(4, bbsInsectID);
			updateHitMod(bbsInsectID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  //�����ͺ��̽� ����
	}
	
	public int delete(int bbsInsectID) {
		String SQL = "UPDATE BBSINSECT SET bbsInsectAvailable = 0 WHERE bbsInsectID = ?";  //�� �����ص� ����� ����
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql���ڸ� �����غ�ܰ� ���·�
			pstmt.setInt(1, bbsInsectID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  //�����ͺ��̽� ����
	}
}
	
	

