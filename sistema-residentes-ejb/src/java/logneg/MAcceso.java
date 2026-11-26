package logneg;

import dao.*;
import modelo.*;
import java.util.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.ejb.*;

@Stateless
@LocalBean
public class MAcceso {

    @EJB
    private CuentaAccesoFacade cuentas;

    public static String hash(String clave) {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return "120000$" + Base64.getEncoder().encodeToString(salt) + "$" + Base64.getEncoder().encodeToString(derivar(clave, salt, 120000));
    }

    private static byte[] derivar(String clave, byte[] salt, int pasos) {
        PBEKeySpec spec = new PBEKeySpec(clave.toCharArray(), salt, pasos, 256);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException(ex);
        } finally {
            spec.clearPassword();
        }
    }

    public static boolean verificar(String clave, String hash) {
        if (clave == null || hash == null) {
            return false;
        }
        try {
            String[] partes = hash.split("\\$");
            return MessageDigest.isEqual(Base64.getDecoder().decode(partes[2]), derivar(clave, Base64.getDecoder().decode(partes[1]), Integer.parseInt(partes[0])));
        } catch (RuntimeException ex) {
            return false;
        }
    }

    public Usuario autenticar(String correo, String clave) {
        for (CuentaAcceso c : cuentas.findAll()) {
            Usuario u = c.getUsuario();
            if (u.getCorreo().equalsIgnoreCase(correo == null ? "" : correo.trim()) && "Activo".equals(u.getEstado()) && verificar(clave, c.getHashClave())) {
                return u;
            }
        }
        return null;
    }

    public void guardarClave(Usuario usuario, String clave) {
        if (clave == null || clave.length() < 10) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 10 caracteres.");
        }
        for (CuentaAcceso c : cuentas.findAll()) {
            if (c.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
                c.setHashClave(hash(clave));
                cuentas.edit(c);
                return;
            }
        }
        CuentaAcceso c = new CuentaAcceso();
        c.setUsuario(usuario);
        c.setHashClave(hash(clave));
        cuentas.create(c);
    }
}
