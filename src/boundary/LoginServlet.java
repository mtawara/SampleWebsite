package boundary;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import logiclayer.BaseLogic;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Map<String, Object> map = new HashMap<String, Object>();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// Logs the user in
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			BaseLogic base = new BaseLogic();
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			HttpSession session = request.getSession();

			// Make a session of the user
			if (session.getAttribute("username") == null) {
				session.setAttribute("username", username);
			}

			// Freemarker
			String templateDir = "templates";
			Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);
			String path = this.getServletContext().getRealPath(templateDir);
			cfg.setDirectoryForTemplateLoading(new File(path));
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
			cfg.setLogTemplateExceptions(false);
			Template temp = null;
			
			//Variables used in Ajax calls
			String account = request.getParameter("account");
			String logout = request.getParameter("logout");
			String deletion = request.getParameter("deletion");
			
			if (account != null && !account.isEmpty()){
				//used to display profile page when account button is clicked
				if(request.getParameter("account").equals("true")) {			
					temp = cfg.getTemplate("profile.html");
					this.map.put("items",  base.getItems(session.getAttribute("username").toString()));
					this.map.put("user", session.getAttribute("username").toString());
					temp.process(this.map, out);
				}
			} else if (logout!=null && !logout.isEmpty()){
				//used to logout & invalidate session
				if(logout.equals("true")) {
					session.invalidate();
				}
			} else if(deletion!=null && !deletion.isEmpty()){
				//used to delete items from a user's profile & refresh the items listed
				base.deleteItem(Integer.parseInt(deletion));
				temp = cfg.getTemplate("profile.html");
				this.map.put("items",  base.getItems(session.getAttribute("username").toString()));
				this.map.put("user", session.getAttribute("username").toString());
				temp.process(this.map, out);
			} else {
				//Runs when Login Servlet is called normally
				temp = cfg.getTemplate("profileB.html");
				String checkLogin = request.getParameter("checkLogin");
				//Checks if there is a session with a user logged in
				if (checkLogin != null && !checkLogin.isEmpty()) {
					if (request.getParameter("checkLogin").equals("true")) {
						if (session.getAttribute("username").toString() == null
								|| session.getAttribute("username").toString().isEmpty()) {
							out.println("0");
						} else {
							out.println("1");
						}
					}
				}
				//Checks if username and password had parameters
				//This code only runs when page before it was loginRegister.html
				if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
					if (base.login(username, password)) {
						// Log in was successful, takes user to profile page
						base.getItems(username);
						this.map.put("items", base.getItems(username));
						this.map.put("user", username);
					
					} else {
						// If log in was unsuccessful
						out.println("<script type=\"text/javascript\">");
						out.println("alert('Email and/or password were incorrect');");
						out.println("</script>");
						templateDir = "/";
						path = this.getServletContext().getRealPath(templateDir);
						cfg.setDirectoryForTemplateLoading(new File(path));
						cfg.setDefaultEncoding("UTF-8");
						cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
						cfg.setLogTemplateExceptions(false);
						temp = cfg.getTemplate("loginRegister.html");
					}
					temp.process(this.map, out);
				}		
			}
			
			base.disconnect();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
