<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.PermissaoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.PermissaoEspecialDt"%>

<jsp:useBean id="Permissaodt" scope="session" class= "br.gov.go.tj.projudi.dt.PermissaoDt"/>

<html>
  <head>
    <title>Permissão</title>
    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
    <style type="text/css">
      @import url('./css/Principal.css');
      @import url('./css/Paginacao.css');
    </style>
    
    
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
    <script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
    <script type="text/javascript" src="./js/AtualizarCheckbox.js"></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
    <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script> 
  </head>
  <body>
    <div id="divCorpo" class="divCorpo" >
      <div class="area"><h2>&raquo; Cadastro de Permissão</h2></div>
      <form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
        <input type="hidden" id="PaginaAtual" name="PaginaAtual" value="<%=request.getAttribute("PaginaAtual")%>">
        <input type="hidden" name="TituloPagina" value="<%=request.getAttribute("tempTituloPagina")%>">
        <input type="hidden" id="tempBuscaId_PermissaoPai" name="tempBuscaId_PermissaoPai">
        <input type="hidden" id="tempBuscaPermissaoPai" name="tempBuscaPermissaoPai">
        <input type="hidden" id="tempBuscaId_PermissaoEspecial" name="tempBuscaId_PermissaoEspecial">
        <input type="hidden" id="tempBuscaPermissaoEspecial" name="tempBuscaPermissaoEspecial">
        <input type="hidden" id="ArrayFuncoesPermissao" name="ArrayFuncoesPermissao">
        <input type="hidden"  name=__Pedido__ value="<%=request.getAttribute("__Pedido__")%>">
        <div id="divPortaBotoes" class="divPortaBotoes">
          <%@ include file="Padroes/Botoes.jspf"%>      
        <a class="divPortaBotoesLink" href="Ajuda/PermissaoAjuda.html" target="_blank">  
          <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" alt="Ajuda" /> 
        </a>
      </div>
      <div id="divEditar" class="divEditar">
        <fieldset class="formEdicao"> 
        <legend class="formEdicaoLegenda">Editar Permissão</legend>
        
        <div class="col20">
        <label class="formEdicaoLabel" for="Id_Permissao">Id</label><br>
        <input class="formEdicaoInputSomenteLeitura" name="Id_Permissao"  id="Id_Permissao"  type="text"  readonly value="<%=Permissaodt.getId()%>"/>
        </div>
        
        <div class="col50">
        <label class="formEdicaoLabel" for="Permissao">*Descrição</label><br> 
        <input class="formEdicaoInput" name="Permissao" id="Permissao"  type="text" size="60" maxlength="60" value="<%=Permissaodt.getPermissao()%>" onkeyup=" autoTab(this,60)"/>
        </div>
        
        <div class="col15">
        <label class="formEdicaoLabel" for="PermissaoCodigo">*Código</label><br> 
        <input class="formEdicaoInputSomenteLeitura" name="PermissaoCodigo" id="PermissaoCodigo" type="text" size="11" maxlength="11" readonly value="<%=Permissaodt.getPermissaoCodigo()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"/>
        </div>
        
        <div class="col45 clear">
        <label class="formEdicaoLabel" for="Id_PermissaoPai">Permissão Pai
         <input class="FormEdicaoimgLocalizar" id="imaLocalizarPermissaoPai" name="imaLocalizarPermissaoPai" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('tempBuscaId_PermissaoPai','Id_PermissaoPai');AlterarValue('tempBuscaPermissaoPai','PermissaoPai');AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.LocalizarAutoPai)%>')" <%if ((Permissaodt.getEMenu().equalsIgnoreCase("false")) && (!Permissaodt.getId().equalsIgnoreCase(""))){ %> readonly <%}%> >
        <input class="FormEdicaoimgLocalizar" id="imaLimparPermissaoPai" name="imaLimparPermissaoPai" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="AlterarValue('PermissaoCodigoPai','');LimparChaveEstrangeira('Id_PermissaoPai','PermissaoPai'); return false;" > 
        </label><br>  
       
        <input name='Id_PermissaoPai' id='Id_PermissaoPai' type='hidden'  value=''>
        <input class="formEdicaoInputSomenteLeitura"  readonly="true" name="PermissaoPai" id="PermissaoPai" type="text" size="60" maxlength="60" value="<%=Permissaodt.getPermissaoPai()%>">
        </div>
        
        <div class="col20">
        <label class="formEdicaoLabel" for="PermissaoCodigoPai">Código Permissão Pai</label><br> 
        <input class="formEdicaoInputSomenteLeitura" name="PermissaoCodigoPai" id="PermissaoCodigoPai" type="text" size="11" maxlength="11" readonly value="<%=Permissaodt.getPermissaoCodigoPai()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"/><br />
        </div>
        
        <div class="col45 clear">
        <label class="formEdicaoLabel" for="Id_PermissaoEspecial">Permissão Especial
        <input class="FormEdicaoimgLocalizar" id="imaLocalizarPermissaoEspecial" name="imaLocalizarPermissaoEspecial" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('tempBuscaId_PermissaoEspecial','Id_PermissaoEspecial');AlterarValue('tempBuscaPermissaoEspecial','PermissaoEspecial');AlterarValue('PaginaAtual','<%=String.valueOf(PermissaoEspecialDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					
        <input class="FormEdicaoimgLocalizar" id="imaLimparPermissaoEspecial" name="imaLimparPermissaoEspecial" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_PermissaoEspecial','PermissaoEspecial'); return false;" > 
        </label><br>  
        
        <input name='Id_PermissaoEspecial' id='Id_PermissaoEspecial' type='hidden'  value=''>
        <input class="formEdicaoInputSomenteLeitura"  readonly="true" name="PermissaoEspecial" id="PermissaoEspecial" type="text" size="60" maxlength="60" value="<%=Permissaodt.getPermissaoEspecial()%>">
       </div>
       
       <div class="col25" style="margin-top:30px">
        <label for="EMenu">*É Menu?</label> 
        <input class="formEdicaoInput" name="EMenu" id="EMenu"  type="checkbox"  value="true" <%if(Permissaodt.getEMenu().equalsIgnoreCase("true")){%> checked <%}%>/><br />
        </div>
        
        
        <div class="col45 clear">
        <label class="formEdicaoLabel" for="Link">Link</label><br> 
        <input class="formEdicaoInput" name="Link" id="Link"  type="text" size="50" maxlength="255" value="<%=Permissaodt.getLink()%>" onkeyup=" autoTab(this,255)"/>
        </div>
        
        <div class="col35">
        <label class="formEdicaoLabel" for="Titulo">Título</label><br> 
        <input class="formEdicaoInput" name="Titulo" id="Titulo"  type="text" size="50" maxlength="255" value="<%=Permissaodt.getTitulo()%>" onkeyup=" autoTab(this,255)"/><br />
        </div>
        
        <div class="col45 clear">
        <label class="formEdicaoLabel" for="IrPara">Ir Para</label><br> 
        <input class="formEdicaoInput" name="IrPara" id="IrPara"  type="text" size="50" maxlength="60" value="<%=Permissaodt.getIrPara()%>" onkeyup=" autoTab(this,60)"/>
        </div>
        
        <div class="col35">
        <label class="formEdicaoLabel" for="Ordenacao">Ordenação</label><br> 
        <input class="formEdicaoInput" name="Ordenacao" id="Ordenacao"  type="text" size="11" maxlength="11" value="<%=Permissaodt.getOrdenacao()%>" onkeypress="return DigitarSoNumero(this, event)" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)"/><br />
        </div>
        
        <%PermissaoDt[] arrayFuncoesPermissao = null;
          arrayFuncoesPermissao = (PermissaoDt[]) Permissaodt.getFuncoesPermissao();
          if (arrayFuncoesPermissao != null) {
            String[] permissoes = {"Excluir", "Imprimir", "Localizar", "LocalizarDWR", "Novo", "Salvar", "Curinga6", "Curinga7", "Curinga8", "Curinga9"};%>
	        <label class="formEdicaoLabel">Funções:</label/><br />
	        <table class="Tabela">
	        <tbody id="tabListaFuncoes">
	        <%for(int i=0; i<10; i++) {%>
	        <tr>
	        <td style="width:170px"><label class="formEdicaoLabel" for="Funcao<%=permissoes[i]%>"><%=permissoes[i]%></label><br>
            <%if(arrayFuncoesPermissao[i] != null) {%>	       
	        <td style="width:20px; font-size: 10px;"><a href="Permissao?PaginaAtual=<%=Configuracao.Editar%>&Id_Permissao=<%=arrayFuncoesPermissao[i].getId()%>"><%=arrayFuncoesPermissao[i].getPermissaoCodigo()%></a></td>
	        <td style="font-size: 10px;"><a href="Permissao?PaginaAtual=<%=Configuracao.Editar%>&Id_Permissao=<%=arrayFuncoesPermissao[i].getId()%>"><%=arrayFuncoesPermissao[i].getPermissao()%></a></td>
	        <td style="font-size: 10px;"><a href="Permissao?PaginaAtual=<%=Configuracao.Editar%>&Id_Permissao=<%=arrayFuncoesPermissao[i].getId()%>"><%=arrayFuncoesPermissao[i].getTitulo()%></a>
	        <%if(!arrayFuncoesPermissao[i].getPermissaoCodigoPai().equalsIgnoreCase(arrayFuncoesPermissao[i].getPermissaoCodigo().substring(0,arrayFuncoesPermissao[i].getPermissaoCodigo().length()-1)) && !arrayFuncoesPermissao[i].getPermissaoCodigoPai().equalsIgnoreCase("")) { %>
            <a style="color: #000080" href="Permissao?PaginaAtual=<%=Configuracao.Editar%>&Id_Permissao=<%=arrayFuncoesPermissao[i].getId_PermissaoPai()%>">Pai: <%=arrayFuncoesPermissao[i].getPermissaoCodigoPai()%>-<%=arrayFuncoesPermissao[i].getPermissaoPai()%></a>
	        <%}%>
	        </td>
	        <%} %>
	        </tr>
	        <%}%>
	        </tbody>	        
	        </table>
          <%}%>
    </div>
	<%@include file="Padroes/Mensagens.jspf"%> 
    <%@include file="Padroes/ConfirmarOperacao.jspf"%>
  </div>
 </body>
</html>
