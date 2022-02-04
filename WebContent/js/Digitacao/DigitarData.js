//Fun��o para mascarar data no formato dd/mm/aaaa

function mascara_data(obj){ 

	var dataMascarada = ''; 
	data = obj.value;
    dataMascarada = dataMascarada + data; 
    
    if (dataMascarada.length == 2){ 
    	dataMascarada = dataMascarada + '/'; 
        obj.value = dataMascarada;
	} 
    
    if (dataMascarada.length == 5){ 
    	dataMascarada = dataMascarada + '/'; 
		obj.value = dataMascarada;
   	} 
} 

//Fun��o para validar data           

function verifica_data (data) { 
	if(!verifica_data_string(data.value)) {
		alert("Data invalida! Formato correto: dd/MM/yyyy"); 
		obj = data;
		setTimeout("obj.focus()",1);
		obj.select();
		return false;
	}
	
	return true;	
}

function verifica_data_string (data) { 
	dia = (data.substring(0,2)); 
    mes = (data.substring(3,5)); 
    ano = (data.substring(6,10)); 

    situacao = ""; 
    //verifica se o formato da data � dd/MM/yyyy
    if(dia.length < 2 || mes.length < 2 || ano.length < 4) {
    	situacao = "falsa";
    }    	
    
    //verifica o dia valido para cada mes 
    if ((dia < 1)||(dia < 1 || dia > 30) && (  mes == 4 || mes == 6 || mes == 9 || mes == 11 ) || dia > 31) { 
        situacao = "falsa"; 
    } 

	//verifica se o mes e valido 
    if (mes < 1 || mes > 12 ) { 
    	situacao = "falsa"; 
    } 

    //verifica se e ano bissexto 
    if (mes == 2 && ( dia < 1 || dia > 29 || ( dia > 28 && (parseInt(ano / 4) != ano / 4)))) { 
    	situacao = "falsa"; 
   	} 

   	if ((situacao == "falsa") && (data != "")) { 
    	return false;
   	}
   	return true; 
}