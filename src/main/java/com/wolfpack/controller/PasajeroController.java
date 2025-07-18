package com.wolfpack.controller;

import com.wolfpack.dto.ChoferDTO;
import com.wolfpack.dto.PasajeroDTO;
import com.wolfpack.model.Chofer;
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
    public ResponseEntity<List<PasajeroDTO>> buscarTodos() throws Exception{
        List<PasajeroDTO> list = service.buscarTodos().stream().map(this::convertToDto).toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PasajeroDTO> buscarPorId(@PathVariable("id") Integer id) throws Exception {
        Pasajero obj = service.buscarPorId(id);

        return ResponseEntity.ok(convertToDto(obj));
    }

    @PostMapping
    public ResponseEntity<Void> guardar(@Valid @RequestBody PasajeroDTO dto) throws Exception{
        Pasajero obj = service.guardarPasajero(convertToEntity(dto));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdPasajero()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PasajeroDTO> actualizar(@Valid @PathVariable("id") Integer id, @RequestBody PasajeroDTO dto) throws Exception{
        dto.setIdPasajero(id);
        Pasajero obj = service.actualizar(id, convertToEntity(dto));

        return ResponseEntity.ok(convertToDto(obj));
    }

    private PasajeroDTO convertToDto(Pasajero obj){
        return modelMapper.map(obj, PasajeroDTO.class);
    }

    private Pasajero convertToEntity(PasajeroDTO dto){
        return modelMapper.map(dto, Pasajero.class);
    }


}
