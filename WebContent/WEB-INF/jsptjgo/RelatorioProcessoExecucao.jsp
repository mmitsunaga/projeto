<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.DiaHoraEventos"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.RelatorioExecpenwebDt"%><html>
<%@page import="br.gov.go.tj.projudi.dt.RegimeExecucaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.LocalCumprimentoPenaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PenaExecucaoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EventoExecucaoStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.FormaCumprimentoExecucaoDt"%>

	<head>
		<title>Processo Execução Penal</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
		<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
		<%@ include file="./js/Paginacao.js"%> 
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/DigitarData.js'></script>
		<script type='text/javascript' src='./js/jquery.mask.min.js'></script>
		<script type="text/javascript">
		$(document).ready(function($) {
			$("#dataInicialPeriodo").mask("99/99/9999");
			$("#dataFinalPeriodo").mask("99/99/9999");
			$("#Serventia").value = "";
		});
		</script>

	</head>

	<body>
  		<div id="divCorpo" class="divCorpo"> 
			<div class="area"><h2>&raquo; Relatórios EXECPENWEB </h2></div>
			
			<form action="RelatorioProcessoExecucao" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" /> 
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>"/>
				<div id="divEditar" class="divEditar">
				<br />
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda">Relatório EXECPENWEB - Lista de Processos</legend>
						<br />
						<table>
							<tr><td><input id="radio" name="radio" type="radio" value="1" onclick="Ocultar('divSituacaoAtual');Mostrar('divPeriodoConsulta')"/>Data Provável</td>
								<td style="margin-left: 10px">
									<div id="divDataProvavel">
									<input id="radioTipoConsulta" name="radioTipoConsulta" type="radio" value="PROGRESSAO"/>Progressão de Regime<br />
									<input id="radioTipoConsulta" name="radioTipoConsulta" type="radio" value="LIVRAMENTO"/>Livramento Condicional<br />
									<input id="radioTipoConsulta" name="radioTipoConsulta" type="radio" value="MANDADOPRISAO"/>Mandado de Prisão<br />
									</div>
								</td>
							</tr>
							<tr><td colspan="2"><input id="radio" name="radio" type="radio" value="2" onclick="Ocultar('divSituacaoAtual');Mostrar('divPeriodoConsulta');"/>Cálculo realizado no período<br /></td></tr>
							<tr><td colspan="2"><input id="radio" name="radio" type="radio" value="3" onclick="Ocultar('divPeriodoConsulta'); Ocultar('divSituacaoAtual')"/>Sem cálculo<br /></td></tr>
							<tr><td colspan="2"><input id="radio" name="radio" type="radio" value="6" onclick="Ocultar('divPeriodoConsulta'); Mostrar('divSituacaoAtual')"/>Situação atual do processo<br /></td></tr>
							<tr><td colspan="2"><input id="radio" name="radio" type="radio" value="7" onclick="Ocultar('divPeriodoConsulta'); Ocultar('divSituacaoAtual')"/>Processos ativos<br /></td></tr>
							<tr><td colspan="2"><input id="radio" name="radio" type="radio" value="13" onclick="Ocultar('divPeriodoConsulta'); Ocultar('divSituacaoAtual')"/>Processos ativos (maiores de 70 anos)<br /></td></tr>
							<tr><td colspan="2"><input id="radio" name="radio" type="radio" value="8" onclick="Ocultar('divPeriodoConsulta'); Ocultar('divSituacaoAtual')"/>Cálculo em atraso<br /></td></tr>
							<tr><td colspan="2"><input id="radio" name="radio" type="radio" value="9" onclick="Ocultar('divPeriodoConsulta'); Ocultar('divSituacaoAtual')"/>Provável Término da Pena anterior à data atual</td></tr>
							<tr><td colspan="2"><input id="radio" name="radio" type="radio" value="10" onclick="Ocultar('divPeriodoConsulta'); Ocultar('divSituacaoAtual')"/>Provável Progressão de Regime em atraso</td></tr>
							<tr><td colspan="2"><input id="radio" name="radio" type="radio" value="11" onclick="Ocultar('divPeriodoConsulta'); Ocultar('divSituacaoAtual')"/>Provável Livramento Condicional em atraso</td></tr>
							<tr><td colspan="2"><input id="radio" name="radio" type="radio" value="12" onclick="Ocultar('divPeriodoConsulta'); Ocultar('divSituacaoAtual')"/>Validade do Mandado de Prisão vencida e sentenciado foragido</td></tr>
						</table>
						<br /><br />
						<div id="divPeriodoConsulta" style="display: none;"> 
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Parâmetros da consulta</legend>
							<br />
							<table>
								<tr><td>Período da Consulta:</td>
									<td><input name="dataInicialPeriodo" id="dataInicialPeriodo" type="text" size="10" maxlength="10" value="" onblur="verifica_data(this);" onkeypress="return DigitarSoNumero(this, event)"/></td>
									<td style="text-align: center">à</td>
									<td><input name="dataFinalPeriodo" id="dataFinalPeriodo" type="text" size="10" maxlength="10" value="" onblur="verifica_data(this);" onkeypress="return DigitarSoNumero(this, event)"/></td>
								</tr>
							</table>
						</fieldset>
						</div>
						<div id="divSituacaoAtual" style="display: none;"> 
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Parâmetros da consulta</legend>
							<%@ include file="RelatorioExecpenwebSituacaoAtual.jsp"%>
						</fieldset>
						</div>
					</fieldset>

					<br />
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="btnConsultar" type="submit" value="Emitir Relatório" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>');getSituacaoAtual();"/> 
				    </div>
				</div>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>