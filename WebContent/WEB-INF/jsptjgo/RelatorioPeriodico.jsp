<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.RelatorioPeriodicoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="RelatorioPeriodicodt" scope="session" class= "br.gov.go.tj.projudi.dt.RelatorioPeriodicoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Relatório Periódico  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
		
		
		<script type='text/javascript' src='./js/checks.js'></script>
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/DivFlutuante.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script> 
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
		
		<script language="javascript" type="text/javascript">

		//function VerificarCampos() {
		//	with(document.Formulario) {
		//		if (SeNulo(RelatorioPeriodico, "O campo Nome é obrigatório!")) return false;
		//		if (SeNulo(CodigoSql, "O campo Código Sql é obrigatório!")) return false;
		//		submit();
		//	}
		//}

		//Método que atualiza os identificadores presentes na tabela de campos.
		//Esses identificadores serão usados para controlar estes registros no
		//controle da aplicação.
		function atualizarIdentificadoresLinhasTabelaCampos(){
			var table = document.getElementById('corpoTabelaCampos');
            var rowCount = table.rows.length;
            var idsLinhas = "";
            for(var i=0; i<rowCount; i++) {
                var row = table.rows[i];
				if(idsLinhas != ""){
					idsLinhas += ";";
				}
                idsLinhas += "campo_" + row.id;
            }
            document.getElementById('idsLinhasTabelaCampos').value = idsLinhas;
		}

		//Método que atualiza as cores das linhas das tabelas
		function colorirLinhasTabela(nomeTabela) {
			var table = document.getElementById(nomeTabela);
            var rowCount = table.rows.length;
            for(var i=0; i<rowCount; i++) {
                var row = table.rows[i];

                if(i%2 == 1){
                    row.className="TabelaLinha1";
                } else {
                    row.className="TabelaLinha2";
                }
            }
		}

	    //Método que realiza a exclusão de um registro da tabela de campos.	    
		function excluirCampo(id) {
			atualizarIdentificadoresLinhasTabelaCampos();
            var tabelaCampos = document.getElementById('corpoTabelaCampos');
			try {
	            var rowCount = tabelaCampos.rows.length;
	            for(var i=0; i<rowCount; i++) {
	                var linhaTabelaCampos = tabelaCampos.rows[i];
					if(linhaTabelaCampos.id == id) {
						tabelaCampos.deleteRow(i);
						calcularTamanhoIframeDefinido(-1*linhaTabelaCampos.clientHeight);
	                    break;
					}
	            }
	         }catch(e) {
	            alert(e);
	         }
	         document.getElementById("tamanhoTabelaCampos").value = tabelaCampos.rows.length;
	         atualizarIdentificadoresLinhasTabelaCampos();
	         colorirLinhasTabela("corpoTabelaCampos");
		}

	    //Método que aumenta ou diminui o tamanho da tela de acordo com a necessidade
		function calcularTamanhoIframeDefinido(valor){
			var objIframe = window.parent.document.getElementById('Principal');
			if (objIframe != null){
				var objTela = window.document.body;	
				objIframe.height =objTela.clientHeight + valor;
			}	
		}

		//Método que adiciona uma operação na tabela de operações. O método faz uso das informações
		//presentes na tabela de itens e informação apresentada pelo usuário
	    function adicionarCamposTabela(nome, descricao, obrigatorio, tipo) {
		    if(nome == "") {
			    alert("O campo Nome deve ser preenchido.");
			    return;
		    }
		    if(descricao == "") {
			    alert("O campo Descrição deve ser preenchido.");
			    return;
		    }
		    if(tipo == "") {
			    alert("O campo Tipo deve ser preenchido.");
			    return;
		    }
	    	try {
	            var tabelaCampos = document.getElementById('corpoTabelaCampos');
	            var qtdeLinhasCampos = tabelaCampos.rows.length;
            	var linhaTabelaCampos = tabelaCampos.insertRow(qtdeLinhasCampos);
            	if(qtdeLinhasCampos > 0) {
					var linhaAnterior = linhaTabelaCampos.previousElementSibling;
					linhaTabelaCampos.id = linhaAnterior.id; 
					linhaTabelaCampos.id++;
				} else {
					linhaTabelaCampos.id = tabelaCampos.rows.length;
				}

				var cell1 = linhaTabelaCampos.insertCell(0);
            	cell1.innerHTML = tipo;
				cell1.className = "Centralizado";
				var cell2 = linhaTabelaCampos.insertCell(1);
				cell2.className = "Centralizado";
            	cell2.innerHTML = nome;
            	var cell3 = linhaTabelaCampos.insertCell(2);
				cell3.className = "Centralizado";
            	cell3.innerHTML = descricao;
            	var cell4 = linhaTabelaCampos.insertCell(3);
            	if(obrigatorio){
            		cell4.innerHTML = "Obrigatório";
            	} else {
            		cell4.innerHTML = "Não Obrigatório";
            	}
            	cell4.className = "Centralizado";
            	var cell5 = linhaTabelaCampos.insertCell(4);
            	cell5.className = "Centralizado";
				cell5.innerHTML = "<input type=\"image\" align=\"center\" src=\"./imagens/imgExcluirPequena.png\" onclick=\"excluirCampo('"+linhaTabelaCampos.id+"');return false;\"/>";
				cell5.innerHTML += "<input type='hidden' name='campo_"+linhaTabelaCampos.id+"' value='"+nome+";"+descricao+";"+obrigatorio+";"+tipo+"'/>";

				//aumentando o iframe para receber a próxima linha da tabela operações
				calcularTamanhoIframeDefinido(linhaTabelaCampos.clientHeight);
				
				document.getElementById("nomeCampo").value = "";
				document.getElementById("descricaoCampo").value = "";
				document.getElementById("campoObrigatorio").checked = false;
				document.getElementById("tamanhoTabelaCampos").value = tabelaCampos.rows.length;
				atualizarIdentificadoresLinhasTabelaCampos();
				colorirLinhasTabela("corpoTabelaCampos");
	         }catch(e) {
	            alert(e);
	         }
		}
		</script>

</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Cadastro de Relatório Periódico</h2></div>
		<form action="RelatorioPeriodico" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo" alt="Novo"  class="imgNovo" title="Novo - Limpa os campos da tela" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" />
				<input id="imgsalvar" alt="Salvar" class="imgsalvar" title="Salvar - Salva os dados digitados" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="javascript:atualizarIdentificadoresLinhasTabelaCampos();AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>')" />	
				<input id="imgLocalizar" alt="Localizar" class="imgLocalizar" title="Localizar - Localiza um registro no banco" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')" /> 
				<input id="imgexcluir" alt="Excluir" class="imgexcluir" title="Excluir - Exclui o registro localizado" name="imaexcluir" type="image" src="./imagens/imgExcluir.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Excluir)%>')" />
			</div/><br />
			<div id="divEditar" class="divEditar">
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Dados do Relatório </legend>
					<input type="hidden" id="Id_RelatorioPeriodico" name="Id_RelatorioPeriodico" value="<%=RelatorioPeriodicodt.getId()%>"/>
					<label class="formEdicaoLabel" for="RelatorioEstPro">*Nome</label><br> 
					<input class="formEdicaoInput" name="RelatorioPeriodico" id="RelatorioPeriodico"  type="text" size="100" maxlength="60" value="<%=RelatorioPeriodicodt.getRelatorioPeriodico()%>" onkeyup=" autoTab(this,60)"/><br />
					<label class="formEdicaoLabel" for="CodigoSql">*Código SQL</label><br> 
					<textarea class="formEdicaoInput" style="width:600px;heigth:500px;" name="CodigoSql" id="CodigoSql" type="text" size="200" maxlength="65535" onkeyup="autoTab(this,65535)"><%=RelatorioPeriodicodt.getCodigoSql()%></textarea><br />
					
					<input type="hidden" id="tamanhoTabelaCampos" name="tamanhoTabelaCampos" value="<%=request.getAttribute("tamanhoTabelaCampos")%>"/>
					<input type="hidden" id="idsLinhasTabelaCampos" name="idsLinhasTabelaCampos" value="<%=request.getAttribute("idsLinhasTabelaCampos")%>"/>
					
					<fieldset> 
						<legend class="formEdicaoLegenda">Campos do SQL </legend>
						<table width="100%" style="font-size:12px;">
							<tr>
								<td> 
									<label for="nomeCampo">*ID Atributo</label><br> 
								
									<input class="formEdicaoInput" name="nomeCampo" id="nomeCampo"  type="text" size="40" maxlength="60" value="" onkeyup=" autoTab(this,60)"/>
								</td>
								<td> 
									<label for="descricaoCampo">*Label Atributo</label><br> 
								
									<input class="formEdicaoInput" name="descricaoCampo" id="descricaoCampo"  type="text" size="40" maxlength="60" value="" onkeyup=" autoTab(this,60)"/>
								</td>
							
								<td>
									<label for="tipoCampo">*Tipo</label><br>
								
									<select id="tipoCampo" name="tipoCampo" class="formEdicaoCombo">
										<option value=""></option>
										<option value="boolean">Boolean</option>
										<option value="data">Data</option>
										<option value="numero">Número</option>
										<option value="texto">Texto</option>
									</select>
								</td>
									<td>
									<label for="campoObrigatorio">Obrigatório</label>
								
									<input type="checkbox" name="campoObrigatorio" id="campoObrigatorio"/>
								</td>
								<td>
									<input name="btAdicionar" id="btAdicionar" title="Adicionar campo" type="image" src="./imagens/22x22/btn_adicionar.png"  onclick="adicionarCamposTabela(document.getElementById('nomeCampo').value, document.getElementById('descricaoCampo').value, document.getElementById('campoObrigatorio').checked, document.getElementById('tipoCampo').options[document.getElementById('tipoCampo').selectedIndex].text); return false;" />
								</td>
							</tr>
						</table>
						<div id="divTabelaOperacoes" name="divTabelaOperacoes" class="divTabela">
							<table class="Tabela" width="60%">
								<thead>
							    	<tr>
							    		<th width="10%">Tipo</th>
							    		<th width="34%">ID Atributo</th>
							    		<th width="34%">Label Atributo</th>
							    		<th width="16%">Obrigatoriedade</th>
							    		<th width="6%">Excluir</th>
							    	</tr>
							  	</thead>
							  	<tbody id="corpoTabelaCampos" name="corpoTabelaCampos">
									<%
										if(request.getAttribute("ListaCampos")!=null){
											List liTemp = (List)request.getAttribute("ListaCampos");
											String[] objTemp;
											String stTempNome="";
											for(int i = 0 ; i < liTemp.size();i++) {
												objTemp = (String[])liTemp.get(i).toString().split(";");
									%>
										<%if (stTempNome.equalsIgnoreCase("")) { stTempNome="a";%> 
						                <tr class="TabelaLinha1" id="<%=i+1%>"> 
										<%}else{ stTempNome=""; %>    
						                <tr class="TabelaLinha2" id="<%=i+1%>">
										<%}%>
						                   	<td class="Centralizado"><%=objTemp[3]%></td>
											<td class="Centralizado"><%=objTemp[0]%></td>
						                   	<td class="Centralizado"><%=objTemp[1]%></td>
						                   	<td class="Centralizado"><% if (objTemp[2].equals("true")){%>Obrigatório<%} else {%>Não Obrigatório<%}%></td>
					                   		<td class="colunaMinima">
									      		<input name="button2" id="button2" title="Retirar estes campos da lista" type="image" 
									      			src="./imagens/imgExcluirPequena.png"  onclick="excluirCampo(<%=i+1%>); return false;" />
									      			<input type='hidden' name='campo_<%=i+1%>' value='<%=objTemp[0]%>;<%=objTemp[1]%>;<%=objTemp[2]%>;<%=objTemp[3]%>'/>
									      	</td>                  
						               	</tr>
									<%}%>
									<%} %>
							  	</tbody>
							</table>
						</div>	
					</fieldset>
				</fieldset>
				<%@ include file="Padroes/Mensagens.jspf"%>
			</div>
		</form>
	</div>
</body>
</html>
