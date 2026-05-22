// Получение CSRF токена из cookie
function getCsrfToken() {
    const name = 'XSRF-TOKEN';
    const cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
        const cookie = cookies[i].trim();
        if (cookie.startsWith(name + '=')) {
            return decodeURIComponent(cookie.substring(name.length + 1));
        }
    }
    return '';
}

// Заголовки для AJAX запросов
function getHeaders() {
    const headers = {
        'Content-Type': 'application/json'
    };
    const token = getCsrfToken();
    if (token) {
        headers['X-XSRF-TOKEN'] = token;
    }
    return headers;
}

// Проверка что пользователь залогинен
async function checkAuth() {
    try {
        const response = await fetch('/admin/auth', {
            credentials: 'include'
        });
        if (!response.ok) {
            window.location.href = '/login';
            return false;
        }
        return true;
    } catch (e) {
        window.location.href = '/login';
        return false;
    }
}


// Достает CSRF-токен из cookie XSRF-TOKEN.
// Spring Security требует этот токен для небезопасных запросов: POST/PATCH/DELETE.
function getCsrfToken() {
    const match = document.cookie.match(new RegExp('(^| )XSRF-TOKEN=([^;]+)'))
    return match ? decodeURIComponent(match[2]) : ''
}

// Готовит заголовки для JSON-запросов к backend.
// Content-Type сообщает серверу, что body содержит JSON.
// X-XSRF-TOKEN подтверждает, что запрос отправлен со страницы приложения.
function jsonHeaders() {
    const headers = {
        "Content-Type": "application/json"
    }
    const token = getCsrfToken()
    if (token) {
        headers["X-XSRF-TOKEN"] = token
    }
    return headers
}

// Загружает текущего авторизованного админа и заполняет navbar + вкладку User.
async function getAdminInfo() {
    let temp = await fetch('http://localhost:8080/admin/auth')
    let user = await temp.json()
    let username = user.username
    let roles = user.roles
    getAdmin(user)
    getAdminNavBar({username, roles})

}

// Заполняет верхнюю панель именем пользователя и его ролями.
function getAdminNavBar({username, roles}) {
    let rolesNavBar = ''
    roles.forEach(role => {
        rolesNavBar += role.name.replace('ROLE_', '') + " "
    })
    document.getElementById('headerUsername').innerHTML = username
    document.getElementById('headerUserRoles').innerHTML = rolesNavBar
}

// Заполняет таблицу "About user" во вкладке User для текущего админа.
function getAdmin(user) {
    let roles = ''
    user.roles.forEach(role => {
        roles += role.name.replace('ROLE_', '') + " "
    })
    let temp = ''
    temp +=
        `<tr>
          <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.hobby}</td>
            <td>${roles}</td>
         </tr>`
    document.getElementById('userInfoId').innerHTML = temp
}

// Стартовая загрузка данных текущего пользователя.
void getAdminInfo()

// Загружает всех пользователей и перерисовывает таблицу в админке.
function getUsersTable() {
    const userTable = fetch('http://localhost:8080/admin/table').then(response => response.json())
    userTable.then(users => {
        let res = ''
        for (let user of users) {
            let roles = ''
            user.roles.forEach(role => {
                roles += role.name.replace('ROLE_', '') + " "
            })
            res += `<tr>
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.hobby}</td>
            <td>${roles}</td>
            <td>
                <button type="button" id="ed_btn" class="btn btn-info" data-toggle="modal"
                data-target="#editModal" 
                onclick="editModal(${user.id})">Edit</button>
            </td>
        
            <td>
                <button class="btn btn-danger" data-toggle="modal"
                data-target="#deleteModal" 
                onclick="deleteModal(${user.id})">Delete</button>
            </td>
        </tr>`
        }
        document.getElementById('tableUsers').innerHTML = res
    })
}

getUsersTable()

// Открытие edit modal: загружаем пользователя по id и подставляем данные в форму.
function editModal(id) {

    fetch('http://localhost:8080/admin/' + id).then(res => {
        res.json().then(userEdit => {
            document.getElementById('editId').value = userEdit.id
            document.getElementById('editName').value = userEdit.username
            document.getElementById('editHobby').value = userEdit.hobby
        })
    })
}

// Отправка формы редактирования пользователя.
document.getElementById('modalEditId').addEventListener('submit', (event) => {
    // Отменяем обычную отправку формы, чтобы страница не перезагружалась.
    event.preventDefault()
    let roles = $("#rolesEdit").val()
    // select возвращает строки "1"/"2", а backend ожидает массив объектов ролей.
    for (let i = 0; i < roles.length; i++) {
        if (roles[i] === '2') {
            roles[i] = {
                'id': 2,
                'name': 'ROLE_ADMIN',
                "authority": "ROLE_ADMIN"
            }
        }
        if (roles[i] === '1') {
            roles[i] = {
                'id': 1,
                'name': 'ROLE_USER',
                "authority": "ROLE_USER"
            }
        }
    }

    // PATCH обновляет существующего пользователя.
    fetch('http://localhost:8080/admin/' + document.getElementById('editId').value + '/edit', {
        credentials: 'include',
        method: 'PATCH',
        headers: jsonHeaders(),
        body: JSON.stringify({
            id: document.getElementById('editId').value,
            username: document.getElementById('editName').value,
            hobby: document.getElementById('editHobby').value,
            password: document.getElementById('editPassword').value,
            roles: roles
        })
    }).then(() => {
        // После успешного ответа закрываем modal и обновляем таблицу.
        $('#editModal').modal('hide')
        getUsersTable()
    })
})

// Открытие delete modal: показываем данные пользователя, которого собираемся удалить.
function deleteModal(id) {
    fetch('http://localhost:8080/admin/' + id).then(res => {
        res.json().then(userDelete => {
            document.getElementById('deleteId').value = userDelete.id
            document.getElementById('deleteName').value = userDelete.username
            document.getElementById('deleteHobby').value = userDelete.hobby
            document.getElementById('rolesDelete').innerHTML = ''
            userDelete.roles.forEach(role => {
                const option = document.createElement('option')
                option.text = role.name
                document.getElementById('rolesDelete').add(option)
            })
        })
    })
}

// Подтверждение удаления пользователя.
document.getElementById('deleteModalId').addEventListener('submit', (event) => {
    event.preventDefault()
    // DELETE удаляет пользователя на backend.
    fetch('http://localhost:8080/admin/' + document.getElementById('deleteId').value + '/delete', {
        credentials: 'include',
        method: 'DELETE',
        headers: jsonHeaders(),

    })
        .then(() => {
            // Закрываем modal и заново загружаем таблицу.
            $('#deleteModal').modal('hide')
            getUsersTable()
        })
})

// selectedOptions содержит выбранные роли в форме создания пользователя.
const rolesNew = document.getElementById('rolesNew').selectedOptions

// Создание нового пользователя из формы Add new user.
document.getElementById('newUserFormId').addEventListener('submit', (event) => {
    event.preventDefault()
    let newRoles = []
    // Backend достаточно получить id ролей, дальше UserService найдет роли в БД.
    for (let i = 0; i < rolesNew.length; i++) {
        newRoles.push({
            id: rolesNew[i].value
        })
    }

    // POST создает новую запись пользователя.
    fetch('http://localhost:8080/admin/create', {
        credentials: 'include',
        method: 'POST',
        headers: jsonHeaders(),
        body: JSON.stringify({
            username: document.getElementById('newName').value,
            hobby: document.getElementById('newHobby').value,
            password: document.getElementById('newPassword').value,
            roles: newRoles
        })
    }).then(() => {
        // После создания возвращаемся на таблицу, очищаем форму и обновляем данные.
        document.getElementById('usersTableTab').click()
        document.getElementById('newUserFormId').reset()
        getUsersTable()
    })
})

// Скачивание Excel из вкладки User в админке.
// Endpoint /user/franchises доступен и админу, потому что ADMIN имеет доступ к /user/**.
document.getElementById('adminDownloadExcelBtn').addEventListener('click', async () => {
    const year = document.getElementById('adminYearInput').value

    const response = await fetch(`/user/franchises?year=${encodeURIComponent(year)}`, {
        method: 'GET'
    })

    if (!response.ok) {
        alert('Failed to download Excel')
        return
    }

    // Excel-файл приходит как Blob, затем скачивается через временную ссылку.
    const blob = await response.blob()
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')

    a.href = url
    a.download = `franchises-${year}.xlsx`

    document.body.appendChild(a)
    a.click()

    a.remove()
    window.URL.revokeObjectURL(url)
})