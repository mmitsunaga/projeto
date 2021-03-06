<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.RelatorioAnaliticoDt"%>

<jsp:useBean id="RelatorioAnaliticodt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.RelatorioAnaliticoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoTipoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<%@page import="java.util.HashMap"%>

<html>
	<head>
		<title> <%=request.getAttribute("tempPrograma")%> </title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
		</style>
		
		
      	<script type='text/javascript' src='./js/checks.js'></script>
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
		<script type="text/javascript" src="./js/jqDnR.js"> </script>  
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
		<%@ include file="./js/Paginacao.js"%> 
	</head>
	<body>
		<div id="divCorpo" class="divCorpo" >
	  		<div class="area"><h2>&raquo; <%=request.getAttribute("tempPrograma")%> </h2></div>
	  	<div id="divLocalizar" class="divLocalizar" > 
			<form action="RelatorioAnaliticoProdutividade" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input id="PosicaoPaginaAtual" name="PosicaoPaginaAtual" type="hidden" value="<%=request.getAttribute("PosicaoPaginaAtual")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			<div id="divEditar" class="divEditar">

				
				<fieldset class="formEdicao"> 
					
					<legend class="formEdicaoLegenda">Par?metros de consulta</legend>
					
					<label for="Data">*M?s/Ano</label><br> 
					<select id="Mes" name ="Mes" class="formEdicaoCombo">
						<option value="1" <%if(RelatorioAnaliticodt.getMes().equals("1")){%>selected="true"<%}%>>Janeiro</option>
						<option value="2" <%if(RelatorioAnaliticodt.getMes().equals("2")){%>selected="true"<%}%>>Fevereiro</option>
						<option value="3" <%if(RelatorioAnaliticodt.getMes().equals("3")){%>selected="true"<%}%>>Mar?o</option>
						<option value="4" <%if(RelatorioAnaliticodt.getMes().equals("4")){%>selected="true"<%}%>>Abril</option>
						<option value="5" <%if(RelatorioAnaliticodt.getMes().equals("5")){%>selected="true"<%}%> >Maio</option>
						<option value="6" <%if(RelatorioAnaliticodt.getMes().equals("6")){%>selected="true"<%}%>>Junho</option>
						<option value="7" <%if(RelatorioAnaliticodt.getMes().equals("7")){%>selected="true"<%}%>>Julho</option>
						<option value="8" <%if(RelatorioAnaliticodt.getMes().equals("8")){%>selected="true"<%}%>>Agosto</option>
						<option value="9" <%if(RelatorioAnaliticodt.getMes().equals("9")){%>selected="true"<%}%>>Setembro</option>
						<option value="10" <%if(RelatorioAnaliticodt.getMes().equals("10")){%>selected="true"<%}%>>Outubro</option>
						<option value="11" <%if(RelatorioAnaliticodt.getMes().equals("11")){%>selected="true"<%}%>>Novembro</option>
						<option value="12" <%if(RelatorioAnaliticodt.getMes().equals("12")){%>selected="true"<%}%>>Dezembro</option>
					</select>
					<select id="Ano" name ="Ano" class="formEdicaoCombo" >
					<%
					for(int i = Funcoes.StringToInt(RelatorioAnaliticodt.getAno().toString()) ; i >= 1997; i--) { %>
						<option value="<%=i%>" <%if(RelatorioAnaliticodt.getAno().equals(String.valueOf(i))){%>selected="true"<%}%> ><%=i%></option>
					<%} %>
					</select>
										
					<br /><br />
				    <label for="Id_Serventia">*Servidor
				    <input class="FormEdicaoimgLocalizar" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
				    <input class="FormEdicaoimgLocalizar" id="imaLimparUsuario" name="imaLimparUsuario" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Usuario','Usuario'); return false;" />
				    </label><br>  
				    
				    
				    <input id="Id_Usuario" name="Id_Usuario" type="hidden" value="<%=RelatorioAnaliticodt.getUsuario().getId()%>"/>
				    <input  class="formEdicaoInputSomenteLeitura" id="Usuario" name="Usuario" readonly="true" type="text" size="60" maxlength="60" value="<%=RelatorioAnaliticodt.getUsuario().getNome()%>"/>
				    <br /><br />
				
					<label for="Id_MovimentacaoTipo">*Estat?stica de Produtividade
					<input class="FormEdicaoimgLocalizar" name="imaLocalizarCargoTipo" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(MovimentacaoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > 
					<input class="FormEdicaoimgLocalizar" id="imaLimparCodigoTipo" name="imaLimparCodigoTipo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_MovimentacaoTipo','MovimentacaoTipo'); return false;" />
					</label><br>  
					
					
					<input id="Id_MovimentacaoTipo" name="Id_MovimentacaoTipo" type="hidden" value="<%=RelatorioAnaliticodt.getId_MovimentacaoTipo()%>"/>
					<input  class="formEdicaoInputSomenteLeitura" id="MovimentacaoTipo" name="MovimentacaoTipo" readonly="true" type="text" size="60" maxlength="60" value="<%=RelatorioAnaliticodt.getMovimentacaoTipo()%>"/>
					<br />
					
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
			                  	<th>M?s/Ano</th>
			                  	<th>N?mero Processo</th>
			                  	<th>Usu?rio</th>
			                  	<th>Estat?stica</th>
			                  	<th class="colunaMinima">Selecionar</th>           
			               	</tr>
			           	</thead>
			           	<tbody id="tabListaProcessoSemMovimentacao">
						<%
						if(request.getAttribute("listaProcessos")!=null){
			  			List liTemp = (List)request.getAttribute("listaProcessos");
			  			RelatorioAnaliticoDt objTemp;
			  			String stTempNome="";
			  			for(int i = 0 ; i < liTemp.size();i++) {
			      			objTemp = (RelatorioAnaliticoDt)liTemp.get(i); %>
							<%if (stTempNome.equalsIgnoreCase("")) { stTempNome="a";%> 
			                <tr class="TabelaLinha1"> 
							<%}else{ stTempNome=""; %>    
			                <tr class="TabelaLinha2">
							<%}%>
								<td class="Centralizado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga8%>'); AlterarValue('Id_Processo','<%=objTemp.getIdProcesso()%>'); FormSubmit('Formulario');"><%= objTemp.getMesRelatorio()%>/<%= objTemp.getAnoRelatorio()%></td>
			                   	<td class="Centralizado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga8%>'); AlterarValue('Id_Processo','<%=objTemp.getIdProcesso()%>'); FormSubmit('Formulario');"><%= objTemp.getNumeroProcesso()%></td>
			                   	<td class="Centralizado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga8%>'); AlterarValue('Id_Processo','<%=objTemp.getIdProcesso()%>'); FormSubmit('Formulario');"><%= objTemp.getUsuarioRelatorio()%></td>
			                   	<td class="Centralizado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga8%>'); AlterarValue('Id_Processo','<%=objTemp.getIdProcesso()%>'); FormSubmit('Formulario');"><%= objTemp.getItemEstatisticaRelatorio()%></td>
		                   		<td class="Centralizado">
				                	<input name="formLocalizarimgEditar" type="image" style="align:center;" src="./imagens/imgEditar.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga8%>'); AlterarValue('Id_Processo','<%=objTemp.getIdProcesso()%>'); FormSubmit('Formulario');"/>     
				               	</td>
			               	</tr>
						<%}%>
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