document.addEventListener("DOMContentLoaded", function() {


const fillCategoriaDropdown = () => {
    $.ajax({
        url: 'http://localhost:8080/categorias/todos', // Cambia la URL según tu backend
        type: 'GET',
        dataType: 'json',
        success: function(categorias) {
            let options = '';

            categorias.forEach(categoria => {
                options += `<option value="${categoria.id}">${categoria.nombre}</option>`;
            });

            $('#categoria_id').html(options);
        }
    });
}

fillCategoriaDropdown();

    //LISTADO PRODUCTOS
const list = () => {
    $.ajax({
        url: 'http://localhost:8080/productos/todos',
        type: 'GET',
        dataType: 'json',
        success: function(productos) {
            let data = '';

            productos.forEach(producto => {
                data += `
                    <tr productoId = ${producto.id}>
                        <td>${producto.id}</td>
                        <td>${producto.nombre}</td>
                        <td>${producto.descripcion}</td>
                        <td>${producto.precio}</td>
                        <td>${producto.stock}</td>
                        <td>${producto.categoria.nombre}</td>
                    </tr>
                `;
            });

            $('#tbody').html(data);
        }
    });
}

list();

})