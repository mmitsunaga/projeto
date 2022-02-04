<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.LogDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="Logdt" scope="session" class= "br.gov.go.tj.projudi.dt.LogDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Log por Data </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<link type="text/css" rel="stylesheet" href="js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" media="screen"></link>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Consulta de Log por Data</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="Log" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()"/>
<%} else {%>
		<form action="Log" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="ConsultaLogData" name="ConsultaLogData" type="hidden" value="" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="" />
			<input name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo" alt="Novo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="javascript:limparTela();" />
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Consulta de Log por Data</legend>
					
					<label class="formEdicaoLabel" for="Data">Data</label><br> 
			    	<input class="formEdicaoInput" name="Data" id="Data" type="text" size="10" maxlength="10" value="<%=Logdt.getData()%>" onkeyup="mascara_data(this)" onblur="verifica_data(this)"> 
			    	<img id="calendarioRgDataExpedicao" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário" alt="Calendário" onclick="displayCalendar(document.forms[0].Data,'dd/mm/yyyy',this)"><br>
          			<div class="space"></div>
          			<input id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>'); AlterarValue('ConsultaLogData','1')"/>
			    	<br/>	
			    	
			    	<%
			    	if(Logdt != null && Logdt.getListaLogData() != null) {
			    		int posicaoPaginaAtual = Funcoes.StringToInt(request.getAttribute("PosicaoPaginaAtual").toString());
			    		int qtdeTotalPaginas = Funcoes.StringToInt(request.getAttribute("qtdeTotalPaginas").toString());
			    		for (int i = 0; i < Logdt.getListaLogData().size(); i++) {
			    		LogDt itemLista = (LogDt)Logdt.getListaLogData().get(i);
			    	%>          	          
			    	
			         <fieldset class="formEdicao"  > 
						<legend class="formEdicaoLegenda">Dados do Log </legend>
						
						<% if(posicaoPaginaAtual == 0){ %>
							<label class="formEdicaoLabel" for="posicao"><b>Posição: <%=i +1%></b></label><br><br />
						<%} else { %>
							<label class="formEdicaoLabel" for="posicao"><b>Posição: <%=(posicaoPaginaAtual*Configuracao.TamanhoRetornoConsulta)+(i +1)%></b></label><br><br />
						<%} %>

						<label class="formEdicaoLabel" for="qtd_erros_dia"><b>Qtde erros</b></label> <input class="formEdicaoInputSomenteLeitura" name="qtd_erros_dia" id="qtd_erros_dia"  type="text"  readonly="true" value="<%=itemLista.getQtdErrosDia()%>">						
						<label class="formEdicaoLabel" for="LogTipo">Id Log</label>					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="IdLog" id="IdLog" type="text" size="20" maxlength="20" value="<%=itemLista.getId()%>"><br />						
						<label class="formEdicaoLabel" for="DescricaoTabela">Tabela</label>			<input class="formEdicaoInputSomenteLeitura" name="DescricaoTabela" readonly="true" id="DescricaoTabela"  type="text" size="60" maxlength="60" value="<%=itemLista.getTabela()%>" />						
						<label class="formEdicaoLabel" for="DescricaoTabela">Id Tabela</label>		<input class="formEdicaoInputSomenteLeitura" name="IdTabela" readonly="true" id="IdTabela"  type="text" size="20" maxlength="20" value="<%=itemLista.getId_Tabela()%>" /><br />						
						<label class="formEdicaoLabel" for="LogTipo">Tipo de Log</label>			<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="LogTipo" id="LogTipo" type="text" size="60" maxlength="60" value="<%=itemLista.getLogTipo()%>"><br />
						<label class="formEdicaoLabel" for="DescricaoTabela">Data</label>			<input class="formEdicaoInputSomenteLeitura" name="Data" readonly="true" id="Data"  type="text" size="20" maxlength="20" value="<%=itemLista.getData()%>" />						
						<label class="formEdicaoLabel" for="DescricaoTabela">Hora</label>			<input class="formEdicaoInputSomenteLeitura" name="Hora" readonly="true" id="Hora"  type="text" size="20" maxlength="20" value="<%=itemLista.getHora()%>" /><br />						
						<label class="formEdicaoLabel" for="LogTipo">Usuário</label>				<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Usuario" id="Usuario" type="text" size="40" maxlength="40" value="<%=itemLista.getId_Usuario()%>">						
						<label class="formEdicaoLabel" for="LogTipo">IP Computador</label>			<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="IpComputador" id="IpComputador" type="text" size="20" maxlength="20" value="<%=itemLista.getIpComputador()%>"><br />					
						<label class="formEdicaoLabel" for="Valor_Novo">Valor Novo</label><br> 
						<textarea rows="7" cols="110" readonly="true"><%=itemLista.getValorNovo()%></textarea>
					</fieldset> 	

					<%	} %>
					 
		  			<div id="Paginacao" class="Paginacao"> 
						  <b>P&aacute;gina </b>
						  <%="<a href=\"Log?PaginaAtual=6&amp;ConsultaLogData=1&amp;PosicaoPaginaAtual=0\">Primeira</a>"%>
						  
					<%
						long loPaginaAtual = Funcoes.StringToLong(request.getAttribute("PosicaoPaginaAtual").toString()); 
						long total = Funcoes.StringToLong(request.getAttribute("qtdeTotalPaginas").toString());  	
						
						//total = (total / Configuracao.TamanhoRetornoConsulta);
						//Determina quantas pag.serão necessarias de acordo com a qtde de registros.
						if ((total%Configuracao.TamanhoRetornoConsulta) != 0){
							total=total/Configuracao.TamanhoRetornoConsulta+1;
						}else{
							total=total/Configuracao.TamanhoRetornoConsulta;
						}
						//Conta a qtde de registros que aparecerá em cada tela
						long loConte =1;
						//Guarda a última página selecionada
						long loPaginaSelecionada= loPaginaAtual;
						
						//determino onde vai começar a contagem de páginas
						long loPaginaInicial= loPaginaAtual - (Configuracao.TamanhoRetornoConsulta / 2 );
						if (loPaginaInicial<1) loPaginaInicial = 1;
						
						long loPaginaFinal = loPaginaInicial + Configuracao.TamanhoRetornoConsulta -1;
						
						if (loPaginaFinal > total)
							loPaginaFinal = total;
						
						if (loPaginaInicial > (loPaginaFinal - (Configuracao.TamanhoRetornoConsulta -1)))
							loPaginaInicial = loPaginaFinal - (Configuracao.TamanhoRetornoConsulta -1); 
						
						if (loPaginaInicial<1) loPaginaInicial = 1;
							
						//Aparecerá uma qtde de páginas a serem escolhidas, sendo que a ultima pag. escolhida ficara sempre marcada
						//Ex. : 1 2 3 4 5 |6| 7 8 9 10 (pag.escolhida 6)
						loPaginaAtual = loPaginaInicial;
						while(loPaginaAtual<=loPaginaFinal){		 			   
							if ((loPaginaAtual-1)==loPaginaSelecionada){ %> 
										<%="<b>| " + (loPaginaAtual) + " |</b>"%>    					
							<%		} else { %>
										<a href="Log?PaginaAtual=6&amp;ConsultaLogData=1&amp;PosicaoPaginaAtual=<%=loPaginaAtual-1%>">&nbsp;<%=loPaginaAtual%></a>
							<%		} 
							loPaginaAtual++;			
						}	%>
						 <a href="Log?PaginaAtual=6&amp;ConsultaLogData=1&amp;PosicaoPaginaAtual=<%=loPaginaAtual-2%>">&nbsp;Última</a> 
				</div>
				<% }%>
				</fieldset>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
