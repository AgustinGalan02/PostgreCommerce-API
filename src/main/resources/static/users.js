document.addEventListener("DOMContentLoaded", function() {

    //LISTADO USUARIOS
    const list = () => {

        $.ajax({
            url: 'http://localhost:8080/users',
            type: 'GET',
            dataType: 'json',
            success: function(res){
                let data = '';
                res.forEach(element => {
                    data+= `
                        <tr userId = ${element.id} >
                            <td>${element.id}</td>
                            <td>${element.email}</td>
                            <td>${element.username}</td>

                        </tr>
                    `
                });

                $('#tbody').html(data);
            }
        })

    }

list();

})