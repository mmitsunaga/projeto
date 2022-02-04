<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="java.util.List"%>


<jsp:useBean id="EstatisticaPendenciaServentiadt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.EstatisticaPendenciaDt"/>




<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>

<html>
<head>
	<title>Estatística de Pendências</title>

      <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
		</style>
      
      
      <script type='text/javascript' src='./js/Funcoes.js'></script>
      <script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	  <script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	  <script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
  		<div class="area"><h2>&raquo; Relatório de Pendências </h2></div>
		<form action="EstatisticaPendencia" method="post" name="Formulario" id="Formulario">
		  <input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
		  <input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
		  <div id="divPortaBotoes" class="divPortaBotoes">
		  	  <input id="imgNovo"  class="imgNovo" title="Novo Consulta" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >
		  	  <input id="imgImprimir"  class="imgImprimir" title="Gerar Relatório" name="imgImprimir" type="image" src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>'); AlterarValue('Id_UsuarioServentia','')">	
		      <a class="divPortaBotoesLink" href="Ajuda/EstatisticaPendenciaServentiaAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" alt="Ajuda" /> </a>
		  </div/><br />
		  <div id="divEditar" class="divEditar">
		    <fieldset class="formEdicao"> 
			    <legend class="formEdicaoLegenda">Período</legend>
				
				<div class="col25">
				<label style="float:left;" for="DataInicial">Data Inicial</label><br> 
				<input style="float:left;" class="formEdicaoInputSomenteLeitura"  readonly name="DataInicial" id="DataInicial"  type="text" size="10" maxlength="10" value="<%=EstatisticaPendenciaServentiadt.getDataInicial()%>"> 
				<img style="float:left;vertical-align:middle;" id="calendarioDataInicial" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataInicial,'dd/mm/yyyy',this)">
				</div>
				<div class="col25">
				<label style="float:left;" for="DataFinal">Data Final</label><br> 
				<input style="float:left;" class="formEdicaoInputSomenteLeitura"  readonly name="DataFinal" id="DataFinal"  type="text" size="10" maxlength="10" value="<%=EstatisticaPendenciaServentiadt.getDataFinal()%>"> 
				<img style="float:left;vertical-align:bottom;" id="calendarioDataFinal" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataFinal,'dd/mm/yyyy',this)">
				</div>
				
			</fieldset>
			<fieldset class="formEdicao"> 
			    <legend class="formEdicaoLegenda">Relatório Pendência por Serventia</legend>
			    <label class="formEdicaoLabel" for="Id_Serventia">Serventia (Vara)
			    <input class="FormEdicaoimgLocalizar" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
			    </label><br>    <input  class="formEdicaoInputSomenteLeitura" id="Id_Serventia"  readonly type="text" size="60" maxlength="60" value="<%=EstatisticaPendenciaServentiadt.getServentia()%>"/><br />
			</fieldset>
			<fieldset class="formEdicao"> 
			    <legend class="formEdicaoLegenda">Relatório Pendência por Usuário</legend>
			    <label class="formEdicaoLabel" for="Id_Serventia">Servidor Judiciário
			    <input class="FormEdicaoimgLocalizar" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > 
			    </label><br>   <input  class="formEdicaoInputSomenteLeitura" id="Id_Usuario"  readonly type="text" size="60" maxlength="60" value="<%=EstatisticaPendenciaServentiadt.getUsuario().getNome()%>"/><br />
			</fieldset>
			<%if (EstatisticaPendenciaServentiadt.getUsuario().getListaUsuarioServentias() != null) { %>
					<%if (EstatisticaPendenciaServentiadt.getUsuario().getId().length() > 0){%>
					<fieldset class="formEdicao">
			    	 	<legend class="formEdicaoLegenda">Serventias do Usuário</legend> 
			    	  		<%@ include file="EstatisticaPendenciaListaServentiasUsuario.jspf"%> 
		    		</fieldset>
		    		<%} else{ %>
		    		<fieldset class="formEdicao">
			    	 	<legend class="formEdicaoLegenda">Usuários da Serventia</legend> 
			    	  		<%@ include file="EstatisticaPendenciaListaUsuariosServentia.jspf"%> 
		    		</fieldset>
		    		<%} %>
		    <%}%> 	
		  </div>
		
		</form>
	</div>
<%if (request.getAttribute("MensagemOk").toString().trim().equals("") == false){ %>
	<div class="divMensagemOk" id="MensagemOk"><%=request.getAttribute("MensagemOk").toString().trim()%></div>
<%}%>

<%if (request.getAttribute("MensagemErro").toString().trim().equals("") == false){ %>
	<div class="divMensagemErro" id="MensagemErro"> <%=request.getAttribute("MensagemErro").toString().trim()%></div>
<%}%>
</body>
</html>