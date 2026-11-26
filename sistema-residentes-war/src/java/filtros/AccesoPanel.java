package filtros;
import java.io.IOException;import javax.servlet.*;import javax.servlet.annotation.WebFilter;import javax.servlet.http.*;import modelo.Usuario;
@WebFilter("/vistas/*") public class AccesoPanel implements Filter {
 public void init(FilterConfig config){}public void destroy(){}
 public void doFilter(ServletRequest req,ServletResponse res,FilterChain chain)throws IOException,ServletException{
 HttpServletRequest r=(HttpServletRequest)req;HttpServletResponse s=(HttpServletResponse)res;
 HttpSession sesion=r.getSession(false);Usuario u=sesion==null?null:(Usuario)sesion.getAttribute("usuarioPanel");
 if(u==null){
 String login=r.getContextPath()+"/login.xhtml";
 if("partial/ajax".equals(r.getHeader("Faces-Request"))){s.setContentType("text/xml;charset=UTF-8");s.getWriter().write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><partial-response><redirect url=\""+login+"\"/></partial-response>");}
 else{s.sendRedirect(login);}return;}
 if(!"Administrador".equals(u.getRol())&&!"Vinculación".equals(u.getRol())){s.sendError(403);return;}
 if((r.getRequestURI().contains("/administracion/")||r.getRequestURI().contains("/gestionEgresados.xhtml"))&&!"Administrador".equals(u.getRol())){s.sendError(403);return;}
 chain.doFilter(req,res);
 }
}
