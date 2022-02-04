<%@page import="br.gov.go.tj.utils.TJDataHora"%>
<%@page import="br.gov.go.tj.projudi.ct.Controle"%>

<%
  Cookie cookiesErro[] = request.getCookies();
  String instanciaServidorErro = null;
  try {
  	if(cookiesErro != null){
		for(Cookie c: cookiesErro){
		  if(c.getName().equals("JSESSIONID")){
			  instanciaServidorErro = c.getValue();
			  instanciaServidorErro = instanciaServidorErro.substring(instanciaServidorErro.indexOf(".") + 1);
				break;
		  }
		}
	}
  } catch(Exception e){}
%>

<%@page contentType="text/html"%>
<%@page pageEncoding="LATIN1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<html>
    <head>        
        <title>Ocorreu um erro</title>
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <style type="text/css">
        	@import url("./css/Principal.css");
        </style>
    </head>

    <body>
	    <% if (request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica")) { %>
		<%@ include file="/CabecalhoPublico.html" %>
		<% } %>
		<div class="divCorpo">
		    <h1 class="titulo">
		    	<img src="./imagens/32x32/ico_erro.png" alt="Erro" />
		    	A opera&ccedil;&atilde;o retornou o seguinte erro
		    </h1>
		    
		    <div class="texto_erro">
		    	<%=request.getAttribute("Mensagem").toString().trim().replaceAll("\n", "<br />")%>
		    </div>
		    
		    <%if (request.getAttribute("Mensagem").toString().trim().equals("Erro n.  - Usurio invlido") == true){%><br />
		    	<div class="texto_sugestao">
		    		O sistema n&atilde;o detectou seu usu&aacute;rio, ent&atilde;o <b>efetue o login novamente</b>
		    	</div>
		    <%}%><br />
			<a href="javascript:history.go(-1)">Clique para voltar a p&aacute;gina anterior</a><br /><br />
			<h2 class="subtitulo">Procedimento</h2>
			<div class="obs">
				<ol>
					<li>Anote o n&uacute;mero do erro</li>
					<li>Informe ao atendente o n&uacute;mero do erro</li>
					<li>Data e Hora: <%=((new TJDataHora()).getDataFormatadaddMMyyyyHHmmss())%></li>
					<li>Instância: <%=instanciaServidorErro%></li>
					<li>IP: <%=Controle.getIpCliente(request)%></li>					
				</ol> 
			</div>	
		</div>
    </body>
</html>
