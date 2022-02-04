<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ArquivoPalavraDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<html>
	<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
		@import url('js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
	</style>
	<title>|<%=request.getAttribute("tempPrograma")%>| Busca de Publicações</title>
	<link href="./css/Principal.css" type="text/css" rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/Digitacao/DigitarData.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type="text/javascript" src="./js/tabelas.js"></script>
	<script type="text/javascript" src="./js/tabelaArquivos.js"></script>
	<%@ include file="js/buscarArquivos.js"%>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
	<script type='text/javascript'>		
		    var _PaginaEditar = <%=request.getAttribute("tempPaginaAtualJSON")%>;
		    var _PaginaExcluir = <%=Configuracao.Excluir%>;
	</script>
	<script>
		function alterarValorRadio(tipoConsulta){
			$("#ConsultaTipo").val(tipoConsulta);
		}
		function buscaDadosPublicoJSON(url, posicaoPaginaAtual, tamanho, boMostrarExcluir, paginaAtual, qtdeNomeBusca){
	
			var timer;
			var tabela =  $('#tabelaLocalizar');
			if (posicaoPaginaAtual=="") posicaoPaginaAtual=$("#CaixaTextoPosicionar").val()-1;	
			tabela.html('');
			var b = "\'" + url + "?PaginaAtual=8";
			var boFecharDialog = false;
			
			$.ajax({
				url: encodeURI(url+'?Passo=1&PaginaAtual='+ paginaAtual + '&Id_Serventia=' + document.getElementById("Id_Serventia").value + '&dataInicial=' + document.getElementById("dataInicial").value + '&dataFinal=' + document.getElementById("dataFinal").value +  '&textoPublicacao=' + document.getElementById("textoPublicacao").value  + '&ConsultaTipo=' + document.getElementById("ConsultaTipo").value + '&PosicaoPaginaAtual=' + posicaoPaginaAtual),
				context: document.body,
				timeout: 300000, async: true,
				success: function(retorno){
					var inLinha=1;			
					var totalPaginas =0;
					var corpoTabela = "";
					tabela.append('<thead><tr><th width="8%" align="center"></th><th width="8%" align="center">Id</th><th align="center">Serventia</th><th align="center">Tipo</th><th align="center">Nome</th><th align="center">Data de Inserção</th><th align="center">Abrir</th></tr></thead>');
					$.each(retorno, function(i,item){
						if(item.id=="-50000"){						
							//Quantidade páginas
							totalPaginas = item.desc1;
						}else if (item.id=="-60000"){
							//posição atual
							posicaoPaginaAtual = item.desc1;
						}else {
		         			corpoTabela +='<tr class="MarcarLinha TabelaLinha' + inLinha + '">';
							corpoTabela +='<td class="Centralizado" >' + (i-1) + '</td>';
							corpoTabela +='<td class="Centralizado" >' + item.id + '</td>';
							corpoTabela +='<td >' + item.desc1 + '</td>';
							var iColuna=2;
		         			while((descricao=eval('item.desc' + iColuna))!=null) {
		         				corpoTabela +='<td >' + descricao + '</td>';
		                     	iColuna++;
		                	}  
		         			var bTemp = b + "&amp;Id_Arquivo=" + item.id + "&amp;PassoBusca=2','_blank','toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=no,resizable=yes,fullscreen=yes";
		         			corpoTabela +='<td class="Centralizado"><img src="imagens/22x22/ico_editar.png" alt="Abrir Arquivo Publicação" title="Abrir Arquivo Publicação" onClick="window.open(' + bTemp +'\')"></td>';
		         			corpoTabela +='</tr>';
							
							if (inLinha==1) inLinha=2; else inLinha=1;
						}
						
					});
					tabela.append(corpoTabela);
					//crio a paginação
					CriarPaginacaoJSON(url, posicaoPaginaAtual,totalPaginas, tamanho, boMostrarExcluir, paginaAtual, qtdeNomeBusca,"buscaDadosPublicoJSON"); 
				},
				beforeSend: function(data ){
					//Mostra a mensagem de "consultando" apenas se o tempo de consulta exceder o do timer
					timer = setTimeout(function() {
						mostrarMensagemConsultando('Projudi - Consultando', 'Aguarde, buscando os dados...');
						boFecharDialog=true;
					}, 1500);
					$("#formLocalizarBotao").hide();			
				},
				 error: function(request, status, error){	
					boFecharDialog=false;
					if (error=='timeout'){
						mostrarMensagemErro('Tempo Excedido', "A ação demorou tempo demais, tente novamente mais tarde.");
					}else{
						mostrarMensagemErro("Projudi - Erro", request.responseText);
					}
		        },				  
				complete: function(data ){
					//Quando completar a consultar, previnir a mensagem de "consultando" de ser mostrada
					clearTimeout(timer);
					if (boFecharDialog){
						$("#dialog").dialog('close');
					}
					$("#formLocalizarBotao").show();
				  }
			}); // fim do .ajax*/				     
			
		}

		function CriarPaginacaoJSON(url, posicaoPaginaAtual, totalPaginas, tamanho, boMostrarExcluir, paginaAtual, qtdeNomeBusca, funcaoBuscaDados){
	        var tempString;									
			var loPaginaAtual = parseInt(posicaoPaginaAtual); 
			var total = Math.ceil((totalPaginas / tamanho));  								
			
			//Guarda a última página selecionada
			var loPaginaSelecionada= (loPaginaAtual+1);
			
			//determino onde vai começar a contagem de páginas
			var loPaginaInicial= loPaginaAtual - Math.floor((tamanho / 2 ));
			if (loPaginaInicial<1) loPaginaInicial = 1;
			
			var loPaginaFinal = loPaginaInicial + tamanho -1;
			
			if (loPaginaFinal > total)
				loPaginaFinal = total;
			
			if (loPaginaInicial > (loPaginaFinal - (tamanho -1)))
				loPaginaInicial = loPaginaFinal - (tamanho -1);
				
			if (loPaginaInicial<1) loPaginaInicial = 1;
			
			//alert('Total Paginas' + total);
			//alert('Total Pagina atual' + PaginaAtual);
			//alert('Total Tamanho' + tamanho);
			//se houver só uma página não gerar paginação
			if(total==1){
				$("#Paginacao").html( "");
				calcularTamanhoIframe();
				return
			}
			tempString = "<b>P&aacute;gina <\/b>\n";
			tempString +="<a href=\"javascript:" + funcaoBuscaDados + "(\'" + url + "\',\'0\'," + tamanho + "," + boMostrarExcluir + "," + paginaAtual + ",\'" + qtdeNomeBusca + "\')\"> Primeira </a>";
	
			loPaginaAtual = loPaginaInicial;
			while(loPaginaAtual<=loPaginaFinal){	
				if (loPaginaAtual==loPaginaSelecionada){
					tempString+= "<b>| " + (loPaginaAtual) + " |<\/b>";
				} else {				
					tempString +="<a href=\"javascript:" + funcaoBuscaDados + " (\'" + url + "\',\'" +(loPaginaAtual-1) + "\',"  + tamanho + "," + boMostrarExcluir + "," + paginaAtual + ",\'" + qtdeNomeBusca + "\')\"> " + (loPaginaAtual)  + " </a>";
				}		
				loPaginaAtual++;			
			}		
			
			tempString +="<a href=\"javascript:" + funcaoBuscaDados + "(\'" + url + "\'," +(total-1) + ","  + tamanho + "," + boMostrarExcluir + "," + paginaAtual + ",\'" + qtdeNomeBusca + "\')\"> Última </a>";
			
			tempString+="<input id=\"CaixaTextoPosicionar\" value=\"" + (total) + "\" class=\"CaixaTextoPosicionar\" type=\"text\" size=\"5\" maxlength=\"10\" /><input class=\"BotaoIr\" type=\"button\" value=\"Ir\" onclick=\"" + funcaoBuscaDados + "(\'"+ url + "\',\'\'," + tamanho + "," + boMostrarExcluir + "," + paginaAtual + ",\'" + qtdeNomeBusca + "\' ); return false;\" />";
			$("#Paginacao").html( tempString);
			calcularTamanhoIframe();
		}
	</script>
	<%
		String stTempRetorno = (String)request.getAttribute("tempRetorno");
		String stTempBuscaPrograma = (String)request.getAttribute("tempBuscaPrograma");
		String[] descricao = (String[])request.getAttribute("lisDescricao");
	%>
</head>
<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de Publicações</h2></div>
		<div id="divLocalizar" class="divLocalizar">
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input type="hidden" id="PaginaAtual" name="PaginaAtual" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input type="hidden" id="PaginaAnterior" name="PaginaAnterior" value="" />
				<input type="hidden" id="Id_Serventia" name="Id_Serventia"	value="<%=request.getAttribute("Id_Serventia")%>" />
				<input type="hidden" id="ConsultaTipo" name="ConsultaTipo" value="1"/>
				<fieldset id="formLocalizar" class="formLocalizar">
					<legend	id="formLocalizarLegenda" class="formLocalizarLegenda">Filtrar</legend>
					
					<label class="formLocalizarLabel">*Texto</label><br>
					<input class="formLocalizarInput" name="textoPublicacao" id="textoPublicacao" type="text" value="<%=request.getSession().getAttribute("textoPublicacao")%>"	size="57" maxlength="60" title="Texo para consulta de publicação" />
					<input type="radio" name="ConsultaTipoRadio" value="1" onclick="alterarValorRadio('1')" checked="checked">Texto Exato
					<input type="radio" name="ConsultaTipoRadio" value="2" onclick="alterarValorRadio('2')">Qualquer Palavra <br />
					
					<label	class="formLocalizarLabel">Serventia
					<input	class="FormEdicaoimgLocalizar" name="imaLocalizarServentia"	type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAnterior', '2');AlterarValue('PaginaAtual', '<%=String.valueOf(ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" title="Selecione a serventia" />
					<input class="FormEdicaoimgLocalizar" name="imaLimparServentia" type="image" src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Serventia', 'Serventia'); return false;"	title="Limpar a serventia" />
					</label><br>
					
					<input class="formLocalizarInput" name="Serventia" readonly type="text" size="50" maxlength="50" id="Serventia" value="<%=request.getAttribute("Serventia")%>" />
					
					<br />
			
					<label class="formLocalizarLabel">Data Inicial</label><br>
					<input class="formLocalizarInput" name="dataInicial" id="dataInicial" type="text" value="<%=request.getSession().getAttribute("dataInicial")%>"	maxlength="60" title="Data inicial da publica&ccedil;&atilde;o"	onkeyup="mascara_data(this)" onblur="verifica_data(this)" />
					<img src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário" alt="Calendário" onclick="displayCalendar(document.forms[0].dataInicial,'dd/mm/yyyy',this)" />
					<input class="FormEdicaoimgLocalizar" name="imaLimparServentia"	type="image" src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('dataInicial', ''); return false;" title="Limpar Data" /><br>
					
					<label class="formLocalizarLabel">Data Final</label><br>
					<input class="formLocalizarInput" name="dataFinal" id="dataFinal" type="text" value="<%=request.getSession().getAttribute("dataFinal")%>" maxlength="60" title="Data final da publica&ccedil;&atilde;o"	onkeyup="mascara_data(this)" onblur="verifica_data(this)" />
					<img src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário" alt="Calendário" onclick="displayCalendar(document.forms[0].dataFinal,'dd/mm/yyyy',this)" />
					<input class="FormEdicaoimgLocalizar" name="imaLimparServentia" type="image" src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('dataFinal', ''); return false;" title="Limpar Data" /><br />
			
					<div class="Centralizado"><input id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" onclick="javascript:buscaDadosPublicoJSON('<%=stTempRetorno%>', '0', <%=Configuracao.TamanhoRetornoConsulta%>, false,  <%=request.getAttribute("PaginaAtual")%>, '4'); return false;" /></div>
				</fieldset>
		
				<fieldset id="formLocalizar" class="formLocalizar">
					<legend	id="formLocalizarLegenda" class="formLocalizarLegenda">Validar Documento</legend>
					
					<label class="formLocalizarLabel">Código de Validação</label><br>
					<input class="formLocalizarInput" name="codPublicacao" id="codPublicacao" type="text" value="" maxlength="12" title="Código do documento para validação" /><br />
					<div class="Centralizado">
						<input id="formValidarBotao" class="formValidarBotao" type="submit" name="Validar" value="Validar" onclick="$('#Formulario').attr('target','_blank'); AlterarValue('PaginaAtual', '<%=Configuracao.Curinga7%>');" />
					</div>
				</fieldset>
				<br />
				<div id="divTabela" class="divTabela">
					<table id="tabelaLocalizar" class="Tabela"></table>
				</div>
			</form>
		</div>
		<div id="Paginacao" class="Paginacao"></div>
	</div>
	<%@ include file="Padroes/Mensagens.jspf"%>
</body>
</html>