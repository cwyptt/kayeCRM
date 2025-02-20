document.addEventListener('DOMContentLoaded', function() {
    // Init collapse state from localStorage
    const sidebarSections = document.querySelectorAll('[data-section]');

    sidebarSections.forEach(section => {
        const sectionName = section.dataset.section;
        const collapseElement = document.getElementById(sectionName + 'Collapse');
        const storedState = localStorage.getItem('sidebar-' + sectionName);

        if (storedState === 'open') {
            collapseElement.classList.add('show');
            section.setAttribute('aria-expanded', 'true');
        }

        // Event listener to save state changes
        collapseElement.addEventListener('shown.bs.collapse', () => {
            localStorage.setItem('sidebar-' + sectionName, 'open');
        });

        collapseElement.addEventListener('hidden.bs.collapse', () => {
            localStorage.setItem('sidebar-' + sectionName, 'closed');
        })
    })
})