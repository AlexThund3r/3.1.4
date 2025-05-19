document.addEventListener('DOMContentLoaded', function () {
    // Обработчик для модального окна редактирования
    document.querySelectorAll('.edit-btn').forEach(button => {
        button.addEventListener('click', function (event) {
            const button = event.target;
            const tableRow = button.closest('tr');

            // Заполнение формы редактирования данными из строки таблицы
            document.getElementById('firstName').value = tableRow.querySelector('td:nth-child(2)').textContent.trim();
            document.getElementById('lastName').value = tableRow.querySelector('td:nth-child(3)').textContent.trim();
            document.getElementById('age').value = tableRow.querySelector('td:nth-child(4)').textContent.trim();
            document.getElementById('email').value = tableRow.querySelector('td:nth-child(5)').textContent.trim();
            document.getElementById('password').value = ''; // Оставляем пустым, если не меняем пароль

            // Заполнение ролей для редактируемого пользователя
            const rolesString = tableRow.querySelector('td:nth-child(6)').textContent.trim();
            const roles = rolesString.split(',').map(role => role.trim().toUpperCase()); // Преобразуем роли в массив
            const rolesSelect = document.getElementById('roles');
            for (let option of rolesSelect.options) {
                option.selected = roles.includes(option.value); // Выбираем соответствующие роли
            }

            // Установка ID пользователя в скрытое поле для отправки формы
            document.getElementById('userId').value = tableRow.querySelector('td:nth-child(1)').textContent.trim();
        });
    });

    // Обработчик для модального окна удаления
    document.querySelectorAll('.delete-btn').forEach(button => {
        button.addEventListener('click', function (event) {
            const button = event.target;
            const tableRow = button.closest('tr');

            // Заполнение скрытых полей формы для удаления
            document.getElementById('delete-user-id').value = tableRow.querySelector('td:nth-child(1)').textContent.trim();
            document.getElementById('delete-user-firstName').value = tableRow.querySelector('td:nth-child(2)').textContent.trim();
            document.getElementById('delete-user-lastName').value = tableRow.querySelector('td:nth-child(3)').textContent.trim();
            document.getElementById('delete-user-age').value = tableRow.querySelector('td:nth-child(4)').textContent.trim();
            document.getElementById('delete-user-email').value = tableRow.querySelector('td:nth-child(5)').textContent.trim();
            document.getElementById('delete-user-roles').value = tableRow.querySelector('td:nth-child(6)').textContent.trim();

            // Открытие модального окна для удаления
            const modal = new bootstrap.Modal(document.getElementById('deleteModal-' + tableRow.querySelector('td:nth-child(1)').textContent.trim()));
            modal.show();
        });
    });
});
