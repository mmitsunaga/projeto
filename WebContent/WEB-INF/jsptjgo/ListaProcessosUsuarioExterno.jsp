<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt" %>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html>
	<head>
		<title>Busca de Processo</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/menusimples.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/checks.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
      	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo" >
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoBusca" name="PassoBusca" type="hidden" value="<%=request.getAttribute("PassoBusca")%>">
				<input id="TipoConsulta" name="TipoConsulta" type="hidden" value="<%=request.getAttribute("TipoConsulta")%>" />
				<input id="RedirecionaOutraServentia" name="RedirecionaOutraServentia" type="hidden" value="" />
				<input id="QuantidadeRegistrosPagina" name="QuantidadeRegistrosPagina" type="hidden" value="<%=request.getAttribute("QuantidadeRegistrosPagina")%>" />
				
				<div class="area"><h2>&raquo; Consulta de Processos </h2></div>
				<div id="divLocalizar" class="divLocalizar"> 
				
					<input type="hidden" id="Id_Processo" name="Id_Processo">
					<input type="hidden" id="Id_Processo_Peticionar" name="Id_Processo_Peticionar">
					
					<div align="left">
						<% 	if (UsuarioSessao.isPodePeticionar()){ %>
						<input name="imgMultipla" type="submit" value="Peticionamento em Lote" onclick="AlterarAction('Formulario','Peticionamento');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('TipoConsulta','<%=request.getAttribute("TipoConsulta")%>');AlterarValue('Id_Processo_Peticionar','<%=""%>');"/>
						<% } if (UsuarioSessao.isPodeTrocarResponsavel()){ %>
						<input name="imgMultipla" type="submit" value="Trocar Respons?vel" onclick="AlterarAction('Formulario','Movimentacao');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('RedirecionaOutraServentia','6');"/>
						<% } %>
					</div>
															
					<%@ include file="ListaProcessosUsuariosExternos.jspf"%>
				</div> 
				<%@ include file="Padroes/PaginacaoProcessoUsuarioExterno.jspf"%>
			</form>
			
			<%@ include file="Padroes/Mensagens.jspf" %>
			
		</div>
	</body>
</html>
