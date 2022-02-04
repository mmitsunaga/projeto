<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="java.util.Iterator"%>
<html>
<head>
  <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
  <title> |<%=request.getAttribute("tempPrograma")%>| Busca de <%=request.getAttribute("tempBuscaPrograma")%>  </title>
  <link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
  <link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
  <script type='text/javascript' src='./js/jquery.js'></script>
  <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
  <script type='text/javascript' src='./js/Funcoes.js'></script>	
  <script type='text/javascript'>	
   
    var _PaginaEditar = <%=request.getAttribute("tempPaginaAtualJSON")%>;
    var _PaginaExcluir = <%=Configuracao.Excluir%>;
               
  </script>
  
  	<script language="javascript" type="text/javascript">
		function buscaDadosJSON(url, posicaoPaginaAtual, tamanho, boMostrarExcluir, paginaAtual, qtdeNomeBusca){
			//buscaDadosJSON
			
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
			var boFecharDialog = false;
			$.ajax({
				url: encodeURI(url+'?Passo=1&PaginaAtual='+ paginaAtual + nomeBusca + '&PosicaoPaginaAtual=' + posicaoPaginaAtual + '&tempFluxo1=' + tempFluxo1 + '&tempFluxo2=' + tempFluxo2),
				context: document.body,
				timeout: 300000, async: true,
				success: function(retorno){
					var inLinha=1;			
					var totalPaginas =0;
					$.each(retorno, function(i,item){
						if(item.id=="-50000"){						
							//Quantidade páginas
							totalPaginas = item.desc1;
							
						}else if (item.id=="-60000"){
							//posição atual
							posicaoPaginaAtual = item.desc1;
						}else {
							tabela.append('<tr class="TabelaLinha' + inLinha + '">');
							tabela.append('<td class="Centralizado" >' + (i-1) + '</td>');
							tabela.append('<td class="Centralizado" >' + item.id + '</td>');
							tabela.append('<td class="Centralizado" onclick="selecionaSubmeteJSON(' + item.id + ',\'' + item.desc1 + '\')">' + item.desc1 + '</td>');
							var iColuna=2;
		         			while((descricao=eval('item.desc' + iColuna))!=null) {
		          				tabela.append('<td class="Centralizado" onclick="selecionaSubmeteJSON(' + item.id + ',\'' + item.desc1 + '\')">' + descricao + '</td>');
		                     	iColuna++;
		                	}        			
							tabela.append('<td class="Centralizado" ><input name="formLocalizarimgEditar" type="image"  src="./imagens/imgEditar.png" onclick="AlterarEditarJSON(' + item.id + ',\'' + item.desc1 + '\')" />   </td>');
							if(boMostrarExcluir)		
								tabela.append('<td class="Centralizado"><input name="formLocalizarimgexcluir" type="image" src="./imagens/imgExcluir.png" onclick="AlterarExcluirJSON(' + item.id + ',\'' + item.desc1 + '\')" /></td>'); 
							tabela.append('</tr>');
			
							if (inLinha=1) inLinha=2; else inLinha=1;
						}								  	
					});
					
					//crio a paginação
					CriarPaginacaoJSON(url, posicaoPaginaAtual,totalPaginas, tamanho, boMostrarExcluir, paginaAtual, qtdeNomeBusca); 
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
  <%
  boolean boMostrarExcluir = false;
  String stTempRetorno = (String)request.getAttribute("tempRetorno");
  String stTempBuscaPrograma = (String)request.getAttribute("tempBuscaPrograma");
  //vejo se é para mostrar o excluir, caso que esteja fazendo a busca seja o proprio formulário principal
  if(stTempRetorno!=null && stTempBuscaPrograma!=null){
    if (stTempRetorno.equalsIgnoreCase(stTempBuscaPrograma)) boMostrarExcluir=true;
  }
  List descricao = (List)request.getAttribute("descricao");
  List nomeBusca = (List)request.getAttribute("nomeBusca");
  String tamColuna = "";
  if( descricao != null && descricao.size() > 0 ) {
  	tamColuna = String.valueOf(60/descricao.size()) + "%";
  }
  %>
</head>
<body>
  <div id="divCorpo" class="divCorpo" >
  <div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de <%=request.getAttribute("tempBuscaPrograma")%></h2></div>
    <div id="divLocalizar" class="divLocalizar"> 
      <form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
        <input type="hidden" id="tempBuscaId" name="<%=request.getAttribute("tempBuscaId").toString()%>"/>
        <input type="hidden" id="tempBuscaDescricao" name="<%=request.getAttribute("tempBuscaDescricao").toString()%>"/>
        <input type="hidden" id="PaginaAtual" name="PaginaAtual"  value="<%=request.getAttribute("PaginaAtual")%>"/>				
		<input type="hidden" id="tempFluxo1" name="tempFluxo1"  value="<%=request.getAttribute("tempFluxo1")%>"/>
		<input type="hidden" id="tempFluxo2" name="tempFluxo2"  value="<%=request.getAttribute("tempFluxo2")%>"/>
        <fieldset id="formLocalizar" class="formLocalizar"> 
          <%Iterator iterator = nomeBusca.listIterator();%>
          <legend id="formLocalizarLegenda" class="formLocalizarLegenda">Consulta de <%=request.getAttribute("tempBuscaPrograma")%></legend>
          <label id="formLocalizarLabel1" class="formLocalizarLabel"><%=iterator.next()%></label><br> 
          <input id="nomeBusca1" class="formLocalizarInput" name="nomeBusca1" type="text" value="" style="width : 100px;"/>
          <input id="formLocalizarBotao" class="formLocalizarBotao" type="button" name="Localizar" value="Consultar" onclick="javascript:buscaDadosJSON('<%=stTempRetorno%>', '0', <%=Configuracao.TamanhoRetornoConsulta%>, <%=boMostrarExcluir%>,  <%=request.getAttribute("PaginaAtual")%>, '<%=nomeBusca.size()%>'); return false;"/>
          <br/>
          <%int i = 2;
          while(iterator.hasNext()){%>
          <label id="formLocalizarLabel<%=i%>" class="formLocalizarLabel"><%=iterator.next()%></label><br> 
          <input id="nomeBusca<%=i%>" class="formLocalizarInput" name="nomeBusca<%=i%>" type="text" value="" style="width : 700px;"/>
          <br/>
		  <%i++;}%> 
<!--           <div class="Centralizado"> -->
<%--           <input id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar" onclick="javascript:buscaDadosJSON('<%=stTempRetorno%>', '0', <%=Configuracao.TamanhoRetornoConsulta%>, <%=boMostrarExcluir%>,  <%=request.getAttribute("PaginaAtual")%>, '<%=nomeBusca.size()%>'); return false;"/></div> --%>
        </fieldset>
        <br/>
        <div id="divTabela" class="divTabela"> 
          <table id="tabelaLocalizar" class="Tabela">
            <thead>
              <tr>
                <th width='8%' align="center"></th>
                <th width='8%' align="center"><%=request.getAttribute("tempDescricaoId")%></th>
                <%Iterator iterator2 = descricao.listIterator();
				while(iterator2.hasNext()) {%>
				<th width=tamColuna><%=iterator2.next()%></th><%}%> 
                <th class="colunaMinima" title="Seleciona o registro para edição">Selecionar</th>
                <%if (boMostrarExcluir) {%>
                <th class="colunaMinima" title="Seleciona o registro para exclusão">Excluir</th><%}%> 
              </tr>
            </thead>
          	<tbody id="CorpoTabela">&nbsp;</tbody>
          </table>
        </div> 
      </form> 
    </div> 
    <div id="Paginacao" class="Paginacao"></div></div> 
    <%@ include file="Padroes/Mensagens.jspf" %>
    
</body>
</html>