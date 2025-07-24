package com.wolfpack.controller;

import com.wolfpack.dto.PasajeroRequestDTO;
import com.wolfpack.dto.PasajeroResponseDTO;
import com.wolfpack.model.Pasajero;
import com.wolfpack.service.IPasajeroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/pasajeros")
@RequiredArgsConstructor
public class PasajeroController {

    private final IPasajeroService service;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<PasajeroResponseDTO>> buscarTodos() throws Exception{
        List<PasajeroResponseDTO> list = service.buscarTodos().stream().map(this::convertToDtoResponse).toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PasajeroResponseDTO> buscarPorId(@PathVariable("id") Integer id) throws Exception {
        Pasajero obj = service.buscarPorId(id);

        return ResponseEntity.ok(convertToDtoResponse(obj));
    }

    @PostMapping
    public ResponseEntity<Void> guardar(@Valid @RequestBody PasajeroRequestDTO dto) throws Exception{
        Pasajero obj = service.guardarPasajero(convertToEntityRequest(dto));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdPasajero()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PasajeroResponseDTO> actualizar(@Valid @PathVariable("id") Integer id, @RequestBody PasajeroRequestDTO dto) throws Exception{
        dto.setIdPasajero(id);
        Pasajero obj = service.actualizarPasajero(id, convertToEntityRequest(dto));

        return ResponseEntity.ok(convertToDtoResponse(obj));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception{
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private PasajeroResponseDTO convertToDtoResponse(Pasajero obj){
        return modelMapper.map(obj, PasajeroResponseDTO.class);
    }

    private Pasajero convertToEntityRequest(PasajeroRequestDTO dto){
        return modelMapper.map(dto, Pasajero.class);
    }


}
