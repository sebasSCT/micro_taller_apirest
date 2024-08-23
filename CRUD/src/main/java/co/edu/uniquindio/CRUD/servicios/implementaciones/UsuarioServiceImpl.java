package co.edu.uniquindio.CRUD.servicios.implementaciones;

import co.edu.uniquindio.CRUD.documentos.Usuario;
import co.edu.uniquindio.CRUD.dtos.general.CambioPasswordDTO;
import co.edu.uniquindio.CRUD.dtos.usuario.ActualizarUsuarioDTO;
import co.edu.uniquindio.CRUD.dtos.usuario.DetalleUsuarioDTO;
import co.edu.uniquindio.CRUD.dtos.usuario.RegistroUsuarioDTO;
import co.edu.uniquindio.CRUD.repositorios.UsuarioRepository;
import co.edu.uniquindio.CRUD.servicios.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public String registrarUsuario(RegistroUsuarioDTO usuario) throws Exception {

        if (existeEmail(usuario.email()))
            throw new Exception("El email ya existe");

        Usuario usuarioDB = new Usuario();

        usuarioDB.setNombre(usuario.nombre());
        usuarioDB.setApellido(usuario.apellido());
        usuarioDB.setEmail(usuario.email());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncriptada = passwordEncoder.encode( usuario.password() );

        usuarioDB.setPassword(passwordEncriptada);

        Usuario usuarioGuardado = usuarioRepository.save(usuarioDB);

        return usuarioGuardado.getCodigo();
    }

    private boolean existeEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }


    @Override
    public void actualizarUsuario(ActualizarUsuarioDTO usuario) throws Exception {

        Optional<Usuario> usuarioDB = usuarioRepository.findById(usuario.codigo());

        if (usuarioDB.isEmpty()) {
            throw new Exception("El usuario no existe");
        }

        Usuario usuarioActual = usuarioDB.get();
        usuarioActual.setNombre(usuario.nombre());
        usuarioActual.setApellido(usuario.apellido());
        usuarioActual.setEmail(usuario.email());

        usuarioRepository.save(usuarioActual);
    }

    @Override
    public DetalleUsuarioDTO obtenerUsuario(String idCuenta) throws Exception {

        Optional<Usuario> usuarioDB = usuarioRepository.findById(idCuenta);

        if (usuarioDB.isEmpty()) {
            throw new Exception("El usuario no existe");
        }

        Usuario usuarioActual = usuarioDB.get();

        return new DetalleUsuarioDTO(usuarioActual.getCodigo(), usuarioActual.getEmail(), usuarioActual.getNombre(), usuarioActual.getApellido());
    }

    @Override
    public void eliminarUsuario(String idCuenta) throws Exception {

        Optional<Usuario> usuarioDB = usuarioRepository.findById(idCuenta);

        if (usuarioDB.isEmpty()) {
            throw new Exception("El usuario no existe");
        }

        Usuario usuarioActual = usuarioDB.get();

        usuarioActual.setEstado(false);

        Usuario usuarioGuardado = usuarioRepository.save(usuarioActual);
    }

    @Override
    public void cambiarPassword(CambioPasswordDTO cambioPasswordDTO) throws Exception {

        Optional<Usuario> usuario = usuarioRepository.findByEmail(cambioPasswordDTO.email());

        if (usuario.isEmpty()) {
            throw new Exception("El usuario no existe");
        } else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String passwordEncriptada = passwordEncoder.encode(cambioPasswordDTO.nuevaPassword());

            usuario.get().setPassword(passwordEncriptada);
            usuarioRepository.save(usuario.get());
        }
    }
}
