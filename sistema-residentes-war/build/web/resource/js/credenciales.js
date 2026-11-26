(function () {
    'use strict';
    const solicitudes = [
        {control: '20161024', nombre: 'Ana López Martínez', carrera: 'Ingeniería en Sistemas Computacionales', correo: 'ana.lopez@example.com', fecha: '17/11/2026', estado: 'Pendiente', motivo: ''},
        {control: '20161058', nombre: 'Carlos Hernández Ruiz', carrera: 'Ingeniería Industrial', correo: 'carlos.hernandez@example.com', fecha: '16/11/2026', estado: 'Pendiente', motivo: ''},
        {control: '19161107', nombre: 'María García Sánchez', carrera: 'Ingeniería Civil', correo: 'maria.garcia@example.com', fecha: '15/11/2026', estado: 'Aprobada', motivo: ''},
        {control: '19161032', nombre: 'Luis Pérez Díaz', carrera: 'Ingeniería en Sistemas Computacionales', correo: 'luis.perez@example.com', fecha: '14/11/2026', estado: 'Emitida', motivo: ''},
        {control: '18161076', nombre: 'Sofía Ramírez Cruz', carrera: 'Ingeniería Industrial', correo: 'sofia.ramirez@example.com', fecha: '13/11/2026', estado: 'Entregada', motivo: ''},
        {control: '20161120', nombre: 'Diego Torres Moreno', carrera: 'Ingeniería Civil', correo: 'diego.torres@example.com', fecha: '17/11/2026', estado: 'Requiere corrección', motivo: 'Actualizar la fotografía.'}
    ];
    const el = id => document.getElementById('cred-' + id);
    const normalizar = texto => texto.normalize('NFD').replace(/[\u0300-\u036f]/g, '').toLowerCase();
    let seleccion = null;
    function celda(fila, texto) {
        const td = document.createElement('td');
        td.textContent = texto;
        fila.appendChild(td);
        return td;
    }
    function dibujar() {
        const busqueda = normalizar(el('buscar').value.trim());
        const visibles = solicitudes.filter(s => normalizar(s.nombre + ' ' + s.control).includes(busqueda)
            && (!el('carrera').value || s.carrera === el('carrera').value)
            && (!el('estado').value || s.estado === el('estado').value));
        el('filas').replaceChildren();
        visibles.forEach(s => {
            const fila = document.createElement('tr');
            if (seleccion === s) fila.setAttribute('aria-selected', 'true');
            const nombre = celda(fila, s.nombre);
            const control = document.createElement('small');
            control.textContent = s.control;
            nombre.appendChild(control);
            celda(fila, s.carrera);
            celda(fila, s.fecha);
            const badge = document.createElement('span');
            badge.className = 'cred-badge';
            badge.dataset.estado = s.estado;
            badge.textContent = s.estado;
            celda(fila, '').appendChild(badge);
            const boton = document.createElement('button');
            boton.type = 'button';
            boton.textContent = 'Revisar';
            boton.setAttribute('aria-label', 'Revisar solicitud de ' + s.nombre);
            boton.addEventListener('click', () => {
                seleccion = s;
                el('mensaje').textContent = '';
                detalle();
                dibujar();
                el('detalle-titulo').tabIndex = -1;
                el('detalle-titulo').focus();
            });
            celda(fila, '').appendChild(boton);
            el('filas').appendChild(fila);
        });
        if (!visibles.length) {
            const fila = document.createElement('tr');
            celda(fila, 'No hay solicitudes con estos filtros. Prueba otra búsqueda o limpia los filtros.').colSpan = 5;
            el('filas').appendChild(fila);
        }
        el('total').textContent = visibles.length + ' de ' + solicitudes.length + ' solicitudes';
        el('pendientes').textContent = solicitudes.filter(s => s.estado === 'Pendiente').length;
        el('aprobadas').textContent = solicitudes.filter(s => s.estado === 'Aprobada').length;
        el('entregadas').textContent = solicitudes.filter(s => s.estado === 'Entregada').length;
    }
    function detalle() {
        el('detalle').hidden = false;
        el('ayuda').hidden = true;
        ['nombre', 'control', 'correo'].forEach(campo => { el(campo).textContent = seleccion[campo]; });
        el('avatar').textContent = seleccion.nombre.split(' ').slice(0, 2).map(n => n[0]).join('');
        el('det-carrera').textContent = seleccion.carrera;
        el('det-estado').textContent = seleccion.estado;
        el('motivo').value = seleccion.motivo;
        const revisable = seleccion.estado === 'Pendiente' || seleccion.estado === 'Requiere corrección';
        el('aprobar').disabled = !revisable;
        el('corregir').disabled = !revisable;
        el('motivo').disabled = !revisable;
        el('emitir').disabled = seleccion.estado !== 'Aprobada';
        el('entregar').disabled = seleccion.estado !== 'Emitida';
    }
    function cambiar(estado) {
        if (!seleccion) return;
        if (estado === 'Requiere corrección' && !el('motivo').value.trim()) {
            el('mensaje').textContent = 'Escribe el motivo de corrección antes de continuar.';
            el('motivo').focus();
            return;
        }
        seleccion.motivo = el('motivo').value.trim();
        seleccion.estado = estado;
        detalle();
        dibujar();
        el('mensaje').textContent = 'Solicitud de ' + seleccion.nombre + ': ' + estado + '. Cambio temporal de demostración.';
    }
    el('buscar').addEventListener('input', dibujar);
    ['carrera', 'estado'].forEach(id => el(id).addEventListener('change', dibujar));
    el('limpiar').addEventListener('click', () => {
        ['buscar', 'carrera', 'estado'].forEach(id => { el(id).value = ''; });
        dibujar();
    });
    [['aprobar', 'Aprobada'], ['corregir', 'Requiere corrección'], ['emitir', 'Emitida'], ['entregar', 'Entregada']]
        .forEach(([id, estado]) => el(id).addEventListener('click', () => cambiar(estado)));
    dibujar();
}());
