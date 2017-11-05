/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.rafaelaznar.bean;

import com.google.gson.annotations.Expose;
import eu.rafaelaznar.bean.specificimplementation.ProductoSpecificBeanImplementation;

/**
 *
 * @author Fernando
 */
public class CarritoBean  {
    
     @Expose
    private Integer cantidad;
    @Expose
    private ProductoSpecificBeanImplementation producto;
    
    public CarritoBean(Integer cantidad, ProductoSpecificBeanImplementation producto) {
        this.cantidad = cantidad;
        this.producto = producto;
    }
   

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public ProductoSpecificBeanImplementation getProducto() {
        return producto;
    }

    public void setProducto(ProductoSpecificBeanImplementation producto) {
        this.producto = producto;
    }
}
