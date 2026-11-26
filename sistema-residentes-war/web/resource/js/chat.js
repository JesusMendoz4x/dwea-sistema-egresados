window.Chat = (function () {
    var posicion = 0;
    var alFinal = true;
    function historial() { return document.getElementById('gestionChat:mensajes'); }
    function irAlFinal() {
        var panel = historial();
        if (panel) panel.scrollTop = panel.scrollHeight;
    }
    function recordarScroll() {
        var panel = historial();
        if (!panel) return;
        posicion = panel.scrollTop;
        alFinal = panel.scrollHeight - panel.scrollTop - panel.clientHeight < 64;
    }
    function restaurarScroll() {
        var panel = historial();
        if (panel) panel.scrollTop = alFinal ? panel.scrollHeight : posicion;
    }
    function teclado(evento) {
        if (evento.key === 'Enter' && (evento.ctrlKey || evento.metaKey) && !evento.isComposing) {
            evento.preventDefault();
            var boton = document.getElementById('gestionChat:enviar');
            if (boton && !boton.disabled) boton.click();
            return false;
        }
        return true;
    }
    if (document.readyState === 'loading') document.addEventListener('DOMContentLoaded', irAlFinal);
    else window.requestAnimationFrame(irAlFinal);
    return {irAlFinal: irAlFinal, recordarScroll: recordarScroll, restaurarScroll: restaurarScroll, teclado: teclado};
}());
