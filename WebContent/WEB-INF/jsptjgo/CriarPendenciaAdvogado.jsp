<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="br.gov.go.tj.projudi.dt.PendenciaTipoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<html>
	<head>
		<title>Criar Pend&ecirc;ncia</title>
		
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
		<style type="text/css">
			@import url('./css/Principal.css');
		</style>
		<script type='text/javascript' src='./js/jquery.js'></script>      
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
		<script type='text/javascript' src='./js/checks.js'></script> 
		<!-- <script type="text/javascript" src="./js/ui/jquery.tabs.min.js"></script>-->
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		
		<link rel="stylesheet" href="/css/jquery.tabs.css" type="text/css" media="print, projection, screen" />
		
		<script type="text/javascript">
			function anexarArquivoNaoAssinado(){
				AlterarValue("arquivo",getTextoEditor("FCKeditor"));
				
				var testeNome = $("#nomeArquivo").val();
				if (testeNome=="")	AlterarValue("nomeArquivo","online.html");
				else  AlterarValue("nomeArquivo", testeNome+".html");		
		
				AlterarValue("assinado","false");
				inserirArquivo("false");
			}
		</script>

	</head>
	
	<body onload="atualizarArquivos();">
	   <div class="divCorpo">
		<div id="divEditar" class="divEditar">
			<form method="post" action="Pendencia" name="Formulario" id="Formulario">
					<div class="area"><h2>&raquo; Criar Pend&ecirc;ncia</h2></div>
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
				<input id="operacaoPendencia" name="operacaoPendencia" type="hidden" value="<%=request.getAttribute("operacaoPendencia")%>" />
				
				<input id="tempBuscaId" name="tempBuscaId" type="hidden" value="" />
				<input id="tempBuscaDescricao" name="tempBuscaDescricao" type="hidden" value="" />
				
				<div id="divAjuda" class="divAjuda" >
					<img src="./imagens/imgAjudaPequena.png" 
						onclick="DivFlutuanteValoresIniciais('Ajuda Inserção Arquivos', getMensagem(0),'200','360','0','0', this);" 
						onmouseup="DivFlutuanteUp('Informe');" width="16" height="16" border="0" />
				</div>
					<div id="dados">		
						<fieldset class="formEdicao">
							<legend>Dados da Pend&ecirc;ncia</legend>
							
							<input type="hidden" id="Id_PendenciaTipo" name="Id_PendenciaTipo" value="<%=request.getAttribute("Id_PendenciaTipo")%>" />
							
							<label class="formEdicaoLabel">Tipo de Pend&ecirc;ncia
							<input class="FormEdicaoimgLocalizar" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png" 
								onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(PendenciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');
										AlterarValue('tempBuscaId', 'Id_PendenciaTipo'); 
										AlterarValue('tempBuscaDescricao', 'PendenciaTipo');" title="Selecione a pend&ecirc;ncia" />
							<input class="FormEdicaoimgLocalizar" name="imaLimparPendenciaTipo" type="image"  src="./imagens/16x16/edit-clear.png"
								onclick="LimparChaveEstrangeira('Id_PendenciaTipo', 'PendenciaTipo'); return false;" title="Limpar a Tipo da pend&ecirc;ncia" />
							<input class="formEdicaoInputSomenteLeitura" name="PendenciaTipo" id="PendenciaTipo" readonly type="text" size="50" maxlength="50" value="<%=request.getAttribute("PendenciaTipo")%>" />	
							</label><br>
							
							<br />
						</fieldset>					
					</div>
					<fieldset class="formEdicao">
							<legend>Inserir Aquivos</legend>
							<div id="insercao">
								<%@ include file="Padroes/InsercaoArquivosSemAssinador.jspf"%>
							</div>
					</fieldset>
				
				<br /><br />
				<%@ include file="Padroes/Mensagens.jspf"%>
				<div id="divBotoesCentralizados" class="divBotoesCentralizados">
				<%if (request.getAttribute("operacaoPendencia") != null && request.getAttribute("operacaoPendencia").equals("Confirmar")){ %>
					<button type="submit" name="operacao" value="Confirmar">
						<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Confirmar" /> -->
						Confirmar
					</button>
				<%} else { %>
					<button type="submit" name="operacao" value="Criar">
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