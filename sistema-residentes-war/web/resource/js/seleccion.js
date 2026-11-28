(function () {
    function iniciar() {
        var enlace = document.querySelector('.sidebar-navigation a.opcion-actual, .sidebar-navigation .opcion-actual a');
        if (enlace) {
            enlace.setAttribute('aria-current', 'page');
            var titulo = enlace.closest('.ui-panelmenu-panel').querySelector('.ui-panelmenu-header');
            PF('menuPrincipal').expandRootSubmenu(jQuery(titulo), true);
            titulo.setAttribute('aria-expanded', 'true');
            titulo.classList.add('seccion-actual');
        }

        var contenido = document.querySelector('.page-content');
        var elegidas = new Map();
        function clave(fila) {
            return Array.from(fila.classList).find(function (c) { return /^registro-\d+$/.test(c); });
        }
        function preparar() {
            contenido.querySelectorAll('.ui-datatable-data > tr').forEach(function (fila) {
                var id = clave(fila);
                if (!id) return;
                fila.tabIndex = 0;
                var activa = elegidas.get(fila.closest('.ui-datatable').id) === id;
                fila.classList.toggle('fila-elegida', activa);
                fila.setAttribute('aria-selected', String(activa));
            });
        }
        function elegir(evento) {
            var fila = evento.target.closest('.ui-datatable-data > tr');
            if (!fila || !clave(fila)) return;
            if (evento.type === 'keydown') {
                if (evento.target !== fila || !['Enter', ' '].includes(evento.key)) return;
                evento.preventDefault();
            }
            elegidas.set(fila.closest('.ui-datatable').id, clave(fila));
            preparar();
        }
        contenido.addEventListener('click', elegir, true);
        contenido.addEventListener('keydown', elegir);
        preparar();
        if (contenido.querySelector('.ui-datatable')) {
            new MutationObserver(preparar).observe(contenido, {childList:true, subtree:true});
        }
    }
    jQuery(function () { window.setTimeout(iniciar, 0); });
}());
