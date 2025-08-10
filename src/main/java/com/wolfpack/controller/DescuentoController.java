package com.wolfpack.controller;

import com.wolfpack.dto.DescuentoDTO;
import com.wolfpack.model.Descuento;
import com.wolfpack.service.IDescuentoService;
import com.wolfpack.util.OnCreate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/descuentos")
@RequiredArgsConstructor
public class DescuentoController {

    private final IDescuentoService service;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<Void> guardarDescuento(@Validated(OnCreate.class) @RequestBody DescuentoDTO dto) throws Exception{
        Descuento obj = service.guardar(convertToEntity(dto));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdDescuento()).toUri();

        return ResponseEntity.created(location).build();
    }


    private Descuento convertToEntity(DescuentoDTO dto){
        return modelMapper.map(dto, Descuento.class);
    }


}
