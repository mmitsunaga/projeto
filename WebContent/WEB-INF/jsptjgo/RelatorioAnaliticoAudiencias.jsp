<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.RelatorioAudienciasDt"%>

<jsp:useBean id="RelatorioAudienciasdt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.RelatorioAudienciasDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>


<%@page import="java.util.HashMap"%>

<%@page import="br.gov.go.tj.projudi.dt.relatorios.RelatorioAudienciasDt"%><html>
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
		
		
	</head>
	<body>
	<div id="divCorpo" class="divCorpo" >

			<div class="area">
			<h2>» Relatório Analítico de Audiências </h2>
			</div>


			<form action="RelatorioAudiencias" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo"  class="imgNovo" title="Nova Consulta" name="imaNovo" type="image" src="./imagens/imgNovo.png"  
						onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >
				<input id="imgImprimir"  class="imgImprimir" title="Gerar Relatório" name="imgImprimir" type="image" 
						src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">
				
				<input type='hidden' name="fluxo" value="<%=RelatorioAudienciasDt.FLUXO_ANALITICO%>"/>
				
			</div/><br />
			<div id="divEditar" class="divEditar">

				<input type="hidden" id="AgrupamentoRelatorio" name="AgrupamentoRelatorio" value="<%=RelatorioAudienciasdt.getAgrupamentoRelatorio()%>"/>
				
				<fieldset class="formEdicao"> 
					
					<legend class="formEdicaoLegenda">Período</legend>
					
					<div class="periodo">
			    		<label class="formEdicaoLabel" for="label_Comarca">Comarca  
			    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarComarca" name="imaLocalizarComarca" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(((ComarcaDt.CodigoPermissao - RelatorioAudienciasDt.CodigoPermissao) * Configuracao.QtdPermissao) + (Configuracao.Localizar + (RelatorioAudienciasDt.CodigoPermissao * Configuracao.QtdPermissao)))%>')" >  
			    		<input class="FormEdicaoimgLocalizar" id="imaLimparComarca" name="imaLimparComarca" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Comarca','Comarca'); return false;" > 
			    		</label><br>
			    		<input  name='Id_Comarca' id='Id_Comarca' type='hidden'  value='<%=RelatorioAudienciasdt.getId_Comarca()%>'> 
			    		<input class="formEdicaoInputSomenteLeitura"  readonly name="Comarca" id="Comarca" type="text" size="36" maxlength="60" value="<%=RelatorioAudienciasdt.getComarca()%>"/>
					</div>
					<br/><br/>
					
					
						<div class="periodo">
						<label class="formEdicaoLabel" for="serventia">*Serventia
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(((ServentiaDt.CodigoPermissao - RelatorioAudienciasDt.CodigoPermissao) * Configuracao.QtdPermissao) + (Configuracao.Localizar + (RelatorioAudienciasDt.CodigoPermissao * Configuracao.QtdPermissao)))%>')" >  
						</label><br>
						<input  name='Id_Serventia' id='Id_Serventia' type='hidden'  value='<%=RelatorioAudienciasdt.getId_Serventia()%>'> 
						<input class="formEdicaoInputSomenteLeitura"  readonly name="Serventia" id="Serventia" type="text" size="70" maxlength="80" value="<%=RelatorioAudienciasdt.getServentia()%>">
						</div>
					<br/><br/>
					
					
					<div class="periodo">
					
						<div class="col30">
						<label>Início</label><br>
						<input id="dataInicial" name="dataInicial" size="10" maxlength="10" title="Clique para escolher uma data."  value="<%=RelatorioAudienciasdt.getDataInicial()%>" onkeyup="mascara_data(this);checarFoco()" onkeypress="return DigitarSoNumero(this, event)" />
						<img id="imgDataInicial" src="./imagens/dlcalendar_2.gif" height="15" width="15" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].dataInicial,'dd/mm/yyyy',this)" />
						</div>

					</div>
					
					<div class="periodo">
							
							<div class="col30">
							<label>Fim</label><br>
							<input id="dataFinal" name="dataFinal" size="10" maxlength="10" title="Clique para escolher uma data."  value="<%=RelatorioAudienciasdt.getDataFinal()%>" onkeyup="mascara_data(this);checarFoco()" onkeypress="return DigitarSoNumero(this, event)" />
							<img id="calDataConsulta" src="./imagens/dlcalendar_2.gif" height="15" width="15" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].dataFinal,'dd/mm/yyyy',this)" />
							</div>
					</div>
				
				</fieldset>

				<br />
				<br />

				 	
			  </div>

			<%@ include file="Padroes/Mensagens.jspf" %>
			
			
	</div>	
	</body>
</html>