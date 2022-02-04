<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>

<html>
	<head>
		<title> <%=request.getAttribute("tempPrograma")%> </title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<link href="imagens/favicon.png" TYPE="image/gif" REL="icon">
		<style type="text/css">
			@import url('./css/Principal.css');
		</style>
		
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	</head>
	<body class="fundo">
		
			<%@ include file="/CabecalhoPublico.html" %>
  			<div id="divCorpo" class="divCorpo" >
	  		<div class="area"><h2>&raquo; <%=request.getAttribute("tempPrograma")%> </h2></div>
			<form action="EstatisticaPublica" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input id="PassoBusca" name="PassoBusca" type="hidden" value="<%=request.getAttribute("PassoBusca")%>"/>
			
			<!-- INÍCIO DA LISTA DE DE TIPOS SERVENTIAS COM PROJUDI INSTALADO NO ESTADO -->
			<div id="divTabela" class="divTabela"> 
				<table id="Tabela" class="Tabela">
					<thead width="50%">
						<tr>
							<th colspan="6">QUANTITATIVO DOS TIPOS DE SERVENTIAS COM PROCESSO JUDICIAL DIGITAL INSTALADO NO ESTADO</th>
						</tr>
						<tr>
							<th width="25%">&nbsp;</th>
							<th width="40%">Tipo de Serentia</th>
							<th width="10%">Quantidade</th>
							<th width="25%">&nbsp;</th>
						</tr>
						</thead>
					<tbody id="tabListaServentiasEstado" width="50%">
					<%
	  				List liTemp = (List)request.getAttribute("listaServentiasEstado");
	 				HashMap objTemp = new HashMap();
	  				boolean boLinha=false; 
	  				if(liTemp!=null){ 
		  				for(int i = 0 ; i< liTemp.size();i++) {
		      				objTemp = (HashMap)liTemp.get(i); 
		      				%>
		  					<tr class="TabelaLinha<%=(boLinha?1:2)%>">
								<th width="25%">&nbsp;</th>
								<th width="40%"><%=objTemp.get("ServentiaTipo")%></th>
								<th width="10%" align="center"><%=objTemp.get("Quantidade")%></th>
								<th width="25%">&nbsp;</th>
							</tr>
							<%
							boLinha = !boLinha;
							%>
						<%}
					}%>
					</tbody>
				</table>
			</div> 
			<!-- FIM DA LISTA DE DE TIPOS SERVENTIAS COM PROJUDI INSTALADO NO ESTADO -->
			
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>