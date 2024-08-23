package co.edu.uniquindio.CRUD.servicios.implementaciones;

import co.edu.uniquindio.CRUD.JWTUtils;
import co.edu.uniquindio.CRUD.documentos.Usuario;
import co.edu.uniquindio.CRUD.dtos.general.LoginDTO;
import co.edu.uniquindio.CRUD.dtos.autenticacionJwt.TokenDTO;
import co.edu.uniquindio.CRUD.repositorios.UsuarioRepository;
import co.edu.uniquindio.CRUD.servicios.interfaces.AutenticacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AutenticacionServiceImpl implements AutenticacionService {

    private final UsuarioRepository usuarioRepository;
    private final JWTUtils jwtUtils;

    @Override
    public TokenDTO login(LoginDTO loginDTO) throws Exception {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Object[] datos = buscarCorreo(loginDTO);

        if (!passwordEncoder.matches(loginDTO.password(), datos[3].toString())) {
            throw new Exception("La contraseña ingresada es incorrecta");
        }

        return new TokenDTO(crearToken(datos));
    }

    private String crearToken(Object[] datos) {

        Map<String, Object> map = new HashMap<>();
        map.put("nombre", datos[1]);
        map.put("apellido", datos[4]);
        map.put("id", datos[2]);

        return jwtUtils.generarToken(datos[0].toString(), map);
    }

    public Object[] buscarCorreo(LoginDTO loginDTO) throws Exception {

        String correo = "";
        String codigo = "";
        String nombre = "";
        String password = "";
        String apellido = "";

        Optional<Usuario> usuario = usuarioRepository.findByEmail(loginDTO.email());

        if (usuario.isEmpty()) {
            throw new Exception("El usuario no existe");
        }else if(usuario.get().isEstado()){

            correo = usuario.get().getEmail();
            nombre = usuario.get().getNombre();
            codigo = usuario.get().getCodigo();
            password = usuario.get().getPassword();
            apellido = usuario.get().getApellido();

        }else{
            throw new Exception("Estás inactivo");
        }

        return new Object[]{correo, nombre, codigo, password, apellido };
    }
}
