document.addEventListener('DOMContentLoaded', function () {
    let allRoles = []; // Храним все доступные роли

    // Получение всех ролей
    function fetchRoles() {
        fetch('/api/admin/roles')
            .then(response => response.json())
            .then(roles => {
                allRoles = roles;
                updateRoleSelectOptions();
            })
            .catch(error => console.error('Ошибка получения ролей:', error));
    }

    // Обновление выпадающих списков ролей
    function updateRoleSelectOptions(selectedRoleIds = []) {
        const selects = document.querySelectorAll('select[name="rolesSelected"]');
        selects.forEach(select => {
            select.innerHTML = '';
            allRoles.forEach(role => {
                const option = document.createElement('option');
                option.value = role.id;
                option.textContent = role.name;
                option.selected = selectedRoleIds.includes(role.id.toString());
                select.appendChild(option);
            });
        });
    }

    // Получение всех пользователей
    function fetchUsers() {
        fetch('/api/admin/users')
            .then(response => response.json())
            .then(users => updateUsersTable(users))
            .catch(error => console.error('Ошибка загрузки пользователей:', error));
    }

    // Обновление таблицы пользователей
    function updateUsersTable(users) {
        const tableBody = document.getElementById('usersTableBody');
        tableBody.innerHTML = '';

        users.forEach(user => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${user.id}</td>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>${user.roles.map(r => r.name).join(', ')}</td>
                <td>
                    <button class="btn btn-info btn-sm edit-btn" 
                            data-id="${user.id}" 
                            data-bs-toggle="modal" 
                            data-bs-target="#editModal">Edit</button>
                </td>
                <td>
                    <button class="btn btn-danger btn-sm delete-btn" data-id="${user.id}">Delete</button>
                </td>`;
            tableBody.appendChild(row);
        });

        // Обработчики событий для кнопок "Edit"
        document.querySelectorAll('.edit-btn').forEach(button => {
            button.addEventListener('click', function () {
                const userId = this.getAttribute('data-id');
                fetch(`/api/admin/users/${userId}`)
                    .then(response => response.json())
                    .then(userData => fillEditForm(userData))
                    .catch(error => console.error('Ошибка получения данных пользователя:', error));
            });
        });

        // Обработчики событий для кнопок "Delete"
        document.querySelectorAll('.delete-btn').forEach(button => {
            button.addEventListener('click', function () {
                const userId = this.getAttribute('data-id');
                if (confirm('Вы уверены, что хотите удалить этого пользователя?')) {
                    fetch(`/api/admin/users/${userId}`, { method: 'DELETE' })
                        .then(response => {
                            if (response.ok) {
                                alert('Пользователь удалён');
                                fetchUsers();
                            } else {
                                alert('Ошибка удаления');
                            }
                        })
                        .catch(error => console.error('Ошибка при удалении:', error));
                }
            });
        });
    }

    // Заполнение формы редактирования
    function fillEditForm(user) {
        const form = document.getElementById('editUserForm');
        if (!form) {
            console.error("Форма 'editUserForm' не найдена");
            return;
        }

        form.querySelector('input[name="firstName"]').value = user.firstName || '';
        form.querySelector('input[name="lastName"]').value = user.lastName || '';
        form.querySelector('input[name="age"]').value = user.age || '';
        form.querySelector('input[name="email"]').value = user.email || '';
        form.querySelector('input[name="password"]').value = '';
        form.querySelector('input[name="userId"]').value = user.id || '';

        const selectedRoleIds = user.roles ? user.roles.map(r => r.id.toString()) : [];
        updateRoleSelectOptions(selectedRoleIds);
    }

    // Обработчик кнопки Save Changes
    const saveBtn = document.getElementById('saveChangesBtn');
    if (saveBtn) {
        saveBtn.addEventListener('click', function () {
            const form = document.getElementById('editUserForm');
            if (!form) {
                console.error("Форма 'editUserForm' не найдена");
                return;
            }

            const userId = form.querySelector('input[name="userId"]').value;

            const dto = {
                id: parseInt(userId),
                firstName: form.querySelector('input[name="firstName"]').value.trim(),
                lastName: form.querySelector('input[name="lastName"]').value.trim(),
                age: parseInt(form.querySelector('input[name="age"]').value),
                email: form.querySelector('input[name="email"]').value.trim(),
                password: form.querySelector('input[name="password"]').value.trim(),
                rolesSelected: Array.from(
                    form.querySelectorAll('select[name="rolesSelected"] option:checked')
                ).map(option => parseInt(option.value))
            };

            const modalElement = document.getElementById('editModal');
            const modal = bootstrap.Modal.getInstance(modalElement);

            fetch(`/api/admin/users/${userId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(dto)
            })
                .then(response => {
                    if (!response.ok) throw new Error(`HTTP ошибка: ${response.status}`);
                    return response.json();
                })
                .then(() => {
                    alert('Пользователь успешно обновлён');
                    fetchUsers(); // Обновляем список пользователей
                    if (modal) modal.hide();
                })
                .catch(error => {
                    console.error('Ошибка:', error);
                    alert('Ошибка при обновлении пользователя');
                    fetchUsers(); // Всё равно обновляем список
                    if (modal) modal.hide();
                });
        });
    }

    // Обработчик формы создания нового пользователя
    const addUserForm = document.getElementById('addUserForm');
    if (addUserForm) {
        addUserForm.addEventListener('submit', function (event) {
            event.preventDefault();

            const dto = {
                firstName: this.querySelector('input[name="firstName"]').value.trim(),
                lastName: this.querySelector('input[name="lastName"]').value.trim(),
                age: parseInt(this.querySelector('input[name="age"]').value),
                email: this.querySelector('input[name="email"]').value.trim(),
                password: this.querySelector('input[name="password"]').value.trim(),
                rolesSelected: Array.from(
                    this.querySelectorAll('select[name="rolesSelected"] option:checked')
                ).map(option => parseInt(option.value))
            };

            fetch('/api/admin/users', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(dto)
            })
                .then(response => {
                    if (!response.ok) throw new Error('Ошибка сервера');
                    return response.json();
                })
                .then(() => {
                    alert('Пользователь создан');
                    fetchUsers();
                    this.reset(); // Очистка формы
                })
                .catch(error => {
                    console.error('Ошибка:', error);
                    alert('Ошибка при создании пользователя');
                    fetchUsers();
                });
        });
    }

    // Инициализация
    fetchRoles();
    fetchUsers();
});