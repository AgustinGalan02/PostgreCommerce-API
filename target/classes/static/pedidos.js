document.addEventListener("DOMContentLoaded", function() {

    function formatDate(dateStr) {
        const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit' };
        const formattedDate = new Date(dateStr).toLocaleDateString('es-ES', options);
        return formattedDate;
    }

    // LISTADO PEDIDOS
const list = () => {
    $.ajax({
        url: 'http://localhost:8080/pedidos/todos',
        type: 'GET',
        dataType: 'json',
        success: function(pedidos) {
            let data = '';

            pedidos.forEach(pedido => {
                const formattedDate = formatDate(pedido.fecha_pedido);
                let productosHtml = '';
                let precioTotal = 0;

                pedido.productos.forEach(producto => {
                    const productoNombre = producto.nombre;
                    const productoPrecio = producto.precio;
                    productosHtml += `${productoNombre} - Precio: ${productoPrecio}<br>`;
                    precioTotal += productoPrecio; // Suma el precio del producto al precio total
                });

                if (pedido.productos.length === 0) {
                    productosHtml = "Producto/s eliminado/s";
                }

                data += `
                    <tr pedidoId=${pedido.id}>
                        <td>${pedido.id}</td>
                        <td>${pedido.estado_pedido}</td>
                        <td>${formattedDate}</td>
                        <td>${productosHtml}</td>
                        <td>Precio Total: ${precioTotal}</td>
                    </tr>
                `;
            });

                $('#tbody').html(data);
            }
        });
    }

    list();
});
