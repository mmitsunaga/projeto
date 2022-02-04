<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.LogDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.LogTipoDt"%>

<jsp:useBean id="Logdt" scope="session" class= "br.gov.go.tj.projudi.dt.LogDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Log  </title>
	
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	
	<script type="text/javascript">
		_tempBuscaId = '<%=request.getAttribute("tempBuscaId_Log")%>';
		_tempBuscaDescricao = '<%=request.getAttribute("tempBuscaLog")%>';
		_PaginaEditar = '<%=Configuracao.Editar%>';		
	</script>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
   	<script type='text/javascript' src='./js/jscalendar/dhtmlgoodies_calendar.js'></script>	
	<link href="js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" type="text/css" rel="stylesheet" media="screen" />	
	
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo;|<%=request.getAttribute("tempPrograma")%>| Busca de Log </h2></div>
	<div id="divLocalizar" class="divLocalizar" > 
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
		<fieldset id="formLocalizar" class="formLocalizar"> 
			<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Consulta de Log </legend>
			<div class="col100 ">
				<input type="checkbox"  id="log_erro" name="log_erro" checked="checked" value="true" />Consultar logs de Erros
			</div>
			<div class='col20'>
				<label>Código</label> 
				<input class="formLocalizarInput" id="tempBuscaId" name="Id_Log" type="text" value="<%=Logdt.getId()%>" size="20" maxlength="20" />
			</div>	</br>		
			<div class="col20">
				<label for="DataInicial">Data Inicial <img id="calendarioDataInicial" src="./imagens/dlcalendar_2.gif" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].dataInicial,'dd/mm/yyyy',this)" /></label> 
				<input class="formLocalizarInput" name="dataInicial" id="dataInicial" type="text" size="10" maxlength="10" value="<%=Logdt.getDataInicial()%>" />
			</div>
			<div class="col20">
				<label for="DataFinal">Data Final <img id="calendarioDataFinal" src="./imagens/dlcalendar_2.gif" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].dataFinal,'dd/mm/yyyy',this)" /></label>
				<input class="formLocalizarInput" name="dataFinal" id="dataFinal" type="text" size="10" maxlength="10" value="<%=Logdt.getDataFinal()%>" />
			</div> </br>
			<div class="col20">
				<label for="formIdTabelaLabel">ID Tabela</label>
				<input class="formLocalizarInput" id="idTabela" name="idTabela" type="text" value="<%=Logdt.getId_Tabela()%>" size="20" maxlength="20" />
			</div>
			<div class="col20 ">
				<label >Tabela</label> 
				<input class="formLocalizarInput" id="nomeTabela" name="nomeTabela" type="text" value="<%=Logdt.getTabela()%>" size="60" maxlength="60" />
			</div> </br>	
			<div class="col50">				
				<label for="Id_LogTipo">Tipo Log 
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarLogTipo" name="imaLocalizarLogTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(LogTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>');AlterarValue('PassoEditar','1');" />
					<input class="FormEdicaoimgLocalizar" id="imaLimparLogTipo" name="imaLimparLogTipo" type="image"  src="./imagens//16x16/edit-clear.png"  	  onclick="AlterarValue('id_LogTipo','');AlterarValue('logTipo','');LimparDiv('MensagemErro', '');buscaDados(0,<%=Configuracao.TamanhoRetornoConsulta %>); return false;" />
				</label>
				<input class="formEdicaoInputSomenteLeitura"  readonly="true" name="logTipo" id="tempBuscaDescricao" type="text" size="50" maxlength="60" value="<%=Logdt.getLogTipo()%>" /><br />
				<input type="hidden" id="id_LogTipo" name="id_LogTipo" value="<%=Logdt.getId_LogTipo()%>" />
			</div> 
			</br>		
			<div id="divBotoesCentralizados" class="divBotoesCentralizados">
				<input  id="formLocalizarBotao"   class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" onclick="javascript:buscaDadosLogJSON('0',  <%=request.getAttribute("PaginaAtual")%>, <%=Configuracao.TamanhoRetornoConsulta%> ); return false;" />			
			</div>
			
		</fieldset>
		
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />		       
		<input id="PassoEditar" name="PassoEditar" type="hidden" value="" />				
		
		<div id="divTabela" class="divTabela" > 
			<table id="Tabela" class="Tabela">
				<thead>
					<tr>
						<th></th>
						<th>Código</th>
						<th>Tipo Log</th>
						<th>Data</th>
						<th>Hora</th>
						<th>Tabela</th>						
						<th>Id Tabela</th>	
						<th class="colunaMinima" title="Seleciona o registro para edição">Selecionar</th>
					</tr>
					</thead>
					<tbody id="CorpoTabela">&nbsp;</tbody>
			</table>
		</div> 
		</form> 
	</div> 

	<div id="Paginacao" class="Paginacao"></div> 	
</div> 
<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
