<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
   
<jsp:useBean id="paginaInicialDt" class="br.gov.go.tj.projudi.dt.PaginaInicialDt" scope="request" />

<%@page import="java.util.Iterator"%>
<%@page import="br.gov.go.tj.projudi.dt.ListaPendenciaDt"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ArquivoBancoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ListaDadosServentiaDt"%>
<html>
	<head>
		<title>Processo Judicial</title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
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
					<%=Funcoes.saudacaoDia()%> <span class="bold"><%=usuario.getNome()%></span>! Seja bem vindo ao <span class="bold">Processo Judicial</span><br>
				
					Serventia:
					<span class="bold"><%= usuario.getServentia() %> &nbsp;&nbsp;&nbsp; </span><br>
					<% if (usuario.getCargoTipo() != null && !usuario.getCargoTipo().equalsIgnoreCase("")){ %>
					Cargo:
					<span class="bold"> <%=usuario.getCargoTipo() %></span></p>
				
				<% } %>
			<%} %>
			
			<% if (paginaInicialDt != null){ %>
			<br />
			<br />
			<fieldset id="VisualizaDados" class="VisualizaDados">
				<legend>Estatística Diária</legend>
				
				<div>5 Últimos Arquivos Recebidos Caixa (RAJADA)</div>
				<span>
					<table class="Tabela">
						<thead>
							<tr>
								<th>Nome Arquivo</th>
								<th>Data e Hora Processamento</th>
								<th>Processado Com Sucesso?</th>
							</tr>
						</thead>
						<tbody>
						<%if( paginaInicialDt.getListaUltimos5ArquivosRecebidoCaixa10Minutos() != null && paginaInicialDt.getListaUltimos5ArquivosRecebidoCaixa10Minutos().size() > 0 ) {%>
							<%for(ArquivoBancoDt arquivoBancoDt: paginaInicialDt.getListaUltimos5ArquivosRecebidoCaixa10Minutos()) { %>
								<tr>
									<td>
										<%=arquivoBancoDt.getArquivoBanco()%>
									</td>
									<td>
										<%=arquivoBancoDt.getDataRealizacao()%>
									</td>
									<td>
										<%=arquivoBancoDt.getLabelErroProcessamento()%>
									</td>
								</tr>
							<%} %>
						<%}
						else {%>
							<tr>
								<td colspan="3">Nenhum arquivo encontrado nos últimos 7 dias</td>
							</tr>
						<%} %>
						</tbody>
					</table>
				</span>
			</fieldset>
			
			<fieldset id="VisualizaDados" class="VisualizaDados">
				<legend>Dados sobre Boletos Emitidos Hoje</legend>
				
<!-- 				<div>Quantidade Emitidos Hoje</div> -->
<!-- 				<span class="span1"> -->
<%-- 					<%=paginaInicialDt.getQuantidadeBoletosEmitidosHoje() %> --%>
<!-- 				</span> -->
				
<!-- 				<br /> -->
<!-- 				<br /> -->
<!-- 				<br /> -->
				
				<div>Hora Último Boleto Emitido</div>
				<span class="span1">
					<%if( paginaInicialDt.getLogUltimoBoletoEmitidoHoje() != null ) {%>
						<%=paginaInicialDt.getLogUltimoBoletoEmitidoHoje().getHora() %>
					<%}%>
				</span>
				
				<br />
				<br />
			</fieldset>
		<%}%>
		</div>
		<%@ include file="../jsptjgo/Padroes/Mensagens.jspf"%>
	</body>
</html>