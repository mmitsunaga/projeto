<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.PendenciaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="AudienciaMovimentacaoDt" class= "br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt" scope="session"/>
<jsp:useBean id="UsuarioSessao" class= "br.gov.go.tj.projudi.ne.UsuarioNe" scope="session"/>

<html>
<head>
	<title>Concluir Audi�ncia</title>	
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
	<script type="text/javascript">
		//<![CDATA[
		onload = function()
		{					
			<%if (AudienciaMovimentacaoDt != null && AudienciaMovimentacaoDt.isPendenciaTipoSomenteLeitura()){%>
			  	iniciarExtratoAta();
				preencherDestinatarios();
			<%} else { %>
				iniciar();
			<%}%>
		}
		//]]>
	</script>	
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
		
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />	
			<input id="SomentePreAnalisadas" name="SomentePreAnalisadas" type="hidden" value="<%=request.getAttribute("SomentePreAnalisadas")%>">
			<!-- Vari�veis para controlar Passos da Movimenta��o -->
			<input id="Passo1" name="Passo1" type="hidden" value="<%=AudienciaMovimentacaoDt.getPasso1()%>">
			<%if (AudienciaMovimentacaoDt != null && !AudienciaMovimentacaoDt.isIgnoraEtapa2Pendencias()){%>
			<input id="Passo2" name="Passo2" type="hidden" value="<%=AudienciaMovimentacaoDt.getPasso2()%>">
			<%}%>			
			<input id="Passo3" name="Passo3" type="hidden" value="<%=AudienciaMovimentacaoDt.getPasso3()%>">			
			
			<div id="divEditar" class="divEditar">	
	
				<% if (AudienciaMovimentacaoDt != null && !AudienciaMovimentacaoDt.getPasso1().equals("")){ %>
				<input name="imgPasso1" type="submit" value="<%=AudienciaMovimentacaoDt.getPasso1()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','-1');">
				<%}%>
				<%if (!AudienciaMovimentacaoDt.isIgnoraEtapa2Pendencias()){%> 
					<%if (AudienciaMovimentacaoDt != null && !AudienciaMovimentacaoDt.getPasso2().equals("")){ %>				
					<input name="imgPasso2" type="submit" value="<%=AudienciaMovimentacaoDt.getPasso2()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');AlterarValue('PassoEditar','0');">
					<%}%>
				<%}%>
				<%if (!AudienciaMovimentacaoDt.getPasso3().equals("")){ %>				
				<input name="imgPasso3" type="submit" value="<%=AudienciaMovimentacaoDt.getPasso3()%>" onclick="AlterarValue('PaginaAtual', '<%=Configuracao.Salvar%>');">
				<%}%>
	
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Passo 2 - Pend�ncias a Gerar </legend>
					<br />
					<input type="hidden" name="pendenciaTipo" id="pendenciaTipo">
					<div id="DivPendenciaTipo" class="coluna30"> 
						<label class="formEdicaoLabel"> Tipo de Pend�ncia </label><br> 	
						<select name="codPendenciaTipo" id="codPendenciaTipo" size="1" onChange="preencherDestinatarios();" <%=(AudienciaMovimentacaoDt.isPendenciaTipoSomenteLeitura() ? "disabled='disabled'" :"")%> >
							<option value="-1">--Selecione o Tipo de Pend�ncia-- </option>
							<%
								List listaPendenciaTipo = AudienciaMovimentacaoDt.getListaPendenciaTipos();
							
								if(listaPendenciaTipo != null && listaPendenciaTipo.size() != 0){						
									for (int i=0;i<listaPendenciaTipo.size();i++){
										PendenciaTipoDt pendenciaTipoDt = (PendenciaTipoDt)listaPendenciaTipo.get(i);
	
							%>
								<option value="<%=pendenciaTipoDt.getPendenciaTipoCodigo()%>" <%=( AudienciaMovimentacaoDt.getPendenciaTipoCodigo() != null && (AudienciaMovimentacaoDt.getPendenciaTipoCodigo().trim().equalsIgnoreCase(pendenciaTipoDt.getPendenciaTipoCodigo())) ? "selected='selected'" :"")%> ><%=pendenciaTipoDt.getPendenciaTipo()%></option>
							<%		
									}
								}else{
									%>
										<option value="-1" > N�o h� tipos de pend�ncia</option>
									<%
								}
							%> 	  						   
						</select>
					</div>
					<div id='divmostrarExpedicao' style="display:none;" > 
						<div class='coluna20' ><input class="formEdicaoInput" type="checkbox"  value="true" onChange="mostrarModeloIntimacaoCitacao();" name="expedicaoAutomatica" id="expedicaoAutomatica"/>Expedi��o Autom�tica</div>
						<div  class='coluna20' id="divOpcoesExpedicaoAutomatica" style="display: none;"></div>
					</div>
					<br />
					<%@ include file="PendenciasGerar.jspf"%>	
					<br />
					
					<label class="formEdicaoLabel" for="Id_Classificador">Classificador
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarClassificador" name="imaLocalizarClassificaodr" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ClassificadorDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
					<input class="FormEdicaoimgLocalizar" name="imaLimparClassificador" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Classificador', 'Classificador'); return false;" title="Limpar Classificador" />
					 </label><br>  
					<input type="hidden" name="Id_Classificador" id="Id_Classificador" />  
					
					<input class="formEdicaoInputSomenteLeitura" readonly name="Classificador" id="Classificador" type="text" size="50" maxlength="60" value="<%=AudienciaMovimentacaoDt.getClassificador()%>"/><br />

					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgConcluir" type="submit" value="Avan�ar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
					</div>
					<br />
				</fieldset>	
			</div>
		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
 	</div>	
</body>
</html>