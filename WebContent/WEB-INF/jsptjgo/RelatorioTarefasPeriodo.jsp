<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.ParametroRelatorioTarefaDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.TJDataHora"%>
<%@page import="br.gov.go.tj.projudi.dt.ProjetoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.TarefaDt"%>

<jsp:useBean id="ParamRelTarefaDt" scope="session" class= "br.gov.go.tj.projudi.dt.ParametroRelatorioTarefaDt"/>


<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%><html>
	<head>
		<title>Relatórios de Tarefas</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
    	
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/menusimples.css');
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
  			<div class="area"><h2>&raquo; Relat&oacute;rio de Tarefas </h2></div>
	  		<form action="RelatorioTarefasPeriodo" method="post" name="Formulario" id="Formulario">
	  		
 				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />				

				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda"> Dados da Tarefa </legend> 
					    
					    <fieldset class="formEdicao">					    
					    	<legend class="formEdicaoLegenda">Período</legend> 					
							<div class="col15">
							<label for="DataInicial" style="float:left;">Data Inicial</label><br> 
			    			<input class="formEdicaoInputSomenteLeitura" style="float:left;"  readonly name="DataInicial" id="DataInicial"  type="text" size="10" maxlength="10" value="<%=ParamRelTarefaDt.getPeriodoInicialUtilizado().getDataFormatadaddMMyyyy()%>"> <img id="calendarioDataInicial" style="float:left;vertical-align:middle;" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataInicial,'dd/mm/yyyy',this)">
			    			</div>
			    			
			    			<div class="col15">
			    			<label class="formEdicaoLabel" for="DataFinal">Data Final</label><br> 
			    			<input class="formEdicaoInputSomenteLeitura"  readonly name="DataFinal" id="DataFinal"  type="text" size="10" maxlength="10" value="<%=ParamRelTarefaDt.getPeriodoFinalUtilizado().getDataFormatadaddMMyyyy()%>"> <img id="calendarioDataFinal" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataFinal,'dd/mm/yyyy',this)"> 
				</div>
				<div class="clear"></div>
						</fieldset>
						
						 <fieldset class="formEdicao">					    
					    	<legend class="formEdicaoLegenda">Projeto</legend>
					    	 
					    	<label id="formLocalizarLabel" class="formLocalizarLabel">Projeto
							<input class="FormEdicaoimgLocalizar" id="imaLocalizarProjeto" name="imaLocalizarProjeto" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProjetoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />
							<input class="FormEdicaoimgLocalizar" id="imaLimparProjeto" name="imaLimparProjeto" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Projeto','Projeto'); return false;" />
							</label><br>
							<input id="Id_Projeto" name="Id_Projeto" type="hidden" value="<%=ParamRelTarefaDt.getId_Projeto()%>"/>
							<input class="formEdicaoInputSomenteLeitura" name="Projeto" id="Projeto" readonly="true" type="text" size="60" maxlength="60" value="<%=ParamRelTarefaDt.getProjeto()%>" />
				
					    </fieldset>
					    
					    <fieldset class="formEdicao">					    
					    	<legend class="formEdicaoLegenda">Analista</legend>
					    	 
					    	<label id="formLocalizarLabelServentiaCargo" class="formLocalizarLabel">Serventia Cargo
					    	
							<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentiaCargo" name="imaLocalizarServentiaCargo" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />
							<input class="FormEdicaoimgLocalizar" id="imaLimparServentiaCargo" name="imaLimparServentiaCargo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_ServentiaCargo','ServentiaCargo'); return false;" />
							</label><br>
							<input id="Id_ServentiaCargo" name="Id_ServentiaCargo" type="hidden" value="<%=ParamRelTarefaDt.getId_ServentiaCargo()%>"/>
							<input class="formEdicaoInputSomenteLeitura" name="ServentiaCargo" id="ServentiaCargo" readonly="true" type="text" size="60" maxlength="60" value="<%=ParamRelTarefaDt.getServentiaCargo()%>" />
				
					    </fieldset>						
						
				
					<div id="divBotoesCentralizados">
						<input name="imgSubmeter" type="submit" value="Buscar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')">
						<input name="imgLimpar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>');">
					</div>                      
					   
					</fieldset>
					
					<br />
					<div id="divTabela" class="divTabela" > 
						<table id="Tabela" class="Tabela">
							<thead>
								<tr>
									<th></th>
									<th>Tarefa</th>
									<th>Situação</th>
									<th>Prioridade</th>
									<th>Criação</th>
									<th>Início</th>
									<th>Fim</th>
									<th>Previsão</th>
									<th>APF</th>
									<th>APG</th>
									<th>Tipo</th>
									<th>Participante</th>						
								</tr>
								</thead>
							<tbody id="tabListaTarefa">
				<%
						if(ParamRelTarefaDt != null){
						  	List liTemp = (List) ParamRelTarefaDt.getListaTarefas();
						 	TarefaDt objTemp;
						  	boolean boLinha=false;
						  	if(liTemp!=null)
						  		for(int i = 0 ; i< liTemp.size();i++) {
					      			objTemp = (TarefaDt)liTemp.get(i); %>
									<tr class="TabelaLinha<%=(boLinha?1:2)%>"  >
										<td > <%=i+1%></td>
										<td><%= objTemp.getTarefa()%></td>
										<td><%= objTemp.getTarefaStatus()%></td>
										<td><%= objTemp.getTarefaPrioridade()%></td>
										<td><%= objTemp.getDataCriacao()%></td>
										<td><%= objTemp.getDataInicio()%></td>
										<td><%= objTemp.getDataFim()%></td>
										<td><%= objTemp.getPrevisao()%></td>
										<td><%= objTemp.getPontosApf()%></td>
										<td><%= objTemp.getPontosApg()%></td>
										<td><%= objTemp.getTarefaTipo()%></td>
										<td><%= objTemp.getProjetoParticipanteResponsavel()%></td>
									</tr>
								<%
									boLinha = !boLinha;
								}}%>
							</tbody>
						</table>
					</div>		
				</div>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>