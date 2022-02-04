<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.MandadoPrisaoStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.MandadoPrisaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PrisaoTipoDt"%>

<jsp:useBean id="MandadoPrisaodt" scope="session" class= "br.gov.go.tj.projudi.dt.MandadoPrisaoDt"/>


<head>
	<title>Mandado de Prisão</title>

	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
	
	<link href="./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" type="text/css" rel="stylesheet" media="screen" />
	
    <link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
    <link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>      
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type='text/javascript' src='./js/checks.js'></script>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>	
	
	
	<script type="text/javascript">
		$(document).ready(function(){						
			$("#nomeBusca1").focus();
		});

		function buscaDadosJSON(posicaoPaginaAtual, tamanho, paginaAtual, qtdeNomeBusca, tempFluxo){
			
			var tabela =  $('#CorpoTabela');
			var nomeBusca = "";
			for(i=1;i<=qtdeNomeBusca;i++) {
				 nomeBusca += "&nomeBusca" + i + "=" + $("#nomeBusca"+ i).val();
			}
			if (posicaoPaginaAtual=="") posicaoPaginaAtual=$("#CaixaTextoPosicionar").val()-1;	
			var tempFluxo1 = tempFluxo;
			if (tempFluxo == null || tempFluxo == "") 
				tempFluxo1 = $("#tempFluxo1").val();	
			var tempFluxo2 = $("#tempFluxo2").val(); 
			var timer;
			
			tabela.html('');
			var boFecharDialog = false;
			$.ajax({
				url: encodeURI('MandadoPrisao?Passo=1&PaginaAtual='+ paginaAtual + nomeBusca + '&PosicaoPaginaAtual=' + posicaoPaginaAtual + '&tempFluxo1=' + tempFluxo1 + '&tempFluxo2=' + tempFluxo2),
				context: document.body,
				timeout: 300000, async: true,
				success: function(retorno){
					var inLinha=1;			
					var totalPaginas =0;
					var stTabela="";
					$.each(retorno, function(i,item){
						if(item.id=="-50000"){						
							//Quantidade páginas
							totalPaginas = item.desc1;
							
						}else if (item.id=="-60000"){
							//posição atual
							posicaoPaginaAtual = item.desc1;
						}else {
							stTabela +='<tr class="TabelaLinha' + inLinha + '" onclick="selecionaSubmeteJSON(' + item.id + ',\'' + item.desc1 + '\')" >';
							stTabela +='<td class="Centralizado" >' + (i-1) + '</td>';
							stTabela +='<td class="Centralizado" style="display: none" >' + item.id + '</td>';
							stTabela +='<td class="Centralizado" >' + item.desc1 + '</td>';
							stTabela +='<td class="Centralizado" >' + item.desc2 + '</td>';
							stTabela +='<td >' + item.desc3 + '</td>';
							var iColuna=4;
		         			while((descricao=eval('item.desc' + iColuna))!=null) {
		         				stTabela +='<td class="Centralizado" >' + descricao + '</td>';
		                     	iColuna++;
		                	}        			
		         			stTabela +='<td class="Centralizado" ><input name="formLocalizarimgEditar" type="image"  src="./imagens/imgEditar.png" onclick="selecionaSubmeteJSON(' + item.id + ',\'' + item.desc1 + '\')" />   </td>';
		          			stTabela +='</tr>';
			
							if (inLinha==1) inLinha=2; else inLinha=1;
						}								  	
					});
					
					tabela.append(stTabela);
					
					//crio a paginação
					CriarPaginacaoJSON(posicaoPaginaAtual,totalPaginas, tamanho); 
				},
				beforeSend: function(data ){					
					timer = setTimeout(function() {
						mostrarMensagemConsultando('Projudi - Consultando', '"Aguarde, buscando os dados...');
						boFecharDialog=true;
					}, 1500);
										
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
					if (boFecharDialog){
						$("#dialog").dialog('close');
					}
					$("#formLocalizarBotao").show();
				  }
			}); // fim do .ajax*/				     
					
		}
		
		function CriarPaginacaoJSON(posicaoPaginaAtual, totalPaginas, tamanho){
	        var tempString;		
	        
	        var loTotalPaginas = parseInt(totalPaginas);
			var loPaginaAtual = parseInt(posicaoPaginaAtual); 
			var loTamanho = parseInt(tamanho);
			var loTotal = Math.ceil((loTotalPaginas / loTamanho));  								
			var loPaginaSelecionada= (loPaginaAtual+1);
			var loPaginaInicial= loPaginaAtual - Math.floor((loTamanho / 2 ));
			if (loPaginaInicial<1) loPaginaInicial = 1;
			var loPaginaFinal = loPaginaInicial + loTamanho -1;
			if (loPaginaFinal > loTotal) loPaginaFinal = loTotal;
			if (loPaginaInicial > (loPaginaFinal - (loTamanho -1)))
				loPaginaInicial = loPaginaFinal - (loTamanho -1);
			if (loPaginaInicial<1) loPaginaInicial = 1;
			
			if(loTotal==1){
				$("#Paginacao").html("");
				calcularTamanhoIframe();
				return
			}
			tempString = '<b>P&aacute;gina <\/b>\n';
			tempString +='<a href=\"javascript:buscaDadosJSON(\'0\',\'' + loTamanho + '\',\'8\',\'5\',\'2\')\"> Primeira </a>';

			loPaginaAtual = loPaginaInicial;
			while(loPaginaAtual<=loPaginaFinal){	
				if (loPaginaAtual==loPaginaSelecionada){
					tempString+= "<b>| " + (loPaginaAtual) + " |<\/b>";
				} else {				
					tempString +="<a href=\"javascript:buscaDadosJSON('" +(loPaginaAtual-1) + "','"  + loTamanho + "',\'8\',\'5\',\'2\')\"> " + (loPaginaAtual)  + " </a>";
				}		
				loPaginaAtual++;			
			}		
			
			tempString +="<a href=\"javascript:buscaDadosJSON('" + (loTotal-1) + "','"  + loTamanho + "',\'8\',\'5\',\'2\')\"> Última </a>";
			
			tempString+="<input id=\'CaixaTextoPosicionar\' value=\'" + (loTotal) + "\' class=\'CaixaTextoPosicionar\' type=\'text\' size=\'5\' maxlength=\'10\'/>";
			tempString+="<input class=\'BotaoIr\' type=\'button\' value=\'Ir\' onclick=\"javascript:buscaDadosJSON('','"  + loTamanho + "',\'8\',\'5\',\'2\')\";return false; />";
			$("#Paginacao").html( tempString);
			calcularTamanhoIframe();
	}

		function selecionaSubmeteJSON(id, descricao) {
			AlterarValue('PaginaAtual', '<%=Configuracao.Curinga8%>');
			AlterarValue('tempFluxo1', '3');
			AlterarValue('tempBuscaId', id);
			FormSubmit('Formulario');
		}
		
	</script>
  <%
  String stTempRetorno = (String)request.getAttribute("tempRetorno");
  String stTempBuscaPrograma = (String)request.getAttribute("tempBuscaPrograma");
  %>
</head>
<body>
  <div id="divCorpo" class="divCorpo">
	<form action="MandadoPrisao" method="post" name="Formulario" id="Formulario">
		<div class="area"><h2>&raquo; Consultar Mandados de Prisão</h2></div>
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>">
		<input id="tempFluxo2" name="tempFluxo2" type="hidden" value="<%=request.getAttribute("tempFluxo2")%>">
        <input type="hidden" id="tempBuscaId" name="<%=request.getAttribute("tempBuscaId").toString()%>"/>

		<div id="divLocalizar" class="divLocalizar" >
			<fieldset id="formLocalizar" class="formLocalizar"> 
		    	<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Filtro da consulta Mandado de Prisão</legend>
		    	
		       	<label id="formEdicaoLabel" class="formLocalizarLabel">Processo</label><br>
		       	<input id="nomeBusca1" class="formLocalizarInput" name="nomeBusca1" type="text" value="<%=request.getParameter("numeroProcesso") != null?request.getParameter("numeroProcesso"):""%>" maxlength="60" /> 
		       	<br />
				<label class="formLocalizarLabel" for="MandadoPrisaoTipo">Tipo</label><br>
			    <select name="MandadoPrisaoTipoCodigo" id="nomeBusca5"name="nomeBusca5" >
				    <option value=""></option>
					<%if (MandadoPrisaodt.getListaPrisaoTipo() != null){
						List listaTipo = (List) MandadoPrisaodt.getListaPrisaoTipo();
						for (int i=0; i<listaTipo.size(); i++) {%>
			    	<option value="<%=((PrisaoTipoDt)listaTipo.get(i)).getPrisaoTipoCodigo()%>"><%=((PrisaoTipoDt)listaTipo.get(i)).getPrisaoTipo()%></option>
						<%} %>
					<%}%>
			    </select/>
				<br />

				<label class="formLocalizarLabel" for="MandadoPrisaoStatus" >Status</label><br>
			    <select name="MandadoPrisaoStatusCodigo" id="nomeBusca4" name="nomeBusca4">
				    <option value=""></option>
					<%if (MandadoPrisaodt.getListaMandadoPrisaoStatus() != null){
						List listaStatus = (List) MandadoPrisaodt.getListaMandadoPrisaoStatus();
						for (int i=0; i<listaStatus.size(); i++) {%>
			    	<option value="<%=((MandadoPrisaoStatusDt)listaStatus.get(i)).getMandadoPrisaoStatusCodigo()%>"><%=((MandadoPrisaoStatusDt)listaStatus.get(i)).getMandadoPrisaoStatus()%></option>
						<%} %>
					<%}%>
			    </select/>
				<br />
				
				<div class="col15">
				<label>Data Inicial</label><br> 
				<input class="formLocalizarInput" name="dataInicial" id="nomeBusca2" type="text" value="" onkeypress="return DigitarSoNumero(this, event)" onblur="verifica_data(this)" onkeyup="mascara_data(this)" />
				<img src="./imagens/dlcalendar_2.gif" class="calendario" title="Calendário"	alt="Calendário" onclick="displayCalendar(document.forms[0].dataInicial,'dd/mm/yyyy',this)" />				     
				</div>
				
				<div class="col15">
				<label>Data Final</label><br> 
				<input class="formLocalizarInput" name="dataFinal" id="nomeBusca3" type="text" value=""  onkeypress="return DigitarSoNumero(this, event)" onblur="verifica_data(this)" onkeyup="mascara_data(this)" />
				<img src="./imagens/dlcalendar_2.gif" class="calendario" title="Calendário"			alt="Calendário" onclick="displayCalendar(document.forms[0].dataFinal,'dd/mm/yyyy',this)" />
				</div>
				
				<div class="clear"></div>
				
				<div id="divBotoesCentralizados" class="divBotoesCentralizados">		       	
					<input id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" title="Consultar Mandados de Prisão"	onclick="javascript:buscaDadosJSON('0', <%=Configuracao.TamanhoRetornoConsulta%>,<%=request.getAttribute("PaginaAtual")%>, '5', '2'); return false;"/>
				</div>
		   	</fieldset>
		   	
			<div id="divTabela" class="divTabela" > 
			   	<table id="tabelaLocalizar" class="Tabela">
			       	<thead>
			           	<tr>
			                <th width='20px' align="center"></th>
			                <th style="display: none">Id</th>
			                <th>Processo</th>
			           		<th class="colunaMinima">N&uacute;mero</th>
			                <th width="40%">Nome Parte</th>
			           		<th class="colunaMinima">Sigilo</th>
							<th align="center">Tipo de Prisão</th>
			                <th width="15%">Data de Validade</th>
			                <th width="20%">Status</th>
			                <th width="15%">Data de Expedição</th>
			                <th width="15%">Data de Cumprimento</th>
			                <th class="colunaMinima">Selecionar</th>
						</tr>
					</thead>
					<tbody id="CorpoTabela">&nbsp;</tbody>
				
				</table>
			</div>
			<div id="Paginacao" class="Paginacao"></div></div>
		<%@ include file="./js/Paginacao.js"%>
		<%@ include file="Padroes/Mensagens.jspf"%>
		</div>
  </form>
 </div>

</body>
</html>