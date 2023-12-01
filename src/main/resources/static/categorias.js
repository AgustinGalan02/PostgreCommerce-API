document.addEventListener("DOMContentLoaded", function() {

    //LISTADO CATEGORIAS
    const list = () => {

        $.ajax({
            url: 'http://localhost:8080/categorias/todos',
            type: 'GET',
            dataType: 'json',
            success: function(res){
                let data = '';
                res.forEach(element => {
                    data+= `
                        <tr categoriaId = ${element.id} >
                            <td>${element.id}</td>
                            <td>${element.nombre}</td>
                            <td>${element.descripcion}</td>
                        </tr>
                    `
                });

                $('#tbody').html(data);
            }
        })

    }

list();

})