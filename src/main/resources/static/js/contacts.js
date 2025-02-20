function deleteContact(button) {
    if (confirm('Are you sure you want to delete this contact?')) {
        const contactId = button.getAttribute('data-contact-id');

        fetch(`/api/v1/contacts/${contactId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(async response => {
                const data = await response.json().catch(() => null);

                if (response.ok) {
                    button.closest('tr').remove();
                    showSuccessToast('Contact deleted successfully');
                } else if (response.status === 409) {
                    showErrorToast('This contact cannot be deleted because they are a customer. Please deactivate their customer relationship first.');
                } else {
                    showErrorToast(data?.message || 'Error deleting contact');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showErrorToast('An unexpected error occurred');
            });
    }
}