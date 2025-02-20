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
                } else {
                    showErrorToast(data?.message || 'Error deleting customer');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showErrorToast('An unexpected error occurred');
            });
    }
}

function deactivateCustomer(customerId) {
    if (confirm('Are you sure you want to deactivate this customer?')) {
        fetch(`/api/v1/customers/${customerId}/deactivate`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    showSuccessToast('Customer deactivated successfully');
                    // Reload the page to show updated status
                    window.location.reload();
                } else {
                    showErrorToast('Error deactivating customer');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showErrorToast('An unexpected error occurred');
            });
    }
}