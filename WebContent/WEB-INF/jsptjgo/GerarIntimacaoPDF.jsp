<!DOCTYPE html>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
		</style>
		<title>|<%=request.getAttribute("tempPrograma")%>| Emitir PDF dos arquivos publicados</title>
		<link rel="shortcut icon" href="./imagens/favicon.png" / ></link>
		<link href="./css/Principal.css" type="text/css" rel="stylesheet" />
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/Digitacao/DigitarData.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
		<style type="text/css">
			#bkg_projudi {display: none}			
		</style>		
	</head>
	<body class="fundo" onload="">
		<%@ include	file="/CabecalhoPublico.html"%>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Emitir PDF das intimações expedidas</h2></div>							
			<form action="<%=request.getAttribute("tempRetorno")%>?PaginaAtual=2" method="post" id="form">
				<div id="divLocalizar" class="divLocalizar">
					<input type="hidden" id="PaginaAtual" name="PaginaAtual" value="<%=request.getAttribute("PaginaAtual")%>" />
					<fieldset id="formLocalizar" class="formLocalizar">
						<legend	id="formLocalizarLegenda" class="formLocalizarLegenda">Filtrar</legend>
						<div>
							<p>
							<label>Data da Intimação</label>
							<br>
							<input class="formLocalizarInput" name="dataPublicacao" id="dataPublicacao" type="text" value="<%=request.getAttribute("dataPublicacao")%>" maxlength="60" title="Data inicial da publica&ccedil;&atilde;o" onkeyup="mascara_data(this)" onblur="verifica_data(this)" />
							<img src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário" alt="Calendário" onclick="displayCalendar(document.forms[0].dataPublicacao,'dd/mm/yyyy',this)" />
							<input class="FormEdicaoimgLocalizar" name="imaLimparServentia"	type="image" src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('dataPublicacao', ''); return false;" title="Limpar Data" />
							</p>							
						</div>
						<div>
							<p>
								<label>Opção</label>
								<br>
								<select name="opcaoPublicacao">
									<option value="0">Todas as publicações</option>
									<option value="1">Publicações de 2o Grau</option>
									<option value="2">Publicações de 1o Grau - Capital</option>
									<option value="3">Publicações de 1o Grau - Interior</option>
								</select>
							</p>
						</div>
						<div class="Centralizado">
							<input id="formLocalizarBotao" class="formLocalizarBotao" type="button" name="Localizar" value="Consultar"/>
						</div>
					</fieldset>
				</div>
			</form>
		</div>
		<div id="dialog" style="display:none">&nbsp;</div>
		<script>
			$(document).ready(function(){
				var interval = 0;
				function podeFecharDialogo(){
					$.getJSON($('#form').attr('action') + '&PassoEditar=1', function(data){
						if (data.flag > 0){
				        	$("#formLocalizarBotao").show();
				        	clearInterval(interval);
						}
					});
				}
				$('#formLocalizarBotao').click(function(){
					$("#formLocalizarBotao").hide();
					interval = setInterval(podeFecharDialogo, 3000);
					$('#form').submit();
				});
			});
		</script>
	</body>
</html>