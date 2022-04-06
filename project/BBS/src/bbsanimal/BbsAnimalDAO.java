package bbsanimal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsAnimalDAO {
	private Connection conn;   // connection: �����ͺ��̽��� ���� �����ϰ� ���ִ� ��ü
	private ResultSet rs;   // ��� ������ ���� �� �ִ� ��ü(DB data), ctrl + shift + o = auto import

	public BbsAnimalDAO() {  //class�̸��� method�̸� ����, DB����
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
		String SQL = "SELECT bbsAnimalID FROM BBSANIMAL ORDER BY bbsAnimalID DESC"; //BBS���̺����� ��������(bbsID����)���� ���� �������� ���� ���� ��ȣ ������
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
	
	public int write(String bbsAnimalTitle, String userID, String bbsAnimalContent) {  //�Խñ� db�� ����
		String SQL = "INSERT INTO BBSANIMAL VALUES (?, ?, ?, ?, ?, ?, ?)"; 
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql���ڸ� �����غ�ܰ� ���·�
			pstmt.setInt(1, getNext());  //�Խñ� ��ȣ
			pstmt.setString(2, bbsAnimalTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsAnimalContent);
			pstmt.setInt(6, 1);  //available = 1: ���� �ȵ�
			pstmt.setInt(7, 0);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  //�����ͺ��̽� ����
		}

	
	
	public ArrayList<BbsAnimal> getList(int pageNumber) {  //DB���� �� ��� �������� �ҽ��ڵ�� BbsDAO.java�� ����Ʈ�� ��� ��ȯ

		String SQL = "SELECT * FROM BBSANIMAL WHERE bbsAnimalID < ? AND bbsAnimalAvailable = 1 ORDER BY bbsAnimalID DESC LIMIT 10"; //���������� �� �ִ� 10��
		ArrayList<BbsAnimal> list  = new ArrayList<BbsAnimal>();  //Bbs��� Ŭ�������� ���� �ν��Ͻ��� ������ �� �ִ� ����Ʈ
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql���ڸ� �����غ�ܰ� ���·�
			pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
			rs = pstmt.executeQuery();  //������ �������� �� ������ ��� ������
			while (rs.next()) {
				BbsAnimal bbsanimal = new BbsAnimal();
				bbsanimal.setBbsAnimalID(rs.getInt(1));
				bbsanimal.setBbsAnimalTitle(rs.getString(2));
				bbsanimal.setUserID(rs.getString(3));
				bbsanimal.setBbsAnimalDate(rs.getString(4));
				bbsanimal.setBbsAnimalContent(rs.getString(5));
				bbsanimal.setBbsAnimalAvailable(rs.getInt(6));
				bbsanimal.setBbsAnimalHit(rs.getInt(7));
				list.add(bbsanimal);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;  //�Խñ� ����Ʈ ���
	}
	
	public boolean nextPage(int pageNumber) {  //����¡ ó��
		String SQL = "SELECT * FROM BBSANIMAL WHERE bbsAnimalID < ? AND bbsAnimalAvailable = 1"; //���������� �� �ִ� 10��
		ArrayList<BbsAnimal> list  = new ArrayList<BbsAnimal>();  //Bbs��� Ŭ�������� ���� �ν��Ͻ��� ������ �� �ִ� ����Ʈ
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
	
	public BbsAnimal getBbsAnimal(int bbsAnimalID) {  //�Խñ��� ���� �ҷ����� �Լ� (Ư���� id�� �ش��ϴ� �Խñ� ������)
		String SQL = "SELECT * FROM BBSANIMAL WHERE bbsAnimalID = ?";  //bbsID�� Ư���� ������ ���
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql���ڸ� �����غ�ܰ� ���·�
			pstmt.setInt(1, bbsAnimalID);
			rs = pstmt.executeQuery();  //������ �������� �� ������ ��� ������
			if (rs.next()) {
				BbsAnimal bbsanimal = new BbsAnimal();
				bbsanimal.setBbsAnimalID(rs.getInt(1));
				bbsanimal.setBbsAnimalTitle(rs.getString(2));
				bbsanimal.setUserID(rs.getString(3));
				bbsanimal.setBbsAnimalDate(rs.getString(4));
				bbsanimal.setBbsAnimalContent(rs.getString(5));
				bbsanimal.setBbsAnimalAvailable(rs.getInt(6));
				bbsanimal.setBbsAnimalHit(rs.getInt(7));
				updateHit(bbsAnimalID, bbsanimal.getBbsAnimalHit());
				
				return bbsanimal;  //�������� ������ ���� �� bbs�ν��Ͻ��� �־���
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;  
	}
	
	public int updateHit(int bbsAnimalID, int bbsAnimalHit) {
		String SQL = "UPDATE BBSANIMAL SET bbsAnimalHit = ? WHERE bbsAnimalID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsAnimalHit + 1);
			pstmt.setInt(2, bbsAnimalID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int updateHitMod(int bbsAnimalID) {
		String SQL = "UPDATE BBSANIMAL SET bbsAnimalHit = bbsAnimalHit-2 WHERE bbsAnimalID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsAnimalID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int update(int bbsAnimalID, String bbsAnimalTitle, String bbsAnimalContent) {  
		String SQL = "UPDATE BBSANIMAL SET bbsAnimalTitle = ?, bbsAnimalContent = ? , bbsAnimalDate = ? WHERE bbsAnimalID = ?";  //Ư���� bbsID�� ���� ����� ���� ���� 
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql���ڸ� �����غ�ܰ� ���·�
			pstmt.setString(1, bbsAnimalTitle);  
			pstmt.setString(2, bbsAnimalContent);
			pstmt.setString(3, getDate());
			pstmt.setInt(4, bbsAnimalID);
			updateHitMod(bbsAnimalID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  //�����ͺ��̽� ����
	}
	
	public int delete(int bbsAnimalID) {
		String SQL = "UPDATE BBSANIMAL SET bbsAnimalAvailable = 0 WHERE bbsAnimalID = ?";  //�� �����ص� ����� ����
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  //sql���ڸ� �����غ�ܰ� ���·�
			pstmt.setInt(1, bbsAnimalID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  //�����ͺ��̽� ����
	}

}