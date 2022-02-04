<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>

<jsp:useBean id="processoParalisadoDt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.ProcessoParalisadoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<%@page import="br.gov.go.tj.projudi.dt.relatorios.ProcessoParalisadoDt"%>
	<head>
		<title>Busca de Processos Paralisados</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/menusimples.css');
		</style>
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
      	<script type='text/javascript' src='./js/checks.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>      	
		<%@ include file="./js/Paginacao.js"%> 		
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
  			<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
  		<div id="divLocalizar" class="divLocalizar" > 
			<form action="ProcessoParalisado" method="post" name="Formulario" id="Formulario">
	
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PosicaoPaginaAtual" name="PosicaoPaginaAtual" type="hidden" value="<%=request.getAttribute("PosicaoPaginaAtual")%>" />
				<input id="fluxoParalisado" name="fluxoParalisado" type="hidden" value="<%=request.getAttribute("fluxoParalisado")%>" />
				<input id="RedirecionaOutraServentia" name="RedirecionaOutraServentia" type="hidden" value="" />
				
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda"><%=request.getAttribute("TituloPagina")%></legend>
					    					    
					    <label for="formEdicaoLabel">*Período Sem Movimentação</label><br>
						<select id="Periodo" name="Periodo" class="formEdicaoCombo">
							<option value="19" <%if(processoParalisadoDt.getPeriodo() != null && processoParalisadoDt.getPeriodo().equals("19")){%>selected="true"<%}%>>Até 20 dias</option>
							<option value="20" <%if(processoParalisadoDt.getPeriodo() != null && processoParalisadoDt.getPeriodo().equals("20")){%>selected="true"<%}%>>Mais de 20 dias</option>
							<option value="30" <%if(processoParalisadoDt.getPeriodo() != null && processoParalisadoDt.getPeriodo().equals("30")){%>selected="true"<%}%>>Mais de 30 dias</option>
							<option value="40" <%if(processoParalisadoDt.getPeriodo() != null && processoParalisadoDt.getPeriodo().equals("40")){%>selected="true"<%}%>>Mais de 40 dias</option>
							<option value="50" <%if(processoParalisadoDt.getPeriodo() != null && processoParalisadoDt.getPeriodo().equals("50")){%>selected="true"<%}%>>Mais de 50 dias</option>
							<option value="60" <%if(processoParalisadoDt.getPeriodo() != null && processoParalisadoDt.getPeriodo().equals("60")){%>selected="true"<%}%>>Mais de 60 dias</option>
							<option value="70" <%if(processoParalisadoDt.getPeriodo() != null && processoParalisadoDt.getPeriodo().equals("70")){%>selected="true"<%}%>>Mais de 70 dias</option>
							<option value="80" <%if(processoParalisadoDt.getPeriodo() != null && processoParalisadoDt.getPeriodo().equals("80")){%>selected="true"<%}%>>Mais de 80 dias</option>
							<option value="90" <%if(processoParalisadoDt.getPeriodo() != null && processoParalisadoDt.getPeriodo().equals("90")){%>selected="true"<%}%>>Mais de 90 dias</option>
							<option value="100" <%if(processoParalisadoDt.getPeriodo() != null && processoParalisadoDt.getPeriodo().equals("100")){%>selected="true"<%}%>>Mais de 100 dias</option>
							<option value="110" <%if(processoParalisadoDt.getPeriodo() != null && processoParalisadoDt.getPeriodo().equals("110")){%>selected="true"<%}%>>Mais de 110 dias</option>
							<option value="120" <%if(processoParalisadoDt.getPeriodo() != null && processoParalisadoDt.getPeriodo().equals("120")){%>selected="true"<%}%>>Mais de 120 dias</option>
							<option value="130" <%if(processoParalisadoDt.getPeriodo() != null && processoParalisadoDt.getPeriodo().equals("130")){%>selected="true"<%}%>>Mais de 130 dias</option>
							<option value="140" <%if(processoParalisadoDt.getPeriodo() != null && processoParalisadoDt.getPeriodo().equals("140")){%>selected="true"<%}%>>Mais de 140 dias</option>
							<option value="150" <%if(processoParalisadoDt.getPeriodo() != null && processoParalisadoDt.getPeriodo().equals("150")){%>selected="true"<%}%>>Mais de 150 dias</option>
							<option value="180" <%if(processoParalisadoDt.getPeriodo() != null && processoParalisadoDt.getPeriodo().equals("180")){%>selected="true"<%}%>>Mais de 180 dias</option>
							<option value="240" <%if(processoParalisadoDt.getPeriodo() != null && processoParalisadoDt.getPeriodo().equals("240")){%>selected="true"<%}%>>Mais de 240 dias</option>
							<option value="360" <%if(processoParalisadoDt.getPeriodo() != null && processoParalisadoDt.getPeriodo().equals("360")){%>selected="true"<%}%>>Mais de 360 dias</option>
						</select>
						<br />
				    	<label class="formEdicaoLabel" for="Id_ServentiaCargo">Magistrado Responsável
				    	<input class="FormEdicaoimgLocalizar" id="imaLocalizarId_ServentiaCargo" name="imaLocalizarId_ServentiaCargo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PosicaoPaginaAtual','0'); AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
		    			<input class="FormEdicaoimgLocalizar" name="imaLimparId_ServentiaCargo" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_ServentiaCargo','ServentiaCargo'); return false;" title="Limpar Juiz Responsável">  
				    	</label><br>  				    	
		    					    					    				    			
		    			<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargo" id="ServentiaCargo" type="text" size="80" maxlength="100" value="<%=processoParalisadoDt.getServentiaCargo()%>"/><br />

					    <br />
					    
						<input type="hidden" name="Id_Serventia" id="Id_Serventia" value="<%=request.getAttribute("Id_Serventia")%>"/>
						<input type="hidden" name="Id_Processo" id="Id_Processo" value=""/>
					    
			    		<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="submit" value="Buscar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');">
							<input name="imgLimpar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
						</div>
					</fieldset>		
				</div>
				
				<div align="left">
  					<% 	if (request.getAttribute("podeMovimentar") != null && request.getAttribute("podeMovimentar").toString().equalsIgnoreCase("true")){ %>
					<input name="imgMultipla" type="submit" value="Movimentação em Lote" onclick="AlterarAction('Formulario','Movimentacao');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');"/>
					<% }if (UsuarioSessao.isPodeTrocarResponsavel()){ %>
					<input name="imgMultipla" type="submit" value="Trocar Responsável" onclick="AlterarAction('Formulario','Movimentacao');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('RedirecionaOutraServentia','6');"/>
					<% } %>
				</div>				
				
				<div id="divTabela" class="divTabela" > 
			   		<table id="Tabela" class="Tabela">
			        	<thead>
			            	<tr class="TituloColuna">
			                	<th class="colunaMinima"></th>
			                	<td class="colunaMinina">
            						<input type="checkbox" id="chkSelTodos" value="" onclick="atualizarChecks(this, 'divTabela')"
			    					title="Alterar os estados de todos os itens da lista" />
			    				</td>
			                  	<th>Nº Processo</th>
			                  	<th>Data Recebimento</th>
			                  	<th>Qtde Dias Paralisado</th>
			                  	<th>Tipo Pendência</th>
			                  	<% if(UsuarioSessao.isDesembargador() || UsuarioSessao.isDistribuidorGabinete() || UsuarioSessao.isEstagiarioGabinete() || UsuarioSessao.isAssessorDesembargador() || UsuarioSessao.isAssistenteGabinete()){%>
			                  		<th>Assistente</th>
			                  	<%} %>
			                  	<th class="colunaMinima">Selecionar</th>           
			               	</tr>
			           	</thead>
			           	<tbody id="tabListaProcessoParalisado">
						<%
						if(request.getAttribute("ListaProcessos")!=null){
			  			List liTemp = (List)request.getAttribute("ListaProcessos");
			 			ProcessoParalisadoDt objTemp;
			  			String stTempNome="";
			  			for(int i = 0 ; i < liTemp.size();i++) {
			      			objTemp = (ProcessoParalisadoDt)liTemp.get(i); %>
							<%if (stTempNome.equalsIgnoreCase("")) { stTempNome="a";%> 
			                <tr class="TabelaLinha1"> 
							<%}else{ stTempNome=""; %>    
			                <tr class="TabelaLinha2">
							<%}%>
			                	<td> <%=i+1%></td>
			                	<td align="center">
									<input class="formEdicaoCheckBox" name="processos" type="checkbox" value="<%=objTemp.getIdProcesso()%>">
								</td>
			                   	<td class="Centralizado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Processo','<%=objTemp.getIdProcesso()%>'); FormSubmit('Formulario');"><%= objTemp.getNumeroProcesso()%></td>
			                   	<td class="Centralizado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Processo','<%=objTemp.getIdProcesso()%>'); FormSubmit('Formulario');"><%= objTemp.getDataRecebimento()%></td>
			                   	<td class="Centralizado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Processo','<%=objTemp.getIdProcesso()%>'); FormSubmit('Formulario');"><%= objTemp.getQuantidadeDiasParalisados()%></td>
			                   	<td onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Processo','<%=objTemp.getIdProcesso()%>'); FormSubmit('Formulario');"><%= objTemp.getTipoPendencia()%></td>
			                   	<% if(UsuarioSessao.isDesembargador() || UsuarioSessao.isDistribuidorGabinete() || UsuarioSessao.isEstagiarioGabinete() || UsuarioSessao.isAssessorDesembargador() || UsuarioSessao.isAssistenteGabinete()){%>
			                  		<td><%= objTemp.getAssistente()%></td>
			                  	<%} %>
		                   		<td class="Centralizado">
				                	<input name="formLocalizarimgEditar" type="image" style="align:center;" src="./imagens/imgEditar.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Processo','<%=objTemp.getIdProcesso()%>'); FormSubmit('Formulario');"/>     
				               	</td>                  
			               	</tr>
						<%}%>
						<%} %>
			           	</tbody>
			      	</table>     
			  	</div> 
			<%@ include file="Padroes/Mensagens.jspf" %>
			</form>
			</div>
			<%if(request.getAttribute("ListaProcessos")!=null){%>
				<%@ include file="./Padroes/PaginacaoSubmit.jspf"%>
			<%}%>			
		</div>
	</body>
</html>