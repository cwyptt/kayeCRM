function deleteCompany(button) {
    if (confirm('Are you sure you want to delete this company?')) {
        const companyId = button.getAttribute('data-company-id');

        fetch(`/api/v1/companies/${companyId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(async response => {
                const data = await response.json().catch(() => null);

                if (response.ok) {
                    button.closest('tr').remove();
                    showSuccessToast('Company deleted successfully');
                } else if (response.status === 409) {
                    showErrorToast('This company cannot be deleted because it has associated contacts or customers.');
                } else {
                    showErrorToast(data?.message || 'Error deleting company');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showErrorToast('An unexpected error occurred');
            });
    }
}
