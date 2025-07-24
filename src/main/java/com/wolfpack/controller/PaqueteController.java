package com.wolfpack.controller;

import com.wolfpack.dto.PaqueteRequestDTO;
import com.wolfpack.dto.PaqueteResponseDTO;
import com.wolfpack.dto.PasajeroRequestDTO;
import com.wolfpack.dto.PasajeroResponseDTO;
import com.wolfpack.model.Paquete;
import com.wolfpack.model.Pasajero;
import com.wolfpack.service.IPaqueteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/paquetes")
@RequiredArgsConstructor
public class PaqueteController {

    //@Autowired
    private final IPaqueteService service;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<PaqueteResponseDTO>> buscarTodos() throws Exception{
        List<PaqueteResponseDTO> list = service.buscarTodos().stream().map(this::convertToDtoResponse).toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaqueteResponseDTO> buscarPorId(@PathVariable("id") Integer id) throws Exception {
        Paquete obj = service.buscarPorId(id);

        return ResponseEntity.ok(convertToDtoResponse(obj));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaqueteResponseDTO> actualizar(@Valid @PathVariable("id") Integer id, @RequestBody PaqueteRequestDTO dto) throws Exception{
        dto.setIdPaquete(id);
        Paquete obj = service.actualizarPaquete(id, convertToEntityRequest(dto));

        return ResponseEntity.ok(convertToDtoResponse(obj));
    }

    @PostMapping
    public ResponseEntity<Void> guardar(@Valid @RequestBody PaqueteRequestDTO dto) throws Exception{
        Paquete obj = service.guardarPaquete(convertToEntityRequest(dto));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdPaquete()).toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception{
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private PaqueteResponseDTO convertToDtoResponse(Paquete obj){
        return modelMapper.map(obj, PaqueteResponseDTO.class);
    }

    private Paquete convertToEntityRequest(PaqueteRequestDTO dto){
        return modelMapper.map(dto, Paquete.class);
    }


}
