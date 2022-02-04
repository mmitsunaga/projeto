<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.TemaDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="Temadt" scope="session" class= "br.gov.go.tj.projudi.dt.TemaDt"/>
<%@page import="br.gov.go.tj.projudi.dt.TemaAssuntoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.TemaSituacaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.TemaOrigemDt"%>
<%@page import="br.gov.go.tj.projudi.dt.TemaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AssuntoDt"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Tema  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<style type="text/css">
		@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
	</style>			
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>	
	<script type='text/javascript' src='./js/jquery.js'> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(Titulo, "O Campo Titulo é obrigatório!")) return false;
				if (SeNulo(TemaCodigo, "O Campo TemaCodigo é obrigatório!")) return false;
				if (SeNulo(TemaSituacao, "O Campo TemaSituacao é obrigatório!")) return false;
				if (SeNulo(TemaOrigem, "O Campo TemaOrigem é obrigatório!")) return false;
				if (SeNulo(TemaTipo, "O Campo TemaTipo é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
</head>

<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; Cadastro de Tema</h2></div>
<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<form action="Tema" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()">
<%} else {%>
		<form action="Tema" method="post" name="Formulario" id="Formulario">
<%}%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input  name="PassoEditar" id="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>
				<c:if test="${isPodeEnviarTemaParaCNJ}">
					<input alt="Enviar" title="Enviar os dados para o CNJ" type="image" src="./imagens/imgUpload.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>')" />
				</c:if>
				<a class="divPortaBotoesLink" href="Ajuda/TemaAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>								
			</div>
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Edita os dados Tema </legend>
					<label class="formEdicaoLabel" for="Id_Tema">Identificador</label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_Tema"  id="Id_Tema"  type="text"  readonly="true" value="<%=Temadt.getId()%>"><br />
					<label class="formEdicaoLabel" for="TemaCodigo">*Código</label><br> 
					<input class="formEdicaoInput" name="TemaCodigo" id="TemaCodigo" type="text" size="9" maxlength="9" value="<%=Temadt.getTemaCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,9)"/><br />
			        <br>					
					<input type="checkbox" name="OpcaoProcessual" <%=(Temadt.getOpcaoProcessual() != null && Temadt.getOpcaoProcessual().equals("1")) ? "checked=checked" : "" %> value="1"/>
					<strong>Tema Processual</strong>
					<br><br>
					<label class="formEdicaoLabel" for="NumeroIRDR">Número CNJ (NUT)</label><br>
					<input class="formEdicaoInput" name="NumeroIRDR" id="NumeroIRDR" type="text" size="15" maxlength="15" value="<%=Temadt.getNumeroIrdrCnj()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,9)"/><br />
					<label class="formEdicaoLabel" for="Vinculantes">Vinculantes</label><br> 
					<input class="formEdicaoInput" name="Vinculantes" id="Vinculantes"  type="text" size="120" maxlength="255" value="<%=Temadt.getVinculantes()%>" onkeyup=" autoTab(this,60)" 
					title="Obs: O número do vinculante deve ser completo (Ex: 5001995-15.2015.8.09.0000) e se mais de um, separar por vírgula, ponto e vírgula e espaço em branco.">
					<br />
					<label class="formEdicaoLabel" for="Titulo">*Titulo</label><br> <input class="formEdicaoInput" name="Titulo" id="Titulo"  type="text" size="120" maxlength="255" value="<%=Temadt.getTitulo()%>" onkeyup=" autoTab(this,60)"><br />
					<label class="formEdicaoLabel" for="QuesDireito">*Questão de Direito</label><br> <textarea rows="4" cols="120" name="QuesDireito" id="QuesDireito"  type="text"  value="<%=Temadt.getQuesDireito()%>"><%=Temadt.getQuesDireito()%></textarea><br />
					<label class="formEdicaoLabel" for="TeseFirmada">Tese Firmada</label><br> <textarea rows="4" cols="120" name="TeseFirmada" id="TeseFirmada" type="text" value="<%=Temadt.getTeseFirmada()%>"><%=Temadt.getTeseFirmada()%></textarea><br />
					<label class="formEdicaoLabel" for="InfoLegislativa">Referência Legislativa</label><br> <textarea rows="3" cols="120" name="InfoLegislativa" id="InfoLegislativa" type="text" value="<%=Temadt.getInfoLegislativa()%>"><%=Temadt.getInfoLegislativa()%></textarea><br />
					<label class="formEdicaoLabel" for="DataTransito">Data de Trânsito</label><br />
					<input id="DataTransito" name="DataTransito" size="10" maxlength="10" title="Clique para escolher uma data."  value="<%=Temadt.getDataTransito()%>" onkeyup="mascara_data(this)" onkeypress="return DigitarSoNumero(this, event)" />
					<img id="calDataFinal" src="./imagens/dlcalendar_2.gif" height="15" width="15" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataTransito,'dd/mm/yyyy',this)" /> <br />
					<label class="formEdicaoLabel" for="DataAdmissao">Data de Admissão</label><br />
					<input id="DataAdmissao" name="DataAdmissao" size="10" maxlength="10" title="Clique para escolher uma data."  value="<%=Temadt.getDataAdmissao()%>" onkeyup="mascara_data(this)" onkeypress="return DigitarSoNumero(this, event)" />
					<img id="calDataAdmissao" src="./imagens/dlcalendar_2.gif" height="15" width="15" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataAdmissao,'dd/mm/yyyy',this)" /> <br />					
					<label class="formEdicaoLabel" for="Id_TemaSituacao">*Tema Situação
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarTemaSituacao" name="imaLocalizarTemaSituacao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(TemaSituacaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" >
					</label><br>  
					 	<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="TemaSituacao" id="TemaSituacao" type="text" size="60" maxlength="60" value="<%=Temadt.getTemaSituacao()%>"/><br />
					<label class="formEdicaoLabel" for="Id_TemaOrigem">*Tema Origem
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarTemaOrigem" name="imaLocalizarTemaOrigem" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(TemaOrigemDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" >
					</label><br>  
					 	<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="TemaOrigem" id="TemaOrigem" type="text" size="60" maxlength="60" value="<%=Temadt.getTemaOrigem()%>"/><br />
					<label class="formEdicaoLabel" for="Id_TemaTipo">*Tema Tipo
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarTemaTipo" name="imaLocalizarTemaTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(TemaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" >
					</label><br>  
					 	<input  class="formEdicaoInputSomenteLeitura"  readonly="true" name="TemaTipo" id="TemaTipo" type="text" size="60" maxlength="60" value="<%=Temadt.getTemaTipo()%>"/><br />
					
					<!-- ASSUNTOS -->
					<br />
		    		<input type="hidden" id="posicaoLista" name="posicaoLista">
		    		<fieldset id="VisualizaDados" class="VisualizaDados" style="width:95%;background-color:#fff">  
  						<legend> 
  							*Assunto(s)
  							<input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoTipo" name="imaLocalizarProcessoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > 
  						</legend>
				   		<%
				   			List listaAssuntos = (List) Temadt.getListaAssuntos();
				   	    	if (listaAssuntos != null && listaAssuntos.size() > 0){ %>
				   			<table width="98%" border="0" cellpadding="0" cellspacing="0" style="font-size: 10px !important;">
				   				<thead align="left">
				   					<th width="90%">Descrição</th>
				   					<th>Excluir</th>
				   				</thead>
							<%
				   	    		for (int i=0;i < listaAssuntos.size();i++){
				   	    			TemaAssuntoDt assuntoDt = (TemaAssuntoDt)listaAssuntos.get(i); %>
					   			<tbody>
									<tr>
				       					<td><%=assuntoDt.getAssunto()%></td>
				       	 				<td><input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('PassoEditar','2');AlterarValue('posicaoLista','<%=i%>')" title="Excluir assunto"/></td>
				       	 			</tr>
				       	 		</tbody>
				       		<%	} %>
				       	</table>
				   		<% } else { %> <em> Nenhum assunto cadastrado </em> <% } %>
					</fieldset>
			    	<br />
			    	<br />
				</fieldset>
			
			<%@ include file="Padroes/ConfirmarOperacaoTema.jspf"%>

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
