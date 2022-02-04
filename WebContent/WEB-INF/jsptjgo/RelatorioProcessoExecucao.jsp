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
		<title>Processo Execu��o Penal</title>
	
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
			<div class="area"><h2>&raquo; Relat�rios EXECPENWEB </h2></div>
			
			<form action="RelatorioProcessoExecucao" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" /> 
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>"/>
				<div id="divEditar" class="divEditar">
				<br />
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda">Relat�rio EXECPENWEB - Lista de Processos</legend>
						<br />
						<table>
							<tr><td><input id="radio" name="radio" type="radio" value="1" onclick="Ocultar('divSituacaoAtual');Mostrar('divPeriodoConsulta')"/>Data Prov�vel</td>
								<td style="margin-left: 10px">
									<div id="divDataProvavel">
									<input id="radioTipoConsulta" name="radioTipoConsulta" type="radio" value="PROGRESSAO"/>Progress�o de Regime<br />
									<input id="radioTipoConsulta" name="radioTipoConsulta" type="radio" value="LIVRAMENTO"/>Livramento Condicional<br />
									<input id="radioTipoConsulta" name="radioTipoConsulta" type="radio" value="MANDADOPRISAO"/>Mandado de Pris�o<br />
									</div>
								</td>
							</tr>
							<tr><td colspan="2"><input id="radio" name="radio" type="radio" value="2" onclick="Ocultar('divSituacaoAtual');Mostrar('divPeriodoConsulta');"/>C�lculo realizado no per�odo<br /></td></tr>
							<tr><td colspan="2"><input id="radio" name="radio" type="radio" value="3" onclick="Ocultar('divPeriodoConsulta'); Ocultar('divSituacaoAtual')"/>Sem c�lculo<br /></td></tr>
							<tr><td colspan="2"><input id="radio" name="radio" type="radio" value="6" onclick="Ocultar('divPeriodoConsulta'); Mostrar('divSituacaoAtual')"/>Situa��o atual do processo<br /></td></tr>
							<tr><td colspan="2"><input id="radio" name="radio" type="radio" value="7" onclick="Ocultar('divPeriodoConsulta'); Ocultar('divSituacaoAtual')"/>Processos ativos<br /></td></tr>
							<tr><td colspan="2"><input id="radio" name="radio" type="radio" value="13" onclick="Ocultar('divPeriodoConsulta'); Ocultar('divSituacaoAtual')"/>Processos ativos (maiores de 70 anos)<br /></td></tr>
							<tr><td colspan="2"><input id="radio" name="radio" type="radio" value="8" onclick="Ocultar('divPeriodoConsulta'); Ocultar('divSituacaoAtual')"/>C�lculo em atraso<br /></td></tr>
							<tr><td colspan="2"><input id="radio" name="radio" type="radio" value="9" onclick="Ocultar('divPeriodoConsulta'); Ocultar('divSituacaoAtual')"/>Prov�vel T�rmino da Pena anterior � data atual</td></tr>
							<tr><td colspan="2"><input id="radio" name="radio" type="radio" value="10" onclick="Ocultar('divPeriodoConsulta'); Ocultar('divSituacaoAtual')"/>Prov�vel Progress�o de Regime em atraso</td></tr>
							<tr><td colspan="2"><input id="radio" name="radio" type="radio" value="11" onclick="Ocultar('divPeriodoConsulta'); Ocultar('divSituacaoAtual')"/>Prov�vel Livramento Condicional em atraso</td></tr>
							<tr><td colspan="2"><input id="radio" name="radio" type="radio" value="12" onclick="Ocultar('divPeriodoConsulta'); Ocultar('divSituacaoAtual')"/>Validade do Mandado de Pris�o vencida e sentenciado foragido</td></tr>
						</table>
						<br /><br />
						<div id="divPeriodoConsulta" style="display: none;"> 
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Par�metros da consulta</legend>
							<br />
							<table>
								<tr><td>Per�odo da Consulta:</td>
									<td><input name="dataInicialPeriodo" id="dataInicialPeriodo" type="text" size="10" maxlength="10" value="" onblur="verifica_data(this);" onkeypress="return DigitarSoNumero(this, event)"/></td>
									<td style="text-align: center">�</td>
									<td><input name="dataFinalPeriodo" id="dataFinalPeriodo" type="text" size="10" maxlength="10" value="" onblur="verifica_data(this);" onkeypress="return DigitarSoNumero(this, event)"/></td>
								</tr>
							</table>
						</fieldset>
						</div>
						<div id="divSituacaoAtual" style="display: none;"> 
						<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Par�metros da consulta</legend>
							<%@ include file="RelatorioExecpenwebSituacaoAtual.jsp"%>
						</fieldset>
						</div>
					</fieldset>

					<br />
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="btnConsultar" type="submit" value="Emitir Relat�rio" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>');getSituacaoAtual();"/> 
				    </div>
				</div>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>