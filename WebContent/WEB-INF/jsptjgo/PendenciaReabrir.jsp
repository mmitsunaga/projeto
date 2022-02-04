
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<jsp:useBean id="Pendenciadt" scope="session" class= "br.gov.go.tj.projudi.dt.PendenciaDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>
<html>
<head>
	<title>Pend&ecirc;ncia</title>

	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
	
	<link rel="stylesheet" href="./css/jquery.tabs.css" type="text/css" media="print, projection, screen" />

	<script type='text/javascript' src='./js/Funcoes.js'></script>	
	<script type='text/javascript' src='./js/checks.js'></script> 
	<script type='text/javascript' src='./js/jquery.js'></script>      
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	
	<style type="text/css">
		@import url('./css/Principal.css');
	</style>
</head>
<body >
 <div class="divCorpo">
	<form action="Pendencia" method="post" name="Formulario" id="Formulario">
		<div class="area"><h2>&raquo; Reabrir Pend&ecirc;ncia (<%=Pendenciadt.getId_PendenciaPai()%>)</h2></div>
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
		<input id="ultimaOperacao" name="ultimaOperacao" type="hidden" value="<%=request.getAttribute("ultimaOperacao")%>" />
		<input id="Id_Pendencia" name="Id_Pendencia" type="hidden" value="<%=Pendenciadt.getId()%>" />

		<br />
		<%@ include file="Padroes/Mensagens.jspf"%>
		<div id="divEditar" class="divEditar">
			<div id="destino">
				<fieldset class="formEdicao">
					<legend>Destino</legend>
					<br />

					<input type="hidden" id="Id_Serventia" name="Id_Serventia" value="<%=request.getAttribute("Id_Serventia")%>" />
					
					<label class="formEdicaoLabel">Serventia
					<input class="FormEdicaoimgLocalizar" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png" 
						onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" 
									title="Selecione a serventia" />
					<input class="FormEdicaoimgLocalizar" name="imaLimparServentia" type="image"  src="./imagens/16x16/edit-clear.png"
						onclick="LimparChaveEstrangeiraCampoRelacionado('Id_Serventia', 'Serventia','Id_ServentiaCargo', 'ServentiaCargo'); return false;" title="Limpar a serventia" />
					</label><br>
					<input class="formEdicaoInputSomenteLeitura" name="Serventia" readonly type="text" size="50" maxlength="50" id="Serventia" 
						value="<%=request.getAttribute("Serventia")%>" />	
					<br />
					
					<input type="hidden" id="Id_ServentiaCargo" name="Id_ServentiaCargo" value="<%=request.getAttribute("Id_ServentiaCargo")%>" />

					<label class="formEdicaoLabel">Cargo da Serventia
					<input class="FormEdicaoimgLocalizar" name="imaLocalizarServentiaCargo" type="image"  src="./imagens/imgLocalizarPequena.png" 
						onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" title="Selecione o cargo da serventia" />
					<input class="FormEdicaoimgLocalizar" name="imaLimparServentiaCargo" type="image"  src="./imagens/16x16/edit-clear.png"
						onclick="LimparChaveEstrangeira('Id_ServentiaCargo', 'ServentiaCargo'); return false;" 	title="Limpar o cargo da serventia" />
					</label><br>
					<input class="formEdicaoInputSomenteLeitura" name="ServentiaCargo" readonly type="text" size="50" maxlength="50" id="ServentiaCargo" 
						value="<%=request.getAttribute("ServentiaCargo")%>" />	
					<br />
				</fieldset>
			</div>
			<div id="insercao">
				<%@ include file="Padroes/InsercaoArquivosAssinador.jspf" %>				 
			</div>
			
		</div>		
		<br />
		<div id="divBotoesCentralizados" class="divBotoesCentralizados">
		<%if (request.getAttribute("PaginaAtual") != null && request.getAttribute("PaginaAtual").equals(Configuracao.Imprimir)){ %>
			<% if (request.getAttribute("Mensagem") != null && !request.getAttribute("Mensagem").equals("")) { %>
        		<div class="divMensagemsalvar"><%=request.getAttribute("Mensagem")%></div>
           	<%  }%> 
			<button type="submit" name="operacao" value="Confirmar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')" >
				<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Confirmar" /> -->
				Confirmar
			</button>
		<%} else { %>
			<button type="submit" name="operacao" value="Reabrir" onclick="AlterarValue('PaginaAtual', <%=Configuracao.Salvar%>);">
				<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Reabrir" /> -->
				Reabrir
			</button>
		<%}%>
		</div>

  </form>
 </div>
		 		
</body>
</html>