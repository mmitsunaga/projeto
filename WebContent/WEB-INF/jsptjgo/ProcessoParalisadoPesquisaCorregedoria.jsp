<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>

<jsp:useBean id="processoParalisadoDt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.ProcessoParalisadoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>

<%@page import="br.gov.go.tj.projudi.dt.relatorios.ProcessoParalisadoDt"%><html>
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
				<input id="CuringaServentiaCargo" name="CuringaServentiaCargo" type="hidden" value="S" />
				
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda">Busca de Processos Paralisados</legend>
					    
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
					    
                        <label class="formEdicaoLabel">Serventia
					 	<input type="hidden" name="Id_Serventia" id="Id_Serventia" value="<%=processoParalisadoDt.getIdServentia()%>">  
					    <input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
						<input class="FormEdicaoimgLocalizar" name="imaLimparServentia" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Serventia','Serventia'); LimparChaveEstrangeira('Id_ServentiaCargo','ServentiaCargo'); return false;" title="Limpar Serventia">  
					    </label><br>
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="Serventia" id="Serventia" type="text" size="64" maxlength="100" value="<%=processoParalisadoDt.getServentia()%>"/>
					    <br />
					    <label class="formEdicaoLabel" for="Id_ServentiaCargo">Magistrado Responsável
					    <input class="FormEdicaoimgLocalizar" id="imaLocalizarId_ServentiaCargo" name="imaLocalizarId_ServentiaCargo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PosicaoPaginaAtual','0'); AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
		    			<input class="FormEdicaoimgLocalizar" name="imaLimparId_ServentiaCargo" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_ServentiaCargo','ServentiaCargo'); return false;" title="Limpar Juiz Responsável">  
					    </label><br>  
				    	<input type="hidden" name="Id_ServentiaCargo" id="Id_ServentiaCargo" value="<%=processoParalisadoDt.getIdServentiaCargo()%>"/>
		    			
		    			<%if(request.getAttribute("ServentiaCargoUsuario") != null){ %>
		    			<input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargo" id="ServentiaCargo" type="text" size="80" maxlength="100" value="<%=processoParalisadoDt.getServentiaCargo()%> - <%=request.getAttribute("ServentiaCargoUsuario")%>"/><br />
						<% } else {%>
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="ServentiaCargo" id="ServentiaCargo" type="text" size="80" maxlength="100" value="<%=processoParalisadoDt.getServentiaCargo()%>"/><br />
						<% } %>
					    <br />
					    
			    		<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="submit" value="Buscar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>');">
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
			                  	<th>Qtde Dias Paralisado</th>
			                  	<th>Tipo Pendência</th>
			                  	<th class="colunaMinima">Selecionar</th>           
			               	</tr>
			           	</thead>
			           	<tbody id="tabListaProcessoParalisado">
						<%
						if(request.getAttribute("ListaProcessos")!=null){
			  			List liTemp = (List)request.getAttribute("ListaProcessos");
			 			ProcessoParalisadoDt objTemp;			  			
			  			boolean boLinha=false; 
			  			for(int i = 0 ; i < liTemp.size();i++) {
			      			objTemp = (ProcessoParalisadoDt)liTemp.get(i); %>			      									
			                <tr class="TabelaLinha<%=(boLinha?1:2)%>"  >							
			                	<td> <%=i+1%></td>
			                   	<td class="Centralizado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Processo','<%=objTemp.getIdProcesso()%>'); FormSubmit('Formulario');"><%= objTemp.getNumeroProcesso()%></td>
			                   	<td class="Centralizado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Processo','<%=objTemp.getIdProcesso()%>'); FormSubmit('Formulario');"><%= objTemp.getDataRecebimento()%></td>
			                   	<td class="Centralizado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Processo','<%=objTemp.getIdProcesso()%>'); FormSubmit('Formulario');"><%= objTemp.getQuantidadeDiasParalisados()%></td>
			                   	<td onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Processo','<%=objTemp.getIdProcesso()%>'); FormSubmit('Formulario');"><%= objTemp.getTipoPendencia()%></td>
		                   		<td class="Centralizado">
		                   			<input name="formLocalizarimgEditar" type="image" style="align:center;" src="./imagens/imgEditar.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Processo','<%=objTemp.getIdProcesso()%>'); FormSubmit('Formulario');"/>  
		                   		</td>                  
			               	</tr>
						<%
							boLinha = !boLinha;
						}%>
						<%} %>
			           	</tbody>
			      	</table>     
			  	</div> 
			</form>
			</div>
			<%if(request.getAttribute("ListaProcessos")!=null){%>
				<%@ include file="./Padroes/PaginacaoSubmit.jspf"%>
		  	<%} %>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>