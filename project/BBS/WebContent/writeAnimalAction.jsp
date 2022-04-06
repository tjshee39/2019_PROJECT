<%@ page language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"%>
 
<%@ page import="bbsanimal.BbsAnimalDAO" %>  <!-- 이거 쓸거임 -->
<%@ page import="bbs.BbsDAO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8");%>   <!-- 건너오는 모든 데이터를 UTR-8로 받음 -->

<jsp:useBean id="bbsanimal" class="bbsanimal.BbsAnimal" scope="page" />   <!-- 하나의 게시글 인스턴스 -->
<jsp:useBean id="bbs" class="bbs.Bbs" scope="page" />
<jsp:setProperty name="bbsanimal" property="bbsAnimalTitle" />
<jsp:setProperty name="bbsanimal" property="bbsAnimalContent" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<title>JSP 웹사이트</title>
</head>
<body>
 
 <%
 	String userID = null;  //로그인 된 사람은 들어갈 수 없음
	if (session.getAttribute("userID") != null) {
		userID = (String) session.getAttribute("userID");  //userID라는 변수가 할당된 세션아이디를 담을 수 있도록 해줌
	}
	if (userID == null) {  //로그인 된 상태에서만 글쓰기 가능
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('로그인 하세요.')");
		script.println("location.href = 'login.jsp");   //login.jsp로 넘어가기
		script.println("</script>");
	} else {
		if (bbsanimal.getBbsAnimalTitle() == null || bbsanimal.getBbsAnimalContent() == null){  //제목이나 내용 입력x
		    	PrintWriter script = response.getWriter();
		    	script.println("<script>");
		    	script.println("alert('입력이 되지 않은 사항이 있습니다.')");
		    	script.println("history.back()");
		    	script.println("</script>");     
		    } else {
				BbsAnimalDAO bbsAnimalDAO = new BbsAnimalDAO();   //데이터베이스에 접근 가능한 객체
				int result = bbsAnimalDAO.write(bbsanimal.getBbsAnimalTitle(), userID, 
						bbsanimal.getBbsAnimalContent()); 
				if (result == -1){
				   PrintWriter script = response.getWriter();
				   script.println("<script>");
			   	   script.println("alert('글쓰기에 실패했습니다.')");
			   	   script.println("history.back()");
			   	   script.println("</script>");   
			  	   }
				else {  
				   //session.setAttribute("userID", bbs.getUserID());  //얘때문에 글쓰고 나면 로그아웃된 것 같음
			   	   PrintWriter script = response.getWriter();
			   	   script.println("<script>");
			   	   script.println("location.href = 'bbsAnimal.jsp'");
			   	   script.println("</script>");
			  	   }
		    }
	} 
 %>
 
 
</body>
</html>