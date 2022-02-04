
<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.relatorios.EstatisticaProcessoServentiaDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>


<jsp:useBean id="EstatisticaProcessoServentiadt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.EstatisticaProcessoServentiaDt"/>




<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Estatística de Processo por Serventia</title>

      <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      
      
      <script type='text/javascript' src='./js/Funcoes.js'></script>
</head>

<body>
  <div id="divCorpo" class="divCorpo" >
  <div class="area"><h2>&raquo; Relatório de Processos por Serventia </h2></div>

<form action="EstatisticaProcessoServentia" method="post" name="Formulario" id="Formulario">
	
  <input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
  <input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
  <div id="divPortaBotoes" class="divPortaBotoes">
  	  <input id="imgNovo"  class="imgNovo" title="Novo Consulta" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >
  	  <input id="imgImprimir"  class="imgImprimir" title="Gerar Relatório" name="imgImprimir" type="image" src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">	
      <a class="divPortaBotoesLink" href="Ajuda/EstatisticaProcessoServentiaAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" alt="Ajuda" /> </a>
  </div/><br />
   <% if (request.getAttribute("PaginaAtual").toString().equalsIgnoreCase(String.valueOf(Configuracao.Editar)))  {%> <%@ include file="EstatisticaProcessoServentiaEditar.jspf"%> 
            <% } else if (request.getAttribute("PaginaAtual").toString().equalsIgnoreCase(String.valueOf(((ServentiaDt.CodigoPermissao - EstatisticaProcessoServentiaDt.CodigoPermissao) * Configuracao.QtdPermissao) + (Configuracao.Localizar + (EstatisticaProcessoServentiaDt.CodigoPermissao * Configuracao.QtdPermissao)))))  {%> <%@ include file="ServentiaLocalizar.jspf"%>
            <% } else if (request.getAttribute("PaginaAtual").toString().equalsIgnoreCase(String.valueOf(((ProcessoTipoDt.CodigoPermissao - EstatisticaProcessoServentiaDt.CodigoPermissao) * Configuracao.QtdPermissao) + (Configuracao.Localizar + (EstatisticaProcessoServentiaDt.CodigoPermissao * Configuracao.QtdPermissao)))))  {%> <%@ include file="ProcessoTipoServentiaSubTipoLocalizar.jspf"%>
 <%} else {%> <%@ include file="Branco.html"%> 
 <%}%>

</form>
<%if (request.getAttribute("MensagemOk").toString().trim().equals("") == false){ %>
	<div class="divMensagemOk" id="MensagemOk"><%=request.getAttribute("MensagemOk").toString().trim()%></div>
<%}%>

<%if (request.getAttribute("MensagemErro").toString().trim().equals("") == false){ %>
	<div class="divMensagemErro" id="MensagemErro"> <%=request.getAttribute("MensagemErro").toString().trim()%></div>
<%}%>
</div>
</body>
</html>