<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="processoAudienciaDt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.ProcessoAudienciaDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>

<%@page import="br.gov.go.tj.projudi.dt.AudienciaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AreaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.ProcessoAudienciaDt"%><html>
	<head>
		<title>Busca de Processos Aguardando Audiência</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/menusimples.css');
		</style>
      	      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>      		
		<%@ include file="./js/Paginacao.js"%> 		
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
  			<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
  		<div id="divLocalizar" class="divLocalizar" > 
			<form action="ProcessoAudiencia" method="post" name="Formulario" id="Formulario">
				<!--  <input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />-->
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PosicaoPaginaAtual" name="PosicaoPaginaAtual" type="hidden" value="<%=request.getAttribute("PosicaoPaginaAtual")%>" />
				
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda">Busca de Processos Aguardando Audiência</legend>
					    
					    <%if(new Boolean(request.getAttribute("acessoEspecial").toString())){ %>
	                        <label class="formEdicaoLabel">Serventia
						 	<input type="hidden" name="Id_Serventia" id="Id_Serventia" value="<%=processoAudienciaDt.getIdServentia()%>">  
						    <input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
							<input class="FormEdicaoimgLocalizar" name="imaLimparServentia" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Serventia','Serventia');return false;" title="Limpar Serventia">  
						    </label><br>
						    <input class="formEdicaoInputSomenteLeitura"  readonly name="Serventia" id="Serventia" type="text" size="64" maxlength="100" value="<%=processoAudienciaDt.getServentia()%>"/><br />
					    <%} %>
					    
					   <div class="col30">
                        <label for="formEdicaoLabel">*Tipo de Audiência</label><br>
						<select id="TipoAudiencia" name="TipoAudiencia" class="formEdicaoCombo">
							<option value="999" <%if(processoAudienciaDt.getTipoAudiencia() != null && processoAudienciaDt.getTipoAudiencia().equals("999")){%>selected="true"<%}%>>Sem Audiência</option>
							<option value="<%=AudienciaTipoDt.Codigo.ADMONITORIA.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudiencia() != null && processoAudienciaDt.getTipoAudiencia().equals(String.valueOf(AudienciaTipoDt.Codigo.ADMONITORIA.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.ADMONITORIA.getCodigo())%></option>
							<option value="<%=AudienciaTipoDt.Codigo.CONCILIACAO.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudiencia() != null && processoAudienciaDt.getTipoAudiencia().equals(String.valueOf(AudienciaTipoDt.Codigo.CONCILIACAO.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.CONCILIACAO.getCodigo())%></option>
							<option value="<%=AudienciaTipoDt.Codigo.EXECUCAO.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudiencia() != null && processoAudienciaDt.getTipoAudiencia().equals(String.valueOf(AudienciaTipoDt.Codigo.EXECUCAO.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.EXECUCAO.getCodigo())%></option>
							<option value="<%=AudienciaTipoDt.Codigo.INICIAL.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudiencia() != null && processoAudienciaDt.getTipoAudiencia().equals(String.valueOf(AudienciaTipoDt.Codigo.INICIAL.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.INICIAL.getCodigo())%></option>
							<option value="<%=AudienciaTipoDt.Codigo.INSTRUCAO.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudiencia() != null && processoAudienciaDt.getTipoAudiencia().equals(String.valueOf(AudienciaTipoDt.Codigo.INSTRUCAO.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.INSTRUCAO.getCodigo())%></option>
							<option value="<%=AudienciaTipoDt.Codigo.INSTRUCAO_JULGAMENTO.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudiencia() != null && processoAudienciaDt.getTipoAudiencia().equals(String.valueOf(AudienciaTipoDt.Codigo.INSTRUCAO_JULGAMENTO.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.INSTRUCAO_JULGAMENTO.getCodigo())%></option>
							<option value="<%=AudienciaTipoDt.Codigo.INTERROGATORIO.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudiencia() != null && processoAudienciaDt.getTipoAudiencia().equals(String.valueOf(AudienciaTipoDt.Codigo.INTERROGATORIO.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.INTERROGATORIO.getCodigo())%></option>
							<option value="<%=AudienciaTipoDt.Codigo.JULGAMENTO.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudiencia() != null && processoAudienciaDt.getTipoAudiencia().equals(String.valueOf(AudienciaTipoDt.Codigo.JULGAMENTO.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.JULGAMENTO.getCodigo())%></option>
							<option value="<%=AudienciaTipoDt.Codigo.JUSTIFICACAO.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudiencia() != null && processoAudienciaDt.getTipoAudiencia().equals(String.valueOf(AudienciaTipoDt.Codigo.JUSTIFICACAO.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.JUSTIFICACAO.getCodigo())%></option>
							<option value="<%=AudienciaTipoDt.Codigo.PRELIMINAR.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudiencia() != null && processoAudienciaDt.getTipoAudiencia().equals(String.valueOf(AudienciaTipoDt.Codigo.PRELIMINAR.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.PRELIMINAR.getCodigo())%></option>
							<option value="<%=AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudiencia() != null && processoAudienciaDt.getTipoAudiencia().equals(String.valueOf(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo())%></option>
							<option value="<%=AudienciaTipoDt.Codigo.SINE_DIE.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudiencia() != null && processoAudienciaDt.getTipoAudiencia().equals(String.valueOf(AudienciaTipoDt.Codigo.SINE_DIE.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.SINE_DIE.getCodigo())%></option>
							<option value="<%=AudienciaTipoDt.Codigo.UNA.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudiencia() != null && processoAudienciaDt.getTipoAudiencia().equals(String.valueOf(AudienciaTipoDt.Codigo.UNA.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.UNA.getCodigo())%></option>
						</select>
					    </div>
					    <div class="col30">
					    <label for="formEdicaoLabel">*Período Sem Audiência</label><br>
						<select id="Periodo" name="Periodo" class="formEdicaoCombo">
							<option value="20" <%if(processoAudienciaDt.getPeriodo() != null && processoAudienciaDt.getPeriodo().equals("20")){%>selected="true"<%}%>>Mais de 20 dias</option>
							<option value="30" <%if(processoAudienciaDt.getPeriodo() != null && processoAudienciaDt.getPeriodo().equals("30")){%>selected="true"<%}%>>Mais de 30 dias</option>
							<option value="40" <%if(processoAudienciaDt.getPeriodo() != null && processoAudienciaDt.getPeriodo().equals("40")){%>selected="true"<%}%>>Mais de 40 dias</option>
							<option value="50" <%if(processoAudienciaDt.getPeriodo() != null && processoAudienciaDt.getPeriodo().equals("50")){%>selected="true"<%}%>>Mais de 50 dias</option>
							<option value="60" <%if(processoAudienciaDt.getPeriodo() != null && processoAudienciaDt.getPeriodo().equals("60")){%>selected="true"<%}%>>Mais de 60 dias</option>
							<option value="70" <%if(processoAudienciaDt.getPeriodo() != null && processoAudienciaDt.getPeriodo().equals("70")){%>selected="true"<%}%>>Mais de 70 dias</option>
							<option value="80" <%if(processoAudienciaDt.getPeriodo() != null && processoAudienciaDt.getPeriodo().equals("80")){%>selected="true"<%}%>>Mais de 80 dias</option>
							<option value="90" <%if(processoAudienciaDt.getPeriodo() != null && processoAudienciaDt.getPeriodo().equals("90")){%>selected="true"<%}%>>Mais de 90 dias</option>
							<option value="100" <%if(processoAudienciaDt.getPeriodo() != null && processoAudienciaDt.getPeriodo().equals("100")){%>selected="true"<%}%>>Mais de 100 dias</option>
							<option value="110" <%if(processoAudienciaDt.getPeriodo() != null && processoAudienciaDt.getPeriodo().equals("110")){%>selected="true"<%}%>>Mais de 110 dias</option>
							<option value="120" <%if(processoAudienciaDt.getPeriodo() != null && processoAudienciaDt.getPeriodo().equals("120")){%>selected="true"<%}%>>Mais de 120 dias</option>
							<option value="130" <%if(processoAudienciaDt.getPeriodo() != null && processoAudienciaDt.getPeriodo().equals("130")){%>selected="true"<%}%>>Mais de 130 dias</option>
							<option value="140" <%if(processoAudienciaDt.getPeriodo() != null && processoAudienciaDt.getPeriodo().equals("140")){%>selected="true"<%}%>>Mais de 140 dias</option>
							<option value="150" <%if(processoAudienciaDt.getPeriodo() != null && processoAudienciaDt.getPeriodo().equals("150")){%>selected="true"<%}%>>Mais de 150 dias</option>
							<option value="180" <%if(processoAudienciaDt.getPeriodo() != null && processoAudienciaDt.getPeriodo().equals("180")){%>selected="true"<%}%>>Mais de 180 dias</option>
							<option value="240" <%if(processoAudienciaDt.getPeriodo() != null && processoAudienciaDt.getPeriodo().equals("240")){%>selected="true"<%}%>>Mais de 240 dias</option>
							<option value="360" <%if(processoAudienciaDt.getPeriodo() != null && processoAudienciaDt.getPeriodo().equals("360")){%>selected="true"<%}%>>Mais de 360 dias</option>
						</select>
					    </div>
					    <div class="clear space"></div>
					    <table>
					    <tr><td colspan="2">&nbsp;</td></tr>
						    <tr>
							    <td colspan="2">
									<label class="formEdicaoLabel" for="APartirDa">A partir da</label><br>  
								</td>
							</tr>
								<td valign="middle" width="400">
									<input type="radio" name="APartirDa" value="1" <%if(processoAudienciaDt.getAPartirDa() != null && (processoAudienciaDt.getAPartirDa().equals("") || processoAudienciaDt.getAPartirDa().equals("1"))){ %> checked<%} %> onclick="javascript:document.getElementById('TipoAudienciaAnterior').disabled=true;"/>Data de Distribuição 
								    <input type="radio" name="APartirDa" value="2" <%if(processoAudienciaDt.getAPartirDa() != null && processoAudienciaDt.getAPartirDa().equals("2")){ %> checked<%} %> onclick="javascript:document.getElementById('TipoAudienciaAnterior').disabled=false;"/>Última audiência do tipo
								</td>
								<td>
								    <select id="TipoAudienciaAnterior" name="TipoAudienciaAnterior" class="formEdicaoCombo" <%if(processoAudienciaDt.getAPartirDa() != null && (processoAudienciaDt.getAPartirDa().equals("") || processoAudienciaDt.getAPartirDa().equals("1"))){ %> disabled="disabled" <%} %> >
										<option value="">-- Selecionar --</option>
										<option value="<%=AudienciaTipoDt.Codigo.ADMONITORIA.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudienciaAnterior() != null && processoAudienciaDt.getTipoAudienciaAnterior().equals(String.valueOf(AudienciaTipoDt.Codigo.ADMONITORIA.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.ADMONITORIA.getCodigo())%></option>
										<option value="<%=AudienciaTipoDt.Codigo.CONCILIACAO.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudienciaAnterior() != null && processoAudienciaDt.getTipoAudienciaAnterior().equals(String.valueOf(AudienciaTipoDt.Codigo.CONCILIACAO.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.CONCILIACAO.getCodigo())%></option>
										<option value="<%=AudienciaTipoDt.Codigo.EXECUCAO.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudienciaAnterior() != null && processoAudienciaDt.getTipoAudienciaAnterior().equals(String.valueOf(AudienciaTipoDt.Codigo.EXECUCAO.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.EXECUCAO.getCodigo())%></option>
										<option value="<%=AudienciaTipoDt.Codigo.INICIAL.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudienciaAnterior() != null && processoAudienciaDt.getTipoAudienciaAnterior().equals(String.valueOf(AudienciaTipoDt.Codigo.INICIAL.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.INICIAL.getCodigo())%></option>
										<option value="<%=AudienciaTipoDt.Codigo.INSTRUCAO.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudienciaAnterior() != null && processoAudienciaDt.getTipoAudienciaAnterior().equals(String.valueOf(AudienciaTipoDt.Codigo.INSTRUCAO.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.INSTRUCAO.getCodigo())%></option>
										<option value="<%=AudienciaTipoDt.Codigo.INSTRUCAO_JULGAMENTO.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudienciaAnterior() != null && processoAudienciaDt.getTipoAudienciaAnterior().equals(String.valueOf(AudienciaTipoDt.Codigo.INSTRUCAO_JULGAMENTO.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.INSTRUCAO_JULGAMENTO.getCodigo())%></option>
										<option value="<%=AudienciaTipoDt.Codigo.INTERROGATORIO.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudienciaAnterior() != null && processoAudienciaDt.getTipoAudienciaAnterior().equals(String.valueOf(AudienciaTipoDt.Codigo.INTERROGATORIO.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.INTERROGATORIO.getCodigo())%></option>
										<option value="<%=AudienciaTipoDt.Codigo.JULGAMENTO.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudienciaAnterior() != null && processoAudienciaDt.getTipoAudienciaAnterior().equals(String.valueOf(AudienciaTipoDt.Codigo.JULGAMENTO.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.JULGAMENTO.getCodigo())%></option>
										<option value="<%=AudienciaTipoDt.Codigo.JUSTIFICACAO.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudienciaAnterior() != null && processoAudienciaDt.getTipoAudienciaAnterior().equals(String.valueOf(AudienciaTipoDt.Codigo.JUSTIFICACAO.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.JUSTIFICACAO.getCodigo())%></option>
										<option value="<%=AudienciaTipoDt.Codigo.PRELIMINAR.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudienciaAnterior() != null && processoAudienciaDt.getTipoAudienciaAnterior().equals(String.valueOf(AudienciaTipoDt.Codigo.PRELIMINAR.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.PRELIMINAR.getCodigo())%></option>
										<option value="<%=AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudienciaAnterior() != null && processoAudienciaDt.getTipoAudienciaAnterior().equals(String.valueOf(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo())%></option>
										<option value="<%=AudienciaTipoDt.Codigo.SINE_DIE.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudienciaAnterior() != null && processoAudienciaDt.getTipoAudienciaAnterior().equals(String.valueOf(AudienciaTipoDt.Codigo.SINE_DIE.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.SINE_DIE.getCodigo())%></option>
										<option value="<%=AudienciaTipoDt.Codigo.UNA.getCodigo() %>" <%if(processoAudienciaDt.getTipoAudienciaAnterior() != null && processoAudienciaDt.getTipoAudiencia().equals(String.valueOf(AudienciaTipoDt.Codigo.UNA.getCodigo()))){%>selected="true"<%}%>><%=AudienciaTipoDt.Codigo.getDescricao(AudienciaTipoDt.Codigo.UNA.getCodigo())%></option>
									</select>
							    </td>
						    </tr>
					    </table>
					    <br />
					    
					    <br />
				    	<label class="formEdicaoLabel" for="Id_ServentiaCargo">Juiz Responsável
				    	<input class="FormEdicaoimgLocalizar" id="imaLocalizarId_ServentiaCargo" name="imaLocalizarId_ServentiaCargo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PosicaoPaginaAtual','0'); AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
		    			<input class="FormEdicaoimgLocalizar" name="imaLimparId_ServentiaCargo" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_ServentiaCargo','ServentiaCargo'); return false;" title="Limpar Juiz Responsável">  
				    	</label><br>  
				    	<input type="hidden" name="Id_ServentiaCargo" id="Id_ServentiaCargo" value="<%=processoAudienciaDt.getIdServentiaCargo()%>"/>
		    			
		    			<%if(request.getAttribute("ServentiaCargoUsuario") != null){ %>
							<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargo" id="ServentiaCargo" type="text" size="80" maxlength="100" value="<%=processoAudienciaDt.getServentiaCargo()%> - <%=request.getAttribute("ServentiaCargoUsuario")%>"/><br />
						<% } else {%>
							<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargo" id="ServentiaCargo" type="text" size="80" maxlength="100" value="<%=processoAudienciaDt.getServentiaCargo()%>"/><br />
						<% } %>
					    <br />
					    
					    <input type="hidden" name="acessoEspecial" id="acessoEspecial" value="<%=new Boolean(request.getAttribute("acessoEspecial").toString())%>">
					    
			    		<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="submit" value="Buscar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');">
							<input name="imgLimpar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
						</div>
					</fieldset>		
				</div>
				
				<input type="hidden" name="Id_Processo" id="Id_Processo" value=""/>
				
				<div id="divTabela" class="divTabela" > 
			   		<table id="Tabela" class="Tabela">
			        	<thead>
			            	<tr class="TituloColuna">
			                	<th class="colunaMinima"></th>
			                  	<th>Nº Processo</th>
			                  	<th>Data Recebimento</th>
			                  	<th>Data Agendada</th>
			                  	<th>Qtde Dias Aguardando</th>
			                  	<th class="colunaMinima">Selecionar</th>           
			               	</tr>
			           	</thead>
			           	<tbody id="tabListaProcessoAudiencia">
						<%
						if(request.getAttribute("ListaProcessos")!=null){
			  			List liTemp = (List)request.getAttribute("ListaProcessos");
			 			ProcessoAudienciaDt objTemp;			  			
			  			boolean boLinha=false; 
			  			for(int i = 0 ; i < liTemp.size();i++) {
			      			objTemp = (ProcessoAudienciaDt)liTemp.get(i); %>			      									
			                <tr class="TabelaLinha<%=(boLinha?1:2)%>"  >							
			                	<td> <%=i+1%></td>
			                   	<td class="Centralizado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Processo','<%=objTemp.getIdProcesso()%>'); FormSubmit('Formulario');"><%= objTemp.getNumeroProcesso()%></td>
			                   	<td class="Centralizado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Processo','<%=objTemp.getIdProcesso()%>'); FormSubmit('Formulario');"><%= objTemp.getDataRecebimento()%></td>
			                   	<td class="Centralizado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Processo','<%=objTemp.getIdProcesso()%>'); FormSubmit('Formulario');"><%= objTemp.getDataAgendada()%></td>
			                   	<td class="Centralizado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Processo','<%=objTemp.getIdProcesso()%>'); FormSubmit('Formulario');"><%= objTemp.getQuantidadeDiasParalisados()%></td>
		                   		<td class="Centralizado"><input name="formLocalizarimgEditar" type="image" style="align:center;" src="./imagens/imgEditar.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Processo','<%=objTemp.getIdProcesso()%>'); FormSubmit('Formulario');"/>  </td>                  
			               	</tr>
						<%
							boLinha = !boLinha;
						}%>
			           	</tbody>
			      	</table>     
			  	</div> 
			</form>
			</div>
				<%@ include file="./Padroes/PaginacaoSubmit.jspf"%>
			  	<%} %>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>