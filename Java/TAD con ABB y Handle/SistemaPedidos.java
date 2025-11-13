public class SistemaPedidos {
    private ABB<Pedido> pedidosPorId;
    private ListaEnlazada<Pedido> pedidosPorLlegada;

    public SistemaPedidos() {
        this.pedidosPorId = new ABB<>();
        this.pedidosPorLlegada = new ListaEnlazada<>();
    }

    public void agregarPedido(Pedido pedido) {
        // Agregar a la lista por orden de llegada (al final)
        pedidosPorLlegada.agregarAtras(pedido);
        
        // Agregar al árbol por ID
        pedidosPorId.insertar(pedido);
    }

    public Pedido proximoPedido() {
        // El próximo pedido es el primero que llegó (FIFO)
        if (pedidosPorLlegada.longitud() == 0) {
            return null;
        }
        
        Pedido proximo = pedidosPorLlegada.obtener(0);
        // Eliminar de ambas estructuras
        pedidosPorLlegada.eliminar(0);
        pedidosPorId.eliminar(proximo);
        
        return proximo;
    }

    public Pedido pedidoMenorId() {
        // El pedido con menor ID es el mínimo en el árbol
        return pedidosPorId.minimo();
    }

    public String obtenerPedidosEnOrdenDeLlegada() {
        return pedidosPorLlegada.toString();
    }

    public String obtenerPedidosOrdenadosPorId() {
        return pedidosPorId.toString();
    }
}