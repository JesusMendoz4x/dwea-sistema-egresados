(function () {
    function prepararMenu() {
        var menu = PF('menuPrincipal');
        var navegacion = document.getElementById('sidebarNavigation');
        var lateral = document.getElementById('sidebar');
        var alternar = document.getElementById('menuToggle');
        var enlace = navegacion.querySelector('a.opcion-actual, .opcion-actual a');
        var tituloActual = enlace && enlace.closest('.ui-panelmenu-panel').querySelector('.ui-panelmenu-header');
        var claveEstado = navegacion.dataset.claveEstado;
        var estado = window.sessionStorage.getItem(claveEstado);
        var abiertas = estado === null ? null : estado.split(',');

        if (enlace) {
            enlace.setAttribute('aria-current', 'page');
        }

        // La cookie de PrimeFaces 5 usa la ruta de la vista actual.
        menu.saveState = function () {
            window.sessionStorage.setItem(claveEstado, this.expandedNodes.join(','));
        };
        menu.expandedNodes = [];
        menu.headers.each(function () {
            var titulo = jQuery(this);
            var panel = titulo.next();
            if (abiertas ? abiertas.includes(panel.attr('id')) : this === tituloActual) {
                menu.expandRootSubmenu(titulo, true);
                menu.expandedNodes.push(panel.attr('id'));
            }
            titulo.attr('aria-expanded', titulo.hasClass('ui-state-active'));
        });
        menu.saveState();

        menu.headers.on('click', function () {
            this.setAttribute('aria-expanded', this.classList.contains('ui-state-active'));
        });

        var claveVisibilidad = claveEstado + ':oculto';
        function mostrarPanel(visible) {
            lateral.classList.toggle('sidebar-oculta', !visible);
            lateral.inert = !visible;
            alternar.setAttribute('aria-expanded', String(visible));
            alternar.setAttribute('aria-label', visible ? 'Ocultar panel lateral' : 'Mostrar panel lateral');
        }

        mostrarPanel(window.sessionStorage.getItem(claveVisibilidad) !== 'true');
        alternar.addEventListener('click', function () {
            var visible = lateral.classList.contains('sidebar-oculta');
            lateral.classList.add('sidebar-animable');
            mostrarPanel(visible);
            window.sessionStorage.setItem(claveVisibilidad, String(!visible));
        });
        alternar.hidden = false;
    }

    function iniciar() {
        prepararMenu();

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
