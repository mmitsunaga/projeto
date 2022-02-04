<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@page import="br.gov.go.tj.projudi.dt.ArquivoTipoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioArquivoDt"%>

<jsp:useBean id="UsuarioArquivodt" scope="session" class= "br.gov.go.tj.projudi.dt.UsuarioArquivoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class= "br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
		<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
						
		<script type='text/javascript' src='./js/Funcoes.js'></script>
	    <script type='text/javascript' src='./js/jquery.js'></script>
	   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>     
		<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type='text/javascript' src='./js/checks.js'></script>
		
		<script type="text/javascript">
			function getTextoEditor(id){
				return "";
			}
		</script>
						
	</head>
	
	<%@ include file="js/InsercaoArquivo.js"%>
	
	<body onLoad="atualizarArquivos();">
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; Inserção de Documentos </h2></div>
		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
   		
   				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
   				<input name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
   				<input name="Id_UsuarioArquivo" id="Id_UsuarioArquivo" type="hidden" value="" />
   				<input name="NomeArquivo" id="NomeArquivo" type="hidden" value="" />

		   		<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend>Inserção de Documentos </legend>
						<% UsuarioDt usuarioDt = UsuarioArquivodt.getUsuarioDt(); %>
						
						<!-- Lista de Documentos do Usuário -->
						<fieldset id="VisualizaDados" class="VisualizaDados">
				    		<legend> Usuário: <%=usuarioDt.getNome() %></legend>
			    			<table id="TabelaArquivos" class="Tabela">
			        			<thead>
			            			<tr class="TituloColuna">
			                			<td class="colunaMinima"></td>
			                   			<td width="30%">Descrição</td>
			                  			<td width="8%">Data</td>
			                  			<td>Documento</td>
			                  			<td class="colunaMinima">Excluir</td>
			               			</tr>
			           			</thead>
			    				<tbody id="tabListaArquivos">
			    				<%
			    				List liTemp = usuarioDt.getListaArquivosUsuario();
			    				UsuarioArquivoDt objTemp;
			    				boolean boLinha=false; 
			    				if (liTemp != null){
			    					for(int i = 0 ; i< liTemp.size();i++) {
			    				    	objTemp = (UsuarioArquivoDt)liTemp.get(i); %>
			    						<tr class="TabelaLinha<%=(boLinha?1:2)%>"  >
			    							<td> <%=i+1%></td>
			    							<td><%=objTemp.getArquivoDt().getArquivoTipo()%></td>
			    							<td><%=objTemp.getArquivoDt().getDataInsercao()%></td>
			    							<td>
			    								<a href="UsuarioArquivo?PaginaAtual=<%=Configuracao.Curinga6%>&Id_UsuarioArquivo=<%=objTemp.getId()%>&hash=<%=objTemp.getHash()%>"> 
												<%=objTemp.getArquivoDt().getNomeArquivoFormatado()%>
												</a>
			    							</td>
			    							<td>
			    								<input name="formLocalizarimgexcluir" type="image" src="./imagens/imgExcluirPequena.png" 
			    								onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('Id_UsuarioArquivo','<%=objTemp.getId()%>');AlterarValue('NomeArquivo','<%=objTemp.getArquivoDt().getNomeArquivoFormatado()%>');" />
			    							</td> 
			    						</tr>
			    				<%
			    					boLinha = !boLinha;
			    				}
			    				} else {%>
			    					<tr>
			    						<td colspan="5"><em>Usuário sem Documentos.</em>
			    					</td>
			    				<% } %>
			           			</tbody>
					    	</table>
						</fieldset>
						<br />
						
						<fieldset>
							<legend>Novos Documentos</legend>
							<input type="hidden" id="id_ArquivoTipo" name="Id_ArquivoTipo" value="<%=UsuarioArquivodt.getId_ArquivoTipo()%>" />
							<input id="nomeArquivo" name="nomeArquivo" type="hidden" size="54" maxlength="255" value=""/>
							<label class="formEdicaoLabel"> Tipo de Arquivo
							<input class="FormEdicaoimgLocalizar" name="imaLocalizarArquivoTipo" type="image" src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" title="Selecionar Tipo de Arquivo" />
							<input class="FormEdicaoimgLocalizar" name="imaLimparArquivoTipo" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('id_ArquivoTipo', 'arquivoTipo'); return false;" title="Limpar Tipo de Arquivo" />
							</label><br>
							
							<input class="formEdicaoInputSomenteLeitura" name="ArquivoTipo" readonly="readonly" type="text" size="50" maxlength="50" id="arquivoTipo" value="<%=UsuarioArquivodt.getArquivoTipo()%>" />		
							<br />
															
							<input type="hidden" name="arquivo" id="arquivo" value="" />
  			           
		    				<div id="divBotoesCentralizados" class="divBotoesCentralizados">
		    					
		    					<div id="upload" >
									<div id="arquivo_assinado">					   
										<span class="btn btn-success fileinput-button" style="display:none">
										    <i class="glyphicon glyphicon-plus"></i>
									    	<span>Selecionar arquivos...</span>
											<input type="file" id="fileupload" name="files[]" multiple accept="<%=UsuarioSessao.getTipoArquivoUpload()%>" onchange="if (!selecionaArquivoAssinado(this.files)) {this.value='';preparaUploadArquivoAssinado();}"/>
										</span>	        
								       	<button type="button" title="Clique para adicionar o arquivo" onclick="submeteArquivoAssinado();LimparChaveEstrangeira('id_ArquivoTipo', 'arquivoTipo'); return false;" id="botaoAplicar" style="display:none">Confirmar</button>
								       	<button type="button" title="Clique para carregar o arquivo" onclick="abreDialogoUploadArquivoAssinado();"  id="botaoUpload">Anexar Arquivos</button>
								       	<button type="button"  onclick="javascript:incluirArquivosAreaTransferencia();">
											Colar
							 			</button>  							 
							 			<button type="button"  onclick="javascript:limparArquivosAreaTransferencia();">
											Limpar
							 			</button>								       	
							      	</div>
							  	</div>		    					
		    				</div>
															 
							<%@ include file="Padroes/ListaArquivos.jspf"%>
															
							<div id="divBotoesCentralizados" class="divBotoesCentralizados">
								<input name="imgConcluir" type="submit" value="Concluir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
							</div>
						</fieldset>
					</fieldset>
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
				</div>
				
				<%@ include file="Padroes/Mensagens.jspf" %>
			</form>
		</div>
	</body>
</html>	