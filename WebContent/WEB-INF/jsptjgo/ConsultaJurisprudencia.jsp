<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
	<meta content="text/html; charset=ISO-8859-1" http-equiv="CONTENT-TYPE" />
	<title>Consulta de Publicações</title>
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
		@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
		p { margin: 0 0 10px; }		
		.hr-dashed-line { border:1px dashed lightgray; color: #ffffff; background-color: #ffffff;}
		.search-result h4 { margin-bottom: 0; color: #3181c3; }		
		.search-result p { font-size: 12px; margin-top: 5px; }
		.hlt1 { background-color: yellow;}
		.tooltip {display: none;}
		.tooltip-head { color:red;}
	</style>
	<link rel="shortcut icon" href="./imagens/favicon.png" />
	<link href="./css/Principal.css" type="text/css" rel="stylesheet" />
	<link rel="stylesheet" type="text/css" href="./css/jquery.powertip-light.min.css" />
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/jquery.mask.min.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>		
	<script type="text/javascript" src="./js/jquery.powertip.min.js"></script>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>	
	<script>
		function submitForm(pos){
			AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');
			AlterarValue('PosicaoPaginaAtual', pos);
			FormSubmit('Formulario');
		}
		function IrParaOnClick(){
			submitForm($('#CaixaTextoPosicionar').val()-1);
		}
		function abrirArquivo(action, id){
			var token = $('#g-recaptcha-response').val();
			$('<form action="'+ encodeURI(action + '?PaginaAtual=1&Id_Arquivo=' + id + '&g-recaptcha-response=' + token) +'" method="post"></form>').appendTo('body').submit().remove();
		}
		$(document).ready(function(){
			$('#Texto').focus();			
			$('#ProcessoNumero').mask('9999999.99.9999.9.99.9999');
			$("#Texto").keypress(function(e){
			 	if (e.which == 13){
			        e.preventDefault();
			        AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');
			        document.forms[0].submit();
			    }
			});
		});
	</script>
</head>
<body class="fundo">
	<% if (request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica")) { %>
  		<%@ include file="/CabecalhoPublico.html" %>
  	<% } %>
	<div id="divCorpo" class="divCorpo" style="margin-bottom:20px;"/>
		<div class="area"><h2> &raquo; Jurisprudências</h2></div>	
		<div id="divLocalizar" class="divLocalizar">			
			<form id="Formulario" method="post" action="${tempRetorno}"/>				
				<input type="hidden" id="PaginaAtual" name="PaginaAtual" value="${PaginaAtual}" />
				<input type="hidden" id="PosicaoPaginaAtual" name="PosicaoPaginaAtual" value="${PosicaoPaginaAtual}" />				
				<div id="divEditar" class="divEditar">
					<!-- Pesquisa por campos de filtro -->
					<fieldset class="formEdicao">
						<div id="filtro-campo">
							<div class="col100 clear">
				    			<label class="formEdicaoLabel" for="Texto">Digite um termo para a pesquisa:</label><br>
				    			<input type="text" id="Texto" name="Texto" title="Texto para consulta de jurisprudência" style="width:70%" value="${pesquisaAvancadaDt.texto}"/>
				    			<em><small>&nbsp;(Operação padrão: E)</small></em>
				    		</div>
				    		<div class="col100 clear">
				    			<label class="formEdicaoLabel" for="ProcessoNumero">Número do Processo</label><br> 
							    <input class=formEdicaoInput name="ProcessoNumero" id="ProcessoNumero"  type="text" size="30" maxlength="25" onkeyup="autoTab(this,25);" value="${pesquisaAvancadaDt.numeroProcesso}"/>
							    <em>&nbsp;Digite o Número do Processo completo. Ex: <strong>5000280.28.2010.8.09.0059</strong></em>
				    		</div>
				    		<div class="col45">
						    	<label class="formEdicaoLabel" for="Serventia">Serventia
						    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" 
						    			type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','${PermissaoLocalizarServentia}');"/>
						    	</label><br>
						    	<input type="hidden" id="Id_Serventia" name="Id_Serventia" value="${pesquisaAvancadaDt.id_serventia}" />
						    	<input class="formEdicaoInputSomenteLeitura" readonly name="Serventia" id="Serventia" type="text" size="60" maxlength="255" value="${pesquisaAvancadaDt.serventia}" />
					    	</div>
				    		<div class="col45">
						    	<label class="formEdicaoLabel" for="Serventia">Magistrado Responsável
						    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarJuiz" name="imaLocalizarJuiz" type="image" src="./imagens/imgLocalizarPequena.png" 
						    			onclick="AlterarValue('PaginaAtual','${PermissaoLocalizaruUsuario}');" />  
						    	</label><br>
						    	<input type="hidden" id="Id_Usuario" name="Id_Usuario" value="${pesquisaAvancadaDt.id_realizador}" />
						    	<input class="formEdicaoInputSomenteLeitura" readonly name="Usuario" id="Usuario" type="text" size="60" maxlength="255" value="${pesquisaAvancadaDt.realizador}" />
					    	</div>
					    	<div class="col45">
						    	<label class="formEdicaoLabel" for="Cidade">Data de Publicação</label><br>
				    			<input class="formEdicaoInputSomenteLeitura" style="float:left;" name="DataInicial" id="DataInicial"  type="text" size="10" maxlength="10" value="${pesquisaAvancadaDt.dataPublicacaoIni}" />
				    			<img id="calendarioDataInicial" style="float:left;vertical-align:middle;" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" 
				    				onclick="displayCalendar(document.forms[0].DataInicial,'dd/mm/yyyy',this)">
				    			<input class="formEdicaoInputSomenteLeitura"  style="margin-left:20px;" name="DataFinal" id="DataFinal"  type="text" size="10" maxlength="10" value="${pesquisaAvancadaDt.dataPublicacaoFim}" />
				    			<img id="calendarioDataFinal" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" 
				    				onclick="displayCalendar(document.forms[0].DataFinal,'dd/mm/yyyy',this)">
					    	</div>
					    	<div class="col45">
					    		<label class="formEdicaoLabel" for="ArquivoTipo">Tipo de Arquivo
						    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarArquivoTipo" name="imaLocalizarArquivoTipo" type="image" src="./imagens/imgLocalizarPequena.png" 
						    			onclick="AlterarValue('PaginaAtual','${PermissaoLocalizarArquivoTipo}');"/>  
						    	</label><br>
						    	<input type="hidden" id="Id_ArquivoTipo" name="Id_ArquivoTipo" value="${pesquisaAvancadaDt.id_tipoArquivo}" />
						    	<input class="formEdicaoInputSomenteLeitura" readonly name="ArquivoTipo" id="ArquivoTipo" type="text" size="60" maxlength="255" value="${pesquisaAvancadaDt.tipoArquivo}" />					    						    	
					    	</div>
					    </div>
					</fieldset>
				</div>
				
				<!-- Barra de botões -->
				<div style="margin:10px;">
					<div class="Centralizado">
						<input id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" onclick="javascript:submitForm(0);"/>						
						<input name="imgInserir" type="submit" value="Limpar" title= "Limpa os campos da tela" onclick="javascript:limparForm('Formulario');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
					</div>
				</div>
								
				<!-- Painel de resultados -->
				<c:if test="${QtdeResultados >= 0 && PaginaAtual == 2}">
					<div class="divEditar" id="divEditar">
						<fieldset id="formLocalizar" class="formLocalizar">							
							<legend	id="formLocalizarLegenda" class="formLocalizarLegenda">${QtdeResultados} resultados encontrados para o filtro da pesquisa</legend>
							<small>Tempo de resposta: (${TempoResposta} milissegundos)</small>
							<br />
							<c:forEach var="item" items="${ListaDocumentos}">
								<div class="search-result">
									<p>
										<h4>
											<c:out value="${item.source.numero}"/>
											<span style="margin-left: 10px">												
												<a href="#" title="Clique aqui para fazer o download do arquivo completo" 
													onclick="javascript: abrirArquivo('${tempRetorno}', '${item.source.id_arquivo}'); return false;">
													<i class="fa fa-download"></i>
													<c:if test="${not empty item.source.extra}">
														Baixar Inteiro teor
													</c:if>
												</a>												
											</span>
										</h4>
									</p>
									<p>
										<b><c:out value="${item.source.serv}"/></b>
									</p>
									<p>
										<b><i><c:out value="${item.source.realizador}"/></i></b>
									</p>
									<p>
										<b><c:out value="${item.source.tipo_arq}"/></b>
									</p>
									<p>
										<b><i><c:out value="${item.source.dataPublicacao}"/></i></b>
									</p>
									<c:if test="${not empty item.source.extra}">
										<p style="text-align:justify">											
											<c:out value="${item.extraDestacado}" escapeXml="false"/>																		
										</p>
									</c:if>								
									<c:if test="${empty item.source.extra}">
										<p style="text-align:justify">
											<c:out value="${item.textoDestacado}" escapeXml="false"/>
										</p>
									</c:if>
									<hr class="hr-dashed-line" />
									<c:if test="${fn:length(item.highlight.texto) gt 0}">
										<p style="text-align:justify">
											<b><c:out value="${fn:length(item.highlight.texto)}"/></b><i> resultado(s) encontrado(s) no INTEIRO TEOR.</i>										
										</p>
										<p style="text-align:justify">											
											<c:forEach var="s" items="${item.highlight.texto}">
												<c:out value="${s}" escapeXml="false"/>
											</c:forEach>
										</p>
									</c:if>								
								</div>								
							</c:forEach>
						</fieldset>
						<c:if test="${QuantidadePaginas > 1}">
							<div id="Paginacao" class="Paginacao" style="float: center">
								<div id="Paginacao" class="Paginacao">
									<b>Página</b>
									<c:forEach begin="${PaginaInicial}" end="${PaginaFinal}" varStatus="index">
										 <c:choose>
										 	<c:when test="${PosicaoPaginaAtual eq index.getIndex() - 1}">
										 		<b>| ${index.getIndex()}  |</b>
										 	</c:when>
										 	<c:otherwise>
										 		<a href="#" onclick="javascript:submitForm(${index.getIndex()-1});">${index.getIndex()}</a>
										 	</c:otherwise>
										 </c:choose>
									</c:forEach>
									<a href="#" onclick="javascript:submitForm(${QuantidadePaginas-1});">Última</a>
									<input id="CaixaTextoPosicionar" name="CaixaTextoPosicionar" class="CaixaTextoPosicionar" type="text" size="5" maxlength="10" value="${QuantidadePaginas}"/>
									<input class="BotaoIr" type="button" value="Ir" onclick="javascript:IrParaOnClick();"/>
								</div>
							</div>
						</c:if>						
					</div>
				</c:if>
				<%@ include file="Padroes/reCaptcha.jspf" %>
			</form>			
		</div>
	</div>
	<%@ include file="Padroes/Mensagens.jspf"%>	
</body>
</html>