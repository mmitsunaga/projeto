package br.gov.go.tj.utils.pdf;

import net.sf.jasperreports.engine.JRException;

/*
 * To change this template, choose Tools | Templates
 * AND open the template in the editor.
 */

/**
 *
 * @author jrcorrea
 */
public interface ImprimirJasper {
    public Object getAtributo(String nome)throws JRException;

}
