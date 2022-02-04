package br.gov.go.tj.utils.pdf;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/*
 * To change this template, choose Tools | Templates
 * AND open the template in the editor.
 */

/**
 * 
 * @author jrcorrea
 */
public class InterfaceJasper implements JRDataSource {
    private List objLista = null;

    private int inPosicaoLista = -1;

    public InterfaceJasper(List objetos) {
        objLista = objetos;
    }

    public boolean next() throws JRException {
        boolean boTemp = false;
        
        if (objLista!= null && (++inPosicaoLista < objLista.size())) {
            boTemp = true;
        }
        return boTemp;
    }

    public Object getFieldValue(JRField campo) throws JRException {

        Object objTemp = null;
        Object objRetorno =null;
        try{
            objTemp = (Object)objLista.get(inPosicaoLista);
            objRetorno= objTemp.getClass().getMethod("get" + campo.getName()).invoke(objTemp);            
            //if (objRetorno instanceof List) objRetorno =  new JRBeanCollectionDataSource((List)objRetorno);                
            if (objRetorno instanceof List) objRetorno =  new InterfaceJasper((List)objRetorno);
            ////System.out.println("Public method found: " + helloMethod.toString());
            //objTemp = helloMethod.invoke(null,null);
            ////System.out.println(objTemp);
        } catch(Exception e) {
            
            e.printStackTrace();       
        } 

        return objRetorno;
    }

}
