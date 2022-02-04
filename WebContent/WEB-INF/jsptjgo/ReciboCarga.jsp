<jsp:useBean id="Pendenciadt" scope="session" class= "br.gov.go.tj.projudi.dt.PendenciaDt"/>
<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Carga de Processo</title>

      <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css"> 
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      <script type='text/javascript' src='./js/Funcoes.js'></script>
	  <script type='text/javascript' src='./js/jquery.js'></script>      
	  <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
      
 	   <script type="text/javascript">
		 function printpage(objeto) {
  			objeto.style.display ="none";
			window.print();
  			
  		 }
		</script>
</head>
<body>
  <div  id="divCorpo" class="divCorpo">
	<div id="divEditar" class="divEditar">
	<fieldset class="fieldEdicaoEscuro">
			<legend>Recibo</legend>
		<table width="100%" border="0" cellspacing="2" cellpadding="2">
		  <tr>
		    <td width="3%">&nbsp;</td>
		    <td width="64%"><div align="center">
		      <table width="100%" border="0" cellspacing="2" cellpadding="2">
		        <tr>
		          <td width="21%"><div align="center">
		            <input type="image" name="imageField" id="imageField" src="https:\\projudi.tjgo.jus.br\imagens\brasaoGoias.png" />
		          </div></td>
		          <td width="79%"><div align="center">Tribunal de Justiça do Estado de Goiás</div></td>
		        </tr>
		             <tr>
		          <td width="21%">&nbsp;</td>
		          <td width="79%"><div align="center">Recibo de Carga de Processo</div></td>
		        </tr>
		      </table>
		
		</td>
		    <td width="3%"><div align="center">
		      <input type="button" name="imprimir" value="Imprimir" onclick="printpage(this)">
		    </div></td>
		  </tr>
		  <tr>
		    <td width="3%">&nbsp;</td>
		    <td><table width="100%" border="0" cellspacing="2" cellpadding="2">
		      <tr>
		        <td width="23%"><div align="right">Processo: </td></div>
		        <td width="1%">&nbsp;</td>
		        <%if (Pendenciadt.getProcessoDt() != null) { %>
		        	<td width="76%"><a href="BuscaProcesso?Id_Processo=<%=Pendenciadt.getId_Processo()%>"><%=Pendenciadt.getProcessoDt().getProcessoNumeroCompleto()%></a></td>
		        <%} %>
		      </tr>
		      <tr>
		        <td><div width="23%%" align="right">Retirado por:</td></div>
		        <td width="1%">&nbsp;</td>
		        <td width="76%"><%=Pendenciadt.getNomeUsuarioCadastrador()+" - "+Pendenciadt.getServentiaCadastrador()%></td>
		      </tr>
		      <tr>
		        <td><div align="right">CPF:</td></div>
		        <td width="1%">&nbsp;</td>
		        <td width="76%"><%=Pendenciadt.getUsuarioCadastrador()%></td>
		      </tr>
		      <tr>
		        <td><div align="right">Data Retirada:</td></div> 
		         <td width="1%">&nbsp;</td>
		        <td width="76%"><%=Pendenciadt.getDataFim()%></td>
		      </tr>
		      <tr>
		        <td><div align="right">Entregue por:</td></div> 
		         <td width="1%">&nbsp;</td>
		        <td width="76%"><%=Pendenciadt.getNomeUsuarioFinalizador()%></td>
		      </tr>
		      <tr>
		        <td><div align="right">Data para Devolução:</td></div>
		         <td width="1%">&nbsp;</td>
		        <td width="76%"><%=Pendenciadt.getDataLimite()%></td>
		      </tr>
		            <tr>
		        <td>&nbsp;</td>
		        <td>&nbsp;</td>
		        <td>&nbsp;</td>
		      </tr>
		            <tr>
		        <td>&nbsp;</td>
		        <td>&nbsp;</td>
		        <td>&nbsp;</td>
		      </tr>
		            <tr>
		        <td>&nbsp;</td>
		        <td>&nbsp;</td>
		        <td>&nbsp;</td>
		      </tr>
		            <tr>
		        <td height="200" colspan="3"><div align="center">
		          <p>_____________________________________________________</p>
		          <p><%=Pendenciadt.getNomeUsuarioCadastrador()%></p>
		        </div></td>
		        </tr>
		    </table></td>
		    <td width="3%">&nbsp;</td>
		  </tr>
		  <tr>
		    <td width="3%">&nbsp;</td>
		    <td><div align="center"></div></td>
		    <td width="3%">&nbsp;</td>
		  </tr>
		</table>
		<%@include file="Padroes/Mensagens.jspf"%>
	</fieldset>
	</div>
 </div>
</body>
</html>
