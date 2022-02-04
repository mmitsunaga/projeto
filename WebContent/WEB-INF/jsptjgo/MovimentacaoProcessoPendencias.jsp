<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.PendenciaTipoDt"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>

<jsp:useBean id="Movimentacaodt" class= "br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt" scope="session"/>
<jsp:useBean id="UsuarioSessao" scope="session" class= "br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html>
<head>
	<title>Movimentar Processo</title>	
    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
    
    	  
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
   	<script type='text/javascript' src='./js/checks.js'></script>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>	
	<script type="text/javascript" src="./js/Digitacao/MascararHoraResumida.js"></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarHoraResumida.js"></script>
	<link type='text/css' rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen' />
</head>

<body onload="atualizarPendencias();">
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
		
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>" />		
			<!-- Variáveis para controlar Passos da Movimentação -->
			<input id="Passo1" name="Passo1" type="hidden" value="<%=Movimentacaodt.getPasso1()%>">
			<input id="Passo2" name="Passo2" type="hidden" value="<%=Movimentacaodt.getPasso2()%>">
			<input id="Passo3" name="Passo3" type="hidden" value="<%=Movimentacaodt.getPasso3()%>">
			
			<div id="divEditar" class="divEditar">	
	
				<% if (Movimentacaodt.getPasso1() != null && !Movimentacaodt.getPasso1().equals("")){ %>
				<input name="imgPasso1" type="submit" value="<%=Movimentacaodt.getPasso1()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','-1');">
				<% } if (Movimentacaodt.getPasso2() != null && !Movimentacaodt.getPasso2().equals("")){ %>				
				<input name="imgPasso2" type="submit" value="<%=Movimentacaodt.getPasso2()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','0');">
				<% } if (Movimentacaodt.getPasso3() != null && !Movimentacaodt.getPasso3().equals("")){ %>				
				<input name="imgPasso3" type="submit" value="<%=Movimentacaodt.getPasso3()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Salvar%>');">
				<% } %>
	
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Passo 2 - Pendências a Gerar </legend>
					<br />
					<input type="hidden" name="pendenciaTipo" id="pendenciaTipo">
					<div id="DivPendenciaTipo" class="coluna30"> 
						<label class="formEdicaoLabel"> Tipo de Pendência </label><br> 	
						<select name="codPendenciaTipo" id="codPendenciaTipo" size="1" onChange="preencherDestinatarios();">
							<option value="-1">--Selecione o Tipo de Pendência-- </option>
							<%
								List listaPendenciaTipo = Movimentacaodt.getListaPendenciaTipos();
								if( listaPendenciaTipo != null && listaPendenciaTipo.size() > 0 ) {
									for (int i=0; i<listaPendenciaTipo.size(); i++){
										PendenciaTipoDt pendenciaTipoDt = (PendenciaTipoDt)listaPendenciaTipo.get(i);
							%>
											<option <%=((request.getSession().getAttribute("PendenciaTipoCodigo") != null && request.getSession().getAttribute("PendenciaTipoCodigo").equals(pendenciaTipoDt.getPendenciaTipoCodigo()))?"selected":"") %> value="<%=pendenciaTipoDt.getPendenciaTipoCodigo()%>"><%=pendenciaTipoDt.getPendenciaTipo()%></option>
							<%		
									}
								}
							%> 	  						   
						</select>
					</div>
					<div id='divmostrarExpedicao' style="display:none;" > 
						<div class='coluna20' ><input class="formEdicaoInput" type="checkbox"  value="true" onChange="mostrarModeloIntimacaoCitacao();" name="expedicaoAutomatica" id="expedicaoAutomatica"/>Expedição Automática</div>
						<div  class='coluna20' id="divOpcoesExpedicaoAutomatica" style="display: none;"></div>
					</div>
					<br />
					<%@ include file="PendenciasGerar.jspf"%>	
					<br />

					<div style="width: 50%; float:left">
						<label class="formEdicaoLabel" for="Id_Classificador">Alterar Classificador Processo 
						<input type="hidden" name="Id_Classificador" id="Id_Classificador" />  
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarClassificador" name="imaLocalizarClassificaodr" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ClassificadorDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('tempFluxo1','processo')" >  
						<input class="FormEdicaoimgLocalizar" name="imaLimparClassificador" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Classificador', 'Classificador'); return false;" title="Limpar Classificador" />
						</label><br>  
						<input class="formEdicaoInputSomenteLeitura" readonly name="Classificador" id="Classificador" type="text" size="50" maxlength="60" value="<%=Movimentacaodt.getClassificador()%>"/>						
					</div>
					<div style="width: 50%; float:right;">
						<label class="formEdicaoLabel" for="Id_ClassificadorPendencia">Alterar Classificador Pendência
						<input type="hidden" name="Id_ClassificadorPendencia" id="Id_ClassificadorPendencia" />  
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarClassificador" name="imaLocalizarClassificador" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ClassificadorDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('tempFluxo1','pendencia')" >  
						<input class="FormEdicaoimgLocalizar" name="imaLimparClassificador" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_ClassificadorPendencia', 'ClassificadorPendencia'); return false;" title="Limpar Classificador" />
						</label><br>  
						<input class="formEdicaoInputSomenteLeitura" readonly name="ClassificadorPendencia" id="ClassificadorPendencia" type="text" size="50" maxlength="60" value="<%=Movimentacaodt.getClassificadorPendencia()%>"/>
					</div>
					
				</fieldset>	
				<div id="divBotoesCentralizados" class="divBotoesCentralizados">
					<input name="imgConcluir" type="submit" value="Avançar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
				</div>
				<br />
			</div>
		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
 	</div>	
</body>
	<script type="text/javascript">
		$(document).ready(function()  {
			preencherDestinatarios();
		 });
	</script>
</html>