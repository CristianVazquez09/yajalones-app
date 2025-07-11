package com.wolfpack.controller;

import com.wolfpack.dto.ViajeDTO;
import com.wolfpack.model.Viaje;
import com.wolfpack.service.IViajeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/viajes")
@RequiredArgsConstructor
public class ViajeController {

    private final IViajeService service;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<ViajeDTO>> findAll() throws Exception{
        List<ViajeDTO> list = service.findAll().stream().map(this::convertToDto).toList();

        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody ViajeDTO dto) throws Exception{
        Viaje obj = service.save(convertToEntity(dto));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdViaje()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ViajeDTO> update(@Valid @PathVariable("id") Integer id, @RequestBody ViajeDTO dto) throws Exception{
        dto.setIdViaje(id);
        Viaje obj = service.update(id, convertToEntity(dto));

        return ResponseEntity.ok(convertToDto(obj));
    }

    private ViajeDTO convertToDto(Viaje obj){
        return modelMapper.map(obj, ViajeDTO.class);
    }

    private Viaje convertToEntity(ViajeDTO dto){
        return modelMapper.map(dto, Viaje.class);
    }


}
