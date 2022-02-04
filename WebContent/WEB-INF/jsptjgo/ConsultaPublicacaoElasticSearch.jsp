<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>
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
		function incluirOperador(token){
			var input = $("#textoDigitado");
			input.val(input.val() + " " + token + " ");
			input.focus();
		}
		function abrirArquivo(action, id){
			var token = $('#g-recaptcha-response').val();
			$('<form action="'+ encodeURI(action + '?PaginaAtual=1&Id_Arquivo=' + id + '&g-recaptcha-response=' + token) +'" method="post"></form>').appendTo('body').submit().remove();
		}		
		$(document).ready(function(){
		
			$('.powertip').powerTip({placement: 's'}).each(function (){
			  	$('#' + $(this).attr('id')).data('powertiptarget', $(this).attr('data-target'));
			});

			$('#ProcessoNumero').mask('9999999.99.9999.9.99.9999');
			
			$("input[name=tipoConsulta][value='texto']").is(':checked') ? $('#textoDigitado').focus() : $('#ProcessoNumero').focus();
						
			$('#textoDigitado').on('focusin', function(){
				$("input[name=tipoConsulta][value='texto']").prop("checked", true);
			});
			$('#textoDigitado').on('change', function(){
				$("input[name=tipoConsulta][value='texto']").prop("checked", true);
			});

			$("#textoDigitado").keypress(function(e){
			 	if (e.which == 13){
			        e.preventDefault();
			        AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');
			        document.forms[0].submit();
			    }
			});

			$('#filtro-campo div input:text').on('focusin', function(){
				$("input[name=tipoConsulta][value='campo']").prop("checked", true);
			});
			$('#filtro-campo div input:text').on('change', function(){
				$("input[name=tipoConsulta][value='campo']").prop("checked", true);
			});
			$('#filtro-campo div img').on('click', function(){
				$("input[name=tipoConsulta][value='campo']").prop("checked", true);
			});
			
		});
	</script>		
</head>
<body class="fundo">
	<%@ include	file="/CabecalhoPublico.html"%>
	<div id="divCorpo" class="divCorpo" style="margin-bottom:20px;">
		<div class="area"><h2> &raquo; Publicações</h2></div>	
		<div id="divLocalizar" class="divLocalizar">			
			<form id="Formulario" method="post" action="${tempRetorno}">
				<input type="hidden" id="PaginaAtual" name="PaginaAtual" value="${PaginaAtual}" />
				<input type="hidden" id="PosicaoPaginaAtual" name="PosicaoPaginaAtual" value="${PosicaoPaginaAtual}" />							
				<div id="divEditar" class="divEditar">
					<!-- Pesquisa por texto -->
					<fieldset class="formEdicao">
						<legend>
							<input type="radio" name="tipoConsulta" value="texto" ${tipoConsulta eq 'texto' ? "checked=\"checked\"" : ""}/>Pesquisa Livre
						</legend>
						<div id="filtro-texto">
							<p><label>Digite um termo para a pesquisa de publicações:</label></p>
							<p>
								<input type="text" id="textoDigitado" name="textoDigitado" title="Texto para consulta de publicação" style="width:100%; padding: 12x 20px;  margin: 8px 0;" value="${textoDigitado}"/>
							</p>
							<div>
								<input type="button" id="btnE" class="powertip" data-target="Etip" onclick="javascript:incluirOperador('E');" value="e"/>
								<input type="button" id="btnOU" class="powertip" data-target="OUtip" onclick="javascript:incluirOperador('OU');" value="ou"/>
								<input type="button" id="btnADJ" class="powertip" data-target="ADJtip" onclick="javascript:incluirOperador('ADJ');" value="adj"/>
								<input type="button" id="btnNAO" class="powertip" data-target="NAOtip" onclick="javascript:incluirOperador('NAO');" value="não"/>
								<input type="button" id="btnPROX" class="powertip" data-target="PROXtip" onclick="javascript:incluirOperador('PROX');" value="prox"/>
								<input type="button" id="btnLIKE" class="powertip" data-target="LIKEtip" onclick="javascript:incluirOperador('$');" value="$"/>
							</div>
						</div>
					</fieldset>					
					<!-- Pesquisa por campos de filtro -->
					<fieldset class="formEdicao">
						<legend>
							<input type="radio" id="tipoConsulta" name="tipoConsulta" value="campo" ${tipoConsulta eq 'campo' ? "checked=\"checked\"" : ""}/>Pesquisa por campo específico
						</legend>
						<div id="filtro-campo">
							<div class="col100 clear">
								<label class="formEdicaoLabel" for="Texto">Texto</label><br>
				    			<input type="text" id="Texto" name="Texto" title="Texto para consulta de publicação" style="width:70%" value="${pesquisaAvancadaDt.texto}" />
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
						    	<input class="formEdicaoInputSomenteLeitura" readonly name="Serventia" id="Serventia" type="text" size="60" maxlength="255" value="${pesquisaAvancadaDt.serventia}" />
					    	</div>
				    		<div class="col45">
						    	<label class="formEdicaoLabel" for="Serventia">Magistrado Responsável
						    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarJuiz" name="imaLocalizarJuiz" type="image" src="./imagens/imgLocalizarPequena.png" 
						    			onclick="AlterarValue('PaginaAtual','${PermissaoLocalizaruUsuario}');" />  
						    	</label><br>  
						    	<input class="formEdicaoInputSomenteLeitura" readonly name="Juiz" id="Juiz" type="text" size="60" maxlength="255" value="${pesquisaAvancadaDt.realizador}" />
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
						    	<input class="formEdicaoInputSomenteLeitura" readonly name="ArquivoTipo" id="ArquivoTipo" type="text" size="60" maxlength="255" value="${pesquisaAvancadaDt.tipoArquivo}" />					    						    	
					    	</div>
					    </div>
					</fieldset>
				</div>
				
				<!-- Barra de botões -->
				<div style="margin:10px;">
					<div class="Centralizado">
						<input id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" onclick="javascript:submitForm(0);"/>						
						<input name="imgInserir" type="submit" value="Limpar" title= "Limpa os campos da tela" onclick="javascript:limparForm('Formulario');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');" />
					</div>
				</div>
								
				<!-- Painel de resultados -->
				<c:if test="${QtdeResultados >= 0 && PaginaAtual == 2}">
					<div class="divEditar" id="divEditar">
						<fieldset id="formLocalizar" class="formLocalizar">
							<c:choose>
								<c:when test="${tipoConsulta eq 'texto'}">
									<legend	id="formLocalizarLegenda" class="formLocalizarLegenda">${QtdeResultados} resultados encontrados para: "${textoDigitado}"</legend>	
								</c:when>
								<c:otherwise>
									<legend	id="formLocalizarLegenda" class="formLocalizarLegenda">${QtdeResultados} resultados encontrados para o filtro da pesquisa</legend>
								</c:otherwise>
							</c:choose>
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
									<c:if test="${not empty item.source.texto}">
										<p style="text-align:justify">
											<c:out value="${item.textoDestacado}" escapeXml="false"/>
										</p>
									</c:if>
									<c:if test="${tipoConsulta eq 'texto'}">
										<p style="text-align:justify">
											<c:forEach var="s" items="${item.highlight.texto}">
												<c:out value="${s}" escapeXml="false"/>
											</c:forEach>
										</p>										
									</c:if>
								</div>
								<c:if test="${tipoConsulta eq 'texto'}">
									<hr class="hr-dashed-line" />
								</c:if>
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
			
			<div id="Etip" class="tooltip">
				<p class="tooltip-head"><b>E</b></p>
				<p>Furto e estacionamento e supermercado</p>
				<span>Obs.: Localiza as três palavras em qualquer lugar do documento.</span>
			</div>
			
			<div id="OUtip" class="tooltip">
				<p class="tooltip-head"><b>OU</b></p>
				<p>Furto e estacionamento e (supermercado ou hipermercado ou mercado)</p>
				<span>Obs.: Localiza um e/ou outro termo digitado entre parênteses.</span>
			</div>
											
			<div id="NAOtip" class="tooltip">
				<p class="tooltip-head"><b>NÃO</b></p>
				<p>Furto e estacionamento não supermercado</p>
				<span>Obs.: Localiza documentos que contenham as palavras "furto" e "estacionamento", excluindo documentos que contenham a palavra "supermercado".</span>
			</div>
			
			<div id="ADJtip" class="tooltip">
				<p class="tooltip-head"><b>ADJ</b></p>
				<p>Furto adj5 estacionamento adj4 supermercado</p>
				<span>Obs.: "Estacionamento" será no máximo a quinta palavra após "furto" e "supermercado" será no máximo a quarta palavra após "estacionamento".</span>
			</div>
			
			<div id="PROXtip" class="tooltip">
				<p class="tooltip-head"><b>PROX</b></p>
				<p>Furto prox6 estacionamento prox4 supermercado</p>
				<span>Obs.: "Estacionamento" será no máximo a sexta palavra antes ou após "furto"; "supermercado" será no máximo a quarta palavra antes ou após "estacionamento".</span>
			</div>
			
			<div id="LIKEtip" class="tooltip">
				<p class="tooltip-head"><b>$</b></p>
				<p>furto e estacionamento e $mercado</p>
				<span>Obs: Localiza as palavras que contenham a parte digitada e suas variações: supermercado, hipermercado ou mercado.</span>
			</div>
			
		</div>	<!-- End divLocalizar -->
		
	</div> <!-- End divCorpo -->
	
	<%@ include file="Padroes/Mensagens.jspf"%>	
</body>
</html>