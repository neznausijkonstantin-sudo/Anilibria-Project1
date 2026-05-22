// Загружает данные текущего авторизованного пользователя с backend.
// Endpoint /user/auth возвращает JSON без пароля: id, username, hobby, roles.
async function getUserInfo() {
    let temp = await fetch('http://localhost:8080/user/auth')
    let user = await temp.json()
    let username = user.username
    let roles = user.roles
    getUser(user)
    getNavBar({username, roles})

}

// Заполняет верхнюю темную панель: имя пользователя и список ролей.
function getNavBar({username, roles}) {
    let rolesNavBar = ''
    roles.forEach(role => {
        rolesNavBar += role.name.replace('ROLE_', '') + " "
    })
    document.getElementById('headerUsername').innerHTML = username
    document.getElementById('headerUserRoles').innerHTML = rolesNavBar
}

// Рисует строку таблицы "About user" на странице /user.
function getUser(user) {
    let rolesUser =''
    user.roles.forEach(role => {
        rolesUser += role.name.replace('ROLE_', '') + " "
    })
    let temp = ''
    temp +=
        `<tr>
          <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.hobby}</td>
            <td>${rolesUser}</td>
         </tr>`
    document.getElementById('userInfoId').innerHTML = temp
}

// Запускаем загрузку пользователя сразу после подключения скрипта.
void getUserInfo()

// Обработчик кнопки Download Excel.
// Берет год из input, вызывает backend и запускает скачивание xlsx-файла.
document.getElementById('downloadExcelBtn').addEventListener('click', async () => {
    const year = document.getElementById('yearInput').value

    // encodeURIComponent защищает URL, если в поле случайно окажутся спецсимволы.
    const response = await fetch(`/user/franchises?year=${encodeURIComponent(year)}`, {
        method: 'GET'
    })

    if (!response.ok) {
        alert('Failed to download Excel')
        return
    }

    // Excel приходит как бинарные данные, поэтому читаем его как Blob.
    const blob = await response.blob()
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')

    // Создаем временную ссылку и программно кликаем по ней, чтобы браузер скачал файл.
    a.href = url
    a.download = `franchises-${year}.xlsx`

    document.body.appendChild(a)
    a.click()

    a.remove()
    // Освобождаем временный URL, чтобы браузер не держал Blob в памяти.
    window.URL.revokeObjectURL(url)
})