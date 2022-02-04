<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.MandadoPrisaoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>

<jsp:useBean id="MandadoPrisaodt" scope="session" class= "br.gov.go.tj.projudi.dt.MandadoPrisaoDt"/>
<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>
<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de MandadoPrisao  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type='text/javascript' src='./js/ckeditor/ckeditor.js?v=24092018'></script>
 	<script type='text/javascript' src='./js/checks.js'></script>
 	    
	<%@ include file="./js/InsercaoArquivo.js"%>
		
</head>

<body onload="atualizarArquivos('false');">
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Expedir Mandado de Prisão</h2></div>
		<form action="MandadoPrisao" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>">
			
			<input type="hidden" id="assinado" name="assinado" value="true" />
			<input type="hidden" id="gerarAssinatura" name="gerarAssinatura" value="false" />
			
			<%String conteudoArquivosSemAspasDuplas = String.valueOf(request.getAttribute("textoEditor")).replaceAll("\"","ASPAS_DUPLAS").replaceAll("&","&amp;");%>
			<input id="conteudoArquivos" name="conteudoArquivos" type="hidden" value="<%=conteudoArquivosSemAspasDuplas%>" />

			<div id="divEditar" class="divEditar">
				<!--INÍCIO DADOS DO PROCESSO-->
				<fieldset id="VisualizaDados" class="VisualizaDados"><legend> Processo </legend>
					<div> N&uacute;mero</div>
					<span> <a href="BuscaProcesso?Id_Processo=<%=processoDt.getId()%>&PassoBusca=2"><%=processoDt.getProcessoNumeroCompleto()%></a></span>
					<div> Área</div>
					<span class="span2"> <%=processoDt.getArea()%></span><br />
					<div> Mandado de Prisão</div>
					<span class="span2"> <%=MandadoPrisaodt.getMandadoPrisaoNumero()%></span><br />
				</fieldset>
				<!--FIM DADOS DO PROCESSO-->

				<!--INÍCIO DADOS MANDADO DE PRISÃO-->
				<fieldset class="formEdicao">
					<legend class="formEdicaoLegenda">Dados do Mandado de Prisão</legend>
						<div id="divTextoEditor" name="divTextoEditor" class="divTextoEditor" style="height: 300px;">
						<%=request.getAttribute("textoEditor")%>
						</div>						
				</fieldset>
				<br />	
					<fieldset class="formEdicao">	
						<legend>Inserir Arquivo(s)</legend>
						<input type="hidden" id="id_arquivo_session" name="id_arquivo_session" value="<%=request.getAttribute("id_arquivo_session")%>" />
						<input type="hidden" id="assinado" name="assinado" value="true" />
						<input type="hidden" id="id_ArquivoTipo" name="Id_ArquivoTipo" value="<%=request.getAttribute("Id_ArquivoTipo")%>" />
						<input type="hidden" id="expedirImprimir" name="expedirImprimir" value="<%=request.getAttribute("expedirImprimir")%>" />

						<label class="formEdicaoLabel"> *Tipo de Arquivo</label><br>
						<input class="formEdicaoInputSomenteLeitura" name="ArquivoTipo" readonly type="text" size="50" maxlength="50" id="arquivoTipo" value="<%=request.getAttribute("ArquivoTipo")%>" />		
						<br />
						<label class="formEdicaoLabel"> Nome Arquivo
						<input type="image"  src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('nomeArquivo', ''); return false;" title="Limpar nome do arquivo" />
						</label><br>
						
						<input id="nomeArquivo" name="nomeArquivo" type="text" size="54" maxlength="255" value="<%=(request.getAttribute("nomeArquivo") != null ? request.getAttribute("nomeArquivo") : "MandadoDePrisao.html")%>" />
						<br />

						<%-- <%@ include file="Padroes/Assinador.jspf"%> --%>												
						<input type="hidden" name="arquivo" id="arquivo" value="" />
						
						<div id="divBotoesCentralizadosAssinador" class="divBotoesCentralizadosAssinador">							
							 <!-- servidor -->
						     <button id="but_assinar" type="button"  onclick="javascript:digitarSenhaCertificado(inserirArquivoSession); return false;">
									Assinar
							 </button>  				   					 							 			  
						     <button type="button"  onclick="javascript:incluirArquivosAreaTransferencia();">
									Colar
							 </button>  					 
							 <button type="button"  onclick="javascript:limparArquivosAreaTransferencia();">
									Limpar
							 </button>				 		              			
			        	</div>
						
								
						<%@ include file="Padroes/ListaArquivos.jspf"%>
					</fieldset>
				
					<div id="divConfirmarSalvar" class="ConfirmarSalvar">
						<button type="submit" name="operacao" value="Expedir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('tempFluxo1','2');" >
							<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Expedir" /> -->
							Expedir
						</button>
						<button type="submit" name="operacao" value="ExpedirImprimir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('tempFluxo1','2');AlterarValue('expedirImprimir','true');" >
							<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Expedir e Imprimir" /> -->
							Expedir e Imprimir
						</button>
					</div>		

					<br />

				<!--FIM DADOS MANDADO DE PRISÃO-->

			</div>

		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
<script type="text/javascript">
		$('.corpo').hide();		
	</script>
</html>
