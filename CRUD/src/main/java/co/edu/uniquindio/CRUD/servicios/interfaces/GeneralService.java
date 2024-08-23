package co.edu.uniquindio.CRUD.servicios.interfaces;

import co.edu.uniquindio.CRUD.dtos.usuario.ItemUsuarioDTO;

import java.util.List;

public interface GeneralService {

    List<ItemUsuarioDTO> listarUsuarios() throws Exception;

}
