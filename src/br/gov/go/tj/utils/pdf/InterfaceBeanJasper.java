package br.gov.go.tj.utils.pdf;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/*
 * To change this template, choose Tools | Templates
 * AND open the template in the editor.
 */

/**
 * 
 * @author lsbernardes
 */
public class InterfaceBeanJasper implements JRDataSource {
	private Object bean;

	private boolean requisitou;

	public InterfaceBeanJasper(Object bean) {
		this.bean = bean;
	}

	public Object getFieldValue(JRField campo) throws JRException {
		Object objRetorno = null;
		try{
			
			objRetorno = this.bean.getClass().getMethod("get" + campo.getName()).invoke(this.bean);
			
	    } catch(Exception e) {
			e.printStackTrace();
		}
		return objRetorno;
	}

	public boolean next() throws JRException {
		if (!requisitou) {
			requisitou = true;
			return true;
		} else {
			return false;
		}
	}

}
