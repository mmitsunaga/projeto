<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.PendenciaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="AnalisePendenciadt" class= "br.gov.go.tj.projudi.dt.AnaliseConclusaoDt" scope="session"/>
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
			<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>">
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />	
			<!-- Variáveis para controlar Passos da Análise -->
			<input id="Passo1" name="Passo1" type="hidden" value="<%=AnalisePendenciadt.getPasso1()%>">
			<input id="Passo2" name="Passo2" type="hidden" value="<%=AnalisePendenciadt.getPasso2()%>">
			<input id="Passo3" name="Passo3" type="hidden" value="<%=AnalisePendenciadt.getPasso3()%>">
			
			<div id="divEditar" class="divEditar">	
	
				<% if (!AnalisePendenciadt.getPasso1().equals("")){ %>
				<input name="imgPasso1" type="submit" value="<%=AnalisePendenciadt.getPasso1()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('tempFluxo1','0');">
				<% } if (!AnalisePendenciadt.getPasso2().equals("")){ %>				
				<input name="imgPasso2" type="submit" value="<%=AnalisePendenciadt.getPasso2()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('tempFluxo1','1');">
				<% } if (!AnalisePendenciadt.getPasso3().equals("")){ %>				
				<input name="imgPasso3" type="submit" value="<%=AnalisePendenciadt.getPasso3()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Salvar%>');">
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
								List listaPendenciaTipo = AnalisePendenciadt.getListaPendenciaTipos();
								if (listaPendenciaTipo != null && listaPendenciaTipo.size() > 0){
									for (int i=0;i < listaPendenciaTipo.size();i++){
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
					
					<label class="formEdicaoLabel" for="Id_Classificador">Classificador 
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarClassificador" name="imaLocalizarClassificaodr" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ClassificadorDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
					<input class="FormEdicaoimgLocalizar" name="imaLimparClassificador" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Classificador', 'Classificador'); return false;" title="Limpar Classificador" />
					</label><br>  
					<input type="hidden" name="Id_Classificador" id="Id_Classificador"  value="<%=AnalisePendenciadt.getId_Classificador()%>" />  					
					<input class="formEdicaoInputSomenteLeitura" readonly name="Classificador" id="Classificador" type="text" size="50" maxlength="60" value="<%=AnalisePendenciadt.getClassificador()%>"/><br />
					
					<input class="formEdicaoInput" name="naoGerarVerificarProcesso" type="checkbox" value="true" <% if(AnalisePendenciadt.isNaoGerarVeficarProcesso()){%>  checked<%}%>>
						<span class="span"> Não Gerar Pendência "Verificar Processo" de forma automática</span/><br />
					
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgConcluir" type="submit" value="Avançar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
					</div>
					<br />
				</fieldset>	
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