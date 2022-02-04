<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
   
<jsp:useBean id="paginaInicialDt" class="br.gov.go.tj.projudi.dt.PaginaInicialDt" scope="request" />

<%@page import="java.util.Iterator"%>
<%@page import="br.gov.go.tj.projudi.dt.ListaPendenciaDt"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ListaDadosServentiaDt"%>
<html>
	<head>
		<title>Processo Judicial</title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
		<style type="text/css">
			@import url('./css/Principal.css');
		</style>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
   		<script type='text/javascript' src='./js/Funcoes.js'></script>
	</head>
	
	<body>
		<div class="divCorpo">
			<% UsuarioDt usuario = (UsuarioDt)request.getSession().getAttribute("UsuarioSessaoDt");	%>
			
			<%if( usuario != null ) { %>
				<div class="area"><h2><i class="fa fa-user"></i> &Aacute;rea do <%=GrupoDt.getAtividadeUsuario(usuario.getGrupoCodigo())%></h2></div>
				<p class="mensagemBoasVindas">
					<%=Funcoes.saudacaoDia()%> <span class="bold"> <%=usuario.getNome()%></span>! Seja bem vindo ao <span class="bold">Processo Judicial</span><br/>
				
					Serventia:
					<span class="bold"><%= usuario.getServentia() %> &nbsp;&nbsp;&nbsp; </span><br/>
					<% if (usuario.getCargoTipo() != null && !usuario.getCargoTipo().equalsIgnoreCase("")){ %>
					Cargo:
					<span class="bold"> <%=usuario.getCargoTipo() %></span></p>
				
				<% } %>
			<%} %>
		
			<% if (paginaInicialDt != null){ %>
			<fieldset class="fieldEdicaoEscuro">
				<legend>Afastamentos</legend>
				
				<table class="Tabela">
					<thead>
						<tr>
							<th width="50%">Tipo</th>
							<th>Quantidade</th>
						</tr>
					</thead>
					<tbody>
						<tr>						
							<td>Afastamentos Abertos</td>
							<td align="center">	
									<% if(paginaInicialDt.getQtdAfastamentoOficiaisAbertos() > 0) { %>							
											<a href="UsuarioServentiaAfastamento?PaginaAtual=<%=Configuracao.Localizar%>"><%=paginaInicialDt.getQtdAfastamentoOficiaisAbertos()%></a>
									<% } else { %>
											<font color="DarkBlue"><%=paginaInicialDt.getQtdAfastamentoOficiaisAbertos()%></font>
									<% } %>
							</td>
							
						</tr>
					</tbody>					
				</table>
			</fieldset>	
												
			<br />
			<% 
		}%>
		</div>
		<%@ include file="../jsptjgo/Padroes/Mensagens.jspf"%>
	</body>
</html>