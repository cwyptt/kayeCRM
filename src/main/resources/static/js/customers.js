// resources/static/js/utils/customers.js
function showSuccessToast(message) {
    const toastEl = document.getElementById('successToast');
    const toast = new bootstrap.Toast(toastEl);
    toastEl.querySelector('.toast-body').textContent = message;
    toast.show();
}

function showErrorToast(message) {
    const toastEl = document.getElementById('errorToast');
    const toast = new bootstrap.Toast(toastEl);
    toastEl.querySelector('.toast-body').textContent = message;
    toast.show();
}

function deleteCustomer(button) {
    if (confirm('Are you sure you want to delete this customer?')) {
        const customerId = button.getAttribute('data-customer-id');

        fetch(`/api/v1/customers/${customerId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(async response => {
                const data = await response.json().catch(() => null);

                if (response.ok) {
                    button.closest('tr').remove();
                    showSuccessToast('Customer deleted successfully');
                } else if (response.status === 409) {
                    // Handle the case where customer has contacts
                    showErrorToast('This customer cannot be deleted because they have associated contacts. Please delete or reassign all contacts first.');
                } else {
                    showErrorToast(data?.message || 'Error deleting customer');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showErrorToast('An unexpected error occurred while deleting the customer');
            });
    }
}