<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.PonteiroLogCompensarDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="ponteiroLogCompensarDt" scope="session" class= "br.gov.go.tj.projudi.dt.PonteiroLogCompensarDt"/>

<%@page import="br.gov.go.tj.projudi.dt.AreaDistribuicaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AreaDistribuicaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
	</style>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de PonteiroLogCompensar  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
	<script type="text/javascript">
		function ocultarMostrarCampos(telaEdicao){					
			if (telaEdicao != null && telaEdicao == 1){
				$('#Justificativa').attr('disabled', true); 
				$('#Qtd').attr('disabled', true);
				$('#DataInicio').attr('disabled', true);
				$('#imgsalvar').attr('disabled', true);
				$('#calendarioDataInicio').hide();
				$('.FormEdicaoimgLocalizar').hide();
			}else{
				$('#Justificativa').attr('disabled', false);
				$('#Qtd').attr('disabled', false);
				$('#calendarioDataInicio').show(); 
				$('.FormEdicaoimgLocalizar').show();		
			}				
		}
	</script>
</head>

<body onload="ocultarMostrarCampos(<%=request.getAttribute("telaEdicao")%>);">  
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2> Cadastro de Ponteiro Log de Compensação</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="PonteiroLogCompensar" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="PonteiroLogCompensar" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<input name="telaEdicao" id="telaEdicao" type="hidden" value="<%=request.getAttribute("telaEdicao")%>" />
			<input name="idRetorno" id="idRetorno" type="hidden" value="<%=request.getAttribute("idRetorno")%>" />
			<input name="descricaoRetorno" id="descricaoRetorno" type="hidden" value="<%=request.getAttribute("descricaoRetorno")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo" alt="Novo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" />
				<input id="imgsalvar" alt="Salvar" class="imgsalvar" title="Salvar - Salva os dados digitados" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>')" />  
				<input id="imgLocalizar" alt="Localizar" class="imgLocalizar" title="Localizar - Localiza um registro no banco" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')" /> 
				<input id="imgAtualizar" alt="Atualizar" class="imgAtualizar" title="Atualizar - Atualiza os dados da tela" name="imaAtualizar" type="image"  src="./imagens/imgAtualizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')" />				
				<a class="divPortaBotoesLink" href="Ajuda/PonteiroLogCompensarAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados do Ponteiro Log de Compensação </legend>
					<label class="formEdicaoLabel lbTelaCompensacao" for="Id_PonteiroLogCompensar">Identificador</label> 
					<input class="formEdicaoInputSomenteLeitura" name="Id_PonteiroLogCompensar"  id="Id_PonteiroLogCompensar" size="10"  type="text"  readonly="true" value="<%=ponteiroLogCompensarDt.getId()%>"><br />

					<label class="formEdicaoLabel lbTelaCompensacao" for="Justificativa">*Justificativa</label> 
					<input class="formEdicaoInput" name="Justificativa" id="Justificativa"  type="text" size="60" maxlength="4000" value="<%=ponteiroLogCompensarDt.getJustificativa()%>" onkeyup=" autoTab(this,4000)"><br />

					<fieldset class="formEdicao"> 
						<legend class="formEdicaoLegenda">*Ponteiro de Origem - obrigatório o preenchimento dos três campos</legend>
						<label for="Aviso" style="float:left;margin-left:25px;color:red;"><small>Ponteiro que, ao receberem um processo, gerará a compensação no ponteiro de distribuição de destino.</small></label><br>
						<label class="formEdicaoLabel lbTelaCompensacao" for="Id_AreaDistribuicao_O">Área de Distribuição Origem</label>  
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarAreadistribuicao" name="imaLocalizarAreadistribuicao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(AreaDistribuicaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>');AlterarValue('idRetorno','Id_AreaDistribuicao_O');AlterarValue('descricaoRetorno','AreaDistribuicao_O')" > 					
						<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="AreaDistribuicao_O" id="AreaDistribuicao_O" type="text" size="60" maxlength="100" value="<%=ponteiroLogCompensarDt.getAreaDistribuicao_O()%>"><br />
	
						<label class="formEdicaoLabel lbTelaCompensacao" lbTelaCompensacao for="Id_Serventia_O">Serventia de Origem</label>  
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>');AlterarValue('idRetorno','Id_Serventia_O');AlterarValue('descricaoRetorno','Serventia_O')" > 					
						<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Serventia_O" id="Serventia_O" type="text" size="60" maxlength="60" value="<%=ponteiroLogCompensarDt.getServentia_O()%>"><br />
	
						<label class="formEdicaoLabel lbTelaCompensacao" for="Id_ServentiaCargo_O">Serventia Cargo Origem</label>  
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentiacargo" name="imaLocalizarServentiacargo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>');AlterarValue('idRetorno','Id_ServentiaCargo_O');AlterarValue('descricaoRetorno','ServentiaCargo_O')" > 				
	   				 	<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="ServentiaCargo_O" id="ServentiaCargo_O" type="text" size="60" maxlength="60" value="<%=ponteiroLogCompensarDt.getServentiaCargo_O()%>"><br />
					</fieldset>
					
					<fieldset class="formEdicao"> 
						<legend class="formEdicaoLegenda">*Ponteiro de Destino - obrigatório o preenchimento dos três campos</legend>
						<label for="Aviso" style="float:left;margin-left:25px;color:red;"><small>Ponteiro que será compensado, na quantidade informada abaixo, quando o ponteiro de origem receber um processo.</small></label><br>
						<label class="formEdicaoLabel lbTelaCompensacao" for="Id_AreaDistribuicao_D">Área de Distribuição Destino</label>  
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarAreadistribuicao" name="imaLocalizarAreadistribuicao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(AreaDistribuicaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>');AlterarValue('idRetorno','Id_AreaDistribuicao_D');AlterarValue('descricaoRetorno','AreaDistribuicao_D')" > 					
						<input class="formEdicaoInputSomenteLeitura"  readonly="true" name="AreaDistribuicao_D" id="AreaDistribuicao_D" type="text" size="60" maxlength="100" value="<%=ponteiroLogCompensarDt.getAreaDistribuicao_D()%>"><br />
	
						<label class="formEdicaoLabel lbTelaCompensacao" for="Id_Serventia_D">Serventia de Destino</label>  
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>');AlterarValue('idRetorno','Id_Serventia_D');AlterarValue('descricaoRetorno','Serventia_D')" > 					
						<input class="formEdicaoInputSomenteLeitura"  readonly="true" name="Serventia_D" id="Serventia_D" type="text" size="60" maxlength="60" value="<%=ponteiroLogCompensarDt.getServentia_D()%>"><br />
	
						<label class="formEdicaoLabel lbTelaCompensacao" for="Id_ServentiaCargo_D">Serventia Cargo Destino</label>  
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentiacargo" name="imaLocalizarServentiacargo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaCargoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>');AlterarValue('idRetorno','Id_ServentiaCargo_D');AlterarValue('descricaoRetorno','ServentiaCargo_D')" >
						<input class="formEdicaoInputSomenteLeitura"  readonly="true" name="ServentiaCargo_D" id="ServentiaCargo_D" type="text" size="60" maxlength="60" value="<%=ponteiroLogCompensarDt.getServentiaCargo_D()%>"><br />
					</fieldset>
					
					<label class="formEdicaoLabel lbTelaCompensacao" for="Qtd">*Quantidade</label> 
					<input class="formEdicaoInput" name="Qtd" id="Qtd"  type="text" size="10" maxlength="22" value="<%=ponteiroLogCompensarDt.getQtd()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,22)"><br />

					<fieldset class="formEdicao"> 
						<legend class="formEdicaoLegenda">*Início de vigência da compensação</legend>
						<%-- <label class="formEdicaoLabel lbTelaCompensacao" for="Id_UsuarioServentia_I">Usuário Cadastrador</label>  
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarUsuarioserventia" name="imaLocalizarUsuarioserventia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>');AlterarValue('idRetorno','Id_UsuarioServentia_I');AlterarValue('descricaoRetorno','Usuario_I')" >		
						<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Usuario_I" id="Usuario_I" type="text" size="60" maxlength="30" value="<%=ponteiroLogCompensarDt.getUsuario_I()%>">
						<br/> --%>
						<label class="formLocalizarLabel lbTelaCompensacao" for="DataInicio">Data de Início</label>
					    <input class="formLocalizarInput" name="DataInicio" id="DataInicio" type="text" size="10" maxlength="10" value="<%=ponteiroLogCompensarDt.getDataInicio()%>" onload="displayCalendar(document.forms[0].DataInicio,'dd/mm/yyyy',this)" /> 
					    <img id="calendarioDataInicio" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataInicio,'dd/mm/yyyy',this)" />
						<br/>
					</fieldset>

					<fieldset class="formEdicao"> 
						<legend class="formEdicaoLegenda">Fim da vigência da compensação (Preenchimento automático)</legend>
						
						<%if (ponteiroLogCompensarDt.getId() != null && !ponteiroLogCompensarDt.getId().equalsIgnoreCase("")){
							if(ponteiroLogCompensarDt.getDataInicio() != null && !ponteiroLogCompensarDt.getDataInicio().equalsIgnoreCase("")
									&& (ponteiroLogCompensarDt.getDataFinal()== null || ponteiroLogCompensarDt.getDataFinal().equalsIgnoreCase(""))){%>
									<label for="Aviso" style="float:left;margin-left:25px;color:blue;">Este ponteiro de compensação encontra-se ATIVO e foi cadastrado por <%=ponteiroLogCompensarDt.getUsuario_I()%>. <br />Para finalizar o ponteiro de compensação, clique aqui: 
							 		<input id="btFinalizar" alt="Finalizar" class="imgexcluir" title="Finalizar ponteiro de compensação" name="btFinalizar" type="image" src="./imagens/imgExcluir.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Excluir)%>')" />  </label>
							<%} else if(ponteiroLogCompensarDt.getDataFinal()== null || !ponteiroLogCompensarDt.getDataFinal().equalsIgnoreCase("")) {%>
									<label for="Aviso" style="float:left;margin-left:25px;color:blue;">Este ponteiro de compensação foi FINALIZADO em <%=ponteiroLogCompensarDt.getDataFinal()%> por <%=ponteiroLogCompensarDt.getUsuario_F()%>.</label><br>
							<%}%>
						<%}%>
						<%-- <label class="formEdicaoLabel lbTelaCompensacao" for="Id_UsuarioServentia_F">Usuário Finalizador</label>  
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarUsuarioserventia" name="imaLocalizarUsuarioserventia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>');AlterarValue('idRetorno','Id_UsuarioServentia_F');AlterarValue('descricaoRetorno','Usuario_F')" >			
						<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="Usuario_F" id="Usuario_F" type="text" size="60" maxlength="30" value="<%=ponteiroLogCompensarDt.getUsuario_F()%>">
						<br/>
						<label class="formLocalizarLabel lbTelaCompensacao" for="DataFinal">Data Final</label>
					    <input class="formLocalizarInput" name="DataFinal" id="DataFinal" type="text" size="10" maxlength="10" value="<%=ponteiroLogCompensarDt.getDataFinal()%>" /> 
					    <img id="calendarioDataFinal" class="calendario" src="./imagens/dlcalendar_2.gif" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataFinal,'dd/mm/yyyy',this)" />
						 --%><br/>
						 
					</fieldset>

				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
