<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.AreaDistribuicaoDt"%>
<jsp:useBean id="RelatorioPonteiroDistribuicaodt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.RelatorioPonteiroDistribuicaoDt"/>

<html>
	<head>
		<title> <%=request.getAttribute("tempPrograma")%> </title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
		</style>		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
		
		<script type='text/javascript'>
			<%@ include file="Padroes/PonteiroDistribuicao.jspf" %>
		</script>
	</head>
	<body onload="baixarRelatorio();">
		<div id="divCorpo" class="divCorpo">
	  		<div class="area"><h2>&raquo; <%=request.getAttribute("tempPrograma")%> </h2></div>
			<form action="RelatorioPonteiroDistribuicao" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo"  class="imgNovo" title="Nova Consulta" name="imaNovo" type="image" src="./imagens/imgNovo.png"  
						onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >
				<input id="imgImprimir"  class="imgImprimir" title="Gerar Relatório" name="imgImprimir" type="image" 
						src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">	

			</div/><br />
			<div id="divEditar" class="divEditar">
				
				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">Área de Distribuição</legend>
					<label class="formEdicaoLabel" for="Id_AreaDistribuicao">*Área de Distribuição
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarAreaDistribuicao" name="imaLocalizarAreaDistribuicao" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > 
					<input class="FormEdicaoimgLocalizar" id="imaLimparAreaDistribuicao" name="imaLimparAreaDistribuicao" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_AreaDistribuicao','AreaDistribuicao'); return false;" />
					</label><br>  
					
					<input id="Id_AreaDistribuicao" name="Id_AreaDistribuicao" type="hidden" value="<%=RelatorioPonteiroDistribuicaodt.getIdAreaDistribuicao()%>"/>
					<input class="formEdicaoInputSomenteLeitura" id="AreaDistribuicao" name="AreaDistribuicao" readonly="true" type="text" size="60" maxlength="60" value="<%=RelatorioPonteiroDistribuicaodt.getAreaDistribuicao()%>"/><br />
				</fieldset> 	
				
				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">Data de Referência</legend>
					<label class="formLocalizarLabel" for="Data_Verificacao">*Data de Referência</label><br> 
				    <input class="formLocalizarInput" name="Data_Verificacao" id="Data_Verificacao" type="text" size="10" maxlength="10" value="<%=RelatorioPonteiroDistribuicaodt.getDataVerificacao()%>" /> 
				    <img id="calendarioDataVerificacao" class="calendario" src="./imagens/dlcalendar_2.gif" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].Data_Verificacao,'dd/mm/yyyy',this)" />
					<br/>
					<label for="Aviso" style="float:left;margin-left:25px;" ><small>A data mínima deve ser 03/06/2016, data de entrada do ponteiro de distribuição.</small></label><br>
				</fieldset> 
				
				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">Tipo de Relatório</legend>
					<label class="formEdicaoLabel" for="tipo_Relatorio">*Tipo de Relatório</label><br>  
					<input type="radio" name="Tipo_Relatorio" id="Tipo_Relatorio" value="1" checked="true"/>Situação Atual do Ponteiro 
				    <input type="radio" name="Tipo_Relatorio" id="Tipo_Relatorio" value="6"/>Acompanhar Situação do Ponteiro
				    <input type="radio" name="Tipo_Relatorio" id="Tipo_Relatorio" value="2"/>Lançamentos do Ponteiro
				    <input type="radio" name="Tipo_Relatorio" id="Tipo_Relatorio" value="5"/>Acompanhar Lançamentos do Ponteiro
			    </fieldset>
				
				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">Tipo de Arquivo</legend>
					<label class="formEdicaoLabel" for="tipo_Arquivo">*Tipo de Arquivo</label><br>  
					<input type="radio" name="Tipo_Arquivo" value="1" checked="true"/>Relatório 
				    <input type="radio" name="Tipo_Arquivo" value="2"/>Texto
			    </fieldset>
				 	
			  </div>
			<%if (request.getAttribute("MensagemOk").toString().trim().equals("") == false){ %>
				<div class="divMensagemOk" id="MensagemOk"><%=request.getAttribute("MensagemOk").toString().trim()%></div>
			<%}%>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>