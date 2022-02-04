<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.MandadoJudicialDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>
<%@page import="java.util.*"%>

<style type="text/css">
@import url('./css/Principal.css');

@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
</style>

<jsp:useBean id="Pendenciadt" scope="session"
	class="br.gov.go.tj.projudi.dt.PendenciaDt" />
<jsp:useBean id="MandadoJudicialdt" scope="session"
	class="br.gov.go.tj.projudi.dt.MandadoJudicialDt" />

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title>Alterar Data Limite do Mandado</title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
	<script type="text/javascript" src="js/jquery.js"> </script>
	<script type='text/javascript' src='js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>   
	<script type='text/javascript' src='js/checks.js'></script>  

<%--
	
	//Merge
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title>Alterar Data Limite do Mandado</title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
	<script type="text/javascript" src="js/jquery.js"> </script>
	<script type='text/javascript' src='js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>   
	<script type='text/javascript' src='js/checks.js'></script>  

--%>

</head>
<body>
	<div id="divCorpo" class="divCorpo">
		<div id="divEditar" class="divEditar">
			<fieldset class="fieldEdicaoEscuro">
				<legend>Alterar Data Limite do Mandado</legend>

				<form action="MandadoJudicial" method="post" name="Formulario"
					id="Formulario">
					<input id="PaginaAtual" name="PaginaAtual" type="hidden"
						value="<%=Configuracao.Curinga9%>" /> <input name="__Pedido__"
						type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
					<input name="TituloPagina" type="hidden"
						value="<%=request.getAttribute("tempTituloPagina")%>" /> <input
						name="Fluxo" id="Fluxo" type="hidden"
						value="<%=request.getAttribute("Fluxo")%>" />
					<fieldset class="formLocalizar">
						<legend>Consulta</legend>

						<div style="display: inline; float: left; padding-right: 15px;">
							<label class="formEdicaoLabel">N&uacute;mero do Mandado:</label><br />
							<input class="formEdicaoInput" name="NumeroMandado"
								id="NumeroMandado" type="text"
								value="<%=MandadoJudicialdt == null ? "" : MandadoJudicialdt.getId()%>"
								onkeypress="return DigitarSoNumero(this, event)"
								<%=MandadoJudicialdt.getDataLimite() != null && !MandadoJudicialdt.getDataLimite().isEmpty()
					? "disabled"
					: ""%> />
						</div>

						<div style="display: inline; float: left;">
							<label class="formEdicaoLabel">Data limite Atual:</label><br /> <input
								class="formEdicaoInput" id="dataLimite" name="dataLimite"
								type="text"
								value="<%=Funcoes.FormatarData(MandadoJudicialdt.getDataLimite())%>"
								disabled /><br />
						</div>

						<br /> <br />

						<%
							if (MandadoJudicialdt.getDataLimite() != null && !MandadoJudicialdt.getDataLimite().isEmpty()) {
						%>

						<label class="formEdicaoLabel" for="novaDataLimite">*Nova
							Data Limite </label><br> <input class="formEdicaoInput"
							name="novaDataLimite" id="novaDataLimite" type="text" size="10"
							maxlength="10"
							value="<%=request.getAttribute("novaDataLimite") != null ? request.getAttribute("novaDataLimite") : ""%>"
							onkeyup="mascara_data(this)" onblur="verifica_data(this)">
						<img id="calendarioNovaDataLimite"
							src="./imagens/dlcalendar_2.gif" height="13" width="13"
							title="Calendário" alt="Calendário"
							onclick="displayCalendar(document.forms[0].novaDataLimite,'dd/mm/yyyy',this)" />
						<br />


						<%
							}
						%>

						<%
							if (request.getAttribute("Fluxo") != null && "inicioAlterarData".equals(request.getAttribute("Fluxo"))) {
						%>
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<button type="submit" name="consultar" value="Consultar"
								onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga9%>');AlterarValue('Fluxo','consultar')">Consultar</button>
						</div>
						<%
							}
						%>

						<%
							if (request.getAttribute("Fluxo") != null
									&& "resultadoConsultaAlterarData".equals(request.getAttribute("Fluxo"))) {
						%>
						<div id="divBotoesCentralizados2" class="divBotoesCentralizados">
							<button type="submit" name="salvar" value="Salvar"
								onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('Fluxo','salvarAlterarDataLimite')">Salvar</button>
							<button type="submit" name="voltar" value="Voltar"
								onclick="AlterarValue('PaginaAtual',''<%=Configuracao.Curinga9%>');AlterarValue('Fluxo','inicioAlterarData');">Voltar</button>
						</div>
						<%
							}
						%>

						<br />

						<div class="divClear"></div>

						<div id="divTabela" class="divTabela">
							<%
								if (request.getAttribute("ListaMandados") != null) {
							%>
							<table id="Tabela" class="Tabela">
								<thead>
									<tr class="TituloColuna">
										<td width="5" class="Centralizado"><input type="checkbox"
											id="chkSelTodos" value=""
											onclick="atualizarChecks(this, 'divTabela')"
											title="Alterar os estados de todos os itens da lista" /></td>
										<td>N&uacute;mero</td>
										<td>Oficial</td>
										<td width="150px">Data Distribuição</td>
									</tr>
								</thead>
								<tbody id="tabListaMandado">
									<%
										List liTemp = (List) request.getAttribute("ListaMandados");
											MandadoJudicialDt mandadoDt;
											String processoNumero = "";
											boolean boLinha = false;
											if (liTemp != null)
												for (int i = 0; i < liTemp.size(); i++) {
													mandadoDt = (MandadoJudicialDt) liTemp.get(i);
									%>
									<tr class="TabelaLinha<%=(boLinha ? 1 : 2)%>">
										<td class="Centralizado"><input
											class="formEdicaoCheckBox" name="mandados" type="checkbox"
											value="<%=mandadoDt.getId()%>"></td>
										<td><%=mandadoDt.getId()%></td>
										<td><%=mandadoDt.getUsuarioServentiaDt_1().getNome()%></td>
										<td><%=mandadoDt.getDataDistribuicao()%></td>
									</tr>
									<%
										}
									%>
								</tbody>
							</table>
							<div id="divBotoesCentralizados" class="divBotoesCentralizados">
								<button type="submit" name="redistribuir" value="Redistribuir"
									onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');AlterarValue('Fluxo','redistribuir')">Redistribuir</button>
							</div>
							<%
								}
							%>
						</div>
					</fieldset>

					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
				</form>
			</fieldset>
		</div>
	</div>
	<%@ include file="Padroes/Mensagens.jspf"%>
</body>
</html>
