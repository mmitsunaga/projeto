<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioMovimentacaoTipoDt"%>

<jsp:useBean id="Peticionamentodt" scope="session" class="br.gov.go.tj.projudi.dt.PeticionamentoDt" />
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html>
	<head>	
		<title>Peticionar</title>
		
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');			
		</style>      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
      	<script type='text/javascript' src='./js/Mensagens.js'></script>
      	<script type='text/javascript' src='./js/DivFlutuante.js'> </script>
		<script type='text/javascript' src='./js/tabelas.js'></script>
		<script type='text/javascript' src='./js/tabelaArquivos.js'></script>
		
		<script type="text/javascript">
			$(document).ready(function() {
				CKEDITOR.on('instanceReady', function(e){
					var cabecalhoTela = 75;
					var objeto = window.parent.document.getElementById('Principal');															
					var altura = objeto.contentDocument.body.offsetHeight + 680;
					objeto.height = altura + cabecalhoTela;
				});
			});
	</script>
			
	</head>

	<body onload="fechar('sim');">
		<div id="divCorpo" class="divCorpo">
  			
  			<div class="area"><h2>&raquo;<%=request.getAttribute("TituloPagina")%> </h2></div>
  		    
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">

				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />			
	  			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
	  			
	  			<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend>Peticionar em <%=(Peticionamentodt.isMultiplo()?"Múltiplos Processos":"Processo")%></legend>
					
						<%@ include file="DadosProcessoPeticionamento.jspf"%>
						
						<br />
						<label class="formEdicaoLabel" for="Id_MovimentacaoTipo">*Tipo Movimentação   
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarMovimentacaoTipo" name="imaLocalizarMovimentacaoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />  
						</label><br>
						<input id="MovimentacaoTipo" name="MovimentacaoTipo" type="hidden" value="<%=Peticionamentodt.getMovimentacaoTipo()%>" />					
						<select name="Id_MovimentacaoTipo" id="Id_MovimentacaoTipo" size="1" onchange="AlterarValue('MovimentacaoTipo',this.options[this.selectedIndex].text)">
							<option value="null">--Selecione o Tipo de Movimentação--</option>
							<%
							    if (Peticionamentodt.getListaTiposMovimentacaoConfigurado() != null){
									List listaMovimentacaoTipo = Peticionamentodt.getListaTiposMovimentacaoConfigurado();
									for (int i=0;i<listaMovimentacaoTipo.size();i++){
									UsuarioMovimentacaoTipoDt usuarioMovimentacaoTipoDt = (UsuarioMovimentacaoTipoDt)listaMovimentacaoTipo.get(i);%>
										<option value="<%=usuarioMovimentacaoTipoDt.getId_MovimentacaoTipo()%>" <%=((Peticionamentodt.getId_MovimentacaoTipo() != null && Peticionamentodt.getId_MovimentacaoTipo().equalsIgnoreCase(usuarioMovimentacaoTipoDt.getId_MovimentacaoTipo()))?"selected='selected'":"")%>><%=usuarioMovimentacaoTipoDt.getMovimentacaoTipo()%></option>
							   		<%}
							    }
				            %> 	  						   
			           </select><br>
						
						<label class="formEdicaoLabel" for="Descricao">Descrição Movimentação
						<input type="image"  src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('MovimentacaoComplemento', ''); return false;" title="Limpar descrição movimentação" />
						</label><br> 
						<input name="MovimentacaoComplemento" id="MovimentacaoComplemento" type="text" size="30" maxlength="80" value="<%=Peticionamentodt.getComplemento()%>"/>
						
						<br />
						<br />
					
						<!--  Inserção de Arquivos com opção de usar Editor de Modelos -->
						<%@ include file="Padroes/InsercaoArquivosAssinador.jspf"%>
			
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgConcluir" type="submit" value="Avançar" onclick="AlterarValue('PaginaAtual',<%=Configuracao.Salvar%>);"> 
						</div>
						
					</fieldset>
				</div>
			</form>
		    <%@ include file="Padroes/Mensagens.jspf" %>
			
		</div>
		 		
	</body>
	<script type="text/javascript">
		function fechar(valor){
			if (valor!=null){
				$('.corpo').hide();
				$('#divEditor').hide();
			}
		}
       	//setTimeout("fechar('sim')", 1000);
	</script>
</html>