var viewed = -1;

var arraynome = [];
var arrayarquivo = [];	
		
function getTextoAssinar(){						
	return replaceAll($("#conteudoArquivos").attr('value'),"ASPAS_DUPLAS", "\"");	
}
  
function getNomeArquivo(){  			
	return replaceAll($("#nomeArquivos").attr('value'),"ASPAS_DUPLAS", "\"");  			
}
  
function podeAssinar(){			
	return 'true';
}
  
function configuraAssinador(){
	return "";
}
  
function iniciarAssinatura(){	   	   	  
	limparVariaveisLocais();
	AssinarPendencia.limparArquivosAssinados();   		   
}
  		
function limparVariaveisLocais(){
	arraynome=[];
	arrayarquivo=[];			
}
  			  
function incluirArquivoAssinatura(nome, arquivo){
	arraynome.push(nome);
	arrayarquivo.push(arquivo);	  	
}  	
   	
function finalizarAssinatura(){	
				   	 	   	 
	if (arraynome.length>=1){
		// o pop() pega e retira a ultima posição do array							   	 
   		inserirArquivo(arraynome.shift(), arrayarquivo.shift());				   		   
	}else{
		limparVariaveisLocais();				
		$("#divBotaoSalvar").css({'display':'block'});									
  		$("#dialog").dialog("close");		  			  			  			  		
	}		  
}

function inserirArquivo(nomeArquivo, conteudoArquivo) {			
	var dados = { id:viewed, id_ArquivoTipo:null, arquivoTipo:null, nomeArquivo:null, arquivo:null, assinado:true };
	
	dados.nomeArquivo = nomeArquivo;
	dados.arquivo = conteudoArquivo;	  		
			
	url = 'AssinarPendencia?AJAX=ajax&Fluxo=4';
	var boFecharDialog=false;
	
	$.ajax({
		url: encodeURI(url),
		method: "POST",  				
		data: dados,
		context: document.body,		
		timeout: 300000, async: true,
		success: function(retorno){
			finalizarAssinatura();					
		},
		beforeSend: function(data ){
			timer = setTimeout(function() {
				mostrarMensagemConsultando("Inserção de Arquivos", "Aguarde, os arquivos estão sendo inseridos");		
				boFecharDialog=true;
			}, 2500);
		},
		error: function(request, status, error){
			if (error=='timeout'){
				mostrarMensagemErro('Tempo Excedido', "A ação demorou tempo demais, tente novamente mais tarde.");
			}
			boFecharDialog = false;
			finalizarAssinatura();
		},
		complete: function(data ){		
			clearTimeout(timer);
			if (boFecharDialog){
				$("#dialog").dialog('close');
			}
		}
	});	
				
}	
		

		
			  	