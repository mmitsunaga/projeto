<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="br.gov.go.tj.projudi.dt.PendenciaTipoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>

<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html>
	<head>
		<title>Criar Pend&ecirc;ncia</title>
		
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
		<style type="text/css">
			@import url('./css/Principal.css');
		</style>
	
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>      
		<!--<script type="text/javascript" src="./js/ui/jquery.tabs.min.js"></script>-->
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
		<script type="text/javascript" src="./js/tabelas.js"></script>
		<script type='text/javascript' src='./js/checks.js'></script> 
		<script type="text/javascript" src="./js/tabelaArquivos.js"></script>
		<%@ include file="js/buscarArquivos.js"%>
		
		<link rel="stylesheet" href="./css/jquery.tabs.css" type="text/css" media="print, projection, screen" />
	
	
	</head>
	
	<body >
	   <div class="divCorpo">
		<div id="divEditar" class="divEditar">
			<form method="post" action="Pendencia" id="Formulario">
					<div class="area"><h2>&raquo; Criar Pend&ecirc;ncia</h2></div>
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
				<input id="operacaoPendencia" name="operacaoPendencia" type="hidden" value="<%=request.getAttribute("operacaoPendencia")%>" />
				
				<input id="tempBuscaId" name="tempBuscaId" type="hidden" value="" />
				<input id="tempBuscaDescricao" name="tempBuscaDescricao" type="hidden" value="" />
				
				<%@ include file="Padroes/Mensagens.jspf"%>
				<div id="divAjuda" class="divAjuda" >
					<img src="./imagens/imgAjudaPequena.png" 
						onclick="DivFlutuanteValoresIniciais('Ajuda Inserção Arquivos', getMensagem(0),'200','360','0','0', this);" 
						onmouseup="DivFlutuanteUp('Informe');" width="16" height="16" border="0" />
				</div>
					<div id="dados">		
						<fieldset class="formEdicao">
							<legend>Dados da Pend&ecirc;ncia</legend>
							
							<input type="hidden" id="Id_PendenciaTipo" name="Id_PendenciaTipo" 
								value="<%=request.getAttribute("Id_PendenciaTipo")%>" />
							
							<label class="formEdicaoLabel">*Tipo de Pend&ecirc;ncia
							<input class="FormEdicaoimgLocalizar" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png" 
								onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(PendenciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');
										AlterarValue('tempBuscaId', 'Id_PendenciaTipo'); 
										AlterarValue('tempBuscaDescricao', 'PendenciaTipo');" title="Selecione a pend&ecirc;ncia" />
							<input class="FormEdicaoimgLocalizar" name="imaLimparPendenciaTipo" type="image"  src="./imagens/16x16/edit-clear.png"
								onclick="LimparChaveEstrangeira('Id_PendenciaTipo', 'PendenciaTipo'); return false;" 
											title="Limpar a Tipo da pend&ecirc;ncia" />
							</label><br>
							
							<input class="formEdicaoInputSomenteLeitura" name="PendenciaTipo" id="PendenciaTipo" readonly type="text" size="50" maxlength="50" value="<%=request.getAttribute("PendenciaTipo")%>" />	
							<br />
						</fieldset>
						
						<fieldset class="formEdicao">
							<legend>Destino</legend>
							
							<input type="hidden" id="Id_ServentiaTipo" name="Id_ServentiaTipo" 
								value="<%=request.getAttribute("Id_ServentiaTipo")%>" />
							
							<label class="formEdicaoLabel">Serventia Tipo
							<input class="FormEdicaoimgLocalizar" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png" 
								onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(ServentiaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" 
											title="Selecione o tipo de serventia" />
							<input class="FormEdicaoimgLocalizar" name="imaLimparServentiaTipo" type="image"  src="./imagens/16x16/edit-clear.png"
								onclick="LimparChaveEstrangeira('Id_ServentiaTipo', 'ServentiaTipo'); return false;" 
											title="Limpar o tipo de serventia" />
							</label><br>
							
							<input class="formEdicaoInputSomenteLeitura" name="ServentiaTipo" readonly type="text" size="50" maxlength="50" id="ServentiaTipo" 
								value="<%=request.getAttribute("ServentiaTipo")%>" />	
							
							<input type="hidden" id="Id_ServentiaExpedir" name="Id_ServentiaExpedir" 
								value="<%=request.getAttribute("Id_ServentiaExpedir")%>" />
							<br />
							<label class="formEdicaoLabel">Serventia
							<input class="FormEdicaoimgLocalizar" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png" 
								onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" 
											title="Selecione a serventia" />
							<input class="FormEdicaoimgLocalizar" name="imaLimparServentia" type="image"  src="./imagens/16x16/edit-clear.png"
								onclick="LimparChaveEstrangeira('Id_ServentiaExpedir', 'ServentiaExpedir'); return false;" 
											title="Limpar a serventia" />
							</label><br>
							
							<input class="formEdicaoInputSomenteLeitura" name="ServentiaExpedir" readonly type="text" size="50" maxlength="50" id="ServentiaExpedir" 
								value="<%=request.getAttribute("ServentiaExpedir")%>" />	
							<br />	
						</fieldset>
					</div>

					<div id="insercao">
						<%@ include file="Padroes/InsercaoArquivosAssinador.jspf" %>
					</div>					
				
				<br />
				<div id="divBotoesCentralizados" class="divBotoesCentralizados">
				<%if (request.getAttribute("operacaoPendencia") != null && request.getAttribute("operacaoPendencia").equals("Confirmar")){ %>
					<% if (request.getAttribute("Mensagem") != null && !request.getAttribute("Mensagem").equals("")) { %>
		        		<div class="divMensagemsalvar"><%=request.getAttribute("Mensagem")%></div>
		           	<%  }%> 
					<button type="submit" name="operacao" value="Confirmar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga8)%>')" >
						<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Confirmar" /> -->
						Confirmar
					</button>
				<%} else { %>
					<button type="submit" name="operacao" value="Criar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga8)%>')" >
						<!--  <img src="./imagens/imgSalvar.png" alt="Criar" /> -->
						Criar
					</button>
				<%}%>
				</div>
			</form>
		</div>
	  </div>

	</body>
</html>