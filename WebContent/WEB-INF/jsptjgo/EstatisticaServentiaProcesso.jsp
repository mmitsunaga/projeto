<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.AssuntoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="Assuntodt" scope="session" class= "br.gov.go.tj.projudi.dt.AssuntoDt"/>

<%-----%>
<%--<jsp:setProperty name="objAssunto" property="*"/>--%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Assunto  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
	
	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  


</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Estatística de Processos das Serventias</h2></div>

		<form action="EstatisticaServentia" method="post" name="Formulario" id="Formulario">

			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
    		<input name="Relatorio" type="hidden" value="0" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<a class="divPortaBotoesLink" href="Ajuda/AssuntoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
					<input type="submit" name="Relatorio" />
			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
