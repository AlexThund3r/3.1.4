document.addEventListener('DOMContentLoaded', function () {

    // Функция для получения всех ролей
    function fetchRoles() {
        fetch('/api/admin/roles')  // Эндпоинт для получения списка ролей
            .then(response => response.json())
            .then(roles => {
                const rolesSelect = document.querySelector('select[name="rolesSelected"]');
                rolesSelect.innerHTML = ''; // Очищаем текущие роли

                roles.forEach(role => {
                    const option = document.createElement('option');
                    option.value = role.id;
                    option.textContent = role.name;
                    rolesSelect.appendChild(option);
                });
            })
            .catch(error => console.error('Ошибка получения ролей:', error));
    }

    // Загружаем роли при загрузке страницы
    fetchRoles();

    // Функция для получения всех пользователей
    function fetchUsers() {
        fetch('/api/users')
            .then(response => response.json())
            .then(users => {
                console.log('Полученные пользователи:', users);  // Логируем полученные данные
                updateUsersTable(users);
            })
            .catch(error => console.error('Ошибка получения пользователей:', error));
    }


    // Функция для обновления таблицы пользователей
    function updateUsersTable(users) {
        const tableBody = document.getElementById('usersTableBody');
        tableBody.innerHTML = ''; // Очищаем таблицу

        console.log('Обновление таблицы с пользователями:', users);  // Логируем, какие данные обрабатываются

        users.forEach(user => {
            const row = document.createElement('tr');
            row.innerHTML = `
            <td>${user.id}</td>
            <td>${user.firstName}</td>
            <td>${user.lastName}</td>
            <td>${user.age}</td>
            <td>${user.email}</td>
            <td>${user.roles.map(role => role.name).join(', ')}</td>
            <td>
                <button class="btn btn-info btn-sm edit-btn" data-bs-toggle="modal" data-bs-target="#editModal-${user.id}" data-id="${user.id}">Edit</button>
            </td>
            <td>
                <button class="btn btn-danger btn-sm delete-btn" data-id="${user.id}">Delete</button>
            </td>
        `;
            tableBody.appendChild(row);
        });
    }


    // Функция для добавления нового пользователя
    const addUserForm = document.getElementById('addUserForm');
    if (addUserForm) {
        addUserForm.addEventListener('submit', function (event) {
            event.preventDefault();

            const newUser = {
                firstName: addUserForm.querySelector('input[name="firstName"]').value,
                lastName: addUserForm.querySelector('input[name="lastName"]').value,
                age: addUserForm.querySelector('input[name="age"]').value,
                email: addUserForm.querySelector('input[name="email"]').value,
                password: addUserForm.querySelector('input[name="password"]').value,
                roles: Array.from(addUserForm.querySelectorAll('select[name="rolesSelected"] option:checked'))
                    .map(option => option.value)
            };

            fetch('/api/users', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(newUser)
            })
                .then(response => response.json())
                .then(user => {
                    alert('Пользователь добавлен');
                    fetchUsers();  // Обновляем таблицу пользователей
                })
                .catch(error => console.error('Ошибка добавления пользователя:', error));
        });
    }

// Обработчик для удаления пользователя
    document.querySelectorAll('.delete-btn').forEach(button => {
        button.addEventListener('click', function (event) {
            const userId = button.getAttribute('data-id');
            if (confirm('Вы уверены, что хотите удалить этого пользователя?')) {
                fetch(`/api/admin/users/${userId}`, {
                    method: 'DELETE'
                })
                    .then(response => {
                        if (response.ok) {
                            alert('Пользователь удалён');
                            fetchUsers();  // Обновляем список пользователей
                        } else {
                            alert('Ошибка удаления');
                        }
                    })
                    .catch(error => console.error('Ошибка удаления:', error));
            }
        });
    });


    // Инициализация таблицы пользователей при загрузке страницы
    fetchUsers();
});
