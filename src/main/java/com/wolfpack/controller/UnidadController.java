package com.wolfpack.controller;

import com.wolfpack.dto.UnidadDTO;
import com.wolfpack.model.Unidad;
import com.wolfpack.service.IUnidadService;
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
@RequestMapping("/unidades")
@RequiredArgsConstructor
public class UnidadController {

    //@Autowired
    private final IUnidadService service;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<UnidadDTO>> buscarTodos() throws Exception{
        List<UnidadDTO> list = service.buscarTodos().stream().map(this::convertToDto).toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnidadDTO> buscarPorId(@PathVariable("id") Integer id) throws Exception {
        Unidad obj = service.buscarPorId(id);

        return ResponseEntity.ok(convertToDto(obj));
    }



    @PostMapping
    public ResponseEntity<Void> guardar(@Validated(OnCreate.class) @RequestBody UnidadDTO dto) throws Exception{
        Unidad obj = service.guardar(convertToEntity(dto));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdUnidad()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnidadDTO> actualizar(@Validated(OnUpdate.class) @PathVariable("id") Integer id, @RequestBody UnidadDTO dto) throws Exception{
        dto.setIdUnidad(id);
        Unidad obj = service.actualizar(id, convertToEntity(dto));

        return ResponseEntity.ok(convertToDto(obj));
    }

    private UnidadDTO convertToDto(Unidad obj){
        return modelMapper.map(obj, UnidadDTO.class);
    }

    private Unidad convertToEntity(UnidadDTO dto){
        return modelMapper.map(dto, Unidad.class);
    }


}
