<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	"http://www.w3.org/TR/html4/loose.dtd">
<html>
	
	<head>
	
		<title>Processo Judicial</title>

		<link href="imagens/favicon.ico" TYPE="image/gif" REL="icon">

		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

		<script language="javascript" type="text/javascript" src="./js/default.js"></script>
		<script type="text/javascript" src="./js/tecladoVirtual.js"></script>        
		<script type="text/javascript" src="./js/jquery.js"> </script>
		<script type="text/javascript" src="./js/ui/jquery-ui.min.js"> </script>
		<script type='text/javascript' src='./js/Funcoes.js'></script>	
		<script type="text/javascript" src="./js/jqDnR.js"> </script>     
		
		<link rel="stylesheet" href="css/cupertino/jquery-ui-1.8.1.custom.css" type="text/css" media="all" />
			
		<style type="text/css">

			@import url('./css/Principal.css');
	   		@import url('./css/LogonTJGO.css');
	   		@import url('./css/tecladoVirtual.css');
			
			dt {
				text-align: left;
				font-weight: bold;
			}
			dd {
				margin-left: 10px;
				text-align: left;
				border-bottom: 1px dotted #D7D7D7;		
			}
			#versao_projudi {
				text-align: right;
				font-family: arial;
				font-size: 10px;
				color: #066082;
				padding-right: 10px;
				padding-top: 5px;
			}

			#descricao_servidor {
				text-align: center;
				font-family: arial;
				font-size: 14px;
				color: #ff0000;
				padding-top: 10px;
			}

			#rodape {
				height: 25px;
				font-size: 10px;
				border-top: 1px solid #555555;
				background: #939393;
				color: #f9f9f9;
				text-align: center;
				padding-top: 10px;
				
			}

			#coluna-um, #coluna-dois, #coluna-tres {
				height: 390px;
				vertical-align: top;
			}

			#coluna-um, #coluna-tres {
				width: 323px;
				padding: 2px 2px 2px 2px;
			}
			
			#coluna-um {						
				float: left;
				border-collapse: collapse;
			}
			#coluna-tres {
				float:right;						
			}	
			#coluna-um, #coluna-tres {										
				align: left;
				border-top: 1px solid #ccc;
				border-bottom: 1px solid #ccc;
				border-collapse: collapse;
			}
	
			#coluna-um h2, #coluna-dois h2, #coluna-tres h2 {
				width: 100%;
				height: 20px;
				padding-top: 9px;
				padding-right: 0px;
				padding-bottom: 0px;
				padding-left: 15px;
				background-image: none;
				margin-right: 0px;
				margin-bottom: 0px;
				margin-top: 0px;				
				margin-left: 0px;
				background: #F6F6F6;
				font-weight: normal;
				clear: right;
				text-align: left;
				font-weight: bold;
				font-size: 11px;
				display: block;
				color: #494b49;
			}

			#coluna-um h2 {
				border-bottom-width: medium;
				border-bottom-style: none;
				border-bottom-color: -moz-use-text-color;
				border-top-width: medium;
				border-top-style: none;
				border-top-color: -moz-use-text-color;
				background-image: url("/imagens/bkg_bloco.gif");
				background-repeat: repeat-x;
				background-position: 0pt -1px;
			}
			
			 #coluna-dois a {
				margin-top: 0px;				
				margin-right: 0px;
				margin-bottom: 0px;
				margin-left: 0px;
				padding-top: 0px;
				padding-right: 0px;
				padding-bottom: 0px;
				padding-left: 0px;
			}

			#coluna-um ul, #coluna-dois ul {
				width: 90%;
				padding-top: 0pt;
				padding-right: 0pt;
				padding-bottom: 0pt;
				padding-left: 0pt;
				margin-top: 0px;				
				margin-right: 0px;
				margin-bottom: 25px;
				margin-left: 0px;
				list-style-type: none;
				list-style-image: none;
				list-style-position: outside;
			}

 			#coluna-um ul li {
				line-height: 30px;				
				margin-top: 0px;				
				margin-right: 0px;
				margin-bottom: 0px;
				margin-left: 0px;
				padding-top: 0px;
				padding-right: 0px;
				padding-bottom: 0px;
				padding-left: 0px;
				border-bottom-width: 1px;
				border-bottom-style: dotted;
				border-bottom-color: #d7d7d7;
				background-color: transparent;
				background-image: none;
				background-repeat: repeat;
				background: #ffffff;
				text-align: left;
				padding-left: 12px;
				margin-left: 5px;
				background-color: transparent;
				background-image: url("/imagens/img_caixa.gif");
				background-repeat: no-repeat;
				background-attachment: scroll;
				background-position: 0pt 13px;
			}

 			
			#coluna-dois {
				width: 340px;
				float: left;
				border: 1px solid #ccc;
				border-collapse:collapse;
				background: #FFFFFF;
			}
			
			#coluna-dois h2 {
				background: #FFFFFF;
				width: 90%;
				padding-top: 9px;
			}

			#coluna-dois form {
				font-family: Verdana,Arial,Helvetica,sans-serif;
				font-size: 10px;
				color: #333333;
				width: 90%;
				align:center;
				text-align: center;
				padding-top: 15px;
			}

			#coluna-dois .textinput {
				font-family: Verdana,Arial,Helvetica,sans-serif;
				font-size: 10px;
				color: #333333;
				background-image: url("./imagens/bg_form_extended.gif");
				border: 1px solid #d5d5d5;
				width: 130px;
				height: 16px;
				margin-top: 5px;
				margin-bottom: 5px;
				padding-left:5px;
				padding-right:5px;
				vertical-align: middle;
			}

			#coluna-dois .passwordinput {
				font-family: Verdana,Arial,Helvetica,sans-serif;
				font-size: 10px;
				color: #000000;
				background-color: #ffffcc;
				border: 1px solid #d5d5d5;
				width: 130px;
				height: 16px;
				margin-top: 5px;
				margin-bottom: 5px;
				padding-left:5px;
				padding-right:5px;
				vertical-align: middle;
			}

			#coluna-dois label {
				font-family: Verdana,Arial,Helvetica,sans-serif;
				font-size: 10px;
				color: #333333;
				width: 80px;
				height: 16px;
				margin-top: 5px;
				margin-bottom: 5px;
				padding-right:5px;
				padding-top: 3px;
				float:left;
				text-align: right;
				font-weight: bold;
				vertical-align: bottom;
			}

			#coluna-dois .botao:hover {
				border: 1px solid #ccc;
				background: #d5d5d5;
				color: #333;
			}

			#coluna-um ul li a {
				color: #333333;
				font-weight: normal;
			}
			
 			#coluna-um ul li a:hover {
				color: #333333 !important;
				font-weight: bold;
			}


			#coluna-tres h2 {
				width: 90%;
				padding-top: 5px;
				padding-right: 0px;
				padding-bottom: 0px;
				padding-left: 15px;
				background-color: #f6f6f6;
				background-image: none;
				background-repeat: repeat;
				background-attachment: scroll;
				background-position: 0% 0%;
				letter-spacing: -0.5px;
				font-weight: bolder;
				color: #696969;
				margin-right: 0px;
				margin-bottom: 0px;
				margin-left: 0px;
				background: #F6F6F6;
				clear: right;
				background: #EFFAFF;
			}

			.mensagens {
				height: 100%;
				overflow: auto;
				background: #EFFAFF;
				color: #333;
				/*text-align: justify;*/
			}
			.texto {
				height: auto;
				width: 95%;
				padding: 3px;
				background: #EFFAFF;
				color: #333;
				text-align: justify;
			}				
			.mensagens p {				
				text-align: justify;
			}			
.style2 {
	font-size: 36px;
	color: #FF0000;
	font-weight: bold;
}
        </style>

		<script type="text/javascript">
		
		function testarNavegador(){
			var browserName = navigator.appName;
			
			if ( browserName == "Microsoft Internet Explorer" ) {
								
				$('#dialog').dialog( { modal: true } );
				$('#dialog').dialog( { position: 'center' } );
				$("#dialog").dialog( "option", "title", "Microsoft Internet Explorer - Não suportado");
				$("#dialog").html("Para a utilização do PROJUDI é necessário a Instalação do navegador Mozilla Firefox, pois, o Sistema é homologado somente para este navegador.");
				$('#dialog').dialog( "dialogbeforeclose", function(event, ui) {	modal: false; });
				$("#dialog").dialog();
				return false;
			}		
		}

		$(document).ready(function() {
			
			$("#accordion1").accordion({ autoHeight: false, collapsible: true });
			$("#accordion0").accordion({ autoHeight: false, collapsible: true });				

			<%
				String stErro=(String)request.getAttribute("MensagemErro");
				String stOk =(String)request.getAttribute("MensagemOk");
				if (stErro!=null) stErro = stErro.replace("\n", "<br />"); else stErro="";
				if (stOk!=null) stOk = stOk.replace("\n", "<br />"); else stOk="";
			%>
			
			var mensagemErro = '<%=stErro%>';	 
			var mensagemOk = '<%=stOk%>';							
			var titulo ="Erro na tentativa de entrar no Sistema";	
						
			if (((mensagemErro!='') && (mensagemErro!='null')) || ((mensagemOk!='') && (mensagemOk!='null')) ){
				$('#dialog').dialog( { modal: true } );
				$('#dialog').dialog( { position: 'center' } );
				$("#dialog").dialog( "option", "title", titulo );
				$('#dialog').dialog( "dialogbeforeclose", function(event, ui) {	modal: false; });
				 if ((mensagemErro!='') && (mensagemErro!='null'))	
					$("#dialog").append(mensagemErro);
				 else 	$("#dialog").append(mensagemOk);
					$("#dialog").dialog();
			}
	  								
			//$('#cabecalho').hover(function () {	$("#login").focus(); });		
			//$('#cabecalho').click(function () {	$("#login").focus(); });		
			//$('#rodape').hover(function () { $("#login").focus(); });		
			//$('#rodape').click(function () { $("#login").focus(); });		
			
			$("#login").focus();
							
	});    
	   
			window.onload = function(){			
				mostraTecladoVirtual(); 
				tcvDefinirOpacity(); 												
			};
	
		</script>

	</head>

	<body>
	
	<div align="center" class ='breadcrumb'> 	

    <div id="cabecalho">        
      <div id="bkg_projudi">   </div>			

        <p class="style2">Pedimos Desculpas pelos Transtornos</p>
        <p class="style2">Neste Momento</p>
        <p class="style2">Estamos em Manuten&ccedil;&atilde;o</p>
    </div>   

<div id="dialog" style="display:none">

			<p>A partir de 28/03/2010 o <strong>usu&aacute;rio de acesso</strong> ser&aacute; seu <strong>CPF</strong>. A senha continua a mesma.</p>

		</div>
      
	</div>
</body>
	
</html>
