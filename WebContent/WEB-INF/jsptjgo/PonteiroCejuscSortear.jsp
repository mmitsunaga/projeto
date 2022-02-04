<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.PonteiroCejuscDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="PonteiroCejuscdt" scope="session" class= "br.gov.go.tj.projudi.dt.PonteiroCejuscDt"/>

<%@page import="br.gov.go.tj.projudi.dt.UsuarioCejuscDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PonteiroCejuscDt"%>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de PonteiroCejusc  </title>
	<style type="text/css">
 		@import url('./css/Principal.css');
 		@import url('./css/Paginacao.css');
		@import url('js/jscalendar/dhtmlgoodies_calendar.css');
	</style>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
   	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script language="javascript" type="text/javascript">
		function VerificarCampos() {
			with(document.Formulario) {
				if (SeNulo(UsuCejusc, "O Campo UsuCejusc é obrigatório!")) return false;
				if (SeNulo(Serv, "O Campo Serv é obrigatório!")) return false;
				if (SeNulo(UsuServConfirmou, "O Campo UsuServConfirmou é obrigatório!")) return false;
				if (SeNulo(UsuServCompareceu, "O Campo UsuServCompareceu é obrigatório!")) return false;
				if (SeNulo(PonteiroCejuscStatus, "O Campo PonteiroCejuscStatus é obrigatório!")) return false;
				submit();
			}
		}
		</script>

	<%}%>
	
	 <%boolean boMostrarExcluir = false;
	  String stTempRetorno = (String)request.getAttribute("tempRetorno");
	  String stTempBuscaPrograma = (String)request.getAttribute("tempBuscaPrograma");
	  //vejo se é para mostrar o excluir, caso que esteja fazendo a busca seja o proprio formulário principal
	  if(stTempRetorno!=null && stTempBuscaPrograma!=null){
	    if (stTempRetorno.equalsIgnoreCase(stTempBuscaPrograma)) boMostrarExcluir=true;
	  }
	  String[] descricao = (String[])request.getAttribute("lisDescricao");
	  String[] nomeBusca = (String[])request.getAttribute("lisNomeBusca");
	  String[] camposHidden = (String[])request.getAttribute("camposHidden");
	  String tamColuna = String.valueOf(60/descricao.length) + "%";
	  %>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
			
			<div class="area"><h2>&raquo;Sortear Conciliador/Mediador</h2></div>
		
			<fieldset>
			
				<form action="PonteiroCejusc?paginaAtual=" method="post">
					
					<div class="col15">
					<label>Data</label><br>
					<input id="nomeBusca1" name="nomeBusca1" size="10" maxlength="10" title="Clique para escolher uma data."  value="" onkeyup="mascara_data(this);checarFoco()" onkeypress="return DigitarSoNumero(this, event)" />
					<img id="calnomeBusca1" src="./imagens/dlcalendar_2.gif" height="15" width="15" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].nomeBusca1,'dd/mm/yyyy',this)" />
					</div>
					
					<br/>
					
					<div class="divPortaBotoes center">
					<input id="btnConsultar" type="button" value="Buscar/Sortear" onclick="javascript:buscaDadosCejuscJSON('<%=stTempRetorno%>', <%=boMostrarExcluir%>,  <%=request.getAttribute("PaginaAtual")%>, '1' , '0', <%=Configuracao.TamanhoRetornoConsulta%>); return false;"/>
					</div>

					<div id="divTabela" class="divTabela"> 
			          <table id="tabelaLocalizar" class="Tabela">
			            <thead>
			              <tr>
			                <th width='20px' align="center"></th>
			                <th width='40px' align="center">Id</th>                
			                <%for (int i=0;i<descricao.length;i++) {%>				
							<th width=tamColuna><%=descricao[i]%></th><%}%> 
			                <th class="colunaMinima" title="Sorteia um novo conciliador/mediador">Novo Sorteio</th>
			              </tr>
			            </thead>
			          	<tbody id="CorpoTabela">&nbsp;</tbody>
			          </table>
			        </div>
			        <div id="Paginacao" class="Paginacao"></div>
					
				</form>
				
			</fieldset>
	
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
		
<script language="javascript" type="text/javascript">
	
	function checarFoco() {
		if( $('#nomeBusca1').val().length >= 10 ){
			$('#btnConsultar').focus();
		}
	}
	
	function substituir(id){
		buscaDadosCejuscJSON('<%=stTempRetorno%>?fluxo=2&idPc='+id, <%=boMostrarExcluir%>,  <%=request.getAttribute("PaginaAtual")%>, "1" , "0", <%=Configuracao.TamanhoRetornoConsulta%>); 
	}
	
	function buscaDadosCejuscJSON(url, boMostrarExcluir, paginaAtual, qtdeNomeBusca, posicaoPaginaAtual, tamanho){
		
		var timer;
		var tabela =  $('#CorpoTabela');
		var nomeBusca = "";
		for(i=1;i<=qtdeNomeBusca;i++) {
			 nomeBusca += "&nomeBusca" + i + "=" + $("#nomeBusca"+ i).val();
		}
		if (posicaoPaginaAtual=="") posicaoPaginaAtual=$("#CaixaTextoPosicionar").val()-1;	
		var tempFluxo1 = $("#tempFluxo1").val();	
		var tempFluxo2 = $("#tempFluxo2").val(); 
		
		tabela.html('');
		var chamada = 'buscaDadosCejuscJSON(\''  + url + '\',' + boMostrarExcluir +  ',\'' + paginaAtual + '\',\'' +  qtdeNomeBusca + '\''  ; 
		
		var boFecharDialog = false;
		if(url.indexOf("?")>0){
			url = url+'&AJAX=ajax&Passo=1&PaginaAtual='+ paginaAtual + nomeBusca + '&PosicaoPaginaAtual=' + posicaoPaginaAtual + '&tempFluxo1=' + tempFluxo1 + '&tempFluxo2=' + tempFluxo2;
		} else{
			url = url+'?AJAX=ajax&Passo=1&PaginaAtual='+ paginaAtual + nomeBusca + '&PosicaoPaginaAtual=' + posicaoPaginaAtual + '&tempFluxo1=' + tempFluxo1 + '&tempFluxo2=' + tempFluxo2;
		}
		$.ajax({
			url: encodeURI(url),
			context: document.body,
			timeout: 300000, async: true,
			success: function(retorno){
				var inLinha=1;			
				var totalPaginas =0;
				var corpoTabela = "";
				
				
				//Testa a mensagem de retorno do servidor
				switch(true) {
					
					//Não existem bancas no dia especificado
					case /semPauta/.test(retorno):
						corpoTabela = "<tr><td colspan='7'> Sem bancas no dia escolhido. </td></tr>";
						break;
					
					//Não preencheu nenhuma data
					case /semData/.test(retorno):
						corpoTabela = "<tr><td colspan='7'> Escolha uma data. </td></tr>";
						break;
						
					//Tentou substituir um conciliador mas não encontrou outro elegível	
					case /semConciliador/.test(retorno):
						$('#dialog').dialog({buttons: [{ text: "OK", click: function() { $( this ).dialog("close");}}]});
						$("#dialog").html("Não foram encontrados conciliadores/mediadores disponíveis");
						$('#dialog').css({'background-image':'url("imagens/32x32/ico_erro.png")','background-repeat':'no-repeat'});
						boErro = true;
						break;
						
					case /substituicaoFeita/.test(retorno):
						$('#dialog').dialog({buttons: [{ text: "OK", click: function() { $( this ).dialog("close");}}]});
						$("#dialog").html("<br/>O responsável pela banca foi substituído");
						$('#dialog').css({'background-image':'url("imagens/22x22/ico_sucesso.png")','background-repeat':'no-repeat'});
						boErro = true;
						break;
					
					//Popular a tabela com uma lista de registros
					default:
						$.each(retorno, function(i,item){
							if(item.id=="-50000"){						
								//Quantidade paginas
								totalPaginas = item.desc1;
								
							}else if (item.id=="-60000"){
								//posicao atual
								posicaoPaginaAtual = item.desc1;
							}else {
								
								var dataSelecoes="";
								var iColuna=2;
			         			while((descricao=eval('item.desc' + iColuna))!=null) {         				
			         				dataSelecoes += "desc" + iColuna + ";" + descricao + ";";
			                     	iColuna++;
			                	}			
	
			         			corpoTabela += '<tr data_descs="'+ dataSelecoes +'" data_id1="' +  item.id  + '" data_desc1="' +  item.desc1 + '" class="TabelaLinha' + inLinha + '">';
								corpoTabela +='<td   class="Centralizado" >' + (i-1) + '</td>';
								corpoTabela +='<td   class="Centralizado" >' + item.id + '</td>';
								corpoTabela +='<td    >' + item.desc1 + '</td>';
								iColuna=2;
			         			while((descricao=eval('item.desc' + iColuna))!=null) {
			         				

									if(iColuna == 4) {
				         				
										if(descricao == "Avisado" )  {
					         				corpoTabela +='<td Style="color:#0404B4">' + descricao + '</td> >';
					         			}
					         			else if(descricao == "Recusado") {
					         				corpoTabela +='<td Style="color:#DF0101">' + descricao + '</td> >';
					         			}
					         			else if(descricao == "Confirmado") {
					         				corpoTabela +='<td Style="color:#31B404">' + descricao + '</td> >';
					         			}
					         			else {
					         				corpoTabela +='<td >' + descricao + '</td> >';
					         			}
			         				
									} else {
			         				
			         					corpoTabela +='<td >' + descricao + '</td>';
			         				}
			                     	
			         				iColuna++;
			                	} 
			         			
			         			if(item.desc4 == "Avisado" )  {
			         				corpoTabela +='<td class="Centralizado"><a onclick="substituir('+item.id+')" > Substituir </a> </td> >';
			         			}
			         			else if(item.desc4 == "Recusado") {
			         				corpoTabela +='<td class="Centralizado"><a onclick="substituir('+item.id+')" > Substituir </a> </td> >';
			         			}
			         			else {
			         				corpoTabela +='<td class="Centralizado" ></td> >';
			         			}
								
			         			corpoTabela +='</tr>';
				
								if (inLinha==1) inLinha=2; else inLinha=1;
							}
						});
						break;
						
				} /*Fim switch */
				
				tabela.append(corpoTabela);
				
				//crio a paginacao
				CriarPaginacaoJSON(chamada, posicaoPaginaAtual,totalPaginas, tamanho); 
				
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
	
</script>

</html>
