package com.wolfpack.controller;

import com.wolfpack.dto.ViajeRequestDTO;
import com.wolfpack.dto.ViajeResponseDTO;
import com.wolfpack.model.Viaje;
import com.wolfpack.service.IViajeService;
import com.wolfpack.util.OnCreate;
import com.wolfpack.util.OnUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<List<ViajeResponseDTO>> buscarTodos() throws Exception{
        List<ViajeResponseDTO> list = service.buscarTodos().stream().map(this::convertToDtoResponse).toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{idViaje}")
    public ResponseEntity<ViajeResponseDTO> buscarPorId(@PathVariable Integer idViaje) throws Exception{
        ViajeResponseDTO dto =convertToDtoResponse( service.buscarPorId(idViaje));

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<Void> guardarViaje (@Validated(OnCreate.class) @RequestBody ViajeRequestDTO dto) throws Exception{
        Viaje obj = service.guardarViaje(convertToEntityRequest(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdViaje()).toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception{
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    private Viaje convertToEntityRequest(ViajeRequestDTO dto){
        return modelMapper.map(dto, Viaje.class);
    }

    private ViajeResponseDTO convertToDtoResponse(Viaje obj){
        return modelMapper.map(obj, ViajeResponseDTO.class);
    }



}
