/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.rafaelaznar.service.specificimplementation;


import com.google.gson.Gson;
import eu.rafaelaznar.bean.CarritoBean;
import eu.rafaelaznar.bean.ReplyBean;
import eu.rafaelaznar.bean.specificimplementation.LineapedidoSpecificBeanImplementation;
import eu.rafaelaznar.bean.specificimplementation.PedidoSpecificBeanImplementation;
import eu.rafaelaznar.bean.specificimplementation.ProductoSpecificBeanImplementation;
import eu.rafaelaznar.bean.specificimplementation.UsuarioSpecificBeanImplementation;
import eu.rafaelaznar.connection.ConnectionInterface;
import eu.rafaelaznar.dao.specificimplementation.LineapedidoSpecificDaoImplementation;
import eu.rafaelaznar.dao.specificimplementation.PedidoSpecificDaoImplementation;
import eu.rafaelaznar.dao.specificimplementation.ProductoSpecificDaoImplementation;
import eu.rafaelaznar.helper.AppConfigurationHelper;
import eu.rafaelaznar.helper.Log4jConfigurationHelper;
import eu.rafaelaznar.service.publicinterface.TableServiceCarrito;
import eu.rafaelaznar.service.publicinterface.ViewServiceCarrito;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author a022583952e
 */

public class CarritoService implements TableServiceCarrito, ViewServiceCarrito {

    HttpServletRequest oRequest = null;

    public CarritoService(HttpServletRequest request) {
        oRequest = request;
    }

    private Boolean checkPermission(String strMethodName) throws Exception {
        UsuarioSpecificBeanImplementation oUsuarioBean = (UsuarioSpecificBeanImplementation) oRequest.getSession().getAttribute("user");
        if (oUsuarioBean != null) {
            return true;
        } else {
            return false;
        }
    }

   
    private CarritoBean find(ArrayList<CarritoBean> alCarrito, int id) {
        Iterator<CarritoBean> iterator = alCarrito.iterator();
        while (iterator.hasNext()) {
            CarritoBean oCarrito = iterator.next();
            if (id == (oCarrito.getproducto().getId())) {
                return oCarrito;
            }
        }
        return null;
    }

    @Override
    public ReplyBean add() throws Exception {
        if (this.checkPermission("add")) {
            ArrayList<CarritoBean> alCarrito = (ArrayList) oRequest.getSession().getAttribute("carrito");
            ReplyBean oReplyBean = null;
            CarritoBean oCarritoBean = null;
            int id = Integer.parseInt(oRequest.getParameter("id"));
            int cantidad = Integer.parseInt(oRequest.getParameter("cantidad"));
            Connection oConnection = null;
            ConnectionInterface oPooledConnection = null;
            try {
                oPooledConnection = AppConfigurationHelper.getSourceConnection();
                oConnection = oPooledConnection.newConnection();
                ProductoSpecificBeanImplementation oBean = new ProductoSpecificBeanImplementation(id);
                ProductoSpecificDaoImplementation oDao = new ProductoSpecificDaoImplementation(oConnection, (UsuarioSpecificBeanImplementation) oRequest.getSession().getAttribute("carritoBean"), null);
                oBean = oDao.get(oBean, AppConfigurationHelper.getJsonMsgDepth());
                oCarritoBean = new CarritoBean(cantidad, oBean);
                CarritoBean oCarrito = find(alCarrito, oCarritoBean.getproducto().getId());
                if (oCarrito == null) {
                    CarritoBean oCarroBean = new CarritoBean(cantidad, oBean);
                    alCarrito.add(oCarroBean);
                } else {
                    Integer oldCantidad = oCarrito.getCantidad();
                    oCarrito.setCantidad(oldCantidad + cantidad);
                }
                Gson oGson = AppConfigurationHelper.getGson();
                String strJson = oGson.toJson(alCarrito);
                oReplyBean = new ReplyBean(200, strJson);
            } catch (Exception ex) {
                String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName();
                Log4jConfigurationHelper.errorLog(msg, ex);
                throw new Exception(msg, ex);
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (AppConfigurationHelper.getSourceConnection() != null) {
                    AppConfigurationHelper.getSourceConnection().disposeConnection();
                }
            }
            return oReplyBean;
        } else {
            return new ReplyBean(401, "Unauthorized operation");
        }
    }

    @Override
    public ReplyBean remove() throws Exception {
        if (this.checkPermission("remove")) {
            ArrayList<CarritoBean> alCarrito = (ArrayList) oRequest.getSession().getAttribute("carrito");
            int id = Integer.parseInt(oRequest.getParameter("id"));
            ReplyBean oReplyBean = null;
            Connection oConnection = null;
            ConnectionInterface oPooledConnection = null;
            try {
                oPooledConnection = AppConfigurationHelper.getSourceConnection();
                oConnection = oPooledConnection.newConnection();
                CarritoBean oCarrito = find(alCarrito, id);
                alCarrito.remove(oCarrito);
                Gson oGson = AppConfigurationHelper.getGson();
                String strJson = oGson.toJson(alCarrito);
                oReplyBean = new ReplyBean(200, strJson);
            } catch (Exception ex) {
                String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName();
                Log4jConfigurationHelper.errorLog(msg, ex);
                throw new Exception(msg, ex);
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (AppConfigurationHelper.getSourceConnection() != null) {
                    AppConfigurationHelper.getSourceConnection().disposeConnection();
                }
            }
            return oReplyBean;
        } else {
            return new ReplyBean(401, "Unauthorized operation");
        }
    }

    @Override
    public ReplyBean list() throws Exception {
        if (this.checkPermission("list")) {
            ArrayList<CarritoBean> alCarrito = (ArrayList) oRequest.getSession().getAttribute("carrito");
            ReplyBean oReplyBean = null;
            Connection oConnection = null;
            ConnectionInterface oPooledConnection = null;
            try {
                oPooledConnection = AppConfigurationHelper.getSourceConnection();
                oConnection = oPooledConnection.newConnection();
                Gson oGson = AppConfigurationHelper.getGson();
                String strJson = oGson.toJson(alCarrito);
                oReplyBean = new ReplyBean(200, strJson);
            } catch (Exception ex) {
                String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName();
                Log4jConfigurationHelper.errorLog(msg, ex);
                throw new Exception(msg, ex);
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (AppConfigurationHelper.getSourceConnection() != null) {
                    AppConfigurationHelper.getSourceConnection().disposeConnection();
                }
            }
            return oReplyBean;
        } else {
            return new ReplyBean(401, "Unauthorized operation");
        }
    }

    @Override
    public ReplyBean buy() throws Exception {
        if (this.checkPermission("buy")) {
            ArrayList<CarritoBean> alCarrito = (ArrayList) oRequest.getSession().getAttribute("carrito");
            ReplyBean oReplyBean = null;
            Connection oConnection = null;
            ConnectionInterface oPooledConnection = null;
            Date fecha = new Date(2017 / 10 /27); //Date.valueOf(oRequest.getParameter("fecha"));
            try {
            /* oPooledConnection = AppConfigurationHelper.getSourceConnection();
                oConnection = oPooledConnection.newConnection();
                UsuarioSpecificBeanImplementation oUsuarioBean = (UsuarioSpecificBeanImplementation) oRequest.getSession().getAttribute("user");
                Integer alCarritoSize = alCarrito.size();
                PedidoSpecificBeanImplementation oPedidoBean = new PedidoSpecificBeanImplementation(fecha,oUsuarioBean.getId());
                PedidoSpecificDaoImplementation oPedidoDao = new PedidoSpecificDaoImplementation(oConnection);
                oPedidoBean.setId(oPedidoDao.set(oPedidoBean));
//                oPedidoDao.set(oPedidoBean);
                ProductoSpecificBeanImplementation oProductoBean = null;
                ProductoSpecificDaoImplementation oProductoDao = new ProductoSpecificDaoImplementation(oConnection);
                LineapedidoSpecificDaoImplementation oLineadepedidoDao = new LineapedidoSpecificDaoImplementation(oConnection);
                for (int i = 0; i < alCarritoSize; i++) {
                    oProductoBean = alCarrito.get(i).getproducto();
                    Integer newCantidad = alCarrito.get(i).getCantidad();
                    LineapedidoSpecificBeanImplementation oLineadepedidoBean = new LineapedidoSpecificBeanImplementation();
                    oLineadepedidoBean.setCantidad(newCantidad);
                    oLineadepedidoBean.setId_pedido(oPedidoBean.getId());
                    oLineadepedidoBean.setId_producto(oProductoBean.getId());
                    oLineadepedidoBean.setId(oLineadepedidoDao.set(oLineadepedidoBean));
//                    oLineadepedidoDao.set(oLineadepedidoBean);
                    oProductoBean.setExistencias(oProductoBean.getExistencias() - newCantidad);
                    oProductoDao.set(oProductoBean);
                }
                alCarrito.clear();*/
            } catch (Exception ex) {
                String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName();
                Log4jConfigurationHelper.errorLog(msg, ex);
                throw new Exception(msg, ex);
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (AppConfigurationHelper.getSourceConnection() != null) {
                    AppConfigurationHelper.getSourceConnection().disposeConnection();
                }
            }
            return oReplyBean = new ReplyBean(200, "Compra realizada correctamente");
        } else {
            return new ReplyBean(401, "Unauthorized operation");
        }
    }

    @Override
    public ReplyBean empty() throws Exception {
        if (this.checkPermission("empty")) {
            ArrayList<CarritoBean> alCarrito = (ArrayList) oRequest.getSession().getAttribute("carrito");
            ReplyBean oReplyBean = null;
            Connection oConnection = null;
            ConnectionInterface oPooledConnection = null;
            try {
                oPooledConnection = AppConfigurationHelper.getSourceConnection();
                oConnection = oPooledConnection.newConnection();

                alCarrito.clear();

                Gson oGson = AppConfigurationHelper.getGson();
                String strJson = oGson.toJson(alCarrito);
                oReplyBean = new ReplyBean(200, strJson);

            } catch (Exception ex) {
                String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName();
                Log4jConfigurationHelper.errorLog(msg, ex);
                throw new Exception(msg, ex);
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (AppConfigurationHelper.getSourceConnection() != null) {
                    AppConfigurationHelper.getSourceConnection().disposeConnection();
                }
            }
            return oReplyBean;
        } else {
            return new ReplyBean(401, "Unauthorized operation");
        }
    }

}
