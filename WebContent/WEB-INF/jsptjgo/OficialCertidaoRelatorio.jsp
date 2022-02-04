<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<html>
<head>
  <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
  <title> |<%=request.getAttribute("tempPrograma")%>| Relatório de <%=request.getAttribute("tempBuscaPrograma")%>  </title>
  <link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
  <link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
  <script type='text/javascript' src='./js/jquery.js'></script>
  <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
  <script type='text/javascript' src='./js/Funcoes.js'></script>	
  
  <style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
  </style>

  <script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
  <script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
  <script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>      	
  <script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script> 
  <script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>  

  <script type='text/javascript'>		
    var _PaginaEditar = <%=request.getAttribute("tempPaginaAtualJSON")%>;
    var _PaginaExcluir = <%=Configuracao.Excluir%>;
  </script>
  <%boolean boMostrarGerarPdf = false;
  String stTempRetorno = (String)request.getAttribute("tempRetorno");
  String stTempBuscaPrograma = (String)request.getAttribute("tempBuscaPrograma");
  //vejo se é para mostrar o excluir, caso que esteja fazendo a busca seja o proprio formulário principal
  if(stTempRetorno!=null && stTempBuscaPrograma!=null){
    if (stTempRetorno.equalsIgnoreCase(stTempBuscaPrograma)) boMostrarGerarPdf=true;
  }
  String[] descricao = (String[])request.getAttribute("lisDescricao");
  String[] nomeBusca = (String[])request.getAttribute("lisNomeBusca");
  String[] camposHidden = (String[])request.getAttribute("camposHidden");
  String tamColuna = String.valueOf(60/descricao.length) + "%";
  if (request.getAttribute("consultaPublica") != null && request.getAttribute("consultaPublica").toString().equalsIgnoreCase("S")) {%>
  <style type="text/css"> #bkg_projudi{ display:none } </style>
  <% } %>
  
  
  
  
  
  <script language="javascript" type="text/javascript">
	  function buscaCertidaoJSON(url, posicaoPaginaAtual, tamanho, boMostrarGerarPdf, paginaAtual, qtdeNomeBusca){
			//Testa para ver se um dos campos estão preenchidos, neste formulários temos 3 campos de busca
			if($("#nomeBusca1").val()=="" && $("#dataInicial").val()=="" && $("#dataFinal").val()==""){
				alert('Favor informar número do mandado ou um período!');
				return;
			}else{
				if($("#nomeBusca1").val()==""){ //Caso o usuário não tenha informado um número de mandado, verifica o periodo de datas
					if($("#dataInicial").val()=="" || $("#dataFinal").val()==""){
						alert('Favor informar as duas datas!');
						return;	
					}else{
						if(comparaDatas('dataInicial', 'dataFinal')){ //Caso a primeira data for maior que a segunda, retorna verdadeiro
							alert('A primeira data não pode ser maior que a segunda!');
							return;
						}
					}
				}			
		  
			    var tabela =  $('#CorpoTabela');
				var nomeBusca = "";
				for(i=1;i<=qtdeNomeBusca;i++) {
					 nomeBusca += "&nomeBusca" + i + "=" + $("#nomeBusca"+ i).val();
				}
				if (posicaoPaginaAtual=="") posicaoPaginaAtual=$("#CaixaTextoPosicionar").val()-1;	
				var tempFluxo1 = $("#tempFluxo1").val();	
				
				tabela.html('');

				var boFecharDialog = false;
				var timer;
				$.ajax({
					url: encodeURI(url+'?Passo=1&PaginaAtual='+ paginaAtual + nomeBusca + '&dataInicial='+$("#dataInicial").val()+'&dataFinal='+$("#dataFinal").val()+'&PosicaoPaginaAtual=' + posicaoPaginaAtual + '&tempFluxo1=' + tempFluxo1),							
					context: document.body,
					timeout: 300000, async: true,
					success: function(retorno){
						var inLinha=1;	
						var QuantidadeRegistro=0; //Utilizado para mostrar o botão GerarPdf e dá mensagem de nenhum registro encontrado
						var totalPaginas =0;
						var corpoTabela = "";
						$.each(retorno, function(i,item){
							if(item.id=="-50000"){						
								//Quantidade páginas
								totalPaginas = item.desc1;
								
							}else if (item.id=="-60000"){
								//posição atual
								posicaoPaginaAtual = item.desc1;
							}else {
								
								var dataSelecoes="";
								var iColuna=2;
			         			while((descricao=eval('item.desc' + iColuna))!=null) {         				
			         				dataSelecoes += "desc" + iColuna + ";" + descricao + ";";
			                     	iColuna++;
			                	}			
		
			         			corpoTabela += '<tr data_descs="'+ dataSelecoes +'" data_id1="' +  item.id  + '" data_desc1="' +  item.desc1 + inLinha + '">';
								corpoTabela +='<td   class="Centralizado" >' + (i-1) + '</td>';
								corpoTabela +='<td   class="Centralizado" >' + item.id + '</td>';
								corpoTabela +='<td    >' + item.desc1 + '</td>';
								iColuna=2;
			         			while((descricao=eval('item.desc' + iColuna))!=null) {
			         				if (iColuna==4){ 
			         					corpoTabela +='<td > <INPUT TYPE=hidden name="Status_' + (i-1) + '" id="Status_' + (i-1) + '" value="' + descricao + '">' + descricao + '</td>';
			         				}else{ 
			         					corpoTabela +='<td >' + descricao + '</td>';
			         				}
			                     	iColuna++;
			                	}        			
			         			//corpoTabela +='<td class="Centralizado" ><input name="formLocalizarimgEditar" type="image"  src="./imagens/imgEditar.png" data_descs="'+ dataSelecoes +'" data_id1="' +  item.id  + '" data_desc1="' +  item.desc1 + '" class="MarcarLinha" />   </td>';
								corpoTabela +='<td class="Centralizado" ><INPUT TYPE=checkbox name="chkSelecao" id="chkSelecao" value="'+item.id+'"></td>';
								corpoTabela +='</tr>';
			         			QuantidadeRegistro=i-1;
								if (inLinha==1) inLinha=2; else inLinha=1;
							}
							
						});
						tabela.append(corpoTabela);
						//função para dar o submite com o click em cada linha da tabela
						// busca os dados inseridos no tr
						$('.MarcarLinha').click(function(event){
								//event.target
							    var jtag = $(this);
								var id1 = jtag.attr("data_id1");
								var desc1 =  jtag.attr("data_desc1");
			                   	var array = jtag.attr("data_descs").split(";");
			                   	//faço as alterações para cada desc maior que 1
			                   	for (i=0; i<(array.length-1);i=i+2)                   		
			                   		AlterarValue(array[i],array[i+1]);
			                   	//submeto com um click na linha
								selecionaSubmeteJSON(id1,desc1);
							});
						
						
						//Limpo o checked do cabeçalho
						document.Formulario.elements['Selecao'].checked=false;
						$('#Selecao').hide(); //oculta checked
						$('#Paginacao').show(); //mostrar DIV						
	
						if (QuantidadeRegistro>0){
							//crio a paginação
							CriarPaginacaoJSON(url, posicaoPaginaAtual,totalPaginas, tamanho, boMostrarGerarPdf, paginaAtual, qtdeNomeBusca, "buscaCertidaoJSON"); 
							$('#divBotoesCentralizados').show(); //mostra DIV
							$('#Selecao').show(); //mostra checked
						}else{
							$('#divBotoesCentralizados').hide(); //ocultar DIV
							$('#Paginacao').hide(); //ocultar DIV
						//	alert("Nenhum registro encontrado!");
						}
						//crio a paginação
						//CriarPaginacaoJSON(url, posicaoPaginaAtual,totalPaginas, tamanho, boMostrarExcluir, paginaAtual, qtdeNomeBusca); 
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
		} 
	  
		  
	//Função usada para selecionar todos os checkbox do formulário
	function selecionar_tudo(){
		for (i=0;i<document.Formulario.elements.length;i++)
	    	if(document.Formulario.elements[i].type == "checkbox"){
				if(document.Formulario.elements['Selecao'].checked==true){
					document.Formulario.elements[i].checked=1;	
				}else{
					document.Formulario.elements[i].checked=0;
				}
	    				    		
	    	}
	}
 
	
	//Gerar arquivos PDFs das certidões selecionadas
	function gera_pdf(){
		var itemchk = 0; // O item 0(zero) é do cabeçalho, a selecão começa no item 1
		var chkMarcados = 0; //Verifica se foi marcado algum registro
		var status_editar = 0; //Caso tenha algum registro selecionado com status editar, muda para true
		for (i=0;i<document.Formulario.elements.length;i++)
	    	if(document.Formulario.elements[i].type == "checkbox"){
				if(itemchk>0){ // O item 0(zero) é do cabeçalho, a selecão começa no item 1
					if(document.Formulario.elements[i].checked==true){
						if($("#Status_"+ (itemchk)).val()=="Editar"){
							status_editar = 1;
						}
						chkMarcados = 1;
					}
				}
				itemchk++;
	    	}
		//Valida se foi selecionado algum registro no formulário com status editar
		if(chkMarcados==0){
			alert("Selecione pelo menos um registro para gerar o relatório!");
		}else if (status_editar==1){
			alert("Favor selecionar somente registros finalizados!"); 
		}else {
			AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('tempFluxo1','4');
			document.Formulario.submit();
			AlterarValue('tempFluxo1','3');
		}
	}
		
		
  </script>  
</head>
<body>
<%if (request.getAttribute("consultaPublica") != null && request.getAttribute("consultaPublica").toString().equalsIgnoreCase("S")) {%>
  <%@ include file="/CabecalhoPublico.html" %> 
  <% } %>
  <div id="divCorpo" class="divCorpo" >
  
  <div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Relatório de <%=request.getAttribute("tempBuscaPrograma")%></h2></div>
    <div id="divLocalizar" class="divLocalizar"> 
      <form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
        <input type="hidden" id="tempBuscaId" name="<%=request.getAttribute("tempBuscaId").toString()%>"/>
        <input type="hidden" id="tempBuscaDescricao" name="<%=request.getAttribute("tempBuscaDescricao").toString()%>"/>
        <input type="hidden" id="PaginaAtual" name="PaginaAtual"  value="<%=request.getAttribute("PaginaAtual")%>"/>
        <input type="hidden" id="PaginaAnterior" name="PaginaAnterior"  value="<%=request.getAttribute("PaginaAnterior")%>"/>        				
		<input type="hidden" id="tempFluxo1" name="tempFluxo1"  value="<%=request.getAttribute("tempFluxo1")%>"/>
		<input type="hidden" id="PassoEditar" name="PassoEditar"  value="<%=request.getAttribute("PassoEditar")%>"/>
		<input type="hidden" id="ParteTipo" name="ParteTipo"  value="<%=request.getAttribute("ParteTipo")%>"/>
		
		<%if (camposHidden!=null)
			for (int i=0;i<camposHidden.length;i++) {%>
				<input type="hidden" id="desc<%=i+2%>" name="<%=camposHidden[i]%>"  value=""/>
			<%}%>			
		
        <fieldset id="formLocalizar" class="formLocalizar"> 
          
          <legend id="formLocalizarLegenda" class="formLocalizarLegenda">Consulta de <%=request.getAttribute("tempBuscaPrograma")%></legend>
          <%for (int i=0;i<nomeBusca.length;i++) {%>          	                    
     		<label id="formLocalizarLabel<%=i%>" class="formLocalizarLabel"><%=nomeBusca[i]%></label><br> 
    		<input id="nomeBusca<%=i+1%>" class="formLocalizarInput" name="nomeBusca<%=i+1%>" type="text" value="" style="width : 100px;"/>
		  <%}%> 

			<div class="col15">
    	  <label id="formLocalizarLabelDataInicial">Data Inicial</label><br> 
    	  <input id="dataInicial" class="formLocalizarInput" name="dataInicial" type="text" value="<%=request.getAttribute("dataInicial")%>" style="width : 100px;" onkeyup="mascara_data(this)" onblur="verifica_data(this)"/>
          <img id="calendarioDataInicial" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].dataInicial,'dd/mm/yyyy',this)">
		</div>
		
		<div class="col15">
    	  <label id="formLocalizarLabelDataFinal">Data Final</label><br> 
    	  <input id="dataFinal" class="formLocalizarInput" name="dataFinal" type="text" value="<%=request.getAttribute("dataFinal")%>" style="width : 100px;" onkeyup="mascara_data(this)" onblur="verifica_data(this)"/>
          <img id="calendarioDataFinal" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].dataFinal,'dd/mm/yyyy',this)">
		</div>
		
		<div class="clear"></div>
   		  <input id="formLocalizarBotao" class="formLocalizarBotao" type="button" name="Localizar" value="Consultar" onclick="javascript:buscaCertidaoJSON('<%=stTempRetorno%>', '0', <%=Configuracao.TamanhoRetornoConsulta%>, <%=boMostrarGerarPdf%>,  <%=request.getAttribute("PaginaAtual")%>, '<%=nomeBusca.length%>'); return false;"/>
        </fieldset>
        <br/>
        <div id="divTabela" class="divTabela"> 
          <table id="tabelaLocalizar" class="Tabela">
            <thead>
              <tr>
                <th width='8%' align="center"></th>
                <th width='8%' align="center"><%=request.getAttribute("tempDescricaoId")%></th>                
                <%for (int i=0;i<descricao.length;i++) {%>				
				<th width=tamColuna><%=descricao[i]%></th><%}%> 
                <th class="colunaMinima">
                	<input type='checkbox'  title="Seleciona todos os registros" name="Selecao" id="Selecao" style="display:none" onclick="javascript:selecionar_tudo();">
                </th>
                
              </tr>
            </thead>
          	<tbody id="CorpoTabela">&nbsp;</tbody>
          </table>
              <div id="divBotoesCentralizados" class="divBotoesCentralizados" style="display:none">		       	
				<button id="formLocalizarBotao" class="formLocalizarBotao" type="button" name="Consultar" value="Consultar"
					onclick="gera_pdf();" 
					title="Gerar arquivo Pdf">
					<img src="imagens/22x22/btn_pdf.png" alt="Gerar arquivo Pdf" />
					Gerar arquivo Pdf 
				</button>
				<br />
				<FONT COLOR="#AA0000" FONT SIZE="1"><b>SOMENTE CERTIDÕES FINALIZADAS</b></FONT>
		  	</div>
          </div> 
      </form> 
    </div> 
    <div id="Paginacao" class="Paginacao"></div></div> 
    <%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>