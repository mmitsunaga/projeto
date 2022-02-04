<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.DiaHoraEventos"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.RelatorioExecpenwebDt"%><html>
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

			function getSituacaoAtual($){
				camposMarcados = new Array();
				$("input[type=checkbox][name='chkRegime[]']:checked").each(function($){
				    camposMarcados.push($(this).attr('id'));
				});
				document.getElementById("RegimeExecucao").value = camposMarcados;
				
				camposMarcados = new Array();
				$("input[type=checkbox][name='chkLocal[]']:checked").each(function($){
				    camposMarcados.push($(this).attr('id'));
				});
				document.getElementById("LocalCumprimentoPena").value = camposMarcados;

				camposMarcados = new Array();
				$("input[type=checkbox][name='chkModalidade[]']:checked").each(function($){
				    camposMarcados.push($(this).attr('id'));
				});
				document.getElementById("Modalidade").value = camposMarcados;

				camposMarcados = new Array();
				$("input[type=checkbox][name='chkStatus[]']:checked").each(function($){
				    camposMarcados.push($(this).attr('id'));
				});
				document.getElementById("EventoExecucaoStatus").value = camposMarcados;

				camposMarcados = new Array();
				$("input[type=checkbox][name='chkFormaCumprimento[]']:checked").each(function($){
				    camposMarcados.push($(this).attr('id'));
				});
				document.getElementById("FormaCumprimento").value = camposMarcados;
			}
		</script>
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo"> 
			<div class="area"><h2>&raquo; Relatórios EXECPENWEB </h2></div>
			
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" /> 
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>"/>
				<div id="divEditar" class="divEditar">
				<br />
				<fieldset class="formEdicao"> 
				    <legend class="formEdicaoLegenda">Relatório EXECPENWEB por Serventia</legend>
					<br />
				    <label class="formEdicaoLabel" for="Id_Serventia">Serventia
					<input id="Id_Serventia" name="Id_Serventia" type="hidden" value="<%=request.getAttribute("Id_Serventia")%>"/> 
					<input class="FormEdicaoimgLocalizar" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
					<input class="FormEdicaoimgLocalizar" name="imaLimparServentia" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Serventia','Serventia'); return false;" title="Limpar Serventia">					
					</label><br>
					<input  class="formEdicaoInputSomenteLeitura" id="Serventia" name="Serventia" readonly type="text" size="60" maxlength="60" value="<%=request.getAttribute("Serventia")%>"/><br />

					<label class="formEdicaoLabel" for="Id_Serventia">Formato do relatório</label><br>
					<input id="tipoArquivo" name="tipoArquivo" type="radio" value="2" checked="checked"/>pdf
					<input id="tipoArquivo" name="tipoArquivo" type="radio" value="1"/>txt
					<br /><br />

					<fieldset>
						<legend class="formEdicaoLegenda">Total de Processos</legend>
						<table style="font-size:12px">
							<tr>
								<td><input id="radio" name="radio" type="radio" value="1" onclick="Ocultar('divSituacaoAtual')"/>Com cálculo no período</td>
								<td><input name="dataInicialPeriodo" id="dataInicialPeriodo" type="text" size="10" maxlength="10" value="" onblur="verifica_data(this);" onkeypress="return DigitarSoNumero(this, event)"/></td>
								<td>à</td>
								<td><input name="dataFinalPeriodo" id="dataFinalPeriodo" type="text" size="10" maxlength="10" value="" onblur="verifica_data(this);" onkeypress="return DigitarSoNumero(this, event)"/></td>
							</tr>
							<tr><td><input id="radio" name="radio" type="radio" value="2" onclick="Ocultar('divSituacaoAtual')" />Cálculo em atraso e sem cálculo</td></tr>
							<tr><td><input id="radio" name="radio" type="radio" value="4" onclick="Ocultar('divSituacaoAtual')"/>Processos por Regime/Modalidade</td>
							</tr>
							<tr><td><input id="radio" name="radio" type="radio" value="5" onclick="Ocultar('divSituacaoAtual')"/>Processos por Serventia</td></tr>
						</table>
					</fieldset>
					<br /><br />
					<fieldset> 
						<legend class="formEdicaoLegenda">Lista de Processos:</legend>
						<table style="font-size:12px">
							<tr><td><input id="radio" name="radio" type="radio" value="6" onclick="Ocultar('divSituacaoAtual')"/>Cálculo em atraso e sem cálculo<br /></td></tr>
							<tr><td><input id="radio" name="radio" type="radio" value="7" onclick="Ocultar('divSituacaoAtual')"/>Provável Término da Pena anterior à data atual</td></tr>
							<tr><td><input id="radio" name="radio" type="radio" value="8" onclick="Ocultar('divSituacaoAtual')"/>Provável Progressão de Regime em atraso</td></tr>
							<tr><td><input id="radio" name="radio" type="radio" value="9" onclick="Ocultar('divSituacaoAtual')"/>Provável Livramento Condicional em atraso</td></tr>
							<tr><td><input id="radio" name="radio" type="radio" value="10" onclick="Ocultar('divSituacaoAtual')"/>Validade do Mandado de Prisão vencida e sentenciado foragido</td></tr>
							<tr><td><input id="radio" name="radio" type="radio" value="11" onclick="Ocultar('divSituacaoAtual')"/>Processos ativos na serventia (somente .txt)</td></tr>
							<tr><td><input id="radio" name="radio" type="radio" value="12" onclick="Ocultar('divSituacaoAtual')"/>Processo eletrônico ativo na serventia (somente .txt)</td></tr>
							<tr><td><input id="radio" name="radio" type="radio" value="13" onclick="Mostrar('divSituacaoAtual')"/>Situação atual dos processos</td></tr>
						</table>

						<div id="divSituacaoAtual" style="display: none;">
							<fieldset class="formEdicao">
							<%@ include file="RelatorioExecpenwebSituacaoAtual.jsp"%>
							</fieldset> 
						</div>
					</fieldset>
				</fieldset>
					<br />
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="btnConsultar" type="submit" value="Emitir Relatório" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Imprimir%>');getSituacaoAtual();"/> 
				    </div>
				</div>
			</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>