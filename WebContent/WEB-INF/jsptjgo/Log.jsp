<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="Logdt" scope="session" class= "br.gov.go.tj.projudi.dt.LogDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Log  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<link type="text/css" rel="stylesheet" href="js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" media="screen"></link>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>

</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Log</h2></div>

		<form action="Log" method="post" name="Formulario" id="Formulario">
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo" alt="Novo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" />
				<input id="imgLocalizar" alt="Localizar" class="imgLocalizar" title="Localizar - Localiza um registro no banco" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>');AlterarValue('PassoEditar','0');" /> 
				<a class="divPortaBotoesLink" href="Ajuda/LogAjuda.html" target="_blank">  
				<img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					
					
					<legend class="formEdicaoLegenda">Visualização dos dados do Log </legend>
					
					<div class="col20">
					<label class="formEdicaoLabel" for="Id_Log">Id_Log</label><br> 
					<input class="formEdicaoInputSomenteLeitura" name="Id_Log"  id="Id_Log"  type="text"  readonly="true" value="<%=Logdt.getId()%>"><br />
					</div>
					
					<div class="col50 clear">
					<label class="formEdicaoLabel" for="DescricaoTabela">Tabela</label><br> 
					<input class="formEdicaoInputSomenteLeitura" name="DescricaoTabela" readonly="true" id="DescricaoTabela"  type="text" size="60" maxlength="60" value="<%=Logdt.getTabela()%>" />
					</div>
					
					<div class="col20">
					<label class="formEdicaoLabel" for="descricaoIdTabela">Id Tabela</label><br> 
					<input class="formEdicaoInputSomenteLeitura" name="idTabela" readonly="true" id="idTabela" type="text" value="<%=Logdt.getId_Tabela()%>" size="20" maxlength="20" /><br />
					</div>
					
					<div class="col40 clear">				
					<label class="formEdicaoLabel" for="Id_LogTipo">LogTipo</label><br>  
					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="LogTipo" id="LogTipo" type="text" size="60" maxlength="60" value="<%=Logdt.getLogTipo()%>"><br />
					</div>
					
					<div class="col15 clear">
					<label class="formEdicaoLabel" for="Data">Data</label><br> 
					<input class="formEdicaoInputSomenteLeitura"  readonly="true" name="Data" id="Data"  type="text" size="10" maxlength="10" value="<%=Logdt.getData()%>"> 
					</div>
					
					<div class="col15">				
					<label class="formEdicaoLabel" for="Hora">Hora</label><br> 
					<input class="formEdicaoInputSomenteLeitura"  readonly="true" name="Hora" id="Hora"  type="text" size="8" maxlength="8" value="<%=Logdt.getSomenteHorario()%>"> 
					</div>
					
					<div class="col30 clear">
					<label class="formEdicaoLabel" for="Id_Usuario">Usuario</label><br>  
					<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Usuario" id="Usuario" type="text" size="30" maxlength="30" value="<%=Logdt.getUsuario()%>"><br />
					</div>
					
					<div class="col40">
					<label class="formEdicaoLabel" for="IpComputador">IP do Computador</label><br> 
					<input class="formEdicaoInputSomenteLeitura" name="IpComputador" readonly="true" id="IpComputador"  type="text" size="30" maxlength="30" value="<%=Logdt.getIpComputador()%>" onkeyup=" autoTab(this,30)"><br />
					</div>
					
					<div class="col100">
						<label  for="Valor_Atual">Valor Anterior</label><br> 
							<textarea rows="7" cols="110" readonly="true">
							 <%=Logdt.getValorAtual()%>
							</textarea>
					</div>	
					<div class="col100">
						<label for="Valor_Novo">Valor Atual</label><br> 
						<textarea rows="7" cols="110" readonly="true"> 
						 <%=Logdt.getValorNovo()%>
						</textarea>
					</div>
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
