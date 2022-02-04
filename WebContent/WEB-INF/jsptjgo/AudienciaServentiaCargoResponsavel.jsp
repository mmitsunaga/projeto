<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.projudi.ne.UsuarioNe"%>

<jsp:useBean id="AudienciaProcessoDt" class= "br.gov.go.tj.projudi.dt.AudienciaProcessoDt" scope="session"/>
<jsp:useBean id="Serventiadt" class= "br.gov.go.tj.projudi.dt.ServentiaDt" scope="session"/>
<jsp:useBean id="ServentiaCargoMagistradodt" class= "br.gov.go.tj.projudi.dt.ServentiaCargoDt" scope="session"/>
<jsp:useBean id="ServentiaCargoAssistentedt" class= "br.gov.go.tj.projudi.dt.ServentiaCargoDt" scope="session"/>
<jsp:useBean id="ServentiaCargoAssistenteDistribuidorAtualDt" class= "br.gov.go.tj.projudi.dt.ServentiaCargoDt" scope="session"/>
<html>
<head>
	<title>Troca de Responsável da Sessão de Segundo Grau</title>	
    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
    
    	  
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<!--<script type="text/javascript" src="./js/ui/jquery.tabs.min.js"></script>-->
	<link rel='stylesheet' href='./css/jquery.tabs.css' type='text/css' media='print, projection, screen'>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
	<script type="text/javascript">
		function processaExibicaoNovoAssistenteReponsavel(check){					
			if (check.checked){
				Ocultar('idNovoAssistenteResponsavel');	
				AlterarValue('Id_ServentiaCargoAssistente','');
				AlterarValue('ServentiaCargoAssistente','');								
			}else{
				Mostrar('idNovoAssistenteResponsavel');					
			}				
		}
	</script>
	<script type="text/javascript">
		//<![CDATA[
		onload = function()
		{				
			var check = document.getElementById('AudienciaSemAssistente');
			if(check) processaExibicaoNovoAssistenteReponsavel(check);
		}
		//]]>
	</script>       
</head>

	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; Trocar Responsável da Sessão de Segundo Grau</h2></div>
		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">				
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />				
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<input id="CargoTipoSelecionado" name="CargoTipoSelecionado" type="hidden" value="<%=request.getAttribute("CargoTipoSelecionado")%>">
										
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend>Trocar Responsável da Sessão de Segundo Grau</legend>
						<fieldset class="formEdicao">
							<legend><%="Data: "+ AudienciaProcessoDt.getAudienciaDt().getDataAgendada()%>; Processo N&uacute;mero:  <a href="BuscaProcesso?Id_Processo=<%=AudienciaProcessoDt.getId_Processo()%>"><%=AudienciaProcessoDt.getProcessoNumero()%></a> </legend>
							<table class="Tabela" id="TabelaArquivos">
								<thead>
									<tr>
										<th width="50%">Cargo</th>										
										<th width="50%">Responsável</th>										
									</tr>
								</thead>							
								<tbody>
									<tr class="TabelaLinha1"> 
										<td><%=AudienciaProcessoDt.getServentiaCargo()%></td>										
										<td><%=AudienciaProcessoDt.getNomeResponsavel()%></td>
									</tr>									
									<tr class="TabelaLinha2">										 
										<td><%=(ServentiaCargoAssistenteDistribuidorAtualDt != null ? ServentiaCargoAssistenteDistribuidorAtualDt.getServentiaCargo() : "")%></td>										
										<td><%=(ServentiaCargoAssistenteDistribuidorAtualDt != null ? ServentiaCargoAssistenteDistribuidorAtualDt.getNomeUsuario() : "")%></td>
									</tr>									
								</tbody>
							</table>
							
						</fieldset>						
						<br />
						<fieldset class="formEdicao">
							<legend>Novo Responsável</legend>
							
							<input type="hidden" id="Id_Serventia" name="Id_Serventia" value="<%=Serventiadt.getId()%>" />
							
							<label class="formEdicaoLabel">Serventia
							<% UsuarioNe usuarioSessao = (UsuarioNe) request.getSession().getAttribute("UsuarioSessao");
							   if(!(usuarioSessao.getUsuarioDt().getGrupoCodigoToInt() == GrupoDt.DISTRIBUIDOR_GABINETE)){ %>
							   	  <input class="FormEdicaoimgLocalizar" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png" 
										onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>'); " title="Selecione a serventia" />			
								  <input class="FormEdicaoimgLocalizar" name="imaLimparServentia" type="image"  src="./imagens/16x16/edit-clear.png"
										onclick="LimparChaveEstrangeiraCampoRelacionado('Id_Serventia', 'Serventia','Id_ServentiaCargoMagistrado', 'ServentiaCargoMagistrado'); LimparChaveEstrangeiraCampoRelacionado('Id_Serventia', 'Serventia','Id_ServentiaCargoAssistente', 'ServentiaCargoAssistente'); return false;" title="Limpar a serventia" />
							<%} %>
							</label><br>						
															
							<input class="formEdicaoInputSomenteLeitura" id="Serventia" name="Serventia" readonly="true" type="text" size="50" maxlength="50" value="<%=Serventiadt.getServentia()%>" />	
							<br />
							
							<input type="hidden" id="Id_ServentiaCargoMagistrado" name="Id_ServentiaCargoMagistrado" value="<%=ServentiaCargoMagistradodt.getId()%>" />
							
							<label class="formEdicaoLabel">Responsável
							<input class="FormEdicaoimgLocalizar" name="imaLocalizarServentiaCargoMagistrado" type="image"  src="./imagens/imgLocalizarPequena.png" 
								onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>'); AlterarValue('CargoTipoSelecionado','1');" 
											title="Selecione o cargo da serventia" />
							
							<input class="FormEdicaoimgLocalizar" name="imaLimparServentiaCargoMagistrado" type="image"  src="./imagens/16x16/edit-clear.png"
								onclick="LimparChaveEstrangeira('Id_ServentiaCargoMagistrado', 'ServentiaCargoMagistrado'); return false;" 	title="Limpar o cargo da serventia - Magistrado" />
							</label><br>
							
							
							<input class="formEdicaoInputSomenteLeitura" name="ServentiaCargoMagistrado" readonly type="text" size="50" maxlength="50" id="ServentiaCargoMagistrado" value="<%=ServentiaCargoMagistradodt.getServentiaCargo()%>" />
														
							
							
							<label class="formEdicaoLabel" for="AudienciaSemAssistente"> 
		    					<input class="formEdicaoInput" name="AudienciaSemAssistente" id="AudienciaSemAssistente"  type="checkbox"  value="true" <% if(request.getSession().getAttribute("AudienciaSemAssistente") != null && String.valueOf(request.getSession().getAttribute("AudienciaSemAssistente")).equalsIgnoreCase("S")){%>  checked<%}%> onchange="processaExibicaoNovoAssistenteReponsavel(this)" /> 
		    					Sem Assistente (Distribuidor)
		    				</label>	
							<br />
							<fieldset id="idNovoAssistenteResponsavel" class="formEdicao">
								<legend>Novo Assistente Responsável</legend>					
							
								<input type="hidden" id="Id_ServentiaCargoAssistente" name="Id_ServentiaCargoAssistente" value="<%=ServentiaCargoAssistentedt.getId()%>" />
							
								<label class="formEdicaoLabel">Assistente
								<input class="FormEdicaoimgLocalizar" name="imaLocalizarServentiaCargoAssistente" type="image"  src="./imagens/imgLocalizarPequena.png" 
									onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('CargoTipoSelecionado', '2');" 
												title="Selecione o cargo da serventia" />
								
								<input class="FormEdicaoimgLocalizar" name="imaLimparServentiaCargoAssistente" type="image"  src="./imagens/16x16/edit-clear.png"
									onclick="LimparChaveEstrangeira('Id_ServentiaCargoAssistente', 'ServentiaCargoAssistente'); return false;" 	title="Limpar o cargo da serventia - Assistente" />
								</label><br>
								
								
								<input class="formEdicaoInputSomenteLeitura" name="ServentiaCargoAssistente" readonly type="text" size="50" maxlength="50" id="ServentiaCargoAssistente" value="<%=ServentiaCargoAssistentedt.getServentiaCargo()%>" />
				    		</fieldset>
				    		
			    		</fieldset>
				    		
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgConcluir" type="submit" value="Concluir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
						</div>						
					</fieldset>
					
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
 		</div>
	</body>
</html>