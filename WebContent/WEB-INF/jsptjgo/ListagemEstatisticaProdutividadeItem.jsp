<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.EstatisticaProdutividadeItemDt"%>

<jsp:useBean id="EstatisticaProdutividadeItemdt" scope="session" class= "br.gov.go.tj.projudi.dt.EstatisticaProdutividadeItemDt"/>

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
			<form action="EstatisticaProdutividadeItem" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input id="PosicaoPaginaAtual" name="PosicaoPaginaAtual" type="hidden" value="<%=request.getAttribute("PosicaoPaginaAtual")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgImprimir"  class="imgImprimir" title="Gerar Relatório" name="imgImprimir" type="image" 
						src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">	
			</div/>
			<br />
			<div id="divEditar" class="divEditar">

				
				<fieldset class="formEdicao"> 
					
					<legend class="formEdicaoLegenda">Parâmetro de consulta</legend>
					<label class="formEdicaoLabel">Descrição</label><br> 
					<input class="formEdicaoInput" name="EstatisticaProdutividadeItem" id="EstatisticaProdutividadeItem"  type="text" size="60" maxlength="60" value="<%=EstatisticaProdutividadeItemdt.getEstatisticaProdutividadeItem()%>" onkeyup=" autoTab(this,60)"/>
					<br />
					<label class="formEdicaoLabel" for="tipoArquivo">Tipo de Impressão</label><br>  
					<input type="radio" name="tipoArquivo" id="tipoArquivo" value="1" checked/>Relatório 
				    <input type="radio" name="tipoArquivo" id="tipoArquivo" value="2"/>Texto
				    <br />
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgSubmeter" type="submit" value="Buscar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>');">
					</div>
				
			    </fieldset>
				 	
			</div>
			
			<div id="divTabela" class="divTabela" > 
			   		<table id="Tabela" class="Tabela">
			        	<thead>
			            	<tr class="TituloColuna">
			                  	<th class="ColunaMinima">&nbsp;</th>
			                  	<th>Estatística Produtividade Item</th>
			               	</tr>
			           	</thead>
			           	<tbody id="tabListaEstatisticaProdutividadeItem">
						<%
						if(request.getAttribute("listaEstatisticaProdutividadeItem")!=null){
			  			List liTemp = (List)request.getAttribute("listaEstatisticaProdutividadeItem");
			  			EstatisticaProdutividadeItemDt objTemp;
			  			String stTempNome="";
			  			for(int i = 0 ; i < liTemp.size();i++) {
			      			objTemp = (EstatisticaProdutividadeItemDt)liTemp.get(i); %>
							<%if (stTempNome.equalsIgnoreCase("")) { stTempNome="a";%> 
			                <tr class="TabelaLinha1"> 
							<%}else{ stTempNome=""; %>    
			                <tr class="TabelaLinha2">
							<%}%>
								<td class="Centralizado" ><%= i+1 %></td>
			                   	<td class="Centralizado" ><%= objTemp.getEstatisticaProdutividadeItem()%></td>
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