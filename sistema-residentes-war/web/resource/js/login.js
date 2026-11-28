(() => {
    const button = document.getElementById('ver-contrasena');
    if (!button) return;

    const password = document.getElementById(button.getAttribute('aria-controls'));

    if (!password) return;

    button.addEventListener('click', () => {
        const reveal = password.type === 'password';
        password.type = reveal ? 'text' : 'password';
        button.setAttribute('aria-label', reveal ? 'Ocultar contraseña' : 'Mostrar contraseña');
        button.classList.toggle('login-password-visible', reveal);
    });

    button.hidden = false;
})();
