<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<jsp:useBean id="Pendenciadt" scope="session" class="br.gov.go.tj.projudi.dt.PendenciaDt" />
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<html>
<head>
	<title>Pend&ecirc;ncia</title>

	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>      
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<script type="text/javascript" src="./js/tabelas.js"></script>
	<script type="text/javascript" src="./js/tabelaArquivos.js"></script>
	
	
	<script type='text/javascript' src='./dwr/interface/Pendencia.js'></script>
	<%@ include file="js/buscarArquivos.js"%>
	<script type="text/javascript">
		modoEdicaoPendencia = false;
		possivelMarcar = true;
	</script>
		
	<style type="text/css">
		@import url('./css/Principal.css');
		
		div.pendencia {
			padding: 5px;
		}
		
		div.pendencia label.lbl {
			font-weight: bold;
			float: left;
			width: 120px;
			text-align: right;
			padding-right: 10px;
		}
	</style>
</head>
<body>
  <div id="divCorpo" class="divCorpo" >
  	<div class="area"><h2>&raquo; Detalhes da Pend&ecirc;ncia (<%=Pendenciadt.getId()%>)</h2></div>
	<form action="Pendencia" method="post" name="Formulario" id="Formulario">
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		<input type="hidden" name="Id_Pendencia" value="<%=Pendenciadt.getId()%>" />

			<br /><br />
			<fieldset class="formEdicao"  > 
				<div style="text-align: center;" >
					<label>Tipo: <%=Pendenciadt.getPendenciaTipo()%> </label><br>
					<br />
					<label >Status: <%=Pendenciadt.getPendenciaStatus()%></label><br>
					<br />
				</div>
					
				<div style="text-align: center;" >
					<label >Data Início de Acesso:</label><br>
					<%=Pendenciadt.getDataInicio()%>
					<br />
					<%if (!Pendenciadt.getDataLimite().equals("")){%>
						<label>Data Limite de Acesso:</label><br>
						<%=Pendenciadt.getDataLimite()%>						
						<br />
					<%}%>
				</div>
				<div style="text-align: center;" >
			<br /><br />
			<label class="formEdicaoLabel">Para ter acesso ao processo na íntegra clique no númeo do processo:</label><br>
				<a href="Pendencia?PaginaAtual=<%=Configuracao.LocalizarDWR%>&amp;Id_Processo=<%=Pendenciadt.getId_Processo()%>"> 
				<%=Pendenciadt.getProcessoNumero()%>
				</a>
				<br /><br />
			</div>	
		</fieldset>
  </form>
 </div>
</body>
</html>