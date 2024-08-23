package co.edu.uniquindio.CRUD.servicios.implementaciones;

import co.edu.uniquindio.CRUD.documentos.Usuario;
import co.edu.uniquindio.CRUD.dtos.usuario.ItemUsuarioDTO;
import co.edu.uniquindio.CRUD.repositorios.UsuarioRepository;
import co.edu.uniquindio.CRUD.servicios.interfaces.GeneralService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GeneralServiceImpl implements GeneralService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public List<ItemUsuarioDTO> listarUsuarios() throws Exception{

        List<Usuario> usuarios = usuarioRepository.findAll();

        if (usuarios.isEmpty()) {
            throw new Exception("No hay usuarios");
        }

        List<ItemUsuarioDTO> usuariosDTO = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            usuariosDTO.add(new ItemUsuarioDTO(usuario.getCodigo(), usuario.getEmail(), usuario.getNombre(), usuario.getApellido()));
        }

        return usuariosDTO;

    }
}
