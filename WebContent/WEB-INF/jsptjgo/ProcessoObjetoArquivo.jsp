<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.ProcessoObjetoArquivoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="ProcessoObjetoArquivodt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoObjetoArquivoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ObjetoSubtipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.BairroDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EnderecoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Objeto Arquivado do Processo </title>
	
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='js/jquery.js'> </script>
	<script type='text/javascript' src='js/ui/jquery-ui.min.js'></script>
	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	<script type='text/javascript' src='./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
	<script type='text/javascript' src='js/FormProcessoObjetoMovimentacao.js'> </script>
	
	<link href="js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" type="text/css" rel="stylesheet" media="screen" />

</head>

<body onload="mostrarMovimentacaoObjeto('<%=ProcessoObjetoArquivodt.getId()%>')">

	<div id="divCorpo" class="divCorpo" >
		<div id="divTitulo" class="divTitulo"> Cadastro de Objeto Arquivado do Processo</div>
		<div id="divEditar" class="divEditar">
		<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
			<form action="ProcessoObjetoArquivo" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
		<%} else {%>
			<form action="ProcessoObjetoArquivo" method="post" name="Formulario" id="Formulario">
		<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<div id="divPortaBotoes" class="divPortaBotoes">
					<%@ include file="Padroes/Botoes.jspf"%>
					<input id="imgNovo" alt="Novo"  class="imgNovo" title="Novo Objeto no mesmo lote" name="imaNovo" type="image" src="./imagens/imgNovo.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>')" />
					<input id="imgImprimir" class="imgImprimir" name="imgImprimir" alt="Imprimir" title="Imprimir Termo de Depósito" type="image" src="./imagens/imgImprimir.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">					
					<a class="divPortaBotoesLink" href="Ajuda/ProcessoObjetoArquivoAjuda.html" target="_blank">  
						<img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> 
					</a>
				</div>
				<fieldset class="formEdicao" id="Campos_Objetos" > 
					<legend class="formEdicaoLegenda">Edita os dados do Objeto Arquivado do Processo</legend>
					<input id='Id_Processo' name='Id_Processo' type='hidden' value='<%=ProcessoObjetoArquivodt.getId_Processo()%>' />
					<input id='Id_ObjetoSubtipo' name='Id_ObjetoSubtipo' type='hidden' value='<%=ProcessoObjetoArquivodt.getId_ObjetoSubtipo()%>' />
					<input id='Id_Delegacia' name='Id_Delegacia' type='hidden' value='<%=ProcessoObjetoArquivodt.getId_Delegacia()%>' />
					
					<div class='col10'>
						<label  for="Id_ProcObjetoArq">Identificador</label> 
						<input class="formEdicaoInputSomenteLeitura" name="Id_ProcObjetoArq"  id="Id_ProcObjetoArq"  type="text"  readonly="true" value="<%=ProcessoObjetoArquivodt.getId()%>">
					</div>
					<div class='col10'>
						<label  for="CodigoLote">*Código do Lote</label> 
						<input class="formEdicaoInputSomenteLeitura" name="CodigoLote" id="CodigoLote"  type="text" readonly="true"  value="<%=ProcessoObjetoArquivodt.getCodigoLote()%>">
					</div>
					<br />
					<div class='col40'>
						<label  for="ProcObjetoArq">*Descrição do Objeto</label> 
						<input class="formEdicaoInput" name="ProcObjetoArq" id="ProcObjetoArq"  type="text" size="60" maxlength="60" value="<%=ProcessoObjetoArquivodt.getProcObjetoArq()%>" onkeyup=" autoTab(this,60)">
					</div>
					<div class='col40'>
						<label  for="Id_ObjetoSubtipo">*Tipo do Objeto
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarObjetosubtipo" name="imaLocalizarObjetosubtipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="MostrarBuscaPadrao('Campos_Objetos','ProcessoObjetoArquivo','Consulta de Objeto subtipo', 'Digite o Objeto subtipo e clique em consultar.', 'Id_ObjetoSubtipo', 'ObjetoSubtipo', ['Objeto Subtipo'], ['Objeto Tipo'], '<%=(ObjetoSubtipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" > 
						<input class="FormEdicaoimgLocalizar" id="imaLimparObjetosubtipo" name="imaLimparObjetosubtipo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_ObjetoSubtipo','ObjetoSubtipo'); return false;" >
						</label> 
						<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="ObjetoSubtipo" id="ObjetoSubtipo" type="text" size="60" maxlength="255" value="<%=ProcessoObjetoArquivodt.getObjetoSubtipo()%>">
					</div>
					<br />
					<div class='col40'>
						<label  for="ProcObjetoArq">*Nome do Depositante</label> 
						<input class="formEdicaoInput" name="NomeDepositante" id="NomeDepositante"  type="text" size="60" maxlength="60" value="<%=ProcessoObjetoArquivodt.getNomeDepositante()%>" onkeyup=" autoTab(this,60)">
					</div>
				
					<div class='col40'>
						<label  for="Id_Delegacia">Delegacia / Local de Origem
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="MostrarBuscaPadrao('Campos_Objetos','ProcessoObjetoArquivo','Consulta de Serventia', 'Digite o Serventia e clique em consultar.', 'Id_Delegacia', 'Delegacia', ['Serventia'], ['Serventia'], '<%=(ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" > 
						<input class="FormEdicaoimgLocalizar" id="imaLimparServentia" name="imaLimparServentia" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Delegacia','Delegacia'); return false;" > 					
						</label>  
						<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Delegacia" id="Delegacia" type="text" size="60" maxlength="60" value="<%=ProcessoObjetoArquivodt.getDelegacia()%>"><br />
					</div>
					<br />
					<div class='col20'>
						<label  for="Id_Proc">*Processo<input class="FormEdicaoimgLocalizar" id="imaLocalizarProcesso" name="imaLocalizarProcesso" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="MostrarBuscaPadrao('Campos_Objetos','ProcessoObjetoArquivo','Consulta de Processo', 'Digite o Número do Processo e clique em consultar.', 'Id_Processo', 'Processo', ['Número Processo'], [], '<%=(ProcessoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" > </label> 
						<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Processo" id="Processo" type="text" size="30" maxlength="30" value="<%=ProcessoObjetoArquivodt.getProcNumero()%>"><br />
					</div> 
					<div class='col10'>
						<label  for="Placa">Inquérito</label> <input class="formEdicaoInput" name="Inquerito" id="Inquerito"  type="text" size="10" maxlength="10" value="<%=ProcessoObjetoArquivodt.getInquerito()%>" onkeyup=" autoTab(this,10)">
					</div>
					
					<div class='col15'>
						<label  for="DataEntrada">Data Entrada <img id="calendarioDataEntrada" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataEntrada,'dd/mm/yyyy',this)"/></label>
						<input class="formEdicaoInput" name="DataEntrada" id="DataEntrada" type="text" size="18" maxlength="10" value="<%=ProcessoObjetoArquivodt.getDataEntrada()%>" onblur="verifica_data(this);" onKeyPress="return DigitarSoNumero(this, event)" OnKeyUp="mascara_data(this)">
					</div>
					<br />
					<div class='col10'>
						<label  for="Placa">Placa</label> <input class="formEdicaoInput" name="Placa" id="Placa"  type="text" size="10" maxlength="10" value="<%=ProcessoObjetoArquivodt.getPlaca()%>" onkeyup=" autoTab(this,10)">
					</div>
					<div class='col10'>
						<label  for="Chassi">Chassi</label> <input class="formEdicaoInput" name="Chassi" id="Chassi"  type="text" size="20" maxlength="20" value="<%=ProcessoObjetoArquivodt.getChassi()%>" onkeyup=" autoTab(this,20)">
					</div>
					<div class='col10'>
						<label  for="Modulo">Módulo</label> <input class="formEdicaoInput" name="Modulo" id="Modulo"  type="text" size="10" maxlength="5" value="<%=ProcessoObjetoArquivodt.getModulo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)">
					</div>
					<div class='col10'>
						<label  for="Perfil">Perfil</label> <input class="formEdicaoInput" name="Perfil" id="Perfil"  type="text" size="10" maxlength="1" value="<%=ProcessoObjetoArquivodt.getPerfil()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)">
					</div>
					<div class='col10'>
						<label  for="Nivel">Nível</label> <input class="formEdicaoInput" name="Nivel" id="Nivel"  type="text" size="10" maxlength="2" value="<%=ProcessoObjetoArquivodt.getNivel()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)">
					</div>
					<div class='col10'>
						<label  for="Unidade">Unidade</label> <input class="formEdicaoInput" name="Unidade" id="Unidade"  type="text" size="10" maxlength="5" value="<%=ProcessoObjetoArquivodt.getUnidade()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)">
					</div>
					<div class='col10'>
						<label  for="NumeroRegistro">N&#x00BA; de Registro</label> 
						<input class="formEdicaoInput" name="NumeroRegistro" id="NumeroRegistro"  type="text" size="40" maxlength="40" value="<%=ProcessoObjetoArquivodt.getNumeroRegistro()%>" onkeyup=" autoTab(this,40)">
					</div>
						
			    	<br />
			    	<br />
									
					<% if (ProcessoObjetoArquivodt.getId() != null && !ProcessoObjetoArquivodt.getId().equalsIgnoreCase(""))  {%> 
						<fieldset id="idDadosRecebedor">
							<legend class="formEdicaoLegenda"> Dados da devolução do objeto </legend>
							<div class='col30'> 
								<label  for="NomeRecebedor">*Nome</label> <input class="formEdicaoInput" name="NomeRecebedor" id="NomeRecebedor"  type="text" size="60" maxlength="60" value="<%=ProcessoObjetoArquivodt.getNomeRecebedor()%>" onkeyup=" autoTab(this,60)">
							</div>
							<div class='col20'>
								<label  for="CpfRecebedor">*CPF</label> <input class="formEdicaoInput" name="CpfRecebedor" id="CpfRecebedor"  type="text" size="11" maxlength="11" value="<%=ProcessoObjetoArquivodt.getCpfRecebedor()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup="autoTab(this,22)">
							</div>
							<div class='col20'>
								<label  for="RgRecebedor">*RG</label> <input class="formEdicaoInput" name="RgRecebedor" id="RgRecebedor"  type="text" size="20" maxlength="30" value="<%=ProcessoObjetoArquivodt.getRgRecebedor()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup="autoTab(this,60)">
							</div>
						
							<div class='col30'>
								<input id='Id_Rgorgaoexpedidor' name='Id_Rgorgaoexpedidor' type='hidden' value='<%=ProcessoObjetoArquivodt.getId_RgOrgaoExpRecebedor()%>' />
								<label  for="Id_RgOrgaoExpRecebedor">*Órgão Expedidor  
									<input class="FormEdicaoimgLocalizar" id="imaLocalizarRgorgaoexpedidor" name="imaLocalizarRgorgaoexpedidor" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="MostrarBuscaPadrao('Campos_Objetos','ProcessoObjetoArquivo','Consulta de Rgorgaoexpedidor', 'Digite o orgão expedidor e clique em consultar.', 'Id_Rgorgaoexpedidor', 'Rgorgaoexpedidor', ['Sigla Orgão Expedior'], ['UF'],'<%=(RgOrgaoExpedidorDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;" > 
									<input class="FormEdicaoimgLocalizar" id="imaLimparRgorgaoexpedidor" name="imaLimparRgorgaoexpedidor" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Rgorgaoexpedidor','Rgorgaoexpedidor'); return false;" >
								</label> 					
								<input  name='Id_Rgorgaoexpedidor' id='Id_Rgorgaoexpedidor' type='hidden'  value=''>
								<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Rgorgaoexpedidor" id="Rgorgaoexpedidor" type="text" size="60" maxlength="60" value="<%=ProcessoObjetoArquivodt.getRgOrgaoExp()%>"><br />
							</div>
							<br />
							<fieldset id="idCadastroEndereco"> 
								<legend class="formEdicaoLegenda">Endereço</legend>							 
								<div class="col100">   				
									<label  for="Logradouro">*Logradouro</label> 
									<input class="formEdicaoInput" name="Logradouro" id="Logradouro"  type="text" size="106" maxlength="100" value="<%=ProcessoObjetoArquivodt.getLogradouro()%>" onKeyUp=" autoTab(this,60)">
								</div>					    		
						    	<div class="col15 clear"> 			
						    		<label  for="Numero">Número</label> 
						    		<input class="formEdicaoInput" name="Numero" id="Numero"  type="text" size="11" maxlength="10" value="<%=ProcessoObjetoArquivodt.getNumero()%>" onKeyPress="return DigitarSoNumero(this, event)" onKeyUp=" autoTab(this,11)">
								</div>							
								<div class="col45"> 	
									<label  for="Complemento">Complemento</label> 
									<input class="formEdicaoInput" name="Complemento" id="Complemento"  type="text" size="59" maxlength="100" value="<%=ProcessoObjetoArquivodt.getComplemento()%>" onKeyUp=" autoTab(this,255)">
								</div>								
								<div class="col100 clear"> 
									<label  for="Bairro">*Bairro
										<input class="FormEdicaoimgLocalizar" id="imaLocalizarBairro" name="imaLocalizarBairro" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
									</label>  						
									<input class="formEdicaoInputSomenteLeitura"  readonly name="Bairro" id="Bairro" type="text" size="80" maxlength="100" value="<%=ProcessoObjetoArquivodt.getBairro()%>">
								</div>					
								<div class="col45 clear"> 
									<label  for="Cidade">Cidade</label>  
									<input class="formEdicaoInputSomenteLeitura" readonly name="Cidade" id="Cidade" type="text" size="40" maxlength="60" value="<%=ProcessoObjetoArquivodt.getCidade()%>">
								</div>							
								<div class="col15"> 			
						   			<label  for="Uf">UF</label> 
									<input class="formEdicaoInputSomenteLeitura" readonly name="Uf" id="Uf"  type="text" size="10" maxlength="10" value="<%=ProcessoObjetoArquivodt.getUf()%>">
								</div>							
								<div class="col15"> 
									<label  for="Cep">*CEP</label> 
									<input class="formEdicaoInput" name="Cep" id="Cep"  type="text" size="11" maxlength="8" value="<%=ProcessoObjetoArquivodt.getCep()%>" onKeyPress="return DigitarSoNumero(this, event)" onKeyUp=" autoTab(this,11)">
								</div>
							</fieldset>
							
			    			<div class='col15'>
								<label  for="DataEntrada">Data Devolução/ Baixa <img id="calendarioDataBaixa" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataBaixa,'dd/mm/yyyy',this)"/></label>
								<input class="formEdicaoInput" name="DataBaixa" id="DataBaixa" type="text" size="18" maxlength="10" value="<%=ProcessoObjetoArquivodt.getDataBaixa()%>" onblur="verifica_data(this);" onKeyPress="return DigitarSoNumero(this, event)" OnKeyUp="mascara_data(this)">
							</div>
							
							<div class="col10" >
							    <label  for="StatusBaixa">Status</label> 
			    				<% if(ProcessoObjetoArquivodt.getStatusBaixa().equalsIgnoreCase("1") || ProcessoObjetoArquivodt.getDataBaixa().length()>0){%>
									<span class="span1">  <font color="red"><b>Baixado</b></font></span>						
								<%}%>
							</div>			
				  
						</fieldset>		
						
							<fieldset id="idDadosLeilao">
					<legend class="formEdicaoLegenda"> Dados de Leilão</legend>
						    		
		    		<div class="col10" >
			    		<label for="StatusLeilao" style="border-bottom-style: solid; border-bottom-color: #e1e1e1; border-bottom-width: 1">Em Leilão</label>		    		
					     <input class="formEdicaoInput"  type="text" disabled="disabled" value="<%=ProcessoObjetoArquivodt.isEmLeilao()?"sim":"não"%>"> 			    		
			    	</div>
			    	
			    	<div class="col15">
			    		<label for="Leilao" style="border-bottom-style: solid; border-bottom-color: #e1e1e1; border-bottom-width: 1">Favorecido Leilão</label>
			    		<input class="formEdicaoInput"  type="text" disabled="disabled" value="<%=ProcessoObjetoArquivodt.getFavorecidoLeilao()%>">					     
			    	</div>
			    	
			    	    	</fieldset>											
					 <%} %>
				</fieldset>		 
			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
		</form>
		 <% if (ProcessoObjetoArquivodt.getId() != null && !ProcessoObjetoArquivodt.getId().equalsIgnoreCase(""))  {%> 
			<fieldset id="idProcessoObjetoArquivoMovimentacao" > 
				<legend class="formEdicaoLegenda">Editar os dados de Movimentação do Objeto <img src="./imagens/imgAtualizar.png" onclick="atualizarMovimentacaoObjeto('<%=ProcessoObjetoArquivodt.getId()%>')"/></legend>						
				<div id='sub_objetos_movimentos' class='DivInvisivel'>						
				</div>						
			</fieldset>			
		<%} %> 
		</div>	
	<%@ include file="Padroes/Mensagens.jspf" %>
	</div>
</body>
</html>
