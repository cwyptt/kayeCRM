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

document.addEventListener('DOMContentLoaded', function() {
    // Check for success message
    const successMessage = document.querySelector('.alert-success');
    if (successMessage) {
        showSuccessToast(successMessage.textContent.trim());
        successMessage.remove(); // Remove the alert after showing toast
    }

    // Check for error message
    const errorMessage = document.querySelector('.alert-danger');
    if (errorMessage) {
        showErrorToast(errorMessage.textContent.trim());
        errorMessage.remove(); // Remove the alert after showing toast
    }
});