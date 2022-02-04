/**
 * @Criado por tamaralsantos
 */

$(document).ready(function() {
   	if ($("#grauProcesso1").prop("checked")) {
   		if($('#tipoProcessoCivel').prop('checked')){
   			if($('#comAssistencia').prop('checked')){
   				AlterarAction('Formulario','ProcessoCivel');
   				AlterarValue('custaTipo','2');
   				if($('#comDependencia').prop('checked')){
   					Mostrar('divProcessoDependente');
   				} else if($('#semDependencia').prop('checked')){
   					Ocultar('divProcessoDependente');
   				}
    		} else if($('#semAssistencia').prop('checked')){
    			AlterarAction('Formulario','ProcessoCivelSemAssistencia');
    			mostrarVarios('divGuiaInicial','divNatureza');
    			AlterarValue('custaTipo','1');
	    	} else if($('#isentoAssistencia').prop('checked')){
   				AlterarAction('Formulario','ProcessoCivel');
   				AlterarValue('custaTipo','3');
   				if($('#comDependencia').prop('checked')){
   					Mostrar('divProcessoDependente');
   				} else if($('#semDependencia').prop('checked')){
   					Ocultar('divProcessoDependente');
   				}
    		}
   			Ocultar('imaLocalizarParteVitimas');
   			Ocultar('lblVitima');
   			Ocultar('divTco');
			Mostrar('divValor');
			Mostrar('divProcesso100');
   		} else if($('#tipoProcessoCriminal').prop('checked')){
   			if($('#comAssistencia').prop('checked')){
   				AlterarAction('Formulario','ProcessoCriminal');
   				AlterarValue('custaTipo','2');
   				if($('#comDependencia').prop('checked')){
   					Mostrar('divProcessoDependente');
   				} else if($('#semDependencia').prop('checked')){
   					Ocultar('divProcessoDependente');
   				}
    		} else if($('#semAssistencia').prop('checked')){
    			AlterarAction('Formulario','ProcessoCriminalSemAssistencia');
    			mostrarVarios('divGuiaInicial','divNatureza');
    			AlterarValue('custaTipo','1');
	    	} else if($('#isentoAssistencia').prop('checked')){
   				AlterarAction('Formulario','ProcessoCriminal');
   				AlterarValue('custaTipo','3');
   				if($('#comDependencia').prop('checked')){
   					Mostrar('divProcessoDependente');
   				} else if($('#semDependencia').prop('checked')){
   					Ocultar('divProcessoDependente');
   				}
    		}
   			MostrarInline('imaLocalizarParteVitimas');
   			MostrarInline('lblVitima');
   			Mostrar('divTco');
			Ocultar('divValor');
			Ocultar('divProcesso100');
   		}
	} else if ($("#grauProcesso2").prop("checked")) {
		if($('#tipoProcessoCivel').prop('checked')){
			if($('#comAssistencia').prop('checked')){
				AlterarAction('Formulario','ProcessoSegundoGrauCivel');
				AlterarValue('custaTipo','2');
   				if($('#comDependencia').prop('checked')){
   					Mostrar('divProcessoDependente');
   				} else if($('#semDependencia').prop('checked')){
   					Ocultar('divProcessoDependente');
   				}
    		} else if($('#semAssistencia').prop('checked')){
    			AlterarAction('Formulario','ProcessoSegundoGrauCivelSemAssistenciaCt');
    			mostrarVarios('divGuiaInicial','divNatureza');
    			AlterarValue('custaTipo','1');
	    	} else if($('#isentoAssistencia').prop('checked')){
				AlterarAction('Formulario','ProcessoSegundoGrauCivel');
				AlterarValue('custaTipo','3');
   				if($('#comDependencia').prop('checked')){
   					Mostrar('divProcessoDependente');
   				} else if($('#semDependencia').prop('checked')){
   					Ocultar('divProcessoDependente');
   				}
    		}
			Ocultar('imaLocalizarParteVitimas');
			Ocultar('lblVitima');
			Ocultar('divTco');
			Mostrar('divValor');
			Ocultar('divProcesso100');
			
    	} else if($('#tipoProcessoCriminal').prop('checked')){
    		if($('#comAssistencia').prop('checked')){
    			AlterarAction('Formulario','ProcessoSegundoGrauCriminal');
    			AlterarValue('custaTipo','2');
   				if($('#comDependencia').prop('checked')){
   					Mostrar('divProcessoDependente');
   				} else if($('#semDependencia').prop('checked')){
   					Ocultar('divProcessoDependente');
   				}
    		} else if($('#semAssistencia').prop('checked')){
    			AlterarAction('Formulario','ProcessoSegundoGrauCriminalSemAssistencia');
    			mostrarVarios('divGuiaInicial','divNatureza');
    			AlterarValue('custaTipo','1');
	    	} else if($('#isentoAssistencia').prop('checked')){
    			AlterarAction('Formulario','ProcessoSegundoGrauCriminal');
    			AlterarValue('custaTipo','3');
   				if($('#comDependencia').prop('checked')){
   					Mostrar('divProcessoDependente');
   				} else if($('#semDependencia').prop('checked')){
   					Ocultar('divProcessoDependente');
   				}
    		}
    		MostrarInline('imaLocalizarParteVitimas');
    		MostrarInline('lblVitima');
    		Mostrar('divTco');
			Ocultar('divValor');
			Ocultar('divProcesso100');
	    }
	} else if ($("#grauProcesso3").prop("checked")) {
		if($('#tipoProcessoCivel').prop('checked')){
			if($('#comAssistencia').prop('checked')){
				AlterarAction('Formulario','ProcessoSegundoGrauCivel');
				AlterarValue('custaTipo','2');
   				if($('#comDependencia').prop('checked')){
   					Mostrar('divProcessoDependente');
   				} else if($('#semDependencia').prop('checked')){
   					Ocultar('divProcessoDependente');
   				}
    		} else if($('#semAssistencia').prop('checked')){
    			AlterarAction('Formulario','ProcessoSegundoGrauCivelSemAssistenciaCt');
    			mostrarVarios('divGuiaInicial','divNatureza');
    			AlterarValue('custaTipo','1');
	    	} else if($('#isentoAssistencia').prop('checked')){
				AlterarAction('Formulario','ProcessoSegundoGrauCivel');
				AlterarValue('custaTipo','3');
   				if($('#comDependencia').prop('checked')){
   					Mostrar('divProcessoDependente');
   				} else if($('#semDependencia').prop('checked')){
   					Ocultar('divProcessoDependente');
   				}
    		}
			Ocultar('imaLocalizarParteVitimas');
			Ocultar('lblVitima');
			Ocultar('divTco');
			Mostrar('divValor');
			Ocultar('divProcesso100');
			
    	} else if($('#tipoProcessoCriminal').prop('checked')){
    		if($('#comAssistencia').prop('checked')){
    			AlterarAction('Formulario','ProcessoSegundoGrauCriminal');
    			AlterarValue('custaTipo','2');
   				if($('#comDependencia').prop('checked')){
   					Mostrar('divProcessoDependente');
   				} else if($('#semDependencia').prop('checked')){
   					Ocultar('divProcessoDependente');
   				}
    		} else if($('#semAssistencia').prop('checked')){
    			AlterarAction('Formulario','ProcessoSegundoGrauCriminalSemAssistencia');
    			mostrarVarios('divGuiaInicial','divNatureza');
    			AlterarValue('custaTipo','1');
	    	} else if($('#isentoAssistencia').prop('checked')){
    			AlterarAction('Formulario','ProcessoSegundoGrauCriminal');
    			AlterarValue('custaTipo','3');
   				if($('#comDependencia').prop('checked')){
   					Mostrar('divProcessoDependente');
   				} else if($('#semDependencia').prop('checked')){
   					Ocultar('divProcessoDependente');
   				}
    		}
    		MostrarInline('imaLocalizarParteVitimas');
    		MostrarInline('lblVitima');
    		Mostrar('divTco');
			Ocultar('divValor');
			Ocultar('divProcesso100');
	    }
	}
});

$(document).ready(function() {
	calcularTamanhoIframe();
	
	$('input[type=radio]').change(function() {
	    	limparForm('Formulario');
	    	setarValueNull('Id_Comarca');
	    	setarValueNull('Id_AreaDistribuicao');
	    	validarProcesso100Digital();
	    	calcularTamanhoIframe();
	    	
	    	if($('#grauProcesso2').prop('checked')){
	    		setarComarcaSegundoGrau();
	    	}
	});
	
	//Mostra as Divs se vier marcado.
	if($('input[type=radio][name=grauProcesso]').is(':checked')){
		Mostrar('divTipoProcesso');
	}
	if($('input[type=radio][name=tipoProcesso]').is(':checked')){
		Mostrar('divAssistencia');
	}
	if($('input[type=radio][name=assistenciaProcesso]').is(':checked')){
		if(!$('#semAssistencia').prop('checked')){
			Mostrar('divDependencia');
		}
	}
	
	//////////////////////////////////////////////////////////////////////////
	//Ainda não existe processo criminal sem assistência nem 1º nem 2º grau
	if($('#tipoProcessoCriminal').prop('checked')){
		$('#semAssistencia').prop('disabled', true);
	}
	
	$('input[type=radio][name=tipoProcesso][value="2"]').change(function() {
			$('#semAssistencia').prop('disabled', true);
	});
	
	$('input[type=radio][name=tipoProcesso][value="1"]').change(function() {
		$('#semAssistencia').prop('disabled', false);
	});
	
	//////////////////////////////////////////////////////////////////////////
	//Só mostra Guia Inicial para processo com custas de primeiro grau///////
//	if($('#semAssistencia').prop('checked')){
//		if($('#grauProcesso1').prop('checked')){
//			mostrarVarios('divGuiaInicial','divNatureza');
//		} else if($('#grauProcesso2').prop('checked')){
//			mostrarVarios('divGuiaInicial','divNatureza');
//		} else if($('#grauProcesso3').prop('checked')){
//			mostrarVarios('divGuiaInicial','divNatureza');
//		}
//	}
//	
//	$('input[type=radio][name=assistenciaProcesso][value="2"]').change(function() {
//		if($('#grauProcesso1').prop('checked')){
//			mostrarVarios('divGuiaInicial','divNatureza');
//		} else if($('#grauProcesso2').prop('checked')){
//			mostrarVarios('divGuiaInicial','divNatureza');
//		} else if($('#grauProcesso3').prop('checked')){
//			ocultarVarios('divGuiaInicial','divNatureza');
//		}
//	});
	//////////////////////////////////////////////////////////////////////////
	
    $('input[type=radio][name=assistenciaProcesso][value="1"]').change(function() {
    	AlterarValue('custaTipo','2');
    	if($('#grauProcesso1').prop('checked')){
	    	if($('#tipoProcessoCivel').prop('checked')){
	    		AlterarAction('Formulario','ProcessoCivel');
	    	} else if($('#tipoProcessoCriminal').prop('checked')){
	    		AlterarAction('Formulario','ProcessoCriminal');
		    }
    	} else if($('#grauProcesso2').prop('checked')){
    		if($('#tipoProcessoCivel').prop('checked')){
	    		AlterarAction('Formulario','ProcessoSegundoGrauCivel');
	    	} else if($('#tipoProcessoCriminal').prop('checked')){
	    		AlterarAction('Formulario','ProcessoSegundoGrauCriminal');
		    }
    	} else if($('#grauProcesso3').prop('checked')){
    		if($('#tipoProcessoCivel').prop('checked')){
	    		AlterarAction('Formulario','ProcessoSegundoGrauCivel');
	    	} else if($('#tipoProcessoCriminal').prop('checked')){
	    		AlterarAction('Formulario','ProcessoSegundoGrauCriminal');
		    }
    	}
    });

    $('input[type=radio][name=assistenciaProcesso][value="2"]').change(function() {
    	AlterarValue('custaTipo','1');
    	if($('#grauProcesso1').prop('checked')){
	    	if($('#tipoProcessoCivel').prop('checked')){
	    		AlterarAction('Formulario','ProcessoCivelSemAssistencia');
	    	} else if($('#tipoProcessoCriminal').prop('checked')){
	    		AlterarAction('Formulario','ProcessoCriminalSemAssistencia');
		    }
    	} else if($('#grauProcesso2').prop('checked')){
    		if($('#tipoProcessoCivel').prop('checked')){
    			AlterarAction('Formulario','ProcessoSegundoGrauCivelSemAssistenciaCt');
	    	} else if($('#tipoProcessoCriminal').prop('checked')){
	    		AlterarAction('Formulario','ProcessoSegundoGrauCriminalSemAssistencia');
		    }
    	} else if($('#grauProcesso3').prop('checked')){
    		if($('#tipoProcessoCivel').prop('checked')){
    			AlterarAction('Formulario','ProcessoSegundoGrauCivelSemAssistenciaCt');
	    	} else if($('#tipoProcessoCriminal').prop('checked')){
	    		AlterarAction('Formulario','ProcessoSegundoGrauCriminalSemAssistencia');
		    }
    	}
    });
    
    
    $('input[type=radio][name=assistenciaProcesso][value="3"]').change(function() {
    	AlterarValue('custaTipo','3');
    	if($('#grauProcesso1').prop('checked')){
	    	if($('#tipoProcessoCivel').prop('checked')){
	    		AlterarAction('Formulario','ProcessoCivel');
	    	} else if($('#tipoProcessoCriminal').prop('checked')){
	    		AlterarAction('Formulario','ProcessoCriminal');
		    }
    	} else if($('#grauProcesso2').prop('checked')){
    		if($('#tipoProcessoCivel').prop('checked')){
	    		AlterarAction('Formulario','ProcessoSegundoGrauCivel');
	    	} else if($('#tipoProcessoCriminal').prop('checked')){
	    		AlterarAction('Formulario','ProcessoSegundoGrauCriminal');
		    }
    	} else if($('#grauProcesso3').prop('checked')){
    		if($('#tipoProcessoCivel').prop('checked')){
	    		AlterarAction('Formulario','ProcessoSegundoGrauCivel');
	    	} else if($('#tipoProcessoCriminal').prop('checked')){
	    		AlterarAction('Formulario','ProcessoSegundoGrauCriminal');
		    }
    	}
    });
    
    
    $("#Formulario").on('click', '#adicionarPartePromovente', function () {
    	$('#adicionarPartePromovente').prop('disabled', true);
    });
    
    
    //Se for processo de 2 grau sem custa mostra a Div Dependencia
    $("input[type=radio][name=assistenciaProcesso]").click(function() {
    	if($('#grauProcesso2').prop('checked')){
	    	if($('#semAssistencia').prop('checked')){	
	    		Mostrar('divDependencia');
			}
    	} else if($('#grauProcesso3').prop('checked')){
	    	if($('#semAssistencia').prop('checked')){	
	    		Mostrar('divDependencia');
			}
    	}
    });
    
    
    //Se for processo de 2 grau a Comarca é Goiânia
    $("input[type=radio][name=grauProcesso]").change(function() {
    	if($('#grauProcesso2').prop('checked')){
    		setarComarcaSegundoGrau();
    	} else {
    		removeComarcaSegundoGrau();
    	}
    });
    
    //Se já tiver marcado 2º Grau não mostra a lupinha da Comarca
    if($('input[type=radio][id=grauProcesso2]').is(':checked')){
    	$("#imaLocalizarComarca").hide();
    }

});

function validarProcesso100Digital(){
	$('#grauProcesso1').prop('checked') && $('#tipoProcessoCivel').prop('checked') ? Mostrar('divProcesso100') : Ocultar('divProcesso100');
}

function setarComarcaSegundoGrau() {
	$("#Id_Comarca").val("12");
	$("#Comarca").val("GOI\u00c2NIA");
	$("#imaLocalizarComarca").hide();
}

function removeComarcaSegundoGrau() {
	$("#Id_Comarca").val("");
	$("#Comarca").val("");
	$("#imaLocalizarComarca").show();
}


function verificaDadosNecessarios() {
	//$("#Id_Comarca").val("");
	//$("#Comarca").val("");
	//$("#imaLocalizarComarca").show();
}