<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="java.util.List"%>

<jsp:useBean id="GraficoProcessodt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.GraficoProcessoDt"/>
<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.GraficoProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EstatisticaProdutividadeItemDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<html>
	<head>
		<title> <%=request.getAttribute("tempPrograma")%> </title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
		</style>
		
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script> 
	</head>
	<body>
		<div id="divCorpo" class="divCorpo" >
	  		<div class="area"><h2>&raquo; <%=request.getAttribute("tempPrograma")%></h2> </div>
			<form action="GraficoProcesso" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
			<input id="removerItemProdutividade" name="removerItemProdutividade" type="hidden" value="<%=false%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo"  class="imgNovo" title="Nova Consulta" name="imaNovo" type="image" src="./imagens/imgNovo.png"  
						onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >
				<input id="imgImprimir"  class="imgImprimir" title="Gerar Gráfico" name="imgImprimir" type="image" 
						src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">	
			</div><br/>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">Período</legend>
					<div class="periodo">
					<label for="DataInicial">Mês/Ano Inicial</label><br> 
					<select id="MesInicial" name ="MesInicial" class="formEdicaoCombo">
						<option value="1" <%if(GraficoProcessodt.getMesInicial().equals("1")){%>selected="true"<%}%>>Janeiro</option>
						<option value="2" <%if(GraficoProcessodt.getMesInicial().equals("2")){%>selected="true"<%}%>>Fevereiro</option>
						<option value="3" <%if(GraficoProcessodt.getMesInicial().equals("3")){%>selected="true"<%}%>>Março</option>
						<option value="4" <%if(GraficoProcessodt.getMesInicial().equals("4")){%>selected="true"<%}%>>Abril</option>
						<option value="5" <%if(GraficoProcessodt.getMesInicial().equals("5")){%>selected="true"<%}%> >Maio</option>
						<option value="6" <%if(GraficoProcessodt.getMesInicial().equals("6")){%>selected="true"<%}%>>Junho</option>
						<option value="7" <%if(GraficoProcessodt.getMesInicial().equals("7")){%>selected="true"<%}%>>Julho</option>
						<option value="8" <%if(GraficoProcessodt.getMesInicial().equals("8")){%>selected="true"<%}%>>Agosto</option>
						<option value="9" <%if(GraficoProcessodt.getMesInicial().equals("9")){%>selected="true"<%}%>>Setembro</option>
						<option value="10" <%if(GraficoProcessodt.getMesInicial().equals("10")){%>selected="true"<%}%>>Outubro</option>
						<option value="11" <%if(GraficoProcessodt.getMesInicial().equals("11")){%>selected="true"<%}%>>Novembro</option>
						<option value="12" <%if(GraficoProcessodt.getMesInicial().equals("12")){%>selected="true"<%}%>>Dezembro</option>
					</select>
					<select id="AnoInicial" name ="AnoInicial" class="formEdicaoCombo" >
					<%
					for(int i = Funcoes.StringToInt(GraficoProcessodt.getAnoInicial().toString()) ; i >= 1997; i--) { %>
						<option value="<%=i%>" <%if(GraficoProcessodt.getAnoInicial().equals(String.valueOf(i))){%>selected="true"<%}%> ><%=i%></option>
					<%} %>
					</select>
					</div>
					<div class="periodo">
					<label for="DataFinal">Mês/Ano Final</label><br> 
					<select id="MesFinal" name ="MesFinal" class="formEdicaoCombo">
						<option value="1" <%if(GraficoProcessodt.getMesFinal().equals("1")){%>selected="true"<%}%>>Janeiro</option>
						<option value="2" <%if(GraficoProcessodt.getMesFinal().equals("2")){%>selected="true"<%}%>>Fevereiro</option>
						<option value="3" <%if(GraficoProcessodt.getMesFinal().equals("3")){%>selected="true"<%}%>>Março</option>
						<option value="4" <%if(GraficoProcessodt.getMesFinal().equals("4")){%>selected="true"<%}%>>Abril</option>
						<option value="5" <%if(GraficoProcessodt.getMesFinal().equals("5")){%>selected="true"<%}%> >Maio</option>
						<option value="6" <%if(GraficoProcessodt.getMesFinal().equals("6")){%>selected="true"<%}%>>Junho</option>
						<option value="7" <%if(GraficoProcessodt.getMesFinal().equals("7")){%>selected="true"<%}%>>Julho</option>
						<option value="8" <%if(GraficoProcessodt.getMesFinal().equals("8")){%>selected="true"<%}%>>Agosto</option>
						<option value="9" <%if(GraficoProcessodt.getMesFinal().equals("9")){%>selected="true"<%}%>>Setembro</option>
						<option value="10" <%if(GraficoProcessodt.getMesFinal().equals("10")){%>selected="true"<%}%>>Outubro</option>
						<option value="11" <%if(GraficoProcessodt.getMesFinal().equals("11")){%>selected="true"<%}%>>Novembro</option>
						<option value="12" <%if(GraficoProcessodt.getMesFinal().equals("12")){%>selected="true"<%}%>>Dezembro</option>
					</select>
					<select id="AnoFinal" name ="AnoFinal" class="formEdicaoCombo">
					<%
					for(int i = Funcoes.StringToInt(GraficoProcessodt.getAnoFinal().toString()) ; i >= 1997; i--) { %>
						<option value="<%=i%>" <%if(GraficoProcessodt.getAnoFinal().equals(String.valueOf(i))){%>selected="true"<%}%> ><%=i%></option>
					<%} %>
					</select>
					</div>
				</fieldset>
				
				<input type="hidden" id="posicaoLista" name="posicaoLista">
	
			   	<fieldset id="VisualizaDados" class="VisualizaDados">   
   				<legend>*Item de Produtividade <input class="FormEdicaoimgLocalizar" id="imaLocalizarEstatisticaProdutividadeItem" name="imaLocalizarEstatisticaProdutividadeItem" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(EstatisticaProdutividadeItemDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
   				</legend>
   				<%
   				List listaEstatisticaProdutividadeItem = GraficoProcessodt.getListaEstatisticaProdutividadeItem();
   	    		if (listaEstatisticaProdutividadeItem != null && listaEstatisticaProdutividadeItem.size() > 0){ %>
   				<table width="98%" border="0" cellpadding="0" cellspacing="0" style="font-size: 10px !important;">
   				<thead align="left">
   					<tr>
   						<th>Descrição</th>
   						<th>Excluir</th>
   					</tr>
   				</thead>
				<%
   	    		for (int i=0;i < listaEstatisticaProdutividadeItem.size();i++){
   	    			EstatisticaProdutividadeItemDt estatisticaProdutividadeItemDt = (EstatisticaProdutividadeItemDt)listaEstatisticaProdutividadeItem.get(i); %>
	   			<tbody>
					<tr>
       					<td><%=estatisticaProdutividadeItemDt.getEstatisticaProdutividadeItem()%></td>
       	 				<td><input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('removerItemProdutividade', '<%=true%>');AlterarValue('posicaoLista','<%=i%>')" title="Excluir Item Produtividade"/></td>
       	 			</tr>
       	 		</tbody>
       			<%	} %>
     		  	</table>
   				<% } else { %>
   				<em> Insira um item de produtividade. </em>
   				<% } %>
				</fieldset>
				
			  </div>
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>