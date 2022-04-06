package bbsplant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsPlantDAO {
	private Connection conn;   // connection: �����ͺ��̽��� ���� �����ϰ� ���ִ� ��ü
	private ResultSet rs;   // ��� ������ ���� �� �ִ� ��ü(DB data), ctrl + shift + o = auto import

	public BbsPlantDAO() {  //class�̸��� method�̸� ����, DB����
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
		String SQL = "SELECT bbsPlantID FROM BBSPLANT ORDER BY bbsPlantID DESC"; //BBS���̺����� ��������(bbsID����)���� ���� �������� ���� ���� ��ȣ ������
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
	
	public int write(String bbsPlantTitle, String userID, String bbsPlantContent) {  //�Խñ� db�� ����
		String SQL = "INSERT INTO BBSPLANT VALUES (?, ?, ?, ?, ?, ?, ?)"; 
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql���ڸ� �����غ�ܰ� ���·�
			pstmt.setInt(1, getNext());  //�Խñ� ��ȣ
			pstmt.setString(2, bbsPlantTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsPlantContent);
			pstmt.setInt(6, 1);  //available = 1: ���� �ȵ�
			pstmt.setInt(7, 0);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  //�����ͺ��̽� ����
		}

	
	
	public ArrayList<BbsPlant> getList(int pageNumber) {  //DB���� �� ��� �������� �ҽ��ڵ�� BbsDAO.java�� ����Ʈ�� ��� ��ȯ

		String SQL = "SELECT * FROM BBSPLANT WHERE bbsPlantID < ? AND bbsPlantAvailable = 1 "
				+ "ORDER BY bbsPlantID DESC LIMIT 10"; //���������� �� �ִ� 10��
		ArrayList<BbsPlant> list  = new ArrayList<BbsPlant>();  //Bbs��� Ŭ�������� ���� �ν��Ͻ��� ������ �� �ִ� ����Ʈ
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql���ڸ� �����غ�ܰ� ���·�
			pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
			rs = pstmt.executeQuery();  //������ �������� �� ������ ��� ������
			while (rs.next()) {
				BbsPlant bbsplant = new BbsPlant();
				bbsplant.setBbsPlantID(rs.getInt(1));
				bbsplant.setBbsPlantTitle(rs.getString(2));
				bbsplant.setUserID(rs.getString(3));
				bbsplant.setBbsPlantDate(rs.getString(4));
				bbsplant.setBbsPlantContent(rs.getString(5));
				bbsplant.setBbsPlantAvailable(rs.getInt(6));
				bbsplant.setBbsPlantHit(rs.getInt(7));
				list.add(bbsplant);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;  //�Խñ� ����Ʈ ���
	}
	
	public boolean nextPage(int pageNumber) {  //����¡ ó��
		String SQL = "SELECT * FROM BBSPLANT WHERE bbsPlantID < ? AND bbsPlantAvailable = 1"; //���������� �� �ִ� 10��
		ArrayList<BbsPlant> list  = new ArrayList<BbsPlant>();  //Bbs��� Ŭ�������� ���� �ν��Ͻ��� ������ �� �ִ� ����Ʈ
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
	
	public BbsPlant getBbsPlant(int bbsPlantID) {  //�Խñ��� ���� �ҷ����� �Լ� (Ư���� id�� �ش��ϴ� �Խñ� ������)
		String SQL = "SELECT * FROM BBSPLANT WHERE bbsPlantID = ?";  //bbsID�� Ư���� ������ ���
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql���ڸ� �����غ�ܰ� ���·�
			pstmt.setInt(1, bbsPlantID);
			rs = pstmt.executeQuery();  //������ �������� �� ������ ��� ������
			if (rs.next()) {
				BbsPlant bbsplant = new BbsPlant();
				bbsplant.setBbsPlantID(rs.getInt(1));
				bbsplant.setBbsPlantTitle(rs.getString(2));
				bbsplant.setUserID(rs.getString(3));
				bbsplant.setBbsPlantDate(rs.getString(4));
				bbsplant.setBbsPlantContent(rs.getString(5));
				bbsplant.setBbsPlantAvailable(rs.getInt(6));
				bbsplant.setBbsPlantHit(rs.getInt(7));
				updateHit(bbsPlantID, bbsplant.getBbsPlantHit());

				return bbsplant;  //�������� ������ ���� �� bbs�ν��Ͻ��� �־���
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;  
	}
	
	public int updateHit(int bbsPlantID, int bbsPlantHit) {
		String SQL = "UPDATE BBSPLANT SET bbsPlantHit = ? WHERE bbsPlantID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsPlantHit + 1);
			pstmt.setInt(2, bbsPlantID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int updateHitMod(int bbsPlantID) {
		String SQL = "UPDATE BBSPLANT SET bbsPlantHit = bbsPlantHit-2 WHERE bbsPlantID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsPlantID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int update(int bbsPlantID, String bbsPlantTitle, String bbsPlantContent) {  
		String SQL = "UPDATE BBSPLANT SET bbsPlantTitle = ?, bbsPlantContent = ? , "
				+ "bbsPlantDate = ? WHERE bbsPlantID = ?";  //Ư���� bbsID�� ���� ����� ���� ���� 
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql���ڸ� �����غ�ܰ� ���·�
			pstmt.setString(1, bbsPlantTitle);  
			pstmt.setString(2, bbsPlantContent);
			pstmt.setString(3, getDate());
			pstmt.setInt(4, bbsPlantID);
			updateHitMod(bbsPlantID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  //�����ͺ��̽� ����
	}
	
	public int delete(int bbsPlantID) {
		String SQL = "UPDATE BBSPLANT SET bbsPlantAvailable = 0 WHERE bbsPlantID = ?";  //�� �����ص� ����� ����
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql���ڸ� �����غ�ܰ� ���·�
			pstmt.setInt(1, bbsPlantID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  //�����ͺ��̽� ����
	}
}
	
	

