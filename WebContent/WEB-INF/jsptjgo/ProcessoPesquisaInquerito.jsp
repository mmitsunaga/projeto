<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoStatusDt"%>

<jsp:useBean id="buscaProcessoDt" scope="session" class= "br.gov.go.tj.projudi.dt.BuscaProcessoDt"/>
<jsp:useBean id="Estadodt" scope="session" class= "br.gov.go.tj.projudi.dt.EstadoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>

<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%><html>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%><html>

	<head>
		<title>Busca de Processos pelo número do Inquérito</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/menusimples.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
      	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
  			<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>
			<form action="BuscaProcesso" method="post" name="Formulario" id="Formulario">
	
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao" id='fieldsetDadosProcesso'> 
					    <legend class="formEdicaoLegenda">Busca de Processos pelo número do Inquérito</legend>
					    
					    <label class="formEdicaoLabel" for="TemaCodigo">Número do Inquérito</label><br>  
						<input class="formEdicaoInput"   name="Inquerito" id="Inquerito" type="text" size="20" maxlength="20" value="<%=buscaProcessoDt.getInquerito()%>"/>
						<br />
                       	<label class="formEdicaoLabel" for="Id_ProcessoStatus">Status do Processo
                       	 <input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoStatus" name="imaLocalizarProcessoStatus" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="MostrarBuscaPadrao('fieldsetDadosProcesso', 'ProcessoStatus', 'Consulta de Status de Processo', 'Digite o Status e clique em consultar.', 'Id_ProcessoStatus', 'ProcessoStatus', ['Processo Status'], ['ProcessoStatusCodigo'], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;")" >  
                       	</label><br>  
					   <input type="hidden" name="Id_ProcessoStatusCodigo" id="Id_ProcessoStatusCodigo" value="<%=buscaProcessoDt.getId_ProcessoStatus() %>">
					    <input type="hidden" name="ProcessoStatusCodigo" id="ProcessoStatusCodigo" value="<%=buscaProcessoDt.getProcessoStatusCodigo() %>">
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="ProcessoStatus" id="ProcessoStatus" type="text" size="67" maxlength="100" value="<%=buscaProcessoDt.getProcessoStatus()%>"/><br />
					    
					    <label class="formEdicaoLabel" for="Id_Serventia">Serventia
					     <input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"   onclick="MostrarBuscaPadrao('fieldsetDadosProcesso', 'Serventia', 'Consulta de Serventia', 'Digite a Serventia e clique em consultar.', 'Id_Serventia', 'Serventia', ['Serventia'], ['Estado'], '<%=(Configuracao.Localizar)%>', '<%=Configuracao.TamanhoRetornoConsulta%>'); return false;")" >
						<input class="FormEdicaoimgLocalizar" name="imaLimparServentia" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Serventia','Serventia'); return false;" title="Limpar Serventia">  
					    </label><br>
					 	<input type="hidden" name="Id_Serventia" id="Id_Serventia">  
					   
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="Serventia" id="Serventia" type="text" size="64" maxlength="100" value="<%=buscaProcessoDt.getServentia()%>"/><br />
					    
										    
			    		<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="submit" value="Buscar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');">

						</div>
					</fieldset>		
				</div>
								
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>