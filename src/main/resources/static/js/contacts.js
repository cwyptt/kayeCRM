// resources/static/js/utils/contact.js
function deleteContact(button) {
    if (confirm('Are you sure you want to delete this contact?')) {
        const contactId = button.getAttribute('data-contact-id');

        fetch(`/api/v1/contacts/${contactId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    button.closest('tr').remove();
                    showSuccessToast('Contact deleted successfully');
                } else {
                    alert('Error deleting contact');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error deleting contact');
            });
    }
}