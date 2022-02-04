<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ComarcaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AreaDistribuicaoDt"%>


<html>
<head>
	<title>Importação dos Dados do Execpen</title>	
	<meta http-equiv="Content-Type" content="text/html; charset=latin1" />
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');		
	</style>
  	
  	
  	<script type='text/javascript' src='./js/Funcoes.js'></script>
  	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/DivFlutuante.js'></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Importar dados do Execpen</h2></div>
<!-- 		<form action="ImportarExecpen"  method="post" name="Formulario" id="Formulario" enctype="multipart/form-data"> -->
			<form action="ImportarExecpen"  method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>"/>
			<input id="conteudoArquivoProcesso" name="conteudoArquivoProcesso" type="hidden" value="<%=request.getAttribute("conteudoArquivoProcesso")%>"/>
			<input id="conteudoArquivoEvento" name="conteudoArquivoEvento" type="hidden" value="<%=request.getAttribute("conteudoArquivoEvento")%>"/>
	
			<div id="divEditar" class="divEditar"><br/>
				<fieldset class="formEdicao"> <br/>
					Antes de efetuar a importação dos dados, verifique os processos de cálculo já cadastrados no Processo Judicial Digital, conforme "Roteiro para Importação" <br/> 
					<a href="ImportarExecpen?PaginaAtual=-1&PassoEditar=2">Consultar processos de cálculo cadastrados no Processo Judicial Digital</a>
					<br/><br/>
			    </fieldset>
				<fieldset class="formEdicao"> <legend class="formEdicaoLegenda"> Dados para a Importação </legend> <br/>
					<table>
						<tbody>
							<tr><td align="right">*Comarca de origem da exportação:</td>
								<td align="left">
									<input id="Id_Comarca" name="Id_Comarca" type="hidden" value="<%=request.getAttribute("Id_Comarca")%>" />  
						    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarComarca" name="imaLocalizarComarca" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>'); AlterarValue('PassoEditar', '-1');" />				    	    
									<input class="formEdicaoInputSomenteLeitura"  readonly name="Comarca" id="Comarca" type="text" size="30" maxlength="100" value="<%=request.getAttribute("Comarca")%>"/>
								</td></tr>

							<tr><td align="right">*Área de Distribuição da exportação (EXCETO COMARCA DE GOIÂNIA):</td>
								<td align="left">
									<input id="Id_AreaDistribuicao" name="Id_AreaDistribuicao" type="hidden" value="<%=request.getAttribute("Id_AreaDistribuicao")%>" />  
						    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarAreaDistribuicao" name="imaLocalizarAreaDistribuicao" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>'); AlterarValue('PassoEditar', '-1');" />  
						    		<input class="formEdicaoInputSomenteLeitura"  readonly name="AreaDistribuicao" id="AreaDistribuicao" type="text" size="50" maxlength="100" value="<%=request.getAttribute("AreaDistribuicao")%>"/><br />
								</td></tr>

							<tr><td align="right" width="30%">*Diretório para salvar o arquivo de LOG:</td>
								<td align="left" width="70%">
									<input class="FormEdicaoimgLocalizar" id="imaLocalizarComarca" name="imaLocalizarComarca" type="image"  src="./imagens/tomcat.gif"  title="Utilizar o caminho do Servidor de Aplicação...." onclick="AlterarValue('PassoEditar', '1'); AlterarValue('PaginaAtual', '-1');" />
									<input class="formEdicaoInput" name="DiretorioArquivo" id="DiretorioArquivo" type="text" size="100" value="<%=request.getAttribute("DiretorioArquivo")%>"/>
								</td></tr>

							<%if (request.getAttribute("Id_AreaDistribuicao").toString().length() > 0 && request.getAttribute("AreaDistribuicao").toString().length() > 0){ %>
							<tr><td></td></tr>
							<tr><td></td></tr>
							<tr><td></td></tr>
							<tr><td colspan="2" align="center"><input type="file" name="arquivo" id="filename" size="50" /></td></tr>

							<tr><td align="right"><label for="filename">Arquivo processo:</label><br></td>
								<%if (request.getAttribute("conteudoArquivoProcesso").toString().length() > 0){ %>
								<td><input class="formEdicaoInputSomenteLeitura"  readonly type="text" size="100"  value="PROCESSO CARREGADO COM SUCESSO"/></td>
								<%} else { %>
								<td>
									<input name="imgInserir" type="submit" value="Carregar dados PROCESSO" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','4');"/>
								</td>		
								<%} %>		
							</tr>
							<tr><td align="right"><label for="filename">Arquivo evento:</label><br></td>
								<%if (request.getAttribute("conteudoArquivoEvento").toString().length() > 0){ %>
								<td><input class="formEdicaoInputSomenteLeitura"  readonly type="text" size="100"  value="EVENTO CARREGADO COM SUCESSO"/></td>
								<%} else { %>
								<td>
									<input name="imgInserir" type="submit" value="Carregar dados EVENTO" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','5');"/>
								</td>		
								<%} %>		
							</tr>
							
							<%} %>

						</tbody>
					</table>
			    </fieldset>
	  		</div>
			<div id="divBotoesCentralizados" class="divBotoesCentralizados">
				<input name="imgInserir" type="submit" value="Importar Dados" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');IniciaBarraProgresso();"> 
				<input name="imgCancelar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
		    </div>
		    
		    <style>
			.ui-progressbar .ui-progressbar-value { background-image: url(./imagens/pbar-ani.gif); }
			</style>
			<script type="text/javascript">
				var executaBarraProgresso;
				var resultado = 0;
				var resultadoProcesso = 0;
				var resultadoEvento = 0;
				var chamaAtualizarBarra = true;
				$( "#valorAtualBarraProgressoProcesso" ).text("Processo: " + resultadoProcesso + " %");
				$( "#valorAtualBarraProgressoEvento" ).text("Evento: " + resultadoEvento + " %");
				
				function IniciaBarraProgresso() {
					executaBarraProgresso = window.setInterval("atualizarBarraProgresso()", 500);
				}
				
				function atualizarBarraProgresso() {
		
					$.ajax({
						  type: "POST",
						  url: "/ImportarExecpen",
						  data: { PaginaAtual: "-1", PassoEditar: "3" }
						}).done(function( retorno ) {
						  //alert( msg );
						  var dados = retorno.split(":");
		
						  var valorAtualBarra = dados[1];
						  var totalBarra = dados[2];

							if( dados[0] == "processo" ) {
								resultadoProcesso = Math.ceil((valorAtualBarra*100) / totalBarra);
								resultado = resultadoProcesso;
							}
							else {
								resultadoEvento = Math.ceil((valorAtualBarra*100) / totalBarra);
								resultado = resultadoEvento;
							}
						  

					});
			
					$(function() {
						$( "#progressbar" ).progressbar({
							value: resultado
						});
					});
		
					$( "#valorAtualBarraProgressoProcesso" ).text("Processo: " + resultadoProcesso + " %");
					$( "#valorAtualBarraProgressoEvento" ).text("Evento: " + resultadoEvento + " %");
				}
				
			</script>
		
			<br />
			<div id="progressbar"></div>
			<br />
			<center><span id="valorAtualBarraProgressoProcesso" /></center>
			<br />
			<center><span id="valorAtualBarraProgressoEvento" /></center>

	    </form>
	    
	</div>    	
	<%@ include file="Padroes/Mensagens.jspf" %>
	
</body>

</html>
