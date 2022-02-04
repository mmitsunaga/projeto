<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AreaDistribuicaoDt"%>


<html>
<head>
	<title>Importação dos Dados do Execpen</title>	
	<meta http-equiv="Content-Type" content="text/html; charset=latin1" />
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');		
	</style>
<!--	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />-->
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type='text/javascript' src='./js/DivFlutuante.js'></script>
	<script language="JavaScript">
		setTimeout('document.Formulario2.submit()',10000);
	</script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Gerar arquivos para Infoseg</h2></div>
    	<form action="Infoseg"  method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>" />
	
			<div id="divEditar" class="divEditar"><br/>
			<label>Os arquivos serão salvos na pasta <%=request.getAttribute("CaminhoArquivo")%>
				<br /><br />Nomenclatura: GO3-? e GO4-?
				<br />Onde:
				<br /> ?: Data de geração do arquivo em aaaa/mm/dd
				<br />GO3: Arquivo com dados dos sentenciados
				<br />GO4: Arquivo com dados dos processos</label><br>
	  		</div>
			<div id="divBotoesCentralizados" class="divBotoesCentralizados">
				<input name="imgInserir" type="submit" value="Gerar Arquivos" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');"> 
		    </div>
	    </form>
	</div>    	
	<%@ include file="Padroes/Mensagens.jspf" %> 
</body>
</html>
