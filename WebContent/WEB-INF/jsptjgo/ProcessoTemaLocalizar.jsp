<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTemaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.TemaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.TemaSituacaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.TemaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.TemaOrigemDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>

<jsp:useBean id="TemaDt" scope="session" class= "br.gov.go.tj.projudi.dt.TemaDt"/>
<jsp:useBean id="ServentiaDt" scope="session" class= "br.gov.go.tj.projudi.dt.ServentiaDt"/>

<html>
	<head>
		<title>Busca de Processo</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/menusimples.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/checks.js'></script>      	
	    <script type='text/javascript' src='./js/jquery.js'></script>
	    <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
      	
      	<script type='text/javascript'>
			function validaForm() {
				if( $("[name=processos]:checked").length < 1){
					mostrarMensagemErro('Projudi - Erro', 'Selecione ao menos um processo.');
					return;
				}
				document.getElementById('Formulario').submit();
			}
		</script>
      	
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PosicaoPaginaAtual" name="PosicaoPaginaAtual" type="hidden" value="<%=request.getAttribute("PosicaoPaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				<input id="PassoBusca" name="PassoBusca" type="hidden" value="<%=request.getAttribute("PassoBusca")%>" />
			
				<div class="area"><h2>&raquo; Consulta de Processos por Tema </h2></div>
				
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda"> Consulta de Processos por Tema </legend>
						<br />
						<label class="formEdicaoLabel" for="Id_Tema">Tema
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarTema" name="imaLocalizarTema" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(TemaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
						<input class="FormEdicaoimgLocalizar" name="imaLimparTema" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Tema','Tema'); return false;" title="Limpar Tema">
						</label><br>  
						<input type="hidden" name="Id_Tema" id="Id_Tema" value="<%=TemaDt.getId()%>">
						
						<input class="formEdicaoInputSomenteLeitura"  readonly name="Tema" id="Tema" type="text" size="100" maxlength="100" value="<%=TemaDt.getTitulo()%>"/>
						<br />
						<label class="formEdicaoLabel" for="Id_TemaOrigem">Origem
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarTemaOrigem" name="imaLocalizarOrigem" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(TemaOrigemDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
						<input class="FormEdicaoimgLocalizar" name="imaLimparTemaOrigem" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_TemaOrigem','TemaOrigem'); return false;" title="Limpar Tema Origem">
						</label><br>  
						<input type="hidden" name="Id_TemaOrigem" id="Id_TemaOrigem" value="<%=TemaDt.getId_TemaOrigem()%>">
						
						<input class="formEdicaoInputSomenteLeitura"  readonly name="TemaOrigem" id="TemaOrigem" type="text" size="100" maxlength="100" value="<%=TemaDt.getTemaOrigem()%>"/>
						<br />
						<label class="formEdicaoLabel" for="Id_TemaSituacao">Situação
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarTema" name="imaLocalizarTema" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(TemaSituacaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
						<input class="FormEdicaoimgLocalizar" name="imaLimparTema" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_TemaSituacao','TemaSituacao'); return false;" title="Limpar Tema Situação">
						</label><br>  
						<input type="hidden" name="Id_TemaSituacao" id="Id_TemaSituacao" value="<%=TemaDt.getId_TemaSituacao()%>">
						
						<input class="formEdicaoInputSomenteLeitura"  readonly name="TemaSituacao" id="TemaSituacao" type="text" size="100" maxlength="100" value="<%=TemaDt.getTemaSituacao()%>"/>
						<br />
						<label class="formEdicaoLabel" for="Id_TemaTipo">Tipo
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarTema" name="imaLocalizarTema" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(TemaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
						<input class="FormEdicaoimgLocalizar" name="imaLimparTema" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_TemaTipo','TemaTipo'); return false;" title="Limpar Tema Tipo">
						</label><br>  
						<input type="hidden" name="Id_TemaTipo" id="Id_TemaTipo" value="<%=TemaDt.getId_TemaTipo()%>">						
						<input class="formEdicaoInputSomenteLeitura"  readonly name="TemaTipo" id="TemaTipo" type="text" size="100" maxlength="100" value="<%=TemaDt.getTemaTipo()%>"/>
						<br />
						
						<label class="formEdicaoLabel" for="Id_Serventia">Serventia
						<input type="hidden" name="Id_Serventia" id="Id_Serventia" value="<%=ServentiaDt.getId()%>">
					    <input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
						<input class="FormEdicaoimgLocalizar" name="imaLimparServentia" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_Serventia','Serventia'); return false;" title="Limpar Serventia">  
					    </label><br>					    
					    <input class="formEdicaoInputSomenteLeitura"  readonly name="Serventia" id="Serventia" type="text" size="100" maxlength="100" value="<%=ServentiaDt.getServentia()%>"/><br />
						<br>
						
						<label class="formEdicaoLabel" for="TemaCodigo">Número do Tema</label><br>  
						<input class="formEdicaoInput"   name="TemaCodigo" id="TemaCodigo" type="text" size="10" maxlength="10" value="<%=TemaDt.getTemaCodigo()%>"/>
						<br />
						<div class="Centralizado">
							<input type="submit" onclick="AlterarValue('PaginaAtual','6');AlterarValue('PassoBusca','1');" value="Buscar" name="imgSubmeter">
						</div>
					</fieldset>
				</div>
				<div id="divLocalizar" class="divLocalizar"> 
					<input type="hidden" id="Id_Processo" name="Id_Processo">
					
					<div align="left">						
  						<% 	if (request.getAttribute("podeMovimentar").toString().equalsIgnoreCase("true")){ %>	  					
						<input name="imgMultipla" type="button" value="Movimentação em Lote" onclick="AlterarAction('Formulario','Movimentacao');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');validaForm();"/> 						
						<%}%>
					</div>
						
					<div id="divTabela" class="divTabela"> 
						
				    	<table id="Tabela" class="Tabela">
				        	<thead>
				            	<tr class="TituloColuna">
				            		<td width="8%" align="center">
				            			<input type="checkbox" id="chkSelTodos" value="" onclick="atualizarChecks(this, 'divTabela')"
							    				title="Alterar os estados de todos os itens da lista" />
							    	</td>
				                  	<td>N&uacute;mero</td>
				                  	<td>Origem</td>
				                  	<td>Situação</td>
				                  	<td>Tipo</td>
				                  	<td>Data Sobrestado</td>
				               	</tr>
				           	</thead>
				           	<tbody id="tabListaProcesso">
							<%
								List liTemp = (List)request.getAttribute("ListaProcessos");
								ProcessoTemaDt processoTemaDt;
								String processoNumero="";
								int contProcessos = 0;
								boolean boLinha=false; 								
								//Percorre Lista Geral de Processos
								if (liTemp != null) {
									for(int i = 0 ; i< liTemp.size();i++) {
										processoTemaDt = (ProcessoTemaDt)liTemp.get(i); 
							%> 
									<tr class="TabelaLinha<%=(boLinha?1:2)%>"> 
										<td width="10px" align="center">
											<input class="formEdicaoCheckBox" name="processos" type="checkbox" value="<%=processoTemaDt.getId_Proc()%>">
										</td>
					                   	<td width="100px"><a href="BuscaProcesso?Id_Processo=<%=processoTemaDt.getId_Proc()%>"><%=processoTemaDt.getProcNumero()%></td>
					                   	<td><%=processoTemaDt.getTemaOrigem()%></td>
					                   	<td><%=processoTemaDt.getTemaSituacao()%></td>
					                   	<td><%=processoTemaDt.getTemaTipo()%></td>
					                   	<td width="150px"><%=processoTemaDt.getDataSobrestado()%></td>
				               		</tr>
							<%
									contProcessos++;
									}
								}
							%>
				           	</tbody>
				           	<tfoot>
								<tr>
									<td colspan="6">Quantidade de processos: <span id="qtd"><%=contProcessos%></span></td>
								</tr>
							</tfoot>
				       	</table>
   					</div> 
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body> 
</html>