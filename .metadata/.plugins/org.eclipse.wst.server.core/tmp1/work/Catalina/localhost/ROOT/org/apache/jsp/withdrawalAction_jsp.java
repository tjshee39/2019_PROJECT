/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/8.5.50
 * Generated at: 2020-12-14 01:45:22 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import user.UserDAO;
import java.io.PrintWriter;

public final class withdrawalAction_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = new java.util.HashSet<>();
    _jspx_imports_classes.add("java.io.PrintWriter");
    _jspx_imports_classes.add("user.UserDAO");
  }

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
      throws java.io.IOException, javax.servlet.ServletException {

    final java.lang.String _jspx_method = request.getMethod();
    if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method) && !javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
      response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET POST or HEAD");
      return;
    }

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write(" \r\n");
      out.write("   <!-- 이거 쓸거임 -->\r\n");
      out.write("\r\n");
 request.setCharacterEncoding("UTF-8");
      out.write("   <!-- 건너오는 데이터를 모두 UTF-8로 받음 -->\r\n");
      out.write("\r\n");
      user.User user = null;
      user = (user.User) _jspx_page_context.getAttribute("user", javax.servlet.jsp.PageContext.PAGE_SCOPE);
      if (user == null){
        user = new user.User();
        _jspx_page_context.setAttribute("user", user, javax.servlet.jsp.PageContext.PAGE_SCOPE);
      }
      out.write("   <!-- 현재 페이지에서만 beans 쓸거임 -->\r\n");
      org.apache.jasper.runtime.JspRuntimeLibrary.introspecthelper(_jspx_page_context.findAttribute("user"), "userID", request.getParameter("userID"), request, "userID", false);
      out.write("   <!-- userID와 userPassword 받음 -->\r\n");
      org.apache.jasper.runtime.JspRuntimeLibrary.introspecthelper(_jspx_page_context.findAttribute("user"), "userPassword", request.getParameter("userPassword"), request, "userPassword", false);
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<!DOCTYPE html>\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<meta charset=\"UTF-8\">\r\n");
      out.write("\r\n");
      out.write("<title>JSP 웹사이트</title>\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write(" \r\n");
      out.write(" \t");

 	String userID = null;
 	String userName = null;
 	if (session.getAttribute("userID") != null) {
 		userID = (String) session.getAttribute("userID");  //userID라는 변수가 할당된 세션아이디를 담을 수 있도록 해줌
 	}
 	if (userID != null) {  //이미 로그인이 되어있으면 로그인페이지로 다시 넘어가지 못함
 		PrintWriter script = response.getWriter();
 		script.println("<script>");
 		script.println("alert('이미 로그인이 되어있습니다.')");
 		script.println("location.href = 'main.jsp");   //main.jsp로 넘어가기
 		script.println("</script>");
 	}
 	UserDAO userDAO = new UserDAO();
 	int result = userDAO.withdrawal(user.getUserID(), user.getUserPassword());  //로그인 시도: login.jsp에서 받음 정보를 함수에서 받음
 	if (result == 1){  //회원 탈퇴 성공
 		session.invalidate();  //현재 이 페이지에 접속한 회원이 세션 빼앗기게 만듦 > 로그아웃
 		PrintWriter script = response.getWriter();
 		script.println("<script>");
 		script.println("alert('회원 탈퇴를 완료하였습니다.')");
 		script.println("location.href = 'main.jsp'");   //main.jsp로 넘어가기
 		script.println("</script>");  
 		}
 	if (result == 0){   //비밀번호 오류
 		PrintWriter script = response.getWriter();
 		script.println("<script>");
 		script.println("alert('비밀번호가 틀립니다.')");
 		script.println("history.back()");   //이전페이지로 사용자 돌려보내기
 		script.println("</script>");  
 		}
 	if (result == -2){
 		PrintWriter script = response.getWriter();
 		script.println("<script>");
 		script.println("alert('데이터 베이스 오류가 발생하였습니다.')");
 		script.println("history.back()");
 		script.println("</script>");  
 		}
 	
      out.write("\r\n");
      out.write(" \t\r\n");
      out.write(" \t");

 		session.invalidate();  //현재 이 페이지에 접속한 회원이 세션 빼앗기게 만듦 > 로그아웃
 	
      out.write("\r\n");
      out.write(" <!-- \r\n");
      out.write(" myql jdbc driver: mysql과 jsp연결\r\n");
      out.write(" eclipse > WEB-INF > lib > bin.jar 복붙\r\n");
      out.write(" project 폴더 우클릭 > property > Java Build Path > Libraries\r\n");
      out.write(" > Add JARs > 추가했던 bin.jar(driver)선택 > 연동완료^~\r\n");
      out.write("  -->\r\n");
      out.write(" \r\n");
      out.write("</body>\r\n");
      out.write("</html>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
