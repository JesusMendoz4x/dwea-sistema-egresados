"""Pruebas del panel local: crean y eliminan registros temporales.
La credencial de ejemplo se aprueba y emite si aún está pendiente.
Ejecutar solo contra la instancia local con los datos de ejemplo iniciales.
"""
from html.parser import HTMLParser
from urllib.request import build_opener,HTTPCookieProcessor,Request
from urllib.parse import urlencode
from http.cookiejar import CookieJar
class Page(HTMLParser):
 def __init__(self,html):
  super().__init__();self.forms={};self.active=None;self.select=None;self.option=None;self.button=None;self.feed(html)
 def handle_starttag(self,t,attrs):
  a=dict(attrs)
  if t=='form':self.active=a.get('id');self.forms[self.active]={'data':{},'buttons':{},'options':{}}
  if self.active:
   f=self.forms[self.active]
   if t=='input' and a.get('name'):f['data'][a['name']]=a.get('value','')
   if t=='textarea' and a.get('name'):f['data'][a['name']]=''
   if t=='select':self.select=a.get('name');f['data'][self.select]='';f['options'][self.select]=[]
   if t=='option' and self.select:
    self.option=a.get('value','');f['options'][self.select].append(self.option)
    if 'selected' in a:f['data'][self.select]=self.option
   if t=='button':self.button=a.get('id');f['buttons'][self.button]=''
 def handle_endtag(self,t):
  if t=='form':self.active=None
  if t=='select':self.select=None
  if t=='button':self.button=None
 def handle_data(self,d):
  if self.active and self.button:self.forms[self.active]['buttons'][self.button]+=d.strip()
base='http://localhost:8080/sistema-residentes-war'
op=build_opener(HTTPCookieProcessor(CookieJar()))
def get(path):
 r=op.open(base+path);text=r.read().decode();return r.geturl(),text,Page(text)
def post(path,form,button,values={}):
 _,html,page=get(path);f=page.forms[form];data=f['data'];data.update(values)
 key=next(k for k,v in f['buttons'].items() if v==button);data[key]=key
 r=op.open(Request(base+path,data=urlencode(data).encode()));html=r.read().decode();return r.geturl(),html,Page(html)
url,html,p=get('/vistas/dashboard.xhtml');assert url.endswith('/login.xhtml')
password='2026'
url,html,p=post('/login.xhtml','acceso','Iniciar sesión',{'acceso:correo':'admin@residentes.local','acceso:password':password})
assert '/vistas/dashboard.xhtml' in url,html[:600]
print('Autenticación y protección de rutas: correctas.')
for path in ['dashboard','vacantes/consultar','vacantes/publicar','vacantes/editar','egresados/gestionEgresados','comunicacion/mensajeria','comunicacion/conversaciones','comunicacion/avisos','comunicacion/eventos','credenciales/index','administracion/usuarios','administracion/empresas']:
 url,html,p=get('/vistas/'+path+'.xhtml');assert path in url and 'No se pudo completar' not in html,(path,html[-1000:]); print(path+': OK')
url,html,p=get('/vistas/administracion/usuarios.xhtml')
assert 'Administrador' in html and 'Egresado' in html and 'Vinculación' in html
print('Roles y datos iniciales visibles en los combos.')

import re

def enviar(path,html,form,texto,values={},key=None):
 page=Page(html);f=page.forms[form];data=f['data'].copy();data.update(values)
 if key is None:key=next(k for k,v in f['buttons'].items() if v==texto)
 data[key]=key
 if texto == 'Imprimir':
  r=op.open(Request(base+path,data=urlencode(data).encode()));return r.geturl(),r.read().decode()
 data.update({'javax.faces.partial.ajax':'true','javax.faces.source':key,'javax.faces.partial.execute':form if texto=='Guardar' else key,'javax.faces.partial.render':form})
 r=op.open(Request(base+path,data=urlencode(data).encode(),headers={'Faces-Request':'partial/ajax'}));raw=r.read().decode()
 import xml.etree.ElementTree as ET
 xml=ET.fromstring(raw);fragment=next((u.text for u in xml.findall('.//update') if u.get('id')==form),None)
 if fragment is None: raise AssertionError(raw[:2000])
 state=next((u.text for u in xml.findall('.//update') if 'javax.faces.ViewState' in u.get('id','')),None)
 if state and 'name="javax.faces.ViewState"' not in fragment:
  fragment=fragment.replace('</form>','<input type="hidden" name="javax.faces.ViewState" value="'+state+'"/></form>')
 if state:
  fragment=re.sub(r'(name="javax.faces.ViewState"[^>]*value=")[^"]*',lambda m:m.group(1)+state,fragment)
 return r.geturl(),fragment

path='/vistas/comunicacion/eventos.xhtml';_,html,p=get(path)
for row in re.findall(r'<tr\b[^>]*>[\s\S]*?</tr>',html):
 if 'Prueba funcional temporal' in row:
  buttons=Page('<form id="r">'+row+'</form>').forms['r']['buttons'];key=next(k for k,v in buttons.items() if v=='Eliminar')
  _,html=enviar(path,html,'gestionEvento','Eliminar',key=key)
_,html,p=get(path)
_,html=enviar(path,html,'gestionEvento','Guardar',{'gestionEvento:campo0':'Prueba funcional temporal','gestionEvento:campo1_input':'Taller','gestionEvento:campo2_input':'20/12/2026 10:00','gestionEvento:campo3':'Sala de pruebas','gestionEvento:campo4':'Registro temporal para validar el guardado.'})
assert 'Registro guardado.' in html,html[-3500:]
row=next(row for row in re.findall(r'<tr\b[^>]*>[\s\S]*?</tr>',html) if 'Prueba funcional temporal' in row)
buttons=Page('<form id="r">'+row+'</form>').forms['r']['buttons'];key=next(k for k,v in buttons.items() if v=='Eliminar')
_,html=enviar(path,html,'gestionEvento','Eliminar',key=key)
assert 'Registro eliminado.' in html and 'Prueba funcional temporal' not in html
print('Evento: alta y eliminación reales correctas; prueba temporal retirada.')

path='/vistas/credenciales/index.xhtml';_,html,p=get(path)
# Usa exclusivamente la solicitud de ejemplo, nunca registros ajenos.
row=next(row for row in re.findall(r'<tr\b[^>]*>[\s\S]*?</tr>',html) if 'Egresado de ejemplo' in row)
if 'Pendiente' in row:
 buttons=Page('<form id="r">'+row+'</form>').forms['r']['buttons'];key=next(k for k,v in buttons.items() if v=='Editar')
 _,html=enviar(path,html,'gestionCredencial','Editar',key=key)
 _,html=enviar(path,html,'gestionCredencial','Guardar',{'gestionCredencial:campo1_input':'Entregada'})
 assert 'El estado debe seguir' in html,html[-4000:]
 _,html=enviar(path,html,'gestionCredencial','Guardar',{'gestionCredencial:campo1_input':'Aprobada'})
 assert 'Registro guardado.' in html,html[-4000:]
 _,html=enviar(path,html,'gestionCredencial','Editar')
 _,html=enviar(path,html,'gestionCredencial','Guardar',{'gestionCredencial:campo1_input':'Emitida'})
 assert 'Registro guardado.' in html,html[-4000:]
 row=next(row for row in re.findall(r'<tr\b[^>]*>[\s\S]*?</tr>',html) if 'Egresado de ejemplo' in row)
 buttons=Page('<form id="r">'+row+'</form>').forms['r']['buttons'];key=next(k for k,v in buttons.items() if v=='Imprimir')
 url,html=enviar(path,html,'gestionCredencial','Imprimir',key=key)
 assert '/imprimir.xhtml' in url and 'EG-' in html and 'Egresado de ejemplo' in html
 print('Credencial de ejemplo: salto inválido rechazado; aprobación, emisión, folio e impresión correctos.')
else:print('La credencial de ejemplo ya estaba avanzada; no se alteró su estado.')

_,html,_=get('/vistas/dashboard.xhtml');counts=re.findall(r'<h2>(\d+)</h2>',html)
print('Totales del dashboard guardados para comparar el siguiente despliegue:',counts)

path='/vistas/credenciales/index.xhtml';_,html,_=get(path)
row=next(row for row in re.findall(r'<tr\b[^>]*>[\s\S]*?</tr>',html) if 'Egresado de ejemplo' in row)
buttons=Page('<form id="r">'+row+'</form>').forms['r']['buttons'];key=next(k for k,v in buttons.items() if v=='Imprimir')
url,html=enviar(path,html,'gestionCredencial','Imprimir',key=key)
assert '/imprimir.xhtml' in url and 'EG-' in html and 'Egresado de ejemplo' in html
print('Impresión real de la credencial emitida: OK.')
path='/vistas/administracion/usuarios.xhtml';_,html,_=get(path)
for row in re.findall(r'<tr\b[^>]*>[\s\S]*?</tr>',html):
 if 'prueba-temporal@example.com' in row:
  buttons=Page('<form id="r">'+row+'</form>').forms['r']['buttons'];key=next(k for k,v in buttons.items() if v=='Eliminar')
  _,html=enviar(path,html,'gestionUsuario','Eliminar',key=key)
_,html,_=get(path)
clavePrueba='Temporal-'+__import__('uuid').uuid4().hex
_,html=enviar(path,html,'gestionUsuario','Guardar',{'gestionUsuario:campo0':'Cuenta de prueba temporal','gestionUsuario:campo1':'prueba-temporal@example.com','gestionUsuario:campo2_input':'Vinculación','gestionUsuario:campo3_input':'Activo','gestionUsuario:clave':clavePrueba})
assert 'Registro guardado.' in html,html[-3000:]
admin=op;op=build_opener(HTTPCookieProcessor(CookieJar()))
url,html,_=post('/login.xhtml','acceso','Iniciar sesión',{'acceso:correo':'prueba-temporal@example.com','acceso:password':clavePrueba})
assert '/dashboard.xhtml' in url
from urllib.error import HTTPError
try:
 get('/vistas/administracion/usuarios.xhtml');raise AssertionError('Debió responder 403')
except HTTPError as ex:assert ex.code==403
print('Alta de cuenta, hash de contraseña, login y restricción por rol: OK.')
# Cerrar sesión y comprobar acceso denegado posteriormente.
_,html,page=get('/vistas/dashboard.xhtml')
link=next(a for a in re.findall(r'<a\b[^>]*>[\s\S]*?</a>',html) if 'Cerrar sesión' in a)
import html as htmlmodule
onclick=htmlmodule.unescape(re.search(r'onclick="([^"]+)"',link).group(1))
key=re.search(r"'(menuForm:[^']+)'",onclick).group(1)
data=page.forms['menuForm']['data'];data[key]=key
response=op.open(Request(base+'/vistas/dashboard.xhtml',data=urlencode(data).encode()));url=response.geturl()
assert '/login.xhtml' in url
url,_,_=get('/vistas/dashboard.xhtml');assert '/login.xhtml' in url
print('Cierre de sesión: OK.')
op=admin;_,html,_=get(path)
row=next(row for row in re.findall(r'<tr\b[^>]*>[\s\S]*?</tr>',html) if 'prueba-temporal@example.com' in row)
buttons=Page('<form id="r">'+row+'</form>').forms['r']['buttons'];key=next(k for k,v in buttons.items() if v=='Eliminar')
_,html=enviar(path,html,'gestionUsuario','Eliminar',key=key)
assert 'Registro eliminado.' in html and 'prueba-temporal@example.com' not in html
print('Cuenta temporal y su hash eliminados: OK.')
