package application;

import domain.exception.DuplicateEmailException;
import domain.model.Duenio;
import domain.port.in.DuenioService;
import domain.port.out.DuenioRepositoryPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DuenioServiceImpl implements DuenioService {
    @Autowired
    private final DuenioRepositoryPort duenioRepository;

    @Autowired
    private DuenioMapper duenioMapper;

    @Transactional
    public DuenioDTO registrarDuenio(DuenioDTO duenioDTO) {
        if(duenioRepository.existsByEmail(duenioDTO.getEmail())) {
            throw new DuplicateResourceException("El duenio ya existe");
        }
        Duenio duenio= duenioMapper.toEntity(duenioDTO);
        return duenioMapper.toDto(duenioRepository.save(duenio));
    }

    @Override
    public List<Duenio> listarTodos() {
        return duenioRepository.findAll();
    }

    @Override
    public Duenio registrar(Duenio duenio) {
        if(duenioRepository.existsByEmail(duenio.getEmail())) {
            throw new DuplicateEmailException(duenio.getEmail());
        }
        return duenioRepository.save(duenio);
    }

    @Override
    public Duenio actualizar(Long id, Duenio duenio) {
        Duenio duenioExistente= buscarPorId(id); //arrojar una excepcion sino existe
        duenioExistente.setNombre(duenio.getNombre());
        duenioExistente.setApellido(duenio.getApellido());
        duenioExistente.setTelefono(duenio.getTelefono());
        duenioExistente.setCedula(duenio.getCedula());
        return duenioRepository.save(duenioExistente);
    }

    @Override
    public void eliminarPorId(Long id) {
        buscarPorId(id);
        duenioRepository.deleteById(id);
    }

    @Transactional
    public Duenio buscarPorId(Long id) {
        Optional<Duenio> duenioBuscado= duenioRepository.findById(id);
        return duenioMapper.toDto(duenioBuscado.get());
    }
    public List<DuenioDTO> buscarTodos(){
        List<Duenio> listaDuenios= duenioRepository.findAll();
        List<DuenioDTO> listaDTO= new ArrayList<>();
        for (Duenio duenio : listaDuenios) {
            listaDTO.add(duenioMapper.toDto(duenio));
                    }
        return listaDTO;
    }

}
