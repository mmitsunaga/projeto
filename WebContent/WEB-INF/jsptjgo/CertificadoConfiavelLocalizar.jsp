<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.CertificadoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> Busca de Certificados Confiáveis  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	
	
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo;  Busca de Certificados Confiáveis </h2></div>
	<div id="divLocalizar" class="divLocalizar" > 
		<form action="CertificadoConfiavel" method="post" name="Formulario" id="Formulario">
		
		<div id="divPortaBotoes" class="divPortaBotoes">
			<input id="imgAtualizar" class="imgAtualizar" title="Voltar para tela principal" name="imaAtualizar" type="image"  src="./imagens/imgVoltar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Editar)%>')">
		</div>
		
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		<fieldset id="formLocalizar" class="formLocalizar"  > 
			<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Consulta de Certificado Confiável </legend>
			<label  id="formLocalizarLabel"   class="formLocalizarLabel">Descrição Certificado:</label><br> 
			<input  id="NomeBusca"   class="formLocalizarInput" name="NomeBusca" type="text" value="" size="30" maxlength="60" />
			<input  id="formLocalizarBotao"   class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')">
		</fieldset>

		<br />
		<div id="divTabela" class="divTabela" > 
			<table id="Tabela" class="Tabela">
				<thead>
					<tr>
						<th></th>
						<th>Certificado</th>
						<th>Data Emissão</th>
						<th>Data Expiração</th>					
					</tr>
					</thead>
				<tbody id="tabListaEstado">
				<%
				  List liTemp = (List)request.getAttribute("ListaCaConfiaveis");
				 CertificadoDt objTemp;
				  boolean boLinha=false; 
				  if(liTemp!=null)
					  for(int i = 0 ; i< liTemp.size();i++) {
					      objTemp = (CertificadoDt)liTemp.get(i); %>
										<tr class="TabelaLinha<%=(boLinha?1:2)%>"  >
											<td > <%=i+1%></td>
											<td> <%=objTemp.getDescricao() %>
											<td align="center">  <%=objTemp.getDataEmissao() %>
											<td align="center">  <%=objTemp.getDataExpiracao() %>			 
										</tr>
					<%
						boLinha = !boLinha;
					}%>
				</tbody>
			</table>
		</div> 
		</form> 
	</div> 
</div> 
<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
