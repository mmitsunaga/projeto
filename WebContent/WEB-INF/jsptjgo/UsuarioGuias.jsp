<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.GuiaEmissaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.ne.GuiaEmissaoNe"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<title>Insert title here</title>
	<style type="text/css">
	     @import url('./css/Principal.css');
	     @import url('./css/Paginacao.css');
	     #bkg_projudi { display: none;}
	</style>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<link type='text/css' rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen' />
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
</head>

<body>

	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Guias do Usuário </h2></div>
		
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="UsuarioGuias" id=""ProcessoGuias"" />
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />

			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao">
                	<legend class="formEdicaoLegenda">Guias do Usuário</legend>
                	<br />
                	
                	<fieldset id="VisualizaDados" class="VisualizaDados">
                		<legend>Guias</legend>
                		
						<table id="Tabela" class="Tabela">
							<thead>
								<tr>
									<th>Nº</th>
									<th>Número Guia</th>
									<th>Tipo Guia</th>
									<th>Data Emissão</th>
									<th>Data Vencimento</th>
									<th>Data Recebimento</th>
									<th>Situação</th>
									<th>Vinculado à Processo?</th>
								</tr>
							</thead>
							<tbody id="tabListaEscala">
							<%
							List liTemp = (List)request.getAttribute("ListaGuiaEmissao");
							GuiaEmissaoDt objTemp = null;
							if( liTemp != null && liTemp.size() > 0 ) {
								for(int i = 0 ; i< liTemp.size();i++) {
									objTemp = (GuiaEmissaoDt)liTemp.get(i);%>
									<tr>
										<td align="center"><%=(i + 1)%></td>
										
										<td align="center">
											<a href="<%=request.getAttribute("tempRetorno")%>?PaginaAtual=<%=Configuracao.Curinga6%>&PassoEditar=<%=Configuracao.Curinga8%>&Id_GuiaEmissao=<%=objTemp.getId()%>&visualizarBotaoVoltar=<%=GuiaEmissaoNe.APRESENTAR_BOTAO_VOLTAR%>&hash=<%=Funcoes.GeraHashMd5(objTemp.getId() + GuiaEmissaoNe.NUMERO_SERIE_GUIA)%>">
												<%=Funcoes.FormatarNumeroSerieGuia(objTemp.getNumeroGuiaCompleto())%>
											</a>
										</td>
										
										<td>
											<%=objTemp.getGuiaModeloDt().getGuiaTipo() %>
										</td>
										
										<td align="center">
											<%=Funcoes.FormatarDataHora(objTemp.getDataEmissao())%>
										</td>
										
										<td align="center">
											<%=Funcoes.TelaData(objTemp.getDataVencimento())%>
										</td>
										
										<td align="center">
											<%=Funcoes.FormatarDataHora(objTemp.getDataRecebimento())%>
										</td>
										
										<td align="center">
											<%=objTemp.getGuiaStatus() %>
										</td>
										
										<td align="center">
											<%if( objTemp.getId_Processo() != null && objTemp.getId_Processo().length() > 0 ) {%>
												<a href="BuscaProcesso?Id_Processo=<%=objTemp.getId_Processo()%>&PassoBusca=2">SIM</a>
											<%}
											else {%>
												NÃO
											<%}%>
										</td>
									</tr>
							<%
								}
							}
							else {
								%>
								<tr>
									<td colspan="5">
										<em> Nenhuma Guia Emitida pelo Processo Judicial Digital Localizada. </em>
									</td>
								</tr>
								<%
							}
							%>
							</tbody>
						</table>
					</fieldset>
					
                </fieldset>
			</div>
			
		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</div>

</body>

</html>