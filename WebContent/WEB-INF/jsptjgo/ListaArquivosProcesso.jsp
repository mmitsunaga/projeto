<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>

<%@page import="br.gov.go.tj.projudi.dt.ArquivoDt"%>
<%@page import="java.util.*" %>
<%@page import="br.gov.go.tj.projudi.ne.MovimentacaoArquivoNe"%>
<%@page import="br.gov.go.tj.projudi.ne.ArquivoNe"%>
<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Geração de PDF completo do processo</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	
	<style type="text/css">
		.Erro {
			margin: 30px;
			font-weight: bold;
			color: #FF0000;
			border:solid #993300 dotted;
		}
		.OK{
			margin: 30px;
			font-weight: bold;
			color: #000000;
		}
			
		.style1 {
			color: #547CBA;
			font-weight: bold;		
			margin-top: 15px;
			margin-bottom: 25px;
			font-size: 16px;
			text-align: center;
		}
		.style2 {		
			font-size: 14px;
			margin-top: 15px;
			margin-bottom: 25px;
			text-align: center;
			font-weight: bold;		
		}
		.style3 {
			color: #547CBA;
			font-weight: bold;			
			font-size: 10px;
			text-align: center;
		}
		
		#ListaCheckBoxFisico ul li {
			list-style:none;
		}
	
	</style>
	
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
		
	<style type="text/css">
		@import url('./css/Cabecalho.css');	
		@import url('./css/jquery.tabs.css');
	</style>
	   
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type='text/javascript' src='./js/Funcoes.js?v=20082020'></script>
	
	<script language="javascript" type="text/javascript">

		var interval = 0;
		var MAX_SIZE = 563200;

		var dialogo = null;
		
		function podeFecharDialogo(){				
			var url = $('#formListaArquivos').attr('action') + '?PaginaAtual=<%=String.valueOf(Configuracao.Imprimir)%>&operacao=Status';
			$.getJSON(url, function(data){
				if (data.flag > 0){
					dialogo.dialog('close');
		        	$("#divGerarPdf").show();
		        	clearInterval(interval);
				}
			});
		}
	
		function TestarTamanho(){
			var qtde = 0;
			qtde = $("input[type=checkbox][name='chk2']:checked").length;			
			if(somar()==MAX_SIZE){
				alert("Selecione pelo menos um dos arquivos.");
				return false;		
			}else if(somar()>MAX_SIZE && qtde > 1){
				alert("O Tamanho máximo de kbytes (" + MAX_SIZE + " KB) foi Ultrapassado, retire alguns arquivos.");
				return false;
			}else {				 
				$('#dialog').html('Aguarde! O PDF está sendo gerado...');				
				dialogo.dialog('open');
				interval = setInterval(podeFecharDialogo, 3000);
			}
		}
	
		function somar(){
			var tempSoma=0;
			var valor1;
			var qtde = 0;
			$("input[type=checkbox]:checked").each(function (i,value){
			    var jtag = $(this);
				valor1 =  jtag.attr('tamanho_arquivo');
				if (valor1!=null) tempSoma= parseFloat(tempSoma) + parseFloat(valor1);				
			});
			
			$(".OK, .Erro").html(tempSoma + " kb Selecionados, valor máximo " + MAX_SIZE + " KB");

			qtde = $("input[type=checkbox][name='chk2']:checked").length;
						
			if (tempSoma > MAX_SIZE && qtde > 1) {
				$("#divGerarPdf").hide();
				$(".OK").addClass("Erro");
				$(".OK").removeClass("OK");
			} else {
				$("#divGerarPdf").show();
				$(".Erro").addClass("OK");
				$(".Erro").removeClass("Erro");
			}
					
			return tempSoma;
		}

		function desmarcarTodos(classSelector){
			$("." + classSelector).prop("checked", false);			
			$("." + classSelector).siblings('ul').find("input[type='checkbox']").prop('checked', false);
		}		
	
		$(document).ready(function(){

			var volumes = $("#Volumes");
			var qtdvolumes = volumes.attr('qtdVolumes');
			var addVolumes="";
			
			for (i=1;i<=qtdvolumes;i++){
				addVolumes += "<input class='SelecionarRadio' type='radio' name='myradio' onclick='Mostrar(\"divGerarPdf\");' value='" + i + "'>Volume " + i;			
			}
			volumes.html(addVolumes);
 
			$( "#abas" ).tabs({ active: 0 });

			dialogo = $('#dialog').dialog({autoOpen:false});

			$('.SelecionarRadio').click(function(event){
			    var jtag = $(this);
				var valor = jtag.attr("value");
				$("input[type=checkbox]").prop("checked",false);
				$(".Volume"+valor).prop("checked",true);
				var primeiro = $(".Volume"+valor)[0];
				$("#" + $(primeiro).attr("pai")).prop("checked",true);									
				somar();
			});
										
			$('input:checkbox').click(function(){
				var pai = $(this).attr('pai');
				$("#" + pai).prop("checked", true);
				$(this).siblings('ul').find("input[type='checkbox']").prop('checked', this.checked);
				somar(); 	
			});
														
		});
	
	</script>
		
</head>
<body>
	
	<div class="pgn_cabecalho">        
        <div class="img_logotj" title="Logo">
            &nbsp;
       	</div>        
        <div class="img_titulo" title="Tribunal de Justi&ccedil;a do Estado de Goi&aacute;s">
            &nbsp;
        </div>
    </div>
	
	<div  id="divCorpo" class="divCorpo">
	
		<div class="style1">Geração de processo em pdf com tamanho Máximo de 563.200 kbytes (550MB) - Migração SEEU CNJ</div>			  
  		
  		<div class="style2">Selecione os arquivos que estarão no pdf do processo.</div>
  		
  		<div id="Mensagem"  align="center" class="OK">0 kb Selecionados, valor máximo 563.200 KB  </div>
  		
  		<div id="divEditar" class="divEditar">
  		
			<form  class='formListaArquivos' id="formListaArquivos" name="formListaArquivos" method="POST" action="ProcessoMigracaoPDF" >
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				
				<div id="abas">
					<ul>
						<li><a href="#ListaCheckBox" id="tabArquivos"><span>Arquivos</span></a></li>
					</ul>
					<div id='ListaCheckBox' onclick="habilitaTelaENTERComponente('ListaCheckBox', 'operacao');">														
						<% List listaMovimentacoes = processoDt.getListaMovimentacoes();						 
						  if (listaMovimentacoes != null){
							Iterator it = listaMovimentacoes.iterator(); %>
				        	<ul id="a0">
				        		<li><input type="checkbox" selecao='chk0' tamanho_arquivo='0' name="chk0" id="todos" class="chk0" value='0' /><strong>Todos os Arquivos</strong><ul>
					        <%	MovimentacaoArquivoNe movimentacaoArquivo = new MovimentacaoArquivoNe(); 
					        	Map mapMovimentacoesArquivos =(Map)request.getAttribute("mapMovimentacoesArquivos");
					        	int inVolume = 1;
					        	long loTamanhoTotal =0;
					        	long loNivel = 0;
					        	long loMovimentacao = 0;
					        	while(it.hasNext()){
					        		loMovimentacao++;
					        		MovimentacaoDt movimentacao = (MovimentacaoDt) it.next();					        		
						        		List listMovimentacaoArquivo =(List) mapMovimentacoesArquivos.get(movimentacao.getId()); %>
						        		<li><%=loMovimentacao%><input type="checkbox" id='<%=movimentacao.getId()%>' class="chk1 chk0 Volume<%=inVolume%> " selecao='nivel<%=loNivel%>' tamanho_arquivo='0' name="chk1"   value='<%=movimentacao.getId()%>'/><strong><%=movimentacao.getMovimentacaoTipo()%></strong><ul id="a<%=movimentacao.getId()%>">
						        		<%if (listMovimentacaoArquivo != null){
						        			Iterator it2 =  listMovimentacaoArquivo.iterator();
								        	 	while(it2.hasNext()) {
								        		   MovimentacaoArquivoDt arq = (MovimentacaoArquivoDt) it2.next();
								        		   if (arq.temAcessoUsuario()){
								        		   		int inTamanhoArquivo = Funcoes.StringToInt(arq.getArquivoDt().getArquivo()) /1024; 
								        		   		loTamanhoTotal+=inTamanhoArquivo;
								        		   		if (loTamanhoTotal>563200){
								        			   		loTamanhoTotal=inTamanhoArquivo;
								        			   		inVolume++;
								        		   		}
								        				 %>
									        			<li><input type='checkbox' class='chk2 chk0 Volume<%=inVolume%> nivel<%=loNivel%>' pai='<%=movimentacao.getId()%>' tamanho_arquivo='<%=inTamanhoArquivo%>' name='chk2'  value="<%=arq.getArquivoDt().getId()%>"/>
									        				<strong><%=arq.getArquivoDt().getNomeArquivoFormatado()%></strong>
									        				<span class="style3">
									        					<strong>  (Tamanho arquivo: <%=inTamanhoArquivo%>  kbytes)</strong>
									        				</span>							        				
									        			</li>
								        		   <%}
								        	 	}
							       		} %>							       	
							       	</ul></li>
					        <%	loNivel++;
					        	}%>
					        	</ul></li> </ul>
					        	<div id="Volumes" qtdVolumes="<%=inVolume%>" class="Volume" align="center"> </div>
				        <%} %>				        
					</div>
				</div>
										
				<div id='divGerarPdf' align="center" style="margin:20px;">					
					<button type="submit" id="operacao" name="operacao" value="GerarPDF" onclick="Ocultar('divGerarPdf');AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>'); TestarTamanho(); " >
						Gerar
		   			</button>					
				</div>
				
			</form>
		</div>
		
		<%@ include file="Padroes/Mensagens.jspf" %>
		
	</div>
	
</body>
</html>