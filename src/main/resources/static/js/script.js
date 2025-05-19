const modalEditUser = document.getElementById('edit-user-modal');
const modalDeleteUser = document.getElementById('delete-user-modal');

modalEditUser.addEventListener('show.bs.modal', event => {
    const button = event.relatedTarget;
    const tableRow = button.closest('tr');

    const userId = document.getElementById('edited-user-id');
    const userFirstName = document.getElementById('edited-user-firstName');
    const userLastName = document.getElementById('edited-user-lastName');
    const userAge = document.getElementById('edited-user-age');
    const userEmail = document.getElementById('edited-user-email');
    const userPassword = document.getElementById('edited-user-password');
    const userRolesSelect = document.getElementById('edited-user-roles');

    userId.value = tableRow.querySelector('td:nth-child(1)').textContent.trim();
    userFirstName.value = tableRow.querySelector('td:nth-child(2)').textContent.trim();
    userLastName.value = tableRow.querySelector('td:nth-child(3)').textContent.trim();
    userAge.value = tableRow.querySelector('td:nth-child(4)').textContent.trim();
    userEmail.value = tableRow.querySelector('td:nth-child(5)').textContent.trim();
    userPassword.value = '';

    for (let option of userRolesSelect.options) {
        option.selected = false;
    }

    const rolesString = tableRow.querySelector('td:nth-child(6)').textContent.trim();
    const roles = rolesString.split(',').map(r => r.trim().toUpperCase());

    for (let option of userRolesSelect.options) {
        const roleName = option.value.replace('ROLE_', '');
        if (roles.includes(roleName)) {
            option.selected = true;
        }
    }
});

modalDeleteUser.addEventListener('show.bs.modal', event => {
    const button = event.relatedTarget;
    const tableRow = button.closest('tr');

    document.getElementById('delete-user-id').value = tableRow.querySelector('td:nth-child(1)').textContent.trim();
    document.getElementById('delete-user-firstName').value = tableRow.querySelector('td:nth-child(2)').textContent.trim();
    document.getElementById('delete-user-lastName').value = tableRow.querySelector('td:nth-child(3)').textContent.trim();
    document.getElementById('delete-user-age').value = tableRow.querySelector('td:nth-child(4)').textContent.trim();
    document.getElementById('delete-user-email').value = tableRow.querySelector('td:nth-child(5)').textContent.trim();

    const rolesSelectDelete = document.getElementById('delete-user-roles');
    for (let option of rolesSelectDelete.options) {
        option.selected = false;
    }

    const rolesString = tableRow.querySelector('td:nth-child(6)').textContent.trim();
    const roles = rolesString.split(',').map(r => r.trim().toUpperCase());

    for (let option of rolesSelectDelete.options) {
        const roleName = option.value.replace('ROLE_', '');
        if (roles.includes(roleName)) {
            option.selected = true;
        }
    }
});
