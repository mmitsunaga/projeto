<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.projudi.dt.ProcessoObjetoArquivoDt"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PermissaoEspecialDt"%>
<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoCriminalDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="org.apache.commons.lang.StringUtils" %>

<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>

<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AreaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaTipoDt"%>

<% 
	boolean boMotrarAnotacao = UsuarioSessao.getVerificaPermissao(ProcessoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Imprimir); 
	boolean boMotrarObjetos = UsuarioSessao.getVerificaPermissao(ProcessoObjetoArquivoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.LocalizarDWR);
%>

<html>
	<head>
		<title>Dados Processo</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/menusimples.css');
			@import url('./css/menuSimplesImagens.css');
			@import url('./css/jquery.tabs.css');
			@import url('./css/rastreamentoCorreios.css');
		</style>
		      	
		<link type="text/css" rel="stylesheet" href="css/jquery.postitall.css?v=11082020">
		<link type="text/css" rel="stylesheet" href="js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" media="screen"></link>
		
      	<%@ include file="js/buscarArquivos.js"%>
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js?v=11082020'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js?v=11082020'></script>
		<script type="text/javascript" src="./js/acessibilidadeMenu.js?v=11082020"></script>
		<script type="text/javascript" src="./js/filtro_movimentacoes.js?v=11082020"></script>
		<script type='text/javascript' src='./js/jquery.postitall.js?v=11082020'></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js?v=11082020'></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>	
		<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
		<script type="text/javascript" src="./js/rastreamentoCorreios.js" charset="utf-8"></script>
		
		<script type="text/javascript" src="./js/FormProcessoObjetos.js"></script>
					
		<script type="text/javascript">
			//variavel para limitar a busca pelos dados da navegação
			//se for diferente de null, mosta a navegação
			//caso contrario faz a busca			

			var Anotacoes;
			
			$(document).ready(function() {	
				
				//pego todos objetos que foram marcados com a class nomes
				//e verifico se tem número no nome
				$(".nomes").each(function( index ) {
				 	var texto =  $( this ).text();
					for(var numero=0; numero<=9; numero++){
						texto= texto.replace(numero,'<p class="destacarNumero" tag="Foi utilizado número no Nome, favor conferir com os dados da petição" title="Foi utilizado número no Nome, favor conferir com os dados da petição">'+ numero +'</p>');
					}

					$( this ).html(texto);
				});	
											    	
				$(".filtro_coluna_movimentacao").each(function( index ) {
					const regex1 = /BH\d{9}BR/;
					const regex2 = /idPendenciaCorreios(.*?)idPendenciaCorreios/;
					var texto =  $(this).html();
					var match1 = regex1.exec(texto);
					if (match1!=null){
					  	texto= texto.replace(match1[0],'<span style="display:inline-block"><a href="RastreamentoCorreios?PaginaAtual=9&codigo='+match1[0]+'" onclick="verInfoRC(event);" style="display:inline">'+match1[0] +'</a>');
					  	var match2 = regex2.exec(texto);
					  	if(match2!=null) {
						  	texto = texto.replace(match2[0],'<a target="_blank" href="BuscaProcesso?PaginaAtual=6&Id_MovimentacaoArquivo='+match2[1]+'&eCarta=true" style="display:inline-block"><img id="idVisualizarECarta1" src="imagens/16x16/btn_endereco.png" alt="e-Carta" title="e-Carta"/></a>');
						}
						texto += '</span>';
						$(this).html(texto);
					}
				});
				
				if ($('#span_prioridade').html().includes('Réu Preso')){
					$('#span_prioridade').addClass('destacar_01');
					$('#span_proc_numero').addClass('destacar_01');
				}
				
				$( "#abas" ).tabs({ active: 0 });
				
				$( "#abas" ).tabs({
					beforeActivate: function( event, ui ) { 
						tab = ui.index;
							if(tab==0) {
								calcularTamanhoIframe();
							}else{		
								var dadosIndice =  $('#dadosIndice');	
								var id1 = dadosIndice.attr("buscaDados");
								
								if (id1=='sim') gerarIndice();
							}
						}
					});
				
				<% if(boMotrarAnotacao) {%>
					//crio as notas dos processo
					buscaNotasProcesso();
					 $.PostItAll.hide();
				<%}%>
				
			});
			
			function mudarStatusMovimentacao(id_Movimentacao, descricao, novoStatus, paginaAtual) {
				if(confirm('Confirma a mudança do status da movimentacao '+ descricao +' para '+ novoStatus +'  ?')) {
					AlterarValue('PaginaAtual', paginaAtual);
					AlterarValue('Id_Movimentacao', id_Movimentacao);
					AlterarValue("TipoBloqueio", novoStatus);
					AlterarAction('Formulario','Movimentacao');
					document.Formulario.submit();
				}
			}
			
			function invalidarArquivo(id_MovimentacaoArquivo, descricao, tipo) {
				if(confirm("Confirma a mudança do status do arquivo "+ descricao + " ?")) {
					AlterarValue('PaginaAtual', '<%=Configuracao.Curinga7%>');
					AlterarValue('Id_MovimentacaoArquivo', id_MovimentacaoArquivo);
					AlterarValue('TipoBloqueio', tipo);
					AlterarAction('Formulario','MovimentacaoArquivo');
					document.Formulario.submit();
				}
			}
			
			function validarArquivo(id_MovimentacaoArquivo, descricao) {
				if(confirm("Confirma a Validação do arquivo "+ descricao + " ?")) {
					AlterarValue('PaginaAtual', '<%=Configuracao.Curinga6%>');
					AlterarValue('Id_MovimentacaoArquivo', id_MovimentacaoArquivo);
					AlterarAction('Formulario','MovimentacaoArquivo');
					document.Formulario.submit();
				}
			}
			
			function gerarPendencias(id_Movimentacao) {
				AlterarValue('PaginaAtual', '<%=Configuracao.Novo%>');
				AlterarValue('Id_Movimentacao', id_Movimentacao);
				AlterarAction('Formulario','PendenciaMovimentacao');
				document.Formulario.submit();
			}

			function manterEventos(descricaoMovimentacao, id_Movimentacao, paginaAtual) {
				AlterarValue('PaginaAtual', paginaAtual);
				AlterarValue('MovimentacaoDataRealizacaoTipo', descricaoMovimentacao);
				AlterarValue('Id_Movimentacao', id_Movimentacao);
				AlterarAction('Formulario','ProcessoEventoExecucao');
				document.Formulario.submit();
			}
			
			function MostrarOpcoes(){
				if ($('#chkVer').is(':checked')){
					
					//$('#chkVer').val('true');					
					$('.dropMovimentacao').each(function( ) {		
						var jtag = $(this);
						jtag.css("display","block");
						var id_movi = jtag.attr('id_movi');
						var movi_tipo = jtag.attr('movi_tipo');
						var codigoTemp = jtag.attr('codigo_temp');
						
						var resposta = MensagemImagemBloqueio(codigoTemp );
						
						var img_bloqueio = resposta[0];
						var txt_bloqueio = resposta[1];
						
						var divsBloquear =" <div class='dropdown coluna5'>";
						divsBloquear += "  		<button onclick='MostrarOcultar(\"myDropdown1"+id_movi+"\"); return false;' class='dropbtn'> <img id='imgValidar' src='" + img_bloqueio + "' alt='"+ txt_bloqueio + "' title='"+ txt_bloqueio + "'  /> </button>";
						divsBloquear += "  		<div id='myDropdown1"+id_movi+"' class='dropdown-content'>";						
						divsBloquear += "  			<a class='ImagemGlobal'    alt='Visualização Bloqueada para todos os Usuários' title='Visualização Bloqueada para todos os Usuários' href='#' onclick=\"javascript: mudarStatusMovimentacao("+id_movi+",'"+movi_tipo+"','Global',7)\"> Bloquear</a>";
						divsBloquear += "  			<a class='ImagemAdv' 	   alt='Visível aos Advs, Delegacia, MP, Cartório e Magistrado' title='Visível aos Advs, Delegacia, MP, Cartório e Magistrado' href='#' onclick=\"javascript: mudarStatusMovimentacao("+id_movi+",'"+movi_tipo+"','Adv',7)\"> Adv</a>";
						divsBloquear += "  			<a class='ImagemDelegacia' alt='Visível à Delegacia, MP, Cartório e Magistrado' title='Visível à Delegacia, ao MP, Cartório e Magistrado' href='#' onclick=\"javascript: mudarStatusMovimentacao("+id_movi+",'"+movi_tipo+"','Delegacia',7)\"> Delegacia</a>";
						divsBloquear += "  			<a class='ImagemMP'        alt='Visível ao MP, Cartório e Magistrado' title='Visível ao Ministério Público, Cartório e Magistrado' href='#' onclick=\"javascript: mudarStatusMovimentacao("+id_movi+",'"+movi_tipo+"','Mp',7)\">   MP</a>";
						divsBloquear += "  			<a class='ImagemCartorio'  alt='Visível ao Cartório e Magistrado' title='Visível ao Cartório e Magistrado' href='#' onclick=\"javascript: mudarStatusMovimentacao("+id_movi+",'"+movi_tipo+"','Cartorio',7)\">   Cartório</a>";
						divsBloquear += "  			<a class='ImagemJuiz'      alt='Visível somente ao Magistrado' title='Visível somente ao Magistrado' href='#' onclick=\"javascript: mudarStatusMovimentacao("+id_movi+",'"+ movi_tipo+"','Juiz',7)\">   Magistrado</a>";
						divsBloquear += "  			<a class='imgValidar'      alt='Visível Normal (Partes, Advogados, MP, Cartório e Magistrado)' title='Visível Normal (Partes, Advogados, MP, Cartório e Magistrado)' href='#' onclick=\"javascript: mudarStatusMovimentacao('" + id_movi + "','" + movi_tipo+ "','" +'VÁLIDA'+  "','" +'6'+ "')\">      Normal </a>";
						divsBloquear += "  </div>";
						divsBloquear += "</div>";
						
						jtag.html( divsBloquear);
	                   	
					});
					$('.dropArquivos').each(function( ) {		
						var jtag = $(this);
						jtag.css("display","block");
						var id_arquivo = jtag.attr('id_arquivo');
						var nome_arquivo = jtag.attr('nome_arquivo');
						var bloqueavel = jtag.attr('bloqueavel');
						var img_bloqueio = jtag.attr('img_bloqueio');
						var txt_bloqueio = jtag.attr('txt_bloqueio');
											
						if (bloqueavel){
							var divsBloquear = "  	<button onclick='MostrarOcultar(\"myDropdown"+id_arquivo+"\"); return false;' class='dropbtn'> <img id='imgValidar' src='" + img_bloqueio + "' alt='"+ txt_bloqueio + "' title='"+ txt_bloqueio + "'  /> </button>";
							divsBloquear += "  		<div id='myDropdown"+id_arquivo+"' class='dropdown-content'>";
							divsBloquear += "  			<a class='ImagemPublico'  alt='Visível a todos os Usuários' title='Visível a todos os Usuários' href='#' onclick=\"javascript: invalidarArquivo('" + id_arquivo + "','" + nome_arquivo+ "','Publico')\">   Público</a>";
							divsBloquear += "  			<a class='ImagemGlobal'   alt='Visualização Bloqueada para todos os Usuários' title='Visualização Bloqueada para todos os Usuários' href='#' onclick=\"javascript: invalidarArquivo('" + id_arquivo + "','" + nome_arquivo+ "','Global')\">   Bloquear</a>";
							divsBloquear += "  			<a class='ImagemAdv'      alt='Visível aos Advs, Delegacia, MP, Cartório e Magistrado' title='Visível aos Advs, Delegacia, MP, Cartório e Magistrado' href='#' onclick=\"javascript: invalidarArquivo('" + id_arquivo + "','" + nome_arquivo+ "','Adv')\"> Advs </a>";
							divsBloquear += "  			<a class='ImagemDelegacia' alt='Visível à Delegacia, ao MP, Cartório e Magistrado' title='Visível à Delegacia, ao Ministério Público, Cartório e Magistrado' href='#' onclick=\"javascript: invalidarArquivo('" + id_arquivo + "','" + nome_arquivo+ "','Delegacia')\"> Delegacia </a>";
							divsBloquear += "  			<a class='ImagemMP'       alt='Visível ao MP, Cartório e Magistrado' title='Visível ao Ministério Público, Cartório e Magistrado' href='#' onclick=\"javascript: invalidarArquivo('" + id_arquivo + "','" + nome_arquivo+ "','Mp')\"> MP </a>";
							divsBloquear += "  			<a class='ImagemCartorio' alt='Visível ao Cartório e Magistrado' title='Visível ao Cartório e Magistrado' href='#' onclick=\"javascript: invalidarArquivo('" + id_arquivo + "','" + nome_arquivo+ "','Cartorio')\"> Cartório </a>";
							divsBloquear += "  			<a class='ImagemJuiz'     alt='Visível somente ao Magistrado' title='Visível somente ao Magistrado' href='#' onclick=\"javascript: invalidarArquivo('" + id_arquivo + "','" + nome_arquivo+ "','Juiz')\">      Magistrado </a>";
							divsBloquear += "  			<a class='imgValidar'     alt='Visível Normal (Partes, Advogados, MP, Cartório e Magistrado)' title='Visível Normal (Partes, Advogados, MP, Cartório e Magistrado)' href='#' onclick=\"javascript: validarArquivo('" + id_arquivo + "','" + nome_arquivo+ "')\">      Normal </a>";
							divsBloquear += "        </div>";
							jtag.html( divsBloquear);	
						}else{
							var divsDesBloquear = "<img id='imgValidar' class='imgValidar' src='imagens/22x22/ico_liberar.png' alt='Desbloquear Arquivo' title='Desbloquear Arquivo' onclick=\"javascript: validarArquivo('" + id_arquivo + "','" + nome_arquivo + "')\" />";
							jtag.html( divsDesBloquear);
						}												
					});
				} else {
					
					//retiro os dopbdown de arquivos e movimentacoes					
					$('.dropMovimentacao').each(function( ) {		
						var jtag = $(this);
						jtag.html( "");
						jtag.css("display","none");
					});						
					$('.dropArquivos').each(function( ) {								
						var jtag = $(this);
						var img_bloqueio = jtag.attr('img_bloqueio');
						var txt_bloqueio = jtag.attr('txt_bloqueio');
						jtag.html( " <img id='imgValidar' src='" + img_bloqueio + "' alt='"+ txt_bloqueio + "' title='"+ txt_bloqueio + "'  /> ");
					});					
				}
				calcularTamanhoIframe();
			}

			function mostrarTodasPartes() {
				AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');
				AlterarValue('PassoEditar', '11');								
				document.Formulario.submit();
			}			
			   			
		</script>
		
	</head>
		    			  	
	<% if (request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica")) { %>
		<body class="fundo">
			<%@ include file="/CabecalhoPublico.html" %>
	<% } else{%>
		<body>
	<%} %> 
			<% if (processoDt.isArquivado()){ %>
	 			<div id="divCorpo" class="divCorpo" style="background-image: url('imagens/img_processoarquivado.png'); background-repeat: no-repeat; background-size: contain;">
			<%} else if (processoDt.isProcessoHibrido()){%>
				<div id="divCorpo" class="divCorpo" style="background-image: url('imagens/img_processomisto.png'); background-repeat: no-repeat; background-size: 100% 150px;">	 				
			<%} else if (processoDt.isCalculoLiquidacao()){%>				
				<div id="divCorpo" class="divCorpo" style="background-image: url('imagens/img_processofisico1.png'); background-repeat: no-repeat; background-size: 100% 150px;">
			<%} else if (processoDt.is100Digital()){%>
				<div id="divCorpo" class="divCorpo" style="background-image: url('imagens/img_processo100digital.jpg'); background-repeat: no-repeat; background-size: contain;">
			<%} else {%>
				<div id="divCorpo" class="divCorpo">
			<%}%>
  			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario" style="opacity: .85;">
  				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="" />
  				<input id="Id_Movimentacao" name="Id_Movimentacao" type="hidden" value="" />
  				<input id="Id_MovimentacaoArquivo" name="Id_MovimentacaoArquivo" type="hidden" value="" />
  				<input id="TipoBloqueio" name="TipoBloqueio" type="hidden" value="" />
  				
  				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
  				<input id="MovimentacaoDataRealizacaoTipo" name="MovimentacaoDataRealizacaoTipo" type="hidden" value="" />
  				
	  			<div id="divEditar" class="divEditar">

				<fieldset id="VisualizaDados" class="VisualizaDados">		<legend style="font-size: 13px;"> Autos </legend>
					<div class="divBotoesDireita"> 
						<%if(boMotrarAnotacao) {%> 
							<img src="imagens/postit.png" name="nota" alt = "Criar Anotação para o Processo (Particular)" title="Criar Anotação para o Processo (Particular)" class="notaProcesso" onclick='criarNota(null,null,null,1);'> 
		  					<%if(UsuarioSessao.isUsuarioInterno()){%> 
		  						<img src="imagens/postit_cartorio.png" name="nota" alt = "Criar Anotação para o Cartório" title="Criar Anotação para o Cartório" class="notaProcesso" onclick='criarNota(null,null,true,0);'>
		  					<%}
 						}%> 
 						<% 	//pode ver os objetos
 						if(boMotrarObjetos){%>
 							<img src="imagens/imgObjetos.png" width="40px" height="40px" onClick="mostrarObjetos('<%=processoDt.getId()%>')" title="Mostrar/Ocultar Objetos do Processo" alt="Mostrar/Ocultar Objetos do Processo" />
 						<%}%>
					</div>
					<div class='aEsquerda'>
			  			<div> N&uacute;mero </div> <span id='span_proc_numero' class="bold"> <%=processoDt.getProcessoNumeroCompleto()%></span></br>
						<div> Área</div> <span class="bold"> <%=processoDt.getArea()%></span></br>
	  				</div>		  				  			
						<div id="sub_objetos" class="DivInvisivel">
							<!-- Aqui será inserido o formulario/tabela -->
						</div>	
	  				<br />
					<blockquote id="menu" class="menuEspecial"> <%=(request.getAttribute("MenuEspecial" + PermissaoEspecialDt.OpcoesProcessoCodigo) != null?request.getAttribute("MenuEspecial" + PermissaoEspecialDt.OpcoesProcessoCodigo):"")%> </blockquote>
					<%if (processoDt.isProcessoHibrido()){%>
						<blockquote id="menu2" class="menuEspecial"> <%=(request.getAttribute("MenuEspecial" + PermissaoEspecialDt.OpcoesProcessoHibrido) != null?request.getAttribute("MenuEspecial" + PermissaoEspecialDt.OpcoesProcessoHibrido):"")%> </blockquote>
					<%}	%>
					<%if (processoDt.isProcessoJaFoiHibrido()){%>
						<blockquote id="menu2" class="menuEspecial"> <%=(request.getAttribute("MenuEspecial" + PermissaoEspecialDt.OpcoesProcessoExHibrido) != null?request.getAttribute("MenuEspecial" + PermissaoEspecialDt.OpcoesProcessoExHibrido):"")%> </blockquote>
					<%}	%>
					<%if (processoDt.isProcessoPrecatoriaExpedidaOnline()){%>
						<blockquote id="menu3" class="menuEspecial"> <%=(request.getAttribute("MenuEspecial" + PermissaoEspecialDt.OPCOES_PRECATORIA) != null?request.getAttribute("MenuEspecial" + PermissaoEspecialDt.OPCOES_PRECATORIA):"")%> </blockquote>
					<%}	%>
					<blockquote id="menu_outras" class="menuEspecial"> <%=(request.getAttribute("MenuEspecial" + PermissaoEspecialDt.OPCOES_OUTRAS) != null?request.getAttribute("MenuEspecial" + PermissaoEspecialDt.OPCOES_OUTRAS):"")%> </blockquote>
					<br />
		  			<%
					if (processoDt.temRecurso()) {%>		  			
		  				<fieldset id="VisualizaDados" class="VisualizaDados field_recurso" >
		  					<legend> Dados Recurso </legend>
		  					<%@ include file="DadosRecurso.jspf"%>
		  				</fieldset>		  			
					<%}%>
						
					<fieldset id="VisualizaDados" class="VisualizaDados <%=processoDt.getClassCorProcesso()%>"><legend style="font-size: 13px;"> Dados do Processo </legend>

						<%@ include file="BuscaPartesProcesso.jspf"%>																	
						
				    	<fieldset id="VisualizaDados" class="VisualizaDados <%=processoDt.getClassCorProcesso()%>">
				    		<legend> Outras Informações</legend>
				    		
				    		<%if (processoDt.temRecurso()) {%>
				    			<div> Serventia Origem</div>
								<span class="span1"><%=processoDt.getRecursoDt().getServentiaOrigem()%></span>
				    		<%} else {%>
				    			<div> Serventia </div>
								<span class="span1"><%=processoDt.getServentia()%></span>
				    		<%}%>
				    		<%if ( processoDt.isProcessoHibrido() && processoDt.temLocalizador()){%>
					    	  	<div> Localizador </div>
					    	  	<span class="span3"><%=processoDt.getLocalizador()%> </span>
							<%} %>
							 	<br />
					   		<div> Classe </div>
				    	  	<span style="width: 750px; height: auto;"><%=processoDt.getClasseCNJ()%> </span>   					    
				    	  	<br />
				    	  	<%@ include file="Padroes\BuscaAssuntosProcesso.jspf"%>
				    	  	
				    	  	<% if (processoDt.temValor()){ %>
								<div> Valor da Causa</div>
								<span class="span1"><%=processoDt.getValor()%></span>
							<%} %>
							
							<div> Valor Condenação</div>
							<span class="span2"><%=processoDt.getValorCondenacao()%></span>
							<br />
				    	  	<div> Processo Originário </div>
							<span class="span1"><a href="BuscaProcesso?ProcessoOutraServentia=true&Id_Processo=<%=processoDt.getId_ProcessoPrincipal()%>"><%=processoDt.getProcessoNumeroPrincipal()%></a></span/><br />	
							
							<%if (processoDt.isProcessoHibrido()){%>
								<div> Principal</div>
								<span class="span1"><%=processoDt.getProcessoFisicoTipo() + ": " + processoDt.getProcessoFisicoNumero() + " - Comarca: " + processoDt.getProcessoFisicoComarcaNome() %></span>
							<%}%>
							<br />
							<div> Fase Processual</div>	
							<span class="span1"><%=processoDt.getProcessoFase()%></span>
							
							<% if (processoDt.temApensos()){%>			
								<div>Dependente/Apenso</div> <span class="span2"> <a href="ProcessoApenso?PaginaAtual=<%=Configuracao.Localizar%>">Visualizar</a> </span>
							<% } %>
							<br />
							<% if (UsuarioSessao.isPodeVisualizarClassificador()){ %>
								<div> Classificador </div>   <span class="span1"> <%= processoDt.getClassificador()%> </span>
						    <% } %>
						    <div> Dt. Distribui&ccedil;&atilde;o</div>
							<span class="span3"><%=processoDt.getDataRecebimento()%></span>
							<br />
							
							<div> Segredo de Justi&ccedil;a</div>
							<span><%=processoDt.mostrarSegredoJustica()%></span>
							
							<div style="width:200px;margin-left:40px;">Dt. Tr&acirc;nsito em Julgado</div>
							<span class="span3" title="Data Tr&acirc;nsito em Julgado"  alt="Data Tr&acirc;nsito em Julgado" ><%=processoDt.getDataTransitoJulgado()%></span>
							<br />
							
							<div> Status </div>
							
							<% if(processoDt.isSigiloso()){%>
								<span class="span1">  <font color="red"><%=processoDt.getProcessoStatus()%></font></span>
							
							<%} else{%>
							    <%if(processoDt.isCriminal()){ %>
								    <%if(processoDt.getProcessoCriminalDt() == null){ 
						    			processoDt.setProcessoCriminalDt(new ProcessoCriminalDt());
						    		} %>
						    		<%if(processoDt.isArquivado()){%>
							    		<span class="span1"><font color="red"><%=processoDt.getProcessoStatus()+ " - "+processoDt.getProcessoCriminalDt().getProcessoArquivamentoTipo()%></font></span>
							    	<%} else {%>
							    		<span class="span1"><%=processoDt.getProcessoStatus()%></span>
							    	<%} %>
							    <%} else { %>
									<span class="span1"><%=processoDt.getProcessoStatus()%></span>
								<%} %>
							<%}%>
	
							<div> Prioridade</div>
							<%if(processoDt.isPrioridade() && UsuarioSessao.getVerificaPermissao(ProcessoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Curinga8)) {%>
								<a href="Processo?PaginaAtual=<%=Configuracao.Curinga8%>" border="0">
									<img src='imagens/16x16/edit-clear.png' alt="Retirar Prioridade" title="Retirar Prioridade" border="0">
								</a>
							<%} %>
							<span id='span_prioridade' class="span3"><%=processoDt.getProcessoPrioridade()%></span>
							
							<br />
							<div> Efeito Suspensivo </div>
							<span class="span1"><%=(processoDt.temEfeitoSuspensivo() ? "Sim" : "Não")%></span>
													
							<div> Julgado 2º Grau </div>
							<%if( processoDt.getJulgado2Grau() != null ){  %>
								<span class="span2"><%=(processoDt.isJulgado2Grau()? "Sim" : "Não")%></span>
							<% } %>
							<br />
							<div> Custa </div>
							<span class="span1">
								<%=processoDt.getCustaTipo()%>
							</span>
																					
							<div>Penhora no Rosto</div>
							<span class="span2"><%=(processoDt.temPenhora() ? "Sim" : "Não")%></span>
							
							<%if( processoDt.temPrescricao()){  %>
								<br />
								<div title="Data Provável Prescrição"> Data Prescrição</div>
						    	<span class="span2"><%=processoDt.getDataPrescricao()%></span>
						    <%} %>
							
							<div style="float: right; width: 135px;">
	 							<%if (!processoDt.isCalculoLiquidacao()){%>

								<input name="inputPendenciasProcesso" type="image" src="./imagens/22x22/ico_aviso.png" 
									onclick="AlterarValue('PaginaAtual','<%=Configuracao.Imprimir%>');" title="Visualizar Pendências no Processo" />
								<input name="inputResponsaveisProcesso" type="image" src="./imagens/imgAssistente.png" 	
									onclick="AlterarValue('PaginaAtual','<%=Configuracao.LocalizarAutoPai%>');" title="Consultar Responsáveis pelo Processo" />
									<% if (UsuarioSessao.isPodeGerarPdfCompleto()){ %>
										<img src="imagens/22x22/btn_pdf.png" alt="Gerar PDF de Processo Completo" title="Gerar PDF de Processo Completo" onclick="window.open('ProcessoCompletoPDF?idProcesso=<%=processoDt.getId()%>&amp;PaginaAtual=<%=Configuracao.Imprimir%>','Processo Completo','toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=no,resizable=yes,fullscreen=yes')">
									<% } 
								}%>
								<% if (processoDt.getProcessoTipoCodigo() != null && Funcoes.StringToInt(processoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.EXECUCAO_DA_PENA
										|| Funcoes.StringToInt(processoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA){ %>
								<input name="inputEventosExecucaoPenal" type="image" src="./imagens/22x22/ico_importante.png" 	
									onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','1');" title="Visualizar Eventos da Execução Penal" />
								<% } %>
							</div>
						</fieldset>
						<% if (processoDt.temExecucaoPenal()){ %>
							<br />
					    	<fieldset id="VisualizaDados" class="VisualizaDados">
									<span><a href="<%=request.getAttribute("tempRetorno")%>?Id_Processo=<%=processoDt.getId()%>&PaginaAtual=<%=Configuracao.Editar%>&PassoEditar=6" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','6');">Visualizar Atestado de Pena a Cumprir</a></span>
							</fieldset>
						<% } %>

						<% if (processoDt.isCriminal()){ %>
							<fieldset id="VisualizaDados" class="VisualizaDados">
					    		<legend> Informações Adicionais </legend>
					    		<%if(processoDt.getProcessoCriminalDt() == null){ 
					    			processoDt.setProcessoCriminalDt(new ProcessoCriminalDt());
					    		} %>
						   		<div title="Réu Preso"> R&eacute;u Preso </div>
						   		<%if(processoDt.getProcessoCriminalDt().getReuPreso() != null){%>
									<span class="span1"><%= (processoDt.getProcessoCriminalDt().getReuPreso().equalsIgnoreCase("true") ? "Sim" : "Não") %></span>
								<%} %>
						   		<div title="Protocolo SSP/Inquérito"> Protocolo SSP</div>
					    	  	<span class="span2"><%=processoDt.getTcoNumero()%></span><br />
					    	  	
								<div title="Data da Prisão"> Data da Pris&atilde;o</div>
								<span class="span1"><%=processoDt.getProcessoCriminalDt().getDataPrisao()%></span>
								
								<div title="Inquérito"> Inquérito</div>
					    	  	<span class="span2"><%=processoDt.getProcessoCriminalDt().getInquerito()%></span><br />
								
					    	  	<div title="Data do Oferecimento da Denúncia/Queixa "> Dt.Ofer.Den./Queixa </div>
								<span class="span1"><%=processoDt.getProcessoCriminalDt().getDataOferecimentoDenuncia()%></span>
								
								<div title="Data do Recebimento da Denúncia/Queixa"> Dt.Rec.Den./Queixa</div>	
								<span class="span2"><%=processoDt.getProcessoCriminalDt().getDataRecebimentoDenuncia()%></span><br />
										    
							    <div title="Data da Transação Penal"> Data Trans. Penal</div>
								<span class="span1"><%=processoDt.getProcessoCriminalDt().getDataTransacaoPenal()%></span>
							    
								<div title="Data da Suspensão Penal"> Data Susp. Penal</div>
								<span class="span2"><%=processoDt.getProcessoCriminalDt().getDataSuspensaoPenal()%></span><br />
								
								<div title="Data do Fato"> Data do Fato</div>
								<span class="span1"><%=processoDt.getProcessoCriminalDt().getDataFato()%></span>
								
								<div title="Data de Baixa"> Data de Baixa</div>
								<span class="span2"><%=processoDt.getProcessoCriminalDt().getDataBaixa()%></span><br />
								
								<div title="Data Prescriçõa"> Data Prescrição</div>
								<span class="span1"><%=processoDt.getProcessoCriminalDt().getDataPrescricao()%></span><br />
							</fieldset>
						<%} %>
						
						</fieldset>
						
					</fieldset>	
					<!-- MOVIMENTAÇÕES -->
					<div id="abas">		
						<ul>
							<li><a href="#EventosProcesso"><span>Eventos do Processo</span></a></li>
							<li><a href="#IndiceProcesso" ><span>Índice Processo</span></a></li>
							<li><a href="#EventosProcesso" onclick="javascript:window.open('BuscaProcesso?PaginaAtual=9&PassoBusca=4', '_blank'); false;" ><span>Navegação de Arquivo</span></a></li>
							<% if (processoDt.getDataDigitalizacao() != null && processoDt.getDataDigitalizacao().trim().length() > 0){
							      if (processoDt.getListaMovimentacoesFisico() != null && processoDt.getListaMovimentacoesFisico().size() > 0) { %>
								<li><a href="#AtosFisicos" ><span>Sentenças do Processo Físico</span></a></li>
							<%} 
							} %>
						</ul>
							
							<div id="EventosProcesso">
				    			<div style="width: 100%;height: 46px; clear: both;">
						    		<div style="text-align: left; float:left;width: 84%; font-size: 10px;" id="lista-agregadores"></div>
						    		<div align="right" style="float: right; width: 16%;">
				    					<% if (UsuarioSessao.isPodeBloquear(processoDt.getId(), processoDt.getId_Serventia())){ %>
			    						<input type="checkbox" id="chkVer" value="false" title="Clique para Bloquear ou Desbloquear Movimentações e Arquivos" onclick="MostrarOpcoes()">Bloquear/Desbloquear
				    					<% } %>
						    		</div>
					    		</div>
				    			<table id="TabelaArquivos" class="Tabela" >
				        			<thead>
				            			<tr class="TituloColuna">
				                			<td>Nº</td>
				                   			<td width="45%">Movimentação</td>
				                  			<td width="10%">Data</td>
				                  			<td width="15%">Usuário</td>
				                  			<td>Arquivo(s)</td>
				                  			<td width="20%" title="Opções">Opções</td>
				               			</tr>
				               			<tr>
				               			    <td colspan="6"></td>
				               			</tr>
				           			</thead>
				    				<tbody id="tabListaProcesso">
						    		<%
						    			List listaMovimentacoes = (List)processoDt.getListaMovimentacoes();	
    					    		    boolean boLinha=false; 
    						   	    	for (int i=(listaMovimentacoes != null?listaMovimentacoes.size()-1:-1);i>=0;i--){
    					   			  		MovimentacaoDt movimentacaoDt = (MovimentacaoDt)listaMovimentacoes.get(i);
    										if(!movimentacaoDt.isValida()){%>
											<tr class='bloqueado filtro-entrada' title='Movimentação Bloqueada'>
												<td class="colunaMinima"> <%=i+1%></td>
												<td class="filtro_coluna_movimentacao" width="55%"><span class="filtro_tipo_movimentacao">Movimentação Bloqueada</span></td>
												<td width="100" align="center"><%= movimentacaoDt.getDataRealizacao()%></td>
						                   		<td width="25%">Não disponível</td>
						                   		<td></td>
						                   		<td><div class="btAcoes"><div class="dropMovimentacao" id_movi = '<%=movimentacaoDt.getId()%>' movi_tipo ='<%=movimentacaoDt.getMovimentacaoTipoLimpa()%>' codigo_temp ='<%=movimentacaoDt.getCodigoTemp()%>' ></div></div></td>
			                   			<%}else{ %>
				                   			<tr class='filtro-entrada TabelaLinha<%=boLinha?1:2%>' >
					                   			<td class="colunaMinima"> <%=i+1%></td>
					                    		<td class="filtro_coluna_movimentacao" width="55%">
					                    			<span class="filtro_tipo_movimentacao"><%=movimentacaoDt.getMovimentacaoTipo()%></span><br /><%=movimentacaoDt.getComplemento()%>
					                    		</td>
						                   		<td width="100" align="center"><%= movimentacaoDt.getDataRealizacao()%></td>
						                   		
						                   		<% if(!UsuarioSessao.isUsuarioInterno() && 
						                   		(movimentacaoDt.getMovimentacaoTipoCodigo().equalsIgnoreCase(String.valueOf(MovimentacaoTipoDt.MUDANCA_ASSUNTO_PROCESSO)) ||
					                   			 movimentacaoDt.getMovimentacaoTipoCodigo().equalsIgnoreCase(String.valueOf(MovimentacaoTipoDt.MUDANCA_CLASSE_PROCESSO)))) {%> 
					                   			 <td width="25%">-</td>
					                   			<%} else { %>
						                   		<td width="25%"> 	<%= movimentacaoDt.getNomeUsuarioRealizador()%>	</td>
						                   		<%} %>
						                   		
												<td class="colunaMinima">
													<%if ( movimentacaoDt.temArquivos()){%>
														<a href="javascript:buscarArquivosMovimentacaoJSON('<%=movimentacaoDt.getId()%>', '<%=request.getAttribute("tempRetorno")%>', 'Id_MovimentacaoArquivo', <%=Configuracao.Curinga6 %>, '<%=UsuarioSessao.isPodeBloquear(processoDt.getId(), processoDt.getId_Serventia())%>')">
															<img id='MostrarArquivos_<%=movimentacaoDt.getId()%>' src="imagens/22x22/go-bottom.png" alt="Mostrar ou ocultar arquivos" title="Mostrar ou ocultar arquivos" />
														</a>
													<%} %>
												</td>
			  									<td class="colunaMinima">	
			  										<div class="btAcoes">	
													<% if  (UsuarioSessao.isPodeGerarEvento()){ %>
														<% if (movimentacaoDt.getMostrarColunaManterEventos()){ %>
					  										<img id="imgAlterar" class="imgAlterar" src="imagens/imgEditar.png" alt="Manter Evento(s) em Movimentação" title="Manter Evento(s) em Movimentação" 
					  										onclick="javascript: manterEventos('<%=movimentacaoDt.getManterEvento()%>','<%=movimentacaoDt.getId()%>','<%=Configuracao.Localizar%>')"/>
							    						<% } %>
						    						<% } %>
					  								<% if (movimentacaoDt.getMostrarColunaGerarPendencias()){ %>												
					  									<img id="imgAlterar" class="imgAlterar" src="imagens/22x22/btn_movimentar.png" alt="Gerar Pendências em Movimentação" title="Gerar Pendências em Movimentação" onclick="javascript: gerarPendencias('<%=movimentacaoDt.getId()%>')"/>												
							    					<% } %>
													<div class="dropMovimentacao" id_movi = '<%=movimentacaoDt.getId()%>' movi_tipo ='<%=movimentacaoDt.getMovimentacaoTipoLimpa()%>' codigo_temp ='<%=movimentacaoDt.getCodigoTemp()%>' ></div>
													 </div>
			  									</td>
					    					
									<%}	%>
										</tr>
										<tr id="linha_<%=movimentacaoDt.getId()%>" carregado='não' style="display: none;">
											<td colspan="6" id="pai_<%=movimentacaoDt.getId()%>" class="Linha"></td>
										</tr>
										
									<%
										boLinha = !boLinha;
									}%>
				           			</tbody>
						    	</table>
							</div>
							<div id="IndiceProcesso">
								<div id="dadosIndice" buscaDados="sim" class="dadosIndice"> </div>
							</div>	
							
							<% if (processoDt.getDataDigitalizacao() != null && processoDt.getDataDigitalizacao().trim().length() > 0){
							  if (processoDt.getListaMovimentacoesFisico() != null && processoDt.getListaMovimentacoesFisico().size() > 0) { %> 
							<div id="AtosFisicos">
				    			<table id="TabelaArquivos" class="Tabela" >
				        			<thead>
				            			<tr class="TituloColuna">
				                			<td>Nº</td>
				                   			<td width="60%">Movimentação</td>
				                  			<td width="20%">Data</td>
				                  			<td width="20%">Usuário</td>               
				               			</tr>
				               			<tr>
				               			    <td colspan="5"></td>
				               			</tr>
				           			</thead>
				    				<tbody id="tabListaProcesso">
						    		<%
						    			List listaMovimentacoesFisico = (List)processoDt.getListaMovimentacoesFisico();	
    					    		    boLinha=false; 
    						   	    	for (int i=(listaMovimentacoesFisico != null?listaMovimentacoesFisico.size()-1:-1);i>=0;i--){
    					   			  		MovimentacaoDt movimentacaoDt = (MovimentacaoDt)listaMovimentacoesFisico.get(i);
    								%>
				                   			<tr class='filtro-entrada TabelaLinha<%=boLinha?1:2%>' >
					                   			<td class="colunaMinima"> <%=i+1%></td>
					                    		<td class="filtro_coluna_movimentacao" width="55%">
					                    			<span class="filtro_tipo_movimentacao"><%=movimentacaoDt.getMovimentacaoTipo()%></span><br />
					                    		</td>
						                   		<td width="100" align="center"><%= movimentacaoDt.getDataRealizacao()%></td>
						                   		<td width="25%">
						                   		<% if (movimentacaoDt.getNomeUsuarioRealizador() != null && movimentacaoDt.getNomeUsuarioRealizador().trim().length() > 0){ %>
						                   		<%=movimentacaoDt.getNomeUsuarioRealizador()%>				                   	
						                   		<% } else {%>&nbsp;<%} %>
						                   		</td>
						                   		
											</tr>
										
									<%
										boLinha = !boLinha;
									}%>
				           			</tbody>
						    	</table>
							</div>
							<%}
							} %>
								
					</div>
				</div>
				<%@include file="Padroes/Mensagens.jspf"%>
		
				<%if(request.getAttribute("expedirImprimir") != null && String.valueOf(request.getAttribute("expedirImprimir")).equalsIgnoreCase("true")){%>
					<input id="expedirImprimir" name="expedirImprimir" type="hidden" value="<%=request.getAttribute("expedirImprimir")%>" />
					<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="" />
					 <script type="text/javascript">			 	
					 	var form = document.getElementById("Formulario");
						form.action = 'MandadoPrisao';
					 	AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');
					 	AlterarValue('expedirImprimir','true');
					 	AlterarValue('tempFluxo1','4');
						form.submit();	
					 </script>    		  
				<%}%>
				
				<%if(request.getAttribute("expedirImprimirSolicitarCargaProcesso") != null && String.valueOf(request.getAttribute("expedirImprimirSolicitarCargaProcesso")).equalsIgnoreCase("true")){%>
					<input id="expedirImprimirSolicitarCargaProcesso" name="expedirImprimirSolicitarCargaProcesso" type="hidden" value="<%=request.getAttribute("expedirImprimirSolicitarCargaProcesso")%>" />
					<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="" />
					 <script type="text/javascript">			 	
					 	var form = document.getElementById("Formulario");
						form.action = 'SolicitarCargaProcesso';
					 	AlterarValue('PaginaAtual','<%=Configuracao.Imprimir%>');
					 	AlterarValue('expedirImprimirSolicitarCargaProcesso','true');
						form.submit();	
						form.action = 'BuscaProcesso';
					 </script>    		  
				<%}%>	

			</form>			
		</div>
		
	</body>
</html>
